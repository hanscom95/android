/**
 * Disclaimer: IMPORTANT:  This Nulana software is supplied to you by Nulana
 * LTD ("Nulana") in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Nulana software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Nulana software.
 *
 * In consideration of your agreement to abide by the following terms, and
 * subject to these terms, Nulana grants you a personal, non-exclusive
 * license, under Nulana's copyrights in this original Nulana software (the
 * "Nulana Software"), to use, reproduce, modify and redistribute the Nulana
 * Software, with or without modifications, in source and/or binary forms;
 * provided that if you redistribute the Nulana Software in its entirety and
 * without modifications, you must retain this notice and the following
 * text and disclaimers in all such redistributions of the Nulana Software.
 * Except as expressly stated in this notice, no other rights or licenses, 
 * express or implied, are granted by Nulana herein, including but not limited 
 * to any patent rights that may be infringed by your derivative works or by other
 * works in which the Nulana Software may be incorporated.
 *
 * The Nulana Software is provided by Nulana on an "AS IS" basis.  NULANA
 * MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE, REGARDING THE NULANA SOFTWARE OR ITS USE AND
 * OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * IN NO EVENT SHALL NULANA BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 * MODIFICATION AND/OR DISTRIBUTION OF THE NULANA SOFTWARE, HOWEVER CAUSED
 * AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 * STRICT LIABILITY OR OTHERWISE, EVEN IF NULANA HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) 2016 Nulana LTD. All Rights Reserved.
 */
 
package com.se.gait.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.renderscript.Float4;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UninitializedMessageException;
import com.nulana.NChart.*;
import com.se.gait.ble.BluetoothLeService;
import com.se.gait.utility.LogFile;
import com.se.gait.R;
import com.se.gait.utility.Serf;
import com.se.gait.utility.StnEggPkt;
import com.se.gait.utility.ULog;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import static com.se.gait.ble.BluetoothLeService.*;

@ReportsCrashes(formUri = "", // will not be used
        mailTo = "taehoon@standing-egg.co.kr", customReportContent = { ReportField.APP_VERSION_CODE,
        ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
        ReportField.STACK_TRACE,
        ReportField.LOGCAT }, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)

public class ChartingDemoActivity extends AppCompatActivity implements NChartSeriesDataSource, NChartValueAxisDataSource {
    private static final String TAG = "ChartingDemoActivity";

    Button mSampleButton, mWifiButton, mClearButton, mViewButton;
    TextView mStepText, mDistanceText, mXText, mYText, mZText, mErrorXText, mErrorYText, mErrorZText, mMotionText, mErrorPercentageText;
    RadioGroup mChartSizeRadioGroup;
    ScrollView mScrollView;
    LinearLayout mScrollImg;
    NChartView mNChartView;
    NChartLineSeries mSeries;
    public static ArrayList<Float4> chartPointArray;
    private ArrayList<ArrayList<String>> logFileArray = new ArrayList<ArrayList<String>>();
    private ArrayList<String> childLogFileArray = new ArrayList<String>();
    final long tickMS = 10;
    int m_count = 0;
    int chartSize = 1;
    int chartView = 0;
    boolean wifiBoolean = false;
    boolean sampleBoolean = false;
    boolean logBoolean = false;

    private Context mContext;
    public String deviceAddress;
    public static boolean devCheck = false;

    private Serf serf;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        mContext = this;

        mNChartView = (NChartView) findViewById(R.id.surface);
        mSampleButton = (Button) findViewById(R.id.sampleButton);
        mWifiButton = (Button) findViewById(R.id.wifiButton);
        mClearButton = (Button) findViewById(R.id.clearButton);
        mViewButton = (Button) findViewById(R.id.viewButton);
        mStepText = (TextView) findViewById(R.id.stepCountText);
        mDistanceText = (TextView) findViewById(R.id.distanceText);
        mXText = (TextView) findViewById(R.id.xText);
        mYText = (TextView) findViewById(R.id.yText);
        mZText = (TextView) findViewById(R.id.zText);
        mErrorXText = (TextView) findViewById(R.id.errorXText);
        mErrorYText = (TextView) findViewById(R.id.errorYText);
        mErrorZText = (TextView) findViewById(R.id.errorZText);
        mMotionText = (TextView) findViewById(R.id.motionText);
        mErrorPercentageText = (TextView) findViewById(R.id.errorPercentageText);
        mChartSizeRadioGroup = (RadioGroup) findViewById(R.id.chartSizeRadioGroup);
        mChartSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.chartSizeRadio1:
                        chartSize = 1;
//                        if(!wifiBoolean){
                        mClearButton.performClick();
//                            mNChartView.getChart().updateData();
//                        }
                        break;
                    case R.id.chartSizeRadio1_1:
                        chartSize = 7;//20m
//                        if(!wifiBoolean){
                        mClearButton.performClick();
//                            mNChartView.getChart().updateData();
//                        }
                        break;
                    case R.id.chartSizeRadio2:
                        chartSize = 2;
//                        if(!wifiBoolean){
                        mClearButton.performClick();
//                            mNChartView.getChart().updateData();
//                        }
                        break;
                    case R.id.chartSizeRadio3:
                        chartSize = 3;
//                        if(!wifiBoolean){
                        mClearButton.performClick();
//                            mNChartView.getChart().updateData();
//                        }
                        break;
                    case R.id.chartSizeRadio4:
                        chartSize = 4;
//                        if(!wifiBoolean){
                        mClearButton.performClick();
//                            mNChartView.getChart().updateData();
//                        }
                        break;
                    case R.id.chartSizeRadio5:
                        chartSize = 5;
//                        if(!wifiBoolean){
                        mClearButton.performClick();
//                            mNChartView.getChart().updateData();
//                        }
                        break;
                    case R.id.chartSizeRadio6:
                        chartSize = 6;
//                        if(!wifiBoolean){
                        mClearButton.performClick();
//                            mNChartView.getChart().updateData();
//                        }
                        break;
                }
            }
        });

        chartPointArray = new ArrayList<Float4>();
        mScrollImg = (LinearLayout) findViewById(R.id.scrollImg);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = mScrollView.getChildAt(mScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView.getScrollY()));
                if (diff == 0) {
                    mScrollImg.setVisibility(View.INVISIBLE);
                } else {
                    mScrollImg.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });

        mWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mWifiButton", "setOnClickListener : " + wifiBoolean);
                wifi_push();
            }
        });

        mSampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mSampleButton", "setOnClickListener : " + sampleBoolean);
                if (!sampleBoolean) {
                    if (true) {
                        String path = "/sdcard/nChart3D/";
                        String fileName = "log.txt";

                        File file = new File(path, fileName);

                        try {
                            FileOutputStream fos = new FileOutputStream("chartLog.txt");
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(chartPointArray); // write MenuArray to ObjectOutputStream
                            oos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return;
                    }

                    sampleBoolean = true;


                    if (m_count == 0) {
                        mNChartView.getChart().beginTransaction();
                        //mNChartView.getChart().updateData();

                        // Update data in the points.
                        NChartPoint[] points = mNChartView.getChart().getSeries()[0].getPoints();

                        Log.d("mSampleButton", "chart cnt: " + points.length);
                        for (NChartPoint point : points) {
                            point.getCurrentState().setDoubleX(0);
                            point.getCurrentState().setDoubleY(0);
                            point.getCurrentState().setDoubleZ(0);
                        }
                        Log.d("mSampleButton", "chart point: " + mNChartView.getChart().getSeries()[0].getPoints().length + "//// getValue : " + mNChartView.getChart().getSeries()[0].getPoints()[0].getCurrentState().getValue()
                                + "//// getX : " + mNChartView.getChart().getSeries()[0].getPoints()[0].getCurrentState().getDoubleX() + "//// getY : " + mNChartView.getChart().getSeries()[0].getPoints()[0].getCurrentState().getDoubleY()
                                + "//// getZ : " + mNChartView.getChart().getSeries()[0].getPoints()[0].getCurrentState().getDoubleZ());
                        // Update data in the chart.
                        mNChartView.getChart().streamData();
                        // End the data changing session from-within separate thread.
                        mNChartView.getChart().endTransaction();
                    }

                    // Start the separated thread that will update data in realtime.
                    // It is recommended to stream data in the separated thread to avoid lags of the UI.
                    thread.start();
                } else {
                    sampleBoolean = false;
                }
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mNChartView.getChart().updateData();
//                if(wifiBoolean) {
//                    wifi_push();
//                }
                if (wifiBoolean) {
                    m_count = 0;
                    chartPointArray = new ArrayList<Float4>();
//                mNChartView.getChart().updateData();
                    mNChartView.getChart().resetTransition();
//                mNChartView.getChart().rebuildSeries();
//                wifi_push();
                } else {
                    m_count = 0;
                    chartPointArray = new ArrayList<Float4>();
                    mNChartView.getChart().updateData();
                }

                //Log.d("mClearButton", "getXAxis getMarks:  " + mNChartView.getChart().getCartesianSystem().getXAxis().getMarks().length + "getValue:  " + mNChartView.getChart().getCartesianSystem().getXAxis().getMarks()[0].getValue());
                //Log.d("mClearButton", "getYAxis getMarks:  " + mNChartView.getChart().getCartesianSystem().getYAxis().getMarks().length + "getValue:  " + mNChartView.getChart().getCartesianSystem().getYAxis().getMarks()[0].getValue());
                //Log.d("mClearButton", "getZAxis getMarssks:  " + mNChartView.getChart()-.getCartesianSystem().getZAxis().getMarks().length + "getValue:  " + mNChartView.getChart().getCartesianSystem().getZAxis().getMarks()[0].getValue());


                //mNChartView.getChart().removeAllSeries();
                //mNChartView.getChart().addSeries(mSeries);
                //mNChartView.getChart().updateData();
            }
        });

        mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chartView == 0) {
                    mNChartView.getChart().setXAngle((float)-1.57);
                    mNChartView.getChart().setYAngle((float)3.1209900);
                    chartView++;
                }else if(chartView == 1) {
                    mNChartView.getChart().setXAngle((float)-3900635);
                    mNChartView.getChart().setYAngle((float)4.703318);
                    chartView++;
                }else if(chartView == 2) {
                    mNChartView.getChart().setXAngle(0);
                    mNChartView.getChart().setYAngle(0);
                    chartView++;
                }else if(chartView == 3) {
                    mNChartView.getChart().setXAngle(mNChartView.getChart().getInitialXAngle());
                    mNChartView.getChart().setYAngle(mNChartView.getChart().getInitialYAngle());
                    chartView = 0;
                }
                mNChartView.getChart().setZoom(1);
            }
        });


        serf = new Serf();

        loadView();


        //logfile set
        childLogFileArray.add("Timestamp");
        childLogFileArray.add("ACCX");
        childLogFileArray.add("ACCY");
        childLogFileArray.add("ACCZ");
        childLogFileArray.add("GYROX");
        childLogFileArray.add("GYROY");
        childLogFileArray.add("GYROZ");
        childLogFileArray.add("MAGX");
        childLogFileArray.add("MAGY");
        childLogFileArray.add("MAGZ");
        childLogFileArray.add("zupt");
        childLogFileArray.add("GaitX");
        childLogFileArray.add("GaitY");
        childLogFileArray.add("GaitZ");
        childLogFileArray.add("quaternion1");
        childLogFileArray.add("quaternion2");
        childLogFileArray.add("quaternion3");
        childLogFileArray.add("quaternion4");
        logFileArray.add(childLogFileArray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!navigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        //return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("onOptionsItemSelected", "id: " + item.getTitle().toString());
        Intent intent;
        switch (item.getItemId()) {
            case R.id.bleSetup:


                if (false) {

                    //Create sequence of items
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle("BLE Setup");
                    dialogBuilder.setAdapter(null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Get the device MAC address, which is the last 17 chars in the
                            // View
                            /*String info = mNewDevicesListAdapter.getItem(which).toString();
                            Log.d("ble ===== ", " info ================ : " + info);
                            if (info != null && info.length() > 16) {
                                deviceAddress = mDevices.get(which).getAddress().toString();
                                mBtAdapter.stopLeScan(mLeScanCallback);
                                BTCTemplateService.ble_flag = false;
                                BTCTemplateService.mBleManager = null;
                                unbindService(mServiceConn);
                            }*/
                        }
                    });
                    //Create alert dialog object via builder
                    AlertDialog alertDialogObject = dialogBuilder.create();
                    //Show the dialog
                    alertDialogObject.show();
                }



                /*if (!mBtAdapter.isEnabled()) {
                    if (!mBtAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }*/




                if(true) {
                    mHandler = new Handler();

                    // Use this check to determine whether BLE is supported on the device.  Then you can
                    // selectively disable BLE-related features.
                    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                        Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
                    // BluetoothAdapter through BluetoothManager.
                    final BluetoothManager bluetoothManager =
                            (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                    mBluetoothAdapter = bluetoothManager.getAdapter();

                    // Checks if Bluetooth is supported on the device.
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }

                    mLeDeviceListAdapter = new LeDeviceListAdapter();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle("BLE Setup");
                    dialogBuilder.setAdapter(mLeDeviceListAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("onclick", "which : " + which);
                            mHandler.removeMessages(0);
                            // Get the device MAC address, which is the last 17 chars in the
                            // View
                            /*String info = mNewDevicesListAdapter.getItem(which).toString();
                            Log.d("ble ===== ", " info ================ : " + info);
//                            if (info != null && info.length() > 16) {
                                deviceAddress = mDevices.get(which).getAddress().toString();
                                mBtAdapter.stopLeScan(mLeScanCallback);
                                BTCTemplateService.ble_flag = false;
                                BTCTemplateService.mBleManager = null;
                                unbindService(mServiceConn);
                            }*/


                            final BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
                            if (device == null) return;
                            Log.d("onclick", "mScanning " );
                            if (mScanning) {
                                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                                mScanning = false;
                            }

                            mDeviceAddress = device.getAddress();
                            mDeviceName = device.getName();
                            bleDeviceControl(mDeviceAddress);
                        }
                    });
                    //Create alert dialog object via builder
                    AlertDialog alertDialogObject = dialogBuilder.create();
                    //Show the dialog
                    alertDialogObject.show();
                    scanLeDevice(true);
                }

                break;

            case R.id.logData:
                if (!logBoolean) {
                    item.setTitle("LogStop");
                    logBoolean = true;
                } else {
                    item.setTitle("LogStart");
                    logBoolean = false;
                    LogFile.main(logFileArray);
                }
                break;

            case R.id.XYChart:
                intent = new Intent(ChartingDemoActivity.this, ChartLineDemoActivity.class);
                startActivity(intent);
                break;

            case R.id.ZChart:
                intent = new Intent(ChartingDemoActivity.this, ChartAreaDemoActivity.class);
                startActivity(intent);
                break;

            case R.id.ZUPTChart:
                intent = new Intent(ChartingDemoActivity.this, ChartZUPTDemoActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    volatile boolean treadIsStopped = false;
    Thread thread = new Thread(new Runnable() {
        public void run() {
            do {
                // Begin the data changing session from-within separated thread.
                // Ensure thread-safe changes in the chart by wrapping the updating routine with beginTransaction and
                // endTransaction calls.
                mNChartView.getChart().beginTransaction();

                // Force chart to extend data.
                mNChartView.getChart().extendData();

                // End the data changing session from-within separate thread.
                mNChartView.getChart().endTransaction();
                try {
                    Thread.sleep(tickMS);
                } catch (InterruptedException e) {
                    return;
                }

                if (ChartingDemoActivity.this.isFinishing())
                    return;
                if (treadIsStopped)
                    return;
            } while (true);
        }
    }, "dataUpdater");

    private void wifi_push() {
        if (!wifiBoolean) {
            wifiBoolean = true;
            exeFlag01 = false;
            run();
            if (wifiBoolean) {
                mWifiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_disconnect_wifi));
                //set default to false
                for (int i = 0; i < mChartSizeRadioGroup.getChildCount(); i++) {
                    ((RadioButton) mChartSizeRadioGroup.getChildAt(i)).setEnabled(false);
                }
            } else {
                mWifiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_connect_wifi));
            }

        } else {
            mWifiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_connect_wifi));
            wifiBoolean = false;
            if (bThreadSwitch01 == true) {
                bThreadSwitch01 = false;
                cThreadSwitch01 = false;

                NetworkThread02.cancel(true);
            }
            //set default to false
            for (int i = 0; i < mChartSizeRadioGroup.getChildCount(); i++) {
                ((RadioButton) mChartSizeRadioGroup.getChildAt(i)).setEnabled(true);
            }
        }
    }


    BufferedReader in;

    private void loadView() {
        // Paste your license key here.
        mNChartView.getChart().setLicenseKey("");

        mNChartView.getChart().getCartesianSystem().setMargin(new NChartMargin(30.0f, 30.0f, 10.0f, 20.0f));

        // Create series that will be displayed on the chart.
        mSeries = new NChartLineSeries();
        mSeries.setBrush(new NChartSolidColorBrush(Color.BLUE));
        mSeries.setBorderThickness(1.0f);
        mSeries.setDataSource(this);
        mNChartView.getChart().addSeries(mSeries);

        mNChartView.getChart().getCartesianSystem().getXAxis().setDataSource(this);
        mNChartView.getChart().getCartesianSystem().getYAxis().setDataSource(this);
        mNChartView.getChart().getCartesianSystem().getZAxis().setDataSource(this);
        mNChartView.getChart().setBackground(new NChartTextureBrush(
                BitmapFactory.decodeResource(getResources(), R.drawable.background2),
                Color.TRANSPARENT, NChartTexturePosition.ScaleKeepMaxAspect));


//        mNChartView.getChart().setShouldAutoScroll(true);
//        mNChartView.getChart().getCartesianSystem().getXAxis().calcOptimalMinTickSpacing();
//        mNChartView.getChart().getCartesianSystem().getYAxis().calcOptimalMinTickSpacing();
//        mNChartView.getChart().getCartesianSystem().getZAxis().calcOptimalMinTickSpacing();
//        mNChartView.getChart().getCartesianSystem().getXAxis().setShouldBeautifyMinAndMax(true);
//        mNChartView.getChart().getCartesianSystem().getYAxis().setShouldBeautifyMinAndMax(true);
//        mNChartView.getChart().getCartesianSystem().getZAxis().setShouldBeautifyMinAndMax(true);

        // Draw in 3D mode
        mNChartView.getChart().setDrawIn3D(true);

        // Activate a streaming mode to best performance.
        mNChartView.getChart().setStreamingMode(true);

        // Increase points history length. Please, don't forget about memory using.
        // Device can crash with out of memory if value is too big.
        //mNChartView.getChart().setPointsHistoryLength(24000);
        mNChartView.getChart().setPointsHistoryLength(50000);

        mNChartView.getChart().getCartesianSystem();
        // Enable auto-zoom of the Y-Axis.
        mNChartView.getChart().getCartesianSystem().setShouldAutoZoom(true);
        mNChartView.getChart().getCartesianSystem().setAutoZoomAxes(NChartAutoZoomAxes.NormalAxis); // This can also be NChartAutoZoomAxes.SecondaryAxis, in case series are hosted on the secondary axis.

        // Prepare data to read.
        try {
            InputStream is = getAssets().open("test.CSV");
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            in.readLine(); // First read to skip title
        } catch (Exception e) {
            Log.d("InputStream error", e.getLocalizedMessage());
            e.printStackTrace();
        }

        // Initialize the data in the chart. Note that this call is mandatory in the beginning of AutoScroll.
        mNChartView.getChart().updateData();
    }

    protected void onResume() {
        super.onResume();
        mNChartView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mNChartView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

        if(mBluetoothLeService != null && mServiceConnection !=null) {
            if(mGattUpdateReceiver != null) {
                unregisterReceiver(mGattUpdateReceiver);
            }

            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
            unbindService(mServiceConnection);
//            mBluetoothLeService.unbindService(mServiceConnection);
            mBluetoothLeService = null;
        }
    }


    NChartPointState nextState() {
        String str;
        try {
            if (sampleBoolean) {
                str = in.readLine();
                if (str == null) {
                    in.close();
                    treadIsStopped = true;
                } else {
                    String[] arr = str.split(",");
                    gaitX = Float.parseFloat(arr[0]) * 1000;
                    gaitY = Float.parseFloat(arr[1]) * 1000;
                    gaitZ = Float.parseFloat(arr[2]) * 1000;
                    Float4 gaitXYZ = new Float4();
                    gaitXYZ.x = Float.parseFloat(arr[0]) * 1000;
                    gaitXYZ.y = Float.parseFloat(arr[1]) * 1000;
                    gaitXYZ.z = Float.parseFloat(arr[2]) * 1000;
                    gaitXYZ.w = 0;
                    chartPointArray.add(gaitXYZ);
                }
            }
        } catch (Exception e) {
            Log.d("ReadLine error", e.getLocalizedMessage());
            e.printStackTrace();
        }
        //Log.d("nextState", "extraPoints : " +  m_count + "//// x : " + x + "//// y : " + y + "//// z : " + z);
        NChartPointState state = NChartPointState.PointStateWithXYZ(gaitX, -gaitZ, gaitY);
        return state;
    }

    public NChartPoint[] points(NChartSeries series) {
        NChartPoint[] result = new NChartPoint[1];
        result[0] = new NChartPoint(NChartPointState.PointStateWithXYZ(50, 50, 50), series);
        return result;
    }

    public String name(NChartSeries series) {
        return "Distance (M)";
    }

    public Bitmap image(NChartSeries series) {
        return null;
    }

    public NChartPoint[] extraPoints(NChartSeries series) {
        if (mNChartView.getChart().getPointsHistoryLength() < m_count) {
            return null;
        }

        NChartPoint[] result = new NChartPoint[1];
        result[0] = new NChartPoint(nextState(), series);
        ++m_count;
        return result;

        /*if(sampleBoolean) {
            // Create extra points.
            NChartPoint[] result = new NChartPoint[1];
            result[0] = new NChartPoint(nextState(), series);
            ++m_count;
            return result;
        }else if(wifiBoolean) {
            // Create extra points.
            NChartPoint[] result = new NChartPoint[1];
            result[0] = new NChartPoint(nextState(), series);
            ++m_count;
            return result;
        }else {
            return null;
        }*/
    }

    @Override
    public String name(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Number min(NChartValueAxis nChartValueAxis) {
        int size = 0;
        if (chartSize == 0) {
            return null;
        } else if (chartSize == 1) {
            size = -10;
        } else if (chartSize == 2) {
            size = -50;
        } else if (chartSize == 3) {
            size = -100;
        } else if (chartSize == 4) {
            size = -500;
        } else if (chartSize == 5) {
            size = -1000;
        } else if (chartSize == 6) {
            return null;
        } else if (chartSize == 7) {//20m
            size = -20;
        }


        if (nChartValueAxis.getKind() == NChartValueAxisKind.X)
            return size;
        else if (nChartValueAxis.getKind() == NChartValueAxisKind.Y)
            return size;
        else if (nChartValueAxis.getKind() == NChartValueAxisKind.Z)
            return size;
        return null;
    }

    @Override
    public Number max(NChartValueAxis nChartValueAxis) {
        int size = 0;
        if (chartSize == 1) {
            size = 10;
        } else if (chartSize == 2) {
            size = 50;
        } else if (chartSize == 3) {
            size = 100;
        } else if (chartSize == 4) {
            size = 500;
        } else if (chartSize == 5) {
            size = 1000;
        } else if (chartSize == 6) {
            return null;
        } else if (chartSize == 7) { //20m
            size = 20;
        }

        if (nChartValueAxis.getKind() == NChartValueAxisKind.X)
            return size;
        else if (nChartValueAxis.getKind() == NChartValueAxisKind.Y)
            return size;
        else if (nChartValueAxis.getKind() == NChartValueAxisKind.Z)
            return size;
        return null;
    }

    @Override
    public Number step(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String[] ticks(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String[] extraTicks(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Number length(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String doubleToString(double v, NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Date minDate(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Date maxDate(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Number dateStep(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String dateToString(Date date, double v, NChartValueAxis nChartValueAxis) {
        return null;
    }


    public void chartPoint() {
        //Log.d("###", "chartPoint Start =============== : "  + m_count );

        Float4 gaitXYZ = new Float4();
        gaitXYZ.x = gaitX;
        gaitXYZ.y = gaitY;
        gaitXYZ.z = gaitZ;
        gaitXYZ.w = gaitZupt;
        chartPointArray.add(gaitXYZ);

        if (logBoolean) {
            childLogFileArray = new ArrayList<String>();
            childLogFileArray.add(timeStamp + "");
            childLogFileArray.add(accX + "");
            childLogFileArray.add(accY + "");
            childLogFileArray.add(accZ + "");
            childLogFileArray.add(gyroX + "");
            childLogFileArray.add(gyroY + "");
            childLogFileArray.add(gyroZ + "");
            childLogFileArray.add(magX + "");
            childLogFileArray.add(magY + "");
            childLogFileArray.add(magZ + "");
            childLogFileArray.add(gaitZupt + "");
            childLogFileArray.add(gaitX + "");
            childLogFileArray.add(gaitY + "");
            childLogFileArray.add(gaitZ + "");
            childLogFileArray.add(quat[0] + "");
            childLogFileArray.add(quat[1] + "");
            childLogFileArray.add(quat[2] + "");
            childLogFileArray.add(quat[3] + "");
            logFileArray.add(childLogFileArray);
        }

        if (m_count == 0) {
            // Initialize the data in the chart. Note that this call is mandatory in the beginning of AutoScroll.
            //mNChartView.getChart().updateData();
            //new NChartPoint(NChartPointState.PointStateWithXYZ(0, 0, 0), series);
            //mNChartView.getChart().getSeries()[0].getPoints()[0].getCurrentState().

            mNChartView.getChart().beginTransaction();
            NChartPoint[] points = mNChartView.getChart().getSeries()[0].getPoints();

            Log.d("chartPoint m_count == 0", "m_count : " + m_count + "//// points:" + points.length);

            for (NChartPoint point : points) {
                point.getCurrentState().setDoubleX(gaitX);
                point.getCurrentState().setDoubleY(-gaitZ);
                point.getCurrentState().setDoubleZ(gaitY);
            }
            // Update data in the chart.
            mNChartView.getChart().streamData();
            // End the data changing session from-within separate thread.
            mNChartView.getChart().endTransaction();
            m_count++;
        } else {
            //ui Text Upload
            // 원래 하고싶었던 일들 (UI변경작업 등...)
            String step = "" + gaitStepCount;
            String distance = "" + gaitDistance;
            String xText = "" + gaitX;
            String yText = "" + gaitY;
            String zText = "" + gaitZ;
            String errorX = "" + gaitErrorX;
            String errorY = "" + gaitErrorY;
            String errorZ = "" + gaitErrorZ;
            String motion;
            if (gaitMotion == 0) {
                motion = "STANDING";
            } else if (gaitMotion == 2) {
                motion = "WALKING";
            } else if (gaitMotion == 3) {
                motion = "RUNNING";
            } else if (gaitMotion == 16) {
                motion = "FELL DOWN";
            } else {
                motion = "---";
            }
            String errorPercentage = "" + gaitErrorPercentage;
            String zupt = "" + gaitZupt;

            mStepText.setText(step);
            mDistanceText.setText(distance);
            mXText.setText(xText);
            mYText.setText(yText);
            mZText.setText(zText);
            mErrorXText.setText(errorX);
            mErrorYText.setText(errorY);
            mErrorZText.setText(errorZ);
            mErrorPercentageText.setText(errorPercentage);
            mMotionText.setText(motion);

            if (ChartZUPTDemoActivity.zUPTBoolen || ChartLineDemoActivity.lineBoolen || ChartAreaDemoActivity.areaBoolen) {
                ArrayList<String> testData = new ArrayList();
                testData.add(step);
                testData.add(distance);
                testData.add(motion);
                testData.add(xText);
                testData.add(yText);
                testData.add(zText);
                testData.add(errorPercentage);
                testData.add(errorX);
                testData.add(errorY);
                testData.add(errorZ);
                testData.add(zupt);

                if (ChartZUPTDemoActivity.zUPTBoolen) {
                    ChartZUPTDemoActivity.handlerSetText(testData);
                }
                if (ChartLineDemoActivity.lineBoolen) {
                    ChartLineDemoActivity.handlerSetText(testData);
                }
                if (ChartAreaDemoActivity.areaBoolen) {
                    ChartAreaDemoActivity.handlerSetText(testData);
                }
            }

            //mXyzText.setText("X : " + gaitX + "  Y : " + gaitY + "  Z : " + gaitZ);
            //if(gaitX != gaitX_before && gaitY != gaitY_before && gaitZ != gaitZ_before) {
            mNChartView.getChart().beginTransaction();

            // Force chart to extend data.
            mNChartView.getChart().extendData();

            mNChartView.getChart().endTransaction();
            // }
        }
    }


    //20160804 wifi run
    public void run() {
        if (!exeFlag01) {
            try {

                ConnectivityManager conMan = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState(); // wifi

                if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {

                    WifiManager wm = (WifiManager) this.getApplicationContext().getSystemService("wifi");

                    DhcpInfo dhcpInfo = wm.getDhcpInfo();

                    clientIp = dhcpInfo.gateway;
                    ipAddress = String.format(

                            "%d.%d.%d.%d", (clientIp & 0xff), (clientIp >> 8 & 0xff), (clientIp >> 16 & 0xff), (clientIp >> 24 & 0xff));

                    if (ipAddress.equals(serverIps)) {
                        exeFlag01 = true;

                        if (bThreadSwitch01 == false) {

                            bThreadSwitch01 = true;
                            cThreadSwitch01 = true;

                            if (android.os.Build.VERSION.SDK_INT > 9) {

                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                                StrictMode.setThreadPolicy(policy);

                            }


                            NetworkThread02 = new udpSocketTask();
                            NetworkThread02.execute();
                        }
                    } else {
                        Toast.makeText(this.getApplicationContext(), "Please check the wifi connection", Toast.LENGTH_SHORT).show();
                        exeFlag01 = false;
                        wifiBoolean = false;
                    }

                } else {
                    Toast.makeText(this.getApplicationContext(), "Please check the wifi connection", Toast.LENGTH_SHORT).show();
                    exeFlag01 = false;
                    wifiBoolean = false;
                }
            } catch (NullPointerException e) {
            }
        } else {

            if (bThreadSwitch01 == true) {

                bThreadSwitch01 = false;
                cThreadSwitch01 = false;

                NetworkThread02.cancel(true);
//					NetworkThread01.cancel(true);
            }

            exeFlag01 = false;
        }

    }


    //20160804 wifi socket
    boolean exeFlag01 = false;
    boolean bThreadSwitch01 = false, cThreadSwitch01 = false;
    //socketTask NetworkThread01;
    udpSocketTask NetworkThread02;
    String ipAddress;
    int clientIp;
    int serverPorts = 48555;
    String serverIps = "192.168.1.118";

    public static float gaitX, gaitY, gaitZ, gaitDistance, gaitErrorX, gaitErrorY, gaitErrorZ, gaitErrorPercentage;
    public static int gaitStepCount;
    public static int gaitMotion;
    public static int gaitZupt;


    public static float[] quat = new float[4];
    public static int timeStamp;
    public static float accX;
    public static float accY;
    public static float accZ;
    public static float gyroX, gyroY, gyroZ;
    public static float magX, magY, magZ;
    //    public float Mouse3D_euler1, Mouse3D_euler2, Mouse3D_euler3;
    public StnEggPkt.StnEggPacket build;


    private class udpSocketTask extends AsyncTask<Integer, Integer, Integer> {
        private final String serverIP = serverIps; // ex: 192.168.1.31
        private final int serverPort = serverPorts; // ex: 48601
        private int x = 0;

        InetAddress serverAddr;
        DatagramPacket outPacket;
        DatagramPacket inPacket;
        DatagramSocket dSock;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (dSock != null) {
                    dSock.close();
                }

                serverAddr = InetAddress.getByName(serverIP);
                dSock = new DatagramSocket(serverPort);

                int len = 1024;
                byte[] data = new byte[len];
                outPacket = new DatagramPacket(data, data.length, serverAddr, serverPort);
                inPacket = new DatagramPacket(data, len);
                byte[] message = new byte[4];
                message[0] = (byte) 0xFE;
                message[1] = (byte) 0x80 | 0x40 | 0x20 | 0x10 | 0x02;
                message[2] = 0;
                message[3] = 0;
                outPacket.setData(message);
                dSock.connect(serverAddr, serverPort);
                dSock.setSoTimeout(100);
                dSock.send(outPacket);

            } catch (UnknownHostException e) {
                e.getStackTrace();
            } catch (SocketException se) {
                se.getStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            x = 0;
            // TODO Auto-generated method stub
            while (bThreadSwitch01) {
                try {
                    dSock.receive(inPacket);
                    //   dSock.setSoTimeout(5000);
                } catch (IOException e) {

                    e.printStackTrace();
                } catch (NullPointerException e) {

                    e.printStackTrace();
                }

                if(inPacket == null) {
                    return null;
                }
                //Serf serfBuild = new Serf(inPacket);
                byte[] resultBuffer = new byte[inPacket.getLength()];
                System.arraycopy(inPacket.getData(), 0, resultBuffer, 0, inPacket.getLength());

                int contentStart = 0, contentEnd = 0;

                for (int i = 0; i < inPacket.getLength() - 1; i++) {
                    //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                    if (resultBuffer[i] == 'S' && resultBuffer[i + 1] == 'E') {
                        //20160804 old version(vr) contentStart = i + 4;
                        contentStart = i + 2;
                    }
                    if (resultBuffer[i] == '\r' && resultBuffer[i + 1] == '\n') {
                        contentEnd = i - 1;
                        break;
                    }
                }


                if (contentEnd > 0) {
                    //Log.e("WIFI", "contentEnd index : " +contentEnd);
                    //Log.e("WIFI", "contentStart index : " +contentStart);
                    //Log.e("WIFI" , " Real Data length -- > " + (contentEnd - contentStart));

                    try {

                        byte[] resultBuffer2 = new byte[contentEnd - contentStart + 1];
                        System.arraycopy(resultBuffer, contentStart, resultBuffer2, 0, contentEnd - contentStart + 1);

                        build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer2);

                        //if (build != null && build.getGait() != null) {
                        if (build != null){
                            timeStamp = build.getTimeStamp();
                            quat[0] = build.getKalman9AxesQuat().getF1();
                            quat[1] = build.getKalman9AxesQuat().getF2();
                            quat[2] = build.getKalman9AxesQuat().getF3();
                            quat[3] = build.getKalman9AxesQuat().getF4();
                            accX = build.getAcceleroDataG().getF1();
                            accY = build.getAcceleroDataG().getF2();
                            accZ = build.getAcceleroDataG().getF3();
                            gyroX = build.getGyroDataDps().getF1();
                            gyroY = build.getGyroDataDps().getF2();
                            gyroZ = build.getGyroDataDps().getF3();
                            magX = build.getMagDataUT().getF1();
                            magY = build.getMagDataUT().getF2();
                            magZ = build.getMagDataUT().getF3();
                            gaitX = build.getGait().getPositionX();
                            gaitY = build.getGait().getPositionY();
                            gaitZ = build.getGait().getPositionZ();
                            gaitStepCount = build.getGait().getZuptStepCounter();
                            gaitDistance = build.getGait().getTotalDistance();
                            gaitMotion = build.getGait().getZuptMotion();
                            gaitZupt = build.getGait().getZupt();
                            gaitErrorX = build.getGait().getErrorPositionX();
                            gaitErrorY = build.getGait().getErrorPositionY();
                            gaitErrorZ = build.getGait().getErrorPositionZ();
                            gaitErrorPercentage = build.getGait().getErrorPercentage();
                            //chartPoint();
                        } else {
                            m_count = 0;
                        }


                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                }



               /* try{
                    gaitX = serfBuild.build.getGait().getPositionX();
                    gaitY = serfBuild.build.getGait().getPositionY();
                    gaitZ = serfBuild.build.getGait().getPositionZ();
                    gaitStepCount = serfBuild.build.getGait().getZuptStepCounter();
                    gaitDistance = serfBuild.build.getGait().getTotalDistance();
                    gaitMotion = serfBuild.build.getGait().getZuptMotion();
                    gaitZupt = serfBuild.build.getGait().getZupt();
                    gaitErrorX = serfBuild.build.getGait().getErrorPositionX();
                    gaitErrorY = serfBuild.build.getGait().getErrorPositionY();
                    gaitErrorZ = serfBuild.build.getGait().getErrorPositionZ();
                    gaitErrorPercentage = serfBuild.build.getGait().getErrorPercentage();

                    childLogFileArray = new ArrayList<String>();
                    childLogFileArray.add(gaitZupt + "");
                    childLogFileArray.add(gaitX + "");
                    childLogFileArray.add(gaitY + "");
                    childLogFileArray.add(gaitZ + "");
                    childLogFileArray.add(serfBuild.build.getKalman9AxesQuat().getF1() + "");
                    childLogFileArray.add(serfBuild.build.getKalman9AxesQuat().getF2() + "");
                    childLogFileArray.add(serfBuild.build.getKalman9AxesQuat().getF3() + "");
                    childLogFileArray.add(serfBuild.build.getKalman9AxesQuat().getF4() + "");
                    logFileArray.add(childLogFileArray);

                    if(serfBuild.build.getGait() != null ){
                        chartPoint();
                    }else {
                        m_count = 0;
                    }
                }catch (NullPointerException e) {
                    e.printStackTrace();
                }*/

                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //ui upload
            chartPoint();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            bThreadSwitch01 = false;

            if (dSock != null) {
                dSock.close();
            } else {
                handler.sendEmptyMessage(0);
            }

            if (cThreadSwitch01 == true) {
                if (NetworkThread02 != null) {
                    bThreadSwitch01 = true;
                    NetworkThread02 = new udpSocketTask();
                    NetworkThread02.execute();
                }
            }
        }

        private Handler handler = new Handler() {
            public void hanleMessage(Message msg) {
                // Toast.makeText(getApplicationContext(), "The wifi connection is unstable. Please reboot the server", Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            }
        };







    }



    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;



    private void scanLeDevice(final boolean enable) {
        if(mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
        }
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = ChartingDemoActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ChartingDemoActivity.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ChartingDemoActivity.ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ChartingDemoActivity.ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String onlyDevice = device.getAddress().substring(0, 8);
                            Log.d("LeScanCallback", "device.getAddress() : " + device.getAddress() + " ;;;; device.getName() : " + device.getName() );
                            if ("CE:17:39".equals(onlyDevice) || "D4:AB:D8".equals(onlyDevice) || "00:0B:57".equals(onlyDevice) || "F5:6C:D6".equals(onlyDevice) || "DE:4C:0D".equals(onlyDevice)) {
                                mLeDeviceListAdapter.addDevice(device);
                                mLeDeviceListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }


    //DeviceControlActivity
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceAddress;
    private String mDeviceName;
    private BluetoothLeService mBluetoothLeService;
    /*private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";*/

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("mServiceConnection", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public byte[] data1, data2, buffer,  startBuffer, endBuffer;
    public boolean confirmFalg = false;
    boolean flags = false;
    boolean seFlag = false;
    boolean meFlag = false;
    boolean epFlag = false;
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_GATT_CONNECTED.equals(action)) {
//                mConnected = true;
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                mBluetoothLeService.displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d("ACTION_DATA_AVAILABLE", "EXTRA_DATA : " + intent.getStringExtra(EXTRA_DATA));
                byte[] data = intent.getByteArrayExtra(EXTRA_CHARACTER_DATA);

//                serf.prtocolBufferCheck(data);
                serfProtocolCheck(data);
                Log.d("onCharacteristicChanged" , "broadcastUpdate : " + serf.confirmFalg);
                if(serf.confirmFalg) {
//                    chartPoint();
                }
            } else if(EXTRA_CHARACTER_DATA.equals(action)) {
                Log.d("ACTION_DATA_AVAILABLE", "EXTRA_CHARACTER_DATA : " + intent.getStringExtra(EXTRA_DATA));
//                chartPoint();
            }
        }
    };

    int bufferInt = 0;
    private void serfProtocolCheck(byte[] characteristic) {
        byte[] data = characteristic;
        if (data != null && data.length > 0) {
            String a = "";

            byte[] test = new byte[1];
            for (int i = 0; i < data.length; i++) {
                test[0] = data[i];
                ULog.d("Protocol", "REAL RECEIVE MESSAGE ==>" + new String(test));
            }

            byte[] ddd = new byte[2];
            ddd[0] = data[data.length - 2];
            ddd[1] = data[data.length - 1];
            ULog.d("Protocol", "dd->>" + new String(ddd));

            if (!"EP".equals(new String(ddd))) {
                data2 = characteristic;
                flags = true;

                return;
            }

            if (flags) {
                ULog.d("Protocol", " 잘렸던 msg 이어 붙임 ");
                byte[] datas = data;
//                data = new byte[data1.length + data2.length + datas.length];
                data = new byte[data2.length + datas.length];

                ULog.d("Protocol", " DATA LENGTH :" + data.length);
                for (int i = 0; i < data.length; i++) {
                    if (i < data2.length)
                        data[i] = data2[i];
                    else
                        data[i] = datas[(i - data2.length)];
                    /*if(i < data1.length) {
                        data[i] = data1[i];
                    }else if (i < data2.length + data1.length) {
                        data[i] = data2[(i - data1.length)];
                    }else {
                        data[i] = datas[(i - data1.length - data2.length)];
                    }*/
                }
                flags = false;
            }

            byte[] d = new byte[2];
            d[0] = data[0];
            d[1] = data[1];

            ULog.d("Protocol", "d->>" + new String(d));

            String cha = new String(d);
            ULog.d("Protocol", "d2->>" + cha);


            //20160822 start
            try {
                int contentStart = 0, contentEnd = 0;
                if (data != null && data.length > 0) {
                    ULog.d("Protocol", "# Read characteristic 20160817: data 있음");

                    for (int i = 0; i < data.length - 1; i++) {
                        //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                        if (data[i] == 'S' && data[i + 1] == 'E') {
                            //20160804 old version(vr) contentStart = i + 4;
                            contentStart = i + 2;
                        }
                        if (data[i] == 'E' && data[i + 1] == 'P') {
                            contentEnd = i - 1;
                            break;
                        }
                    }
                    ULog.d("Protocol", "contentStart : " + contentStart + "////contentEnd : " + contentEnd);

                    if(contentStart > contentEnd) {
                        return;
                    }
                }
                byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
                build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);

                if(build != null) {
                    ULog.d("serfProtocolCheck", "build gait getF1: " + build.getGaitPos().getF1() + "///getF2: " + build.getGaitPos().getF1() + "///getF2: " + build.getGaitPos().getF3());
                    gaitX = build.getGait().getPositionX();
                    gaitY = build.getGait().getPositionY();
                    gaitZ = build.getGait().getPositionZ();
                    accX = build.getAcceleroDataG().getF1();
                    accY = build.getAcceleroDataG().getF2();
                    accZ = build.getAcceleroDataG().getF3();
                    gyroX = build.getGyroDataDps().getF1();
                    gyroY = build.getGyroDataDps().getF2();
                    gyroZ = build.getGyroDataDps().getF3();
                    magX = build.getMagDataUT().getF1();
                    magY = build.getMagDataUT().getF2();
                    magZ = build.getMagDataUT().getF3();
                    gaitX = build.getGait().getPositionX();
                    gaitY = build.getGait().getPositionY();
                    gaitZ = build.getGait().getPositionZ();
                    gaitStepCount = build.getGait().getZuptStepCounter();
                    gaitDistance = build.getGait().getTotalDistance();
                    gaitMotion = build.getGait().getZuptMotion();
                    gaitZupt = build.getGait().getZupt();
                    gaitErrorX = build.getGait().getErrorPositionX();
                    gaitErrorY = build.getGait().getErrorPositionY();
                    gaitErrorZ = build.getGait().getErrorPositionZ();
                    gaitErrorPercentage = build.getGait().getErrorPercentage();
                    chartPoint();
                }

            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            //20160822 end

            // init
            buffer = null;
            bufferInt = 0;
            seFlag = false;
            epFlag = false;
        }

    }
    //20160728 start
    private static final String HEXES = "0123456789ABCDEF";
    public static String byteArrayToHexString(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        boolean firstEntry = true;
        sb.append('[');

        for (final byte b : array) {
            if (!firstEntry) {
                sb.append(", ");
            }
            sb.append(HEXES.charAt((b & 0xF0) >> 4));
            sb.append(HEXES.charAt((b & 0x0F)));
            firstEntry = false;
        }

        sb.append(']');
        return sb.toString();
    }
    //20160728 end



    private void bleDeviceControl(String address) {
        Log.d("bleDeviceControl", "address :" + address);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        if (startService(gattServiceIntent) != null) {
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(address);
            Log.d("bleDeviceControl", "Connect request result=" + result);
        }
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}

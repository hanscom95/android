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

package com.se.idt.activity;

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
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.se.idt.utility.StnEggPkt;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UninitializedMessageException;


import com.se.idt.R;
import com.se.idt.ble.BluetoothLeService;
import com.se.idt.utility.ULog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.se.idt.ble.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static com.se.idt.ble.BluetoothLeService.ACTION_GATT_CONNECTED;
import static com.se.idt.ble.BluetoothLeService.ACTION_GATT_DISCONNECTED;
import static com.se.idt.ble.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED;
import static com.se.idt.ble.BluetoothLeService.EXTRA_CHARACTER_DATA;

public class IdtActivity extends AppCompatActivity {

    private Context mContext;

    Button mActivityButton, mFitnessButton, mHeartrateButton;
    TextView mWaitText;

    public static int status = 0;
    public static int eStatus = 0;

    static final int MESSAGE_HEART_RATE_REQUEST = 43;
    static final int MESSAGE_HEART_RATE_ACCEPT = 44;

    static final int MESSAGE_REAL_TIME_ACTIVITY_REPORT_CONTENT = 23 ;
    static final int MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT = 21;

    private Timer mTimer = null;
    int mTime = 0;

    boolean testBoolean = true;
    boolean initPacket = false;

    static int seqNo;
    static int oldSeqNo;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        mContext = this;

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar);

        mActivityButton = (Button) findViewById(R.id.activity_button);
        mFitnessButton = (Button) findViewById(R.id.fitness_button);
        mHeartrateButton = (Button) findViewById(R.id.heartrate_button);

        mActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBluetoothLeService != null && mServiceConnection !=null) {
                    if(!initPacket) {
                        return;
                    }

                    status = 1;
                    motionMessageSend(3);

                    Intent intent = new Intent(getApplicationContext(), WalkRunActivity.class);
                    startActivity(intent);
                    motionMessageSend(3);
                }else {
                    Toast.makeText(getApplicationContext(), "Please connect ble.", Toast.LENGTH_LONG).show();
                }
            }
        });
        mFitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBluetoothLeService != null && mServiceConnection !=null) {
                    if(!initPacket) {
                        return;
                    }
                    status = 2;
                    motionMessageSend(4);

                    Intent intent = new Intent(getApplicationContext(), FitnessActivity.class);
                    startActivity(intent);
                    motionMessageSend(4);
                }else {
                    Toast.makeText(getApplicationContext(), "Please connect ble.", Toast.LENGTH_LONG).show();
                }
            }
        });
        mHeartrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBluetoothLeService != null && mServiceConnection !=null) {
                    if(!initPacket) {
                        return;
                    }
                    status = 3;
//                    heartRateMessageSend();

                    Intent intent = new Intent(getApplicationContext(), HeartRateActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Please connect ble.", Toast.LENGTH_LONG).show();
                }
            }
        });

        mWaitText = (TextView)findViewById(R.id.waitText);


        mTimer = new Timer();
        mTimer.schedule(new mTimerTask(), 1000);

        seqNo = 0;
        oldSeqNo = 0;
    }

    private class mTimerTask extends TimerTask {
        public mTimerTask() {
        }

        public void run() {
            mTime++;
            mTimer.schedule(new mTimerTask(), 1000);
        }
    }

    public static void heartRateMessageSend() {
        StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
        builder.setSerpCommand(MESSAGE_HEART_RATE_REQUEST);

        try {
            byte[] encoded = new byte[builder.build().toByteArray().length+5];
            byte[] builderTobyte = builder.build().toByteArray();

            encoded[0] = 'S';
            encoded[1] = 'E';
            int i;
            for( i = 0; i < builderTobyte.length; i++) {
                encoded[i+2] = builderTobyte[i];
            }
            encoded[i+2] = 'E';
            encoded[i+3] = '\r';
            encoded[i+4] = '\n';
            mBluetoothLeService.bleWrite(encoded);
        }catch (UninitializedMessageException e){
            e.printStackTrace();
        }
    }

    public static void motionMessageSend(int motion) {
        //command
        StnEggPkt.StnEggPacket.Builder sendBuilder2 = StnEggPkt.StnEggPacket.newBuilder();
        sendBuilder2.setSerpCommand(mBluetoothLeService.MESSAGE_BAND_MODE_REQUEST);
        sendBuilder2.setUserUint01(motion);

        try {
            byte[] encoded = new byte[sendBuilder2.build().toByteArray().length + 5];
            byte[] builderTobyte = sendBuilder2.build().toByteArray();

            encoded[0] = 'S';
            encoded[1] = 'E';
            int i;
            for (i = 0; i < builderTobyte.length; i++) {
                encoded[i + 2] = builderTobyte[i];
            }
            encoded[i + 2] = 'E';
            encoded[i + 3] = '\r';
            encoded[i + 4] = '\n';
            mBluetoothLeService.bleWrite(encoded);

        } catch (UninitializedMessageException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!navigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.bleSetup:
                if(mBluetoothLeService != null) {
                    if(mGattUpdateReceiver != null) {
                        unregisterReceiver(mGattUpdateReceiver);
                    }

                    if(mServiceConnection != null) {
                        unbindService(mServiceConnection);
                    }

                    mBluetoothLeService.disconnect();
                    mBluetoothLeService.close();
                    mBluetoothLeService = null;
                }

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

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    protected void onResume() {
        super.onResume();
        if(mBluetoothLeService != null && mServiceConnection !=null && eStatus == 1) {
            status = 0;
            eStatus = 0;
            motionMessageSend(3);
        }
    }

    protected void onPause() {
        super.onPause();
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



    public StnEggPkt.StnEggPacket build;

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

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
            mInflator = IdtActivity.this.getLayoutInflater();
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
            IdtActivity.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new IdtActivity.ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (IdtActivity.ViewHolder) view.getTag();
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
                            String onlyDeviceName = null;
                            if(device.getName() != null) {
                                onlyDeviceName = device.getAddress().substring(0, 2);
                            }

                            ULog.d("LeScanCallback", "device.getAddress() : " + device.getAddress() + " ;;;; device.getName() : " + device.getName() );
//                            if ("00:0B:57".equals(onlyDevice) || "F5:6C:D6".equals(onlyDevice) || "DE:4C:0D".equals(onlyDevice) || "E3:BC:5E".equals(onlyDevice) || "EF:06:AB".equals(onlyDevice)
//                                || "DE:76:F6".equals(onlyDevice) || "D4:EF:63".equals(onlyDevice)|| "DD:6A:D9".equals(onlyDevice)|| "CE:87:2C".equals(onlyDevice) ) {
//                                mLeDeviceListAdapter.addDevice(device);
//                                mLeDeviceListAdapter.notifyDataSetChanged();
//                            }else if(onlyDeviceName != null && "EG".equals(onlyDeviceName)){
                            if(device.getName() != null){
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
    private static BluetoothLeService mBluetoothLeService;
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
            Toast.makeText(getApplicationContext(), "Disabled to initialize Bluetooth", Toast.LENGTH_LONG).show();
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
                seqNo = 0;
                oldSeqNo = 0;
                mWaitText.setVisibility(View.VISIBLE);
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
                Toast.makeText(getApplicationContext(), "Disconnect yout Bluetooth Device", Toast.LENGTH_LONG).show();
//                mConnected = false;
                mBluetoothLeService = null;
                unregisterReceiver(mGattUpdateReceiver);
                unbindService(mServiceConnection);
                if(status == 1) {
                    WalkRunActivity.mActivity.finish();
                }else if(status == 2) {
                    FitnessActivity.mFitness.finish();
                }
                status = 0;
                initPacket = false;
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                mBluetoothLeService.displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                initPacket = true;
                mWaitText.setVisibility(View.GONE);
                byte[] data = intent.getByteArrayExtra(EXTRA_CHARACTER_DATA);
                protocolCheck(data);
            } else if(EXTRA_CHARACTER_DATA.equals(action)) {
            }
        }
    };

    private void protocolCheck(byte[] characteristic) {
        byte[] data = characteristic;
        if (data != null && data.length > 0) {
            String a = "";

            byte[] test = new byte[1];
            for (int i = 0; i < data.length; i++) {
                test[0] = data[i];
                ULog.d("Protocol", "REAL RECEIVE MESSAGE ==>" + new String(test));

                if(data.length < 3) {
                    return;
                }
            }

            byte[] ddd = new byte[2];
            ddd[0] = data[data.length - 2];
            ddd[1] = data[data.length - 1];
            ULog.d("Protocol", "dd->>" + new String(ddd));
//            if ("SE".equals(new String(aaa))) {
//                data1 = characteristic;
//
//                return;
//            }

            if (!"\r\n".equals(new String(ddd))) {
                if(data2 != null && data2.length > 0){
                    byte[] datas = new byte[data2.length + data.length];
                    for (int i = 0; i < datas.length; i++) {
                        if(i < data2.length) {
                            datas[i] = data2[i];
                        }else {
                            datas[i] = data[(i - data2.length)];
                        }
                    }
                    data2 = datas;
                }else {
                    data2 = characteristic;
                }

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
                        if (data[i] == '\r' && data[i + 1] == '\n') {
                            contentEnd = i - 2;
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
                ULog.d("result buffer : " + byteArrayToHexString(resultBuffer));


                StnEggPkt.StnEggPacket build;
                build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);


                data2 = new byte[0];

                ULog.d("idtActivity", "getSerpCommand : " + build.getSerpCommand() + " /////getMotion : " + build.getMotion() + " /////getCount : " + build.getCount() + " /////getSeqNo : " + build.getSeqNo()+ " /// seq : " + seqNo + " /////getDistanceMeters : " + build.getDistanceMeters() + " /////getCalories : " + build.getCalories() + " /////getUserInt01 : " + build.getUserInt01() + " /////getUserInt02 : " + build.getUserInt02() + " /////getUserInt04 : " + build.getUserInt04() +" /////getUserUint01 : " + build.getUserUint01() + " /////getUserUint02 : " + build.getUserUint02() + " /////getUserUint04 : " + build.getUserUint04());
                ULog.d("idtActivity", "getSerpCommand : " + build.getSerpCommand() + " /////date : " + build.getDate().getYear() + "-" + build.getDate().getMonth() + "-" + build.getDate().getDay() + " " + build.getDate().getHour() + ":" + build.getDate().getMinutes() + ":" + build.getDate().getSeconds());

//                TextView logText = (TextView) findViewById(R.id.log_text);
//                String log = logText.getText().toString();
//                if(log.length() > 500) {
//                    log = "";
//                }
//                log += "command : " + logText.getText().toString()  + "/ motion : " + build.getMotion()  + "/ count : " + build.getCount()   + "/ date : " + build.getDate().getYear()   + "-" + build.getDate().getMonth()   + "-" + build.getDate().getDay()   + " " + build.getDate().getHour()   + ":" + build.getDate().getMinutes()   + ":" + build.getDate().getSeconds() + "\r\n";
//                logText.setText(log);


                if(build != null) {

                    // now time setting
                    Date tdays = new Date();
                    int tday = tdays.getDay();
                    int ty = tdays.getYear() + 1900;
                    int tmonth = tdays.getMonth() + 1;
                    int tdate = tdays.getDate();

                    int thour = tdays.getHours();
                    int tminute = tdays.getMinutes();
                    int tsecond = tdays.getSeconds();

                    StnEggPkt.StnEggPacket.Builder sendBuilder = null;
                    if(build.getSerpCommand() == 10) {
                        sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                        sendBuilder.setSerpCommand(11);

                        // date
                        sendBuilder.getDateBuilder().setYear(ty);
                        sendBuilder.getDateBuilder().setMonth(tmonth);
                        sendBuilder.getDateBuilder().setDay(tdate);
                        sendBuilder.getDateBuilder().setHour(thour);
                        sendBuilder.getDateBuilder().setMinutes(tminute);
                        sendBuilder.getDateBuilder().setSeconds(tsecond);
                        if (tday == 0) {
                            sendBuilder.getDateBuilder().setWeekDay(7);
                        } else {
                            sendBuilder.getDateBuilder().setWeekDay(tday);
                        }



                    }else if(build.getSerpCommand() == 12) {

                        sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                        sendBuilder.setSerpCommand(15);
                        sendBuilder.getPhysiqueBuilder().setHeight(180);
                        sendBuilder.getPhysiqueBuilder().setWeight(70);

                        sendBuilder.getPhysiqueBuilder().setHeartRateHigh(130);
                        sendBuilder.getPhysiqueBuilder().setHeartRateLow(70);

                        sendBuilder.getPhysiqueBuilder().setGender(0);

                        int birthYear = 0;
                        int birthMonth = 0;
                        int birthDay = 0;
                        birthYear = ty;
                        birthMonth = tmonth;
                        birthDay = tdate;
                        sendBuilder.getDateBuilder().setYear(birthYear);
                        sendBuilder.getDateBuilder().setMonth(birthMonth);
                        sendBuilder.getDateBuilder().setDay(birthDay);


                        sendBuilder.setCount(1000);
                        sendBuilder.setCalories(600);

                    }else if(build.getSerpCommand() == 23) {

                        if(testBoolean) {
                            sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                            sendBuilder.setSerpCommand(45);
                            sendBuilder.setUserUint01(3);
                            testBoolean = false;
                        }
                    }

                    if(sendBuilder != null) {
                        try {
                            byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
                            byte[] builderTobyte = sendBuilder.build().toByteArray();

                            encoded[0] = 'S';
                            encoded[1] = 'E';
                            int i;
                            for (i = 0; i < builderTobyte.length; i++) {
                                encoded[i + 2] = builderTobyte[i];
                            }
                            encoded[i + 2] = 'E';
                            encoded[i + 3] = '\r';
                            encoded[i + 4] = '\n';

                            mBluetoothLeService.bleWrite(encoded);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (build.getSerpCommand() == MESSAGE_HEART_RATE_ACCEPT) {
//                    mMainLayout.setBackgroundResource(R.drawable.heart_rate);
                        HeartRateActivity.setText(build.getHearthRate());
                    }


                    if (build.getSerpCommand() == MESSAGE_REAL_TIME_ACTIVITY_REPORT_CONTENT || build.getSerpCommand() == MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT) {
                        seqNo++;

                        if (status == 1) {// walk, run
                            int walkCnt = 0;
                            int runCnt = 0;

                            int distance = 0, calories = 0;

                            if (build.getMotion() == 2) {
                                walkCnt = build.getCount();
                            } else if (build.getMotion() == 3) {
                                runCnt = build.getCount();
                            }

                            distance = build.getDistanceMeters();
                            calories = build.getCalories();
                            int motion = build.getMotion();

                            WalkRunActivity.setText(walkCnt, runCnt, motion);
                        } else if (status == 2) { // fitness
                            String motion = "";
                            int pushCnt = 0, sitCnt = 0, butterflyCnt = 0, bicepsCnt = 0, shoulderCnt = 0, kettlebellCnt = 0, seatedCnt = 0, dumbellCnt = 0, tricepsCnt = 0, benchCnt = 0, squatCnt = 0, alternateCnt = 0,
                                    waitingCnt = 0, analyzingCnt = 0;
                            if (build.getUserInt01() != 2 || build.getUserInt01() != 3 || build.getUserInt01() != 99 || build.getUserInt01() != 97 || build.getUserInt01() != 98 || build.getUserInt01() != 36) {
                                if (build.getUserInt01() == 6) {
                                    motion = "PUSH UP";
                                    pushCnt = build.getCount();
                                } else if (build.getUserInt01() == 7) {
                                    motion = "SIT UP";
                                    sitCnt = build.getCount();
                                } else if (build.getUserInt01() == 8) {
                                    motion = "BUTTERFLY";
                                    butterflyCnt = build.getCount();
                                } else if (build.getUserInt01() == 9) {
                                    motion = "BICEPS CURL";
                                    bicepsCnt = build.getCount();
                                } else if (build.getUserInt01() == 10) {
                                    motion = "SHOULDER PRESS";
                                    shoulderCnt = build.getCount();
                                } else if (build.getUserInt01() == 30) {
                                    motion = "KETTLEBELL SWING";
                                    kettlebellCnt = build.getCount();
                                } else if (build.getUserInt01() == 32) {
                                    motion = "SEATED CABLE ROW";
                                    seatedCnt = build.getCount();
                                } else if (build.getUserInt01() == 35) {
                                    motion = "DUMBELL TRICEPS PRESS";
                                    dumbellCnt = build.getCount();
                                }/*else if(build.getUserInt01() == 36) {
                                tricepsCnt = build.getCount();
                            }*/ else if (build.getUserInt01() == 37) {
                                    motion = "BENCH KICKBACK";
                                    benchCnt = build.getCount();
                                } else if (build.getUserInt01() == 38) {
                                    motion = "SQUAT";
                                    squatCnt = build.getCount();
                                } else if (build.getUserInt01() == 39) {
                                    motion = "ALTERNATE DELTOID RAISE";
                                    alternateCnt = build.getCount();
                                }/*else if(build.getUserInt01() == 97) {
                                waitingCnt = build.getCount();
                            }else if(build.getUserInt01() == 98) {
                                analyzingCnt = build.getCount();
                            }*/
                            }

                            if (build.getCount() > 0 && build.getUserInt01() != 97) {
                                mTime = 0;
                                FitnessActivity.setProgressVisibility(true, motion, false, build.getCount());
                            }

                            if (build.getUserInt01() == 97) {
                                FitnessActivity.setProgressVisibility(true, "Waiting", true, 0);
                            }


                            /*if (build.getUserInt02() == 1 && (seqNo+3 < build.getSeqNo())) {
                                seqNo = build.getSeqNo();
                                mTime = 0;
                                FitnessActivity.setProgressVisibility(true, "Waiting", true, 0);
//                            FitnessActivity.setText(pushCnt, sitCnt, butterflyCnt, bicepsCnt, shoulderCnt, kettlebellCnt, seatedCnt, dumbellCnt, tricepsCnt, benchCnt, squatCnt, alternateCnt, waitingCnt, analyzingCnt);
                                FitnessActivity.setText(pushCnt, sitCnt, butterflyCnt, bicepsCnt, shoulderCnt, kettlebellCnt, seatedCnt, dumbellCnt, benchCnt, squatCnt, alternateCnt);
                            }*/

                            if(build.getUserInt02() == 1 && (oldSeqNo + 3 < seqNo)) {
                                oldSeqNo = seqNo;
                                mTime = 0;
                                FitnessActivity.setProgressVisibility(true, "Waiting", true, 0);
                                FitnessActivity.setText(pushCnt, sitCnt, butterflyCnt, bicepsCnt, shoulderCnt, kettlebellCnt, seatedCnt, dumbellCnt, benchCnt, squatCnt, alternateCnt);
                            }
                        }
                    }
                }




            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            //20160822 end
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
        ULog.d("bleDeviceControl", "address :" + address);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        if (startService(gattServiceIntent) != null) {
            ULog.d("bleDeviceControl", "mServiceConnection : service != null");
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(address);
            ULog.d("bleDeviceControl", "Connect request result=" + result);
        }
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}

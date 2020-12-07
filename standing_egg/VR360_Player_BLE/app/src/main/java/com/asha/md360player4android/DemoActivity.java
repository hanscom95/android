package com.asha.md360player4android;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



/**
 * Created by hzqiujiadi on 16/1/26.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class DemoActivity extends AppCompatActivity {
    public static int video = 0;
    public int mStatus = 0;
//    public String mBleAddress;


    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private String mDeviceAddress;
    private String mDeviceName;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final EditText et = (EditText) findViewById(R.id.edit_text_url);
        final RadioGroup rd = (RadioGroup) this.findViewById(R.id.videoGroup);
        //et.setText("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.trinity_360);
        //Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.trinity_360);

       // et.setText(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getPackageName() + "/raw/" + R.raw.trinity_360);
       // final String UrlPath="android.resource://"+getPackageName()+"/"+R.raw.vr;
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = et.getText().toString();

                Log.e("url" , "uri --- >  "+ url);

                Uri uri = getRawUri(R.raw.trinity_360);

                if (!TextUtils.isEmpty(url)) {
                    if(mStatus == 0) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 0);
                    }else {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 0, mDeviceAddress);
                    }
                } else {
                    Toast.makeText(DemoActivity.this, "empty url!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.button2).setVisibility(View.GONE);

        findViewById(R.id.bitmap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = getDrawableUri(R.drawable.texture);
                if(mStatus == 0) {
                    MD360PlayerActivity.startBitmap(DemoActivity.this, uri);
                }else {
                    MD360PlayerActivity.startBitmap(DemoActivity.this, uri, mDeviceAddress);
                }
            }
        });

        findViewById(R.id.mp4_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStatus == 0) {
                    if (rd.getCheckedRadioButtonId() == R.id.video1) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 0);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video2) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 1);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video3) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 2);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video4) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 3);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video5) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 4);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video6) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 5);
                    }
                }else {
                    if (rd.getCheckedRadioButtonId() == R.id.video1) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 0, mDeviceAddress);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video2) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 1, mDeviceAddress);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video3) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 2, mDeviceAddress);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video4) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 3, mDeviceAddress);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video5) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 4, mDeviceAddress);
                    } else if (rd.getCheckedRadioButtonId() == R.id.video6) {
                        MD360PlayerActivity.startVideo(DemoActivity.this, 5, mDeviceAddress);
                    }
                }
            }
        });
    }

//
//    Uri videoUrl = Uri.parse("android.resource://" + getContext().getPackageName() + "/"
//            + R.raw.test);
//    mediaPlayer.setDataSource(getContext(),Uri.parse(videoUrl));
//
//

   // "android.resource://com.asha.md360player4android/"


    private Uri getDrawableUri(@DrawableRes int resId){
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }

    private Uri getRawUri(@RawRes int resId){
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        Log.d("onOptionsItemSelected", "id: " + item.getTitle().toString());
        switch (item.getItemId()) {
            case R.id.wifi:
                mStatus = 0;
                break;
            case R.id.ble:
                mStatus = 1;


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
                        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
                        if (device == null) return;
                        if (mScanning) {
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            mScanning = false;
                        }

                        mDeviceAddress = device.getAddress();
                        mDeviceName = device.getName();
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



    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
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
                            if ("00:0B:57".equals(onlyDevice) || "F5:6C:D6".equals(onlyDevice) || "DE:4C:0D".equals(onlyDevice) || "CD:8A:B9".equals(onlyDevice) || "CE:17:39".equals(onlyDevice)) {
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


    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DemoActivity.this.getLayoutInflater();
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
            DemoActivity.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new DemoActivity.ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (DemoActivity.ViewHolder) view.getTag();
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


}

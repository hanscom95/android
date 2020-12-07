package se.com.band.login;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.com.band.MainMenuActivity;
import se.com.band.R;
import se.com.band.utility.Preferences;
import se.com.band.utility.ULog;

public class DeviceConnectActivity extends AppCompatActivity {

    Context mContext;

    AlertDialog alertDialogObject;
    Preferences mPreference;

    View mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect);

        mContext = this;
        mPreference = Preferences.getInstance(mContext);

        if (ActivityCompat.checkSelfPermission(mContext,Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext,Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADMIN)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(mContext, "BLUETOOTH Admin storage", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH},
                    0);
        }else {
            bleSearch();
        }

        mMainView = (View) findViewById(R.id.content_device_connect);
        mMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleSearch();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bleSearch();
                }
                break;
            }
        }
    }

    LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private String mDeviceAddress;
    private String mDeviceName;

    private boolean mDialogFlag = false;

    private static final long SCAN_PERIOD = 10000;


    private void bleSearch() {
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(mContext, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
            return;
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
//                    finish();
            return;
        }

        mLeDeviceListAdapter = new LeDeviceListAdapter();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
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
                mPreference.setUserDevice(mDeviceAddress, mDeviceName);
                ULog.d("addr : " + mDeviceAddress + "// name : " + mDeviceName);

                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialogBuilder.setOnCancelListener(cancelListener);
        //Create alert dialog object via builder
        alertDialogObject = dialogBuilder.create();
        //Show the dialog
        scanLeDevice(true);
    }

    DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            mDialogFlag = false;
        }
    };


    private void scanLeDevice(final boolean enable) {
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
//                            if ("00:0B:57".equals(onlyDevice) || "F5:6C:D6".equals(onlyDevice) || "DE:4C:0D".equals(onlyDevice) || "CA:ED:77".equals(onlyDevice)) {
                    if(device.getName() != null) {
                        mLeDeviceListAdapter.addDevice(device);
                        mLeDeviceListAdapter.notifyDataSetChanged();

                        if(mLeDeviceListAdapter.getCount() > 0) {
                            if(!mDialogFlag) {
                                mDialogFlag = true;
                                alertDialogObject.show();
                            }
                        }
                    }
//                            }
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
            mInflator = DeviceConnectActivity.this.getLayoutInflater();
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
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
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

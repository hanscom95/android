package se.com.band;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;
import se.com.band.activity.WalkRunActivity;
import se.com.band.ble.BluetoothLeService;
import se.com.band.dfu.DfuService;
import se.com.band.heart.HeartActivity;
import se.com.band.login.LoginActivity;
import se.com.band.option.NotificationActivity;
import se.com.band.option.NotificationAdapter;
import se.com.band.utility.StnEggPkt;
import se.com.band.utility.ULog;

import static se.com.band.ble.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static se.com.band.ble.BluetoothLeService.ACTION_GATT_CONNECTED;
import static se.com.band.ble.BluetoothLeService.ACTION_GATT_DISCONNECTED;
import static se.com.band.ble.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED;
import static se.com.band.ble.BluetoothLeService.EXTRA_CHARACTER_DATA;

public class HelloActivity extends AppCompatActivity{

    Context mContext;

    ProgressBar mProgressBar;
    TextView mTextPercentage, mTextUploading;
    Button mButton, mButton2, mButton3, mButton4, mButton5, mButton6, mButton7, mButton8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;



        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(mContext, "ACCESS_FINE_LOCATION permission", Toast.LENGTH_SHORT).show();
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(mContext, "ACCESS_FINE_LOCATION permission", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }


        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] value = new byte[1];
                value[0] = 1;
                mBluetoothLeService.bleWrite(value);
            }
        });


        mButton2 = (Button) findViewById(R.id.button2);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDfuServiceRunning()) {
                    showUploadCancelDialog();
                    return;
                }



                DfuServiceInitiator starter = new DfuServiceInitiator(mDeviceAddress)
                        .setDeviceName(mDeviceName)
                        .setKeepBond(false);
//                Uri mFileStreamUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ota_file.zip");
                Uri mFileStreamUri = Uri.parse("file:///mnt/sdcard/Download/ota_file.zip");

                String mFilePath = null;
                starter.setZip(mFileStreamUri, mFilePath);
                starter.start(mContext, DfuService.class);
            }
        });


        mButton3 = (Button) findViewById(R.id.button3);
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        ||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to write the permission.
                        Toast.makeText(getApplicationContext(), "BLUETOOTH Admin storage", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                            2);
                }else {
                    new FileDownloadTask().execute();
                }
            }
        });


        mButton4 = (Button) findViewById(R.id.button4);
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                List<ApplicationInfo> smsPackages = new ArrayList<ApplicationInfo>();
                NotificationAdapter adapter = new NotificationAdapter(mContext);

                for (ApplicationInfo applicationInfo : packages) {
                    try {
                        PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

                        //Get Permissions
                        String[] requestedPermissions = packageInfo.requestedPermissions;

                        if(requestedPermissions != null) {
                            for (int i = 0; i < requestedPermissions.length; i++) {
//                                if(requestedPermissions[i].equals("android.permission.READ_SMS") && ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
                                if(requestedPermissions[i].equals("android.permission.READ_SMS")) {
                                    smsPackages.add(applicationInfo);
                                    adapter.addList(applicationInfo);
                                }
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }


                for (ApplicationInfo smsApplicationInfo : smsPackages) {
                    if((smsApplicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        Log.d("Notifiaction", "App: " + smsApplicationInfo.loadLabel(getPackageManager()) + " Package: " + smsApplicationInfo.packageName + " Flag: " + smsApplicationInfo.flags);
                    }
                }

                adapter.notifyDataSetChanged();
                AlertDialog.Builder notifiDialogBuilder = new AlertDialog.Builder(mContext);
                notifiDialogBuilder.setTitle("NOTIFICATION");
                notifiDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });

                notifiDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("HelloActivity", "which : " + which);
                    }
                });
                //Create alert dialog object via builder
                AlertDialog alertDialogObject = notifiDialogBuilder.create();
                //Show the dialog
                alertDialogObject.show();


            }
        });


        mButton5 = (Button) findViewById(R.id.button5);
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        mButton6 = (Button) findViewById(R.id.button6);
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WalkRunActivity.class);
                startActivity(intent);
            }
        });


        mButton7 = (Button) findViewById(R.id.button7);
        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), NumberActivity.class);
//                startActivity(intent);
//                Intent intent = new Intent(getApplicationContext(), TestTabActivity.class);
//                startActivity(intent);

//                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
//                startActivity(intent);


            }
        });


        mButton8 = (Button) findViewById(R.id.button8);
        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HeartActivity.class);
                startActivity(intent);
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextPercentage = (TextView) findViewById(R.id.ota_checked_msg);
        mTextUploading = (TextView) findViewById(R.id.ota_uploading_msg);

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("HelloActivity", "requestCode : " + requestCode + " //permissions : " + permissions);
        switch (requestCode) {
            case 0: {
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new FileDownloadTask().execute();
                }
                break;
            }
            case 3: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bleSearch();
                }
                break;
            }
            case 4: {
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
    }

    private void bleSearch() {
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(mContext, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
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
                if(!mDeviceName.substring(0,3).equals("Dfu")) {
                    bleDeviceControl(mDeviceAddress);
                }
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
        scanLeDevice(true);
    }




    LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    //DeviceControlActivity
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceAddress;
    private String mDeviceName;
    private static BluetoothLeService mBluetoothLeService;

    private static final long SCAN_PERIOD = 10000;


    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String onlyDevice = device.getAddress().substring(0, 8);
//                            Log.d("LeScanCallback", "device.getAddress() : " + device.getAddress() + " ;;;; device.getName() : " + device.getName() );
//                            if ("00:0B:57".equals(onlyDevice) || "F5:6C:D6".equals(onlyDevice) || "DE:4C:0D".equals(onlyDevice) || "CA:ED:77".equals(onlyDevice)) {
                                mLeDeviceListAdapter.addDevice(device);
                                mLeDeviceListAdapter.notifyDataSetChanged();
//                            }
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }


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
            mInflator = HelloActivity.this.getLayoutInflater();
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
            HelloActivity.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new HelloActivity.ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (HelloActivity.ViewHolder) view.getTag();
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
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

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
            Log.d("HelloActivity", action);
            if (ACTION_GATT_CONNECTED.equals(action)) {
//                mConnected = true;
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
                mBluetoothLeService = null;
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                mBluetoothLeService.displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] data = intent.getByteArrayExtra(EXTRA_CHARACTER_DATA);
                protocolCheck(data);
            } else if(EXTRA_CHARACTER_DATA.equals(action)) {
            }
        }
    };


    private void showUploadCancelDialog() {
        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        final Intent pauseAction = new Intent(DfuService.BROADCAST_ACTION);
        pauseAction.putExtra(DfuService.EXTRA_ACTION, DfuService.ACTION_PAUSE);
        manager.sendBroadcast(pauseAction);

    }


    private boolean isDfuServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(DfuService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private class FileDownloadTask extends AsyncTask<String, Integer, Integer> {

        ProgressDialog mDlg;

        @Override
        protected Integer doInBackground(String... params) {
            String address = "http://52.78.81.0/ota/ota_download.php";

            int count = 0;
            try {
                Thread.sleep(100);
                URL url = new URL(address);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ota_file.zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            }catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDlg = new ProgressDialog(mContext);
            mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDlg.setMessage("Start");
            mDlg.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            if (values[0].equals("progress")) {
//                mDlg.setProgress(values[1]);
//                mDlg.setMessage(values[2]);
//            } else if (values[0].equals("max")) {
//                mDlg.setMax(Integer.parseInt(values[1]));
//            }
            mDlg.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mDlg.dismiss();
        }
    }



    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(final String deviceAddress) {
            mProgressBar.setIndeterminate(true);
            mTextPercentage.setText(R.string.dfu_status_connecting);
        }

        @Override
        public void onDfuProcessStarting(final String deviceAddress) {
            mProgressBar.setIndeterminate(true);
            mTextPercentage.setText(R.string.dfu_status_starting);
        }

        @Override
        public void onEnablingDfuMode(final String deviceAddress) {
            mProgressBar.setIndeterminate(true);
            mTextPercentage.setText(R.string.dfu_status_switching_to_dfu);
        }

        @Override
        public void onFirmwareValidating(final String deviceAddress) {
            mProgressBar.setIndeterminate(true);
            mTextPercentage.setText(R.string.dfu_status_validating);
        }

        @Override
        public void onDeviceDisconnecting(final String deviceAddress) {
            mProgressBar.setIndeterminate(true);
            mTextPercentage.setText(R.string.dfu_status_disconnecting);
        }

        @Override
        public void onDfuCompleted(final String deviceAddress) {
            mTextPercentage.setText(R.string.dfu_status_completed);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    onTransferCompleted();

                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);
        }

        @Override
        public void onDfuAborted(final String deviceAddress) {
            mTextPercentage.setText(R.string.dfu_status_aborted);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    onUploadCanceled();

                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);
        }

        @Override
        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
            mProgressBar.setIndeterminate(false);
            mProgressBar.setProgress(percent);
            mTextPercentage.setText(getString(R.string.dfu_uploading_percentage, percent));
            if (partsTotal > 1)
                mTextUploading.setText(getString(R.string.dfu_status_uploading_part, currentPart, partsTotal));
            else
                mTextUploading.setText(R.string.dfu_status_uploading);
        }

        @Override
        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
//            showErrorMessage(message);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

            // We have to wait a bit before canceling notification. This is called before DfuService creates the last notification.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);
        }
    };

    byte[] data2;
    boolean flags = false;
    StnEggPkt.StnEggPacket build;
    private void protocolCheck(byte[] characteristic) {
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
//            if ("SE".equals(new String(aaa))) {
//                data1 = characteristic;
//
//                return;
//            }

            if (!"\r\n".equals(new String(ddd))) {
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
                build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);

                ULog.d("idtActivity", "getSerpCommand : " + build.getSerpCommand() + " /////getMotion : " + build.getMotion() + " /////getCount : " + build.getCount() + " /////getUserInt01 : " + build.getUserInt01() + " /////getUserInt02 : " + build.getUserInt02() + " /////getUserInt04 : " + build.getUserInt04() +" /////getUserUint01 : " + build.getUserUint01() + " /////getUserUint02 : " + build.getUserUint02() + " /////getUserUint04 : " + build.getUserUint04());

            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            //20160822 end
        }

    }
}

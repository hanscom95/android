/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android;


import android.Manifest;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ble.BluetoothLeService;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.android.ble.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static com.example.android.ble.BluetoothLeService.ACTION_GATT_CONNECTED;
import static com.example.android.ble.BluetoothLeService.ACTION_GATT_DISCONNECTED;
import static com.example.android.ble.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED;
import static com.example.android.ble.BluetoothLeService.EXTRA_CHARACTER_DATA;
import static com.example.android.ble.BluetoothLeService.EXTRA_DATA;

/**
 * This activity uses a {@link android.view.TextureView} to render the frames of a video decoded using
 * {@link android.media.MediaCodec} API.
 */
public class MainActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener{

    MediaPlayer mExtractor;
    AudioManager mAudioManager;
    SurfaceView surfaceView;

    LeDeviceListAdapter mLeDeviceListAdapter;
    BluetoothLeService mBluetoothLeService;
    BluetoothAdapter mBluetoothAdapter;
    boolean mScanning;
    Handler mHandler;

    String mDeviceAddress;
    // Stops scanning after 10 seconds.
    static final long SCAN_PERIOD = 10000;

    int video = 0;

    boolean mPlayStatus = false;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        surfaceView = (SurfaceView) findViewById(R.id.videoView);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
                    Log.d("onRequestPermissionsResult", "Permission always deny");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExtractor != null ) {
            mExtractor.release();
            mExtractor = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        startPlayback();
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
            mBluetoothLeService = null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_ble) {
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
                return true;
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
                    bleDeviceControl(mDeviceAddress);
                }
            });
            //Create alert dialog object via builder
            AlertDialog alertDialogObject = dialogBuilder.create();
            //Show the dialog
            alertDialogObject.show();
            scanLeDevice(true);


        }else if(item.getItemId() == R.id.menu_next) {
            startPlayback();
        }
        return true;
    }


    public void startPlayback() {
        // Construct a URI that points to the video resource that we want to play
        try {

            if(mExtractor != null) {
                mExtractor.stop();
                mExtractor.reset();
                mExtractor = null;
            }

            mExtractor = new MediaPlayer();

            Log.d("startPlayback", "video : " +video);
            // BEGIN_INCLUDE(initialize_extractor)
            if(video == 0) {
                mExtractor.setDataSource("file:///mnt/sdcard/Movies/se_youtube1.wmv");
                video++;
            }else if(video == 1) {
                mExtractor.setDataSource("file:///mnt/sdcard/Movies/se_youtube2.wmv");
                video++;
            }else if(video == 2) {
                mExtractor.setDataSource("file:///mnt/sdcard/vr/4minute.mp4");
                video++;
            }else if(video == 3) {
                mExtractor.setDataSource("file:///mnt/sdcard/Movies/se_youtube3.wmv");
                video++;
            }else if(video == 4) {
                mExtractor.setDataSource("file:///mnt/sdcard/Movies/se_youtube4.wmv");
                video = 0;
            }

            mExtractor.setDisplay(surfaceView.getHolder());
            mExtractor.prepare();
            mExtractor.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =  new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String onlyDevice = device.getAddress().substring(0, 8);
                    Log.d("LeScanCallback", "device.getAddress() : " + device.getAddress() + " ;;;; device.getName() : " + device.getName() );
                    if ("00:0B:57".equals(onlyDevice) || "F5:6C:D6".equals(onlyDevice) || "DE:4C:0D".equals(onlyDevice)) {
                        mLeDeviceListAdapter.addDevice(device);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

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

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        video--;
        mExtractor.stop();
        mExtractor.release();
        mExtractor = null;
        startPlayback();
        return false;
    }

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
            mInflator = MainActivity.this.getLayoutInflater();
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
            MainActivity.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (MainActivity.ViewHolder) view.getTag();
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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_GATT_CONNECTED.equals(action)) {
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                mBluetoothLeService.displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] data = intent.getByteArrayExtra(EXTRA_CHARACTER_DATA);
                String spin = new String(data).substring(0,1);
                if(mExtractor == null) {
                    startPlayback();
                    return;
                }

                if(spin.equals("J")) {//stop, play
                    if(!mPlayStatus) {
                        mExtractor.pause();
                        mPlayStatus = true;
                    }else {
                        mExtractor.start();
                        mPlayStatus = false;
                    }
                }else if(spin.equals("I")) {//되 감기
                    if(mExtractor.getCurrentPosition() > 1000) {
                        mExtractor.seekTo(mExtractor.getCurrentPosition() - 1000);
                    }else {
                        mExtractor.seekTo(0);
                    }
                }else if(spin.equals("H")) {//빨리 감기
                    if(mExtractor.getCurrentPosition() + 1000 < mExtractor.getDuration()) {
                        mExtractor.seekTo(mExtractor.getCurrentPosition() + 1000);
                    }else {
                        mExtractor.seekTo(mExtractor.getDuration());
                    }
                }else if(spin.equals("C")) {// 이전 동영상
                    if(video == 0) {
                        video = 3;
                    }else if(video == 1) {
                        video = 4;
                    }else {
                        video = video - 2;
                    }
                    startPlayback();
                }else if(spin.equals("B")) {// 다음 동영상
                    startPlayback();
                }else if(spin.equals("E")) {// 볼륨 다운
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                }else if(spin.equals("D")) {// 볼륨 업
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                }


                Log.d("onCharacteristicChanged" , "video : " + video);
                Log.d("onCharacteristicChanged" , "spin : " + spin);

            } else if(EXTRA_CHARACTER_DATA.equals(action)) {
                Log.d("ACTION_DATA_AVAILABLE", "EXTRA_CHARACTER_DATA : " + intent.getStringExtra(EXTRA_DATA));
//                chartPoint();
            }
        }
    };
    //20160728 start
    private static final String HEXES = "0123456789ABCDEF";
    public static String byteArrayToHexString(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        boolean firstEntry = true;
//        sb.append('[');

        for (final byte b : array) {
//            if (!firstEntry) {
//                sb.append(", ");
//            }
            sb.append(HEXES.charAt((array[0] & 0xF0) >> 4));
            sb.append(HEXES.charAt((array[0] & 0x0F)));
            firstEntry = false;
        }

//        sb.append(']');
        return sb.toString();
    }
    //20160728 end
}

/*
 * Copyright (C) 2014 Bluetooth Connection Template
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

package com.standingegg.band.bluetooth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.standingegg.band.DeviceConnectFragment;
import com.standingegg.band.DeviceConnectFragment.FragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.service.BTCTemplateService;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.ULog;

import java.util.ArrayList;

public class DeviceScanListActivity extends Activity implements FragmentListener {
	// Debugging
	private static final String TAG = "DeviceScanListActivity";
	private static final boolean D = true;

	// Constants
	public static final long SCAN_PERIOD = 8 * 1000; // Stops scanning after a
														// pre-defined scan
														// period.

	// Return Intent extra
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	// Member fields
	private ActivityHandler mActivityHandler;
	private BluetoothAdapter mBtAdapter;
	private BleManager mBleManager;
	private BTCTemplateService mService;
	// private ArrayAdapter<String> mPairedDevicesListAdapter;
	private ArrayAdapter<String> mNewDevicesListAdapter;

	private ArrayList<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();

	Button mScanButton = null;
	private LinearLayout mMain;

	DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_connect_3);

		mMain = (LinearLayout) findViewById(R.id.device_conn_main);

		doStartService();
		setFragmentOn();

		// Set result CANCELED incase the user backs out
		setResult(Activity.RESULT_CANCELED);

		mActivityHandler = new ActivityHandler();
		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		// Get BLE Manager
		mBleManager = BleManager.getInstance(getApplicationContext(), null);
		mBleManager.setScanCallback(mLeScanCallback);
		// Initialize array adapters. One for already paired devices and
		// one for newly discovered devices
		// mPairedDevicesListAdapter = new ArrayAdapter<String>(this,
		// R.layout.adapter_device_name);
		mNewDevicesListAdapter = new ArrayAdapter<String>(this, R.layout.adapter_device_name);

		// // Find and set up the ListView for paired devices
		// ListView pairedListView = (ListView)
		// findViewById(R.id.paired_devices);
		// pairedListView.setAdapter(mPairedDevicesListAdapter);
		// pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesListAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// Initialize the button to perform device discovery
		mScanButton = (Button) findViewById(R.id.button_scan);
//		mScanButton.setVisibility(View.INVISIBLE);
		mScanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				if (mBleManager.handlerState()) {
					mNewDevicesListAdapter.clear();
					doDiscovery();
//					v.setVisibility(View.GONE);
//				}

			}
		});

	}

	FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();

	public void setFragmentOn() {
		// menu bar 슬라이드 막기
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_device_conn_1);
		Fragment fragment = new DeviceConnectFragment(getApplicationContext(), this);
		Bundle args = new Bundle();
		// args.putInt(LoginDataLoadingFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment

		fragmentManager.replace(R.id.frame2, fragment).commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery() {
		if (D)
			ULog.d(TAG, "doDiscovery()");

		// Indicate scanning in the title
		setProgressBarIndeterminateVisibility(true);
		// setTitle(R.string.scanning);

		// Empty cache
		mDevices.clear();

		// If we're already discovering, stop it
		if (mBleManager.getState() == BleManager.STATE_SCANNING) {
			mBleManager.scanLeDevice(false);
		}
		// Request discover from BluetoothAdapter
		mBleManager.scanLeDevice(true);

		// Stops scanning after a pre-defined scan period.
		
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				stopDiscovery();
			}
		}, SCAN_PERIOD);
		
//		mActivityHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				stopDiscovery();
//			}
//		}, SCAN_PERIOD);
	}

	/**
	 * Stop device discover
	 */
	private void stopDiscovery() {
		// Indicate scanning in the title
		setProgressBarIndeterminateVisibility(false);
		// setTitle(R.string.device_find);
		// Show scan button
//		mScanButton.setVisibility(View.VISIBLE);
		mBleManager.scanLeDevice(false);
	}

	/**
	 * Check if it's already cached
	 */
	private boolean checkDuplicated(BluetoothDevice device) {
		for (BluetoothDevice dvc : mDevices) {
			if (device.getAddress().equalsIgnoreCase(dvc.getAddress())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The on-click listener for all devices in the ListViews
	 */
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			mBtAdapter.cancelDiscovery();

			// Get the device MAC address, which is the last 17 chars in the
			// View
			String info = ((TextView) v).getText().toString();
			if (info != null && info.length() > 16) {
				String address = info.substring(info.length() - 17);
				ULog.d(TAG, "User selected device : " + address);

				// Create the result Intent and include the MAC address
				/*
				 * if(dbManager.isUsingDevice(address)){
				 * 
				 * //"다른 계정에서 이미 사용중인 device 입니다." alertDialog return; }
				 */

				Intent intent = new Intent(getApplicationContext(), BandConnect.class);
				// Intent intent = new
				// Intent(getApplicationContext(),MainActivity.class);
				intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

				ULog.d("address==========>" + address);

				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				mBtAdapter.stopLeScan(mLeScanCallback);
				BTCTemplateService.ble_flag = false;  
				BTCTemplateService.mBleManager = null;
				unbindService(mServiceConn);

				// Set result and finish this Activity
				// setResult(Activity.RESULT_OK, intent);
				// finish();
				
				
				
				
			}
		}
	};

	/**
	 * BLE scan callback
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			ULog.d("# Scan device rssi is " + rssi);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// if (device.getBondState() != BluetoothDevice.BOND_BONDED)
					// {
					if (!checkDuplicated(device)) {
						String onlyDevice = device.getAddress().substring(0, 8);
						//if ("80:EA:CA".equals(onlyDevice)) {
//						if ("00:0B:57".equals(onlyDevice)) {
							mNewDevicesListAdapter.add(device.getName() + "\n" + device.getAddress());
							mNewDevicesListAdapter.notifyDataSetChanged();
							mDevices.add(device);
//						}
					}
					// }
				}
			});
		}
	};

	@Override
	public void OnFragmentCallback(int msgType) {
		switch (msgType) {
		case FragmentListener.CALLBACK_BACK:
			moveTaskToBack(true);
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		case FragmentListener.CALLBACK_OK:
			// Intent intent = new Intent(getApplicationContext(),
			// DeviceScanListActivity.class);
			// startActivity(intent);
			// finish();

			Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame2);
			fragmentManager.remove(mFragment);
			findViewById(R.id.frame2).setVisibility(View.GONE);
			mMain.setVisibility(View.VISIBLE);

			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			getActionBar().setCustomView(R.layout.actionbar_device_conn_3);

		default:
			break;
		}

	}

	private ServiceConnection mServiceConn = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			ULog.d(TAG, "Activity - Service connected");

			mService = ((BTCTemplateService.ServiceBinder) binder).getService();

			// Activity couldn't work with mService until connections are made
			// So initialize parameters and settings here. Do not initialize
			// while running onCreate()
			initialize();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};

	/**
	 * Start service if it's not running
	 */
	private void doStartService() {
		ULog.d(TAG, "# Activity - doStartService()");
		startService(new Intent(this, BTCTemplateService.class));
		bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * Initialization / Finalization
	 */
	private void initialize() {
		ULog.d(TAG, "# Activity - initialize()");

		// Use this check to determine whether BLE is supported on the device.
		// Then
		// you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.bt_ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
		}

		mService.setupService(mActivityHandler);

		// If BT is not on, request that it be enabled.
		// RetroWatchService.setupBT() will then be called during
		// onActivityResult
		if (!mService.isBluetoothEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
		}
		mNewDevicesListAdapter.clear();
		
//		
//		Handler mHandler = new Handler();
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
				doDiscovery();
//			}
//		}, 3000);
	}

	@Override
	protected void onActivityResult(int request, int result, Intent Data) {
		ULog.d(TAG, "onActivityResult " + request);

		switch (request) {
		case Constants.REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (result == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a BT session
				mService.setupBLE();
			} else {
				// User did not enable Bluetooth or an error occured
				ULog.e(TAG, "BT is not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
			}
			break;
		} // End of switch(requestCode)
	}

	public class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			}
			super.handleMessage(msg);
		}
	}
}

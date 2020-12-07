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

package com.standingegg.band.service;

import com.google.protobuf.UninitializedMessageException;
import com.standingegg.band.R;
import com.standingegg.band.TransactionBuilder;
import com.standingegg.band.TransactionReceiver;
import com.standingegg.band.bluetooth.BleManager;
import com.standingegg.band.bluetooth.ConnectionInfo;
import com.standingegg.band.util.AppSettings;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.StnEggPkt;
import com.standingegg.band.util.ULog;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Date;

public class BTCTemplateService extends Service {
	private static final String TAG = "LLService";

	// Context, System
	private Context mContext = null;
	private static Handler mActivityHandler = null;
	private ServiceHandler mServiceHandler = null;
	private IBinder mBinder = null;
	public static boolean ble_flag=false;

	// Bluetooth
	private BluetoothAdapter mBluetoothAdapter = null; // local Bluetooth
														// adapter managed by
														// Android Framework
	// private BluetoothManager mBtManager = null;
	public static BleManager mBleManager = null;
	private boolean mIsBleSupported = true;
	private ConnectionInfo mConnectionInfo = null; // Remembers connection info
													// when BT connection is
													// made
	// private CommandParser mCommandParser = null;

	private TransactionBuilder mTransactionBuilder = null;
	private TransactionReceiver mTransactionReceiver = null;

	/*****************************************************
	 * Overrided methods
	 ******************************************************/
	@Override
	public void onCreate() {
		ULog.d(TAG, "# Service - onCreate() starts here");
		mServiceHandler = new ServiceHandler();
		mBinder = new ServiceBinder();
		mContext = getApplicationContext();
		initialize();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ULog.d(TAG, "# Service - onStartCommand() starts here");

		// If service returns START_STICKY, android restarts service
		// automatically after forced close.
		// At this time, onStartCommand() method in service must handle null
		// intent.
		return Service.START_STICKY;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// This prevents reload after configuration changes
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public IBinder onBind(Intent intent) {
		ULog.d(TAG, "# Service - onBind()");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		ULog.d(TAG, "# Service - onUnbind()");
		return true;
	}

	@Override
	public void onDestroy() {
		ULog.d(TAG, "# Service - onDestroy()");
		finalizeService();
	}

	@Override
	public void onLowMemory() {
		ULog.d(TAG, "# Service - onLowMemory()");
		// onDestroy is not always called when applications are finished by
		// Android system.
		finalizeService();
	}

	/*****************************************************
	 * Private methods
	 ******************************************************/
	private void initialize() {
		ULog.d(TAG, "# Service : initialize ---");

		AppSettings.initializeAppSettings(mContext);
		startServiceMonitoring();

		// Use this check to determine whether BLE is supported on the device.
		// Then
		// you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.bt_ble_not_supported, Toast.LENGTH_SHORT).show();
			mIsBleSupported = false;
		}

		// Make instances
		mConnectionInfo = ConnectionInfo.getInstance(mContext);
		// mCommandParser = new CommandParser();

		// Get local Bluetooth adapter
		if (mBluetoothAdapter == null)
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			return;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			// BT is not on, need to turn on manually.
			// Activity will do this.
		} else {
			if (mBleManager == null && mIsBleSupported) {
				setupBLE(); // mServiceHandler null......
			}
		}
	}

	/**
	 * Send message to device.
	 * 
	 * @param message
	 *            message to send
	 */
	private void sendMessageToDevice(String message) {
		if (message == null || message.length() < 1)
			return;

		TransactionBuilder.Transaction transaction = mTransactionBuilder.makeTransaction();
		transaction.begin();
		transaction.setMessage(message);
		transaction.settingFinished();
		transaction.sendTransaction();
	}
	/**
	 * Send message to device.
	 *
	 * @param message
	 *            message to send
	 */
	private void sendMessageToDevice(byte[] message) {
		if (message == null || message.length < 1)
			return;

		TransactionBuilder.Transaction transaction = mTransactionBuilder.makeTransaction();
		transaction.begin();
		transaction.setMessage(message);
//		transaction.settingFinished();
		transaction.sendTransaction();
	}


	/**
	 * 
	 */

	/*****************************************************
	 * Public methods
	 ******************************************************/
	public void finalizeService() {
		ULog.d(TAG, "# Service : finalize ---");

		// Stop the bluetooth session
		mBluetoothAdapter = null;
		if (mBleManager != null) {
			mBleManager.finalize();
		}
		mBleManager = null;
	}

	/**
	 * Setting up bluetooth connection
	 * 
	 * @param h
	 */
	public void setupService(Handler h) {
		mActivityHandler = h;

		// Double check BT manager instance
		if (mBleManager == null)
			setupBLE();

		// Initialize transaction builder & receiver
		if (mTransactionBuilder == null)
			mTransactionBuilder = new TransactionBuilder(mBleManager, mActivityHandler);
		if (mTransactionReceiver == null)
			mTransactionReceiver = new TransactionReceiver(mActivityHandler);

		// TODO: If ConnectionInfo holds previous connection info,
		// try to connect using it.
		if (mConnectionInfo.getDeviceAddress() != null && ble_flag) {
//			ULog.v("default device address :" + mConnectionInfo.getDeviceAddress());
			connectDevice(mConnectionInfo.getDeviceAddress()); // BTCTemplate
		} else {
			if (mBleManager.getState() == BleManager.STATE_NONE) {
				// Do nothing
			}
		}
	}

	/**
	 * Setup and initialize BLE manager
	 */
	public void setupBLE() {
		ULog.d(TAG, "Service - setupBLE()");

		// Initialize the BluetoothManager to perform bluetooth le scanning
		if (mBleManager == null){
			ULog.d(TAG, "Service - setupBLE()2");
			ULog.d(TAG, "Service - setupBLE()2mServiceHandler" + mServiceHandler);
			mBleManager = BleManager.getInstance(mContext, mServiceHandler);
			ble_flag = true;
		}
	}

	/**
	 * Check bluetooth is enabled or not.
	 */
	public boolean isBluetoothEnabled() {
		if (mBluetoothAdapter == null) {
			ULog.e(TAG, "# Service - cannot find bluetooth adapter. Restart app.");
			return false;
		}
		return mBluetoothAdapter.isEnabled();
	}

	/**
	 * Get scan mode
	 */
	public int getBluetoothScanMode() {
		int scanMode = -1;
		if (mBluetoothAdapter != null)
			scanMode = mBluetoothAdapter.getScanMode();

		return scanMode;
	}

	/**
	 * Connect to a remote device.
	 * 
	 * @param address
	 *            The BluetoothDevice to connect
	 */
	public void connectDevice(String address) {
		if (address != null && mBleManager != null) {
			// mBleManager.disconnect();
			
			
//			mServiceHandler= new ServiceHandler();
			ULog.v("mServiceHandler", "mServiceHandler:" + mServiceHandler);
			mBleManager = BleManager.getInstance(mContext, mServiceHandler);
			ULog.v("mServiceHandler", "mBleManager.mHandler:" + mBleManager.mHandler);
			if (mBleManager.connectGatt(mContext, true, address,mServiceHandler)) {
				try {
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
					mConnectionInfo.setDeviceAddress(address);
					mConnectionInfo.setDeviceName(device.getName());
				}catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Connect to a remote device.
	 * 
	 * @param device
	 *            The BluetoothDevice to connect
	 */
	public void connectDevice(BluetoothDevice device) {
		if (device != null && mBleManager != null) {
			// mBleManager.disconnect();
			mBleManager = BleManager.getInstance(mContext, mServiceHandler);
			if (mBleManager.connectGatt(mContext, true, device.getAddress())) {
				mConnectionInfo.setDeviceAddress(device.getAddress());
				mConnectionInfo.setDeviceName(device.getName());
			}
		}
	}

	// public void disConnect() {
	// if(mBleManager != null) {
	//// mBleManager.disconnect();
	// }
	// }

	/**
	 * Get connected device name
	 */
	public String getDeviceName() {
		return mConnectionInfo.getDeviceName();
	}

	/**
	 * Send message to remote device using Bluetooth
	 */
	public void sendMessageToRemote(String message) {
		sendMessageToDevice(message+"\r\n");
	}
	/**
	 * Send message to remote device using Bluetooth
	 * @param message
	 */
	public void sendMessageToRemote(byte[] message) {
		sendMessageToDevice(message);
	}
	
//	public void sendAlarmsMessage(String message) {
//		sendMessageToDevice(message);
//	}

	/**
	 * Start service monitoring. Service monitoring prevents unintended close of
	 * service.
	 */
	public void startServiceMonitoring() {
		if (AppSettings.getBgService()) {
			ServiceMonitoring.startMonitoring(mContext);
		} else {
			ServiceMonitoring.stopMonitoring(mContext);
		}
	}

	/*****************************************************
	 * Handler, Listener, Timer, Sub classes
	 ******************************************************/
	public class ServiceBinder extends Binder {
		public BTCTemplateService getService() {
			return BTCTemplateService.this;
		}
	}

	/**
	 * Receives messages from bluetooth manager
	 */
	class ServiceHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {


			/*if(msg.what > 1) {
				try {
					ULog.d(TAG, "Service - handleMessage: ====================== : " + msg.what);
					StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
					builder.setSerpCommand(0);

					byte[] encoded = new byte[builder.build().toByteArray().length + 5];
					byte[] builderTobyte = builder.build().toByteArray();

					encoded[0] = 'S';
					encoded[1] = 'E';
					int i;
					for (i = 0; i < builderTobyte.length; i++) {
						Log.d(TAG, "protocolBuffer encoded: " + builderTobyte[i]);

						encoded[i + 2] = builderTobyte[i];
					}
					int lastBuffer = builder.build().toByteArray().length + 5;
					encoded[i + 2] = 'E';
					encoded[i + 3] = '\r';
					encoded[i + 4] = '\n';

					sendMessageToRemote(encoded);
				} catch (UninitializedMessageException e) {
					e.printStackTrace();
				}
			}*/



			switch (msg.what) {
			// Bluetooth state changed
			case BleManager.MESSAGE_STATE_CHANGE:
				// Bluetooth state Changed
				ULog.d(TAG, "Service - MESSAGE_STATE_CHANGE: " + msg.arg1);

				switch (msg.arg1) {

				// MESSAGE_BT_STATE_LISTENING는 어디로?

				case BleManager.STATE_NONE:
					mActivityHandler.obtainMessage(Constants.MESSAGE_BT_STATE_INITIALIZED).sendToTarget();
					break;

				case BleManager.STATE_CONNECTING:
					mActivityHandler.obtainMessage(Constants.MESSAGE_BT_STATE_CONNECTING).sendToTarget();
					break;

				case BleManager.STATE_CONNECTED:
					ULog.e(TAG, "MESSAGE_BT_STATE_CONNECTED/STATE_CONNECTED");
					mActivityHandler.obtainMessage(Constants.MESSAGE_BT_STATE_CONNECTED).sendToTarget();
					break;

				case BleManager.STATE_IDLE:
					mActivityHandler.obtainMessage(Constants.MESSAGE_BT_STATE_INITIALIZED).sendToTarget();
					break;
				}
				break;

			// If you want to send data to remote
			case BleManager.MESSAGE_WRITE:
				// ULog.d(TAG, "Service - MESSAGE_WRITE: ");
				String message = (String) msg.obj;
				// if(message != null && message.length() > 0)
				// sendMessageToDevice(message);

				ULog.e("MSG", message);
				mActivityHandler.obtainMessage(Constants.MESSAGE_WRITE_CHAT_DATA, message).sendToTarget();

				break;

			// Received packets from remote
			case BleManager.MESSAGE_READ:
				ULog.d(TAG, "Service - MESSAGE_READ: ");

				/*try {
					ULog.d(TAG, "Service - handleMessage: ====================== : " + msg.what);
					StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
					builder.setSerpCommand(0);

					byte[] encoded = new byte[builder.build().toByteArray().length + 5];
					byte[] builderTobyte = builder.build().toByteArray();

					encoded[0] = 'S';
					encoded[1] = 'E';
					int i;
					for (i = 0; i < builderTobyte.length; i++) {
						encoded[i + 2] = builderTobyte[i];
					}
					int lastBuffer = builder.build().toByteArray().length + 5;
					encoded[i + 2] = 'E';
					encoded[i + 3] = '\r';
					encoded[i + 4] = '\n';

					sendMessageToRemote(encoded);
				} catch (UninitializedMessageException e) {
					e.printStackTrace();
				}*/

				/*if(msg.obj instanceof byte[]) {
					ULog.d(TAG, "msg.obj:true");
					byte[] byteMsg = (byte[]) msg.obj;
					sendMessageToRemote(byteMsg);
				}*/

				if(msg.obj instanceof StnEggPkt.StnEggPacket) {
					if(msg.obj != null) {
						mActivityHandler.obtainMessage(Constants.MESSAGE_READ_CHAT_DATA, msg.obj).sendToTarget();
					}
				}



				if(msg.obj instanceof String) {
					String strMsg = (String) msg.obj;
					ULog.e("READ MSG", strMsg);

					int readCount = strMsg.length();
					// send bytes in the buffer to activity
					if (strMsg != null && strMsg.length() > 0) {

	//					if(mFullMsg!="" || mFullMsg.length()!=0){
	//						mFullMsg = mFullMsg + strMsg;
	//						mActivityHandler.obtainMessage(Constants.MESSAGE_READ_CHAT_DATA, mFullMsg).sendToTarget();
	//						mFullMsg = "";
	//
	//						return;
	//					}
	//
	//
	//					if(strMsg.substring(strMsg.length()-4) != "\r\n" ){
	//						mFullMsg = mFullMsg + strMsg;
	//					}else{
							mActivityHandler.obtainMessage(Constants.MESSAGE_READ_CHAT_DATA, strMsg).sendToTarget();
							/*
							 * int command = mCommandParser.setString(strMsg);
							 * if(command == CommandParser.COMMAND_THINGSPEAK) {
							 *
							 * //PKSTRYMDT
							 * sendMessageToRemote("PKSTY2015M05D28H13M45S12");
							 * //PKSTRYMDT
							 *
							 *
							 * String parameters = mCommandParser.getParameterString();
							 * StringBuilder requestUrl = new
							 * StringBuilder("http://184.106.153.149/update?");
							 * if(parameters != null && parameters.length() > 0)
							 * requestUrl.append(parameters);
							 *
							 * //ULog.d("# Find thingspeak command. URL = "+requestUrl);
							 *
							 * HttpAsyncTask task = new HttpAsyncTask(mHTTPListener, 0,
							 * requestUrl.toString(), HttpInterface.REQUEST_TYPE_GET);
							 * task.execute(); mCommandParser.resetParser(); }
							 */
	//					}
					}


				}
				break;

			case BleManager.MESSAGE_DEVICE_NAME:
				ULog.d(TAG, "Service - MESSAGE_DEVICE_NAME: ");

				// save connected device's name and notify using toast
				String deviceAddress = msg.getData().getString(Constants.SERVICE_HANDLER_MSG_KEY_DEVICE_ADDRESS);
				String deviceName = msg.getData().getString(Constants.SERVICE_HANDLER_MSG_KEY_DEVICE_NAME);

				if (deviceName != null && deviceAddress != null) {
					// Remember device's address and name
					mConnectionInfo.setDeviceAddress(deviceAddress);
					mConnectionInfo.setDeviceName(deviceName);

					Toast.makeText(getApplicationContext(), "Connected to " + deviceName, Toast.LENGTH_SHORT).show();
				}
				break;

			case BleManager.MESSAGE_TOAST:
				ULog.d(TAG, "Service - MESSAGE_TOAST: ");

				Toast.makeText(getApplicationContext(),
						msg.getData().getString(Constants.SERVICE_HANDLER_MSG_KEY_TOAST), Toast.LENGTH_SHORT).show();
				break;

			} // End of switch(msg.what)

			super.handleMessage(msg);
		}
	} // End of class MainHandler
	
	String mFullMsg = "";
	
	@SuppressLint("Instantiatable")
	public class PhoneStateReceiver extends BroadcastReceiver {
		private int pState = TelephonyManager.CALL_STATE_IDLE;

		public void onReceive(Context context, final Intent intent) {
			TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			telManager.listen(new PhoneStateListener() {
				public void onCallStateChanged(int state, String incomingNumber) {
					if (state != pState) {
						if (state == TelephonyManager.CALL_STATE_IDLE) {
							ULog.i("Phone", "IDLE");
							// if (cLog != null) {
							// cLog.setEndDate(System.currentTimeMillis());
							// Log.i("Phone", cLog.toString());
							// cLog = null;
							// }
						} else if (state == TelephonyManager.CALL_STATE_RINGING) {
							ULog.i("Phone", "RINGING");
							try {
								sendMessageToRemote("VIB");
							} catch (Exception e) {
								// TODO: handle exception
							}

							// cLog = new CallLog(incomingNumber,
							// LogKind.KIND_RECEIVE);
							// cLog.setRingingDate(System.currentTimeMillis());
						} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
							ULog.i("Phone", "OFFHOOK");
							// cLog.setStartDate(System.currentTimeMillis());
						}

						pState = state;
					}
				}
			}, PhoneStateListener.LISTEN_CALL_STATE);

			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				ULog.i("Phone", "out");
				// cLog = new
				// CallLog(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER),
				// LogKind.KIND_SEND);
				// cLog.setRingingDate(System.currentTimeMillis());
			}
		}
	}

	public class SMSReceiver extends BroadcastReceiver {
		static final String logTag = "SmsReceiver";
		static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION)) {
				// Bundel 널 체크
				Bundle bundle = intent.getExtras();
				if (bundle == null) {
					return;
				}

				// pdu 객체 널 체크
				Object[] pdusObj = (Object[]) bundle.get("pdus");
				if (pdusObj == null) {
					return;
				}

				try {
					// message 처리
					SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
					for (int i = 0; i < pdusObj.length; i++) {
						smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
						ULog.e(logTag, "NEW SMS " + i + "th");
						ULog.e(logTag, "DisplayOriginatingAddress : " + smsMessages[i].getDisplayOriginatingAddress());
						ULog.e(logTag, "DisplayMessageBody : " + smsMessages[i].getDisplayMessageBody());
						ULog.e(logTag, "EmailBody : " + smsMessages[i].getEmailBody());
						ULog.e(logTag, "EmailFrom : " + smsMessages[i].getEmailFrom());
						ULog.e(logTag, "OriginatingAddress : " + smsMessages[i].getOriginatingAddress());
						ULog.e(logTag, "MessageBody : " + smsMessages[i].getMessageBody());
						ULog.e(logTag, "ServiceCenterAddress : " + smsMessages[i].getServiceCenterAddress());
						ULog.e(logTag, "TimestampMillis : " + smsMessages[i].getTimestampMillis());

						Toast.makeText(context, smsMessages[i].getMessageBody(), 0).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}

}

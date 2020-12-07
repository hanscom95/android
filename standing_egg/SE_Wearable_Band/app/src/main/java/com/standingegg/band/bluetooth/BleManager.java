package com.standingegg.band.bluetooth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.google.protobuf.InvalidProtocolBufferException;
import com.standingegg.band.ota.Statics;
import com.standingegg.band.util.StnEggPkt;
import com.standingegg.band.util.ULog;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BleManager {

	// Debugging
	private static final String TAG = "BleManager";

	// Constants that indicate the current connection state
	public static final int STATE_ERROR = -1;
	public static final int STATE_NONE = 0; // Initialized
	public static final int STATE_IDLE = 1; // Not connected
	public static final int STATE_SCANNING = 2; // Scanning
	public static final int STATE_CONNECTING = 13; // Connecting
	public static final int STATE_CONNECTED = 16; // Connected

	// Message types sent from the BluetoothManager to Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;


	// Message types sent from the BluetoothManager to Handler
	public static final int MESSAGE_DATE_TIME_REQUEST = 10;
	public static final int MESSAGE_DATE_TIME_CONTENT = 11;
	public static final int MESSAGE_DATE_TIME_RECEIVED = 12;

	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_REQUEST = 13;
	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_ACCEPT = 14;
	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_CONTENT = 15;
	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_RECEIVED = 16;

	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_REQUEST = 19;
	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_ACCEPT = 20;
	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT = 21;
	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_RECEIVED = 22
			;
	public static final int MESSAGE_REAL_TIME_ACTIVITY_REPORT_CONTENT = 23;

	public static final int MESSAGE_DATA_COMPUTATION_HOLD_REQUEST = 24;
	public static final int MESSAGE_DATA_COMPUTATION_HOLD_ACCEPT = 25;

	public static final int MESSAGE_DATA_COMPUTATION_RESUME_REQUEST = 26;
	public static final int MESSAGE_DATA_COMPUTATION_RESUME_ACCEPT = 27;

	public static final int MESSAGE_VIBRATION_REQUEST = 28;
	public static final int MESSAGE_VIBRATION_ACCEPT = 29;

	public static final int MESSAGE_BAND_MODE_REQUEST = 45;
	public static final int MESSAGE_BAND_MODE_ACCEPT = 46;

	public static final int MESSAGE_HEART_RATE_REQUEST = 43;
	public static final int MESSAGE_HEART_RATE_ACCEPT = 44;

	public static final int MESSAGE_BATTERY_REQUEST = 49;
	public static final int MESSAGE_BATTERY_CONTENT = 50;

	public static final int MESSAGE_SLEEP_STATUS_CONTENT = 47;
	public static final int MESSAGE_SLEEP_STATUS_RECEIVED = 48;

	public static final int MESSAGE_GENERAL_DATA_CONTENT = 34;

	public static final long SCAN_PERIOD = 5 * 1000; // Stops scanning after a
														// pre-defined scan
														// period.
	public static final long SCAN_INTERVAL = 5 * 60 * 1000;

	// System, Management
	private static Context mContext = null;
	private static BleManager mBleManager = null; // Singleton pattern
	public static Handler mHandler;

	// Bluetooth
	private final BluetoothAdapter mBluetoothAdapter;
	private BluetoothAdapter.LeScanCallback mLeScanCallback = null;

	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	private BluetoothDevice mDefaultDevice = null;

	private BluetoothGatt mBluetoothGatt = null;

	private ArrayList<BluetoothGattService> mGattServices = new ArrayList<BluetoothGattService>();
	private BluetoothGattService mDefaultService = null;
	private ArrayList<BluetoothGattCharacteristic> mGattCharacteristics = new ArrayList<BluetoothGattCharacteristic>();
	private ArrayList<BluetoothGattCharacteristic> mWritableCharacteristics = new ArrayList<BluetoothGattCharacteristic>();
	private BluetoothGattCharacteristic mDefaultChar = null;

	// Parameters
	private int mState = -1;

	//standing-egg packet
	private StnEggPkt.StnEggPacket build;
	int protocolBufferLen = 0;

	byte[] data2;
	boolean flags = false;

	/**
	 * Constructor. Prepares a new Bluetooth session.
	 * 
	 * @param context
	 *            The UI Activity Context
	 * @param h
	 *            A Listener to receive messages back to the UI Activity
	 */
	private BleManager(Context context, Handler h) {
		ULog.d("#BLEManager BleManager ");
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		mHandler = h;
		mContext = context;

		ULog.v("BleManager Handler","BleManager Handler:" + mHandler);
		if (mContext == null)
			return;
		if (mHandler == null)
			return;
	}

	public synchronized static BleManager getInstance(Context c, Handler h) {
		ULog.d("#BLEManager getInstance ");
		
		
		if (mBleManager == null)
			mBleManager = new BleManager(c, h);

		return mBleManager;
	}

	public synchronized void finalize() {
		// Make sure we're not doing discovery anymore
		if (mBluetoothAdapter != null) {
			mState = STATE_IDLE;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			disconnect();
		}

		mDefaultDevice = null;
		mBluetoothGatt = null;
		mDefaultService = null;
		mGattServices.clear();
		mGattCharacteristics.clear();
		mWritableCharacteristics.clear();

		if (mContext == null)
			return;

		// Don't forget this!!
		// Unregister broadcast listeners
		// mContext.unregisterReceiver(mReceiver);
	}

	/*****************************************************
	 * Private methods
	 ******************************************************/

	/**
	 * This method extracts UUIDs from advertised data Because Android native
	 * code has bugs in parsing 128bit UUID use this method instead.
	 */

	private void stopScanning() {
		if (mState < STATE_CONNECTING) {
			mState = STATE_IDLE;
//			mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_IDLE, 0).sendToTarget();
		}
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
	}

	/**
	 * Check services and looking for writable characteristics
	 */
	private int checkGattServices(List<BluetoothGattService> gattServices) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			ULog.d("# BluetoothAdapter not initialized");
			return -1;
		}

		if(true) {
			if (gattServices == null) return 0;
			ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

			// Loops through available GATT Services.
			for (BluetoothGattService gattService : gattServices) {
				// Remember service
				mGattServices.add(gattService);

				List<BluetoothGattCharacteristic> gattCharacteristics =
						gattService.getCharacteristics();
				ArrayList<BluetoothGattCharacteristic> charas =
						new ArrayList<BluetoothGattCharacteristic>();

				Log.d("gattCharacteristics", "number : " + gattCharacteristics.size());
				for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
					charas.add(gattCharacteristic);
				}
				mGattCharacteristics.add(charas);
			}


			// auto click build
			if (mGattCharacteristics != null) {
				BluetoothGattCharacteristic characteristic;
//            characteristic = mGattCharacteristics.get(2).get(0);
//            Log.d("BluetoothLeService", "mGattCharacteristics uuid : " + characteristic.getUuid());
				characteristic = mGattCharacteristics.get(1).get(0);
				final int charaProp = characteristic.getProperties();
            /*if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    setCharacteristicNotification(mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                readCharacteristic(characteristic);
            }*/
				if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
					setCharacteristicNotification(characteristic, true);
				}


				mHandler.obtainMessage(MESSAGE_READ, "test").sendToTarget();
				return 1;
			}
		}

		for (BluetoothGattService gattService : gattServices) {
			// Default service info
			ULog.d("# GATT Service: " + gattService.toString());

			// Remember service
			mGattServices.add(gattService);

			// Extract characteristics
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				gattCharacteristic.getProperties();
				// Remember characteristic
				mGattCharacteristics.add(gattCharacteristic);
				ULog.d("# GATT Char: " + gattCharacteristic.toString());

				boolean isWritable = isWritableCharacteristic(gattCharacteristic);
				if (isWritable) {
					ULog.d("# GATT isWritable: " + gattCharacteristic.getUuid());
					mWritableCharacteristics.add(gattCharacteristic);
				}

				boolean isReadable = isReadableCharacteristic(gattCharacteristic);
				if (isReadable) {
					ULog.d("isReadable", "getUUID : " + gattCharacteristic.getUuid());
						readCharacteristic(gattCharacteristic);
				}

				if (isNotificationCharacteristic(gattCharacteristic)) {
					ULog.d("isNotificationCharacteristic", "gattCharacteristic.getUUID : " + gattCharacteristic.getUuid());
					ULog.d("isNotificationCharacteristic", "getDescriptors.getUUID : " + gattCharacteristic.getDescriptors().get(0).getUuid());
					setCharacteristicNotification(gattCharacteristic, true);
//					BluetoothGattDescriptor descriptor =gattCharacteristic.getDescriptor(
//							Statics.SPOTA_DESCRIPTOR_UUID);
//					//gattCharacteristic.getDescriptors().get(0);
//					descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//					mBluetoothGatt.writeDescriptor(descriptor);
					if (isWritable && isReadable) {
						mDefaultChar = gattCharacteristic;
					}
				}
			}
		}

		return mWritableCharacteristics.size();
	}

	private boolean isWritableCharacteristic(BluetoothGattCharacteristic chr) {
		if (chr == null)
			return false;

		final int charaProp = chr.getProperties();
		if (((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE)
				| (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0) {
			ULog.d("# Found writable characteristic");
			return true;
		} else {
			ULog.d("# Not writable characteristic");
			return false;
		}
	}

	private boolean isReadableCharacteristic(BluetoothGattCharacteristic chr) {
		if (chr == null)
			return false;

		final int charaProp = chr.getProperties();
		if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
			ULog.d("# Found readable characteristic");
			return true;
		} else {
			ULog.d("# Not readable characteristic");
			return false;
		}
	}

	private boolean isNotificationCharacteristic(BluetoothGattCharacteristic chr) {
		if (chr == null)
			return false;

		final int charaProp = chr.getProperties();
		if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
			ULog.d("# Found notification characteristic");
			return true;
		} else {
			ULog.d("# Not notification characteristic");
			return false;
		}
	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 *
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			ULog.d("# BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 *
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			ULog.d("# BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

		Log.w(TAG, "BluetoothAdapter not initialized : setCharacteristicNotification");
		// This is specific to Heart Rate Measurement.
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
					UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}


		mDescriptors = new ArrayList<BluetoothGattDescriptor>(characteristic.getDescriptors());
		iterDescriptor = mDescriptors.iterator();
		writeNextDescriptor(characteristic);

//		Log.d("setCharacteristicNotification", "characteristic.getUuid() : " +characteristic.getUuid());
//		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//		descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//		mBluetoothGatt.writeDescriptor(descriptor);
	}

	private List<BluetoothGattDescriptor> mDescriptors;
	private Iterator<BluetoothGattDescriptor> iterDescriptor;
	private BluetoothGattDescriptor lastDescriptor;

	private static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
	private static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
	public final static UUID UUID_HEART_RATE_MEASUREMENT =
			UUID.fromString(HEART_RATE_MEASUREMENT);


	// Writes next descriptor in order to enable notification or indication
	protected void writeNextDescriptor(BluetoothGattCharacteristic characteristic) {
		if (iterDescriptor.hasNext()) {
			lastDescriptor = iterDescriptor.next();
			if(mGattCharacteristics.size() < 3) {
				Log.d("BluetoothLeService", "여기들옴 1 : " + characteristic.getUuid());
				if (lastDescriptor.getCharacteristic() == characteristic) {
					lastDescriptor.setValue(isSetProperty(characteristic
							.getProperties()) ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
							: BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
					//mBluetoothLeService.writeDescriptor(mDevice, lastDescriptor);
					mBluetoothGatt.writeDescriptor(lastDescriptor);
				}
			}else {
				if (lastDescriptor.getCharacteristic() == characteristic) {
					lastDescriptor.setValue(isSetProperty(characteristic
							.getProperties()) ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
							: BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
					//mBluetoothLeService.writeDescriptor(mDevice, lastDescriptor);
					mBluetoothGatt.writeDescriptor(lastDescriptor);
				}
			}
		}
	}


	// Checks if given property is set
	private static boolean isSetProperty(int properties) {
		boolean isSet = ((properties >> (4)) & 1) != 0;
		return isSet;
	}

	/*****************************************************
	 * Public methods
	 ******************************************************/

	public void setScanCallback(BluetoothAdapter.LeScanCallback cb) {
		mLeScanCallback = cb;
	}

	public int getState() {
		return mState;
	}

	public boolean handlerState() {
		if (mHandler == null)
			return false;
		else
			return true;
	}

	public boolean scanLeDevice(final boolean enable) {
		boolean isScanStarted = false;
		if (enable) {
			if (mState == STATE_SCANNING)
				return false;

			if (mBluetoothAdapter.startLeScan(mLeScanCallback)) {
				mState = STATE_SCANNING;
				mDeviceList.clear();

				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						stopScanning();
					}
				}, SCAN_PERIOD);
				
				
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						stopScanning();
//					}
//				}, SCAN_PERIOD);
//
//				mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_SCANNING, 0).sendToTarget();
				isScanStarted = true;
				// If you want to scan for only specific types of peripherals
				// call below function instead
				// startLeScan(UUID[], BluetoothAdapter.LeScanCallback);

				// Stops scanning after a pre-defined scan period.

			}
		} else {
			if (mState < STATE_CONNECTING) {
				mState = STATE_IDLE;
//				mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_IDLE, 0).sendToTarget();
			}
			stopScanning();

		}

		return isScanStarted;
	}

	public boolean connectGatt(Context c, boolean bAutoReconnect, BluetoothDevice device) {
		if (c == null || device == null)
			return false;

		mGattServices.clear();
		mGattCharacteristics.clear();
		mWritableCharacteristics.clear();

		mBluetoothGatt = device.connectGatt(c, bAutoReconnect, mGattCallback);
		mDefaultDevice = device;

		mState = STATE_CONNECTING;
		mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_CONNECTING, 0).sendToTarget();
		return true;
	}

	public boolean connectGatt(Context c, boolean bAutoReconnect, String address) {
		if (c == null || address == null)
			return false;

		if (mBluetoothGatt != null && mDefaultDevice != null && address.equals(mDefaultDevice.getAddress())) {
			if (mBluetoothGatt.connect()) {
				mState = STATE_CONNECTING;
				return true;
			}
		}

		BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
		if (device == null) {
			ULog.d("# Device not found.  Unable to connect.");
			return false;
		}

		mGattServices.clear();
		mGattCharacteristics.clear();
		mWritableCharacteristics.clear();

		mBluetoothGatt = device.connectGatt(c, bAutoReconnect, mGattCallback);
		mDefaultDevice = device;

		mState = STATE_CONNECTING;
		
		
		ULog.v("BleManager Handler2","BleManager Handler2:" + mHandler);
		mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_CONNECTING, 0).sendToTarget();
		
		return true;
	}
	public boolean connectGatt(Context c, boolean bAutoReconnect, String address, Handler h) {
		if (c == null || address == null)
			return false;

		if (mBluetoothGatt != null && mDefaultDevice != null && address.equals(mDefaultDevice.getAddress())) {
			if (mBluetoothGatt.connect()) {
				mState = STATE_CONNECTING;
				return true;
			}
		}

		BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
		if (device == null) {
			ULog.d("# Device not found.  Unable to connect.");
			return false;
		}


		//20161114 추가
		mDescriptors = new ArrayList<BluetoothGattDescriptor>();

		mGattServices.clear();
		mGattCharacteristics.clear();
		mWritableCharacteristics.clear();

		ULog.e("sdk version :"+ android.os.Build.VERSION.SDK_INT);
		
		
		if (android.os.Build.VERSION.SDK_INT > 20) {
			// Little hack with reflect to use the connect gatt with defined transport in Lollipop
            Method connectGattMethod = null;

            try {
                connectGattMethod = device.getClass().getMethod("connectGatt", Context.class, boolean.class, BluetoothGattCallback.class, int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                mBluetoothGatt = (BluetoothGatt) connectGattMethod.invoke(device, c, false, mGattCallback, 2);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else  {
        	mBluetoothGatt = device.connectGatt(c, bAutoReconnect, mGattCallback);
        }
		
		mDefaultDevice = device;

		mState = STATE_CONNECTING;
		
		mHandler = h;
		ULog.v("BleManager Handler2","BleManager Handler2:" + mHandler);
		mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_CONNECTING, 0).sendToTarget();
		
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			ULog.d("# BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt.disconnect();
	}

	public boolean write(BluetoothGattCharacteristic chr, byte[] data) {
		if (mBluetoothGatt == null) {
			ULog.d(TAG, "# BluetoothGatt not initialized");
			return false;
		}

		ULog.d(TAG, "# BluetoothGatt write init===" + mState);
		BluetoothGattCharacteristic writableChar = null;

		ULog.d("write", "mWritableCharacteristics.size : " + mWritableCharacteristics.size());
		ULog.d("write", "mBluetoothGatt.getDevice : " + mBluetoothGatt.getDevice());
		ULog.d("write", "mBluetoothGatt.getServices : " + mBluetoothGatt.getServices().size());
		writableChar = mGattServices.get(1).getCharacteristics().get(0);
		//setCharacteristicNotification(writableChar, true);
		ULog.d("write", "writableChar.getUuid : " + writableChar.getUuid());
		/*
		 * if(chr == null) { if(mDefaultChar == null) {
		 * for(BluetoothGattCharacteristic bgc : mWritableCharacteristics) {
		 * if(isWritableCharacteristic(bgc)) { writableChar = bgc; } }
		 * if(writableChar == null) { ULog.d(TAG,
		 * "# Write failed - No available characteristic"); return false; } }
		 * else { if(isWritableCharacteristic(mDefaultChar)) { ULog.d(
		 * "# Default GattCharacteristic is PROPERY_WRITE | PROPERTY_WRITE_NO_RESPONSE"
		 * ); writableChar = mDefaultChar; } else { ULog.d(
		 * "# Default GattCharacteristic is not writable"); mDefaultChar = null;
		 * return false; } } } else { if (isWritableCharacteristic(chr)) {
		 * ULog.d(
		 * "# user GattCharacteristic is PROPERY_WRITE | PROPERTY_WRITE_NO_RESPONSE"
		 * ); writableChar = chr; } else { ULog.d(
		 * "# user GattCharacteristic is not writable"); return false; } }
		 */

//		writableChar.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
		writableChar.setValue(data);

		if (mBluetoothGatt == null) {
			ULog.e(TAG, " mBluetoothGatt null ");
		}

		mBluetoothGatt.writeCharacteristic(writableChar);
		mDefaultChar = writableChar;
		return true;
	}

	public ArrayList<BluetoothGattService> getServices() {
		return mGattServices;
	}

	public ArrayList<BluetoothGattCharacteristic> getCharacteristics() {
		return mGattCharacteristics;
	}

	public ArrayList<BluetoothGattCharacteristic> getWritableCharacteristics() {
		return mWritableCharacteristics;
	}

	/*****************************************************
	 * Handler, Listener, Timer, Sub classes
	 ******************************************************/

	// Various callback methods defined by the BLE API.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mState = STATE_CONNECTED;
				ULog.d(TAG, "# Connected to GATT server.");
				mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_CONNECTED, 0).sendToTarget();

				gatt.discoverServices();

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				mState = STATE_IDLE;
				ULog.d(TAG, "# Disconnected from GATT server.");
				mHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_IDLE, 0).sendToTarget();
				mBluetoothGatt = null;
				mGattServices.clear();
				mDefaultService = null;
				mGattCharacteristics.clear();
				mWritableCharacteristics.clear();
				mDefaultChar = null;
				mDefaultDevice = null;
			}
		}

		@Override
		// New services discovered
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				ULog.d(TAG, "# New GATT service discovered.");
				checkGattServices(gatt.getServices());
			} else {
				ULog.d(TAG, "# onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			ULog.d(TAG, "# Write characteristic status: " + status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				final byte[] data = characteristic.getValue();
				mHandler.obtainMessage(MESSAGE_WRITE, new String(data)).sendToTarget();
//				mHandler.obtainMessage(MESSAGE_WRITE, characteristic.getValue()).sendToTarget();
			}

		};


		// Called when descriptor was written
		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			ULog.i("BLE service", "onDescriptorWrite - status: " + status + "  - UUID: " + descriptor.getUuid());
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		// Result of a characteristic read operation
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			ULog.d(TAG, "# Read characteristic status : " + status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				// We've received data from remote
				ULog.d(TAG, "# Read characteristic 20160817: " + characteristic.getUuid());
				byte[] data = characteristic.getValue();

				if(false) {
					if (data != null && data.length > 0) {
						final StringBuilder stringBuilder = new StringBuilder(data.length);
						stringBuilder.append(data);
						;
						int contentStart = 0, contentEnd = 0;

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

						if (contentEnd > 0) {
							try {
								byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
								System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
								build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);
							} catch (InvalidProtocolBufferException e) {
								e.printStackTrace();
							}
						}

						ULog.d(TAG, "Byte Length" + data.length);
						mHandler.obtainMessage(MESSAGE_READ, new String(data)).sendToTarget();
					}
				}

				if (data != null && data.length > 0) {
					if(data[0] == 'E' && data[1] == 'g' && data[2] == 'g') {
						mHandler.obtainMessage(MESSAGE_READ, new String(data)).sendToTarget();
						return;
					}

					byte[] test = new byte[1];
					for (int i = 0; i < data.length; i++) {
						test[0] = data[i];
						ULog.d(TAG, "REAL RECEIVE MESSAGE ==>" + new String(test));
						//ULog.d(TAG, "REAL RECEIVE MESSAGE ==>" + test);
					}

					byte[] ddd = new byte[2];
					ddd[0] = data[data.length - 2];
					ddd[1] = data[data.length - 1];
					//
					ULog.d(TAG, "dd->>" + new String(ddd));

					if (!"\r\n".equals(new String(ddd))) {
						data2 = characteristic.getValue();
						flags = true;

						return;
					}


					byte[] d = new byte[2];
					d[0] = data[0];
					d[1] = data[1];

					ULog.d(TAG, "d->>" + new String(d));

					String cha = new String(d);
					ULog.d(TAG, "d2->>" + cha);


					//20160822 start
					try {
						int contentStart = 0, contentEnd = 0;
						if (data != null && data.length > 0) {
							ULog.d(TAG, "# Read characteristic 20160817: data 있음");

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
							ULog.d(TAG, "contentStart : " + contentStart + "////contentEnd : " + contentEnd);
						}

						if(contentStart == 0) {
							mHandler.obtainMessage(MESSAGE_READ, "제대로 안들옴!!!!!!!!!!!").sendToTarget();
							return;
						}

						byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
						System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
						build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);

						if (build.hasSerpCommand()) {
							mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
							return;
						}

						if (build.getSerpCommand() == MESSAGE_DATE_TIME_REQUEST) {
							mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
							return;
						} else if (build.getSerpCommand() == MESSAGE_DATE_TIME_REQUEST) {
							mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
							return;
						} else if (build.getSerpCommand() == 15) {
							mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
							return;
						} else if (build.getSerpCommand() == 18) {
							mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
							return;
						} else if (build.getSerpCommand() == 20) {
							mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
							return;
						} else if (build.getSerpCommand() == 22) {
							mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
							return;
						}

					} catch (InvalidProtocolBufferException e) {
						e.printStackTrace();
					}
					//20160822 end
				}else {
					ULog.d(TAG, "contentStart :  data 안들옴");
					mHandler.obtainMessage(MESSAGE_READ, "제대로 안들옴!!!!!!!!!!!").sendToTarget();
					return;
				}


			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			// We've received data from remote
			ULog.d(TAG, "# onCharacteristicChanged 20160817: " + characteristic.toString());



			//20160728 test start
			/*String unknownCharaString = "Unknown characteristic";
			String unknownServiceString = "Unknown service";
			String gattValue = GattAttributeResolver.getAttributeName(characteristic.getUuid().toString(), unknownCharaString);
			ULog.d(TAG, "# onCharacteristicGattValue: " + gattValue);
			for (final BluetoothGattService gattService : gatt.getServices()) {
				String gattValue2 = GattAttributeResolver.getAttributeName(gattService.getUuid().toString(), unknownServiceString);
				ULog.d(TAG, "# onServiceGattValue: " + gattValue2);


				for (final BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
					String gattValue3 = GattAttributeResolver.getAttributeName(gattCharacteristic.getUuid().toString(), unknownServiceString);
					ULog.d(TAG, "# onCharacteristicGattValueSecond: " + gattValue3);
					byte[] dataArr = gattCharacteristic.getValue();
					byte[] dataArr2 = characteristic.getValue();
					ULog.d(TAG, "# onCharacteristicGattValueSecondValue: " + byteArrayToHexString(dataArr));
					ULog.d(TAG, "# onCharacteristicGattValueSecondValue2: " + byteArrayToHexString(dataArr2));
				}
			}
			ULog.d(TAG, "# ========================= # ");*/
			//20160728 test end



			byte[] data = characteristic.getValue();
			if (data != null && data.length > 0) {
				// final StringBuilder stringBuilder = new
				// StringBuilder(data.length);
				// final StringBuilder number = new StringBuilder(1);
				// for(byte byteChar : data)
				// stringBuilder.append(String.format("%02X ", byteChar));
				String a = "";

				byte[] test = new byte[1];
				for (int i = 0; i < data.length; i++) {
					test[0] = data[i];
					ULog.d(TAG, "REAL RECEIVE MESSAGE ==>" + new String(test));
					//ULog.d(TAG, "REAL RECEIVE MESSAGE ==>" + test);
				}

//				if(data.length == 1) {
				if(false) {
					byte[] ddd = new byte[1];
					ddd[0] = data[data.length - 1];
					ULog.d(TAG, "dd->>" + new String(ddd));

					if (!"\r".equals(new String(ddd))) {

						ULog.d(TAG, "rrrr 들옴");
						data2 = characteristic.getValue();
						flags = true;

						return;
					}else if (!"\n".equals(new String(ddd))) {
						ULog.d(TAG, "nnnn 들옴");
						data2 = characteristic.getValue();
						flags = true;

						return;
					}
				}else {
					byte[] ddd = new byte[2];
					ddd[0] = data[data.length - 2];
					ddd[1] = data[data.length - 1];
					//
					ULog.d(TAG, "dd->>" + new String(ddd));

					if (!"\r\n".equals(new String(ddd))) {
						data2 = characteristic.getValue();
						flags = true;

						return;
					}
				}

				if (flags) {
					ULog.d(TAG, " 잘렸던 msg 이어 붙임 ");
					byte[] datas = data;
					data = new byte[data2.length + datas.length];

					ULog.d(TAG, " DATA LENGTH :" + data.length);
					for (int i = 0; i < data.length; i++) {
						if (i < data2.length)
							data[i] = data2[i];
						else
							data[i] = datas[(i - data2.length)];
					}
					flags = false;
				}

				byte[] d = new byte[2];
				d[0] = data[0];
				d[1] = data[1];

				ULog.d(TAG, "d->>" + new String(d));

				String cha = new String(d);
				ULog.d(TAG, "d2->>" + cha);



				//20160822 start
				try {
					int contentStart = 0, contentEnd = 0;
					if (data != null && data.length > 0) {
						ULog.d(TAG, "# Read characteristic 20160817: data 있음");

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
						ULog.d(TAG, "contentStart : " + contentStart + "////contentEnd : " + contentEnd);
					}

					byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
					System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
					ULog.d(TAG, "resultBuffer : " + resultBuffer+ "///resultBuffer len : " + resultBuffer.length  + "//////dataArr : " + data + "//////dataArr len : " + data.length);
					ULog.d(TAG, "# resultBuffer: " + byteArrayToHexString(resultBuffer));
					build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);
					ULog.d(TAG, "getSerpCommand: " + build.getSerpCommand() + "//hasSerpCommand: " + build.hasSerpCommand());
					ULog.d(TAG, "2222getDate: " + build.getDate() + "//getYear: " + build.getDate().getYear() + "//getMonth: " + build.getDate().getMonth() + "//getDay: " + build.getDate().getDay());
					ULog.d(TAG, "getLedColor: " + build.getLedColor());

					if(build.hasSerpCommand()){
						mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
						return;
					}

					if(build.getSerpCommand() == MESSAGE_DATE_TIME_REQUEST) {
						mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
						return;
					}else if(build.getSerpCommand() == MESSAGE_DATE_TIME_REQUEST) {
						mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
						return;
					}else if(build.getSerpCommand() == 15) {
						mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
						return;
					}else if(build.getSerpCommand() == 18) {
						mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
						return;
					}else if(build.getSerpCommand() == 20) {
						mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
						return;
					}else if(build.getSerpCommand() == 22) {
						mHandler.obtainMessage(MESSAGE_READ, build).sendToTarget();
						return;
					}

				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
				//20160822 end



				if ("AR".equals(new String(d)) || "AW".equals(new String(d))) {
					int nums = 0;
					a = cha;
					// walk cnt
					String numFront = "", numBack = "";

					for (int i = 2; i < 4; i++) {
						if (i - 2 == 0) {
							// data[i] = (byte) 60;
							numFront = Integer.toString(data[i] * 256);
						} else {
							// data[i] = (byte) 000;
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "W x->>" + numFront + " " + numBack);

					nums = Integer.parseInt(numFront) + Integer.parseInt(numBack);
					ULog.d(TAG, "W x2->>" + nums);

					a = a + Integer.toString(nums);
					ULog.d(TAG, "AW/AR a->>" + a);

					// E
					for (int i = 5; i < 7; i++) {
						if (i - 5 == 0) {
							numFront = Integer.toString(data[i]);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "E x->>" + numFront + " " + numBack);

					a = a + "E" + (numFront.length() < 2 ? "0" + numFront : numFront)
							+ (numBack.length() < 2 ? "0" + numBack : numBack);
					ULog.d(TAG, "E a->>" + a);

					// C
					for (int i = 8; i < 10; i++) {
						if (i - 8 == 0) {
							numFront = Integer.toString(data[i] * 256);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "C x->>" + numFront + " " + numBack);
					nums = Integer.parseInt(numFront) + Integer.parseInt(numBack);
					ULog.d(TAG, "C x2->>" + nums);
					a = a + "C" + Integer.toString(nums);

					ULog.d(TAG, "C a->>" + a);

					// D
					for (int i = 11; i < 13; i++) {
						if (i - 11 == 0) {
							numFront = Integer.toString(data[i] * 256);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "D x->>" + numFront + " " + numBack);
					nums = Integer.parseInt(numFront) + Integer.parseInt(numBack);
					ULog.d(TAG, "D x2->>" + nums);

					a = a + "D" + Integer.toString(nums);

					ULog.d(TAG, "D a->>" + a);

					ULog.d(TAG, "Byte Length" + data.length);
					mHandler.obtainMessage(MESSAGE_READ, a).sendToTarget();

				} else if ("SD".equals(new String(d)) || "SL".equals(new String(d))) {
					a = cha;
					// walk cnt
					String numFront = "", numBack = "";
					String y = "", m = "", dd = "", hh = "", mm = "";
					for (int i = 2; i < 7; i++) {
						switch (i) {
						case 2:
							y = Integer.toString(data[i]);
							break;
						case 3:
							m = Integer.toString(data[i]);
							break;
						case 4:
							dd = Integer.toString(data[i]);
							break;
						case 5:
							hh = Integer.toString(data[i]);
							break;
						case 6:
							mm = Integer.toString(data[i]);
							break;
						}
					}
					ULog.d(TAG, "SD/SL x->>" + y + " " + m + " " + d + " " + hh + " " + mm);

					a = a + (y.length() < 2 ? "0" + y : y) + (m.length() < 2 ? "0" + m : m)
							+ (dd.length() < 2 ? "0" + dd : dd) + (hh.length() < 2 ? "0" + hh : hh)
							+ (mm.length() < 2 ? "0" + mm : mm);

					ULog.d(TAG, "SD/SL a->>" + a);

					// E
					for (int i = 8; i < 10; i++) {
						if (i - 8 == 0) {
							numFront = Integer.toString(data[i]);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "E x->>" + numFront + " " + numBack);

					a = a + "E" + (numFront.length() < 2 ? "0" + numFront : numFront)
							+ (numBack.length() < 2 ? "0" + numBack : numBack);
					ULog.d(TAG, "E a->>" + a);

					ULog.d(TAG, "Byte Length" + data.length);
					mHandler.obtainMessage(MESSAGE_READ, a).sendToTarget();
				} else if ("UW".equals(new String(d)) || "UR".equals(new String(d))) {
					int nums = 0;
					a = cha;
					// walk cnt
					String numFront = "", numBack = "";

					for (int i = 2; i < 4; i++) {
						if (i - 2 == 0) {
							numFront = Integer.toString(data[i] * 256);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "W x->>" + numFront + " " + numBack);

					nums = Integer.parseInt(numFront) + Integer.parseInt(numBack);
					ULog.d(TAG, "W x2->>" + nums);

					a = a + Integer.toString(nums);
					ULog.d(TAG, "UW/UR a->>" + a);

					// S
					String y = "", m = "", dd = "", hh = "", mm = "";
					for (int i = 5; i < 10; i++) {
						switch (i) {
						case 5:
							y = Integer.toString(data[i]);
							break;
						case 6:
							m = Integer.toString(data[i]);
							break;
						case 7:
							dd = Integer.toString(data[i]);
							break;
						case 8:
							hh = Integer.toString(data[i]);
							break;
						case 9:
							mm = Integer.toString(data[i]);
							break;
						}
					}
					ULog.d(TAG, "S x->>" + y + " " + m + " " + d + " " + hh + " " + mm);

					a = a + "S" + (y.length() < 2 ? "0" + y : y) + (m.length() < 2 ? "0" + m : m)
							+ (dd.length() < 2 ? "0" + dd : dd) + (hh.length() < 2 ? "0" + hh : hh)
							+ (mm.length() < 2 ? "0" + mm : mm);
					ULog.d(TAG, "S a->>" + a);

					// E
					for (int i = 11; i < 13; i++) {
						switch (i) {
						case 11:
							numFront = Integer.toString(data[i]);
							break;
						case 12:
							numBack = Integer.toString(data[i]);
							break;
						}
					}
					ULog.d(TAG, "E x->>" + numFront + " " + numBack);

					a = a + "E" + (numFront.length() < 2 ? "0" + numFront : numFront)
							+ (numBack.length() < 2 ? "0" + numBack : numBack);

					ULog.d(TAG, "E a->>" + a);

					// C
					for (int i = 14; i < 16; i++) {
						if (i - 14 == 0) {
							numFront = Integer.toString(data[i] * 256);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "C x->>" + numFront + " " + numBack);
					nums = Integer.parseInt(numFront) + Integer.parseInt(numBack);
					ULog.d(TAG, "C x2->>" + nums);
					a = a + "C" + Integer.toString(nums);

					ULog.d(TAG, "C a->>" + a);

					// D
					for (int i = 17; i < 19; i++) {
						if (i - 17 == 0) {
							numFront = Integer.toString(data[i] * 256);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "D x->>" + numFront + " " + numBack);
					nums = Integer.parseInt(numFront) + Integer.parseInt(numBack);
					ULog.d(TAG, "D x2->>" + nums);

					a = a + "D" + Integer.toString(nums);

					ULog.d(TAG, "D a->>" + a);

					ULog.d(TAG, "Byte Length" + data.length);
					mHandler.obtainMessage(MESSAGE_READ, a).sendToTarget();

				} else if ("UD".equals(new String(d)) || "UL".equals(new String(d))) {
					a = cha;
					// sleep
					String numFront = "", numBack = "";
					String y = "", m = "", dd = "", hh = "", mm = "";
					for (int i = 2; i < 7; i++) {
						switch (i) {
						case 2:
							y = Integer.toString(data[i]);
							break;
						case 3:
							m = Integer.toString(data[i]);
							break;
						case 4:
							dd = Integer.toString(data[i]);
							break;
						case 5:
							hh = Integer.toString(data[i]);
							break;
						case 6:
							mm = Integer.toString(data[i]);
							break;
						}
					}
					ULog.d(TAG, "UD/UL x->>" + y + " " + m + " " + d + " " + hh + " " + mm);

					a = a + (y.length() < 2 ? "0" + y : y) + (m.length() < 2 ? "0" + m : m)
							+ (dd.length() < 2 ? "0" + dd : dd) + (hh.length() < 2 ? "0" + hh : hh)
							+ (mm.length() < 2 ? "0" + mm : mm);

					ULog.d(TAG, "UD/UL a->>" + a);

					// E
					for (int i = 8; i < 10; i++) {
						if (i - 8 == 0) {
							numFront = Integer.toString(data[i]);
						} else {
							numBack = Integer.toString(data[i]);
						}
					}
					ULog.d(TAG, "E x->>" + numFront + " " + numBack);

					a = a + "E" + (numFront.length() < 2 ? "0" + numFront : numFront)
							+ (numBack.length() < 2 ? "0" + numBack : numBack);
					ULog.d(TAG, "E a->>" + a);

					ULog.d(TAG, "Byte Length" + data.length);
					mHandler.obtainMessage(MESSAGE_READ, a).sendToTarget();

				} else {
					ULog.d(TAG, new String(data));
					ULog.d(TAG, new String(a));
					ULog.d(TAG, "Byte Length" + data.length);
					mHandler.obtainMessage(MESSAGE_READ, new String(data)).sendToTarget();
				}

			}

			// if(mDefaultChar == null &&
			// isWritableCharacteristic(characteristic)) {
			// mDefaultChar = characteristic;
			// }
		};
	};

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

}

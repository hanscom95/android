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

package com.example.android.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private String mBluetoothDeviceName;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private TransactionBuilder mTransactionBuilder = null;



    private List<BluetoothGattDescriptor> mDescriptors;
    private Iterator<BluetoothGattDescriptor> iterDescriptor;
    private BluetoothGattDescriptor lastDescriptor;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String  EXTRA_CHARACTER_DATA=
            "com.example.bluetooth.le.EXTRA_CHARACTER_DATA";

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                mBluetoothGatt.discoverServices());

                mBluetoothGatt.readRemoteRssi();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server. : " + status);
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            if(characteristic.getValue().length > 0) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
//            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//            Log.d(TAG, "# onCharacteristicChanged " + characteristic.toString());
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt,int rssi, int status){
            super.onReadRemoteRssi(gatt, rssi, status);
//            if (status == BluetoothGatt.GATT_SUCCESS)
//                System.out.println("onReadRemoteRssi: " + rssi);

//            if (mBluetoothGatt != null)
//                mBluetoothGatt.readRemoteRssi();//true

        }
    };

    public void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
                intent.putExtra(EXTRA_CHARACTER_DATA, characteristic.getValue());
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        mDescriptors = new ArrayList<BluetoothGattDescriptor>();

        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mBluetoothDeviceName = device.getName();
        mConnectionState = STATE_CONNECTING;


        if (mTransactionBuilder == null)
            mTransactionBuilder = new TransactionBuilder(this);

        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized : disconnect");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized : readCharacteristic");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        Log.w(TAG, "BluetoothAdapter not initialized : setCharacteristicNotification");
        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }


        mDescriptors = new ArrayList<BluetoothGattDescriptor>(characteristic.getDescriptors());
        iterDescriptor = mDescriptors.iterator();
        writeNextDescriptor();
    }

    // Writes next descriptor in order to enable notification or indication
    protected void writeNextDescriptor() {
        if (iterDescriptor.hasNext()) {
            lastDescriptor = iterDescriptor.next();

            if (lastDescriptor.getCharacteristic() == mGattCharacteristics.get(2).get(0)) {
                lastDescriptor.setValue(isSetProperty(mGattCharacteristics.get(2).get(0)
                        .getProperties()) ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        : BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                //mBluetoothLeService.writeDescriptor(mDevice, lastDescriptor);
                mBluetoothGatt.writeDescriptor(lastDescriptor);
            }
        }
    }


    // Checks if given property is set
    private static boolean isSetProperty(int properties) {
        boolean isSet = ((properties >> (4)) & 1) != 0;
        return isSet;
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }


    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    public void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
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
//            Log.d("BluetoothLeService", "mGattCharacteristics uuid : " + mGattCharacteristics.get(1).get(1).getUuid());
            if(mGattCharacteristics.size() < 3) {
                characteristic = mGattCharacteristics.get(1).get(0);
                Log.d("BluetoothLeService", "여기들옴 1 : " + mGattCharacteristics.get(1).get(1).getUuid());
            }else {
                if("EggBand".equals(mBluetoothDeviceName.substring(0,7))) {
                    Log.d("BluetoothLeService", "여기들옴 2");
                    characteristic = mGattCharacteristics.get(1).get(0);
                }else {
                    Log.d("BluetoothLeService", "여기들옴 3");
                    characteristic = mGattCharacteristics.get(2).get(0);
                }
            }
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
                mNotifyCharacteristic = characteristic;
                setCharacteristicNotification(characteristic, true);
            }
        }
    }

    public void bleWrite(byte[] data) {
        TransactionBuilder.Transaction transaction = mTransactionBuilder.makeTransaction();
        transaction.begin();
        transaction.setMessage(data);
        transaction.sendTransaction();
    }



    public boolean write(BluetoothGattCharacteristic chr, byte[] data) {
        if (mBluetoothGatt == null) {
            return false;
        }

        BluetoothGattCharacteristic writableChar = null;

        writableChar = mGattCharacteristics.get(2).get(1);
//        writableChar = mGattCharacteristics.get(2).get(1);
        //setCharacteristicNotification(writableChar, true);
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


        mBluetoothGatt.writeCharacteristic(writableChar);
        return true;
    }
}

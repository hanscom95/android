package com.standingegg.band.ota;

import java.util.ArrayList;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.google.protobuf.UninitializedMessageException;
import com.standingegg.band.MainActivity;
import com.standingegg.band.setting.DeviceActivity;
import com.standingegg.band.ota.DeviceConnectTask;
import com.standingegg.band.ota.Statics;
import com.standingegg.band.util.StnEggPkt;
import com.standingegg.band.util.ULog;

/**
 * Created by wouter on 9-10-14.
 */
public class Callback extends BluetoothGattCallback {
    public static String TAG = "Callback";
    DeviceConnectTask task;
    private Handler handler = new Handler();
    private int checkRefreshAttempts;
    private boolean refreshDone;
    private int refreshAttempt;
	private ArrayList<BluetoothGattService> mGattServices = new ArrayList<BluetoothGattService>();

    public Callback(DeviceConnectTask task) {
        this.task = task;
    }

    @Override
    public void onConnectionStateChange(final BluetoothGatt gatt, int status,
                                        int newState) {
        ULog.i(TAG, "le onConnectionStateChange [" + newState + "]");
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            ULog.i(TAG, "le device connected");
            gatt.discoverServices();
            //mGattServices.clear();
            // Wait for cache refresh before starting service discovery.
            /*handler.post(new Runnable() {
                @Override
                public void run() {
                    if (task.refreshSucceeded() || ++checkRefreshAttempts > 20) {
                        gatt.discoverServices();
                    }
                    else {
                        Log.d(TAG, "Delay service discovery: " + checkRefreshAttempts);
                        handler.postDelayed(this, 100);
                    }
                }
            });*/
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            ULog.i(TAG, "le device disconnected");
            handler.removeCallbacksAndMessages(null);
            gatt.disconnect();
        } else if (newState == BluetoothProfile.STATE_CONNECTING) {
            ULog.i(TAG, "le device connecting");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Statics.CONNECTION_STATE_UPDATE);
        intent.putExtra("state", newState);
        task.context.sendBroadcast(intent);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        ULog.i(TAG, "onServicesDiscovered");
        if(task.mConnectionInfo.mDeviceConncted=="true"){
            sendVibrationMsg(gatt);	
        }else {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Intent intent = new Intent();
                intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
                intent.putExtra("error", Statics.ERROR_COMMUNICATION);
                task.context.sendBroadcast(intent);
                return;
            }
            // Refresh device cache. This is the safest place to initiate the procedure.
            if (!refreshDone && ++refreshAttempt <= 10) {
                refreshDone = BluetoothManager.refresh(gatt); // should not fail
                if (refreshDone)
                    ULog.d(TAG, "restart discovery after refresh");
                gatt.discoverServices();
                return;
            }
            // Check for SUOTA support
            BluetoothGattService suota = gatt.getService(Statics.SPOTA_SERVICE_UUID);
            if (suota == null
                || suota.getCharacteristic(Statics.SPOTA_MEM_DEV_UUID) == null
                || suota.getCharacteristic(Statics.SPOTA_GPIO_MAP_UUID) == null
                || suota.getCharacteristic(Statics.SPOTA_MEM_INFO_UUID) == null
                || suota.getCharacteristic(Statics.SPOTA_PATCH_LEN_UUID) == null
                || suota.getCharacteristic(Statics.SPOTA_PATCH_DATA_UUID) == null
                || suota.getCharacteristic(Statics.SPOTA_SERV_STATUS_UUID) == null
                || suota.getCharacteristic(Statics.SPOTA_SERV_STATUS_UUID).getDescriptor(Statics.SPOTA_DESCRIPTOR_UUID) == null
                ) {
                Intent intent = new Intent();
                intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
                intent.putExtra("error", Statics.ERROR_SUOTA_NOT_FOUND);
                task.context.sendBroadcast(intent);
                return;
            }
            Intent intent = new Intent();
            intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
            intent.putExtra("step", 0);
            task.context.sendBroadcast(intent);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        boolean sendUpdate = true;
        int index = -1;
        int step = -1;

        if (characteristic.getUuid().equals(Statics.ORG_BLUETOOTH_CHARACTERISTIC_MANUFACTURER_NAME_STRING)) {
            index = 0;
        } else if (characteristic.getUuid().equals(Statics.ORG_BLUETOOTH_CHARACTERISTIC_MODEL_NUMBER_STRING)) {
            index = 1;
        } else if (characteristic.getUuid().equals(Statics.ORG_BLUETOOTH_CHARACTERISTIC_FIRMWARE_REVISION_STRING)) {
            index = 2;
        } else if (characteristic.getUuid().equals(Statics.ORG_BLUETOOTH_CHARACTERISTIC_SOFTWARE_REVISION_STRING)) {
            index = 3;
        }
        // SPOTA
        else if (characteristic.getUuid().equals(Statics.SPOTA_MEM_INFO_UUID)) {
//			int memInfoValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0);
//			Log.d("mem info", memInfoValue + "");
//			DeviceActivity.getInstance().logMemInfoValue(memInfoValue);
            step = 5;
        } else {
            sendUpdate = false;
        }

        if (sendUpdate) {
            ULog.d(TAG, "onCharacteristicRead: " + index);
            Intent intent = new Intent();
            intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
            if (index >= 0) {
                intent.putExtra("characteristic", index);
                intent.putExtra("value", new String(characteristic.getValue()));
            } else {
                intent.putExtra("step", step);
                intent.putExtra("value", characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0));
            }
            task.context.sendBroadcast(intent);
        }

        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(final BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        ULog.d(TAG, "onCharacteristicWrite: " + characteristic.getUuid().toString());

        if (status == BluetoothGatt.GATT_SUCCESS) {
            ULog.i(TAG, "write succeeded");
            int step = -1;
            // Step 3 callback: write SPOTA_GPIO_MAP_UUID value
            if (characteristic.getUuid().equals(Statics.SPOTA_GPIO_MAP_UUID)) {
                step = 4;
            }
            // Step 4 callback: set the patch length, default 240
            else if (characteristic.getUuid().equals(Statics.SPOTA_PATCH_LEN_UUID)) {
                step = DeviceActivity.getInstance().bluetoothManager.type == SuotaManager.TYPE ? 5 : 7;
            } else if (characteristic.getUuid().equals(Statics.SPOTA_MEM_DEV_UUID)) {
            }
            else if (characteristic.getUuid().equals(Statics.SPOTA_PATCH_DATA_UUID)
                    //&& DeviceActivity.getInstance().bluetoothManager.type == SuotaManager.TYPE
                    && DeviceActivity.getInstance().bluetoothManager.chunkCounter != -1
                    ) {
                //step = DeviceActivity.getInstance().bluetoothManager.type == SuotaManager.TYPE ? 5 : 7;
                /*DeviceActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DeviceActivity.getInstance().bluetoothManager.sendBlock();
                    }
                });*/
                ULog.d(TAG, "Next block in chunk " + DeviceActivity.getInstance().bluetoothManager.chunkCounter);
                DeviceActivity.getInstance().bluetoothManager.sendBlock();
            }

            if (step > 0) {
                Intent intent = new Intent();
                intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
                intent.putExtra("step", step);
                task.context.sendBroadcast(intent);
            }
            
            if(task.mConnectionInfo.mViblation != null){
            	Handler mHandler = new Handler(Looper.getMainLooper());
            	Runnable mRunnable = new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

		            	handler.removeCallbacksAndMessages(null);
						mGattServices.clear();
						gatt.close();
		            	gatt.disconnect();
		            	task.mConnectionInfo.isViblation(null);
					}
				};
            	mHandler.postDelayed(mRunnable, 1000);
            }
        } else {
            ULog.e(TAG, "write failed: " + status);
            // Suota on remote device doesn't send write response before reboot
            if (!DeviceActivity.getInstance().bluetoothManager.rebootsignalSent) {
                Intent intent = new Intent();
                intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
                intent.putExtra("error", Statics.ERROR_COMMUNICATION);
                task.context.sendBroadcast(intent);
            }
        }
        super.onCharacteristicWrite(gatt, characteristic, status);
    }
    
    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        ULog.d(TAG, "onDescriptorWrite");
        if (status != BluetoothGatt.GATT_SUCCESS) {
            Intent intent = new Intent();
            intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
            intent.putExtra("error", Statics.ERROR_COMMUNICATION);
            task.context.sendBroadcast(intent);
            return;
        }
        if (descriptor.getCharacteristic().getUuid().equals(Statics.SPOTA_SERV_STATUS_UUID)) {
            int step = 2;

            Intent intent = new Intent();
            intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
            intent.putExtra("step", step);
            task.context.sendBroadcast(intent);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        int value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        ULog.d(TAG, String.format("SPOTA_SERV_STATUS notification: %#04x", value));

        int step = -1;
        int error = -1;
        int memDevValue = -1;
        // Set memtype callback
        if (value == 0x10) {
            step = 3;
        }
        // Successfully sent a block, send the next one
        else if (value == 0x02) {
            step = DeviceActivity.getInstance().bluetoothManager.type == SuotaManager.TYPE ? 5 : 8;
        } else if (value == 0x03 || value == 0x01) {
            memDevValue = value;
        } else {
            error = value;
        }
        if (step >= 0 || error >= 0 || memDevValue >= 0) {
            Intent intent = new Intent();
            intent.setAction(Statics.BLUETOOTH_GATT_UPDATE);
            intent.putExtra("step", step);
            intent.putExtra("error", error);
            intent.putExtra("memDevValue", memDevValue);
            task.context.sendBroadcast(intent);
        }
    }
    
    private void sendVibrationMsg(BluetoothGatt gatt) {
        StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
        builder.setSerpCommand(28);
        builder.setLedColor(Integer.parseInt(task.mConnectionInfo.mViblation));

        try {
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

            BluetoothGattCharacteristic writableChar = gatt.getServices().get(1).getCharacteristics().get(0);
            writableChar.setValue(encoded);
            writableChar.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            gatt.writeCharacteristic(writableChar);
        }catch (UninitializedMessageException e){
            e.printStackTrace();
        }
    }
}
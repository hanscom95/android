package com.standingegg.band.ota;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.View;

import com.standingegg.band.setting.DeviceActivity;
import com.standingegg.band.R;
import com.standingegg.band.bluetooth.ConnectionInfo;
import com.standingegg.band.ota.File;
import com.standingegg.band.ota.Statics;
import com.standingegg.band.util.ULog;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

/**
 * Created by wouter on 6-11-14.
 */
public abstract class BluetoothManager {
	static final String TAG = "BluetoothManager";

	public static final int END_SIGNAL = 0xfe000000;
	public static final int REBOOT_SIGNAL = 0xfd000000;

	// Input values
	int memoryType;

	// SPI
	int MISO_GPIO;  // P0_5 (0x05)
	int MOSI_GPIO;  // P0_6 (0x06)
	int CS_GPIO;    // P0_3 (0x03)
	int SCK_GPIO;   // P0_0 (0x00)

	// I2C
	int I2CDeviceAddress;
	int SCL_GPIO;
	int SDA_GPIO;

	// SUOTA
	int imageBank;

	// SPOTA
	int patchBaseAddress;

	DeviceActivity activity;
	File file;
	String fileName;
	Context context;
	BluetoothDevice device;
	HashMap errors;

	boolean lastBlock = false;
	boolean lastBlockSent = false;
	boolean preparedForLastBlock = false;
	boolean endSignalSent = false;
	boolean rebootsignalSent = false;
	boolean finished = false;
	boolean hasError = false;
	boolean refreshPending;
	public int type;
	protected int step;
	int blockCounter = 0;
	int chunkCounter = -1;

	public Queue characteristicsQueue;
	private ConnectionInfo mConnectionInfo = null;

	public BluetoothManager(Context context) {
		this.context = context;
		initErrorMap();
		characteristicsQueue = new ArrayDeque<BluetoothGattCharacteristic>();
		mConnectionInfo = ConnectionInfo.getInstance(context);
	}

	public abstract void processStep(Intent intent);

	public boolean isFinished() {
		return finished;
	}

	public boolean isRefreshPending() {
		return refreshPending;
	}

	public void setRefreshPending(boolean refreshPending) {
		this.refreshPending = refreshPending;
	}

    public boolean getError() {
        return hasError;
    }

	public File getFile() {
		return file;
	}

	public void setFile(File file) throws IOException {
		this.file = file;
		this.file.setType(this.type);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}

	public void setMemoryType(int memoryType) {
		this.memoryType = memoryType;
	}

	public void setPatchBaseAddress(int patchBaseAddress) {
		this.patchBaseAddress = patchBaseAddress;
	}

	public void setImageBank(int imageBank) {
		this.imageBank = imageBank;
	}

	public void setMISO_GPIO(int MISO_GPIO) {
		this.MISO_GPIO = MISO_GPIO;
	}

	public void setMOSI_GPIO(int MOSI_GPIO) {
		this.MOSI_GPIO = MOSI_GPIO;
	}

	public void setCS_GPIO(int CS_GPIO) {
		this.CS_GPIO = CS_GPIO;
	}

	public void setSCK_GPIO(int SCK_GPIO) {
		this.SCK_GPIO = SCK_GPIO;
	}

	public void setSCL_GPIO(int SCL_GPIO) {
		this.SCL_GPIO = SCL_GPIO;
	}

	public void setSDA_GPIO(int SDA_GPIO) {
		this.SDA_GPIO = SDA_GPIO;
	}

	public void setI2CDeviceAddress(int I2CDeviceAddress) {
		this.I2CDeviceAddress = I2CDeviceAddress;
	}

	public void enableNotifications() {
		ULog.d(TAG, "- enableNotifications");
		if(mConnectionInfo==null || mConnectionInfo.mDeviceConncted == "false")
			activity.log("- Enable notifications for SPOTA_SERV_STATUS characteristic");
		// Get the service status UUID from the gatt and enable notifications
		List<BluetoothGattService> services = BluetoothGattSingleton.getGatt().getServices();
		for (BluetoothGattService service : services) {
			if(mConnectionInfo==null || mConnectionInfo.mDeviceConncted == "false")
				activity.log("  Found service: " + service.getUuid().toString());
			List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
			for (BluetoothGattCharacteristic characteristic : characteristics) {
				if(mConnectionInfo==null || mConnectionInfo.mDeviceConncted == "false")
					activity.log("  Found characteristic: " + characteristic.getUuid().toString());
				if (characteristic.getUuid().equals(Statics.SPOTA_SERV_STATUS_UUID)) {
					if(mConnectionInfo==null || mConnectionInfo.mDeviceConncted == "false")
						activity.log("*** Found SUOTA service");
					BluetoothGattSingleton.getGatt().setCharacteristicNotification(characteristic, true);
					BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
							Statics.SPOTA_DESCRIPTOR_UUID);
					descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
					BluetoothGattSingleton.getGatt().writeDescriptor(descriptor);
				}
			}
		}
	}

	protected abstract int getSpotaMemDev();

	public void setSpotaMemDev() {
		BluetoothGattCharacteristic characteristic = BluetoothGattSingleton.getGatt().getService(Statics.SPOTA_SERVICE_UUID)
				.getCharacteristic(Statics.SPOTA_MEM_DEV_UUID);

		ULog.e("bluetoothManager", "setSpotaMemDev ============== null");
		int memType = this.getSpotaMemDev();
		characteristic.setValue(memType, BluetoothGattCharacteristic.FORMAT_UINT32, 0);
		ULog.d(TAG, "setSpotaMemDev: " + String.format("%#10x", memType));
		BluetoothGattSingleton.getGatt().writeCharacteristic(characteristic);
		if(mConnectionInfo==null || mConnectionInfo.mDeviceConncted == "false"){
			activity.log("Set SPOTA_MEM_DEV: " + String.format("%#10x", memType));	
		}
	}

	/**
	 * 0x05060300 when
	 * mem_type:        "External SPI" (0x13)
	 * MISO GPIO:       P0_5 (0x05)
	 * MOSI GPIO:       P0_6 (0x06)
	 * CS GPIO:         P0_3 (0x03)
	 * SCK GPIO:        P0_0 (0x00)
	 * image_bank:      "Oldest" (value: 0)
	 */
	private int getMemParamsSPI() {
		return (MISO_GPIO << 24) | (MOSI_GPIO << 16) | (CS_GPIO << 8) | SCK_GPIO;
	}

	/**
	 * 0x01230203 when
	 * mem_type:			"External I2C" (0x12)
	 * I2C device addr:		0x0123
	 * SCL GPIO:			P0_2
	 * SDA GPIO:			P0_3
	 */
	private int getMemParamsI2C() {
		return (I2CDeviceAddress << 16) | (SCL_GPIO << 8) | SDA_GPIO;
	}

	// Step 8 in documentation
	public void setSpotaGpioMap() {
		int memInfoData = 0;
		boolean valid = false;
		switch (memoryType) {
			case Statics.MEMORY_TYPE_SPI:
				memInfoData = this.getMemParamsSPI();
				valid = true;
				break;
			case Statics.MEMORY_TYPE_I2C:
				memInfoData = this.getMemParamsI2C();
				valid = true;
				break;
		}
		if (valid) {
			ULog.d(TAG, "setSpotaGpioMap: " + String.format("%#10x", memInfoData));
			activity.log("Set SPOTA_GPIO_MAP: " + String.format("%#10x", memInfoData));
			BluetoothGattCharacteristic characteristic = BluetoothGattSingleton.getGatt().getService(Statics.SPOTA_SERVICE_UUID)
					.getCharacteristic(Statics.SPOTA_GPIO_MAP_UUID);
			characteristic.setValue(memInfoData, BluetoothGattCharacteristic.FORMAT_UINT32, 0);
			BluetoothGattSingleton.getGatt().writeCharacteristic(characteristic);
		} else {
			ULog.e(TAG, "Memory type not set.");
			activity.log("Set SPOTA_GPIO_MAP: Memory type not set.");
		}
	}

	public void setPatchLength() {
		int blocksize = file.getFileBlockSize();
//		int blocksizeLE = (blocksize & 0xFF) << 8 | ((blocksize & 0xFF00) >> 8);
		if (lastBlock) {
			blocksize = this.file.getNumberOfBytes() % file.getFileBlockSize();
			preparedForLastBlock = true;
		}
		ULog.d(TAG, "setPatchLength: " + blocksize + " - " + String.format("%#4x", blocksize));
		activity.log("Set SPOTA_PATCH_LENGTH: " + blocksize);
		BluetoothGattCharacteristic characteristic = BluetoothGattSingleton.getGatt().getService(Statics.SPOTA_SERVICE_UUID)
				.getCharacteristic(Statics.SPOTA_PATCH_LEN_UUID);
		characteristic.setValue(blocksize, BluetoothGattCharacteristic.FORMAT_UINT16, 0);
		BluetoothGattSingleton.getGatt().writeCharacteristic(characteristic);
	}

	public float sendBlock() {
		//float progress = 0;
		final float progress = ((float) (blockCounter + 1) / (float) file.getNumberOfBlocks()) * 100;
		if (!lastBlockSent) {
			//progress = ((float) (blockCounter + 1) / (float) file.getNumberOfBlocks()) * 100;
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					sendProgressUpdate((int) progress);
				}
			});
			//sendProgressUpdate((int) progress);
			ULog.d(TAG, "Sending block " + (blockCounter + 1) + " of " + file.getNumberOfBlocks());
			byte[][] block = file.getBlock(blockCounter);

			int i = ++chunkCounter;
			boolean lastChunk = false;
			if (chunkCounter == block.length - 1) {
				chunkCounter = -1;
				lastChunk = true;
			}
			byte[] chunk = block[i];

			final int chunkNumber = (blockCounter * file.getChunksPerBlockCount()) + i + 1;
			final String message = "Sending chunk " + chunkNumber + " of " + file.getTotalChunkCount() + " (with " + chunk.length + " bytes)";
            if (chunkNumber == 1)
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.log("Update procedure started.");
                        activity.progressChunk.setVisibility(View.VISIBLE);
                    }
                });
            /*if (chunkNumber < 100 || chunkNumber % 100 == 0 || chunkNumber > (file.getNumberOfBlocks() - 1) * file.getChunksPerBlockCount())
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.log(message);
                    }
                });*/
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.progressChunk.setText("Sending chunk " + chunkNumber + " of " + file.getTotalChunkCount());
                }
            });
			String systemLogMessage = "Sending block " + (blockCounter + 1) + ", chunk " + (i + 1) + ", blocksize: " + block.length + ", chunksize " + chunk.length;
			ULog.d(TAG, systemLogMessage);
			BluetoothGattCharacteristic characteristic = BluetoothGattSingleton.getGatt().getService(Statics.SPOTA_SERVICE_UUID)
					.getCharacteristic(Statics.SPOTA_PATCH_DATA_UUID);
			characteristic.setValue(chunk);
//			characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
			boolean r = BluetoothGattSingleton.getGatt().writeCharacteristic(characteristic);
			ULog.d(TAG, "writeCharacteristic: " + r);

			if (lastChunk) {

				// SUOTA
				if (!lastBlock) {
					blockCounter++;
				} else {
					lastBlockSent = true;
				}
				if (blockCounter + 1 == file.getNumberOfBlocks()) {
					lastBlock = true;
				}

				// SPOTA
				if (type == SpotaManager.TYPE) {
					lastBlockSent = true;
				}
			}
		}
		return progress;
	}

	public void sendEndSignal() {
		ULog.d(TAG, "sendEndSignal");
		activity.log("send SUOTA END command");
		BluetoothGattCharacteristic characteristic = BluetoothGattSingleton.getGatt().getService(Statics.SPOTA_SERVICE_UUID)
				.getCharacteristic(Statics.SPOTA_MEM_DEV_UUID);
		characteristic.setValue(END_SIGNAL, BluetoothGattCharacteristic.FORMAT_UINT32, 0);
		BluetoothGattSingleton.getGatt().writeCharacteristic(characteristic);
		endSignalSent = true;
	}

	public void sendRebootSignal() {
		ULog.d(TAG, "sendRebootSignal");
		activity.log("send SUOTA REBOOT command");
		BluetoothGattCharacteristic characteristic = BluetoothGattSingleton.getGatt().getService(Statics.SPOTA_SERVICE_UUID)
				.getCharacteristic(Statics.SPOTA_MEM_DEV_UUID);
		characteristic.setValue(REBOOT_SIGNAL, BluetoothGattCharacteristic.FORMAT_UINT32, 0);
		BluetoothGattSingleton.getGatt().writeCharacteristic(characteristic);
		rebootsignalSent = true;
		activity.enableCloseButton();
	}

	public void readNextCharacteristic() {
		if (characteristicsQueue.size() >= 1) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) characteristicsQueue.poll();
			BluetoothGattSingleton.getGatt().readCharacteristic(characteristic);
			ULog.d(TAG, "readNextCharacteristic");
		}
	}

	private void sendProgressUpdate(int progress) {
		activity.progressBar.setProgress(progress);
	}

	public void disconnect() {
		try {
			BluetoothGattSingleton.getGatt();//.disconnect();
            // Refresh device cache if update was successful
            if (refreshPending)
                refresh(BluetoothGattSingleton.getGatt());
			BluetoothGattSingleton.getGatt().close();
			if(activity != null) {
				activity.log("Disconnect from device");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(activity != null) {
				activity.log("Error disconnecting from device");	
			}
		}
		try {
			if(file != null) {
				file.close();
			}
		}
		catch (Exception e) { }
	}

	protected void onSuccess() {
		finished = true;
        refreshPending = true;
		activity.log("Upload completed");
		new AlertDialog.Builder(context)
				.setTitle("Upload completed")
				.setMessage(context.getResources().getString(R.string.restart))
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String filesDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LamBand/" + fileName;
						java.io.File removeFile = new java.io.File(filesDir);
						if (removeFile.exists()) {
							removeFile.delete();
				    	}
						
						sendRebootSignal();
					}
				})
				/*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
                        activity.switchView(0);
						//disconnect();
					}
				})*/
				.show();
	}


	public void onError(int errorCode) {
		if (!hasError) {
			ULog.d(TAG, "Error: " + errorCode + " " + errors.get(errorCode));
			String error = (String) errors.get(errorCode);
			activity.log("Error: " + error);
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
			dialogBuilder.setTitle("An error occurred.")
					.setMessage(error);
			dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
				}
			});
			/*dialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
				}
			});*/
            dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    activity.finish();
                }
            });
			dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
			dialogBuilder.show();
			disconnect();
			hasError = true;
		}
	}

	private void initErrorMap() {
		this.errors = new HashMap<Integer, String>();
		// Value zero must not be used !! Notifications are sent when status changes.
		errors.put(0x03, "Forced exit of SPOTA service. See Table 1");
		errors.put(0x04, "Patch Data CRC mismatch.");
		errors.put(0x05, "Received patch Length not equal to PATCH_LEN characteristic value.");
		errors.put(0x06, "External Memory Error. Writing to external device failed.");
		errors.put(0x07, "Internal Memory Error. Not enough internal memory space for patch.");
		errors.put(0x08, "Invalid memory device.");
		errors.put(0x09, "Application error.");

		// SUOTAR application specific error codes
		errors.put(0x11, "Invalid image bank");
//		errors.put(0x12, "Invalid image header");
		errors.put(0x12, "The device does not support");
		errors.put(0x13, "Invalid image size");
		errors.put(0x14, "Invalid product header");
		errors.put(0x15, "Same Image Error");
		errors.put(0x16, "Failed to read from external memory device");

        // Application error codes
        errors.put(Statics.ERROR_COMMUNICATION, "Communication error.");
        errors.put(Statics.ERROR_SUOTA_NOT_FOUND, "The remote device does not support SUOTA.");
	}

	protected void goToStep(int step) {
		Intent i = new Intent();
		i.putExtra("step", step);
		processStep(i);
	}

    public static boolean refresh (BluetoothGatt gatt) {
        try {
			ULog.d(TAG, "refresh device cache");
            Method localMethod = gatt.getClass().getMethod("refresh", (Class[]) null);
            if (localMethod != null) {
                boolean result = (Boolean) localMethod.invoke(gatt, (Object[]) null);
                if (!result)
					ULog.d(TAG, "refresh failed");
                return result;
            }
        } catch (Exception e) {
			ULog.e(TAG, "An exception occurred while refreshing device cache");
        }
        return false;
    }
}
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

package com.se.idt.ble;

import android.util.Log;


/**
 * If you want to send something to remote add methods here.
 * 
 * begin() : Initialize parameters setXxxx() : Add methods as you wish
 * settingFinished() : Every data is ready. sendTransaction() : Send to remote
 * 
 */
public class TransactionBuilder {

	private static final String TAG = "TransactionBuilder";

	private BluetoothLeService mBleManager = null;

	private static final int MESSAGE_CMD_ERROR_NOT_CONNECTED = -50;

	public TransactionBuilder(BluetoothLeService bm) {
		mBleManager = bm;
	}

	public Transaction makeTransaction() {
		return new Transaction();
	}

	public class Transaction {

		public static final int MAX_MESSAGE_LENGTH = 16;

		// Transaction instance status
		private static final int STATE_NONE = 0; // Instance created
		private static final int STATE_BEGIN = 1; // Initialize transaction
		private static final int STATE_SETTING_FINISHED = 2; // End of setting
																// parameters
		private static final int STATE_TRANSFERED = 3; // End of sending
														// transaction data
		private static final int STATE_ERROR = -1; // Error occurred

		// Transaction parameters
		private int mState = STATE_NONE;
		private byte[] mBuffer = null;
		private String mMsg = null;

		/**
		 * Make new transaction instance
		 */
		public void begin() {
			mState = STATE_BEGIN;
			mMsg = null;
			mBuffer = null;
		}

		/**
		 * Set string to send
		 * 
		 * @param msg
		 *            String to send
		 */
		public void setMessage(String msg) {
			// TODO: do what you want
			mMsg = msg;
		}		/**
		 * Set string to send
		 *
		 * @param msg
		 *            String to send
		 */
		public void setMessage(byte[] msg) {
			// TODO: do what you want
			mState = STATE_SETTING_FINISHED;
			mBuffer = msg;
			Log.d(TAG, ":"+ mBuffer);
		}

		/**
		 * Ready to send data to remote
		 */
		
//		String data ;
		public void settingFinished() {
			mState = STATE_SETTING_FINISHED;
//			data = String.valueOf(mMsg);
			mBuffer =  mMsg.getBytes();// Base64.decode(mMsg, Base64.DEFAULT); //data.getBytes();//convertHexToString(convertStringToHex(mMsg));
			
			Log.d(TAG, ":"+ mBuffer);
		}

		/**
		 * Send packet to remote
		 * 
		 * @return boolean is succeeded
		 */
		public boolean sendTransaction() {
			if (mBuffer == null || mBuffer.length < 1) {
				return false;
			}

			// TODO: For debug. Comment out below lines if you want to see the
			// packets

			/*
			 * if (mBuffer.length > 0) { StringBuilder sb = new StringBuilder();
			 * sb.append("Message : "); for (int i = 0; i < mBuffer.length; i++)
			 * { sb.append(String.format("%03X ", mBuffer[i])); } ULog.d(TAG,
			 * " "); ULog.d(TAG, sb.toString()); }
//			 */
//			if (mBuffer.length > 0) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("Message : ");
//
//				for (int i = 0; i < mBuffer.length; i++) {
//
//					int c = (int) mBuffer[i];
//					sb.append(" "+c);
//					// System.out.print(c);
//				}
//				ULog.d(TAG, sb.toString());
//			}
			if (mState == STATE_SETTING_FINISHED) {
				if (mBleManager != null) {
					// Check that we're actually connected before trying
					// anything
//					if (mBleManager.getState() == BluetoothLeService.STATE_CONNECTED) {
						// Check that there's actually something to send
						if (mBuffer.length > 0) {
							// Get the message bytes and tell the BleManager to
							// write

//							if(MainActivity.alrams_day != 0){
//								mBleManager.write(null,MainActivity.alrams_day & mBuffer);
//							}
							mBleManager.write(null, mBuffer);

							mState = STATE_TRANSFERED;
							return true;
						}
						mState = STATE_ERROR;
//					}
					// Report result
//					mHandler.obtainMessage(MESSAGE_CMD_ERROR_NOT_CONNECTED).sendToTarget();
				}
			}
			return false;
		}

		/**
		 * Get buffers to send to remote
		 */
		public byte[] getPacket() {
			if (mState == STATE_SETTING_FINISHED) {
				return mBuffer;
			}
			return null;
		}

	} // End of class Transaction

	public static byte[] stringToBytesASCII(String str) {
		byte[] b = new byte[str.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) str.charAt(i);
		}
		return b;
	}

}

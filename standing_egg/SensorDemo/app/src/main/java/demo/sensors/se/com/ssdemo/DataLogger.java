/*
Copyright (c) 2013, 2014, Freescale Semiconductor, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
 * Neither the name of Freescale Semiconductor, Inc. nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL FREESCALE SEMICONDUCTOR, INC. BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package demo.sensors.se.com.ssdemo;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import demo.sensors.se.com.ssdemo.MainActivity.GuiState;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
* class to encapsulate all write operations.  This includes both log window and output file.
* <p>This class operates in a separate thread in order to not log down the main GUI thread.
* <p>It gets messages from the write() and cls() functions in class MainActivity.
* @author Michael Stanley
*/
public class DataLogger extends Thread {
	public static Handler handlerToUi;
	public static Handler handlerFromUi;
    public static String fileName = new String("");
    public static long refreshInterval = 200; // ms
    private static boolean loggingWindowUpToDate = true;
    private Handler periodicHandler = new Handler();
	private MainActivity demo;
    private static final Object lock = new Object();
    private static boolean fileLoggingEnabled=false;
    private static ArrayList<String> messageStrings = new ArrayList<String>();
    private static int numMsgs = 0;
    private static int maxMessages = 100;
    private static long maxRecordsLoggedToOutputFile=0;
    private static long numMsgsLoggedToFile = 0;
    private static String fullFileName = null;
    private static BufferedOutputStream outputStream = null;

	DataLogger(MainActivity demo, Handler handlerToUi) {
		DataLogger.handlerToUi = handlerToUi; 
		this.demo = demo;
		String defaultFileName  = demo.getString(R.string.defaultOutputFile);
        fileName = demo.myPrefs.getString("fileName", defaultFileName);
        String mrs = demo.myPrefs.getString("maxFileMsgs", new String("10000"));
        maxRecordsLoggedToOutputFile = Long.parseLong(mrs);
	}

	@Override
	public synchronized void run() {
		// Message format:
		// what=1: unconditional log statement
		// what=2: log only when the GUI is set to LOGGING
		// what=3: clear the buffer, close the output file
		// arg1: 0=no file logging, anything else=file logging
		Looper.prepare();


		handlerFromUi = new Handler() {
			public void handleMessage(Message msg) {
				String s = null;
				synchronized(lock) {
					fileLoggingEnabled = (msg.arg1!=0);
					switch (msg.what) {
					case 1: // always log
						s = msg.obj.toString();
						Log.i("UILOG", s);
						updateLoggingWindowQueue(s); 
						writeToFile(s);	// file may or may not be updated depending upon other factors						
						break;
					case 2: // log only if logging window is active
						s = msg.obj.toString();
						if (demo.guiState==GuiState.LOGGING) {  // only update when the log window is visible
							updateLoggingWindowQueue(s);
						}
						writeToFile(s);	// file may or may not be updated depending upon other factors					
						break;
					case 3: // Clear
						numMsgs=0;
						messageStrings.clear();
						loggingWindowUpToDate = true;
						closeFile(); // will automatically be reopened & overwritten later if needed
						numMsgsLoggedToFile = 0;
						break;
					default: // not currently used
					}
				}
			}
		};
		Looper.loop();
	}
	private void updateLoggingWindowQueue(String str) {
		try {
			if (numMsgs < maxMessages) {
				numMsgs = numMsgs + 1;
			} else {
				messageStrings.remove(0);
			}
			messageStrings.add(str);
		} catch (Throwable t) {
			// We'll just loose any message with problems
			t.printStackTrace();
		}
		if (loggingWindowUpToDate) {
			periodicHandler.postDelayed(logUpdaterTask,  refreshInterval);
			loggingWindowUpToDate = false;
		}
	}

	private Runnable logUpdaterTask = new Runnable() {
		public void run() {
			int upper, lower;
			synchronized(lock) {
				upper = (int) numMsgsLoggedToFile/1024;
				lower = (int) numMsgsLoggedToFile - 1024*upper;
				if (loggingWindowUpToDate) {
					handlerToUi.sendMessage(handlerToUi.obtainMessage(2,upper,lower,null));  
				} else {
					String newTextView="";
					for (String s:messageStrings) {
						newTextView+=s;
					}
					// now send new contents of text view back to the main UI
					handlerToUi.sendMessage(handlerToUi.obtainMessage(1,upper,lower,newTextView));  
					loggingWindowUpToDate = true;
				}
			}
			try {
				if (outputStream!=null) outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public Handler getHandler() {
		return(handlerFromUi);
	}
	private boolean outputFileAvailable() {
		boolean sts = false;
		if (outputStream!=null) {
			sts = true;
		} else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  // do we have external storage
			if (fullFileName==null) {  // have we previously gotten the external file path
				fullFileName = MyUtils.getFilePath(fileName);
			}			
			if ((outputStream==null)&&(fullFileName!=null)) {
				try {
					outputStream = new BufferedOutputStream(new FileOutputStream(fullFileName));
					numMsgsLoggedToFile=0;
				} catch (FileNotFoundException e){
					outputStream=null;
					e.printStackTrace();
				}
			}
		} else {
			outputStream=null;
		}
		return(sts);
	}
	private boolean writeToFile(String s) {
		boolean sts = false;
		if (fileLoggingEnabled) {
			if (numMsgsLoggedToFile <= maxRecordsLoggedToOutputFile) {
				if (outputFileAvailable()) {
					try {
						outputStream.write(s.getBytes());
						numMsgsLoggedToFile = numMsgsLoggedToFile + 1;
						sts = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return(sts);
	}
	private void closeFile() {
		if (outputStream!=null) {
			try {
				outputStream.close();
				outputStream=null; // required to get back into file initiation later if needed
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

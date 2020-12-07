package com.example.newdrawtest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.example.newdrawtest.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class TwoFragment extends Fragment implements OnClickListener, CompoundButton.OnCheckedChangeListener {

	socketTask NetworkThread01;
	udpSocketTask NetworkThread02;
	DataMode data02;

	boolean  bThreadSwitch01 = false, cThreadSwitch01 = false;
	boolean exeFlag01 = false;
	Button button;
	Switch aSwitch1, aSwitch2, aSwitch3, aSwitch4;
	boolean bSwitch1 = false, bSwitch2 = false, bSwitch3 = false, bSwitch4 = false;
	ImageView imageView;

	// WIFI 확인
	String ipAddress;
	int serverIp;
	String serverIP_sensor = "192.168.1.88";

	//Calibration 
	private float acc_magnitude;
	private  int tempX, tempY, tempZ;

	//Test
	public static int testX = 0;
	public static int testY = 0;
	public static int testZ = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.textnbtn, container, false);

		data02 = new DataMode();

		button = (Button)getActivity().findViewById(R.id.bt_ok);
		button.setOnClickListener(this);

		aSwitch1 = (Switch)view.findViewById(R.id.switch1);
		aSwitch2 = (Switch)view.findViewById(R.id.switch2);
		aSwitch3 = (Switch)view.findViewById(R.id.switch3);
		aSwitch4 = (Switch)view.findViewById(R.id.switch4);
		aSwitch1.setOnCheckedChangeListener(this);
		aSwitch2.setOnCheckedChangeListener(this);
		aSwitch3.setOnCheckedChangeListener(this);
		aSwitch4.setOnCheckedChangeListener(this);

		imageView = (ImageView)view.findViewById(R.id.imageView);
		
		Animation animFade = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
		animFade.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				// when fadeout animation ends, fade in your second image
//				imageView.setBackgroundResource(R.drawable.standing);
				imageView.startAnimation(animation);
			}
		});

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.bt_ok:
				if(!exeFlag01){

					// network 연결 상태 확인

					try {

						ConnectivityManager conMan = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);


						State wifi = conMan.getNetworkInfo(1).getState(); // wifi

						if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {

							//Toast.makeText(getApplicationContext(), "와이파이연결", Toast.LENGTH_SHORT).show();

							WifiManager wm = (WifiManager)getActivity().getSystemService("wifi");

							DhcpInfo dhcpInfo = wm.getDhcpInfo() ;

							serverIp = dhcpInfo.gateway;

							ipAddress = String.format(

									"%d.%d.%d.%d", (serverIp & 0xff), (serverIp >> 8 & 0xff),(serverIp >> 16 & 0xff),(serverIp >> 24 & 0xff));

							if(ipAddress.equals(serverIP_sensor)){
								//Toast.makeText(getApplicationContext(), "센서서버에 접속 완료", Toast.LENGTH_SHORT).show();

								exeFlag01=true;
								///
								if(bThreadSwitch01==false){

									bThreadSwitch01=true;
									cThreadSwitch01=true;

									if(android.os.Build.VERSION.SDK_INT > 9) {

										StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

										StrictMode.setThreadPolicy(policy);

									}
									button.setText("Pause");
//							NetworkThread01 = new socketTask();
//						    NetworkThread01.execute();
									NetworkThread02 = new udpSocketTask();
									NetworkThread02.execute();
								}

							}else{
								Toast.makeText(getActivity(), "서버 연결 상태를 확인하세요.", Toast.LENGTH_SHORT).show();
								exeFlag01=false;
							}
						}else{
							Toast.makeText(getActivity(), "WIFI 연결 상태를 확인하세요", Toast.LENGTH_SHORT).show();
							exeFlag01=false;
						}
					} catch (NullPointerException e) {
					}

				}else{

					if(bThreadSwitch01==true){

						bThreadSwitch01=false;
						cThreadSwitch01=false;

						//NetworkThread01.cancel(true);
						NetworkThread02.cancel(true);
					}

					button.setText("Play");
					exeFlag01=false;
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.switch1:
				bSwitch1 = isChecked;
				if(isChecked) {
					aSwitch2.setChecked(!isChecked);
					aSwitch3.setChecked(!isChecked);
					aSwitch4.setChecked(!isChecked);
				}
				break;
			case R.id.switch2:
				bSwitch2 = isChecked;
				if(isChecked) {
					aSwitch1.setChecked(!isChecked);
					aSwitch3.setChecked(!isChecked);
					aSwitch4.setChecked(!isChecked);
				}
				break;
			case R.id.switch3:
				bSwitch3 = isChecked;
				if(isChecked) {
					aSwitch1.setChecked(!isChecked);
					aSwitch2.setChecked(!isChecked);
					aSwitch4.setChecked(!isChecked);
				}
				break;
			case R.id.switch4:
				bSwitch4 = isChecked;
				if(isChecked) {
					aSwitch1.setChecked(!isChecked);
					aSwitch2.setChecked(!isChecked);
					aSwitch3.setChecked(!isChecked);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if(bThreadSwitch01==true){

			bThreadSwitch01=false;
			cThreadSwitch01=false;

			//NetworkThread01.cancel(true);
			NetworkThread02.cancel(true);
		}

		button.setText("Play");
		exeFlag01=false;
	}

	private class socketTask extends AsyncTask<Integer, Integer, Integer>{

		private static final String serverIP = "192.168.1.55"; // ex: 192.168.0.100
		private static final int serverPort = 48555; // ex: 5555
		private float  valueX = 0, valueY = 0, valueZ = 0, MvalueX = 0, MvalueY = 0, MvalueZ = 0;

		InetAddress serverAddr;
		Socket socket;
		BufferedInputStream in;
		private int return_msg;

		// �뜝�룞�삕�땱占� �뜝�룞�삕�뜝�룞�삕
		private int Cnt_avg = 0;
		private int[] buffer_tempX = {0, 0, 0};
		private int[] buffer_tempY = {0, 0, 0};
		private int[] buffer_tempZ = {0, 0, 0};

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			try {
				serverAddr = InetAddress.getByName(serverIP);
				socket = new Socket(serverAddr, serverPort);
				socket.setSoTimeout(10);
				in = new BufferedInputStream(socket.getInputStream());

			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			//int x = 0;	//입력되는 순서를 나타내기 위한 값

			//bThreadSwitch=true;
			while(bThreadSwitch01){

				try {
					try{
						return_msg = in.read();
					} catch (SocketTimeoutException e){
						e.printStackTrace();
					} catch ( NullPointerException e) {

					}

					switch(data02.MODE){

						case 1 : data02.m_pkst(return_msg); break;
						case 2 : data02.m_pklt(return_msg); break;
						default : data02.m_default(return_msg); break;

					}
					if(data02.ValueFlag01){
						valueX = data02.test01;
						valueY = data02.test02;
						valueZ = data02.test03;
						MvalueX = data02.test04;
						MvalueY = data02.test05;
						MvalueZ = data02.test06;


						acc_magnitude = (float)Math.sqrt((valueX*valueX)+(valueY*valueY)+(valueZ*valueZ));
						//acc_magnitude = 16384;


						tempX = (int)(Math.asin(valueX / acc_magnitude)*(180/3.14159265358979323846264338327));
						tempY = (int)(Math.asin(valueY / acc_magnitude)*(180/3.14159265358979323846264338327));
						tempZ = (int)(Math.acos(valueZ / acc_magnitude)*(180/3.14159265358979323846264338327));

						//tempY = tempY+5;
						//tempZ = tempZ-5;

						switch (Cnt_avg) {
							case 0:
								buffer_tempX[0] = tempX;
								buffer_tempY[0] = tempY;
								buffer_tempZ[0] = tempZ;

								break;

							case 1:
								buffer_tempX[1] = tempX;
								buffer_tempY[1] = tempY;
								buffer_tempZ[1] = tempZ;

								break;

							case 2:
								buffer_tempX[2] = tempX;
								buffer_tempY[2] = tempY;
								buffer_tempZ[2] = tempZ;
								Cnt_avg = -1;

								testY = (buffer_tempX[0] + buffer_tempX[1] + buffer_tempX[2])/3*(-1);
								testX = (buffer_tempY[0] + buffer_tempY[1] + buffer_tempY[2])/3;
								testZ = (buffer_tempZ[0] + buffer_tempZ[1] + buffer_tempZ[2])/3;

//						testX = tempY;
//			    		testY = tempX;
//			    		testZ = tempZ;
								break;

							default:
								break;
						}

						Cnt_avg++;
	
		    		/*
		    		if(tempX>90)
		    			tempX = 180 - tempX;
		    		if(tempX<-90)
		    			tempX = -180 - tempX;
		    		
		    		if(tempY>90)
		    			tempY = 180 - tempY;
		    		if(tempY<-90)
		    			tempY = -180 - tempY;
		    		 */
//		    		testX = tempY;
//		    		testY = tempX;
//		    		testZ = tempZ;


						publishProgress();
						data02.ValueFlag01=false;

					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch ( NullPointerException e) {

				}

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			if(tempY==-5 || tempY==-4){
				tempY = 0;
			}

			if(tempZ==5 || tempZ==4){
				tempZ = 0;
			}
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();

			bThreadSwitch01 = false;

			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch ( NullPointerException e) {

			}

			if(cThreadSwitch01==true){

				bThreadSwitch01 = true;
				NetworkThread01 = new socketTask();
				NetworkThread01.execute();
			}
		}
	}

	private class udpSocketTask  extends AsyncTask<Integer, Integer, Integer>{
		private static final String serverIP = "192.168.1.88"; // ex: 192.168.0.100
		private static final int serverPort = 48688; // ex: 5555
		private float  valueX = 0, valueY = 0, valueZ = 0, MvalueX = 0, MvalueY = 0, MvalueZ = 0;
		private int x = 0;

		InetAddress serverAddr;
		DatagramPacket outPacket;
		DatagramPacket inPacket;
		DatagramSocket dSock;

		// 흔들림 보정
		private int Cnt_avg = 0;
		private int[] buffer_tempX = {0, 0, 0};
		private int[] buffer_tempY = {0, 0, 0};
		private int[] buffer_tempZ = {0, 0, 0};
		
		private int motionBefore = 0;
		private int motion = 0;
		private boolean cmdSwitch = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			try{
				if(dSock != null) {
					dSock.close();
				}

				serverAddr = InetAddress.getByName(serverIP);
				dSock = new DatagramSocket(serverPort);
				byte[] data = new byte[72];
				outPacket = new DatagramPacket(data, data.length, serverAddr, serverPort);
				inPacket = new DatagramPacket(data, data.length);
				dSock.send(outPacket);
			}catch(UnknownHostException e){
				e.getStackTrace();
			}catch(SocketException se){
				se.getStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			x = 0;
			while(bThreadSwitch01){
				try {
					dSock.receive(inPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String value = new String(inPacket.getData());
				int sgaValue01, sgaValue02, sgaValue03, sgaValue04, sgaValue05, sgaValue06;
				if("STEG".equals(value.substring(0, 4))) {
					// SGA100 X, Y, Z setting
					sgaValue02 = Integer.parseInt(value.substring(4, 6), 16);
					sgaValue01 = Integer.parseInt(value.substring(6, 8), 16);
					sgaValue04 = Integer.parseInt(value.substring(8, 10), 16);
					sgaValue03 = Integer.parseInt(value.substring(10, 12), 16);
					sgaValue06 = Integer.parseInt(value.substring(12, 14), 16);
					sgaValue05 = Integer.parseInt(value.substring(14, 16), 16);
					try {
						motion = Integer.parseInt(value.substring(16, 17));
					}catch(NumberFormatException e){
						motion = 0;
					}
					data02.m_pkst(sgaValue01);
					data02.m_pkst(sgaValue02);
					data02.m_pkst(sgaValue03);
					data02.m_pkst(sgaValue04);
					data02.m_pkst(sgaValue05);
					data02.m_pkst(sgaValue06);
					valueX = data02.test01;
					valueY = data02.test02;
					valueZ = data02.test03;

					acc_magnitude = (float)Math.sqrt((valueX*valueX)+(valueY*valueY)+(valueZ*valueZ));

					tempX = (int)(Math.asin(valueX / acc_magnitude)*(180/3.14159265358979323846264338327));
					tempY = (int)(Math.asin(valueY / acc_magnitude)*(180/3.14159265358979323846264338327));
					tempZ = (int)(Math.acos(valueZ / acc_magnitude)*(180/3.14159265358979323846264338327));

					switch (Cnt_avg) {
						case 0:
							buffer_tempX[0] = tempX;
							buffer_tempY[0] = tempY;
							buffer_tempZ[0] = tempZ;

							break;

						case 1:
							buffer_tempX[1] = tempX;
							buffer_tempY[1] = tempY;
							buffer_tempZ[1] = tempZ;

							break;

						case 2:
							buffer_tempX[2] = tempX;
							buffer_tempY[2] = tempY;
							buffer_tempZ[2] = tempZ;
							Cnt_avg = -1;

							testY = (buffer_tempX[0] + buffer_tempX[1] + buffer_tempX[2])/3*(-1);
							testX = (buffer_tempY[0] + buffer_tempY[1] + buffer_tempY[2])/3;
							testZ = (buffer_tempZ[0] + buffer_tempZ[1] + buffer_tempZ[2])/3;
							break;

						default:
							break;
					}

					Cnt_avg++;
					publishProgress();
					data02.ValueFlag01=false;
				}
				
				if("CMD_ACK".equals(value.substring(0, 8))) {
					cmdSwitch = false;
				}
				
				if(cmdSwitch) {
					if(bSwitch1) {
						try {
							udpDataSend("SEAHIFREQ");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(bSwitch2) {
						try {
							udpDataSend("SEAFUFREQ");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					if(bSwitch3) {
						try {
							udpDataSend("SEAMOFREQ");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					if(bSwitch4) {
						try {
							udpDataSend("SEALOFREQ");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			if(tempY==-5 || tempY==-4){
				tempY = 0;
			}

			if(tempZ==5 || tempZ==4){
				tempZ = 0;
			}
			
			if(motion != motionBefore) {
				if(motion == 9) {
					imageView.setBackgroundResource(R.drawable.motionoff);
				}else if(motion == 0) {
					imageView.setBackgroundResource(R.drawable.standing);
				}else if(motion == 1) {
					imageView.setBackgroundResource(R.drawable.slowwalking);
				}else if(motion == 2) {
					imageView.setBackgroundResource(R.drawable.walking);
				}else if(motion == 3) {
					imageView.setBackgroundResource(R.drawable.running);
				}else if(motion == 4) {
					imageView.setBackgroundResource(R.drawable.sitting);
				}else if(motion == 5) {
					imageView.setBackgroundResource(R.drawable.pushup);
				}else if(motion == 6) {
					imageView.setBackgroundResource(R.drawable.situp);
				}else if(motion == 7) {
					imageView.setBackgroundResource(R.drawable.jumpingjack);
				}
			}
			
			motionBefore = motion;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			bThreadSwitch01 = false;

			if(dSock != null) {
				dSock.close();
			}else {
				handler.sendEmptyMessage(0);
			}

			if(cThreadSwitch01==true){
				if(NetworkThread02 != null) {
					bThreadSwitch01 = true;
					NetworkThread02 = new udpSocketTask();
					NetworkThread02.execute();
				}
			}
		}

		private Handler handler = new Handler() {
			public void hanleMessage(Message msg) {
				Toast.makeText(getActivity(), "The wifi connection is unstable. Please reboot the server", Toast.LENGTH_SHORT).show();
				super.handleMessage(msg);
			}
		};

		private void udpDataSend(String comm) throws IOException {
			if(NetworkThread02 != null) {
				String data = String.valueOf(comm);
				if(outPacket != null) { 
					byte[] by = new byte[72];
					outPacket = new DatagramPacket(by, by.length, serverAddr, serverPort);
				}
				outPacket.setData(data.getBytes());
				dSock.send(outPacket);
				cmdSwitch = true;
			}
		};
	}
}

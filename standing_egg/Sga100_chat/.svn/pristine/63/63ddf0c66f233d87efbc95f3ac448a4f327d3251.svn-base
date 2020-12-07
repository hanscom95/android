package com.example.test1218;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OneFragment extends Fragment implements OnClickListener {

	socketTask NetworkThread01;
	udpSocketTask NetworkThread02;
	DataMode data01;
	RealTimeChart rtc01;
	RealTimeChart rtc02;
	RealTimeChart rtc03;
	
	boolean  bThreadSwitch01 = false, cThreadSwitch01 = false;
	boolean exeFlag01 = false;
	
	private static LinearLayout chartLayout;
	private static LinearLayout tilLayout;
	
	// WIFI 확인
	String ipAddress;
	int serverIp;
	//String serverIP_sensor = "192.168.1.4";

	TextView Texttest01, Texttest02, Texttest03, Texttest04, Texttest05, Texttest06;
	TextView Texttest07, Texttest08, Texttest09, Texttest10, Texttest11, Texttest12;
	TextView Texttest13, Texttest14, Texttest15, Texttest16, Texttest17, Texttest18;
	TextView Texttest19, Texttest20, Texttest21, Texttest22, Texttest23, Texttest24;
	Button conButton, tiltButton;
	LinearLayout sgaLayout, bmaLayout, lisLayout, mpuLayout, textLayout, checkLayout;
	static CheckBox intervalCheckbox, sgaCheckBox, bma250CheckBox, lis331dlhCheckBox, mpu6500CheckBox;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.onefragment, container, false);
		
		//getActionBar().setIcon(R.drawable.fhrh);
		getActivity().getActionBar().setIcon(R.drawable.actionbar_logo);
		getActivity().getActionBar().setTitle("Sensor Chart Data");

		rtc01 = new RealTimeChart(getActivity());
		rtc02 = new RealTimeChart(getActivity());
		rtc03 = new RealTimeChart(getActivity());

		LinearLayout realTimeLayout = (LinearLayout)view.findViewById(R.id.RealTimelayout);
		LinearLayout realTimeLayout2 = (LinearLayout)view.findViewById(R.id.RealTimelayout2);
		LinearLayout realTimeLayout3 = (LinearLayout)view.findViewById(R.id.RealTimelayout3);

		//	2. 화면에 뷰를 등록
		realTimeLayout.addView(rtc01); // RealTimeChart 뷰를 화면에 추가
		realTimeLayout2.addView(rtc02); // RealTimeChart 뷰를 화면에 추가
		realTimeLayout3.addView(rtc03); // RealTimeChart 뷰를 화면에 추가
		//	3. 뷰의 크기를 설정
		rtc01.Initialize(700, 1300, 1); // RealTimeChart뷰 초기화(넓이, 높이)  **뷰를 등록(addView) 후 Initialize()를 호출해야 적용됨
		rtc02.Initialize(700, 1300, 2); // RealTimeChart뷰 초기화(넓이, 높이)  **뷰를 등록(addView) 후 Initialize()를 호출해야 적용됨
		rtc03.Initialize(700, 1300, 3); // RealTimeChart뷰 초기화(넓이, 높이)  **뷰를 등록(addView) 후 Initialize()를 호출해야 적용됨
		
		data01 = new DataMode();

		chartLayout = (LinearLayout)view.findViewById(R.id.chartLayout);
		tilLayout = (LinearLayout)view.findViewById(R.id.tiltLayout);
		sgaLayout = (LinearLayout)view.findViewById(R.id.sgaLayout);
		bmaLayout = (LinearLayout)view.findViewById(R.id.bmaLayout);
		lisLayout = (LinearLayout)view.findViewById(R.id.lisLayout);
		mpuLayout = (LinearLayout)view.findViewById(R.id.mpuLayout);
		
		//sga
		Texttest01 = (TextView)view.findViewById(R.id.Texttest01);
		Texttest02 = (TextView)view.findViewById(R.id.Texttest02);
		Texttest03 = (TextView)view.findViewById(R.id.Texttest03);
		Texttest04 = (TextView)view.findViewById(R.id.Texttest04);
		Texttest05 = (TextView)view.findViewById(R.id.Texttest05);
		Texttest06 = (TextView)view.findViewById(R.id.Texttest06);
		
		//bma
		Texttest07 = (TextView)view.findViewById(R.id.Texttest07);
		Texttest08 = (TextView)view.findViewById(R.id.Texttest08);
		Texttest09 = (TextView)view.findViewById(R.id.Texttest09);
		Texttest10 = (TextView)view.findViewById(R.id.Texttest10);
		Texttest11 = (TextView)view.findViewById(R.id.Texttest11);
		Texttest12 = (TextView)view.findViewById(R.id.Texttest12);
		
		//lis
		Texttest13 = (TextView)view.findViewById(R.id.Texttest13);
		Texttest14 = (TextView)view.findViewById(R.id.Texttest14);
		Texttest15 = (TextView)view.findViewById(R.id.Texttest15);
		Texttest16 = (TextView)view.findViewById(R.id.Texttest16);
		Texttest17 = (TextView)view.findViewById(R.id.Texttest17);
		Texttest18 = (TextView)view.findViewById(R.id.Texttest18);
		
		//mpu
		Texttest19 = (TextView)view.findViewById(R.id.Texttest19);
		Texttest20 = (TextView)view.findViewById(R.id.Texttest20);
		Texttest21 = (TextView)view.findViewById(R.id.Texttest21);
		Texttest22 = (TextView)view.findViewById(R.id.Texttest22);
		Texttest23 = (TextView)view.findViewById(R.id.Texttest23);
		Texttest24 = (TextView)view.findViewById(R.id.Texttest24);
		
		textLayout = (LinearLayout)view.findViewById(R.id.textLayout);
		checkLayout = (LinearLayout)view.findViewById(R.id.checkLayout);
		
		intervalCheckbox = (CheckBox)view.findViewById(R.id.intervalCheckBox);
		sgaCheckBox = (CheckBox)view.findViewById(R.id.sgaCheckBox);
		bma250CheckBox = (CheckBox)view.findViewById(R.id.bma250CheckBox);
		lis331dlhCheckBox = (CheckBox)view.findViewById(R.id.lis331dlhCheckBox);
		mpu6500CheckBox = (CheckBox)view.findViewById(R.id.mpu6500CheckBox);
		
		conButton = (Button) view.findViewById(R.id.bt_ok);
		tiltButton = (Button) view.findViewById(R.id.bt_ok2);
		conButton.setOnClickListener(this);
		tiltButton.setOnClickListener(this);

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
					
					if(ipAddress.equals(MainActivity.serverIp)){
						exeFlag01=true;
						
						if(bThreadSwitch01==false){
							
							bThreadSwitch01=true;
							cThreadSwitch01=true;
							
							if(android.os.Build.VERSION.SDK_INT > 9) {

							    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

							        StrictMode.setThreadPolicy(policy);

							}
							conButton.setText("Stop");
							checkLayout.setVisibility(View.GONE);
							textLayout.setVisibility(View.VISIBLE);
							
							if(sgaCheckBox.isChecked()) {
								sgaLayout.setVisibility(View.VISIBLE);
							}else {
								sgaLayout.setVisibility(View.GONE);
							}
							
							if(bma250CheckBox.isChecked()) {
								bmaLayout.setVisibility(View.VISIBLE);
							}else {
								bmaLayout.setVisibility(View.GONE);
							}
							
							if(lis331dlhCheckBox.isChecked()) {
								lisLayout.setVisibility(View.VISIBLE);
							}else {
								lisLayout.setVisibility(View.GONE);
							}
							
							if(mpu6500CheckBox.isChecked()) {
								mpuLayout.setVisibility(View.VISIBLE);
							}else {
								mpuLayout.setVisibility(View.GONE);
							}
							
//							NetworkThread01 = new socketTask();
//						    NetworkThread01.execute();
							NetworkThread02 = new udpSocketTask();
							NetworkThread02.execute();
						}
						
					}else{
						Toast.makeText(getActivity(), "Check the server connection status", Toast.LENGTH_SHORT).show();
						exeFlag01=false;
					}
				}else{
					Toast.makeText(getActivity(), "Please check the wifi connection", Toast.LENGTH_SHORT).show();
					exeFlag01=false;
					}
				} catch (NullPointerException e) {
				}
			}else{
				
				if(bThreadSwitch01==true){
					
					bThreadSwitch01=false;
					cThreadSwitch01=false;
					
					NetworkThread02.cancel(true);
//					NetworkThread01.cancel(true);
				}
				
				conButton.setText("Connect");
				textLayout.setVisibility(View.GONE);
				checkLayout.setVisibility(View.VISIBLE);
				exeFlag01=false;
			}
			break;
			
		case R.id.bt_ok2:
			if(tilLayout.getVisibility() == View.GONE) {
				tilLayout.setVisibility(View.VISIBLE);
			}else {
				tilLayout.setVisibility(View.GONE);
			}
			break;
			
		default:
			break;
		}

	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(bThreadSwitch01==true){
			
			bThreadSwitch01=false;
			cThreadSwitch01=false;
			
//			NetworkThread01.cancel(true);
			NetworkThread02.cancel(true);
		}
		
		conButton.setText("Connect");
		exeFlag01=false;
		
	}

	// socket udp로 변경하여 사용하지 않는 중
	private class socketTask extends AsyncTask<Integer, Integer, Integer>{
		  
		  private static final String serverIP = "192.168.1.4"; // ex: 192.168.0.100
		  private static final int serverPort = 8014; // ex: 5555
		  private int  valueX = 0, valueY = 0, valueZ = 0;
		
		  InetAddress serverAddr;
	      Socket socket;
	      BufferedInputStream in;
	      private int return_msg;
	      
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
			int x = 0;	//입력되는 순서를 나타내기 위한 값
			
			//bThreadSwitch=true;
			while(bThreadSwitch01){
				
				try {
					
					try{
						return_msg = in.read();
					} catch (SocketTimeoutException e){
					}

					switch(data01.MODE){
					
					case 1 : data01.m_pkst(return_msg); break;
					case 2 : data01.m_pklt(return_msg); break;
					default : data01.m_default(return_msg); break;
					
					}
					if(data01.ValueFlag01){

						valueX = (int)data01.test01; 
						valueY = (int)data01.test02;
						valueZ = (int)data01.test03;
						
						if(!RealTimeChart.bDrawing){
							rtc01.addData(x++, valueX, 0, 0);
							rtc02.addData(x, valueY, 0, 0);
							rtc03.addData(x, valueZ, 0, 0);
						}
						
						publishProgress();
						data01.ValueFlag01=false;
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		
				Texttest01.setText("X-axis : "+ valueX);
				Texttest02.setText("Y-axis : "+ valueY);
				Texttest03.setText("Z-axis : "+ valueZ);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			bThreadSwitch01 = false;
			
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(cThreadSwitch01==true){
				
				bThreadSwitch01 = true;
				NetworkThread01 = new socketTask();
				NetworkThread01.execute();
			}
		}
	}
	
	private class udpSocketTask  extends AsyncTask<Integer, Integer, Integer>{
		  private final String serverIP = MainActivity.serverIp; // ex: 192.168.1.31
		  private final int serverPort = MainActivity.serverPort; // ex: 48601
		  private int  sgaValueX = 0, sgaValueY = 0, sgaValueZ = 0;
		  private int  bmaValueX = 0, bmaValueY = 0, bmaValueZ = 0;
		  private int  bmaCValueX = 0, bmaCValueY = 0, bmaCValueZ = 0;
		  private int  lisValueX = 0, lisValueY = 0, lisValueZ = 0;
		  private int  mpuValueX = 0, mpuValueY = 0, mpuValueZ = 0;
		  private int x = 0;
		
		  InetAddress serverAddr;
		  DatagramPacket outPacket;
		  DatagramPacket inPacket;
		  DatagramSocket dSock;
	      
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			try{
				if(dSock != null) {
					dSock.close();
				}
				
				serverAddr = InetAddress.getByName(serverIP);
				dSock = new DatagramSocket(serverPort);
				byte[] data = new byte[64];
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
				int bmaValue01, bmaValue02, bmaValue03, bmaValue04, bmaValue05, bmaValue06;
				int lisValue01, lisValue02, lisValue03, lisValue04, lisValue05, lisValue06;
				int mpuValue01, mpuValue02, mpuValue03, mpuValue04, mpuValue05, mpuValue06;
				if("PKST".equals(value.substring(0, 4)) || "STEG".equals(value.substring(0, 4))) {
					// SGA100 X, Y, Z setting
					sgaValue01 = Integer.parseInt(value.substring(4, 6), 16);
					sgaValue02 = Integer.parseInt(value.substring(6, 8), 16);
					sgaValue03 = Integer.parseInt(value.substring(8, 10), 16);
					sgaValue04 = Integer.parseInt(value.substring(10, 12), 16);
					sgaValue05 = Integer.parseInt(value.substring(12, 14), 16);
					sgaValue06 = Integer.parseInt(value.substring(14, 16), 16);
					data01.m_pkst(sgaValue02);
					data01.m_pkst(sgaValue01);
					data01.m_pkst(sgaValue04);
					data01.m_pkst(sgaValue03);
					data01.m_pkst(sgaValue06);
					data01.m_pkst(sgaValue05);
					sgaValueX = (int)data01.test01; 
					sgaValueY = (int)data01.test02;
					sgaValueZ = (int)data01.test03;

					// BMA250E X, Y, Z setting
					bmaValue01 = Integer.parseInt(value.substring(16, 18), 16);
					bmaValue02 = Integer.parseInt(value.substring(18, 20), 16);
					bmaValue03 = Integer.parseInt(value.substring(20, 22), 16);
					bmaValue04 = Integer.parseInt(value.substring(22, 24), 16);
					bmaValue05 = Integer.parseInt(value.substring(24, 26), 16);
					bmaValue06 = Integer.parseInt(value.substring(26, 28), 16);
					data01.m_pkbma(bmaValue02);
					data01.m_pkbma(bmaValue01);
					data01.m_pkbma(bmaValue04);
					data01.m_pkbma(bmaValue03);
					data01.m_pkbma(bmaValue06);
					data01.m_pkbma(bmaValue05);
					bmaValueX = (int)data01.test01; 
					bmaValueY = (int)data01.test02;
					bmaValueZ = (int)data01.test03;

					data01.m_pkst(bmaValue02);
					data01.m_pkst(bmaValue01);
					data01.m_pkst(bmaValue04);
					data01.m_pkst(bmaValue03);
					data01.m_pkst(bmaValue06);
					data01.m_pkst(bmaValue05);
					bmaCValueX = (int)data01.test01; 
					bmaCValueY = (int)data01.test02;
					bmaCValueZ = (int)data01.test03;

					// LIS331DLH X, Y, Z setting
					lisValue01 = Integer.parseInt(value.substring(28, 30), 16);
					lisValue02 = Integer.parseInt(value.substring(30, 32), 16);
					lisValue03 = Integer.parseInt(value.substring(32, 34), 16);
					lisValue04 = Integer.parseInt(value.substring(34, 36), 16);
					lisValue05 = Integer.parseInt(value.substring(36, 38), 16);
					lisValue06 = Integer.parseInt(value.substring(38, 40), 16);
					data01.m_pkst(lisValue02);
					data01.m_pkst(lisValue01);
					data01.m_pkst(lisValue04);
					data01.m_pkst(lisValue03);
					data01.m_pkst(lisValue06);
					data01.m_pkst(lisValue05);
					lisValueX = (int)data01.test01; 
					lisValueY = (int)data01.test02;
					lisValueZ = (int)data01.test03;

					mpuValue01 = Integer.parseInt(value.substring(42, 44), 16);
					mpuValue02 = Integer.parseInt(value.substring(40, 42), 16);
					mpuValue03 = Integer.parseInt(value.substring(46, 48), 16);
					mpuValue04 = Integer.parseInt(value.substring(44, 46), 16);
					mpuValue05 = Integer.parseInt(value.substring(50, 52), 16);
					mpuValue06 = Integer.parseInt(value.substring(48, 50), 16);
					data01.m_pkst(mpuValue01);
					data01.m_pkst(mpuValue02);
					data01.m_pkst(mpuValue03);
					data01.m_pkst(mpuValue04);
					data01.m_pkst(mpuValue05);
					data01.m_pkst(mpuValue06);
					mpuValueX = (int)data01.test01; 
					mpuValueY = (int)data01.test02;
					mpuValueZ = (int)data01.test03;
					
					if(data01.ValueFlag01){
						if(intervalCheckbox.isChecked()) {
							if(!RealTimeChart.bDrawing){
								rtc01.addData(x++, sgaValueX+20000, bmaCValueX+8000, lisValueX-8000, mpuValueX-20000);
								rtc02.addData(x, sgaValueY+20000, bmaCValueY+8000, lisValueY-8000, mpuValueY-20000);
								rtc03.addData(x, sgaValueZ+4000, bmaCValueZ-8000, lisValueZ-24000, mpuValueZ-37000);
							}
						}else {
							if(!RealTimeChart.bDrawing){
								rtc01.addData(x++, sgaValueX, bmaCValueX, lisValueX, mpuValueX);
								rtc02.addData(x, sgaValueY, bmaCValueY, lisValueY, mpuValueY);
								rtc03.addData(x, sgaValueZ, bmaCValueZ, lisValueZ, mpuValueZ);
							}
						}
						
						publishProgress();
						data01.ValueFlag01=false;
					}
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			float acc_magnitude = (float)Math.sqrt((sgaValueX*sgaValueX)+(sgaValueY*sgaValueY)+(sgaValueZ*sgaValueZ));
			float tempX = (float)(Math.asin(sgaValueX / acc_magnitude)*(180/3.14159265358979323846264338327));
			float tempY = (float)(Math.asin(sgaValueY / acc_magnitude)*(180/3.14159265358979323846264338327));
			float tempZ = (float)(Math.acos(sgaValueZ / acc_magnitude)*(180/3.14159265358979323846264338327));
			Texttest01.setText("X-axis : "+ sgaValueX);
			Texttest02.setText("Y-axis : "+ sgaValueY);
			Texttest03.setText("Z-axis : "+ sgaValueZ);
			
			float acc_magnitude2 = (float)Math.sqrt((bmaValueX*bmaValueX)+(bmaValueY*bmaValueY)+(bmaValueZ*bmaValueZ));
			float temp2X = (float)(Math.asin(bmaValueX / acc_magnitude2)*(180/3.14159265358979323846264338327));
			float temp2Y = (float)(Math.asin(bmaValueY / acc_magnitude2)*(180/3.14159265358979323846264338327));
			float temp2Z = (float)(Math.acos(bmaValueZ / acc_magnitude2)*(180/3.14159265358979323846264338327));
			Texttest07.setText("X-axis : "+ bmaValueX);
			Texttest08.setText("Y-axis : "+ bmaValueY);
			Texttest09.setText("Z-axis : "+ bmaValueZ);
			
			float acc_magnitude3 = (float)Math.sqrt((lisValueX*lisValueX)+(lisValueY*lisValueY)+(lisValueZ*lisValueZ));
			float temp3X = (float)(Math.asin(lisValueX / acc_magnitude3)*(180/3.14159265358979323846264338327));
			float temp3Y = (float)(Math.asin(lisValueY / acc_magnitude3)*(180/3.14159265358979323846264338327));
			float temp3Z = (float)(Math.acos(lisValueZ / acc_magnitude3)*(180/3.14159265358979323846264338327));
			Texttest13.setText("X-axis : "+ lisValueX);
			Texttest14.setText("Y-axis : "+ lisValueY);
			Texttest15.setText("Z-axis : "+ lisValueZ);
			
			float acc_magnitude4 = (float)Math.sqrt((mpuValueX*mpuValueX)+(mpuValueY*mpuValueY)+(mpuValueZ*mpuValueZ));
			float temp4X = (float)(Math.asin(mpuValueX / acc_magnitude4)*(180/3.14159265358979323846264338327));
			float temp4Y = (float)(Math.asin(mpuValueY / acc_magnitude4)*(180/3.14159265358979323846264338327));
			float temp4Z = (float)(Math.acos(mpuValueZ / acc_magnitude4)*(180/3.14159265358979323846264338327));
			Texttest19.setText("X-axis : "+ mpuValueX);
			Texttest20.setText("Y-axis : "+ mpuValueY);
			Texttest21.setText("Z-axis : "+ mpuValueZ);
			
			if(x%10 == 0) {
				Texttest04.setText(String.format("%.1f", tempX)+"");
				Texttest05.setText(String.format("%.1f", tempY)+"");
				Texttest06.setText(String.format("%.1f", tempZ)+"");
				
				Texttest10.setText(String.format("%.1f", temp2X)+"");
				Texttest11.setText(String.format("%.1f", temp2Y)+"");
				Texttest12.setText(String.format("%.1f", temp2Z)+"");
				
				Texttest16.setText(String.format("%.1f", temp3X)+"");
				Texttest17.setText(String.format("%.1f", temp3Y)+"");
				Texttest18.setText(String.format("%.1f", temp3Z)+"");
				
				Texttest22.setText(String.format("%.1f", temp4X)+"");
				Texttest23.setText(String.format("%.1f", temp4Y)+"");
				Texttest24.setText(String.format("%.1f", temp4Z)+"");
			}
			
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
		
	}
}

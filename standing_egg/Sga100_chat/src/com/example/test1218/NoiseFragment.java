package com.example.test1218;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorManager;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoiseFragment extends Fragment implements OnClickListener {

	long lastTime;
   // private float speed;
    float lastX;
    float lastY;
    float lastZ;
    float x, y, z;
 
    final int SHAKE_THRESHOLD = 800;
    final int DATA_X = SensorManager.DATA_X;
    final int DATA_Y = SensorManager.DATA_Y;
    final int DATA_Z = SensorManager.DATA_Z;

	udpSocketTask NetworkThread02;
	DataMode data01;
	
	boolean  bThreadSwitch01 = false, cThreadSwitch01 = false;
	boolean exeFlag01 = false;
	
	// WIFI 확인
	String ipAddress;
	int serverIp;
 
    RealTimeNoiseChart rtc02;
    
    boolean exeFlag02 = false, bThreadSwitch02 = false;
    
    List values = new ArrayList();
    
    LinearLayout chartLayout, noiseLayout;
    Button conButton, noiseButton;
    

	TextView Texttest04, Texttest05, Texttest06;
	TextView Texttest10, Texttest11, Texttest12;
	TextView Texttest16, Texttest17, Texttest18;
	TextView Texttest22, Texttest23, Texttest24;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.noisefragment, container, false);
		getActivity().getActionBar().setIcon(R.drawable.actionbar_logo);
		getActivity().getActionBar().setTitle("Noise Chart Data");
		
		// 1. 사용할 RealTimeChart 객채 생성
		rtc02 = new RealTimeNoiseChart(getActivity());

		LinearLayout realTimeLayout = (LinearLayout)view.findViewById(R.id.RealTimelayout);

		//	2. 화면에 뷰를 등록
		realTimeLayout.addView(rtc02); // RealTimeChart 뷰를 화면에 추가
		//	3. 뷰의 크기를 설정
		rtc02.Initialize(2100, 1280);//(1800, 500); // RealTimeChart뷰 초기화(넓이, 높이)  **뷰를 등록(addView) 후 Initialize()를 호출해야 적용됨
		
	    bThreadSwitch02 = true;

		chartLayout = (LinearLayout)view.findViewById(R.id.chartLayout);
		noiseLayout = (LinearLayout)view.findViewById(R.id.noiseLayout);
		
	    conButton = (Button)view.findViewById(R.id.bt_ok);
	    noiseButton = (Button)view.findViewById(R.id.bt_ok2);
	    conButton.setOnClickListener(this);
	    noiseButton.setOnClickListener(this);
	    
	    //sga
		Texttest04 = (TextView)view.findViewById(R.id.Texttest04);
		Texttest05 = (TextView)view.findViewById(R.id.Texttest05);
		Texttest06 = (TextView)view.findViewById(R.id.Texttest06);
		
		//bma
		Texttest10 = (TextView)view.findViewById(R.id.Texttest10);
		Texttest11 = (TextView)view.findViewById(R.id.Texttest11);
		Texttest12 = (TextView)view.findViewById(R.id.Texttest12);
		
		//lis
		Texttest16 = (TextView)view.findViewById(R.id.Texttest16);
		Texttest17 = (TextView)view.findViewById(R.id.Texttest17);
		Texttest18 = (TextView)view.findViewById(R.id.Texttest18);
		
		//mpu
		Texttest22 = (TextView)view.findViewById(R.id.Texttest22);
		Texttest23 = (TextView)view.findViewById(R.id.Texttest23);
		Texttest24 = (TextView)view.findViewById(R.id.Texttest24);
	    
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
			}
			break;
			
		case R.id.bt_ok2:
			if(noiseLayout.getVisibility() == View.GONE) {
				noiseLayout.setVisibility(View.VISIBLE);
			}else {
				noiseLayout.setVisibility(View.GONE);
			}
			break;
			
		default:
			break;
		}
	}
	
	@Override
    public void onStart() {
        super.onStart();
    }
 
    @Override
    public void onStop() {
        super.onStop();
    }
    
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(bThreadSwitch01==true){
			
			bThreadSwitch01=false;
			cThreadSwitch01=false;
			
			NetworkThread02.cancel(true);
		}
		
		exeFlag01=false;
		
	}
 
	private class udpSocketTask  extends AsyncTask<Integer, Integer, Integer>{
		  private final String serverIP = MainActivity.serverIp; // ex: 192.168.1.31
		  private final int serverPort = MainActivity.serverPort; // ex: 48601
		  List<Integer> sgaValueX = new ArrayList<Integer>();
		  List<Integer> sgaValueY = new ArrayList<Integer>();
		  List<Integer> sgaValueZ = new ArrayList<Integer>();
		  List<Integer> bmaValueX = new ArrayList<Integer>();
		  List<Integer> bmaValueY = new ArrayList<Integer>();
		  List<Integer> bmaValueZ = new ArrayList<Integer>();
		  List<Integer> lisValueX = new ArrayList<Integer>();
		  List<Integer> lisValueY = new ArrayList<Integer>();
		  List<Integer> lisValueZ = new ArrayList<Integer>();
		  List<Integer> mpuValueX = new ArrayList<Integer>();
		  List<Integer> mpuValueY = new ArrayList<Integer>();
		  List<Integer> mpuValueZ = new ArrayList<Integer>();

		  float sgaNoiseX, sgaNoiseY, sgaNoiseZ, bmaNoiseX, bmaNoiseY, bmaNoiseZ, lisNoiseX, lisNoiseY, lisNoiseZ, mpuNoiseX, mpuNoiseY, mpuNoiseZ = 0; 
		
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
			while(bThreadSwitch01){
				if(sgaValueX.size() == 150) {// 4초에 4800byte data 받아온다. 75
					float sgaStdevX = (float) standardDeviation(sgaValueX, 1);
					float sgaStdevY = (float) standardDeviation(sgaValueY, 1);
					float sgaStdevZ = (float) standardDeviation(sgaValueZ, 1);
					float bmaStdevX = (float) standardDeviation(bmaValueX, 1);
					float bmaStdevY = (float) standardDeviation(bmaValueY, 1);
					float bmaStdevZ = (float) standardDeviation(bmaValueZ, 1);
					float lisStdevX = (float) standardDeviation(lisValueX, 1);
					float lisStdevY = (float) standardDeviation(lisValueY, 1);
					float lisStdevZ = (float) standardDeviation(lisValueZ, 1);
					float mpuStdevX = (float) standardDeviation(mpuValueX, 1);
					float mpuStdevY = (float) standardDeviation(mpuValueY, 1);
					float mpuStdevZ = (float) standardDeviation(mpuValueZ, 1);

					sgaNoiseX = (float) (((sgaStdevX/16384)*1000)/Math.sqrt(21.6*1.57))*1000;  
					sgaNoiseY = (float) (((sgaStdevY/16384)*1000)/Math.sqrt(21.6*1.57))*1000;
					sgaNoiseZ = (float) (((sgaStdevZ/16384)*1000)/Math.sqrt(21.6*1.57))*1000;
					bmaNoiseX = (float) (((bmaStdevX/16384)*1000)/Math.sqrt(31.25*1.1*0.7))*1000;
					bmaNoiseY = (float) (((bmaStdevY/16384)*1000)/Math.sqrt(31.25*1.1*0.7))*1000;
					bmaNoiseZ = (float) (((bmaStdevZ/16384)*1000)/Math.sqrt(31.25*1.1*0.7))*1000;
//					bmaNoiseX = (float) (((bmaStdevX/16384)*1000)/Math.sqrt(31.25*1.57))*1000;
//					bmaNoiseY = (float) (((bmaStdevY/16384)*1000)/Math.sqrt(31.25*1.57))*1000;
//					bmaNoiseZ = (float) (((bmaStdevZ/16384)*1000)/Math.sqrt(31.25*1.57))*1000;
					lisNoiseX = (float) (((lisStdevX/16384)*1000)/Math.sqrt(37*1.57))*1000;
					lisNoiseY = (float) (((lisStdevY/16384)*1000)/Math.sqrt(37*1.57))*1000;
					lisNoiseZ = (float) (((lisStdevZ/16384)*1000)/Math.sqrt(37*1.57))*1000;
//					mpuNoiseX = (float) (((mpuStdevX/16384)*1000)/Math.sqrt(20*1.57))*1000;
//					mpuNoiseY = (float) (((mpuStdevY/16384)*1000)/Math.sqrt(20*1.57))*1000;
//					mpuNoiseZ = (float) (((mpuStdevZ/16384)*1000)/Math.sqrt(20*1.57))*1000;
					mpuNoiseX = (float) (((mpuStdevX/16384)*1000)/Math.sqrt(20*1.57*0.7))*1000;
					mpuNoiseY = (float) (((mpuStdevY/16384)*1000)/Math.sqrt(20*1.57*0.7))*1000;
					mpuNoiseZ = (float) (((mpuStdevZ/16384)*1000)/Math.sqrt(20*1.57*0.7))*1000;
					
					if(data01.ValueFlag01){
						if(!RealTimeChart.bDrawing){
							rtc02.addData(0, (int)sgaNoiseX, (int)bmaNoiseX, (int)lisNoiseX, (int)mpuNoiseX);
							rtc02.addData(1, (int)sgaNoiseY, (int)bmaNoiseY, (int)lisNoiseY, (int)mpuNoiseY);
							rtc02.addData(2, (int)sgaNoiseZ, (int)bmaNoiseZ, (int)lisNoiseZ, (int)mpuNoiseZ);
						}
						
						publishProgress();
						data01.ValueFlag01=false;
					}
					
					if(bThreadSwitch01==true){
						
						bThreadSwitch01=false;
						cThreadSwitch01=false;
						
						NetworkThread02.cancel(true);
					}
					exeFlag01=false;
				}
				
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
				if("PKST".equals(value.substring(0, 4))) {
					// SGA100 X, Y, Z setting
					sgaValue01 = Integer.parseInt(value.substring(4, 6), 16);
					sgaValue02 = Integer.parseInt(value.substring(6, 8), 16);
					sgaValue03 = Integer.parseInt(value.substring(8, 10), 16);
					sgaValue04 = Integer.parseInt(value.substring(10, 12), 16);
					sgaValue05 = Integer.parseInt(value.substring(12, 14), 16);
					sgaValue06 = Integer.parseInt(value.substring(14, 16), 16);
					data01.m_pkst(sgaValue01);
					data01.m_pkst(sgaValue02);
					data01.m_pkst(sgaValue03);
					data01.m_pkst(sgaValue04);
					data01.m_pkst(sgaValue05);
					data01.m_pkst(sgaValue06);
					sgaValueX.add((int)data01.test01); 
					sgaValueY.add((int)data01.test02);
					sgaValueZ.add((int)data01.test03);

					// BMA250E X, Y, Z setting
					bmaValue01 = Integer.parseInt(value.substring(16, 18), 16);
					bmaValue02 = Integer.parseInt(value.substring(18, 20), 16);
					bmaValue03 = Integer.parseInt(value.substring(20, 22), 16);
					bmaValue04 = Integer.parseInt(value.substring(22, 24), 16);
					bmaValue05 = Integer.parseInt(value.substring(24, 26), 16);
					bmaValue06 = Integer.parseInt(value.substring(26, 28), 16);
					data01.m_pkst(bmaValue01);
					data01.m_pkst(bmaValue02);
					data01.m_pkst(bmaValue03);
					data01.m_pkst(bmaValue04);
					data01.m_pkst(bmaValue05);
					data01.m_pkst(bmaValue06);
					bmaValueX.add((int)data01.test01); 
					bmaValueY.add((int)data01.test02);
					bmaValueZ.add((int)data01.test03);

					// LIS331DLH X, Y, Z setting
					lisValue01 = Integer.parseInt(value.substring(28, 30), 16);
					lisValue02 = Integer.parseInt(value.substring(30, 32), 16);
					lisValue03 = Integer.parseInt(value.substring(32, 34), 16);
					lisValue04 = Integer.parseInt(value.substring(34, 36), 16);
					lisValue05 = Integer.parseInt(value.substring(36, 38), 16);
					lisValue06 = Integer.parseInt(value.substring(38, 40), 16);
					data01.m_pkst(lisValue01);
					data01.m_pkst(lisValue02);
					data01.m_pkst(lisValue03);
					data01.m_pkst(lisValue04);
					data01.m_pkst(lisValue05);
					data01.m_pkst(lisValue06);
					lisValueX.add((int)data01.test01); 
					lisValueY.add((int)data01.test02);
					lisValueZ.add((int)data01.test03);

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
					mpuValueX.add((int)data01.test01); 
					mpuValueY.add((int)data01.test02);
					mpuValueZ.add((int)data01.test03);
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			
			//색상 초기화
			Texttest04.setTextColor(Color.WHITE);
			Texttest05.setTextColor(Color.WHITE);
			Texttest06.setTextColor(Color.WHITE);
			Texttest10.setTextColor(Color.WHITE);
			Texttest11.setTextColor(Color.WHITE);
			Texttest12.setTextColor(Color.WHITE);
			Texttest16.setTextColor(Color.WHITE);
			Texttest17.setTextColor(Color.WHITE);
			Texttest18.setTextColor(Color.WHITE);
			Texttest22.setTextColor(Color.WHITE);
			Texttest23.setTextColor(Color.WHITE);
			Texttest24.setTextColor(Color.WHITE);
			
			Texttest04.setText(String.format("%.1f", sgaNoiseX)+"");
			Texttest05.setText(String.format("%.1f", sgaNoiseY)+"");
			Texttest06.setText(String.format("%.1f", sgaNoiseZ)+"");

			Texttest10.setText(String.format("%.1f", bmaNoiseX)+"");
			Texttest11.setText(String.format("%.1f", bmaNoiseY)+"");
			Texttest12.setText(String.format("%.1f", bmaNoiseZ)+"");

			Texttest16.setText(String.format("%.1f", lisNoiseX)+"");
			Texttest17.setText(String.format("%.1f", lisNoiseY)+"");
			Texttest18.setText(String.format("%.1f", lisNoiseZ)+"");

			Texttest22.setText(String.format("%.1f", mpuNoiseX)+"");
			Texttest23.setText(String.format("%.1f", mpuNoiseY)+"");
			Texttest24.setText(String.format("%.1f", mpuNoiseZ)+"");
			
			//x 최소값 red 표시
			if(sgaNoiseX < bmaNoiseX && sgaNoiseX < lisNoiseX && sgaNoiseX < mpuNoiseX) {
				Texttest04.setTextColor(Color.parseColor("#C3143B"));
			}else if (bmaNoiseX < lisNoiseX && bmaNoiseX < mpuNoiseX) {
				Texttest10.setTextColor(Color.parseColor("#C3143B"));
			}else if (lisNoiseX < mpuNoiseX) {
				Texttest16.setTextColor(Color.parseColor("#C3143B"));
			}else {
				Texttest22.setTextColor(Color.parseColor("#C3143B"));
			}
			
			//y 최소값 red 표시
			if(sgaNoiseY < bmaNoiseY && sgaNoiseY < lisNoiseY && sgaNoiseY < mpuNoiseY) {
				Texttest05.setTextColor(Color.parseColor("#C3143B"));
			}else if (bmaNoiseY < lisNoiseY && bmaNoiseY < mpuNoiseY) {
				Texttest11.setTextColor(Color.parseColor("#C3143B"));
			}else if (lisNoiseY < mpuNoiseY) {
				Texttest17.setTextColor(Color.parseColor("#C3143B"));
			}else {
				Texttest23.setTextColor(Color.parseColor("#C3143B"));
			}
			
			//z 최소값 red 표시
			if(sgaNoiseZ < bmaNoiseZ && sgaNoiseZ < lisNoiseZ && sgaNoiseZ < mpuNoiseZ) {
				Texttest06.setTextColor(Color.parseColor("#C3143B"));
			}else if (bmaNoiseZ < lisNoiseZ && bmaNoiseZ < mpuNoiseZ) {
				Texttest12.setTextColor(Color.parseColor("#C3143B"));
			}else if (lisNoiseZ < mpuNoiseZ) {
				Texttest18.setTextColor(Color.parseColor("#C3143B"));
			}else {
				Texttest24.setTextColor(Color.parseColor("#C3143B"));
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
	
	  public static double mean(List<Integer> array) {  // 산술 평균 구하기
	    double sum = 0.0;
	    for (int i = 0; i < array.size(); i++)
	      sum += array.get(i);
	    return sum / array.size();
	  }

	  public static double standardDeviation(List<Integer> array, int option) {
	    if (array.size() < 2) return Double.NaN;
	    double sum = 0.0;
	    double sd = 0.0;
	    double diff;
	    double meanValue = mean(array);
	    for (int i = 0; i < array.size(); i++) {
	      diff = array.get(i) - meanValue;
	      sum += diff * diff;
	    }
	    sd = Math.sqrt(sum / (array.size() - option));
	    return sd;
	  }
}

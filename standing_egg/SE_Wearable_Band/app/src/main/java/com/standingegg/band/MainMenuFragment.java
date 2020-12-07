/**
 * 
 */
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.UninitializedMessageException;
import com.standingegg.band.util.StnEggPkt;
import com.standingegg.band.util.ULog;

import java.util.Date;

/**
 * 로그인 정보 로딩
 * 
 * @author cczzee
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {

	Button mActivityButton, mMotionButton, mGaitButton, mHeartButton, mSensorButton, mUserButton;
	TextView mLogTextView;
	int protocolBufferLen = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_menu, container, false);

		mActivityButton = (Button) v.findViewById(R.id.activity_button);
		mActivityButton.setOnClickListener(this);
		mMotionButton = (Button) v.findViewById(R.id.motion_button);
		mMotionButton.setOnClickListener(this);
		mGaitButton = (Button) v.findViewById(R.id.gait_button);
		mGaitButton.setOnClickListener(this);
		mHeartButton = (Button) v.findViewById(R.id.heart_button);
		mHeartButton.setOnClickListener(this);
		mSensorButton = (Button) v.findViewById(R.id.sensor_button);
		mSensorButton.setOnClickListener(this);
		mUserButton = (Button) v.findViewById(R.id.user_button);
		mUserButton.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.activity_button:
				MainActivity.flagMainMenu = false;
				getActivity().findViewById(R.id.frame_loading).setVisibility(View.GONE);
				getActivity().findViewById(R.id.frame_activity).setVisibility(View.VISIBLE);
				getActivity().findViewById(R.id.frame_motion).setVisibility(View.GONE);

				((MainActivity)getActivity()).setActivityFragment();

				TextView title = (TextView)getActivity().getActionBar().getCustomView().findViewById(R.id.action_bar_title);
				title.setText(R.string.activity_title);


				break;
			case R.id.motion_button:
				MainActivity.flagMainMenu = false;
				getActivity().findViewById(R.id.frame_loading).setVisibility(View.GONE);
				getActivity().findViewById(R.id.frame_activity).setVisibility(View.GONE);
				getActivity().findViewById(R.id.frame_motion).setVisibility(View.VISIBLE);

				((MainActivity)getActivity()).setMotionFragment();

				TextView motionTitle = (TextView)getActivity().getActionBar().getCustomView().findViewById(R.id.action_bar_title);
				motionTitle.setText(R.string.motion_title);


				//command
				StnEggPkt.StnEggPacket.Builder sendBuilder2 = StnEggPkt.StnEggPacket.newBuilder();
				sendBuilder2.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_BAND_MODE_REQUEST);
				sendBuilder2.setUserUint01(4);

				try {
					byte[] encoded = new byte[sendBuilder2.build().toByteArray().length + 5];
					byte[] builderTobyte = sendBuilder2.build().toByteArray();

					encoded[0] = 'S';
					encoded[1] = 'E';
					int i;
					for (i = 0; i < builderTobyte.length; i++) {
						encoded[i + 2] = builderTobyte[i];
					}
					encoded[i + 2] = 'E';
					encoded[i + 3] = '\r';
					encoded[i + 4] = '\n';
					MainActivity.mService.sendMessageToRemote(encoded);

				} catch (UninitializedMessageException e) {
					e.printStackTrace();
				}


				break;
			case R.id.gait_button:
//				MainActivity.gaitFragment();





				if(true) {
					try {
						Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.nulana.nchart3d.example.AutoScroll");
						startActivity(intent);
						((MainActivity) getActivity()).appFinish();
					}catch (NullPointerException e) {
						Toast.makeText(getActivity(), "gait tracking installation demand", Toast.LENGTH_SHORT).show();
					}

					return;
				}









				StnEggPkt.StnEggPacket.Builder builder2 = StnEggPkt.StnEggPacket.newBuilder();
				ULog.d("builder", "111 getYear : " + builder2.getDate().getYear() + "//getMonth : " + builder2.getDate().getMonth() + "//getDay : " + builder2.getDate().getDay());

				//command
				builder2.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_DATE_TIME_CONTENT);

				// date
				Date tdays = new Date();
				builder2.getDateBuilder().setYear(tdays.getYear()+1900);
				builder2.getDateBuilder().setMonth(tdays.getMonth()+1);
				builder2.getDateBuilder().setDay(tdays.getDate());
				builder2.getDateBuilder().setHour(tdays.getHours());
				builder2.getDateBuilder().setMinutes(tdays.getMinutes());
				builder2.getDateBuilder().setSeconds(tdays.getSeconds());
				if(tdays.getDay() == 0) {
					builder2.getDateBuilder().setWeekDay(7);
				}else {
					builder2.getDateBuilder().setWeekDay(tdays.getDay());
				}
				/*Calendar cal = Calendar.getInstance();
				cal.setTime(tdays);
				if(cal.get(Calendar.DAY_OF_WEEK) == 1){
					builder2.getDateBuilder().setWeekDay(7);
				}else {
					builder2.getDateBuilder().setWeekDay(cal.get(Calendar.DAY_OF_WEEK)-1);
				}*/
				ULog.d("builder", "222 getYear : " + builder2.getDate().getYear() + "//getMonth : " + builder2.getDate().getMonth() + "//getDay : " + builder2.getDate().getDay()
						+ "//getHour : " + builder2.getDate().getHour() + "//getMinutes : " + builder2.getDate().getMinutes() + "//getSeconds : " + builder2.getDate().getSeconds() + "//getWeekDay : " + builder2.getDate().getWeekDay());


				try {
					byte[] encoded = new byte[builder2.build().toByteArray().length+5];
					byte[] builderTobyte2 = builder2.build().toByteArray();
					//byte[] builderTobyte2 = builder2.getDate().toByteArray();

					encoded[0] = 'S';
					encoded[1] = 'E';
					int i;
					for( i = 0; i < builderTobyte2.length; i++) {
						encoded[i+2] = builderTobyte2[i];
					}
					encoded[i+2] = 'E';
					encoded[i+3] = '\r';
					encoded[i+4] = '\n';
					MainActivity.mService.sendMessageToRemote(encoded);

					/*for(int j = 0; j < encoded.length; j++) {
						if (MainActivity.mService != null) {
							byte[] newByte = new byte[1];
							newByte[0]= encoded[j];
							MainActivity.mService.sendMessageToRemote(newByte);
						}
					}*/
				}catch (UninitializedMessageException e){
					e.printStackTrace();
				}

				break;
			case R.id.heart_button:
//				MainActivity.heartFragment();
				MainActivity.flagMainMenu = false;

				((MainActivity)getActivity()).heartRateFragment();

				TextView title_heart = (TextView)getActivity().getActionBar().getCustomView().findViewById(R.id.action_bar_title);
				title_heart.setText(R.string.heart_rate_title);
				break;
			case R.id.sensor_button:


				if(false) {

					//command
					StnEggPkt.StnEggPacket.Builder sensorBuilder = StnEggPkt.StnEggPacket.newBuilder();
					sensorBuilder.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_BAND_MODE_REQUEST);
					sensorBuilder.setUserUint01(5);

					try {
						byte[] encoded = new byte[sensorBuilder.build().toByteArray().length + 5];
						byte[] builderTobyte = sensorBuilder.build().toByteArray();

						encoded[0] = 'S';
						encoded[1] = 'E';
						int i;
						for (i = 0; i < builderTobyte.length; i++) {
							encoded[i + 2] = builderTobyte[i];
						}
						encoded[i + 2] = 'E';
						encoded[i + 3] = '\r';
						encoded[i + 4] = '\n';
						MainActivity.mService.sendMessageToRemote(encoded);

					} catch (UninitializedMessageException e) {
						e.printStackTrace();
					}



					MainActivity.flagMainMenu = false;

					((MainActivity) getActivity()).totalDataFragment();

					TextView title_sensor = (TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.action_bar_title);
					title_sensor.setText(R.string.sensor_data_title);
					return;
				}



				if(false) {
					MainActivity.flagMainMenu = false;

					((MainActivity) getActivity()).sensorDataFragment();

					TextView title_sensor = (TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.action_bar_title);
					title_sensor.setText(R.string.sensor_data_title);
					return;
				}




//				byte newValue[] = hexToByteArray("66");
//				byte newValue[] = hexToByteArray("ABC1234897");
				/*byte newValue[] = hexToByteArray("AB");
				byte[] originalBytes = new byte[newValue.length];
				for (int j = 0; j < newValue.length; j++) {
					if(j > originalBytes.length - 1) {
						break;
					}

					originalBytes[j] = newValue[j];
				}

				Log.d("MainMenu", "SensorButton : " + originalBytes.length );
				if (MainActivity.mService != null)
					MainActivity.mService.sendMessageToRemote(originalBytes);*/




				/*StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
				builder.setSerpCommand(protocolBufferLen);
				protocolBufferLen++;

				try {
					byte[] encoded = new byte[builder.build().toByteArray().length+5];
					byte[] builderTobyte = builder.build().toByteArray();

					encoded[0] = 'S';
					encoded[1] = 'E';
					int i;
					for( i = 0; i < builderTobyte.length; i++) {
						encoded[i+2] = builderTobyte[i];
					}
					int lastBuffer = builder.build().toByteArray().length+5;
					encoded[i+2] = 'E';
					encoded[i+3] = '\r';
					encoded[i+4] = '\n';
					MainActivity.mService.sendMessageToRemote(encoded);

					*//*for(int j = 0; j < encoded.length; j++) {
						if (MainActivity.mService != null) {
							byte[] newByte = new byte[1];
							newByte[0]= encoded[j];
							MainActivity.mService.sendMessageToRemote(newByte);
						}
					}*//*
				}catch (UninitializedMessageException e){
					e.printStackTrace();
				}*/


				if(false) {
					byte newValue[] = hexToByteArray("AAA");

					// this ensures that value sent back is the same number of bytes as value that was read
					ULog.d("writeValueToCharacteristic", "newValue : " + newValue.toString());
					byte[] originalBytes = new byte[newValue.length];
					for (int i = 0; i < newValue.length; i++) {
						if (i > originalBytes.length - 1) {
							break;
						}

						originalBytes[i] = newValue[i];
					}
//                mBluetoothCharact.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);//20160824
					MainActivity.mService.sendMessageToRemote(originalBytes);
				}


//				MainActivity.sensorFragment();
				break;
			case R.id.user_button:
				MainActivity.flagMainMenu = false;
				((MainActivity)getActivity()).userOptionFragment();

				TextView title_user = (TextView)getActivity().getActionBar().getCustomView().findViewById(R.id.action_bar_title);
				title_user.setText(R.string.user_title);
//				Intent setting = new Intent(getActivity(), UserProfileActivity.class);
//				startActivity(setting);
				//((MainActivity)getActivity()).logout();
				break;
		}
	}


	//20160819 start
	// Converts string given in hexadecimal system to byte array
	private byte[] hexToByteArray(String hex) {
		byte byteArr[] = new byte[hex.length() / 2];
		for (int i = 0; i < byteArr.length; i++) {
			int temp = Integer.parseInt(hex.substring(i * 2, (i * 2) + 2), 16);
			byteArr[i] = (byte) (temp & 0xFF);
		}
		return byteArr;
	}
	//20160819 end

}
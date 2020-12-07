/**
 * @author taehoon Moon
 *
 */
package com.standingegg.band.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.protobuf.UninitializedMessageException;
import com.standingegg.band.MainActivity;
import com.standingegg.band.R;
import com.standingegg.band.setting.AlarmAcitivity;
import com.standingegg.band.setting.UserProfileActivity;
import com.standingegg.band.setting.BatteryActivity;
import com.standingegg.band.setting.SettingActivity;
import com.standingegg.band.util.StnEggPkt;

/**
 * User 정보
 * 
 * @author cczzee
 */
public class UserOptionFragment extends Fragment implements View.OnClickListener {

	Button mProfileButton, mAlarmButton, mBatteryButton, mVibButton, mNotiButton, mLogoutButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.user_option, container, false);

		mProfileButton = (Button) v.findViewById(R.id.profile_button);
		mProfileButton.setOnClickListener(this);
		mAlarmButton = (Button) v.findViewById(R.id.alarm_button);
		mAlarmButton.setOnClickListener(this);
		mBatteryButton = (Button) v.findViewById(R.id.battery_button);
		mBatteryButton.setOnClickListener(this);
		mVibButton = (Button) v.findViewById(R.id.vib_button);
		mVibButton.setOnClickListener(this);
		mNotiButton = (Button) v.findViewById(R.id.noti_button);
		mNotiButton.setOnClickListener(this);
		mLogoutButton = (Button) v.findViewById(R.id.logout_button);
		mLogoutButton.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.profile_button:
				Intent profile = new Intent(getActivity(), UserProfileActivity.class);
				startActivity(profile);
				break;
			case R.id.alarm_button:
				Intent alarm = new Intent(getActivity(), AlarmAcitivity.class);
				startActivity(alarm);
				break;
			case R.id.battery_button:
				Intent battery = new Intent(getActivity(), BatteryActivity.class);
				startActivity(battery);
				break;
			case R.id.vib_button:
				StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
				builder.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_VIBRATION_REQUEST);
				builder.setLedColor(0);

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
					MainActivity.mService.sendMessageToRemote(encoded);
				}catch (UninitializedMessageException e){
					e.printStackTrace();
				}
				break;
			case R.id.noti_button:
				Intent noti = new Intent(getActivity(), SettingActivity.class);
				startActivity(noti);
				break;
			case R.id.logout_button:
				((MainActivity)getActivity()).logout();
				break;
		}
	}

}
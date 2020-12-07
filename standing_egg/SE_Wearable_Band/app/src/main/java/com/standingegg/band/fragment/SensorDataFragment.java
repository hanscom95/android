/**
 * 
 */
/**
 * @author taehoon moon
 *
 */
package com.standingegg.band.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.protobuf.UninitializedMessageException;
import com.standingegg.band.MainActivity;
import com.standingegg.band.R;
import com.standingegg.band.util.StnEggPkt;
import com.standingegg.band.util.TodayPreferences;

/**
 * all data Fragment
 * 
 * @author taehoon moon
 */
public class SensorDataFragment extends Fragment {

	TextView mWalkingCnt, mWalkingKm, mWalkingCal, mRunningCnt, mRunningKm, mRunningCal, mShoulderCnt, mShoulderCal, mHammerCnt, mHammerCal, mHeartRate;
	LinearLayout mHeartRateLinearLayout;
	int mTotalWalkCnt, mTotalWalkKm, mTotalWalkCal, mTotalRunCnt, mTotalRunKm, mTotalRunCal, mTotalShoulderCnt, mTotalShoulderCal, mTotalHammerCnt, mTotalHammerCal;
	private TodayPreferences mTodayPreference = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.total_information, container, false);
		mTodayPreference = TodayPreferences.getInstance(this.getActivity());

		mWalkingCnt = (TextView) v.findViewById(R.id.walking_cnt);
		mWalkingKm = (TextView) v.findViewById(R.id.walking_km);
		mWalkingCal = (TextView) v.findViewById(R.id.walking_cal);
		mRunningCnt = (TextView) v.findViewById(R.id.running_cnt);
		mRunningKm = (TextView) v.findViewById(R.id.running_km);
		mRunningCal = (TextView) v.findViewById(R.id.running_cal);
		mShoulderCnt = (TextView) v.findViewById(R.id.shoulder_cnt);
		mShoulderCal = (TextView) v.findViewById(R.id.shoulder_cal);
		mHammerCnt = (TextView) v.findViewById(R.id.hammer_cnt);
		mHammerCal = (TextView) v.findViewById(R.id.hammer_cal);
		mHeartRate = (TextView) v.findViewById(R.id.heart_rate_bpm);
		mHeartRateLinearLayout = (LinearLayout) v.findViewById(R.id.heart_rate_linear);
		mHeartRateLinearLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setHeartRate();
			}
		});

		setHeartRate();
		initData();
		return v;
	}

	private  void initData() {
			mWalkingCnt.setText(mTodayPreference.getActivityAllCount() +"");
			mWalkingKm.setText(String.format("%.2f", (mTodayPreference.getWalkDistance()/1000.0f)));
			mWalkingCal.setText(String.format("%.2f", (mTodayPreference.getWalkCal()/1000.0f)));
			mRunningCnt.setText(mTodayPreference.getActivityRunAllCount() +"");
			mRunningKm.setText(String.format("%.2f", (mTodayPreference.getRunDistance()/1000.0f)));
			mRunningCal.setText(String.format("%.2f", (mTodayPreference.getRunCal()/1000.0f)));
			mHammerCnt.setText(mTodayPreference.getMotionHammerAllCount()+"");
			mHammerCal.setText(String.format("%.2f", (mTotalHammerCal/1000.0f)));
			mShoulderCnt.setText(mTodayPreference.getMotionShoulderAllCount() +"");
			mShoulderCal.setText(String.format("%.2f", (mTotalShoulderCal/1000.0f)));
	}

	private void setHeartRate(){
		StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
		builder.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_HEART_RATE_REQUEST);

		try {
			byte[] encoded = new byte[builder.build().toByteArray().length+5];
			byte[] builderTobyte = builder.build().toByteArray();

			encoded[0] = 'S';
			encoded[1] = 'E';
			int i;
			for( i = 0; i < builderTobyte.length; i++) {
				encoded[i+2] = builderTobyte[i];
			}
			encoded[i+2] = 'E';
			encoded[i+3] = '\r';
			encoded[i+4] = '\n';
			MainActivity.mService.sendMessageToRemote(encoded);
		}catch (UninitializedMessageException e){
			e.printStackTrace();
		}
	}

	public void setData(int[] data) {
		if(false){
			if(data[0] == 0) {
				mHeartRate.setText(data[4] + "");
			}else if(data[0] == 2) {
				mTotalWalkCnt = data[1];
				mTotalWalkKm = data[2];
				mTotalWalkCal = data[3];
				mWalkingCnt.setText(mTotalWalkCnt +"");
				mWalkingKm.setText(String.format("%.2f", (mTotalWalkKm/1000.0f)));
				mWalkingCal.setText(String.format("%.2f", (mTotalWalkCal/1000.0f)));
			}else if(data[0] == 3) {
				mTotalRunCnt = data[1];
				mTotalRunKm = data[2];
				mTotalRunCal = data[3];
				mRunningCnt.setText(mTotalRunCnt +"");
				mRunningKm.setText(String.format("%.2f", (mTotalRunKm/1000.0f)));
				mRunningCal.setText(String.format("%.2f", (mTotalRunCal/1000.0f)));
			}else if(data[0] == 9) {
				mTotalHammerCnt = data[1];
				mTotalHammerCal = data[3];
				mHammerCnt.setText(mTotalHammerCnt +"");
				mHammerCal.setText(String.format("%.2f", (mTotalHammerCal/1000.0f)));
			}else if(data[0] == 10) {
				mTotalShoulderCnt = data[1];
				mTotalShoulderCal = data[3];
				mShoulderCnt.setText(mTotalShoulderCnt +"");
				mShoulderCal.setText(String.format("%.2f", (mTotalShoulderCal/1000.0f)));
			}
			return;
		}




		if(data[0] == 2) {
			mTotalWalkCnt = mTotalWalkCnt+data[1];
			mTotalWalkKm = mTotalWalkKm+data[2];
			mTotalWalkCal = mTotalWalkCal+data[3];
			mWalkingCnt.setText(mTotalWalkCnt +"");
			mWalkingKm.setText(String.format("%.2f", (mTotalWalkKm/1000.0f)) +"");
			mWalkingCal.setText(String.format("%.2f", (mTotalWalkCal/1000.0f)) +"");
		}else if(data[0] == 3) {
			mTotalRunCnt = mTotalRunCnt+data[1];
			mTotalRunKm = mTotalRunKm+data[2];
			mTotalRunCal = mTotalRunCal+data[3];
			mRunningCnt.setText(mTotalRunCnt +"");
			mRunningKm.setText(String.format("%.2f", (mTotalRunKm/1000.0f)) +"");
			mRunningCal.setText(String.format("%.2f", (mTotalRunCal/1000.0f)) +"");
		}else if(data[0] == 9) {
			mTotalHammerCnt = mTotalHammerCnt+data[1];
			mTotalHammerCal = mTotalHammerCal+data[3];
			mHammerCnt.setText(mTotalHammerCnt +"");
			mHammerCal.setText(String.format("%.2f", (mTotalHammerCal/1000.0f)) +"");
		}else if(data[0] == 10) {
			mTotalShoulderCnt = mTotalShoulderCnt+data[1];
			mTotalShoulderCal = mTotalShoulderCal+data[3];
			mShoulderCnt.setText(mTotalShoulderCnt +"");
			mShoulderCal.setText(String.format("%.2f", (mTotalShoulderCal/1000.0f)) +"");
		}

		if(data[4] != 0) {
			mHeartRate.setText(data[4] + "");
		}
	}

}
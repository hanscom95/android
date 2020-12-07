
package com.standingegg.band.motion;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.standingegg.band.IFragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.contents.CircularProgressBar;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;

public class HammerFragment extends Fragment implements OnClickListener {
	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;
	private DBManager dbManager;

	// packet show
	// TextView mTextChat;
	TextView mHammerCntTv, mMotionName;
	TextView mSittingCnt, mJumpingCnt, mPushUpCnt, mSitUpCnt, mButterflyCnt, mBicepsCurlCnt, mShoulderPressCnt, mUprightRowCnt, mToeRaiseCnt, mLateralCnt;
	LinearLayout mMotionImage;

	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;

	private int todayHammerCnt, todayDis,todayCal ;

	float activityGoal = 1000.0f;

	public HammerFragment() {};

	public HammerFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.motion_hammer, container, false);

		setContents(v);
		mPreference = Preferences.getInstance(mContext);
		activityGoal =  mPreference.getUserActivityGoal()< 1000? 1000.0f : (float)mPreference.getUserActivityGoal();
		mTodayPreference = TodayPreferences.getInstance(mContext);
		dbManager = new DBManager(mContext, "seband.db", null, 1);
		
		setData();
		return v;
	}

	public void setData() {
		todayHammerCnt = mTodayPreference.getMotionHammerAllCount();

//		mHammerCntTv.setText(Integer.toString(todayHammerCnt));

		setCount(Integer.toString(todayHammerCnt), 0, 0, 0);
	}

	private void setContents(View v) {
		mHammerCntTv = (TextView) v.findViewById(R.id.hammer_cnt_tv);
		mMotionName = (TextView) v.findViewById(R.id.motion_name);

		mSittingCnt = (TextView) v.findViewById(R.id.sittingCntText);
		mJumpingCnt = (TextView) v.findViewById(R.id.jumpingCntText);
		mPushUpCnt = (TextView) v.findViewById(R.id.pushupCntText);
		mSitUpCnt = (TextView) v.findViewById(R.id.situpCntText);
		mButterflyCnt = (TextView) v.findViewById(R.id.butterflyCntText);
		mBicepsCurlCnt = (TextView) v.findViewById(R.id.bicepscurlCntText);
		mShoulderPressCnt = (TextView) v.findViewById(R.id.shoulderpressCntText);
		mUprightRowCnt = (TextView) v.findViewById(R.id.uprightrowCntText);
		mToeRaiseCnt = (TextView) v.findViewById(R.id.toeraiseCntText);
		mLateralCnt = (TextView) v.findViewById(R.id.lateralCntText);

		mMotionImage = (LinearLayout) v.findViewById(R.id.motion_image);

	}

	public void setProgress(float activityCnt) {
	}
//	public void setProgressOver(float activityCnt) {
//		animate(mActivityProgressOver, activityCnt);
//	}

	public void animate(final CircularProgressBar progressBar, float activityCnt) {
	}

	// Sends user message to remote
	public void sendMessage(String message) {
		if (message == null || message.length() < 1)
			return;
		// send to remote
		if (mFragmentListener != null)
			mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE,message);
		else
			return;
	}

	// Sends user message to remote
	public void showsendMessage(String message) {
		if (message == null || message.length() < 1)
			return;

//		if (mTextChat != null) {
//			mTextChat.append("\nSend: ");
//			mTextChat.append(message);
//			int scrollamout = mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) - mTextChat.getHeight();
//			if (scrollamout > mTextChat.getHeight())
//				mTextChat.scrollTo(0, scrollamout);
//		}
	}

	private static final int NEW_LINE_INTERVAL = 1000;
	private long mLastReceivedTime = 0L;

	public void showMessage(String message) {
		if (message != null && message.length() > 0) {

			long current = System.currentTimeMillis();
//
//			if (current - mLastReceivedTime > NEW_LINE_INTERVAL) {
//				mTextChat.append("\nRcv: ");
//			}
//			mTextChat.append(message);
//			int scrollamout = mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) - mTextChat.getHeight();
//			if (scrollamout > mTextChat.getHeight())
//				mTextChat.scrollTo(0, scrollamout);

			mLastReceivedTime = current;
		}
	}

	/**
	 * set 활동 main UI walk, progress cnt
	 * 
	 * @param cnt
	 * @param mActivityAllM
	 * @param mActivityAllCal
	 * @param motionName
	 */
	public void setCount(String cnt, int mActivityAllM, int mActivityAllCal, int motionName) {
		if (cnt == null || cnt.length() < 1)
			return;

//		Cursor cursor = dbManager.dailyHammerTotalCntDate(mTodayPreference.getDataDate());
//		int totalCnt = 0, totalCar = 0;
//		while (cursor.moveToNext()) {
//			totalCnt = totalCnt + cursor.getInt(0);
//			totalCar = totalCar + cursor.getInt(1);
//		}

		mHammerCntTv.setText(""+cnt);


		if(motionName == Constants.MOTION_STANDING) {
			mHammerCntTv.setText("");
			mMotionName.setText("STANDING");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_standing));
		}else if(motionName == Constants.MOTION_SITTING) {
			mSittingCnt.setText(cnt);
			mMotionName.setText("SITTING");
		}else if(motionName == Constants.MOTION_JUMPING) {
			mJumpingCnt.setText(cnt);
			mMotionName.setText("JUMPING");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_jumping));
		}else if(motionName == Constants.MOTION_PUSH_UP) {
			mPushUpCnt.setText(cnt);
			mMotionName.setText("PUSH UP");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_push_up));
		}else if(motionName == Constants.MOTION_SIT_UP) {
			mSitUpCnt.setText(cnt);
			mMotionName.setText("SIT UP");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_sit_up));
		}else if(motionName == Constants.MOTION_BUTTERFLY) {
			mButterflyCnt.setText(cnt);
			mMotionName.setText("BUTTERFLY");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_butterfly));
		}else if(motionName == Constants.MOTION_BICEPS_CURL) {
			mBicepsCurlCnt.setText(cnt);
			mMotionName.setText("BICEPS CURL");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_bg_hammer));
		}else if(motionName == Constants.MOTION_SHOULDER_PRESS) {
			mShoulderPressCnt.setText(cnt);
			mMotionName.setText("SHOULDER PRESS");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_bg_shoulder));
		}else if(motionName == Constants.MOTION_CRUNCH) {
			mMotionName.setText("CRUNCH");
		}else if(motionName == Constants.MOTION_UPRIGHT_ROW) {
			mUprightRowCnt.setText(cnt);
			mMotionName.setText("UPRIGHT ROW");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_upright_row));
		}else if(motionName == Constants.MOTION_TOE_RAISE) {
			mToeRaiseCnt.setText(cnt);
			mMotionName.setText("TOE RAISE");
			mMotionImage.setBackground(getResources().getDrawable(R.drawable.element_toe_raise));
		}else if(motionName == Constants.MOTION_PEC_FLY) {
			mMotionName.setText("PEC FLY");
		}else if(motionName == Constants.MOTION_CHEST_PRESS) {
			mMotionName.setText("CHEST PRESS");
		}else if(motionName == Constants.MOTION_LATERAL_RAISE) {
			mLateralCnt.setText(cnt);
			mMotionName.setText("LATERAL RAISE");
		}

//		if(activityGoal<Float.parseFloat(cnt)){
//			setProgressOver(Float.parseFloat(cnt) - activityGoal);
//			setProgress(activityGoal);
//		}else{
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.activity_bar:
//			Intent i = new Intent(mContext, ActivityTodayDetailActivity.class);
//			startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
//			break;
		// case R.id.call_btn:
		// sendMessage("VIB");
		// break;
		// case R.id.date_btn:
		// Date days = new Date();
		// String y = Integer.toString(days.getYear() + 1900);
		// String month = Integer.toString(days.getMonth() + 1);
		// String mm = month.length() == 1 ? "0" + month : month;
		// String date = Integer.toString(days.getDate());
		// String d = date.length() == 1 ? "0" + date : date;
		//
		// String hour = Integer.toString(days.getHours());
		// String h = hour.length() == 1 ? "0" + hour : hour;
		// String minute = Integer.toString(days.getMinutes());
		// String m = minute.length() == 1 ? "0" + minute : minute;
		// String second = Integer.toString(days.getSeconds());
		// String s = second.length() == 1 ? "0" + second : second;
		//
		// sendMessage("RYMDT" + y + "" + mm + "" + d + "" + h + "" + m + "" +
		// s);
		// break;
		// case R.id.ok_btn:
		// sendMessage("OK");
		// break;
		// case R.id.find_btn:
		// sendMessage("U-R");
		// break;
		// case R.id.btn_clear:
		// mTextChat.setText("");
		default:
			break;
		}

	}
}
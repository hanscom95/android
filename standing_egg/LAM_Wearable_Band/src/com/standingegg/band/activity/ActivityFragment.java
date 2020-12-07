
package com.standingegg.band.activity;

import com.standingegg.band.IFragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ActivityFragment extends Fragment implements OnClickListener {
	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;

	// packet show
	// TextView mTextChat;
	TextView mWalkCntTv;
	TextView mKmAllCnt;
	TextView mKcalAllCnt;
	
	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;
	// Button mCallBtn;
	// Button mDateBtn;
	// Button mOkBtn;
	// Button mFindBtn;
	// Button mClearBtn;
	private CircularProgressBar mActivityProgress;
	private CircularProgressBar mActivityProgressOver;
	
	private int todayWalkCnt, todayDis,todayCal ;

	float activityGoal = 1000.0f;

	public ActivityFragment() {};
	
	public ActivityFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity, container, false);

		setContents(v);
		mPreference = Preferences.getInstance(mContext);
		activityGoal =  mPreference.getUserActivityGoal()< 1000? 1000.0f : (float)mPreference.getUserActivityGoal();
		mTodayPreference = TodayPreferences.getInstance(mContext);
		
		setData();
		return v;
	}

	public void setData() {
		todayWalkCnt = mTodayPreference.getActivityAllCount();
		todayDis = mTodayPreference.getRunDistance()+mTodayPreference.getWalkDistance();
		todayCal = mTodayPreference.getRunCal()+mTodayPreference.getWalkCal();
		
		mWalkCntTv.setText(Integer.toString(todayWalkCnt));
		mKmAllCnt.setText(Float.toString(todayDis/1000.0f));
		mKcalAllCnt.setText(Float.toString(todayCal/1000.0f));
		
		setWalking(Integer.toString(todayWalkCnt), todayDis, todayCal);
	}

	private void setContents(View v) {
		// mActivityProgress = (ProgressBar) v.findViewById(R.id.activity_bar);
		// mActivityProgress

		// mCallBtn = (Button) v.findViewById(R.id.call_btn);
		// mDateBtn = (Button) v.findViewById(R.id.date_btn);
		// mOkBtn = (Button) v.findViewById(R.id.ok_btn);
		// mFindBtn = (Button) v.findViewById(R.id.find_btn);
		// mClearBtn = (Button) v.findViewById(R.id.btn_clear);
		////
		// mCallBtn.setOnClickListener(this);
		// mDateBtn.setOnClickListener(this);
		// mOkBtn.setOnClickListener(this);
		// mFindBtn.setOnClickListener(this);
		// mClearBtn.setOnClickListener(this);
		////
		// mTextChat = (TextView) v.findViewById(R.id.text_chat);
		// mTextChat.setMaxLines(1000);
		// mTextChat.setVerticalScrollBarEnabled(true);
		// mTextChat.setMovementMethod(new ScrollingMovementMethod());
		
		
		
		mWalkCntTv = (TextView) v.findViewById(R.id.walk_cnt_tv);
		mActivityProgress = (CircularProgressBar) v.findViewById(R.id.activity_bar);
		mActivityProgressOver = (CircularProgressBar) v.findViewById(R.id.activity_bar_over);

		mKmAllCnt = (TextView) v.findViewById(R.id.km_all);
		mKcalAllCnt = (TextView) v.findViewById(R.id.cal_all);

		mActivityProgress.setOnClickListener(this);
		// mActivityProgress.setProgressBackgroundColor(R.drawable.main_wcircle2);
		mActivityProgress.setProgressColor(Color.RED);
		mActivityProgress.setMarkerEnabled(false);
		mActivityProgressOver.setProgressBackgroundColor(Color.GRAY);
		mActivityProgressOver.setProgressColor(getResources().getColor(R.color.red2));
		mActivityProgressOver.setMarkerEnabled(false);
		setProgress(0);

		
		// mActivityProgress.set
		// mActivityProgress.setMax(10000);
		// mActivityProgress.setProgress(1000);
		// mActivityProgress.setOnTouchListener(null);
		// mActivityProgress.set
		// mActivityProgress.stopAnimation();

		// if (mActivityProgress instanceof Animatable) {
		// ((Animatable) mActivityProgress).stop();
		// // mShouldStartAnimationDrawable = false;
		// }

		// mActivityProgress.cancelLongPress();

		// mActivityProgress.stop();

	}

	public void setProgress(float activityCnt) {
		animate(mActivityProgress, activityCnt);
	}
	public void setProgressOver(float activityCnt) {
		animate(mActivityProgressOver, activityCnt);
	}

	public void animate(final CircularProgressBar progressBar, float activityCnt) {
		ULog.e("CNT:" + activityCnt);

		final float progress = (activityCnt / activityGoal);// *100;

		ULog.e("CNT % ===> " + progress);
		progressBar.setProgress(progress);
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
	 * @param message
	 * @param mActivityAllKm
	 */
	public void setWalking(String cnt, int mActivityAllM, int mActivityAllCal) {
		if (cnt == null || cnt.length() < 1)
			return;
		mWalkCntTv.setText(cnt);
		mKmAllCnt.setText(String.format("%.2f", (mActivityAllM/1000.0f)));
		mKcalAllCnt.setText(String.format("%.2f", (mActivityAllCal/1000.0f)));
		
		if(activityGoal<Float.parseFloat(cnt)){
			setProgressOver(Float.parseFloat(cnt) - activityGoal);
			setProgress(activityGoal);
		}else{
			setProgress(Float.parseFloat(cnt));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_bar:
			Intent i = new Intent(mContext, ActivityTodayDetailActivity.class);
			startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
			break;
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
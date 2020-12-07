/**
 * 수면 메인
 * 
 * 
 * 
 * 
 * 총 카운트 = 수면 목표량 총 분 ! 
 */
package com.standingegg.band.sleeping;

import com.standingegg.band.IFragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.activity.CircularProgressBar;
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
import android.view.ViewGroup;
import android.widget.TextView;

public class SleepFragment extends Fragment implements android.view.View.OnClickListener {

	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;

	// packet show
	// TextView mTextChat;
	private CircularProgressBar mSleepProgress;
	private CircularProgressBar mSleepProgressOver;

	TextView mAllSleepHour;
	TextView mAllSleepMin;
	TextView mDeepSleepHour;
	TextView mDeepSleepMin;
	private TodayPreferences mTodayPreference = null;
	private Preferences mPreference = null;
	int mSleepGoal = 360; // 분
	
	
	int mDeepSleepM=0;
	int mLowSleepM =0;
	int mAwakeMin =0;

	public SleepFragment() {};
	public SleepFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View v = inflater.inflate(R.layout.sleep, container, false);
		setContents(v);
		mTodayPreference = TodayPreferences.getInstance(mContext);
		
		mPreference = Preferences.getInstance(mContext);
		mSleepGoal =  mPreference.getUserSleepGoal() == 0? 60 : mPreference.getUserSleepGoal();
		
		
		setData();
		
		return v;
	}

	private void setData() {
		mDeepSleepM = mTodayPreference.getDeepSleepMin();
		mLowSleepM = mTodayPreference.getLowSleepMin();
		mAwakeMin = mTodayPreference.getAwakeMin();

		setSleep(mDeepSleepM,mLowSleepM);
		
	}

	private void setContents(View v) {

		mSleepProgress = (CircularProgressBar) v.findViewById(R.id.sleep_bar);
		mSleepProgressOver = (CircularProgressBar) v.findViewById(R.id.sleep_bar_over);

		mSleepProgress.setOnClickListener(this);
		mAllSleepHour = (TextView) v.findViewById(R.id.all_sleep_h);
		mAllSleepMin = (TextView) v.findViewById(R.id.all_sleep_m);
		mDeepSleepHour = (TextView) v.findViewById(R.id.d_sleep_h);
		mDeepSleepMin = (TextView) v.findViewById(R.id.d_sleep_m);
		mSleepProgress.setProgressColor(getResources().getColor(R.color.blue));
		mSleepProgress.setMarkerEnabled(false);
		mSleepProgressOver.setProgressBackgroundColor(Color.GRAY);
		mSleepProgressOver.setProgressColor(getResources().getColor(R.color.blue2));
		mSleepProgressOver.setMarkerEnabled(false);
		setProgress(0);

	}

	public void setProgress(int activityCnt) {
		animate(mSleepProgress, activityCnt);
	}
	public void setProgressOver(int activityCnt) {
		animate(mSleepProgressOver, activityCnt);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void animate(final CircularProgressBar progressBar, int activityCnt) {

		ULog.e("CNT:" + activityCnt);

		final float progress = (activityCnt / mSleepGoal);

		ULog.e("CNT % ===> " + progress);
		progressBar.setProgress(progress);
	}

	// Sends user message to remote
	public void sendMessage(String message) {
		if (message == null || message.length() < 1)
			return;
		// send to remote
		if (mFragmentListener != null)
			mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, message);
		else
			return;
	}

	// Sends user message to remote
	public void showsendMessage(String message) {
		if (message == null || message.length() < 1)
			return;

		// if (mTextChat != null) {
		// mTextChat.append("\nSend: ");
		// mTextChat.append(message);
		// int scrollamout =
		// mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) -
		// mTextChat.getHeight();
		// if (scrollamout > mTextChat.getHeight())
		// mTextChat.scrollTo(0, scrollamout);
		// }
	}

	private static final int NEW_LINE_INTERVAL = 1000;
	private long mLastReceivedTime = 0L;

	public void showMessage(String message) {
		if (message != null && message.length() > 0) {

			long current = System.currentTimeMillis();
			//
			// if (current - mLastReceivedTime > NEW_LINE_INTERVAL) {
			// mTextChat.append("\nRcv: ");
			// }
			// mTextChat.append(message);
			// int scrollamout =
			// mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) -
			// mTextChat.getHeight();
			// if (scrollamout > mTextChat.getHeight())
			// mTextChat.scrollTo(0, scrollamout);

			mLastReceivedTime = current;
		}
	}

	public void setSleep(int deepSleep, int lowSleep) {
		// if (deepSleep == 0)
		// return;

		String h = String.valueOf((deepSleep + lowSleep) / 60);
		String m = String.valueOf((deepSleep + lowSleep) % 60);
		String dh = String.valueOf(deepSleep / 60);
		String dm = String.valueOf(deepSleep % 60);

		mAllSleepHour.setText(h.length() == 1 ? "0" + h : h);
		mAllSleepMin.setText(m.length() == 1 ? "0" + m : m);
		mDeepSleepHour.setText(dh.length() == 1 ? "0" + dh : dh);
		mDeepSleepMin.setText(dm.length() == 1 ? "0" + dm : dm);

		if(mSleepGoal< (deepSleep + lowSleep)){
			setProgress(mSleepGoal);
			setProgressOver((deepSleep + lowSleep) - mSleepGoal);
		}else{
			setProgress(deepSleep + lowSleep);
		}
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sleep_bar:
			Intent i = new Intent(mContext, SleepTodayDetailActivity.class);
			startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
			break;
		default:
			break;
		}

	}

	public void updateUi() {
		ULog.v("SLEEP FRAGMENT",
				"message:" + mTodayPreference.getDeepSleepMin() + " / " + mTodayPreference.getLowSleepMin());

		setSleep(mTodayPreference.getDeepSleepMin(), mTodayPreference.getLowSleepMin());

	}

}
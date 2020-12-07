
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.setting;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.standingegg.band.MainActivity.MyReceiver;
import com.standingegg.band.R;
import com.standingegg.band.contents.OnOffButton;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;

public class BatteryActivity extends Activity {

	private Context mContext;

	private Button mFindBtn;
	private static TextView mBatteryTv;
	private Preferences mPreference = null;

	private static ClipDrawable mImageDrawable;

	private Dialog dialog;
	// a field in your class
	private static int mLevel = 0;
	private static int fromLevel = 0;
	private static int toLevel = 0;

	public static final int MAX_LEVEL = 10000;
	public static final int LEVEL_DIFF = 100;
	public static final int DELAY = 30;

	private static Handler mUpHandler = new Handler();
	private static Runnable animateUpImage = new Runnable() {

		@Override
		public void run() {
			doTheUpAnimation(fromLevel, toLevel);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mPreference = Preferences.getInstance(mContext);
		setContentView(R.layout.battery);

		ImageView img = (ImageView) findViewById(R.id.imageView1);
		//img.setScaleX(-1.0f);
		mImageDrawable = (ClipDrawable) img.getDrawable();
		mImageDrawable.setLevel(0);




//		ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setIcon(android.R.color.transparent);



		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		TextView title = (TextView)actionBar.getCustomView().findViewById(R.id.action_bar_title);
		title.setText("BATTERY");

		setComponent();
		setBattery();
//		setBat("88"); sample data
	}

	private void setComponent() {
		mBatteryTv = (TextView) findViewById(R.id.battery);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:

		}
		return super.onOptionsItemSelected(item);
	}

	private void setBattery() {
		Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
		intent.putExtra(Constants.BROADCAST_RECEIVER, Constants.BROADCAST_BAT);
		sendBroadcast(intent);

	}

	public static void setBat(String s) {
		mBatteryTv.setText(s + "%");

		int diff = 0;
		int battInt = Integer.parseInt(s);
		if (battInt > 70) {
			diff = 15;
		} else if (battInt > 50) {
			diff = 5;
		} else if (battInt > 30) {
			diff = -5;
		} else {
			diff = -15;
		}
		int temp_level = ((battInt - diff) * 100);

		mLevel = 0;
		toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;

		mUpHandler.post(animateUpImage);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public static class MyReceiver1 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String s = bundle.getString(Constants.BROADCAST_RECEIVER);
			setBat(s);
		}
	}

	private static void doTheUpAnimation(int fromLevel, int toLevel) {
		mLevel += LEVEL_DIFF;
		mImageDrawable.setLevel(mLevel);

		if (mLevel <= toLevel) {
			mUpHandler.postDelayed(animateUpImage, DELAY);
		} else {
			mUpHandler.removeCallbacks(animateUpImage);
			fromLevel = toLevel;
		}
	}
}
package com.standingegg.band.sleeping;

import com.standingegg.band.R;
import com.standingegg.band.activity.ActivityDetailPartActivity;
import com.standingegg.band.util.Constants;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SleepDetailPartActivity extends Activity implements OnClickListener {

	// private Gallery galleryLoop = null;
	private Gallery graphLoop = null;
	private TextView tvNum = null;
	ImageView mPoint = null;

	TextView mSleepHour, mSleepDeep, mSleepLow;
	TextView mSleepStart, mWakeup, mWaking;

	ImageButton mMinusBtn, mActivityBtn, mPlusBtn;

	int UNIT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleep_detail_part);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setTitle("오늘");
		setComponent();
	}

	int goal = 10000;

	int graph_width = 70;

	public void setComponent() {
		mPoint = (ImageView) findViewById(R.id.point);

		mSleepHour = (TextView) findViewById(R.id.sleep_h);
		mSleepDeep = (TextView) findViewById(R.id.sleep_deep);
		mSleepLow = (TextView) findViewById(R.id.sleep_low);
		mSleepStart = (TextView) findViewById(R.id.sleep_start);
		mWakeup = (TextView) findViewById(R.id.sleep_wakeup);
		mWaking = (TextView) findViewById(R.id.sleep_waking);

		mMinusBtn = (ImageButton) findViewById(R.id.minus_btn);
		mActivityBtn = (ImageButton) findViewById(R.id.activity_btn);
		mPlusBtn = (ImageButton) findViewById(R.id.plus_btn);

		mMinusBtn.setOnClickListener(this);
		mActivityBtn.setOnClickListener(this);
		mPlusBtn.setOnClickListener(this);

		try {
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();

			int unit = bundle.getInt(Constants.DAY_UNIT);
			switch (unit) {
			case Constants.DAILY:
				setDaily();
				break;
			case Constants.WEEKLY:
				setWeekly();
				break;
			case Constants.MONTHLY:
				setMonthly();
				break;
			}
		} catch (Exception e) {
			UNIT = Constants.DAILY;
			mPlusBtn.setEnabled(false);
		}

		// return 단위 + 천단위 콤마 찍은 공통함수 !!!
		mSleepHour.setText("7시간 21분");
		mSleepDeep.setText("4시간 33분");
		mSleepLow.setText("2시간 48분");
		mSleepStart.setText("05:52");
		mWakeup.setText("13:13");
		mWaking.setText("0분");

		tvNum = (TextView) findViewById(R.id.num);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(graph_width, 100);
		params.gravity = Gravity.CENTER;
		mPoint.setLayoutParams(params);

		graphLoop = (Gallery) findViewById(R.id.graphLoop);
		graphLoop.setAdapter(new LoopAdapter(this));
		graphLoop.setScaleX(-1.0f);

		graphLoop.setSpacing(10);

		graphLoop.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int num = arg2 + 1;
				// .show();
				tvNum.setText("Gallery：" + num);
			}
		});
		graphLoop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mSleepHour.setText(position + "시간 21분");
				mSleepDeep.setText(position + "시간 33분");
				mSleepLow.setText(position + "시간 48분");
				mSleepStart.setText("0" + position + ":52");
				mWakeup.setText(position + "3:13");
				mWaking.setText(position + "분");

				String[] date = { "오늘", "어제", "11/09", "11/08", "11/07", "11/06", "11/05", "11/04", "11/03", "11/02",
						"11/01" };
				String[] week = { "이번주", "저번주", "10/26~11/01", "10/19~10/25", "10/26~11/01", "10/26~11/01",
						"10/26~11/01", "10/26~11/01", "10/26~11/01", "10/26~11/01", "10/26~11/01" };
				String[] month = { "이번달", "저번달", "9월", "8월", "7월", "6월", "5월", "4월", "3월", "2월", "1월" };

				if (UNIT == Constants.DAILY) {
					getActionBar().setTitle(date[position]);
				} else if (UNIT == Constants.WEEKLY) {
					getActionBar().setTitle(week[position]);
				} else if (UNIT == Constants.MONTHLY) {
					getActionBar().setTitle(month[position]);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

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

	class LoopAdapter extends BaseAdapter {
		private Context context;

		public LoopAdapter(Context context) {
			this.context = context;
		}

		private String[] date = { "오늘", "어제", "11/09", "11/08", "11/07", "11/06", "11/05", "11/04", "11/03", "11/02",
				"11/01" };
		private String[] week = { "이번주", "저번주", "10/26~11/01", "10/19~10/25", "10/26~11/01", "10/26~11/01",
				"10/26~11/01", "10/26~11/01", "10/26~11/01", "10/26~11/01", "10/26~11/01" };
		private String[] month = { "이번달", "저번달", "9월", "8월", "7월", "6월", "5월", "4월", "3월", "2월", "1월" };

		private Integer[] imageHeight = { 200, 145, 100, 333, 1, 120, 287, 22, 198, 30, 1 };

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public int getCount() {
			return date.length;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout l = new LinearLayout(context);
			l.setOrientation(LinearLayout.VERTICAL);

			ImageView low = new ImageView(context);
			low.setImageResource(R.drawable.activity_graph_point);
			low.setScaleType(ImageView.ScaleType.FIT_XY);
			low.setLayoutParams(new Gallery.LayoutParams(graph_width, (imageHeight[position] / 3)));

			ImageView deep = new ImageView(context);
			deep.setImageResource(R.drawable.sleep_graph_bar);
			deep.setScaleType(ImageView.ScaleType.FIT_XY);
			deep.setLayoutParams(new Gallery.LayoutParams(graph_width, (imageHeight[position] / 2)));

			TextView tv = new TextView(context);
			tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			if (UNIT == Constants.DAILY) {
				tv.setText(date[position]);
			} else if (UNIT == Constants.WEEKLY) {
				tv.setText(week[position]);
			} else if (UNIT == Constants.MONTHLY) {
				tv.setText(month[position]);
			}
			tv.setScaleX(-1.0f);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(8);

			l.addView(low);
			l.addView(deep);
			l.addView(tv);

			return l;

		}

	}

	private void setDaily() {
		UNIT = Constants.DAILY;
		graph_width = 70;

		// getActionBar().setTitle("이번달");

		((BaseAdapter) graphLoop.getAdapter()).notifyDataSetChanged();
		graphLoop.setSelection(0);
		mPlusBtn.setEnabled(false);
		mMinusBtn.setEnabled(true);
	}

	private void setWeekly() {
		UNIT = Constants.WEEKLY;
		graph_width = 110;

		// getActionBar().setTitle("이번주");

		((BaseAdapter) graphLoop.getAdapter()).notifyDataSetChanged();
		graphLoop.setSelection(0);
		mPlusBtn.setEnabled(true);
		mMinusBtn.setEnabled(true);
	}

	private void setMonthly() {
		UNIT = Constants.MONTHLY;
		graph_width = 150;

		// getActionBar().setTitle("이번달");ㅊ

		// galleryLoop.invalidate();
		((BaseAdapter) graphLoop.getAdapter()).notifyDataSetChanged();
		graphLoop.setSelection(0);
		mMinusBtn.setEnabled(false);
		mPlusBtn.setEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.minus_btn:
			if (UNIT == Constants.DAILY) {
				setWeekly();
			} else if (UNIT == Constants.WEEKLY) {
				setMonthly();
			}
			;

			break;
		case R.id.activity_btn:
			Intent activity = new Intent(getApplicationContext(), ActivityDetailPartActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.DAY_UNIT, UNIT);
			activity.putExtras(bundle);
			activity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(activity);

			break;
		case R.id.plus_btn:
			if (UNIT == Constants.WEEKLY) {
				setDaily();

			} else if (UNIT == Constants.MONTHLY) {
				setWeekly();
			}
			;

			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent();
		this.setResult(RESULT_OK, intent);

		// 일단 result 말고 main intent 로 ... 일단...

		finish();
	}
}

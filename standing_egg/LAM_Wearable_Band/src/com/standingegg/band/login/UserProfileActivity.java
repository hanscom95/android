
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.NumberPickerDialog;
import com.standingegg.band.util.NumberPickerDialog.OnNumberSetListener;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

public class UserProfileActivity extends Activity implements OnClickListener, OnNumberSetListener {

	Button mBeforeBtn;
	private Context mContext;
	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;
	
	private DBManager mDBManager;

	private TextView mName, mId, mAvgCnt, mAllday, mAllDis, mBirth, mHeight, mHeightUnit, mWeight, mWeightUnit,
			mActGoal, mSleepGoal;

	private ImageButton mNameBtn, mBirthBtn, mHeightBtn, mWeightBtn, mActGoalBtn, mSleepGoalBtn;
	Calendar calendar;
	
	boolean edtFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.user_profile);
		mDBManager = new DBManager(getApplicationContext(), "lamband.db", null, 1);
		mContext = this;
		mPreference = Preferences.getInstance(mContext);
		mTodayPreference = TodayPreferences.getInstance(mContext);

		setComponent();
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		calendar = Calendar.getInstance();
	}

	private void setComponent() {
		mName = (TextView) findViewById(R.id.show_user_name);
		mId = (TextView) findViewById(R.id.show_user_id);
		mAvgCnt = (TextView) findViewById(R.id.user_avg_cnt);
		mAllday = (TextView) findViewById(R.id.user_all_day);
		mAllDis = (TextView) findViewById(R.id.user_all_distance);
		mBirth = (TextView) findViewById(R.id.show_user_birth);
		mHeight = (TextView) findViewById(R.id.show_user_height);
		mHeightUnit = (TextView) findViewById(R.id.show_user_height_unit);
		mWeight = (TextView) findViewById(R.id.show_user_weight);
		mWeightUnit = (TextView) findViewById(R.id.show_user_weight_unit);
		mActGoal = (TextView) findViewById(R.id.show_user_act_goal);
		mSleepGoal = (TextView) findViewById(R.id.show_user_sleep_goal);
		mNameBtn = (ImageButton) findViewById(R.id.name_edit_btn);
		mBirthBtn = (ImageButton) findViewById(R.id.birth_edit_btn);
		mHeightBtn = (ImageButton) findViewById(R.id.height_edit_btn);
		mWeightBtn = (ImageButton) findViewById(R.id.weight_edit_btn);
		mActGoalBtn = (ImageButton) findViewById(R.id.act_goal_edit_btn);
		mSleepGoalBtn = (ImageButton) findViewById(R.id.sleep_goal_edit_btn);

		mNameBtn.setOnClickListener(this);
		mBirthBtn.setOnClickListener(this);
		mHeightBtn.setOnClickListener(this);
		mWeightBtn.setOnClickListener(this);
		mActGoalBtn.setOnClickListener(this);
		mSleepGoalBtn.setOnClickListener(this);

		mName.setText(mPreference.getUserName());
		mId.setText(mPreference.getUserId());
		
		if(mPreference.getUsingDate() == 1){
			mAvgCnt.setText(Integer.toString(mTodayPreference.getActivityAllCount()));
		}else{
			mAvgCnt.setText(Integer.toString(mDBManager.getAvgActCnt()));
		}
		
		mAllday.setText(Integer.toString(mPreference.getUsingDate()));

		ULog.v("mTodayPreference.getWalkDistance() , " + mTodayPreference.getWalkDistance());
		ULog.v("mTodayPreference.getRunDistance() , " + mTodayPreference.getRunDistance());
		ULog.v("mPreference.getmUserAllDis() , " + mPreference.getmUserAllDis());

		mAllDis.setText(Float.toString(
				(mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance() + mPreference.getmUserAllDis())
						/ 1000.0f));
		mBirth.setText(mPreference.getUserBirth());
		mHeight.setText(Integer.toString(mPreference.getUserTall()));
		mHeightUnit.setText(mPreference.getUserTallUnit());
		mWeight.setText(Integer.toString(mPreference.getUserWeight()));
		mWeightUnit.setText(mPreference.getUserWeigthUnit());
		mActGoal.setText(Integer.toString(mPreference.getUserActivityGoal()));
		mSleepGoal.setText(Integer.toString(mPreference.getUserSleepGoal() / 60) + "시간 "
				+ Integer.toString(mPreference.getUserSleepGoal() % 60) + "분");
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

	private void InputDialog(String s, final int flag) {
		AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

		alert1.setTitle(s);
		// alert1.setMessage("Message");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);

		switch (flag) {
		case 1:
			input.setText(mName.getText().toString());
			break;
		case 3:
			input.setText(mHeight.getText().toString());
			input.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
			break;
		case 4:
			input.setText(mWeight.getText().toString());
			input.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
			break;
		}

		alert1.setView(input);

		alert1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				edtFlag = false;
				String value = input.getText().toString();
				value.toString();
				switch (flag) {
				case 1:
					mPreference.setUserName(value.toString());
					mName.setText(value.toString());
					break;
				case 3:
					mPreference.setUserTall(Integer.parseInt(value.toString()));
					mHeight.setText(value.toString());
					break;
				case 4:
					mPreference.setUserWeight(Integer.parseInt(value.toString()));
					mWeight.setText(value.toString());
					break;
				}
				// mActGoal.setText(value.toString());
				// mSleepGoal.setText(value.toString());

			}
		});
		alert1.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});

		alert1.show();
	}

	private void NumberPickerDialog() {
		String[] walkcnt = new String[90];
		int j = 1000;
		for (int i = 0; i < walkcnt.length; i++) {
			walkcnt[i] = Integer.toString(j);
			j = j + 1000;
		}

		NumberPickerDialog dialog = new NumberPickerDialog(mContext, this,
				(mPreference.getUserActivityGoal() / 1000 - 1), walkcnt, 0, 89, R.string.activity_goal_edt,
				R.string.activity_walk, 1111);
		
		dialog.show();

	}

	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			edtFlag= false;
			// TODO Auto-generated method stub
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			String year1 = Integer.toString(calendar.getTime().getYear() + 1900);
			String month1 = Integer.toString(calendar.getTime().getMonth() + 1);
			String day1 = Integer.toString(calendar.getTime().getDate());

			mBirth.setText(year1 + "-" + (month1.length() == 1 ? "0" + month1 : month1) + "-"
					+ (day1.length() == 1 ? "0" + day1 : day1));
			mPreference.setUserBirth(year1 + "-" + (month1.length() == 1 ? "0" + month1 : month1) + "-"
					+ (day1.length() == 1 ? "0" + day1 : day1));

		}
	};

	public void alertCalendar() {
		DatePickerDialog dialog = new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		try {
			ULog.e("birth get==>" + mBirth.getText().toString().replaceAll("-", ""));

			String[] b = mBirth.getText().toString().split("-");

			Date d = new SimpleDateFormat("yyyyMMdd").parse("19100001");
			dialog.getDatePicker().setMinDate(d.getTime());
			dialog.getDatePicker().init(Integer.parseInt(b[0]), (Integer.parseInt(b[1]) - 1), Integer.parseInt(b[2]),
					null);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		dialog.getDatePicker().setMaxDate(new Date().getTime());
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.name_edit_btn:
			this.InputDialog("이름 수정", 1);
			// 확인 버튼 누르면? back button?
			// server, preferences update
			break;
		case R.id.birth_edit_btn:
			alertCalendar();
			break;
		case R.id.height_edit_btn:
			this.InputDialog("키 수정", 3);
			break;
		case R.id.weight_edit_btn:
			this.InputDialog("몸무게 수정", 4);
			break;
		case R.id.act_goal_edit_btn:
			NumberPickerDialog();
			break;
		case R.id.sleep_goal_edit_btn:
			SleepGoalDialog();
			break;
		}

	}

	private void SleepGoalDialog() {
		int before_hour = (mPreference.getUserSleepGoal() / 60);
		int before_min = (mPreference.getUserSleepGoal() % 60);

		AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

		alert1.setTitle("수면 목표 수정");

		final NumberPicker mNumberPicker, mNumberPicker2;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.number_picker_dialog2, null);
		alert1.setView(view);

		mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
		mNumberPicker2 = (NumberPicker) view.findViewById(R.id.number_picker_2);

		TextView unit = (TextView) view.findViewById(R.id.unit);
		TextView unit2 = (TextView) view.findViewById(R.id.unit_2);

		String[] time = new String[25];
		String t;
		for (int i = 0; i < time.length; i++) {
			t = Integer.toString(i);
			time[i] = t.length() < 2 ? "0" + t : t;
		}

		mNumberPicker.setDisplayedValues(time);
		mNumberPicker.setMinValue(0);
		mNumberPicker.setMaxValue(24);
		mNumberPicker.setValue(before_hour);

		String[] minutes = new String[60];
		for (int i = 0; i < minutes.length; i++) {
			t = Integer.toString(i);
			minutes[i] = t.length() < 2 ? "0" + t : t;
		}

		mNumberPicker2.setDisplayedValues(minutes);
		mNumberPicker2.setMinValue(0);
		mNumberPicker2.setMaxValue(59);
		mNumberPicker2.setValue(before_min);

		alert1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				edtFlag = false;
				String value = Integer.toString(mNumberPicker.getValue());
				String value2 = Integer.toString(mNumberPicker2.getValue());
				mSleepGoal.setText(value + "시간 " + value2 + "분");

				mPreference.setUserTall(((Integer.parseInt(value) * 60) + Integer.parseInt(value2)));
			}
		});
		alert1.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});

		alert1.show();
	}

	private void DialogSimple(String s) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(s).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.setTitle("경고");
		alert.show();
	}

	@Override
	public void onBackPressed() {
		if(edtFlag){
			super.onBackPressed();
		}else{
		

			try {
				ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (mobile.isConnected() || wifi.isConnected()) {
//					if (mobile.getState() == NetworkInfo.State.CONNECTED
//							|| mobile.getState() == NetworkInfo.State.CONNECTING) {
//						DialogSimple("Wi-Fi를 제외한 3G, LTE, LTE-A를 사용 서비스를 이용할 경우 추가 요금이 발생할 수 있습니다.");
//					}
					Bundle user = new Bundle();
					user.putInt(Constants.UID, mPreference.getUid());
					user.putString(Constants.USER_GENDER, mPreference.getUserGender());
					user.putString(Constants.USER_NAME, mPreference.getUserName());
					user.putInt(Constants.USER_TALL, mPreference.getUserTall());
					user.putInt(Constants.USER_WEIGHT, mPreference.getUserWeight());
					user.putString(Constants.USER_BIRTH, mPreference.getUserBirth());
					user.putInt(Constants.USER_ACTIVITY_GOAL, mPreference.getUserActivityGoal());
					user.putInt(Constants.USER_SLEEP_GOAL, mPreference.getUserSleepGoal());

					ULog.e("mPreference.getUid()->" + mPreference.getUid());
					ULog.e("mPreference.getUserGender()->" + mPreference.getUserGender());
					ULog.e("mPreference.getUserName()->" + mPreference.getUserName());
					ULog.e("mPreference.getUserTall()->" + mPreference.getUserTall());
					ULog.e("mPreference.getUserWeight()->" + mPreference.getUserWeight());
					ULog.e("mPreference.getUserBirth()->" + mPreference.getUserBirth());
					ULog.e("mPreference.getUserActivityGoal()->" + mPreference.getUserActivityGoal());
					ULog.e("mPreference.getUserSleepGoal()->" + mPreference.getUserSleepGoal());

					// "(int) uid
					// (String) gender
					// (String) name
					// (int) tall
					// (int) weight
					// (String) birth
					// (int) activity_goal
					// (int) sleep_goal"
					
					int i = RecvSendPacket.updateUserInfo(user);
					// "200 OK "업데이트 성공
					// 400 invalid value 데이터 형식 오류
					// 460 user id does not exist 해당 아이디의 유저가 존재하지 않음
					// 417 invalided date value" birth date 형식 다름"

					switch (i) {
					case Constants.REQUEST_OK:
						super.onBackPressed();
						break;
					case Constants.REQUEST_INVAILD_VALUE:
						DialogSimple("데이터 형식 오류입니다.");
						break;
					case Constants.REQUEST_USER_IS_NOT:
						DialogSimple("해당 아이디의 유저가 존재하지 않습니다.");
						break;
					case Constants.REQUEST_INVALID_DATA_VALUE:
						DialogSimple("birth date 형식이 다릅니다.");
						break;
					default:
						break;
					}

				} else {
					DialogSimple("네트워크에 연결중이지 않을 경우 ....");
				}

			} catch (NullPointerException e) {
				DialogSimple("네트워크에 연결중이지 않을 경우 ....");
			}
		
		
		}
		

		// server up edit data //인터넷 연결 중일 때만
	}

	@Override
	public void onNumberSet(int dialogId, int number) {
		if (dialogId == 1111) {
			edtFlag = false;
			String[] walkcnt = new String[90];
			int j = 1000;
			for (int i = 0; i < walkcnt.length; i++) {
				walkcnt[i] = Integer.toString(j);
				j = j + 1000;
			}
			mActGoal.setText(walkcnt[number]);
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("!!!!!!!!!!!!!!********");
			ULog.e("********"+walkcnt[number]+"*********");
			mPreference.setUserActGoal(Integer.parseInt(walkcnt[number]));
		}
	}
}
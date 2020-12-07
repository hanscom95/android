
/**
 * 가입 - 사용자 정보 입력 ( 생년월일, 성별, 키, 몸무게)
 * @author CCZZZEE
 *
 */
package com.standingegg.band.join;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.standingegg.band.R;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateUserInfoActivity extends Activity implements OnClickListener, OnFocusChangeListener {
	String TAG = this.getClass().getName();

	EditText mUserName;
	TextView mBirthDate;
	RadioGroup mUserGender;
	EditText mUserTall;
	EditText mUserWeight;
	Spinner mTallUnit;
	Spinner mWeightUnit;
	Button mPrev, mNext;
	// EditText mText;
	Calendar calendar = Calendar.getInstance();
	Bundle bundle;
	
	public static Activity sActivityReference; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_user_2);
		Intent intent = getIntent();
		bundle = intent.getExtras();
		setComponent();
		
		sActivityReference = CreateUserInfoActivity.this;

	}

	private void setComponent() {
		mPrev = (Button) findViewById(R.id.before);
		mNext = (Button) findViewById(R.id.next);
		// mText = (EditText)findViewById(R.id.editText1);
		mUserName = (EditText) findViewById(R.id.join_name);
		mBirthDate = (TextView) findViewById(R.id.user_birth);
		mUserGender = (RadioGroup) findViewById(R.id.user_gender);
		mUserTall = (EditText) findViewById(R.id.user_tall);
		mUserWeight = (EditText) findViewById(R.id.user_weight);
		mTallUnit = (Spinner) findViewById(R.id.height_unit);
		mWeightUnit = (Spinner) findViewById(R.id.weight_unit);

		mPrev.setOnClickListener(this);
		mNext.setOnClickListener(this);
		mBirthDate.setOnClickListener(this);
		
		mBirthDate.setOnFocusChangeListener(new OnFocusChangeListener(){
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if(hasFocus){
		        	alertCalendar();

		        }
		    }
		});
		

		addItemsOnSpinner();
	}

	// add items into spinner dynamically

	public void addItemsOnSpinner() {
		List<String> list = new ArrayList<String>();

		list.add("cm");
		list.add("feet");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mTallUnit.setAdapter(dataAdapter);

		List<String> list2 = new ArrayList<String>();

		list2.add("kg");
		list2.add("lbs");

		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list2);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWeightUnit.setAdapter(dataAdapter2);

	}

	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			String year1 = Integer.toString(calendar.getTime().getYear() + 1900);
			String month1 = Integer.toString(calendar.getTime().getMonth() + 1);
			String day1 = Integer.toString(calendar.getTime().getDate());

			mBirthDate.setText(year1 + "-" + (month1.length() == 1 ? "0" + month1 : month1) + "-"
					+ (day1.length() == 1 ? "0" + day1 : day1));
			
			mUserTall.setFocusable(true);
			mUserTall.setClickable(true);
		}
	};

	public void dateCalendar(View v) {
		alertCalendar();
	}

	public void alertCalendar() {
		DatePickerDialog dialog = new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		try {
			String b[] = new String[3];
			if ("".equals(mBirthDate.getText().toString())) {
				b[0] = "1990";
				b[1] = "01";
				b[2] = "01";
			} else {
				b = mBirthDate.getText().toString().split("-");
			}
			Date d = new SimpleDateFormat("yyyyMMdd").parse("19100001");
			dialog.getDatePicker().setMinDate(d.getTime());
			dialog.getDatePicker().init(Integer.parseInt(b[0]), (Integer.parseInt(b[1]) - 1), Integer.parseInt(b[2]), null);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		dialog.getDatePicker().setMaxDate(new Date().getTime());
		dialog.show();
	}

	private void DialogSimple() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("정확한 값을 입력하세요.").setCancelable(false).setNegativeButton("확인",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setTitle("경고");
		alert.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.before:
			onBackPressed();
			break;
		case R.id.next:
			String name = mUserName.getText().toString();
			String birth = mBirthDate.getText().toString();
			String gender = mUserGender.getCheckedRadioButtonId() == R.id.female ? "female" : "male";
			String tall = mUserTall.getText().toString();
			String weight = mUserWeight.getText().toString();
			String tallUnit = mTallUnit.getSelectedItem().toString();
			String weightUnit = mWeightUnit.getSelectedItem().toString();

			ULog.d(TAG, "name===>" + name);
			ULog.d(TAG, "birth===>" + birth);
			ULog.d(TAG, "gender===>" + gender);
			ULog.d(TAG, "tall===>" + tall);
			ULog.d(TAG, "weight===>" + weight);
			ULog.d(TAG, "tallUnit===>" + tallUnit);
			ULog.d(TAG, "weightUnit===>" + weightUnit);

			if (Integer.parseInt(tall) <= 0 || Integer.parseInt(weight) <= 0 || "".equals(birth) || "".equals(name)
					|| name.length() < 3) {
				this.DialogSimple();
				return;
			}

			Intent intent = new Intent(getApplicationContext(), CreateGoalActivity.class);

			bundle.putString(Constants.USER_NAME, name);
			bundle.putString(Constants.USER_BIRTH, birth);
			bundle.putString(Constants.USER_GENDER, gender);
			bundle.putString(Constants.USER_TALL, tall);
			bundle.putString(Constants.USER_WEIGHT, weight);
			bundle.putString(Constants.USER_TALL_UNIT, tallUnit);
			bundle.putString(Constants.USER_WEIGHT_UNIT, weightUnit);

			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.user_birth:
			alertCalendar();
			break;
		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (mBirthDate.isFocusable()) {
			alertCalendar();
		}

	}
	
	
}
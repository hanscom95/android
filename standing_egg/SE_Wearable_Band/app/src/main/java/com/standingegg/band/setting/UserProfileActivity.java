
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.setting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
//20160727 import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class UserProfileActivity extends Activity implements OnClickListener, View.OnFocusChangeListener {
	String TAG = this.getClass().getName();
	Context mContext;
	EditText mUserName;
	TextView mBirthDate;
	RadioGroup mUserGender;
	RadioButton mFemale, mMale;
	EditText mUserTall;
	EditText mUserWeight;
	Spinner mTallUnit;
	Spinner mWeightUnit;
	Button mSave;
	// EditText mText;
	Calendar calendar = Calendar.getInstance();

	public static Activity sActivityReference;

	private Preferences mPreferences = null;

	boolean btn_click = false;

	private DBManager mDBManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.user_option_profile);
		Intent intent = getIntent();

		mDBManager = new DBManager(getApplicationContext(), "seband.db", null, 1);

		mPreferences = Preferences.getInstance(mContext);
		setComponent();
		sActivityReference = UserProfileActivity.this;


		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setIcon(android.R.color.transparent);
		TextView title = (TextView)actionBar.getCustomView().findViewById(R.id.action_bar_title);
		title.setText("USER PROFILE");
	}

	private void setComponent() {
		mSave = (Button) findViewById(R.id.save);
		mUserName = (EditText) findViewById(R.id.join_name);
		mBirthDate = (TextView) findViewById(R.id.user_birth);
		mUserGender = (RadioGroup) findViewById(R.id.user_gender);
		mFemale = (RadioButton) findViewById(R.id.female);
		mMale = (RadioButton) findViewById(R.id.male);
		mUserTall = (EditText) findViewById(R.id.user_tall);
		mUserWeight = (EditText) findViewById(R.id.user_weight);
		mTallUnit = (Spinner) findViewById(R.id.height_unit);
		mWeightUnit = (Spinner) findViewById(R.id.weight_unit);

		mSave.setOnClickListener(this);
		mBirthDate.setOnClickListener(this);

		mBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					alertCalendar();
				}
			}
		});


		addItemsOnSpinner();



		mUserName.setText(mPreferences.getUserName());
		mBirthDate.setText(mPreferences.getUserBirth());
		if(mPreferences.getUserGender() == "female") {
			mFemale.setChecked(true);
		}else {
			mMale.setChecked(true);
		}
		mUserTall.setText(mPreferences.getUserTall()+"");
		mUserWeight.setText(mPreferences.getUserWeight()+"");
		mTallUnit.setSelection(mPreferences.getUserTallUnit());
		mWeightUnit.setSelection(mPreferences.getUserWeigthUnit());






	}

	// add items into spinner dynamically
	int selected = 0;
	public void addItemsOnSpinner() {
		List<String> list = new ArrayList<String>();

		list.add("cm");
		list.add("feet");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mTallUnit.setAdapter(dataAdapter);
		mTallUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ULog.e("position ==>" + position);
				((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray2));
				if(mUserTall.getText().toString() != null && !"".equals(mUserTall.getText().toString()))	{
					switch (position) {
						case 0: //cm
							if(selected!=0)
								mUserTall.setText( String.format("%.0f",(Float.parseFloat( mUserTall.getText().toString())*30.48)));
							break;
						case 1: //feet
							mUserTall.setText( String.format("%.2f",(Float.parseFloat( mUserTall.getText().toString())*0.032808)));

							break;
					}
					selected++;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});


		List<String> list2 = new ArrayList<String>();

		list2.add("kg");
		list2.add("lbs");

		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list2);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWeightUnit.setAdapter(dataAdapter2);
		mWeightUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ULog.e("position ==>" + position);
				((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray2));
				if(mUserWeight.getText().toString() != null && !"".equals(mUserWeight.getText().toString()) ){
					switch (position) {
						case 0: //kg
							if(mPreferences.getUserWeigthUnit() != 0) {
								mUserWeight.setText( String.format("%.0f",(Float.parseFloat( mUserWeight.getText().toString())*0.453592)));
							}
							break;
						case 1: //lbs
							if(mPreferences.getUserWeigthUnit() != 1) {
								mUserWeight.setText(String.format("%.2f", (Float.parseFloat(mUserWeight.getText().toString()) * 2.204623)));
							}
							break;
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});


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
		dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.set), dialog);
		dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), dialog);
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
		dialog.setTitle(getString(R.string.birth));
		dialog.show();
	}

	private void DialogSimple() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(getString(R.string.forgot_value_chk)).setCancelable(false).setNegativeButton(getString(R.string.set),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(getString(R.string.login_arlam));
		alert.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.save:
				if(btn_click) return;

				btn_click = true;

				String name = mUserName.getText().toString();
				String birth = mBirthDate.getText().toString();
				String gender = mUserGender.getCheckedRadioButtonId() == R.id.female ? "female" : "male";
				String tall = mUserTall.getText().toString();
				String weight = mUserWeight.getText().toString();
				int tallUnit = mTallUnit.getSelectedItemPosition();
				int weightUnit = mWeightUnit.getSelectedItemPosition();

				ULog.d(TAG, "name===>" + name);
				ULog.d(TAG, "birth===>" + birth);
				ULog.d(TAG, "gender===>" + gender);
				ULog.d(TAG, "tall===>" + tall);
				ULog.d(TAG, "weight===>" + weight);
				ULog.d(TAG, "tallUnit===>" + tallUnit);
				ULog.d(TAG, "weightUnit===>" + weightUnit);

				if("".equals(weight) || "".equals(tall)){
					btn_click = false;
					this.DialogSimple();
					return;
				}

				if (Float.parseFloat(tall) <= 0 || Float.parseFloat(weight) <= 0 || "".equals(birth) || "".equals(name)
						|| name.getBytes().length < 6) {
					this.DialogSimple();
					btn_click = false;
					return;
				}

				if(tallUnit == 1){
					tall = String.format("%.0f",((float)Float.parseFloat(tall)*30.48));
				}

				if(weightUnit == 1){
					weight = String.format("%.0f",((float)Float.parseFloat(weight)*0.453592));
				}


				mPreferences.setUserName(name);
				mPreferences.setUserBirth(birth);
				mPreferences.setUserGender(gender);
				mPreferences.setUserTall(Integer.parseInt(tall));
				mPreferences.setUserWeight(Integer.parseInt(weight));
				mPreferences.setUserUnit(tallUnit, weightUnit);


				mDBManager.updateUserInfo(mPreferences.getUserId(),mPreferences.getUserPw() , mPreferences.getUserTall(), mPreferences.getUserWeight(), mPreferences.getUserGender(), mPreferences.getUserDeviceAdderess(), mPreferences.getUserName(), mPreferences.getUserBirth());



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
/**
 * 
 */
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import com.standingegg.band.R;
//20160727 import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPwActivity extends Activity implements OnClickListener {

	Button mNextBtn;
	TextView mFindCancel;
	
	EditText mName;
	EditText mId;
	TextView mBirth;
	
	// EditText mText;
	Calendar calendar;

	boolean button_chk = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.find_pw1);
		calendar = Calendar.getInstance();
		setComponent();
	}

	private void setComponent() {
		mNextBtn = (Button) findViewById(R.id.next);
		mFindCancel = (TextView) findViewById(R.id.find_cancel);
		mFindCancel.setPaintFlags(mFindCancel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // Text Under Line
		
		mName = (EditText) findViewById(R.id.join_name);
		mId = (EditText) findViewById(R.id.join_id);
		mBirth = (TextView) findViewById(R.id.birth);
		
		mBirth.setOnClickListener(this);
		
		mNextBtn.setOnClickListener(this);
		mFindCancel.setOnClickListener(this);
	}
	

	public void dateCalendar(View v) {
		alertCalendar();
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

			mBirth.setText(year1 + "-" + (month1.length() == 1 ? "0" + month1 : month1) + "-"
					+ (day1.length() == 1 ? "0" + day1 : day1));
		}
	};
	
	public void alertCalendar() {
		DatePickerDialog dialog = new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.set), dialog);
		dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), dialog);
		try {
			String b[]= new String[3];
			if ("".equals(mBirth.getText().toString())) {
				b[0] = "1990";
				b[1] = "01";
				b[2] = "01";
			} else {
				b = mBirth.getText().toString().split("-");
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

	public void alertDialog(String s){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(s).setCancelable(false).setNegativeButton(R.string.set,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(R.string.login_arlam);
		alert.show();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.next:
			//20160727 start
			/*
			if(button_chk) return;
			
			button_chk = true;
			
			String name= mName.getText().toString();
			String id= mId.getText().toString();
			String birth= mBirth.getText().toString();
			
			
			ULog.d("birthbirthbirthbirthbirth"+birth);
			ULog.d("birthbirthbirthbirthbirth"+birth);
			ULog.d("birthbirthbirthbirthbirth"+birth);
			ULog.d("birthbirthbirthbirthbirth"+birth);
			
			
			if("".equals(name) || "".equals(id) || "".equals(birth)){
				alertDialog(getString(R.string.forgot_value_chk));
				button_chk= false;
				return;
			}
			
			JSONObject jObject;

			try {
				jObject = new JSONObject(RecvSendPacket.findUserPw(name, id, birth));
				int result_code = jObject.getInt("result");
				
				switch (result_code) {
				case 200:
					// String msg = jObject.getString("message");
					String data = jObject.getString("data");
					intent = new Intent(getApplicationContext(), ChangePwActivity.class);
					intent.putExtra("token_key", data);
					intent.putExtra("id", id);
					startActivity(intent);
				
					finish();
					break;
				case 400:
					button_chk= false;
					alertDialog(getString(R.string.forgot_400_error));
					break;
				case 460:
					button_chk= false;
					alertDialog(getString(R.string.forgot_460_error));
					break;
				default:
					break;
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		*/
			//20160727 end
			break;
		case R.id.find_cancel:
			onBackPressed();
			break;
		case R.id.birth:
			
			alertCalendar();
			
			
			
			
			break;
		default:
			break;
		}

	}

}
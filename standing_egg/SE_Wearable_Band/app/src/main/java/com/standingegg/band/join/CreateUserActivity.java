package com.standingegg.band.join;

import java.util.regex.Pattern;

import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
//20160727 import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 가입 - id / pw 입력
 * 
 * @author CCZZZEE
 *
 */
public class CreateUserActivity extends Activity implements OnClickListener {
	String TAG = "CreateUserActivity";
	Button mCreateAccountBtn;
	// TextView mAlertTxtName;
	TextView mAlertTxt;
	TextView mAlertTxt2, mTearmsPrivacy;
	EditText mId;
	// EditText mName;
	EditText mPw;
	EditText mPwChk;
	DBManager dbManager;
	LinearLayout mLinearLayout;
	
	CheckBox mAgreeChk;

	boolean mChkId = false;
	boolean mChkName = false;
	boolean mChkPw = false;

	String address;
	
	boolean btn_click = false;
	
	public static Activity sActivityReference; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		address = intent.getStringExtra(Constants.EXTRA_DEVICE_ADDRESS);

		setContentView(R.layout.create_user_1);
		setComponent();
		//dbManager = new DBManager(getApplicationContext(), "account.db", null, 1);
		dbManager = new DBManager(getApplicationContext(), "seband.db", null, 1);
		
//		getActionBar().setIcon(android.R.color.transparent);
		
		sActivityReference = CreateUserActivity.this;
	}

	private void setComponent() {
		mCreateAccountBtn = (Button) findViewById(R.id.create_account_btn);
		mAgreeChk = (CheckBox)findViewById(R.id.agree_chk);
		mId = (EditText) findViewById(R.id.join_id);
		// mName = (EditText) findViewById(R.id.join_name);
		mPw = (EditText) findViewById(R.id.join_pw);
		mPwChk = (EditText) findViewById(R.id.join_pw_chk);
		// mAlertTxtName = (TextView) findViewById(R.id.alert_txt_name);

		mAlertTxt = (TextView) findViewById(R.id.alert_txt);
		mAlertTxt2 = (TextView) findViewById(R.id.alert_pw_txt);
		mTearmsPrivacy = (TextView) findViewById(R.id.terms_privacy_policy);
		mTearmsPrivacy.setPaintFlags(mTearmsPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // Text Under Line
		
		mLinearLayout = (LinearLayout) findViewById(R.id.alert_linear);

		mCreateAccountBtn.setOnClickListener(this);
		mTearmsPrivacy.setOnClickListener(this);

		// mName.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if (!hasFocus) {
		// Pattern ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐]+$");// 영문,
		// // 숫자,
		// // 한글만
		// // 허용
		//
		// if ("".equals(mName.getText().toString().trim()) ||
		// mName.getText().toString().contains(" ")
		// || !ps.matcher(mName.getText().toString()).matches()) {
		// mAlertTxtName.setText(getString(R.string.validate)+"(공백, 특수문자 입력
		// 불가)");
		// mAlertTxtName.setVisibility(View.VISIBLE);
		// mName.setFocusable(true);
		// mChkName = false;
		// } else {
		// mAlertTxtName.setVisibility(View.INVISIBLE);
		// mChkName = true;
		// }
		// }
		//
		// }
		// });

		// String result = SendByHttp(); // 메시지를 서버에 보냄

		// ULog.d(result);
		// String[][] parsedData = jsonParserList(result); // 받은 메시지를 json 파싱

		mId.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$"); // 영문 숫자만 허용
					if ("".equals(mId.getText().toString().trim()) || mId.getText().toString().contains(" ")
							|| !ps.matcher(mId.getText().toString()).matches()) {
						mAlertTxt.setText(getString(R.string.validate) + getString(R.string.validate_msg));
						mLinearLayout.setVisibility(View.VISIBLE);
						mId.setFocusable(true);
						mChkId = false;
					}else if(mId.getText().length() < 6) {
						mAlertTxt.setText(getString(R.string.validate) + getString(R.string.validate_eng_length_msg));
						mLinearLayout.setVisibility(View.VISIBLE);
						mId.setFocusable(true);
						mChkId = false;
					}else {
						mLinearLayout.setVisibility(View.INVISIBLE);
						mChkId = true;
					}
				}

			}
		});
	}
	private void DialogSimple(){
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setMessage(R.string.login_member_agr).setCancelable(
	        false).setNegativeButton(R.string.set,
	        new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            dialog.cancel();
	        }
	        });
	    AlertDialog alert = alt_bld.create();
	    alert.setTitle(R.string.login_member_policy);
	    alert.show();
	}
	
	
	private void DialogSimple2(String s){
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setMessage(s).setCancelable(
	        false).setNegativeButton(R.string.set,
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
		case R.id.terms_privacy_policy:
			this.DialogSimple();
			break;
		case R.id.create_account_btn:

			intent = new Intent(getApplicationContext(), CreateUserInfoActivity.class);
			// intent = new Intent(getApplicationContext(),
			// JoinTermActivity.class);

			String id = mId.getText().toString();
			String pw = mPw.getText().toString();
			Bundle bundle = new Bundle();
			bundle.putString(Constants.USER_ID, id);
			bundle.putString(Constants.USER_PW, pw);
			bundle.putString(Constants.EXTRA_DEVICE_ADDRESS, address);

			intent.putExtras(bundle);
			startActivity(intent);
			//20160727 start
		/*
			if(btn_click) return;
			btn_click = true;
			String id = mId.getText().toString();
			// String name = mName.getText().toString();
			String pw = mPw.getText().toString();
			String pwChk = mPwChk.getText().toString();
			
			Log.d("CreateUser", "pw : " + pw);
			if (!pw.equals(pwChk) || pw.length() < 4) {
				Log.d("CreateUser", "pw : " + pw.length());
				mAlertTxt2.setText(getString(R.string.unsame_pw));
				mAlertTxt2.setVisibility(View.VISIBLE);
				mPw.setFocusable(true);
				mChkPw = false;
			}else {
				Log.d("CreateUser", "pw : false");
				mAlertTxt2.setVisibility(View.INVISIBLE);
				mChkPw = true;
			}
			
			if (mId.getText().toString().trim().length()<3) {
				mAlertTxt.setText(getString(R.string.validate) + getString(R.string.validate_length_msg));
				mLinearLayout.setVisibility(View.VISIBLE);
				mId.setFocusable(true);
				mChkId = false;
				btn_click = false;
				return;
			} else {
//				mAlertTxt2.setVisibility(View.INVISIBLE);
				mChkId = true;
			}
			
			if(!mAgreeChk.isChecked()){
				DialogSimple2(getString(R.string.login_member_policy_chk));
				btn_click = false;
				return;
			}
			

			if (!mChkId || *//* !mChkName || *//* !mChkPw) {
				if (!mChkId) {
					mAlertTxt.setText(getString(R.string.validate) + getString(R.string.validate_msg));
					mLinearLayout.setVisibility(View.VISIBLE);
				}

				Log.d("CreateUser", "ccccc");
				btn_click = false;
				return;
			}

			// http://52.69.202.139:8080/user/check
			int req_cd = RecvSendPacket.usingIdCheck(id);
			
			if (req_cd == Constants.REQUEST_DUPLICATE_USER_ID ) {  
				mAlertTxt.setText(getString(R.string.same_id));
				mLinearLayout.setVisibility(View.VISIBLE);
				btn_click = false;
				return;
			}else if (req_cd == Constants.REQUEST_INVAILD_VALUE) {  
				mAlertTxt.setText(getString(R.string.Invalid_user_id));
				mLinearLayout.setVisibility(View.VISIBLE);
				btn_click = false;
				return;
			} else{
				mLinearLayout.setVisibility(View.INVISIBLE);
			}

			intent = new Intent(getApplicationContext(), CreateUserInfoActivity.class);
			// intent = new Intent(getApplicationContext(),
			// JoinTermActivity.class);

			Bundle bundle = new Bundle();
			// bundle.putString(Constants.USER_NAME, name);
			bundle.putString(Constants.USER_ID, id);
			bundle.putString(Constants.USER_PW, pw);
			bundle.putString(Constants.EXTRA_DEVICE_ADDRESS, address);

			intent.putExtras(bundle);
			startActivity(intent);*/
			//20160727 end
			break;
		default:
			break;
		}
	}

}
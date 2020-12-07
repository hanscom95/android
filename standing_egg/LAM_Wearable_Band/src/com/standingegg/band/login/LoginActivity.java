/**
 * 
 */
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.standingegg.band.MainActivity;
import com.standingegg.band.R;
import com.standingegg.band.join.CreateUserActivity;
import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	
	Button mLoginBtn;
	TextView mCreatAccount, mFindPw;
	TextView mAlert;
	EditText mId,mPw;
	String address;

	public static Activity sActivityReference; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		address = intent.getStringExtra(Constants.EXTRA_DEVICE_ADDRESS);
		
		setContentView(R.layout.login);
		
		setComponent();
		
		sActivityReference = LoginActivity.this;
	}
	
	
	private void setComponent() {
		mLoginBtn = (Button)findViewById(R.id.login_btn);
		mCreatAccount = (TextView)findViewById(R.id.create_account);
		mFindPw = (TextView) findViewById(R.id.forgot_pw);
		mId = (EditText) findViewById(R.id.login_id);
		mPw = (EditText) findViewById(R.id.login_pw);
		mAlert = (TextView) findViewById(R.id.login_alert);
		
		mLoginBtn.setOnClickListener(this);
		mCreatAccount.setOnClickListener(this);
		mFindPw.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.login_btn:
			String id =mId.getText().toString();
			String pw = mPw.getText().toString();
			if("".equals(id) || "".equals(pw)){
				mAlert.setVisibility(View.VISIBLE);
				mAlert.setText(R.string.login_null_chk);
				return;
			}
			JSONObject jObject;
			try {
				jObject = new JSONObject(RecvSendPacket.UserLogin(id,pw,address));
//				460 user id does not exist 아이디가 존재하지 않음
//				470 password does not match 비밀번호가 다름
//				471 device address does not matched 디바이스 주소 다름
//				480 invalid device address "유효하지 않은 device address
				int result_code = jObject.getInt("result");
				String data = jObject.getString("data");
				
				switch (result_code) {
				case Constants.REQUEST_OK:
					mAlert.setVisibility(View.INVISIBLE);
					
					intent = new  Intent(getApplicationContext(), MainActivity.class);
					
					Bundle login_data = new Bundle();
					login_data.putString(Constants.EXTRA_DEVICE_ADDRESS, this.address);
					login_data.putString(Constants.USER_ID, id);
					login_data.putString(Constants.USER_PW, pw);
					login_data.putInt(Constants.UID, Integer.parseInt(data));
					intent.putExtras(login_data);
					
					ULog.e("XXXXXXXXXXXXXXXXXXXX", "Login return uid>>>>>>>>"+data+"<<<<<<");
					ULog.e("XXXXXXXXXXXXXXXXXXXX", "Login return pw>>>>>>>>"+pw+"<<<<<<");
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					this.finish();
					break;
				case Constants.REQUEST_DB_ERROR:
					mAlert.setVisibility(View.VISIBLE);
					mAlert.setText(R.string.login_db_error_msg);
					break;
				case Constants.REQUEST_DEVICE_ADDRESS_DOES_NOT_MATCHED: 
					mAlert.setVisibility(View.VISIBLE);
					mAlert.setText(R.string.login_device_address_msg);
					break;
				case Constants.REQUEST_INVALID_DEVICE_ADDRESS: 
					mAlert.setVisibility(View.VISIBLE);
					mAlert.setText(R.string.login_invalid_device_msg);
					break;
				case Constants.REQUEST_USER_IS_NOT: 
				case Constants.REQUEST_PW_NOT_MATCH: 
					mAlert.setVisibility(View.VISIBLE);
					mAlert.setText(R.string.login_pw_confirm_msg);
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mAlert.setVisibility(View.VISIBLE);
				mAlert.setText(R.string.login_db_error_msg);
				
				return;
			}
			
			break;
		case R.id.create_account:
			intent = new  Intent(getApplicationContext(), CreateUserActivity.class);
			intent.putExtra(Constants.EXTRA_DEVICE_ADDRESS, this.address);
			startActivity(intent);
			break;
		case R.id.forgot_pw:
			intent = new  Intent(getApplicationContext(), ForgotPwActivity.class);
			startActivity(intent);
			
			break;
		default:
			break;
		}
		
	}
	
	
}
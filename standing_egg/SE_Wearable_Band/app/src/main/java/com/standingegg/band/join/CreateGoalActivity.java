
/**
 * 가입 - 목표량 설정
 * @author CCZZZEE
 *
 */
package com.standingegg.band.join;

import org.json.JSONException;
import org.json.JSONObject;

import com.standingegg.band.MainActivity;
import com.standingegg.band.R;
import com.standingegg.band.login.LoginActivity;
//20160727 import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.NumberPickerDialog;
import com.standingegg.band.util.NumberPickerDialog.OnNumberSetListener;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CreateGoalActivity extends Activity implements OnClickListener, OnNumberSetListener {

	Button mPrev, mNext;
	Bundle bundle;
	TextView mActivityGoal;
	TextView mSleepGoalH, mSleepGoalM;
	private Context mContext;
	
	
	boolean btn_click = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_user_3);
		mContext = this;
		setComponent();

		Intent intent = getIntent();
		bundle = intent.getExtras();

		//20160802 getActionBar().setIcon(android.R.color.transparent);
	}

	private void setComponent() {
		mPrev = (Button) findViewById(R.id.button1);
		mNext = (Button) findViewById(R.id.button2);
		mActivityGoal = (TextView) findViewById(R.id.user_activity_goal);
		mSleepGoalH = (TextView) findViewById(R.id.user_sleep_goal_h);
		mSleepGoalM = (TextView) findViewById(R.id.user_sleep_goal_m);

		mActivityGoal.setOnClickListener(this);
		mSleepGoalH.setOnClickListener(this);
		mSleepGoalM.setOnClickListener(this);
		mPrev.setOnClickListener(this);
		mNext.setOnClickListener(this);
	}
	private void DialogSimple(String s){
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setMessage(s).setCancelable(
	        false).setNegativeButton(getString(R.string.set),
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
		switch (v.getId()) {
		case R.id.button1:
			onBackPressed();
			// intent = new Intent(getApplicationContext(), .class);
			// startActivity(intent);
			break;
		case R.id.button2:
			if(btn_click) return;

			//20160802 start
			Intent intent = new Intent(getApplicationContext(), JoinLoadingActivity.class);
			String a = mActivityGoal.getText().toString();
			bundle.putInt(Constants.USER_ACTIVITY_GOAL, Integer.parseInt(a));
			bundle.putInt(Constants.USER_SLEEP_GOAL, Integer.parseInt(mSleepGoalH.getText().toString()) * 60
					+ Integer.parseInt(mSleepGoalM.getText().toString()));


			bundle.putString(Constants.EXTRA_DEVICE_ADDRESS, bundle.getString(Constants.EXTRA_DEVICE_ADDRESS ));
			bundle.putString(Constants.USER_ID, bundle.getString(Constants.USER_ID ));
			bundle.putString(Constants.USER_PW, bundle.getString(Constants.USER_PW ));
			bundle.putString(Constants.USER_NAME, bundle.getString(Constants.USER_NAME));
			bundle.putBoolean(Constants.JOIN_FLAG, true);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			Activity userInfoActivity = CreateUserInfoActivity.sActivityReference;
			Activity userActivity = CreateUserActivity.sActivityReference;
			Activity loginActivity = LoginActivity.sActivityReference;

			if(userInfoActivity != null) {
				userInfoActivity.finish();
			}

			if(userActivity != null) {
				userActivity.finish();
			}

			if(loginActivity != null) {
				loginActivity.finish();
			}
			btn_click = true;
			finish();
			//20160802 end

			//20160727 start
			// 이전화면에서 sharedPreferences에 담아 옴.
			/*Intent intent = new Intent(getApplicationContext(), JoinLoadingActivity.class);
			String a = mActivityGoal.getText().toString();
			bundle.putInt(Constants.USER_ACTIVITY_GOAL, Integer.parseInt(a));
			bundle.putInt(Constants.USER_SLEEP_GOAL, Integer.parseInt(mSleepGoalH.getText().toString()) * 60
					+ Integer.parseInt(mSleepGoalM.getText().toString()));
			
			JSONObject jObject;
			try {
				btn_click = true;
				
				jObject = new JSONObject(RecvSendPacket.UserSignIn(bundle));

				int requestcode = jObject.getInt("result");
				String msg = jObject.getString("messege");
				
				switch (requestcode) {
				case Constants.REQUEST_OK:
					int data = jObject.getInt("data");
					ULog.v("data ====>" + data);
					bundle.putString(Constants.EXTRA_DEVICE_ADDRESS, bundle.getString(Constants.EXTRA_DEVICE_ADDRESS ));
					bundle.putString(Constants.USER_ID, bundle.getString(Constants.USER_ID ));
					bundle.putString(Constants.USER_PW, bundle.getString(Constants.USER_PW ));
					bundle.putInt(Constants.UID, data);
					bundle.putString(Constants.USER_NAME, bundle.getString(Constants.USER_NAME));
					bundle.putBoolean(Constants.JOIN_FLAG, true);
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					startActivity(intent);
					
					Activity userInfoActivity = CreateUserInfoActivity.sActivityReference;
					Activity userActivity = CreateUserActivity.sActivityReference;
					Activity loginActivity = LoginActivity.sActivityReference;
					
					if(userInfoActivity != null) {
						userInfoActivity.finish();
					}
					
					if(userActivity != null) {
						userActivity.finish();
					}
					
					if(loginActivity != null) {
						loginActivity.finish();
					}
					btn_click = true;
					finish();
					break;
				case Constants.REQUEST_DUPLICATE_USER_ID:
					btn_click = false;
					DialogSimple(getString(R.string.id_duplicate));
					return;
				case Constants.REQUEST_DUPLICATE_DEVICE_ADDRESS:
					btn_click = false;
					DialogSimple(getString(R.string.address_duplicate)); 
					return;
				case Constants.REQUEST_INVAILD_VALUE:
					btn_click = false;
					DialogSimple(getString(R.string.invaild_value)); 
					return;
				case Constants.REQUEST_INVALID_DATA_VALUE:
					btn_click = false;
					DialogSimple(getString(R.string.invalid_data_value)); 
					return;
				case Constants.REQUEST_DEVICE_ADDRESS:
					btn_click = false;
					DialogSimple(getString(R.string.device_address_invaild)); 
					return;
				default:
					btn_click = false;
					DialogSimple( getString(R.string.server_error)); 
					return;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//20160727 end
			

			break;

		case R.id.user_activity_goal:
			String[] walkcnt = new String[90];
			int j = 1000;
			for (int i = 0; i < walkcnt.length; i++) {
				walkcnt[i] = Integer.toString(j);
				j = j+1000;
			}

			NumberPickerDialog dialog = new NumberPickerDialog(mContext, this, 9, walkcnt, 0, 89, R.string.activity_goal, R.string.activity_walk, 1111);

			dialog.show();
			break;
		case R.id.user_sleep_goal_h:

			String[] time = new String[25];
			String t;
			for (int i = 0; i < time.length; i++) {
				t = Integer.toString(i);
				time[i] = t.length() < 2 ? "0" + t : t;
			}
			NumberPickerDialog dialog2 = new NumberPickerDialog(mContext, this, 0, time, 0, 24, R.string.sleep_goal_h,
					0, 2222);
			dialog2.show();
			break;
		case R.id.user_sleep_goal_m:

			String[] minutes = new String[60];
			for (int i = 0; i < minutes.length; i++) {
				t = Integer.toString(i);
				minutes[i] = t.length() < 2 ? "0" + t : t;
			}

			NumberPickerDialog dialog3 = new NumberPickerDialog(mContext, this, 0, minutes, 0, 59,
					R.string.sleep_goal_m, 0, 3333);
			dialog3.show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onNumberSet(int dialogId, int number) {
		if (dialogId == 1111) {
			String[] walkcnt = new String[90];
			int j = 1000;
			for (int i = 0; i < walkcnt.length; i++) {
				walkcnt[i] = Integer.toString(j);
				j = j+1000;
			}
			mActivityGoal.setText(walkcnt[number]);
		} else if (dialogId == 2222) {
			mSleepGoalH.setText(Integer.toString(number));
		} else if (dialogId == 3333) {
			mSleepGoalM.setText(Integer.toString(number));
		}

	}

}
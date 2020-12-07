/**
 * 
 *//*
*//**
 * @author CCZZZEE
 *
 *//*
package com.standingegg.band.login;

import com.standingegg.band.FragmentAdapter;
import com.standingegg.band.R;
import com.standingegg.band.activity.ActivityFragment;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

*//**
 * 로딩
 * 
 * @author cczzee
 *
 *//*
public class LoginDataLoading extends Activity extends Fragment  {
	
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { return
	 * inflater.inflate(R.layout.login_loading, container, false); }
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_loading);

		setC();
	}

	private void setC() {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				Intent intent = new Intent();
				intent.putExtra("resultSetting", "결과를 처리하였습니다.");
				setResult(RESULT_OK, intent);
				finish();
			}
		}, 3000);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case Constants.MESSAGE_BT_STATE_CONNECTED:
				break;
			
			default:
				break;
			}

			super.handleMessage(msg);
		}
	}
	
	
	
	
	
	
	public void finished(){
		finish();
	}
}*/
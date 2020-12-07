
/**
 * 가입 - 이용약관
 * @author CCZZZEE
 *
 */
package com.standingegg.band.join;

import com.standingegg.band.R;
import com.standingegg.band.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class JoinTermActivity extends Activity implements OnClickListener {

	Button mBeforeBtn;
	Button mNextBtn;
	CheckBox mAgreeChk;
	CheckBox mAgree2Chk;
	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_user_1_1);

		Intent intent = getIntent();
		bundle = intent.getExtras();

		setComponent();
	}

	private void setComponent() {
		mBeforeBtn = (Button) findViewById(R.id.before_btn);
		mNextBtn = (Button) findViewById(R.id.next_btn);
		mAgreeChk = (CheckBox) findViewById(R.id.agree_chk);
		mAgree2Chk = (CheckBox) findViewById(R.id.agree2_chk);

		mBeforeBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.before_btn:
			// intent = new Intent(getApplicationContext(), UserActivity.class);
			// startActivity(intent);
			break;
		case R.id.next_btn:
			if (!validate()) {
				Toast.makeText(getBaseContext(), "전체 약관에 동의해야 합니다.", Toast.LENGTH_LONG).show();
				return;
			}
			intent = new Intent(getApplicationContext(), CreateUserInfoActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private boolean validate() {
		if (!mAgreeChk.isChecked())
			return false;
		else if (!mAgree2Chk.isChecked())
			return false;
		else
			return true;
	}
}
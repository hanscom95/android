/**
 * 
 */
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import com.standingegg.band.R;
//20160727 import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePwActivity extends Activity implements OnClickListener{
	
	Button mNextBtn;
	TextView mFindCancel;
	String token_key="";
	String id= "";
	
	
	EditText mPw;
	EditText mPwChk;
	boolean button_chk= false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_pw2);
		setComponent();
		token_key = getIntent().getExtras().getString("token_key");
		id = getIntent().getExtras().getString("id");
		ULog.e("token_key***************************"+token_key);
		ULog.e("id***************************"+id);
	}
	
	private void setComponent() {
		mNextBtn = (Button)findViewById(R.id.next);
		mPw = (EditText) findViewById(R.id.re_pw);
		mPwChk = (EditText) findViewById(R.id.re_pw_chk);
		mNextBtn.setOnClickListener(this);
	}

	public void alertDialog(String s,final int flag){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(s).setCancelable(false).setNegativeButton(getString(R.string.set),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						
						if(flag == 1){
							Intent intent = new Intent();
							intent = new  Intent(getApplicationContext(), LoginActivity.class);
							startActivity(intent);
							finish();
						}
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(getString(R.string.login_arlam));
		alert.show();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.next:
			if(button_chk) return;
			
			String pw = mPw.getText().toString();
			String pw_chk = mPwChk.getText().toString();

			if("".equals(pw) || "".equals(pw_chk)){
				alertDialog(getString(R.string.forgot_value_chk),0);
				button_chk= false;
				return;
			}
			if(!pw.equals(pw_chk)){
				alertDialog(getString(R.string.unsame_pw),0);
				button_chk= false;
				return;
			}
			try {
				//20160727 start
				/*int status = RecvSendPacket.changePw(id, pw, token_key);
				
				switch (status) {
				case 200:
					alertDialog(getString(R.string.change_ok),1);
					break;
				case 400:
					button_chk= false;
					alertDialog(getString(R.string.forgot_400_error),0);
					break;
				case 460:
					button_chk= false;
					alertDialog(getString(R.string.forgot_460_error),0);
					break;
				default:
					break;
				}*/
				//20160727 end
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			break;
		default:
			break;
		}
		
	}
//
//	@Override
//	public void onBackPressed() {
//		return;
//	}
	
}
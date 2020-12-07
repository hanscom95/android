/**
 * 
 */
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import com.standingegg.band.R;
import com.standingegg.band.packet.RecvSendPacket;
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
		alt_bld.setMessage(s).setCancelable(false).setNegativeButton("확인",
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
		alert.setTitle("알림");
		alert.show();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.next:
			
			String pw = mPw.getText().toString();
			String pw_chk = mPwChk.getText().toString();

			if("".equals(pw) || "".equals(pw_chk)){
				alertDialog("정확한 값을 입력하세요.",0);
				return;
			}
			if(!pw.equals(pw_chk)){
				alertDialog("비밀번호를 확인하세요.",0);
				return;
			}
			try {
				int status = RecvSendPacket.changePw(id, pw, token_key);
				
				switch (status) {
				case 200:
					alertDialog("변경이 완료되었습니다.",1);
					break;
				case 400:
					alertDialog("입력된 정보가 일치하지 않습니다.",0);
					break;
				case 460:
					alertDialog("해당 사용자가 존재하지 않습니다.",0);
					break;
				default:
					break;
				}
				
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

/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.setting;

import java.util.ArrayList;

import com.standingegg.band.IntroActivity;
import com.standingegg.band.MainActivity;
import com.standingegg.band.MainActivity.MyReceiver;
import com.standingegg.band.R;
import com.standingegg.band.bluetooth.BleManager;
import com.standingegg.band.bluetooth.ConnectionInfo;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.ota.File;
//20160727 import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SupportActivity extends Activity {

	private Context mContext;
	private BluetoothDevice bluetoothDevice = null;

	private RelativeLayout mFirmwareUpdate;
	private RelativeLayout mPolicyTxt;
	private RelativeLayout mUserDrop;
	private static TextView mVersionTxt;
	private TextView mVersionStateTxt;
	
	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;
	private DBManager dbManager;
	private static ConnectionInfo mConnectionInfo = null;
	private BleManager bleManager = null;

	private ImageView mFirmwareImg, mFirmwareArrowImg;
	ArrayList<String> filesList = new ArrayList<String>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mConnectionInfo = ConnectionInfo.getInstance(this);

		bleManager = BleManager.getInstance(getApplicationContext(), null);

		bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mConnectionInfo.getDeviceAddress());
		mContext = this;
		mPreference = Preferences.getInstance(mContext);
		mTodayPreference = TodayPreferences.getInstance(mContext);
		
		dbManager = new DBManager(getApplicationContext(), "seband.db", null, 1);
		
		setContentView(R.layout.support);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);

		setComponent();

		setVserion();
	}

	private void setVserion() {
		Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
		intent.putExtra(Constants.BROADCAST_RECEIVER, Constants.BROADCAST_VERSION);
		sendBroadcast(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:

		}
		return super.onOptionsItemSelected(item);
	}

	private void DialogSimple(String s,String title,int resid) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

		if(resid!=0){
			final TextView mTextView;
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.textview_dialog, null);
			alt_bld.setView(view);

			mTextView = (TextView) view.findViewById(R.id.dialog_txt); 
			mTextView.setText(getString(R.string.poilcy_txt));
			mTextView.setMovementMethod(new ScrollingMovementMethod());
			
		}

		alt_bld.setMessage(s).setCancelable(false).setNegativeButton(R.string.set, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(title);
		alert.show();
	}

	private void DialogSimple2(String s) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

		alt_bld.setMessage(s).setCancelable(false).setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				//20160727 start
				/*try {
					int response = RecvSendPacket.signout(mPreference.getUserId(),mPreference.getUserPw(),mPreference.getUserDeviceAdderess());
					
					switch (response) {
					case Constants.REQUEST_OK:
						
						AlertDialog.Builder alt_bld = new AlertDialog.Builder(mContext);

						alt_bld.setMessage(R.string.account_drop_success).setCancelable(false).setNegativeButton(R.string.set, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent();
								SupportActivity.this.setResult(RESULT_OK, intent);
								
								SupportActivity.this.finish();
								//receiver 호출 
							}
						});
						AlertDialog alert = alt_bld.create();
						alert.setTitle(R.string.login_arlam);
						alert.show();
						
						break;
					case Constants.REQUEST_USER_IS_NOT:
						DialogSimple(getString(R.string.account_user_is_not),getString(R.string.login_arlam),0);
						break;
					case Constants.REQUEST_PW_NOT_MATCH:
						DialogSimple(getString(R.string.account_pw_not_match),getString(R.string.login_arlam),0);
						break;
					case Constants.REQUEST_INVALID_DEVICE_ADDRESS:
						DialogSimple(getString(R.string.account_invalid_device_address),getString(R.string.login_arlam),0);
						break;
					case Constants.REQUEST_DB_ERROR:
						DialogSimple(getString(R.string.account_db_error),getString(R.string.login_arlam),0);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					dialog.cancel();
					DialogSimple(getString(R.string.account_drop_fail),getString(R.string.login_arlam),0);
				}*/
				//20160727 end
				
				
				
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(R.string.caution);
		alert.show();
	}

	private void setComponent() {
		mVersionTxt = (TextView) findViewById(R.id.version_);
		
		mFirmwareUpdate = (RelativeLayout) findViewById(R.id.firmware_update);
		mPolicyTxt = (RelativeLayout) findViewById(R.id.poilcy_txt);
		mUserDrop = (RelativeLayout) findViewById(R.id.account_drop);
		mFirmwareImg = (ImageView) findViewById(R.id.firmware_img);
		mFirmwareArrowImg = (ImageView) findViewById(R.id.firmware_arrow);
		mVersionStateTxt = (TextView) findViewById(R.id.firmware_state_txt);
		
		filesList = File.list();
		if(filesList.size() < 1) {
			mFirmwareImg.setVisibility(View.GONE);
			mFirmwareArrowImg.setVisibility(View.GONE);
			mVersionStateTxt.setVisibility(View.VISIBLE);
		}else {
			mVersionStateTxt.setVisibility(View.GONE);
			mFirmwareImg.setVisibility(View.VISIBLE);
			mFirmwareArrowImg.setVisibility(View.VISIBLE);
		}

		mFirmwareUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(bleManager.getState() != 16) {
					return;
				}
				if(filesList.size() > 0) {
					Intent firmware = new Intent(getApplicationContext(), DeviceActivity.class);// FirmwareUpdateActivity.class,
					firmware.putExtra("device", bluetoothDevice);
					startActivityForResult(firmware, Constants.REQUEST_SUPPORT_FIRMWARE);
				}
			}
		});
		mPolicyTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogSimple(getString(R.string.privacy_policy),getString(R.string.privacy_policy),1);
			}
		});
		mUserDrop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

					ConnectivityManager manager = (ConnectivityManager) mContext
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

					if (mobile.isConnected() || wifi.isConnected()) {
						DialogSimple2(getString(R.string.account_drop_question));
						// 탈퇴 

					} else {
						DialogSimple(getString(R.string.network_connect_fail),getString(R.string.caution),0);
						return;
					}

				} catch (NullPointerException e) {
					DialogSimple(getString(R.string.network_connect_fail),getString(R.string.login_arlam),0);
					return;
				}

			}
		});

	}
	
	@Override
	protected void onActivityResult(int request, int result, Intent Data) {
		switch (request) {
		case Constants.REQUEST_SUPPORT_FIRMWARE:
			if (result == Activity.RESULT_OK) {
				Intent intent = new Intent();
				SupportActivity.this.setResult(Constants.RESULT_FIRM_WARE, intent);
				SupportActivity.this.finish();
			}
			break;
		}
	}
	
	public static void setVersionT(String s) {
		mVersionTxt.setText(s+" ");
	}
	
	public static class MyReceiver2 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String s = bundle.getString(Constants.BROADCAST_RECEIVER);
			setVersionT(s);
		}
	}
}
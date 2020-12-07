package com.example.test1218;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity{

	final String TAG = "MainActivity";

	int mCurrentFragmentIndex;

	private Menu menu;
	private ActionBar actionbar;
	public final static int FRAGMENT_ONE = 0;
	public final static int FRAGMENT_TWO = 1;
	private int serverCount = 0;
	private int serverCount2 = 0;
	public static String serverIp = "192.168.1.41";
	public static int serverPort = 48701;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.actionbar = getActionBar();
		actionbar.setTitle("Sensor Data Chart");

		mCurrentFragmentIndex = FRAGMENT_ONE;
		
		fragmentReplace(mCurrentFragmentIndex);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_action_bar, menu);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_button: 
				actionbar.setTitle("Noise Chart Data");
				menu.findItem(R.id.action_button).setVisible(false);
				menu.findItem(R.id.action_button2).setVisible(true);
				
				mCurrentFragmentIndex = FRAGMENT_TWO;
				fragmentReplace(mCurrentFragmentIndex);
				break;
			case R.id.action_button2: 
				actionbar.setTitle("Sensor Chart Data");
				menu.findItem(R.id.action_button).setVisible(true);
				menu.findItem(R.id.action_button2).setVisible(false);
				
				mCurrentFragmentIndex = FRAGMENT_ONE;
				fragmentReplace(mCurrentFragmentIndex);
				break;
				
			case R.id.action_button3: 
				final String items[] = { "192.168.1.41 / 48701", "192.168.1.42 / 48702", "192.168.1.43 / 48703" , "192.168.1.28 / 48598" , "192.168.1.88 / 48688" }; //48702
			    AlertDialog.Builder ab = new AlertDialog.Builder(this);
			    ab.setTitle("IP/Port Checked");
			    ab.setSingleChoiceItems(items, serverCount,
			        new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int item) {
			            // 각 리스트를 선택했을때 
			        	serverCount = item;
			        }
			        }).setPositiveButton("Ok",
			        new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {
				            // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
				        	if(serverCount == 0) {
				        		serverIp = "192.168.1.41";
				        		serverPort = 48701;
				        	}else if(serverCount == 1) {
				        		serverIp = "192.168.1.42";
				        		serverPort = 48702;
				        	}else if(serverCount == 2) {
				        		serverIp = "192.168.1.43";
				        		serverPort = 48703;
				        	}else if(serverCount == 3) {
				        		serverIp = "192.168.1.28";
				        		serverPort = 48598;
				        	}else if(serverCount == 4) {
				        		serverIp = "192.168.1.88";
				        		serverPort = 48688;
				        	}
				        	serverCount2 = serverCount;
				        }
			        }).setNegativeButton("Cancel",
			        new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {
				            // Cancel 버튼 클릭시
				        	serverCount = serverCount2;
				        }
			        });
			    ab.show();
				break;
			
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void fragmentReplace(int reqNewFragmentIndex) {

		Fragment newFragment = null;

		newFragment = getFragment(reqNewFragmentIndex);

		// replace fragment
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.ll_fragment, newFragment);

		// Commit the transaction
		transaction.commit();

	}
	
	private Fragment getFragment(int idx) {
		Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_ONE:
			newFragment = new OneFragment();
			break;
		case FRAGMENT_TWO:
			newFragment = new NoiseFragment();
			break;

		default:
			break;
		}

		return newFragment;
	}

}

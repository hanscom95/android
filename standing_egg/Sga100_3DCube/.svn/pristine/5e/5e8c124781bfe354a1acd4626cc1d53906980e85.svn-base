package com.example.newdrawtest;

import com.example.newdrawtest.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements OnClickListener {

	final String TAG = "MainActivity";

	private  Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// replace fragment
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.add(R.id.l_fragment, new ThreeDFragment());
		transaction.add(R.id.ll_fragment, new TwoFragment());

		// Commit the transaction
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}

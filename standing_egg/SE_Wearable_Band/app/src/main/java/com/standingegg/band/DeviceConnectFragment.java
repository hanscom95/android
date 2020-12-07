/**
 * 기기 연결 설정 / 기기를 등록하시겠습니까?
 */
package com.standingegg.band;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class DeviceConnectFragment extends Fragment {

	private FragmentListener mFragmentListener = null;
	
	public interface FragmentListener {
		public static final int CALLBACK_BACK = 1;
		public static final int CALLBACK_OK = 2;
		
		public void OnFragmentCallback(int msgType);
	}

	private Handler mHandler = null;
	
	public DeviceConnectFragment(Context c, FragmentListener l) {
		mContext = c;
		mFragmentListener = l; 
	}
	
	private Button mNoBtn, mYesBtn;
	private LinearLayout mBtnLinear;

	private Context mContext = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.device_connect_1, container, false);
		mContext = getActivity().getApplicationContext();
		setContent(v);
		
		
		
		return v;
	}

	private void setContent(View v) {
		mNoBtn = (Button) v.findViewById(R.id.no_btn);
		mYesBtn = (Button) v.findViewById(R.id.yes_btn);
		mBtnLinear = (LinearLayout) v.findViewById(R.id.btn_linear);
		mBtnLinear.setVisibility(View.VISIBLE);
		
		mNoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentListener != null)
					mFragmentListener.OnFragmentCallback(FragmentListener.CALLBACK_BACK);
			}
		});
		
		mYesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentListener != null)
					mFragmentListener.OnFragmentCallback(FragmentListener.CALLBACK_OK);
			}
		});
		
		
	}

}
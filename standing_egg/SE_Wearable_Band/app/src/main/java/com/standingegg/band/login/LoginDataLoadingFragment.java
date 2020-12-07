/**
 * 
 */
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import com.standingegg.band.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 로그인 정보 로딩
 * 
 * @author cczzee
 */
public class LoginDataLoadingFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.login_loading, container, false);

		return v;
	}

}
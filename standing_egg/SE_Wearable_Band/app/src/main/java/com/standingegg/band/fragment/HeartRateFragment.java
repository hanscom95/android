/**
 * @author taehoon moon
 *
 */
package com.standingegg.band.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.protobuf.UninitializedMessageException;
import com.standingegg.band.MainActivity;
import com.standingegg.band.R;
import com.standingegg.band.util.StnEggPkt;

/**
 * 심박 정보 뷰
 * 
 * @author cczzee
 */
public class HeartRateFragment extends Fragment {

	TextView heartRateText;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.heart_rate, container, false);

		heartRateText = (TextView) v.findViewById(R.id.heart_rate_text);

		setData();
		return v;
	}

	public void setData(){
		StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
		builder.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_HEART_RATE_REQUEST);

		try {
			byte[] encoded = new byte[builder.build().toByteArray().length+5];
			byte[] builderTobyte = builder.build().toByteArray();

			encoded[0] = 'S';
			encoded[1] = 'E';
			int i;
			for( i = 0; i < builderTobyte.length; i++) {
				encoded[i+2] = builderTobyte[i];
			}
			encoded[i+2] = 'E';
			encoded[i+3] = '\r';
			encoded[i+4] = '\n';
			MainActivity.mService.sendMessageToRemote(encoded);
		}catch (UninitializedMessageException e){
			e.printStackTrace();
		}
	}

	public void setHeartRate(int value) {
		heartRateText.setText(value + "bpm");
	}

}
package com.standingegg.band.setting;

import java.util.ArrayList;

import com.standingegg.band.R;
import com.standingegg.band.contents.OnOffButton;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter implements Filterable {

	private Context mContext;
	private int mViewIndex;
	public int mIndex;
	private ArrayList<String> mData;
	private ArrayList<String> mDay;
	private ArrayList<String> mOnOff;

	// mData, mDay, mOnOff 값 preferences 에 저장 되어 있는 값 get!

	public AlarmAdapter(Context context,int layoutIndex) {
		super();
		mContext = context;
		mViewIndex = layoutIndex;
		mData = new ArrayList<String>();
		mDay = new ArrayList<String>();
		mOnOff = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(mContext);
			v = vi.inflate(R.layout.alarm_draw, null);
		}

		String value = (String) this.getItem(position);
		if (value != null) {
			TextView tt = (TextView) v.findViewById(R.id.alarm_time);
			tt.setText(mData.get(position));
			TextView day = (TextView) v.findViewById(R.id.alarm_day);
			day.setText(mDay.get(position));
			final OnOffButton sw = (OnOffButton) v.findViewById(R.id.alarm_onoff);
			if("On".equals(mOnOff.get(position))){
				sw.setChecked(true);
			}else {
				sw.setChecked(false);	
			}
			
			sw.setOnCheckChangedListner(((AlarmAcitivity) mContext));
//			sw.setOnCheckChangedListner(new OnOffButton.OnCheckChangedListner() {
//				@Override
//				public void onCheckChanged(View v, boolean isChecked) {
//					if(isChecked){
//						Log.d("AlarmAdapter", "test1 : " + isChecked);
//						sw.setBackgroundResource(R.drawable.alarm_background_on_state);
//					}else{
//						Log.d("AlarmAdapter", "test2 : " + isChecked);
//						sw.setBackgroundResource(R.drawable.alarm_background_off_state);
//					}
//				}
//			});
			
			v.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			Button bu = (Button) v.findViewById(R.id.alarm_remove_button);
			bu.setTag(R.string.msg_tag_contents_seq, position);
			bu.setOnClickListener(((AlarmAcitivity) mContext));

			sw.setOnCheckChangedListner(((AlarmAcitivity) mContext));
		}
		return v;
	}

	// 외부에서 아이템 추가 요청 시 사용
	public void add(String _msg, String day,String flag) {
		mData.add(_msg);
		mDay.add(day);
		if("Off".equals(flag)){
			mOnOff.add("Off");  //임의로 추가한 애는 off 로 추가 되어야 함! <사용자가 알람 설정 기록이 없으면, 수정 기록이 없으면!>
		}else{
			mOnOff.add("On"); 
		}
		
	}

	// 외부에서 아이템 삭제 요청 시 사용
	public void remove(int _position) {
		mData.remove(_position);
		mDay.remove(_position);
	}

	@Override
	public Filter getFilter() {
		return null;
	}
}
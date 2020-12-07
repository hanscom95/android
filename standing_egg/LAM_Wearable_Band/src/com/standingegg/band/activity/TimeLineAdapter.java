package com.standingegg.band.activity;

import java.util.ArrayList;

import com.standingegg.band.R;
import com.standingegg.band.util.ULog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class TimeLineAdapter extends BaseAdapter implements Filterable {

	private Context mContext;
	private int mViewIndex;
	private ArrayList<String> mTimeLine1;
	private ArrayList<String> mTimeLine2;

	public TimeLineAdapter(Context context,int layoutIndex) {
		super();
		mContext = context;
		mViewIndex = layoutIndex;
		mTimeLine1 = new ArrayList<String>();
		mTimeLine2 = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		return mTimeLine1.size();
	}

	@Override
	public Object getItem(int position) {
		return mTimeLine1.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(mContext);
			v = vi.inflate(R.layout.day_flow_draw, null);
			
			
			
			
		}

		String value = (String) this.getItem(position);
		if (value != null) {
			TextView tt = (TextView) v.findViewById(R.id.time_line);
			tt.setText(mTimeLine1.get(position));
			TextView day = (TextView) v.findViewById(R.id.time_line2);
			day.setText(mTimeLine2.get(position));
		}
		return v;
	}
	
	
	

	// 외부에서 아이템 추가 요청 시 사용
	public void add(String _msg, String _msg2) {
		mTimeLine1.add(0, _msg);
		mTimeLine2.add(0, _msg2);
	}

	// 외부에서 아이템 삭제 요청 시 사용
	public void remove(int _position) {
		mTimeLine1.remove(_position);
		mTimeLine2.remove(_position);
	}
	public void removeAll() {
		mTimeLine1.clear();
		mTimeLine2.clear();
	}
	
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		return null;
	}
}
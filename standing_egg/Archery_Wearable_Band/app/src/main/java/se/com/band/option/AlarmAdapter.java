package se.com.band.option;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import se.com.band.R;
import se.com.band.utility.ULog;

/**
 * Created by moon on 2017-01-17.
 */

public class AlarmAdapter extends BaseAdapter {

    private ArrayList<String> alarmList;
    private ArrayList<Boolean> aSwitchList;
    private LayoutInflater mInflator;
    private Activity mActivity;
    private ArrayList<Integer> checkList;

    public AlarmAdapter(Context mContext) {
        super();
        ULog.d("AlarmAdapter", "AlarmAdapter");
        mActivity = (Activity) mContext;
        alarmList = new ArrayList<String>();
        aSwitchList = new ArrayList<Boolean>();
        mInflator = mActivity.getLayoutInflater();
        checkList = new ArrayList<>();
    }

    public void addList(ArrayList<String> timeList, ArrayList<Boolean> switchList) {
        ULog.d("AlarmAdapter", "addList");
        if(!alarmList.contains(timeList)) {
            alarmList.addAll(timeList);
            aSwitchList.addAll(switchList);
            checkList.add(0);
        }
    }

    public void clear() {
        alarmList.clear();
        aSwitchList.clear();
    }

    static class ViewHolder {
        protected TextView time;
        protected Switch aSwitch;
    }


    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int i) {
        return alarmList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ULog.d("AlarmAdapter", "getView :" + i);
        final ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.alarm_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) view.findViewById(R.id.alarm_text);
            viewHolder.aSwitch = (Switch) view.findViewById(R.id.alarm_switch);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        ULog.d("AlarmAdapter", "alarmList :" + alarmList.size());
        ULog.d("AlarmAdapter", "switchList :" + aSwitchList.size());
        final String time = alarmList.get(i);
        if (time != null && time.length() > 0)
            viewHolder.time.setText(time);
        else
            viewHolder.time.setText(R.string.unknown_time);

        viewHolder.aSwitch.setChecked(aSwitchList.get(i));


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ULog.d("AlarmAdapter", "view click checkList :" + i + " // checked : " + viewHolder.checkBox.isChecked());
//                if(viewHolder.checkBox.isChecked()) {
//                    viewHolder.checkBox.setChecked(false);
//                    checkList.set(i, 0);
//                }else {
//                    viewHolder.checkBox.setChecked(true);
//                    checkList.set(i, 1);
//                }
//            }
//        });
//
//        if(viewHolder.checkBox != null) {
//            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ULog.d("AlarmAdapter", "viewHolder click checkList :" + i + " // checked : " + viewHolder.checkBox.isChecked());
//                    if(viewHolder.checkBox.isChecked()) {
//                        checkList.set(i, 1);
//                    }else {
//                        checkList.set(i, 0);
//                    }
//                }
//            });
//        }


        return view;
    }
}

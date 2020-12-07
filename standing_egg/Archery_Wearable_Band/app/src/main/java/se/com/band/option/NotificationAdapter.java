package se.com.band.option;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import se.com.band.R;
import se.com.band.utility.ULog;

/**
 * Created by moon on 2017-01-17.
 */

public class NotificationAdapter extends BaseAdapter {

    private ArrayList<ApplicationInfo> appList;
    private LayoutInflater mInflator;
    private Activity mActivity;
    private PackageManager mPackageManager;
    private ArrayList<Integer> checkList;

    public NotificationAdapter(Context mContext) {
        super();
        ULog.d("NotificationAdapter", "NotificationAdapter");
        mActivity = (Activity) mContext;
        appList = new ArrayList<ApplicationInfo>();
        mInflator = mActivity.getLayoutInflater();
        mPackageManager = mActivity.getPackageManager();
        checkList = new ArrayList<>();
    }

    public void addList(ApplicationInfo array) {
        ULog.d("NotificationAdapter", "addList");
        if(!appList.contains(array)) {
            appList.add(array);
            checkList.add(0);
        }
    }

    public void clear() {
        appList.clear();
    }

    static class ViewHolder {
        protected TextView name;
        protected ImageView img;
        protected CheckBox checkBox;
    }


    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int i) {
        return appList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ULog.d("NotificationAdapter", "getView :" + i);
        final ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.notification_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.img= (ImageView) view.findViewById(R.id.appImage);
            viewHolder.name = (TextView) view.findViewById(R.id.appText);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.appCheckbox);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            viewHolder.img.setImageDrawable(mPackageManager.getApplicationIcon(appList.get(i).packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final String appName = appList.get(i).loadLabel(mPackageManager).toString();
        if (appName != null && appName.length() > 0)
            viewHolder.name.setText(appName);
        else
            viewHolder.name.setText(R.string.unknown_device);

        if(checkList.get(i) == 1) {
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ULog.d("NotificationAdapter", "view click checkList :" + i + " // checked : " + viewHolder.checkBox.isChecked());
                if(viewHolder.checkBox.isChecked()) {
                    viewHolder.checkBox.setChecked(false);
                    checkList.set(i, 0);
                }else {
                    viewHolder.checkBox.setChecked(true);
                    checkList.set(i, 1);
                }
            }
        });

        if(viewHolder.checkBox != null) {
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ULog.d("NotificationAdapter", "viewHolder click checkList :" + i + " // checked : " + viewHolder.checkBox.isChecked());
                    if(viewHolder.checkBox.isChecked()) {
                        checkList.set(i, 1);
                    }else {
                        checkList.set(i, 0);
                    }
                }
            });
        }


        return view;
    }
}

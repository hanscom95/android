package artech.com.manager.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import artech.com.manager.R;

/**
 * Created by moon on 2017-01-17.
 */

public class AdminListAdapter extends BaseAdapter {

//    public ArrayList<String> idList;
//    public ArrayList<String> nameList;
//    public ArrayList<String> infoList;
    public ArrayList<AdminListActivity.Admin> adminList;
    private LayoutInflater mInflator;
    private Activity mActivity;

    int selected_position = -1;

    public AdminListAdapter(Context mContext) {
        super();
        mActivity = (Activity) mContext;
//        idList = new ArrayList<String>();
//        nameList = new ArrayList<String>();
//        infoList = new ArrayList<String>();
        adminList = new ArrayList<>();
        mInflator = mActivity.getLayoutInflater();
    }

    public void clear() {
//        idList.clear();
//        nameList.clear();
//        infoList.clear();
        adminList.clear();
    }

//    public void addList(ArrayList<String> idArrayList, ArrayList<String> nameArrayList, ArrayList<String> infoArrayList) {
//        if(!idList.contains(idArrayList)) {
//            idList.addAll(idArrayList);
//            nameList.addAll(nameArrayList);
//            infoList.addAll(infoArrayList);
//        }
//    }

    public int getCheckedRow() {
        return selected_position;
    }

    public void setinitCheckedRow() {
        selected_position = -1;
    }

    public void addList(ArrayList<AdminListActivity.Admin> arrayList) {
        if(!adminList.contains(arrayList)) {
            adminList.addAll(arrayList);
        }
    }

    static class ViewHolder {
        protected CheckBox checkBox;
        protected TextView number;
        protected TextView info;
        protected TextView name;
    }

    @Override
    public int getCount() {
        return adminList.size();
    }

    @Override
    public Object getItem(int i) {
        return adminList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.admin_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            viewHolder.checkBox.setTag(i);
            viewHolder.number = (TextView) view.findViewById(R.id.number_text);
            viewHolder.info = (TextView) view.findViewById(R.id.info_text);
            viewHolder.name = (TextView) view.findViewById(R.id.name_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        Log.d("BookingList", "i : " + i + "persons  convertStringToList : " + convertToArray(personsList.get(i)).get(0) + "/persons  convertStringToList : " + convertToArray(personsList.get(i)).get(1) + " / persons : " + personsList.get(i));

        viewHolder.number.setText(adminList.get(i).number);
        viewHolder.name.setText(adminList.get(i).name);
        viewHolder.info.setText(adminList.get(i).info);


        viewHolder.checkBox.setChecked(i==selected_position);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(viewHolder.checkBox.isChecked()) {
                    selected_position =  i;
                } else{
                    selected_position = -1;
                }
                notifyDataSetChanged();
            }
        });


//        if(viewHolder.checkBox != null) {
//            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    CheckBox c = (CheckBox) v;
//
//                    int row_id = (Integer) v.getTag();
//
//                    selected_position = row_id;
//
//
//                }
//            });
//        }



        return view;
    }

    private ArrayList<String> convertToArray(String string) {
        try {
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(";")));
            return list;
        } catch (Exception e) {
            return null;
        }
    }

}

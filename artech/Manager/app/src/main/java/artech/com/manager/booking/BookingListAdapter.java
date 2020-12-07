package artech.com.manager.booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import artech.com.manager.R;
import artech.com.manager.group.GroupListActivity;

/**
 * Created by moon on 2017-01-17.
 */

public class BookingListAdapter extends BaseAdapter {

    public ArrayList<String> idList;
    public ArrayList<String> dateList;
    public ArrayList<String> emailList;
    public ArrayList<String> teamList;
    public ArrayList<String> nameList;
    public ArrayList<String> phoneList;
    public ArrayList<String> numberList;
    public ArrayList<String> teamNumberList;
    public ArrayList<String> programList;
    public ArrayList<String> etcList;
    public ArrayList<String> personsList;
    private LayoutInflater mInflator;
    private Activity mActivity;

    int selected_position = -1;

    public BookingListAdapter(Context mContext) {
        super();
        mActivity = (Activity) mContext;
        idList = new ArrayList<String>();
        dateList = new ArrayList<String>();
        emailList = new ArrayList<String>();
        teamList = new ArrayList<String>();
        nameList = new ArrayList<String>();
        phoneList = new ArrayList<String>();
        numberList = new ArrayList<String>();
        teamNumberList = new ArrayList<String>();
        programList = new ArrayList<String>();
        etcList = new ArrayList<String>();
        personsList = new ArrayList<String>();
        mInflator = mActivity.getLayoutInflater();
    }

    public void clear() {
        idList.clear();
        dateList.clear();
        emailList.clear();
        teamList.clear();
        nameList.clear();
        phoneList.clear();
        numberList.clear();
        teamNumberList.clear();
        programList.clear();
        etcList.clear();
        personsList.clear();
    }

    public void addList(ArrayList<String> idArrayList, ArrayList<String> dateArrayList, ArrayList<String> emailArrayList, ArrayList<String> teamArrayList, ArrayList<String> nameArrayList,
                        ArrayList<String> phoneArrayList, ArrayList<String> numberArrayList, ArrayList<String> teamNumberArrayList, ArrayList<String> programArrayList, ArrayList<String> etcArrayList, ArrayList<String> personsArrayList) {
        if(!idList.contains(idArrayList)) {
            idList.addAll(idArrayList);
            dateList.addAll(dateArrayList);
            emailList.addAll(emailArrayList);
            teamList.addAll(teamArrayList);
            nameList.addAll(nameArrayList);
            phoneList.addAll(phoneArrayList);
            numberList.addAll(numberArrayList);
            teamNumberList.addAll(teamNumberArrayList);
            programList.addAll(programArrayList);
            etcList.addAll(etcArrayList);
            personsList.addAll(personsArrayList);
        }
    }

    public int getCheckedRow() {
        return selected_position;
    }

    public void setinitCheckedRow() {
        selected_position = -1;
    }

    static class ViewHolder {
        protected CheckBox checkBox;
        protected TextView date;
        protected RelativeLayout teamLayout;
        protected TextView team;
        protected TextView name;
        protected TextView phone;
        protected TextView number;
        protected TextView program;
        protected TextView etc;
    }

    @Override
    public int getCount() {
        return idList.size();
    }

    @Override
    public Object getItem(int i) {
        return idList.get(i);
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
            view = mInflator.inflate(R.layout.booking_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            viewHolder.checkBox.setTag(i);
            viewHolder.date = (TextView) view.findViewById(R.id.date_text);
            viewHolder.team = (TextView) view.findViewById(R.id.team_text);
            viewHolder.name = (TextView) view.findViewById(R.id.bookings_name_text);
            viewHolder.phone = (TextView) view.findViewById(R.id.bookings_phone_text);
            viewHolder.number = (TextView) view.findViewById(R.id.bookings_personnel_text);
            viewHolder.program = (TextView) view.findViewById(R.id.pragram_name_text);
            viewHolder.etc = (TextView) view.findViewById(R.id.etc_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        Log.d("BookingList", "i : " + i + "persons  convertStringToList : " + convertToArray(personsList.get(i)).get(0) + "/persons  convertStringToList : " + convertToArray(personsList.get(i)).get(1) + " / persons : " + personsList.get(i));

        viewHolder.date.setText(dateList.get(i));
        viewHolder.team.setText(teamList.get(i));
        viewHolder.name.setText(nameList.get(i));
        viewHolder.phone.setText(phoneList.get(i));
        viewHolder.number.setText(numberList.get(i));
        viewHolder.program.setText(programList.get(i));
        viewHolder.etc.setText(etcList.get(i));


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



        viewHolder.team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GroupListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", viewHolder.team.getText().toString());
                if(!numberList.get(i).equals("")) {
                    bundle.putInt("people", Integer.parseInt(numberList.get(i)) > 0 ? Integer.parseInt(numberList.get(i)) : 1);
                }else {
                    bundle.putInt("people", 1);
                }

                if(!teamNumberList.get(i).equals("")) {
                    bundle.putInt("team", Integer.parseInt(teamNumberList.get(i)) > 0 ? Integer.parseInt(teamNumberList.get(i)) : 1);
                }else {
                    bundle.putInt("team", 1);
                }

                bundle.putString("email", emailList.get(i));
                bundle.putStringArrayList("nameArray", convertToArray(personsList.get(i)));
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
                mActivity.finish();
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

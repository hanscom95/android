package artech.com.semi.utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import artech.com.semi.R;

/**
 * Created by moon on 2018-07-09.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<String> mTitle;
    ArrayList<ArrayList<String>> mSub;

    public ExpandableAdapter(Context context, ArrayList<String> expandableListTitle,
                             ArrayList<ArrayList<String>> expandableListDetail) {
        mContext = context;
        mTitle = expandableListTitle;
        mSub = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mSub.get(listPosition).get(expandedListPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSub.get(groupPosition).size();
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_expandable_list_item, null);
        }
        TextView expandedListTextView = convertView
                .findViewById(R.id.text1);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTitle.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mTitle.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(final int listPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_expandable_group_list, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.text1);
        listTitleTextView.setText(listTitle);
        listTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandableListView mExpandableListView = (ExpandableListView) parent;
                if(mExpandableListView.isGroupExpanded(listPosition)) {
                    mExpandableListView.setY(150);
                    mExpandableListView.collapseGroup(listPosition);
                }else {
                    mExpandableListView.setY(40);
                    mExpandableListView.expandGroup(listPosition);
                }

            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return false;
    }

}
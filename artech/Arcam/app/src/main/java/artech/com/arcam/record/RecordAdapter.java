package artech.com.arcam.record;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import artech.com.arcam.R;


/**
 * Created by moon on 2017-01-17.
 */

public class RecordAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflator;
    private Activity mActivity;

    public ArrayList<String> dateList;
    private ArrayList<String> colList;
    private ArrayList<Integer> scoreList;
    private ArrayList<Integer> braceList;
    private ArrayList<Integer> tillerList;
    private ArrayList<Integer> nockingPointList;
    private ArrayList<Integer> weatherList;
    private ArrayList<Integer> windList;
    private ArrayList<Integer> distanceList;
    private ArrayList<String> commentList;

    int selected_position = -1;

    public RecordAdapter(Context mContext) {
        super();
        mActivity = (Activity) mContext;
        dateList = new ArrayList<>();
        colList = new ArrayList<>();
        scoreList = new ArrayList<>();
        braceList = new ArrayList<>();
        tillerList = new ArrayList<>();
        nockingPointList = new ArrayList<>();
        weatherList = new ArrayList<>();
        windList = new ArrayList<>();
        distanceList = new ArrayList<>();
        commentList = new ArrayList<>();
        mInflator = mActivity.getLayoutInflater();
    }

    public void clear() {
        selected_position = -1;

        dateList.clear();
        colList.clear();
        scoreList.clear();
        braceList.clear();
        tillerList.clear();
        nockingPointList.clear();
        weatherList.clear();
        windList.clear();
        distanceList.clear();
        commentList.clear();
    }

    public void addList(ArrayList<String> colArrayList, ArrayList<String> dateArrayList, ArrayList<Integer> scoreArrayList, ArrayList<Integer> braceArrayList
            , ArrayList<Integer> tillerArrayList, ArrayList<Integer> nockingPointArrayList, ArrayList<Integer> weatherArrayList
            , ArrayList<Integer> windArrayList, ArrayList<Integer> distanceArrayList, ArrayList<String> commentListArrayList) {
        if(!dateList.contains(dateArrayList)) {
            dateList.addAll(dateArrayList);
            colList.addAll(colArrayList);
            scoreList.addAll(scoreArrayList);
            braceList.addAll(braceArrayList);
            tillerList.addAll(tillerArrayList);
            nockingPointList.addAll(nockingPointArrayList);
            weatherList.addAll(weatherArrayList);
            windList.addAll(windArrayList);
            distanceList.addAll(distanceArrayList);
            commentList.addAll(commentListArrayList);
        }
    }

    static class ViewHolder {
        protected CheckBox checkBox;
        protected TextView date;
        protected TextView score;
        protected TextView brace;
        protected TextView tiller;
        protected TextView nockingPoint;
        protected ImageView weather;
        protected ImageView wind;
        protected TextView distance;
        protected TextView comment;
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int i) {
        return dateList.get(i);
    }

    public Object getItemArray(int i) {
        return dateList;
    }

    public String getColItem() {
        if(selected_position >= 0) {
            return colList.get(selected_position);
        }else {
            return "";
        }
    }

    public String getItem() {
        if(selected_position >= 0) {
            return colList.get(selected_position);
        }else {
            return "";
        }
    }

    public int getCheckedRow() {
        return selected_position;
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
            view = mInflator.inflate(R.layout.content_record_list, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            viewHolder.date = (TextView) view.findViewById(R.id.date_text);
            viewHolder.score = (TextView) view.findViewById(R.id.score_text);
            viewHolder.brace = (TextView) view.findViewById(R.id.brace_high_value_text);
            viewHolder.tiller= (TextView) view.findViewById(R.id.tiller_high_value_text);
            viewHolder.nockingPoint= (TextView) view.findViewById(R.id.nocking_point_value_text);
            viewHolder.weather = (ImageView) view.findViewById(R.id.weather_img);
            viewHolder.wind = (ImageView) view.findViewById(R.id.wind_img);
            viewHolder.distance = (TextView) view.findViewById(R.id.distance_value_text);
            viewHolder.comment = (TextView) view.findViewById(R.id.comment_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

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

        viewHolder.date.setText(dateList.get(i));
        viewHolder.score.setText(scoreList.get(i)+ "");
        viewHolder.brace.setText(braceList.get(i)+ "");
        viewHolder.tiller.setText(tillerList.get(i) + "");
        viewHolder.nockingPoint.setText(nockingPointList.get(i)+ "");
        viewHolder.comment.setText(commentList.get(i));


        if(weatherList.get(i) == 0) {
            viewHolder.weather.setImageResource(R.mipmap.ic_sunny_press);
        }else if(weatherList.get(i) == 1) {
            viewHolder.weather.setImageResource(R.mipmap.ic_cloudy_press);
        }else if(weatherList.get(i) == 2) {
            viewHolder.weather.setImageResource(R.mipmap.ic_fog_press);
        }else if(weatherList.get(i) == 3) {
            viewHolder.weather.setImageResource(R.mipmap.ic_typhoon_press);
        }else if(weatherList.get(i) == 4) {
            viewHolder.weather.setImageResource(R.mipmap.ic_rain_press);
        }else if(weatherList.get(i) == 5) {
            viewHolder.weather.setImageResource(R.mipmap.ic_snow_press);
        }

        if(windList.get(i) == 0) {
            viewHolder.wind.setImageResource(R.mipmap.ic_wind_zero_press);
        }else if(windList.get(i) == 1) {
            viewHolder.wind.setImageResource(R.mipmap.ic_wind_one_press);
        }else if(windList.get(i) == 2) {
            viewHolder.wind.setImageResource(R.mipmap.ic_wind_two_press);
        }else if(windList.get(i) == 3) {
            viewHolder.wind.setImageResource(R.mipmap.ic_wind_three_press);
        }else if(windList.get(i) == 4) {
            viewHolder.wind.setImageResource(R.mipmap.ic_wind_four_press);
        }

        if(distanceList.get(i) == 0) {
            viewHolder.distance.setText("18m");
        }else if(distanceList.get(i) == 1) {
            viewHolder.distance.setText("30m");
        }else if(distanceList.get(i) == 2) {
            viewHolder.distance.setText("50m");
        }else if(distanceList.get(i) == 3) {
            viewHolder.distance.setText("70m");
        }

        return view;
    }
}

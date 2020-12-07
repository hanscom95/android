package artech.com.manager.scale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import artech.com.manager.R;
import artech.com.manager.group.GroupListActivity;

/**
 * Created by moon on 2017-01-17.
 */

public class ScaleListAdapter extends BaseAdapter {

    private Context mContext;
    public ArrayList<String> nameList;
    private ArrayList<Integer> score1List;
    private ArrayList<Integer> score2List;
    private ArrayList<Integer> score3List;
    private ArrayList<Integer> score4List;
    private ArrayList<Integer> score5List;
    private ArrayList<Integer> score6List;
    private ArrayList<Integer> score7List;
    private ArrayList<Integer> score8List;
    private ArrayList<Integer> score9List;
    private ArrayList<Integer> score10List;
    public ArrayList<Ranking> rankingArrayList;

    private int teamPeople = 0, teamPeopleSurplus = 0;
    private int mPeople;
    private int mTeam;
    private int teamNumber = 0;
    private LayoutInflater mInflator;
    private Activity mActivity;

    public ScaleListAdapter(Context mContext) {
        super();
        mActivity = (Activity) mContext;
        nameList = new ArrayList<>();
        score1List = new ArrayList<>();
        score2List = new ArrayList<>();
        score3List = new ArrayList<>();
        score4List = new ArrayList<>();
        score5List = new ArrayList<>();
        score6List = new ArrayList<>();
        score7List = new ArrayList<>();
        score8List = new ArrayList<>();
        score9List = new ArrayList<>();
        score10List = new ArrayList<>();
        rankingArrayList = new ArrayList<>();
        mInflator = mActivity.getLayoutInflater();
    }

    public void clear() {
//        nameList.clear();
//        score1List.clear();
//        score2List.clear();
//        score3List.clear();
//        score4List.clear();
//        score5List.clear();
//        score6List.clear();
//        score7List.clear();
//        score8List.clear();
//        score9List.clear();
//        score10List.clear();
        rankingArrayList.clear();
    }

    public void addList(ArrayList<String> nameArrayList, ArrayList<Integer> score1ArrayList, ArrayList<Integer> score2ArrayList
            , ArrayList<Integer> score3ArrayList, ArrayList<Integer> score4ArrayList, ArrayList<Integer> score5ArrayList
            , ArrayList<Integer> score6ArrayList, ArrayList<Integer> score7ArrayList, ArrayList<Integer> score8ArrayList
            , ArrayList<Integer> score9ArrayList, ArrayList<Integer> score10ArrayList, int people, int team) {
        if(!nameList.contains(nameArrayList)) {
            mPeople = people;
            mTeam = team;
            nameList.addAll(nameArrayList);
            score1List.addAll(score1ArrayList);
            score2List.addAll(score2ArrayList);
            score3List.addAll(score3ArrayList);
            score4List.addAll(score4ArrayList);
            score5List.addAll(score5ArrayList);
            score6List.addAll(score6ArrayList);
            score7List.addAll(score7ArrayList);
            score8List.addAll(score8ArrayList);
            score9List.addAll(score9ArrayList);
            score10List.addAll(score10ArrayList);


            for(int i = 0; i < nameList.size(); i++) {
                Ranking rankingArray = new Ranking();
                rankingArray.name = nameList.get(i);
                rankingArray.score[0] = score1List.get(i);
                rankingArray.score[1] = score2List.get(i);
                rankingArray.score[2] = score3List.get(i);
                rankingArray.score[3] = score4List.get(i);
                rankingArray.score[4] = score5List.get(i);
                rankingArray.score[5] = score6List.get(i);
                rankingArray.score[6] = score7List.get(i);
                rankingArray.score[7] = score8List.get(i);
                rankingArray.score[8] = score9List.get(i);
                rankingArray.score[9] = score10List.get(i);
                rankingArray.total = (score1List.get(i)>10?10:score1List.get(i))+
                        (score2List.get(i)>10?10:score2List.get(i))+
                        (score3List.get(i)>10?10:score3List.get(i))+
                        (score4List.get(i)>10?10:score4List.get(i))+
                        (score5List.get(i)>10?10:score5List.get(i))+
                        (score6List.get(i)>10?10:score6List.get(i))+
                        (score7List.get(i)>10?10:score7List.get(i))+
                        (score8List.get(i)>10?10:score8List.get(i))+
                        (score9List.get(i)>10?10:score9List.get(i))+
                        (score10List.get(i)>10?10:score10List.get(i));
                rankingArray.sort = (score1List.get(i)+
                        score2List.get(i)+
                        score3List.get(i)+
                        score4List.get(i)+
                        score5List.get(i)+
                        score6List.get(i)+
                        score7List.get(i)+
                        score8List.get(i)+
                        score9List.get(i)+
                        score10List.get(i));
                rankingArrayList.add(rankingArray);
                Log.d("GroupListAdapter", "i nameList : " + i+ " / name : " + rankingArrayList.get(i).name+ " / score1 : " + rankingArrayList.get(i).score[0]+ " / score2 : " + rankingArrayList.get(i).score[1]);
            }
            Collections.sort(rankingArrayList);

            if(mPeople%mTeam == 0) {
                teamPeople = mPeople/mTeam;
            }else {
                teamPeople = (mPeople)/mTeam;
                teamPeopleSurplus = mPeople%mTeam;
            }

            for(int i = 0; i < nameList.size(); i++) {
                if (i % teamPeople == 0 && teamNumber < mTeam) {
                    teamNumber++;
//                    Log.d("GroupListAdapter", "i%teamPeople == 0 : " + teamNumber);
                }
            }
        }
    }



    /*public void addList(ArrayList<Ranking> arrayList) {
        for(int i = 0; i < arrayList.size(); i++) {
            Log.d("ScaleListAdapter" , " i = "+ i + "/ name : " + arrayList.get(i).name + "/ score size : " + arrayList.get(i).score.length + "/ score 1 : " + arrayList.get(i).score[0] + "/ score 2 : " + arrayList.get(i).score[1]);
        }

        if(!rankingArrayList.contains(arrayList)) {
                rankingArrayList.addAll(arrayList);
        }

        Log.d("ScaleListAdapter", "size : " + rankingArrayList.size());
    }*/

    static class ViewHolder {
        protected TextView name;
        protected TextView ranking;
        protected TextView score_text_1;
        protected TextView score_text_2;
        protected TextView score_text_3;
        protected TextView score_text_4;
        protected TextView score_text_5;
        protected TextView score_text_6;
        protected TextView score_text_7;
        protected TextView score_text_8;
        protected TextView score_text_9;
        protected TextView score_text_10;
        protected TextView total;
    }

    @Override
    public int getCount() {
        return rankingArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return rankingArrayList.get(i).name;
    }

    public Object getItemArray(int i) {
        return rankingArrayList;
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
            view = mInflator.inflate(R.layout.scale_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name_text);
            viewHolder.total = (TextView) view.findViewById(R.id.total_text);
            viewHolder.ranking = (TextView) view.findViewById(R.id.ranking_text);
            viewHolder.score_text_1 = (TextView) view.findViewById(R.id.circle_1);
            viewHolder.score_text_2 = (TextView) view.findViewById(R.id.circle_2);
            viewHolder.score_text_3 = (TextView) view.findViewById(R.id.circle_3);
            viewHolder.score_text_4 = (TextView) view.findViewById(R.id.circle_4);
            viewHolder.score_text_5 = (TextView) view.findViewById(R.id.circle_5);
            viewHolder.score_text_6 = (TextView) view.findViewById(R.id.circle_6);
            viewHolder.score_text_7 = (TextView) view.findViewById(R.id.circle_7);
            viewHolder.score_text_8 = (TextView) view.findViewById(R.id.circle_8);
            viewHolder.score_text_9 = (TextView) view.findViewById(R.id.circle_9);
            viewHolder.score_text_10 = (TextView) view.findViewById(R.id.circle_10);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


//        viewHolder.name.setText(nameList.get(i));
//        viewHolder.score_text_1.setText((score1List.get(i)>10?"X":score1List.get(i))+"");
//        viewHolder.score_text_2.setText((score2List.get(i)>10?"X":score2List.get(i))+"");
//        viewHolder.score_text_3.setText((score3List.get(i)>10?"X":score3List.get(i))+"");
//        viewHolder.score_text_4.setText((score4List.get(i)>10?"X":score4List.get(i))+"");
//        viewHolder.score_text_5.setText((score5List.get(i)>10?"X":score5List.get(i))+"");
//        viewHolder.score_text_6.setText((score6List.get(i)>10?"X":score6List.get(i))+"");
//        viewHolder.score_text_7.setText((score7List.get(i)>10?"X":score7List.get(i))+"");
//        viewHolder.score_text_8.setText((score8List.get(i)>10?"X":score8List.get(i))+"");
//        viewHolder.score_text_9.setText((score9List.get(i)>10?"X":score9List.get(i))+"");
//        viewHolder.score_text_10.setText((score10List.get(i)>10?"X":score10List.get(i))+"");
//
//        viewHolder.total.setText((
//                (score1List.get(i)>10?10:score1List.get(i))+
//                (score2List.get(i)>10?10:score2List.get(i))+
//                (score3List.get(i)>10?10:score3List.get(i))+
//                (score4List.get(i)>10?10:score4List.get(i))+
//                (score5List.get(i)>10?10:score5List.get(i))+
//                (score6List.get(i)>10?10:score6List.get(i))+
//                (score7List.get(i)>10?10:score7List.get(i))+
//                (score8List.get(i)>10?10:score8List.get(i))+
//                (score9List.get(i)>10?10:score9List.get(i))+
//                (score10List.get(i)>10?10:score10List.get(i)))+"");

        //Log.d("ScaleListAdapter", "i : " + i + " / name : " + rankingArrayList.get(i).name + " / score1 : " + rankingArrayList.get(i).score[0]);

//        Log.d("GroupListAdapter", "i nameList : " + i+ " / name : " + rankingArrayList.get(i).name+ " / score1 : " + rankingArrayList.get(i).score[0]+ " / score2 : " + rankingArrayList.get(i).score[1]);
        viewHolder.ranking.setText((i+1)+"");
        viewHolder.name.setText(rankingArrayList.get(i).name);
        viewHolder.score_text_1.setText((rankingArrayList.get(i).score[0]>10?"X":rankingArrayList.get(i).score[0])+"");
        viewHolder.score_text_2.setText((rankingArrayList.get(i).score[1]>10?"X":rankingArrayList.get(i).score[1])+"");
        viewHolder.score_text_3.setText((rankingArrayList.get(i).score[2]>10?"X":rankingArrayList.get(i).score[2])+"");
        viewHolder.score_text_4.setText((rankingArrayList.get(i).score[3]>10?"X":rankingArrayList.get(i).score[3])+"");
        viewHolder.score_text_5.setText((rankingArrayList.get(i).score[4]>10?"X":rankingArrayList.get(i).score[4])+"");
        viewHolder.score_text_6.setText((rankingArrayList.get(i).score[5]>10?"X":rankingArrayList.get(i).score[5])+"");
        viewHolder.score_text_7.setText((rankingArrayList.get(i).score[6]>10?"X":rankingArrayList.get(i).score[6])+"");
        viewHolder.score_text_8.setText((rankingArrayList.get(i).score[7]>10?"X":rankingArrayList.get(i).score[7])+"");
        viewHolder.score_text_9.setText((rankingArrayList.get(i).score[8]>10?"X":rankingArrayList.get(i).score[8])+"");
        viewHolder.score_text_10.setText((rankingArrayList.get(i).score[9]>10?"X":rankingArrayList.get(i).score[9])+"");



        if(viewHolder.score_text_1.getText().equals("0") || viewHolder.score_text_1.getText().equals("1") || viewHolder.score_text_1.getText().equals("2")) {
            viewHolder.score_text_1.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_1.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_1.getText().equals("3") || viewHolder.score_text_1.getText().equals("4")) {
            viewHolder.score_text_1.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_1.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_1.getText().equals("5") || viewHolder.score_text_1.getText().equals("6")) {
            viewHolder.score_text_1.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_1.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_1.getText().equals("7") || viewHolder.score_text_1.getText().equals("8")) {
            viewHolder.score_text_1.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_1.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_1.getText().equals("9") || viewHolder.score_text_1.getText().equals("10") || viewHolder.score_text_1.getText().equals("X")) {
            viewHolder.score_text_1.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_1.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_2.getText().equals("0") || viewHolder.score_text_2.getText().equals("1") || viewHolder.score_text_2.getText().equals("2")) {
            viewHolder.score_text_2.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_2.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_2.getText().equals("3") || viewHolder.score_text_2.getText().equals("4")) {
            viewHolder.score_text_2.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_2.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_2.getText().equals("5") || viewHolder.score_text_2.getText().equals("6")) {
            viewHolder.score_text_2.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_2.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_2.getText().equals("7") || viewHolder.score_text_2.getText().equals("8")) {
            viewHolder.score_text_2.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_2.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_2.getText().equals("9") || viewHolder.score_text_2.getText().equals("10") || viewHolder.score_text_2.getText().equals("X")) {
            viewHolder.score_text_2.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_2.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_3.getText().equals("0") || viewHolder.score_text_3.getText().equals("1") || viewHolder.score_text_3.getText().equals("2")) {
            viewHolder.score_text_3.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_3.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_3.getText().equals("3") || viewHolder.score_text_3.getText().equals("4")) {
            viewHolder.score_text_3.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_3.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_3.getText().equals("5") || viewHolder.score_text_3.getText().equals("6")) {
            viewHolder.score_text_3.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_3.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_3.getText().equals("7") || viewHolder.score_text_3.getText().equals("8")) {
            viewHolder.score_text_3.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_3.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_3.getText().equals("9") || viewHolder.score_text_3.getText().equals("10") || viewHolder.score_text_3.getText().equals("X")) {
            viewHolder.score_text_3.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_3.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_4.getText().equals("0") || viewHolder.score_text_4.getText().equals("1") || viewHolder.score_text_4.getText().equals("2")) {
            viewHolder.score_text_4.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_4.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_4.getText().equals("3") || viewHolder.score_text_4.getText().equals("4")) {
            viewHolder.score_text_4.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_4.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_4.getText().equals("5") || viewHolder.score_text_4.getText().equals("6")) {
            viewHolder.score_text_4.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_4.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_4.getText().equals("7") || viewHolder.score_text_4.getText().equals("8")) {
            viewHolder.score_text_4.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_4.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_4.getText().equals("9") || viewHolder.score_text_4.getText().equals("10") || viewHolder.score_text_4.getText().equals("X")) {
            viewHolder.score_text_4.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_4.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_5.getText().equals("0") || viewHolder.score_text_5.getText().equals("1") || viewHolder.score_text_5.getText().equals("2")) {
            viewHolder.score_text_5.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_5.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_5.getText().equals("3") || viewHolder.score_text_5.getText().equals("4")) {
            viewHolder.score_text_5.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_5.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_5.getText().equals("5") || viewHolder.score_text_5.getText().equals("6")) {
            viewHolder.score_text_5.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_5.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_5.getText().equals("7") || viewHolder.score_text_5.getText().equals("8")) {
            viewHolder.score_text_5.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_5.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_5.getText().equals("9") || viewHolder.score_text_5.getText().equals("10") || viewHolder.score_text_5.getText().equals("X")) {
            viewHolder.score_text_5.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_5.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_6.getText().equals("0") || viewHolder.score_text_6.getText().equals("1") || viewHolder.score_text_6.getText().equals("2")) {
            viewHolder.score_text_6.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_6.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_6.getText().equals("3") || viewHolder.score_text_6.getText().equals("4")) {
            viewHolder.score_text_6.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_6.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_6.getText().equals("5") || viewHolder.score_text_6.getText().equals("6")) {
            viewHolder.score_text_6.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_6.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_6.getText().equals("7") || viewHolder.score_text_6.getText().equals("8")) {
            viewHolder.score_text_6.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_6.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_6.getText().equals("9") || viewHolder.score_text_6.getText().equals("10") || viewHolder.score_text_6.getText().equals("X")) {
            viewHolder.score_text_6.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_6.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_7.getText().equals("0") || viewHolder.score_text_7.getText().equals("1") || viewHolder.score_text_7.getText().equals("2")) {
            viewHolder.score_text_7.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_7.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_7.getText().equals("3") || viewHolder.score_text_7.getText().equals("4")) {
            viewHolder.score_text_7.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_7.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_7.getText().equals("5") || viewHolder.score_text_7.getText().equals("6")) {
            viewHolder.score_text_7.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_7.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_7.getText().equals("7") || viewHolder.score_text_7.getText().equals("8")) {
            viewHolder.score_text_7.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_7.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_7.getText().equals("9") || viewHolder.score_text_7.getText().equals("10") || viewHolder.score_text_7.getText().equals("X")) {
            viewHolder.score_text_7.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_7.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_8.getText().equals("0") || viewHolder.score_text_8.getText().equals("1") || viewHolder.score_text_8.getText().equals("2")) {
            viewHolder.score_text_8.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_8.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_8.getText().equals("3") || viewHolder.score_text_8.getText().equals("4")) {
            viewHolder.score_text_8.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_8.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_8.getText().equals("5") || viewHolder.score_text_8.getText().equals("6")) {
            viewHolder.score_text_8.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_8.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_8.getText().equals("7") || viewHolder.score_text_8.getText().equals("8")) {
            viewHolder.score_text_8.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_8.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_8.getText().equals("9") || viewHolder.score_text_8.getText().equals("10") || viewHolder.score_text_8.getText().equals("X")) {
            viewHolder.score_text_8.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_8.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_9.getText().equals("0") || viewHolder.score_text_9.getText().equals("1") || viewHolder.score_text_9.getText().equals("2")) {
            viewHolder.score_text_9.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_9.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_9.getText().equals("3") || viewHolder.score_text_9.getText().equals("4")) {
            viewHolder.score_text_9.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_9.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_9.getText().equals("5") || viewHolder.score_text_9.getText().equals("6")) {
            viewHolder.score_text_9.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_9.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_9.getText().equals("7") || viewHolder.score_text_9.getText().equals("8")) {
            viewHolder.score_text_9.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_9.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_9.getText().equals("9") || viewHolder.score_text_9.getText().equals("10") || viewHolder.score_text_9.getText().equals("X")) {
            viewHolder.score_text_9.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_9.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }



        if(viewHolder.score_text_10.getText().equals("0") || viewHolder.score_text_10.getText().equals("1") || viewHolder.score_text_10.getText().equals("2")) {
            viewHolder.score_text_10.setBackgroundResource(R.drawable.white_circle);
            viewHolder.score_text_10.setTextColor(mActivity.getResources().getColor(android.R.color.black));
        }else if(viewHolder.score_text_10.getText().equals("3") || viewHolder.score_text_10.getText().equals("4")) {
            viewHolder.score_text_10.setBackgroundResource(R.drawable.black_circle);
            viewHolder.score_text_10.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_10.getText().equals("5") || viewHolder.score_text_10.getText().equals("6")) {
            viewHolder.score_text_10.setBackgroundResource(R.drawable.blue_circle);
            viewHolder.score_text_10.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_10.getText().equals("7") || viewHolder.score_text_10.getText().equals("8")) {
            viewHolder.score_text_10.setBackgroundResource(R.drawable.red_circle);
            viewHolder.score_text_10.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }else if(viewHolder.score_text_10.getText().equals("9") || viewHolder.score_text_10.getText().equals("10") || viewHolder.score_text_10.getText().equals("X")) {
            viewHolder.score_text_10.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.score_text_10.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }

        viewHolder.total.setText(rankingArrayList.get(i).total+"");

        return view;
    }

    public class Ranking implements Comparable<Ranking>{
        int rank;
        String name;
        int[] score = new int[10];
        int total;
        int sort;


        /*@Override
        public int compareTo(@NonNull Ranking o) {
            if(sort < o.sort) {
                return 1;
            }else if(sort > o.sort){
                return -1;
            }
            return 0;
        }*/

        @Override
        public int compareTo(@NonNull Ranking o) {
            if(total < o.total) {
                return 1;
            }else if(total > o.total){
                return -1;
            }else {
                if(sort < o.sort) {
                    return 1;
                }else if(sort > o.sort){
                    return -1;
                }
            }
            return 0;
        }
    }
}

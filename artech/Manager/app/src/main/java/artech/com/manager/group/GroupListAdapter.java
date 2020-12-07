package artech.com.manager.group;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import artech.com.manager.R;

/**
 * Created by moon on 2017-01-17.
 */

public class GroupListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> peopleList;
    public ArrayList<String> nameList;
    public ArrayList<Integer> score1List;
    public ArrayList<Integer> score2List;
    public ArrayList<Integer> score3List;
    public ArrayList<Integer> score4List;
    public ArrayList<Integer> score5List;
    public ArrayList<Integer> score6List;
    public ArrayList<Integer> score7List;
    public ArrayList<Integer> score8List;
    public ArrayList<Integer> score9List;
    public ArrayList<Integer> score10List;
    private int teamPeople = 0, teamPeopleSurplus = 0;
    private int mPeople;
    private int mTeam;
    private int teamNumber = 0;
    private LayoutInflater mInflator;
    private Activity mActivity;

    public GroupListAdapter(Context mContext) {
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
        peopleList = new ArrayList<>();
        mInflator = mActivity.getLayoutInflater();
    }

    public void clear() {
        peopleList.clear();
        nameList.clear();
        score1List.clear();
        score2List.clear();
        score3List.clear();
        score4List.clear();
        score5List.clear();
        score6List.clear();
        score7List.clear();
        score8List.clear();
        score9List.clear();
        score10List.clear();
    }

    public void addList(ArrayList<String> nameArrayList, int people, int team) {
        if(!nameList.contains(nameArrayList)) {
            mPeople = people;
            mTeam = team;
            nameList.addAll(nameArrayList);
            if(mPeople%mTeam == 0) {
                teamPeople = mPeople/mTeam;
            }else {
                teamPeople = (mPeople)/mTeam;
                teamPeopleSurplus = mPeople%mTeam;
            }

            for(int i = 0; i < nameArrayList.size(); i++) {
                if (i % teamPeople == 0 && teamNumber < mTeam) {
                    teamNumber++;
//                    Log.d("GroupListAdapter", "i%teamPeople == 0 : " + teamNumber);
                }
                peopleList.add(teamNumber);
//                nameList.add(nameArrayList.get(i));

                score1List.add(0);
                score2List.add(0);
                score3List.add(0);
                score4List.add(0);
                score5List.add(0);
                score6List.add(0);
                score7List.add(0);
                score8List.add(0);
                score9List.add(0);
                score10List.add(0);
            }
        }
    }

    static class ViewHolder {
        protected TextView team;
        protected TextView name;
        protected RelativeLayout score;
        protected TextView total;
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
    }

    @Override
    public int getCount() {
        return peopleList.size();
    }

    @Override
    public Object getItem(int i) {
        return peopleList.get(i);
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
            view = mInflator.inflate(R.layout.group_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.team = (TextView) view.findViewById(R.id.team_text);
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

//        Log.d("GroupListAdapter", "i : " + i + "/ name : " + nameList.get(i));

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(mActivity);
                editText.setText(viewHolder.name.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("이름");
                builder.setView(editText);
                AlertDialog alertDialog = builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nameList.set(i, editText.getText().toString());
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });

        viewHolder.name.setText(nameList.get(i));
        viewHolder.team.setText(peopleList.get(i)+"조");

        viewHolder.score_text_1.setText((score1List.get(i)>10?"X":score1List.get(i))+"");
        viewHolder.score_text_2.setText((score2List.get(i)>10?"X":score2List.get(i))+"");
        viewHolder.score_text_3.setText((score3List.get(i)>10?"X":score3List.get(i))+"");
        viewHolder.score_text_4.setText((score4List.get(i)>10?"X":score4List.get(i))+"");
        viewHolder.score_text_5.setText((score5List.get(i)>10?"X":score5List.get(i))+"");
        viewHolder.score_text_6.setText((score6List.get(i)>10?"X":score6List.get(i))+"");
        viewHolder.score_text_7.setText((score7List.get(i)>10?"X":score7List.get(i))+"");
        viewHolder.score_text_8.setText((score8List.get(i)>10?"X":score8List.get(i))+"");
        viewHolder.score_text_9.setText((score9List.get(i)>10?"X":score9List.get(i))+"");
        viewHolder.score_text_10.setText((score10List.get(i)>10?"X":score10List.get(i))+"");


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

        viewHolder.total.setText((
                (score1List.get(i)>10?10:score1List.get(i))+
                (score2List.get(i)>10?10:score2List.get(i))+
                (score3List.get(i)>10?10:score3List.get(i))+
                (score4List.get(i)>10?10:score4List.get(i))+
                (score5List.get(i)>10?10:score5List.get(i))+
                (score6List.get(i)>10?10:score6List.get(i))+
                (score7List.get(i)>10?10:score7List.get(i))+
                (score8List.get(i)>10?10:score8List.get(i))+
                (score9List.get(i)>10?10:score9List.get(i))+
                (score10List.get(i)>10?10:score10List.get(i)))+"");

        viewHolder.score_text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 0, i, viewHolder.score_text_2);
            }
        });
        viewHolder.score_text_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 1, i, viewHolder.score_text_3);
            }
        });
        viewHolder.score_text_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 2, i, viewHolder.score_text_4);
            }
        });
        viewHolder.score_text_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 3, i, viewHolder.score_text_5);
            }
        });
        viewHolder.score_text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 4, i, viewHolder.score_text_6);
            }
        });
        viewHolder.score_text_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 5, i, viewHolder.score_text_7);
            }
        });
        viewHolder.score_text_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 6, i, viewHolder.score_text_8);
            }
        });
        viewHolder.score_text_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 7, i, viewHolder.score_text_9);
            }
        });
        viewHolder.score_text_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 8, i, viewHolder.score_text_10);
            }
        });
        viewHolder.score_text_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDialogShow(v, 9, i, null);
            }
        });


        return view;
    }

    public void goalDialogShow(final View view, final int flag, final int i, final TextView nextView) {
        final TextView textView = (TextView) view;

        LayoutInflater inflater = mInflator;
        final View dialogView = inflater.inflate(R.layout.content_number_popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
        alertDialog.getWindow().setLayout(400, 550);
        alertDialog.setCanceledOnTouchOutside(false);

        int score = 0;
        Button numbr0Button, numbr1Button, numbr2Button, numbr3Button, numbr4Button, numbr5Button, numbr6Button, numbr7Button, numbr8Button, numbr9Button, numbr10Button, numbrXButton;
        numbr0Button = (Button) dialogView.findViewById(R.id.archery_goal_zero);
        numbr1Button = (Button) dialogView.findViewById(R.id.archery_goal_one);
        numbr2Button = (Button) dialogView.findViewById(R.id.archery_goal_two);
        numbr3Button = (Button) dialogView.findViewById(R.id.archery_goal_three);
        numbr4Button = (Button) dialogView.findViewById(R.id.archery_goal_four);
        numbr5Button = (Button) dialogView.findViewById(R.id.archery_goal_five);
        numbr6Button = (Button) dialogView.findViewById(R.id.archery_goal_six);
        numbr7Button = (Button) dialogView.findViewById(R.id.archery_goal_seven);
        numbr8Button = (Button) dialogView.findViewById(R.id.archery_goal_eight);
        numbr9Button = (Button) dialogView.findViewById(R.id.archery_goal_nine);
        numbr10Button = (Button) dialogView.findViewById(R.id.archery_goal_ten);
        numbrXButton = (Button) dialogView.findViewById(R.id.archery_goal_full_scale);

        numbr0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 0);
//                textView.setText("0");
                alertDialog.dismiss();

                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 1);
//                textView.setText("1");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 2);
//                textView.setText("2");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 3);
//                textView.setText("3");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 4);
//                textView.setText("4");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 5);
//                textView.setText("5");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 6);
//                textView.setText("6");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 7);
//                textView.setText("7");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 8);
//                textView.setText("8");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 9);
//                textView.setText("9");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbr10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 10);
//                textView.setText("10");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
        numbrXButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(i, flag, 11);
//                textView.setText("X");
                alertDialog.dismiss();
                if(view.getId() != R.id.circle_10) {
                    nextView.callOnClick();
                }
            }
        });
    }

    private void setScore(int i, int flag, int number) {
        if(flag == 0) {
            score1List.set(i, number);
        }else if(flag == 1) {
            score2List.set(i, number);
        }else if(flag == 2) {
            score3List.set(i, number);
        }else if(flag == 3) {
            score4List.set(i, number);
        }else if(flag == 4) {
            score5List.set(i, number);
        }else if(flag == 5) {
            score6List.set(i, number);
        }else if(flag == 6) {
            score7List.set(i, number);
        }else if(flag == 7) {
            score8List.set(i, number);
        }else if(flag == 8) {
            score9List.set(i, number);
        }else if(flag == 9) {
            score10List.set(i, number);
        }

        notifyDataSetChanged();
    }

}

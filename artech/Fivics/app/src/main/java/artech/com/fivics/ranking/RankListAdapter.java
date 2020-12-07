package artech.com.fivics.ranking;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import artech.com.fivics.R;

/**
 * Created by moon on 2017-01-17.
 */

public class RankListAdapter extends BaseAdapter {

//    private ArrayList<String> nameList;
//    private ArrayList<String> phoneList;
//    private ArrayList<Integer> scoreList;
//    private ArrayList<Integer> arrowList;
//    private ArrayList<String> shopList;
//    private ArrayList<Integer> memberList;
    private ArrayList<RankingActivity.Ranking> rankingArrayList;
    private LayoutInflater mInflator;
    private Activity mActivity;

    public RankListAdapter(Context mContext) {
        super();
        mActivity = (Activity) mContext;
//        nameList = new ArrayList<String>();
//        phoneList = new ArrayList<String>();
//        scoreList = new ArrayList<Integer>();
//        arrowList = new ArrayList<Integer>();
//        shopList = new ArrayList<String>();
//        memberList = new ArrayList<Integer>();
        rankingArrayList = new ArrayList<>();
        mInflator = mActivity.getLayoutInflater();
    }

//    public void addList(ArrayList<String> name, ArrayList<String> phone, ArrayList<Integer> score, ArrayList<Integer> arrow, ArrayList<String> shop, ArrayList<Integer> member) {
//    public void addList(ArrayList<Ranking> arrayList) {
//        if(!nameList.contains(arrayList)) {
//            nameList.addAll(arrayList);
//            phoneList.addAll(phone);
//            scoreList.addAll(score);
//            arrowList.addAll(arrow);
//            shopList.addAll(shop);
//            memberList.addAll(member);
//        if(!rankingArrayList.contains(arrayList)) {
//            rankingArrayList.addAll(arrayList);
//        }
//    }

    public void addList(ArrayList<RankingActivity.Ranking> arrayList) {
        if(!rankingArrayList.contains(arrayList)) {
            rankingArrayList.addAll(arrayList);
        }
    }

    public void clear() {
//        nameList.clear();
//        phoneList.clear();
//        scoreList.clear();
//        arrowList.clear();
//        shopList.clear();
//        memberList.clear();
        rankingArrayList.clear();
    }

    static class ViewHolder {
        protected ImageView rankingImg;
        protected TextView rank;
        protected ImageView memberImg;
        protected TextView name;
        protected TextView avg;
        protected TextView score;
        protected TextView arrow;
        protected TextView shop;
        protected String id;
    }

    @Override
    public int getCount() {
        return rankingArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return rankingArrayList.get(i);
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
            view = mInflator.inflate(R.layout.content_record_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.rank = (TextView) view.findViewById(R.id.rank_text);
            viewHolder.rankingImg = (ImageView) view.findViewById(R.id.ranking_medal_img);
            viewHolder.memberImg = (ImageView) view.findViewById(R.id.player_member_img);
            viewHolder.name = (TextView) view.findViewById(R.id.player_text);
            viewHolder.avg = (TextView) view.findViewById(R.id.avg_text);
            viewHolder.score = (TextView) view.findViewById(R.id.total_text);
            viewHolder.arrow = (TextView) view.findViewById(R.id.arrow_text);
            viewHolder.shop = (TextView) view.findViewById(R.id.homeshop_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        nameList.clear();
//        phoneList.clear();
//        scoreList.clear();
//        arrowList.clear();
//        shopList.clear();
//        memberList.clear();

//        final String name = nameList.get(i);
//        String phone = "";
//        if(phoneList.get(i) == null || "".equals(phoneList.get(i))) {
//            phone = "0000";
//        }else {
//             phone = phoneList.get(i).substring(7, 11);

        final String name = rankingArrayList.get(i).name;
        String phone = "";
        if(rankingArrayList.get(i).phone == null || "".equals(rankingArrayList.get(i).phone)) {
            phone = "0000";
        }else {
            phone = rankingArrayList.get(i).phone.substring(rankingArrayList.get(i).phone.length()-4,rankingArrayList.get(i).phone.length());
//            phone = phoneList.get(i).substring(6, 10);
        }
//        final Integer score  = scoreList.get(i);
//        final Integer arrow = arrowList.get(i);
//        final String shop = shopList.get(i);
//        final Integer member = memberList.get(i);
        final Integer score  = rankingArrayList.get(i).score;
        final Integer arrow = rankingArrayList.get(i).arrow;
        final String shop = rankingArrayList.get(i).shop;
        final Integer member = rankingArrayList.get(i).member;
        if (name != null && name.length() > 0) {
//            viewHolder.date.setText(date);
//            viewHolder.time.setText(time);
//            viewHolder.score.setText("score : " + score);
            if(rankingArrayList.get(i).rank == 1) {
                viewHolder.rank.setVisibility(View.GONE);
                viewHolder.rankingImg.setImageResource(R.mipmap.medal_1);
                viewHolder.rankingImg.setVisibility(View.VISIBLE);
            }else if(rankingArrayList.get(i).rank == 2) {
                viewHolder.rank.setVisibility(View.GONE);
                viewHolder.rankingImg.setImageResource(R.mipmap.medal_2);
                viewHolder.rankingImg.setVisibility(View.VISIBLE);
            }else if(rankingArrayList.get(i).rank == 3) {
                viewHolder.rank.setVisibility(View.GONE);
                viewHolder.rankingImg.setImageResource(R.mipmap.medal_3);
                viewHolder.rankingImg.setVisibility(View.VISIBLE);
            }else {
                viewHolder.rank.setVisibility(View.VISIBLE);
                viewHolder.rank.setText(rankingArrayList.get(i).rank+"");
                viewHolder.rankingImg.setVisibility(View.GONE);
            }

            if(member == 2) {
                viewHolder.memberImg.setImageResource(R.mipmap.img_member);
            }else {
                viewHolder.memberImg.setImageResource(R.mipmap.img_nonmember);
            }
            viewHolder.name.setText(name+"("+phone+")");

            viewHolder.avg.setText(String.format("%.1f", rankingArrayList.get(i).avg));

            viewHolder.score.setText(score+"");
            viewHolder.arrow.setText("("+arrow+")");

            viewHolder.shop.setText(shop);
        }else {
//            viewHolder.date.setText("2017-01-01");
//            viewHolder.time.setText("00:00:00");
//            viewHolder.score.setText("0");
        }
        
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ArcheryActivity)mActivity).recordClickEvent(viewHolder.id);
//            }
//        });

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

package artech.com.fivics.ranking;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import artech.com.fivics.R;

/**
 * Created by moon on 2017-01-17.
 */

public class RankDetailListAdapter extends BaseAdapter {

//    private ArrayList<String> nameList;
//    private ArrayList<String> phoneList;
//    private ArrayList<Integer> scoreList;
//    private ArrayList<Integer> arrowList;
//    private ArrayList<String> shopList;
//    private ArrayList<Integer> memberList;
    private ArrayList<RankingActivity.RankingDeatilHistory> rankingArrayList;
    private LayoutInflater mInflator;
    private Activity mActivity;

    public RankDetailListAdapter(Context mContext) {
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

    public void addList(ArrayList<RankingActivity.RankingDeatilHistory> arrayList) {
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
        protected TextView name;
        protected TextView date;
        protected TextView arrow;
        protected TextView avg;
        protected TextView score;
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
            view = mInflator.inflate(R.layout.content_record_detail_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.rank_name);
            viewHolder.date = (TextView) view.findViewById(R.id.date_text);
            viewHolder.avg = (TextView) view.findViewById(R.id.avg_text);
            viewHolder.score = (TextView) view.findViewById(R.id.score_text);
            viewHolder.arrow = (TextView) view.findViewById(R.id.arrow_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final String name = rankingArrayList.get(i).name;
        final String date = rankingArrayList.get(i).date;
        final Integer arrow = rankingArrayList.get(i).arrow;
        final float avg = rankingArrayList.get(i).avg;
        final Integer score  = rankingArrayList.get(i).score;
        if (name != null && name.length() > 0) {
            viewHolder.name.setText(name);
            viewHolder.date.setText(date);
            viewHolder.avg.setText(String.format("%.1f", avg));
            viewHolder.score.setText(score+"");
            viewHolder.arrow.setText(arrow+"");
        }else {
        }



        return view;
    }
}

package artech.com.rangking;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by moon on 2017-01-17.
 */

public class RankListAdapter extends BaseAdapter {

    private ArrayList<MainActivity.Ranking> rankingArrayList;
    private LayoutInflater mInflator;
    private Activity mActivity;

    public RankListAdapter(Context mContext) {
        super();
        mActivity = (Activity) mContext;
        rankingArrayList = new ArrayList<>();
        mInflator = mActivity.getLayoutInflater();
    }

    public void addList(ArrayList<MainActivity.Ranking> arrayList) {
        if(!rankingArrayList.contains(arrayList)) {
            rankingArrayList.addAll(arrayList);
        }
    }

    public void clear() {
        rankingArrayList.clear();
    }

    static class ViewHolder {
        protected ImageView rankingImg;
        protected ImageView memberImg;
        protected TextView rank;
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
            view = mInflator.inflate(R.layout.content_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.rank = (TextView) view.findViewById(R.id.rank_text);
            viewHolder.rankingImg = (ImageView) view.findViewById(R.id.ranking_medal_img);
            viewHolder.memberImg = (ImageView) view.findViewById(R.id.player_member_img);
            viewHolder.name = (TextView) view.findViewById(R.id.player_text);
            viewHolder.avg = (TextView) view.findViewById(R.id.avg_text);
            viewHolder.shop = (TextView) view.findViewById(R.id.homeshop_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final String name = rankingArrayList.get(i).name;
        String phone = "";
        if(rankingArrayList.get(i).phone == null || "".equals(rankingArrayList.get(i).phone)) {
            phone = "0000";
        }else {
            phone = rankingArrayList.get(i).phone.substring(rankingArrayList.get(i).phone.length()-4,rankingArrayList.get(i).phone.length());
        }
        final Integer score  = rankingArrayList.get(i).score;
        final Integer arrow = rankingArrayList.get(i).arrow;
        final String shop = rankingArrayList.get(i).shop;
        final Integer member = rankingArrayList.get(i).member;
        if (name != null && name.length() > 0) {
            if(rankingArrayList.get(i).rank == 1) {
                viewHolder.rank.setVisibility(View.GONE);
                viewHolder.rankingImg.setImageResource(R.mipmap.img_medal_1);
                viewHolder.rankingImg.setVisibility(View.VISIBLE);
            }else if(rankingArrayList.get(i).rank == 2) {
                viewHolder.rank.setVisibility(View.GONE);
                viewHolder.rankingImg.setImageResource(R.mipmap.img_medal_2);
                viewHolder.rankingImg.setVisibility(View.VISIBLE);
            }else if(rankingArrayList.get(i).rank == 3) {
                viewHolder.rank.setVisibility(View.GONE);
                viewHolder.rankingImg.setImageResource(R.mipmap.img_medal_3);
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

            if(rankingArrayList.get(i).flag == 1) {
                viewHolder.avg.setText(String.format("%.1f", rankingArrayList.get(i).avg));
            }else if(rankingArrayList.get(i).flag == 2){
                viewHolder.avg.setText(score+"("+arrow+")");
            }


            viewHolder.shop.setText(shop);
        }else {
        }
        
        return view;
    }


}

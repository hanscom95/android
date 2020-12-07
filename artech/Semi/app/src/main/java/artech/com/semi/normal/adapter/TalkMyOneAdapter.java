package artech.com.semi.normal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.normal.TalkDetailActivity;
import artech.com.semi.normal.fragment.TalkMyOneFragment;
import artech.com.semi.normal.item.ProductManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class TalkMyOneAdapter extends RecyclerView.Adapter<TalkMyOneAdapter.ViewHolder> {
    List<List<String>> mItems;
    TalkMyOneFragment mFragment;
    Context mContext;
    JSONArray mJsonArray;

    public int mFlag = -1;

    public TalkMyOneAdapter(TalkMyOneFragment fragment, Context context) {
        super();
        mFragment = fragment;
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray jsonArray) {
        List<String> list;
        mJsonArray = jsonArray;
        Log.d("talkadapter" , "list : " + mJsonArray.toString());
        for(int i = 0; i < mJsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
                Log.d("talkadapter" , "title : " + mJsonArray.getJSONObject(i).getString("title"));
                list.add(mJsonArray.getJSONObject(i).getString("name"));
                list.add(mJsonArray.getJSONObject(i).getString("address"));
                list.add(mJsonArray.getJSONObject(i).getString("title"));
                list.add(mJsonArray.getJSONObject(i).getString("contents"));
                list.add(mJsonArray.getJSONObject(i).getString("date"));
                list.add(mJsonArray.getJSONObject(i).getString("like"));
                list.add(mJsonArray.getJSONObject(i).getString("scrap"));
                list.add(mJsonArray.getJSONObject(i).getString("review"));
                list.add(mJsonArray.getJSONObject(i).getString("picture1"));
                list.add(mJsonArray.getJSONObject(i).getString("picture2"));
                list.add(mJsonArray.getJSONObject(i).getString("picture3"));
                list.add(mJsonArray.getJSONObject(i).getString("talk_id"));
                list.add(mJsonArray.getJSONObject(i).getString("profile"));
                mItems.add(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        mItems.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_talk_my_one_list_customer, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        List<String> nature = mItems.get(i);

        viewHolder.nameText.setText(Html.fromHtml(nature.get(0).replace("\n", "<br>")));
        viewHolder.titleText.setText(Html.fromHtml(nature.get(2).replace("\n", "<br>")));
        viewHolder.contentsText.setText(Html.fromHtml(nature.get(3).replace("\n", "<br>")));
        viewHolder.dateText.setText(Html.fromHtml(nature.get(4)));
        viewHolder.favoriteText.setText(Util.toNumFormat(Integer.parseInt(nature.get(5))));
        viewHolder.scrapText.setText(Util.toNumFormat(Integer.parseInt(nature.get(6))));
        viewHolder.reviewText.setText(Util.toNumFormat(Integer.parseInt(nature.get(7))));

//        if(nature.get(12) != null || !"".equals(nature.get(12)) || nature.get(12) != "" )
        if(nature.get(12) != "null" )
            viewHolder.thumbnailImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(12)));


        viewHolder.detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.setDetailClick(i);
//                try {
//                    Intent intent = new Intent(mContext, TalkDetailActivity.class);
//                    intent.putExtra("jsonObject", mJsonArray.getJSONObject(i).toString());
//                    mContext.startActivity(intent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImg;
        public TextView nameText, dateText, titleText, contentsText, favoriteText, reviewText, scrapText;
        public RelativeLayout detailLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImg = itemView.findViewById(R.id.item_profile_img);
            nameText = itemView.findViewById(R.id.item_name);
            dateText = itemView.findViewById(R.id.item_date);
            titleText = itemView.findViewById(R.id.item_title_text);
            contentsText = itemView.findViewById(R.id.item_contents_text);
            favoriteText = itemView.findViewById(R.id.item_favorite);
            reviewText = itemView.findViewById(R.id.item_review_value_text);
            scrapText = itemView.findViewById(R.id.item_scrap_value_text);
            detailLayout = itemView.findViewById(R.id.sort_one_layout);
        }
    }

}


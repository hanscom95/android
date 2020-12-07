package artech.com.semi.normal.adapter;

import android.content.Context;
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
import artech.com.semi.normal.fragment.TalkMyFourFragment;
import artech.com.semi.normal.fragment.TalkMyOneFragment;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class TalkMyFourAdapter extends RecyclerView.Adapter<TalkMyFourAdapter.ViewHolder> {
    List<List<String>> mItems;
    TalkMyFourFragment mFragment;
    Context mContext;

    public int mFlag = -1;

    public TalkMyFourAdapter(TalkMyFourFragment fragment, Context context) {
        super();
        mFragment = fragment;
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray mJsonArray) {
        List<String> list;
        Log.d("talkadapter" , "list : " + mJsonArray.toString());
        for(int i = 0; i < mJsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
                list.add(mJsonArray.getJSONObject(i).getString("name"));
                list.add(mJsonArray.getJSONObject(i).getString("product_name"));
                list.add(mJsonArray.getJSONObject(i).getString("product_content"));
                list.add(mJsonArray.getJSONObject(i).getString("reg_dt"));
                list.add(mJsonArray.getJSONObject(i).getString("recommend"));
                list.add(mJsonArray.getJSONObject(i).getString("scrap"));
                list.add(mJsonArray.getJSONObject(i).getString("review"));
                list.add(mJsonArray.getJSONObject(i).getString("picture"));
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
                .inflate(R.layout.adapter_talk_my_four_list_customer, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        List<String> nature = mItems.get(i);

        viewHolder.nameText.setText(Html.fromHtml(nature.get(0).replace("\n", "<br>")));
        viewHolder.titleText.setText(Html.fromHtml(nature.get(1).replace("\n", "<br>")));
        viewHolder.contentsText.setText(Html.fromHtml(nature.get(2).replace("\n", "<br>")));
        viewHolder.dateText.setText(Html.fromHtml(nature.get(3)));
        viewHolder.favoriteText.setText(Util.toNumFormat(Integer.parseInt(nature.get(4))));
        viewHolder.scrapText.setText(Util.toNumFormat(Integer.parseInt(nature.get(5))));
        viewHolder.reviewText.setText(Util.toNumFormat(Integer.parseInt(nature.get(6))));
//        viewHolder.thumbnailImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(7)));
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
            thumbnailImg = (ImageView) itemView.findViewById(R.id.item_profile_img);
            nameText = (TextView) itemView.findViewById(R.id.item_name);
            dateText = (TextView) itemView.findViewById(R.id.item_date);
            titleText = (TextView) itemView.findViewById(R.id.item_title_text);
            contentsText = (TextView) itemView.findViewById(R.id.item_contents_text);
            favoriteText = (TextView) itemView.findViewById(R.id.item_favorite);
            reviewText = (TextView) itemView.findViewById(R.id.item_review_value_text);
            scrapText = (TextView) itemView.findViewById(R.id.item_scrap_value_text);
        }
    }

}


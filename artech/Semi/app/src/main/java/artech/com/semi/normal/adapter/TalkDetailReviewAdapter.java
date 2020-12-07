package artech.com.semi.normal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class TalkDetailReviewAdapter extends RecyclerView.Adapter<TalkDetailReviewAdapter.ViewHolder> {
    List<List<String>> mItems;
    Context mContext;

    public TalkDetailReviewAdapter(Context context) {
        super();
        mContext = context;

        mItems = new ArrayList<>();
    }

    public void addList(JSONArray mJsonArray) {
        List<String> list;
        for(int i = 0; i < mJsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
                list.add(mJsonArray.getJSONObject(i).getString("name"));
                list.add(mJsonArray.getJSONObject(i).getString("contents"));
                list.add(mJsonArray.getJSONObject(i).getString("reg_dt"));
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
                .inflate(R.layout.adapter_talk_review_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        List<String> nature = mItems.get(i);
        viewHolder.nameText.setText(Html.fromHtml(nature.get(0)));
        viewHolder.contentsText.setText(Html.fromHtml(nature.get(1).replace("\n", "<br>")));
        viewHolder.detailText.setText(Html.fromHtml(nature.get(2)));
//        if(nature.get(3) != "")
        if(nature.get(3) != "null")
            viewHolder.thumnailImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(3)));


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText, contentsText, detailText, replyText;
        public CheckBox favoritesCheckbox;
        public ImageView thumnailImg;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.item_name);
            contentsText = itemView.findViewById(R.id.item_contents_text);
            detailText = itemView.findViewById(R.id.item_detail_text);
            replyText = itemView.findViewById(R.id.item_reply_text);
            thumnailImg = itemView.findViewById(R.id.item_profile);
        }
    }

}


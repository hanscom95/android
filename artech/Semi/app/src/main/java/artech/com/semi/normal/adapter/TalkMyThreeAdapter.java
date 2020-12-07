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
import artech.com.semi.normal.fragment.TalkMyOneFragment;
import artech.com.semi.normal.fragment.TalkMyThreeFragment;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class TalkMyThreeAdapter extends RecyclerView.Adapter<TalkMyThreeAdapter.ViewHolder> {
    List<List<String>> mItems;
    TalkMyThreeFragment mFragment;
    Context mContext;

    public int mFlag = -1;

    public TalkMyThreeAdapter(TalkMyThreeFragment fragment, Context context) {
        super();
        mFragment = fragment;
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray mJsonArray) {
        List<String> list;
        Log.d("TalkMyTwoAdapter" , "list : " + mJsonArray.toString());
        for(int i = 0; i < mJsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
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
                .inflate(R.layout.adapter_talk_my_three_list_customer, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        List<String> nature = mItems.get(i);
        viewHolder.image.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(0)));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }

}


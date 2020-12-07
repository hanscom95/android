package artech.com.semi.normal.adapter;

import android.content.Context;
import android.content.Intent;
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
import artech.com.semi.normal.ShopDetailActivity;
import artech.com.semi.normal.item.ProductManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class ShopTagAdapter extends RecyclerView.Adapter<ShopTagAdapter.ViewHolder> {
    List<String> mItems;
    Context mContext;

    public ShopTagAdapter(Context context) {
        super();
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(String tag) {
        List<String> item = Util.convertToArray(Html.fromHtml(tag).toString());
        List<String> items = new ArrayList<>();
        for(int i = 0; i < item.size(); i++) {
            if(i > 0) {
                items.add(item.get(i));
            }
        }
        mItems = items;

    }

    public void clear() {
        mItems.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_shop_tag_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final String tag = mItems.get(i);
        Log.d("ShopTagAdapter", "i : " + i + " / tag : " + tag);
        viewHolder.valueText.setText("#"+tag);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView valueText;

        public ViewHolder(View itemView) {
            super(itemView);
            valueText = itemView.findViewById(R.id.item_value);
        }
    }

}


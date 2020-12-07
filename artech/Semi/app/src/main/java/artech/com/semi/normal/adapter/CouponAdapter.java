package artech.com.semi.normal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;

/**
 * Created by taehoon on 01/04/2018.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    List<List<String>> mItems;
    Context mContext;

    public CouponAdapter(Context context) {
        super();
        mContext = context;

        mItems = new ArrayList<>();
    }

    public void addList(JSONArray jsonArray) {
        List<String> list;
        for(int i = 0; i < jsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
                list.add(jsonArray.getJSONObject(i).getString("id"));
                list.add(Html.fromHtml(jsonArray.getJSONObject(i).getString("contents")).toString());
                list.add(jsonArray.getJSONObject(i).getString("price"));
                list.add(jsonArray.getJSONObject(i).getString("reg_dt"));
                list.add(jsonArray.getJSONObject(i).getString("number"));
                list.add(Html.fromHtml(jsonArray.getJSONObject(i).getString("title")).toString());
                list.add(jsonArray.getJSONObject(i).getString("start_date"));
                list.add(jsonArray.getJSONObject(i).getString("end_date"));
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
                .inflate(R.layout.adapter_coupon_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        List<String> nature = mItems.get(i);
        viewHolder.contentsText.setText(nature.get(1));
        viewHolder.priceText.setText(nature.get(2));
        viewHolder.detailText.setText(nature.get(5));
        viewHolder.titleText.setText("모든 미끼");
        viewHolder.dateText.setText(nature.get(3));
        viewHolder.stateText.setText("사용하기");


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView priceText, contentsText, detailText, titleText, dateText, stateText;

        public ViewHolder(View itemView) {
            super(itemView);
            priceText = itemView.findViewById(R.id.item_price);
            contentsText = itemView.findViewById(R.id.item_contents);
            detailText = itemView.findViewById(R.id.item_detail);
            titleText = itemView.findViewById(R.id.item_title);
            dateText = itemView.findViewById(R.id.item_date);
            stateText = itemView.findViewById(R.id.item_download);
        }
    }

}


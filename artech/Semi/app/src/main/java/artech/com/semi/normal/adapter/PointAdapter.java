package artech.com.semi.normal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.normal.item.QuestionItem;

/**
 * Created by taehoon on 01/04/2018.
 */
public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {
    List<List<String>> mItems;
    Context mContext;

    public PointAdapter(Context context) {
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
                .inflate(R.layout.adapter_point_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        List<String> nature = mItems.get(i);
        viewHolder.titleText.setText(nature.get(1));
        if(Integer.parseInt(nature.get(2)) > 0) {
            viewHolder.pointText.setText("+ "+nature.get(2));
        }else {
            viewHolder.pointText.setText("- "+nature.get(2));
        }
        viewHolder.dateText.setText(nature.get(3));
        viewHolder.stateText.setText("적립완료");


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText, pointText, dateText, stateText;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.item_title);
            pointText = itemView.findViewById(R.id.item_point);
            dateText = itemView.findViewById(R.id.item_date);
            stateText = itemView.findViewById(R.id.item_state);
        }
    }

}


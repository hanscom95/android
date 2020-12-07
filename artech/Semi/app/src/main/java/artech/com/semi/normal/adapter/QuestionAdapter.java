package artech.com.semi.normal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    List<QuestionItem> mItems;
    Context mContext;

    public QuestionAdapter(Context context) {
        super();
        mContext = context;

        mItems = new ArrayList<>();
    }

    public void addList(JSONArray mJsonArray) {
        QuestionItem nama;
        for(int i = 0; i < mJsonArray.length() ;i++) {
            nama = new QuestionItem();
            try {
                nama.setTitle(mJsonArray.getJSONObject(i).getString("title"));
                nama.setContents(mJsonArray.getJSONObject(i).getString("contents"));
                nama.setDate(mJsonArray.getJSONObject(i).getString("date"));
                mItems.add(nama);
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
                .inflate(R.layout.adapter_question_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        QuestionItem nature = mItems.get(i);
        viewHolder.titleText.setText(nature.getTitle());
        viewHolder.contentsText.setText(nature.getContents());
        viewHolder.dateText.setText(nature.getDate());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.expandableLayout.toggle();
            }
        });
//        viewHolder.expandableLayout.toggle();

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText, contentsText, dateText;
        public RelativeLayout layout;
        public ExpandableLinearLayout expandableLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.item_title_text);
            contentsText = itemView.findViewById(R.id.item_contents_text);
            dateText = itemView.findViewById(R.id.item_date_text);

            layout = itemView.findViewById(R.id.item_layout);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);
        }
    }

}


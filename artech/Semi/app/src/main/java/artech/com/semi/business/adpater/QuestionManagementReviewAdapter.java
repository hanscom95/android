package artech.com.semi.business.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import artech.com.semi.business.fragment.QuestionManagementAdminFragment;
import artech.com.semi.business.fragment.QuestionManagementReviewFragment;
import artech.com.semi.business.item.QuestionManagementItem;
import artech.com.semi.business.item.SaleManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class QuestionManagementReviewAdapter extends RecyclerView.Adapter<QuestionManagementReviewAdapter.ViewHolder> {
    List<QuestionManagementItem> mItems;
    QuestionManagementReviewFragment mFragment;
    Context mContext;

    int mFlag = -1;

    public QuestionManagementReviewAdapter(QuestionManagementReviewFragment fragment, Context context, int flag) {
        super();
        mFragment = fragment;
        mContext = context;

        mItems = new ArrayList<>();

        mFlag = flag;
    }


    public void addList(JSONArray mJsonArray) {
        QuestionManagementItem nama;
        for(int i = 0; i < mJsonArray.length() ;i++) {
            nama = new QuestionManagementItem();
            try {
                nama.setId(mJsonArray.getJSONObject(i).getString("review_id")+ "");
                nama.setName(Html.fromHtml(mJsonArray.getJSONObject(i).getString("name")).toString());
                nama.setContent(Html.fromHtml(mJsonArray.getJSONObject(i).getString("review")).toString());
                nama.setRegDate(mJsonArray.getJSONObject(i).getString("reg_dt"));
                nama.setPoint(mJsonArray.getJSONObject(i).getInt("point"));
                nama.setThumbnail(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
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
                .inflate(R.layout.adapter_question_product_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        QuestionManagementItem nature = mItems.get(i);
        viewHolder.nameText.setText(nature.getName());
        viewHolder.titleText.setText(nature.getContent());
        viewHolder.regText.setText(nature.getRegDate());
        if(nature.getThumbnail() != null) {
            viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
        }


        if(mFlag > 0) {
            viewHolder.waitingLayout.setBackgroundResource(R.mipmap.line_wait_press);
            viewHolder.waitingText.setTextColor(mContext.getColor(R.color.white));
            viewHolder.waitingText.setText("답변완료");
        }

//
        viewHolder.gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.setDetailClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView regText, titleText, nameText, waitingText;
        public RelativeLayout gridLayout, waitingLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.purchase_img);
            regText = itemView.findViewById(R.id.purchase_time);
            titleText = itemView.findViewById(R.id.purchase_title);
            nameText = itemView.findViewById(R.id.purchase_name);
            waitingText = itemView.findViewById(R.id.waiting_text);
            gridLayout = itemView.findViewById(R.id.grid_layout);
            waitingLayout = itemView.findViewById(R.id.waiting_layout);

            imgThumbnail.setVisibility(View.GONE);
        }
    }

}


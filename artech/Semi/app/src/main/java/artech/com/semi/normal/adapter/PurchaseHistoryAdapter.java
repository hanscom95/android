package artech.com.semi.normal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.normal.PurchaseHistoryDetailActivity;
import artech.com.semi.normal.ShopDetailActivity;
import artech.com.semi.normal.item.PurchaseHistoryItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {
    List<PurchaseHistoryItem> mItems;
    Context mContext;

    int visible = 0;

    public PurchaseHistoryAdapter(Context context, int i) {
        super();
        mContext = context;
        visible = i;

        mItems = new ArrayList<>();
    }

    public void addList(JSONArray jsonArray) {
        PurchaseHistoryItem item;
        for(int i = 0; i < jsonArray.length() ;i++) {
            item = new PurchaseHistoryItem();
            try {
                item.setId(jsonArray.getJSONObject(i).getString("number"));
                item.setTitle(Html.fromHtml(jsonArray.getJSONObject(i).getString("product_name")).toString());
                item.setAddress(Html.fromHtml(jsonArray.getJSONObject(i).getString("address")).toString());
                item.setShop(Html.fromHtml(jsonArray.getJSONObject(i).getString("company")).toString());
                item.setOrderNumber(jsonArray.getJSONObject(i).getString("merchant_uid"));
                item.setOrderTime(jsonArray.getJSONObject(i).getString("reg_dt"));
                item.setState(jsonArray.getJSONObject(i).getInt("state"));
                item.setThumbnail(Util.setDecoded64ImageStringFromBitmap(jsonArray.getJSONObject(i).getString("picture")));
                mItems.add(item);
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
                .inflate(R.layout.adapter_normal_purchase_history_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final PurchaseHistoryItem nature = mItems.get(i);
//        viewHolder.titleText.setText(nature.getTitle());
        viewHolder.titleText.setText("[" + nature.getAddress().split(" ")[0] + "][" + nature.getShop() + "]" + nature.getTitle());
        viewHolder.orderNumberText.setText("주문번호 : " + nature.getOrderNumber());
        viewHolder.orderTimeText.setText("주문시간 : " + nature.getOrderTime());
        if(nature.getThumbnail() != null) {
            viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
        }

        if(nature.getState() == 0) {
            viewHolder.orderStatusText.setText("신규주문");
        }else if(nature.getState() == 1) {
            viewHolder.orderStatusText.setText("수령완료");
        }else if(nature.getState() == 2) {
            viewHolder.orderStatusText.setText("취소요청");
        }else if(nature.getState() == 4) {
            viewHolder.orderStatusText.setText("입금대기");
        }else if(nature.getState() == 5) {
            viewHolder.orderStatusText.setText("수령대기");
        }

        if(visible == 1) {
            viewHolder.orderStatusText.setBackgroundColor(mContext.getColor(R.color.aqua_marine));
            viewHolder.rightArrowImg.setVisibility(View.VISIBLE);
        }else {
            viewHolder.rightArrowImg.setVisibility(View.GONE);
        }


        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ShopDetailActivity.class);
//                intent.putExtra("id", mItems.get(i).getId());
//                mContext.startActivity(intent);

                Intent intent = new Intent(mContext, PurchaseHistoryDetailActivity.class);
                intent.putExtra("id", nature.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail, rightArrowImg;
        public TextView titleText, orderNumberText, orderStatusText, orderTimeText;
        public RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.item_name_text);
            orderNumberText = itemView.findViewById(R.id.order_number_text);
            orderStatusText = itemView.findViewById(R.id.order_status_text);
            orderTimeText = itemView.findViewById(R.id.item_time_text);
            layout = itemView.findViewById(R.id.first_layout);
            imgThumbnail = itemView.findViewById(R.id.item_img);
            rightArrowImg = itemView.findViewById(R.id.right_arrow_img);
        }
    }

}


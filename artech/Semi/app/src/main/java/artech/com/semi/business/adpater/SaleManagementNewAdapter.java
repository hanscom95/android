package artech.com.semi.business.adpater;

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
import artech.com.semi.business.fragment.SaleManagementNewFragment;
import artech.com.semi.business.item.SaleManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class SaleManagementNewAdapter extends RecyclerView.Adapter<SaleManagementNewAdapter.ViewHolder> {
    List<SaleManagementItem> mItems;
    SaleManagementNewFragment mFragment;
    Context mContext;

    public SaleManagementNewAdapter(SaleManagementNewFragment fragment, Context context) {
        super();
        mFragment = fragment;
        mContext = context;

        mItems = new ArrayList<SaleManagementItem>();
    }


    public void addList(JSONArray jsonArray) {
        SaleManagementItem nama;
        for(int i = 0; i < jsonArray.length() ;i++) {
            nama = new SaleManagementItem();
            try {
                nama.setName(Html.fromHtml(jsonArray.getJSONObject(i).getString("name").replace("\n", "<br>")).toString());
                nama.setRegDate(jsonArray.getJSONObject(i).getString("reg_dt"));
                nama.setReceiptDate(jsonArray.getJSONObject(i).getString("receipt_dt"));
                nama.setTitle(Html.fromHtml(jsonArray.getJSONObject(i).getString("product_name").replace("\n", "<br>")).toString());
                nama.setState(jsonArray.getJSONObject(i).getInt("state"));
                nama.setAmmount(jsonArray.getJSONObject(i).getInt("quantity"));
                nama.setThumbnail(Util.setDecoded64ImageStringFromBitmap(jsonArray.getJSONObject(i).getString("picture")));
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
                .inflate(R.layout.adapter_sale_new_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        SaleManagementItem nature = mItems.get(i);
        viewHolder.nameText.setText(nature.getName());
        viewHolder.titleText.setText(nature.getTitle());
        viewHolder.regText.setText("구매일 " + nature.getRegDate());
        viewHolder.receiptText.setText(nature.getReceiptDate());
        viewHolder.amountText.setText("총 "+nature.getAmmount() + "개");

        if(nature.getState() == 4) {
            viewHolder.statusText.setText("입금대기");
        }

        if(nature.getThumbnail() != null) {
            viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
        }

        viewHolder.detailLayout.setOnClickListener(new View.OnClickListener() {
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
        public TextView regText, titleText, nameText, receiptText, amountText, statusText;
        public RelativeLayout detailLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.purchase_img);
            regText = itemView.findViewById(R.id.purchase_date);
            titleText = itemView.findViewById(R.id.purchase_title);
            nameText = itemView.findViewById(R.id.purchase_name);
            receiptText = itemView.findViewById(R.id.purchase_time);
            amountText = itemView.findViewById(R.id.quantity);
            statusText = itemView.findViewById(R.id.payment_state);
            detailLayout = itemView.findViewById(R.id.detail_layout);
        }
    }

}


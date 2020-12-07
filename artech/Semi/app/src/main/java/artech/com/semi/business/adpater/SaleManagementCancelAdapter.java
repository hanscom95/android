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
import artech.com.semi.business.fragment.SaleManagementCancelFragment;
import artech.com.semi.business.item.SaleManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class SaleManagementCancelAdapter extends RecyclerView.Adapter<SaleManagementCancelAdapter.ViewHolder> {
    List<SaleManagementItem> mItems;
    SaleManagementCancelFragment mSaleManagementCancelFragment;
    Context mContext;

    public SaleManagementCancelAdapter(SaleManagementCancelFragment saleManagementFragment, Context context) {
        super();
        mSaleManagementCancelFragment = saleManagementFragment;

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
                .inflate(R.layout.adapter_sale_cancel_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        SaleManagementItem nature = mItems.get(i);
        viewHolder.regText.setText(nature.getRegDate());
        viewHolder.titleText.setText(nature.getTitle());
        viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaleManagementCancelFragment.setDetailClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView regText, titleText;
        public RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.purchase_img);
            regText = itemView.findViewById(R.id.purchase_time);
            titleText = itemView.findViewById(R.id.purchase_title);
            layout = itemView.findViewById(R.id.grid_layout);
        }
    }
}


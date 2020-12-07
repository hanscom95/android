package artech.com.semi.business.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.business.fragment.SaleManagementSuccessFragment;
import artech.com.semi.business.item.SaleManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class SaleManagementSuccessAdapter extends RecyclerView.Adapter<SaleManagementSuccessAdapter.ViewHolder> {
    static public List<SaleManagementItem> mItems;
    SaleManagementSuccessFragment mFragment;
    Context mContext;

    static public int visible = 0;
    static public int checked = 0;

    public SaleManagementSuccessAdapter(SaleManagementSuccessFragment saleManagementFragment, Context context) {
        super();
        mFragment = saleManagementFragment;
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray jsonArray) {
        SaleManagementItem nama;
        for(int i = 0; i < jsonArray.length() ;i++) {
            nama = new SaleManagementItem();
            try {
                nama.setId(jsonArray.getJSONObject(i).getString("number"));
                nama.setName(Html.fromHtml(jsonArray.getJSONObject(i).getString("name").replace("\n", "<br>")).toString());
                nama.setRegDate(jsonArray.getJSONObject(i).getString("reg_dt"));
                nama.setReceiptDate(jsonArray.getJSONObject(i).getString("receipt_dt"));
                nama.setTitle(Html.fromHtml(jsonArray.getJSONObject(i).getString("product_name").replace("\n", "<br>")).toString());
                nama.setState(jsonArray.getJSONObject(i).getInt("state"));
                nama.setAmmount(jsonArray.getJSONObject(i).getInt("quantity"));
                nama.setThumbnail(Util.setDecoded64ImageStringFromBitmap(jsonArray.getJSONObject(i).getString("picture")));
                nama.setChecked(0);
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
                .inflate(R.layout.adapter_sale_success_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

//        viewHolder.detailLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSaleManagementSuccessFragment.setDetailClick();
//            }
//        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final SaleManagementItem nature = mItems.get(i);
//        viewHolder.tvspecies.setText(nature.getName());
        viewHolder.regText.setText(nature.getRegDate());
        viewHolder.titleText.setText(nature.getTitle());
        viewHolder.nameText.setText(nature.getName());
        viewHolder.quantityText.setText("총 " + nature.getAmmount() + "개");
        viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    nature.setChecked(1);
                }else {
                    nature.setChecked(0);
                }
            }
        });


        if(visible == 0) {
            viewHolder.checkbox.setVisibility(View.GONE);
        }else {
            viewHolder.checkbox.setVisibility(View.VISIBLE);
        }


        if(checked == 0) {
            viewHolder.checkbox.setChecked(false);
        }else {
            viewHolder.checkbox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView regText, titleText, nameText, quantityText, detailText;
        public CheckBox checkbox;
//        public TextView tvspecies;
//        public RelativeLayout detailLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.purchase_img);
            regText = itemView.findViewById(R.id.purchase_time);
            titleText = itemView.findViewById(R.id.purchase_title);
            nameText = itemView.findViewById(R.id.purchase_name);
            quantityText = itemView.findViewById(R.id.quantity);
            detailText = itemView.findViewById(R.id.detail_view);
            checkbox = itemView.findViewById(R.id.purchase_checkbox);
        }
    }
}


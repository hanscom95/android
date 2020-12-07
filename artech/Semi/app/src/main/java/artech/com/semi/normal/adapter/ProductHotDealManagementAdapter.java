package artech.com.semi.normal.adapter;

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
import artech.com.semi.normal.fragment.ProductHotDealManagementFragment;
import artech.com.semi.normal.item.ProductManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class ProductHotDealManagementAdapter extends RecyclerView.Adapter<ProductHotDealManagementAdapter.ViewHolder> {
    List<ProductManagementItem> mItems;
    ProductHotDealManagementFragment mFragment;
    Context mContext;

    public ProductHotDealManagementAdapter(ProductHotDealManagementFragment fragment, Context context) {
        super();
        mFragment = fragment;
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray mJsonArray) {
        ProductManagementItem item;
        for(int i = 0; i < mJsonArray.length() ;i++) {
            item = new ProductManagementItem();
            try {
                item.setTitle(Html.fromHtml(mJsonArray.getJSONObject(i).getString("product_name")).toString());
                item.setAddress(Html.fromHtml(mJsonArray.getJSONObject(i).getString("address")).toString());
                item.setShop(Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")).toString());
                item.setRecommand(mJsonArray.getJSONObject(i).getInt("favorites"));
                item.setFreshness(mJsonArray.getJSONObject(i).getInt("freshness"));
                item.setPrice(mJsonArray.getJSONObject(i).getInt("price"));
                item.setSalePrice(mJsonArray.getJSONObject(i).getInt("sale_price"));
                item.setState(mJsonArray.getJSONObject(i).getInt("state"));
                item.setThumbnail(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
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
                .inflate(R.layout.adapter_normal_product_hot_deal_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ProductManagementItem nature = mItems.get(i);
//        viewHolder.titleText.setText(Html.fromHtml(nature.getTitle()));
        viewHolder.titleText.setText("[" + nature.getAddress().split(" ")[0] + "][" + nature.getShop() + "]" + nature.getTitle());

        double mPrice = nature.getPrice()-(nature.getPrice()*(nature.getSalePrice()*0.01));

        viewHolder.priceDefaultText.setText(Util.toNumFormat(nature.getPrice()) + "원");
        viewHolder.priceText.setText(Util.toNumFormat((int) mPrice) + "원");
//        viewHolder.priceDefaultText.setText(Util.toNumFormat(nature.getDefaultPrice())+"원");
//        viewHolder.priceText.setText(Util.toNumFormat(nature.getPrice())+"원");
//        viewHolder.favoritesText.setText(Util.toNumFormat(nature.getRecommend()));

        viewHolder.discountText.setText(nature.getSalePrice() + "%");

        if(nature.getState() == 0){
            viewHolder.statusText.setBackgroundResource(R.mipmap.img_state_ing);
            viewHolder.statusText.setText("판매중");
        }else{
            viewHolder.statusText.setBackgroundResource(R.mipmap.img_state_end);
            if(nature.getState() == 1) {
                viewHolder.statusText.setText("재고없음");
            }else if(nature.getState() == 2) {
                viewHolder.statusText.setText("판매종료");
            }
        }

        if(nature.getThumbnail() != null) {
            viewHolder.thumbnailImg.setImageBitmap(nature.getThumbnail());
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
        public ImageView thumbnailImg;
        public TextView titleText, priceDefaultText, favoritesText, discountText, priceText, statusText;
        public RelativeLayout detailLayout, discountLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImg = itemView.findViewById(R.id.item_img);
            titleText = itemView.findViewById(R.id.item_title_text);
            statusText = itemView.findViewById(R.id.item_status_text);
            priceDefaultText = itemView.findViewById(R.id.item_default_price_text);
            discountText = itemView.findViewById(R.id.discount_text);
            priceText = itemView.findViewById(R.id.price_text);
            detailLayout = itemView.findViewById(R.id.first_layout);
        }
    }

}


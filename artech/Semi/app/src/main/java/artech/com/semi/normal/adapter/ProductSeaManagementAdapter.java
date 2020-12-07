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
import artech.com.semi.normal.fragment.ProductSeaManagementFragment;
import artech.com.semi.normal.item.ProductManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class ProductSeaManagementAdapter extends RecyclerView.Adapter<ProductSeaManagementAdapter.ViewHolder> {
    List<ProductManagementItem> mItems;
    ProductSeaManagementFragment mFragment;
    Context mContext;

    public ProductSeaManagementAdapter(ProductSeaManagementFragment fragment, Context context) {
        super();
        mFragment = fragment;
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray jsonArray) {
        ProductManagementItem item;
        for(int i = 0; i < jsonArray.length() ;i++) {
            item = new ProductManagementItem();
            try {
                item.setId(jsonArray.getJSONObject(i).getString("product_id"));
                item.setTitle(Html.fromHtml(jsonArray.getJSONObject(i).getString("product_name")).toString());
                item.setAddress(Html.fromHtml(jsonArray.getJSONObject(i).getString("address")).toString());
                item.setShop(Html.fromHtml(jsonArray.getJSONObject(i).getString("company")).toString());
                item.setContents(jsonArray.getJSONObject(i).getString("product_content"));
                item.setRecommand(jsonArray.getJSONObject(i).getInt("favorites"));
                item.setFreshness(jsonArray.getJSONObject(i).getInt("freshness"));
                item.setPrice(jsonArray.getJSONObject(i).getInt("price"));
                item.setSalePrice(jsonArray.getJSONObject(i).getInt("sale_price"));
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
                .inflate(R.layout.adapter_normal_product_sea_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ProductManagementItem nature = mItems.get(i);
//        viewHolder.titleText.setText(Html.fromHtml(nature.getTitle()));
        viewHolder.titleText.setText("[" + nature.getAddress().split(" ")[0] + "][" + nature.getShop() + "]" + nature.getTitle());
        viewHolder.contentsText.setText(Html.fromHtml(nature.getContents()));
//        viewHolder.priceDefaultText.setText(Util.toNumFormat(nature.getDefaultPrice())+"원");
//        viewHolder.priceText.setText(Util.toNumFormat(nature.getPrice())+"원");
        viewHolder.favoritesText.setText(Util.toNumFormat(nature.getRecommend()));


        double mPrice = nature.getPrice()-(nature.getPrice()*(nature.getSalePrice()*0.01));

        viewHolder.priceDefaultText.setText(Util.toNumFormat(nature.getPrice()) + "원");
        viewHolder.priceText.setText(Util.toNumFormat((int) mPrice) + "원");

        if(nature.getSalePrice() > 0) {
            viewHolder.discountText.setText(nature.getSalePrice() + "% 할인");
        }else {
            viewHolder.discountLayout.setVisibility(View.GONE);
            viewHolder.defaultLayout.setVisibility(View.GONE);
        }

        if(nature.getThumbnail() != null) {
            viewHolder.thumbnailImg.setImageBitmap(nature.getThumbnail());
        }

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

        if(nature.getFreshness() == 0) {
            viewHolder.rankingImg.setImageResource(R.mipmap.img_fresh_poor_2);
        }else if(nature.getFreshness() == 1) {
            viewHolder.rankingImg.setImageResource(R.mipmap.img_fresh_good_2);
        }else if(nature.getFreshness() == 2) {
            viewHolder.rankingImg.setImageResource(R.mipmap.img_fresh_excellent_2);
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
        public ImageView thumbnailImg, rankingImg;
        public TextView titleText, contentsText, priceDefaultText, favoritesText, discountText, priceText, statusText;
        public RelativeLayout detailLayout, discountLayout, defaultLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImg = itemView.findViewById(R.id.item_img);
            rankingImg = itemView.findViewById(R.id.item_rank_img);
            titleText = itemView.findViewById(R.id.item_title_text);
            statusText = itemView.findViewById(R.id.item_status_text);
            contentsText = itemView.findViewById(R.id.item_contents_text);
            priceDefaultText = itemView.findViewById(R.id.item_default_price_text);
            favoritesText = itemView.findViewById(R.id.favorites_text);
            discountText = itemView.findViewById(R.id.discount_text);
            priceText = itemView.findViewById(R.id.price_text);
            detailLayout = itemView.findViewById(R.id.first_layout);
            discountLayout = itemView.findViewById(R.id.discount_layout);
            defaultLayout = itemView.findViewById(R.id.item_default_layout);
        }
    }

}


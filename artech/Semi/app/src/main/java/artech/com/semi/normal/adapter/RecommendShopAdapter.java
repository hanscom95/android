package artech.com.semi.normal.adapter;

import android.content.Context;
import android.content.Intent;
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
import artech.com.semi.normal.ShopDetailActivity;
import artech.com.semi.normal.item.ProductManagementItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class RecommendShopAdapter extends RecyclerView.Adapter<RecommendShopAdapter.ViewHolder> {
    List<ProductManagementItem> mItems;
    Context mContext;

    public RecommendShopAdapter(Context context) {
        super();
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray jsonArray) {
        ProductManagementItem item;
        for(int i = 0; i < jsonArray.length() ;i++) {
            item = new ProductManagementItem();
            try {
                item.setId(jsonArray.getJSONObject(i).getString("id"));
                item.setTitle(jsonArray.getJSONObject(i).getString("company"));
                item.setContents(jsonArray.getJSONObject(i).getString("address"));
                item.setPrice(jsonArray.getJSONObject(i).getInt("product_cnt"));
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
                .inflate(R.layout.adapter_recommend_shop_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final ProductManagementItem nature = mItems.get(i);
        viewHolder.titleText.setText(Html.fromHtml(nature.getTitle()));
        viewHolder.contentsText.setText(Html.fromHtml(nature.getContents()));
//        viewHolder.priceDefaultText.setText(Util.toNumFormat(nature.getDefaultPrice())+"원");
//        viewHolder.priceText.setText(Util.toNumFormat(nature.getPrice())+"원");
//        viewHolder.favoritesText.setText(Util.toNumFormat(nature.getRecommend()));
//
//        if(nature.getSalePrice() > 0) {
//            viewHolder.discountText.setText(nature.getSalePrice() + "% 할인");
//        }else {
//            viewHolder.discountLayout.setVisibility(View.GONE);
//        }
//
        if(nature.getThumbnail() != null) {
            viewHolder.thumbnailImg.setImageBitmap(nature.getThumbnail());
        }

        if(nature.getPrice() > 0){
            viewHolder.rankingImg.setVisibility(View.VISIBLE);
        }else {
            viewHolder.rankingImg.setVisibility(View.GONE);
        }
//
//        if(nature.getFreshness() == 0) {
//            viewHolder.rankingImg.setImageResource(R.mipmap.img_fresh_poor_2);
//        }else if(nature.getFreshness() == 1) {
//            viewHolder.rankingImg.setImageResource(R.mipmap.img_fresh_good_2);
//        }else if(nature.getFreshness() == second) {
//            viewHolder.rankingImg.setImageResource(R.mipmap.img_fresh_excellent_2);
//        }
        viewHolder.detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
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
        public ImageView thumbnailImg, rankingImg;
        public TextView titleText, contentsText, priceDefaultText, favoritesText, discountText, priceText;
        public RelativeLayout detailLayout, discountLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImg = itemView.findViewById(R.id.item_img);
            rankingImg = itemView.findViewById(R.id.item_ranking_img);
            titleText = itemView.findViewById(R.id.item_name_text);
            contentsText = itemView.findViewById(R.id.item_contents_text);
//            priceDefaultText = (TextView) itemView.findViewById(R.id.item_default_price_text);
//            favoritesText = (TextView) itemView.findViewById(R.id.favorites_text);
//            discountText = (TextView) itemView.findViewById(R.id.discount_text);
//            priceText = (TextView) itemView.findViewById(R.id.price_text);
            detailLayout = itemView.findViewById(R.id.first_layout);
//            discountLayout = (RelativeLayout) itemView.findViewById(R.id.discount_layout);
        }
    }

}


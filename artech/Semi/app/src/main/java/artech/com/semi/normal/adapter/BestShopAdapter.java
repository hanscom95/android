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
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class BestShopAdapter extends RecyclerView.Adapter<BestShopAdapter.ViewHolder> {
    List<List<String>> mItems;
    Context mContext;

    public BestShopAdapter(Context context) {
        super();
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray jsonArray) {
        List<String> list;
        for(int i = 0; i < jsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
                list.add(Html.fromHtml(jsonArray.getJSONObject(i).getString("company")).toString());
                list.add(Html.fromHtml(jsonArray.getJSONObject(i).getString("address")).toString());
                list.add("0");
//                list.add(jsonArray.getJSONObject(i).getString("price"));
                list.add(jsonArray.getJSONObject(i).getString("picture"));
                list.add(jsonArray.getJSONObject(i).getString("id"));
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
                .inflate(R.layout.adapter_best_shop_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final List<String> nature = mItems.get(i);
        viewHolder.titleText.setText(nature.get(0));
        viewHolder.addressText.setText(nature.get(1));
        viewHolder.priceText.setText(Util.toNumFormat(Integer.parseInt(nature.get(2)))+"원");

        viewHolder.thumbnailImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(3)));

//        ProductManagementItem nature = mItems.get(i);
//        viewHolder.titleText.setText(Html.fromHtml(nature.getTitle()));
//        viewHolder.contentsText.setText(Html.fromHtml(nature.getContents()));
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
//        if(nature.getThumbnail() != null) {
//            viewHolder.thumbnailImg.setImageBitmap(nature.getThumbnail());
//        }
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
                intent.putExtra("id", nature.get(4));
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
        public TextView titleText, addressText, priceText;
        public RelativeLayout detailLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImg = itemView.findViewById(R.id.item_img);
            titleText = itemView.findViewById(R.id.item_title_text);
            addressText = itemView.findViewById(R.id.item_address_text);
            priceText = itemView.findViewById(R.id.item_price_text);
            detailLayout = itemView.findViewById(R.id.first_layout);
        }
    }

}


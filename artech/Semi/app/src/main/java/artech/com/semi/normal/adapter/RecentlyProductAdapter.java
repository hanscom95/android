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
import artech.com.semi.normal.ProductDetailActivity;
import artech.com.semi.normal.item.FavoritesItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class RecentlyProductAdapter extends RecyclerView.Adapter<RecentlyProductAdapter.ViewHolder> {
    List<FavoritesItem> mItems;
    Context mContext;

    public RecentlyProductAdapter(Context context) {
        super();
        mContext = context;

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray mJsonArray) {
        FavoritesItem nama;
        for(int i = 0; i < mJsonArray.length() ;i++) {
            nama = new FavoritesItem();
            try {
                nama.setId(mJsonArray.getJSONObject(i).getString("product_id"));
                nama.setAddress(Html.fromHtml(mJsonArray.getJSONObject(i).getString("address")).toString());
                nama.setTitle(Html.fromHtml(mJsonArray.getJSONObject(i).getString("product_name")).toString());
                nama.setContents(Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")).toString());
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
    public RecentlyProductAdapter.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_normal_product_favorites_edit_list_view, viewGroup, false);
        RecentlyProductAdapter.ViewHolder viewHolder = new RecentlyProductAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecentlyProductAdapter.ViewHolder viewHolder, final int i) {
        FavoritesItem nature = mItems.get(i);
//        viewHolder.titleText.setText(nature.getTitle());
        viewHolder.titleText.setText("[" + nature.getAddress().split(" ")[0] + "][" + nature.getContents() + "]" + nature.getTitle());
        viewHolder.subText.setText(nature.getContents());
        if(nature.getThumbnail() != null) {
            viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
        }



        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("id", mItems.get(i).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView titleText, subText;
        public CheckBox checkBox;
        public RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.item_title_text);
            subText = itemView.findViewById(R.id.item_subtitle_text);
            layout = itemView.findViewById(R.id.item_layout);
            imgThumbnail = itemView.findViewById(R.id.item_img);
            checkBox = itemView.findViewById(R.id.item_checkbox);
        }
    }

}

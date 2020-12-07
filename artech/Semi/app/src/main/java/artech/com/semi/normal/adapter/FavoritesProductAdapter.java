package artech.com.semi.normal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class FavoritesProductAdapter extends RecyclerView.Adapter<FavoritesProductAdapter.ViewHolder> {
    public List<FavoritesItem> mItems;
    Context mContext;

    public int visible = 0;
    public int checked = 0;

    public FavoritesProductAdapter(Context context) {
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
                nama.setFId(mJsonArray.getJSONObject(i).getString("favorites_id"));
                nama.setTitle(Html.fromHtml(mJsonArray.getJSONObject(i).getString("product_name")).toString());
                nama.setAddress(Html.fromHtml(mJsonArray.getJSONObject(i).getString("address")).toString());
                nama.setContents(Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")).toString());
                nama.setShop(Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")).toString());
                nama.setThumbnail(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
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
    public FavoritesProductAdapter.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_normal_product_favorites_edit_list_view, viewGroup, false);
        FavoritesProductAdapter.ViewHolder viewHolder = new FavoritesProductAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoritesProductAdapter.ViewHolder viewHolder, final int i) {
        final FavoritesItem nature = mItems.get(i);
        viewHolder.titleText.setText("[" + nature.getAddress().split(" ")[0] + "][" + nature.getShop() + "]" + nature.getTitle());
        viewHolder.subText.setText(nature.getContents());
        if(nature.getThumbnail() != null) {
            viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    nature.setChecked(1);
                }else {
                    nature.setChecked(0);
                }
            }
        });

//        viewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if(viewHolder.checkBox.getVisibility() == View.VISIBLE) {
//                    visible = 0;
//                }else {
//                    visible = 1;
//                }
//
//                notifyDataSetChanged();
//
//                if(visible == 0) {
//                    ((FavoritesActivity) mContext).setBottomVisibility(false);
//                }else {
//                    ((FavoritesActivity) mContext).setBottomVisibility(true);
//                }
//                return false;
//            }
//        });

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("id", mItems.get(i).getId());
                mContext.startActivity(intent);
            }
        });


        if(visible == 0) {
            viewHolder.checkBox.setVisibility(View.GONE);
        }else {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }

        if(checked == 0) {
            viewHolder.checkBox.setChecked(false);
        }else {
            viewHolder.checkBox.setChecked(true);
        }
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

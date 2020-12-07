package artech.com.semi.normal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    List<List<String>> mItems;
    Context mContext;

    public ReviewAdapter(Context context) {
        super();
        mContext = context;
    }


    public void addList(JSONArray jsonArray) {
        List<String> list;
        for(int i = 0; i < jsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
                list.add(jsonArray.getJSONObject(i).getString("review_id"));
                list.add(Html.fromHtml(jsonArray.getJSONObject(i).getString("name")).toString());
                list.add(Html.fromHtml(jsonArray.getJSONObject(i).getString("review")).toString());
                list.add(jsonArray.getJSONObject(i).getString("reg_dt"));
                list.add(jsonArray.getJSONObject(i).getString("point"));
                list.add(jsonArray.getJSONObject(i).getString("picture"));
                list.add(jsonArray.getJSONObject(i).getString("picture1"));
                list.add(jsonArray.getJSONObject(i).getString("picture2"));
                list.add(jsonArray.getJSONObject(i).getString("picture3"));
                mItems.add(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        mItems = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        LayoutInflater layout = LayoutInflater.from(viewGroup.getContext());
        View v = null;
        ViewHolder viewHolder = null;

        v = layout.inflate(R.layout.adapter_review_list, viewGroup, false);
        viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        List<String> nature = mItems.get(i);
        viewHolder.nameText.setText(nature.get(1));
        viewHolder.contentsText.setText(nature.get(2));
        viewHolder.regText.setText(nature.get(3).substring(0,10));


        int point = Integer.parseInt(nature.get(4));

        if(point == 1) {
            viewHolder.reviewFirstImg.setImageResource(R.mipmap.img_fish_press);
        }else if(point == 2) {
            viewHolder.reviewFirstImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewSecondImg.setImageResource(R.mipmap.img_fish_press);
        }else if(point == 3) {
            viewHolder.reviewFirstImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewSecondImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewThirdImg.setImageResource(R.mipmap.img_fish_press);
        }else if(point == 4) {
            viewHolder.reviewFirstImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewSecondImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewThirdImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewFourthImg.setImageResource(R.mipmap.img_fish_press);
        }else if(point == 5) {
            viewHolder.reviewFirstImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewSecondImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewThirdImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewFourthImg.setImageResource(R.mipmap.img_fish_press);
            viewHolder.reviewFifthImg.setImageResource(R.mipmap.img_fish_press);
        }

        if(nature.get(5) != "null") {
            viewHolder.imgThumbnail.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(5)));
        }


        if(nature.get(6) != "null") {
            viewHolder.firstImg.setVisibility(View.VISIBLE);
            viewHolder.firstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(6)));
        }
        if(nature.get(7) != "null") {
            viewHolder.secondImg.setVisibility(View.VISIBLE);
            viewHolder.secondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(7)));
        }
        if(nature.get(8) != "null") {
            viewHolder.thirdImg.setVisibility(View.VISIBLE);
            viewHolder.thirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(8)));
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail, reviewFirstImg, reviewSecondImg, reviewThirdImg, reviewFourthImg, reviewFifthImg, firstImg, secondImg, thirdImg;
        public TextView nameText, contentsText, regText, replyText;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.item_profile_img);

            firstImg = itemView.findViewById(R.id.item_first_img);
            secondImg = itemView.findViewById(R.id.item_second_img);
            thirdImg = itemView.findViewById(R.id.item_third_img);

            reviewFirstImg = itemView.findViewById(R.id.item_first_fish_img);
            reviewSecondImg = itemView.findViewById(R.id.item_second_fish_img);
            reviewThirdImg = itemView.findViewById(R.id.item_third_fish_img);
            reviewFourthImg = itemView.findViewById(R.id.item_fourth_fish_img);
            reviewFifthImg = itemView.findViewById(R.id.item_fifth_fish_img);

            nameText = itemView.findViewById(R.id.item_name);
            contentsText = itemView.findViewById(R.id.item_contents_text);
            regText = itemView.findViewById(R.id.item_time);
            replyText = itemView.findViewById(R.id.item_review_text);
        }
    }

}


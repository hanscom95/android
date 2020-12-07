package artech.com.semi.normal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.business.item.SaleManagementItem;
import artech.com.semi.normal.fragment.ProductBestManagementFragment;
import artech.com.semi.normal.fragment.ProductReviewFragment;
import artech.com.semi.normal.item.ProductReviewItem;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.ViewHolder> {
    List<ProductReviewItem> mItems;
    ProductReviewFragment mFragment;
    Context mContext;

    int mFlag = 0;

    public ProductReviewAdapter(ProductReviewFragment fragment, Context context) {
        super();
        mFragment = fragment;
        mContext = context;
    }


    public void addList(ArrayList<ProductReviewItem> arrayList) {
        mItems = arrayList;
    }

    public void clear() {
        mItems = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        LayoutInflater layout = LayoutInflater.from(viewGroup.getContext());
        View v = null;
        ViewHolder viewHolder = null;

        Log.d("ProductReview" , "onCreateViewHolder i  : " + i );

//        if(mItems.get(i).getFlag() == 0) {
//            v = layout.inflate(R.layout.adapter_review_chat_list_customer, viewGroup, false);
//        }else if(mItems.get(i).getFlag() == 1) {
//            v = layout.inflate(R.layout.adapter_review_chat_list_buyer, viewGroup, false);
//        }
        v = layout.inflate(R.layout.adapter_review_chat_list_customer, viewGroup, false);
        viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ProductReviewItem nature = mItems.get(i);
        if(nature.getFlag() == 0) {
            viewHolder.customerLayout.setVisibility(View.VISIBLE);
            viewHolder.nameText.setText(Html.fromHtml(nature.getName()));
            viewHolder.contentsText.setText(Html.fromHtml(nature.getContents()));
            viewHolder.regText.setText(nature.getDate().substring(0,10));

            if(nature.getThumbnail() != null) {
                viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
            }


            if(nature.getFirstImg() != null) {
                viewHolder.firstImg.setImageBitmap(nature.getFirstImg());

                if(nature.getSecondImg() != null) {
                    viewHolder.secondImg.setImageBitmap(nature.getSecondImg());

                    if(nature.getThirdImg() != null) {
                        viewHolder.thirdImg.setImageBitmap(nature.getThirdImg());
                    }else {
                        viewHolder.thirdImg.setVisibility(View.GONE);
                    }
                }else {
                    viewHolder.secondImg.setVisibility(View.GONE);
                    viewHolder.thirdImg.setVisibility(View.GONE);
                }
            }else {
                viewHolder.firstImg.setVisibility(View.GONE);
                viewHolder.secondImg.setVisibility(View.GONE);
                viewHolder.thirdImg.setVisibility(View.GONE);
            }

        }else if(nature.getFlag() == 1) {
            viewHolder.buyerLayout.setVisibility(View.VISIBLE);
            viewHolder.nameReplyText.setText(Html.fromHtml(nature.getName()));
            viewHolder.contentsReplyText.setText(Html.fromHtml(nature.getContents()));
            viewHolder.regReplyText.setText(nature.getDate().substring(0, 10));
        }
//        viewHolder.nameText.setText(nature.getName());
//        viewHolder.titleText.setText(nature.getTitle());
//        viewHolder.regText.setText("구매일 " + nature.getRegDate());
//        viewHolder.receiptText.setText(nature.getReceiptDate());
//        viewHolder.amountText.setText("총 "+nature.getAmmount() + "개");
//        if(nature.getThumbnail() != null) {
//            viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
//        }
//
//        viewHolder.detailLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mProductBestManagementFragment.setDetailClick(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail, firstImg, secondImg, thirdImg;
        public TextView nameText, contentsText, regText, replyText, nameReplyText, contentsReplyText, regReplyText;
        public RelativeLayout detailLayout, customerLayout, buyerLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.item_profile_img);
            firstImg = itemView.findViewById(R.id.item_first_img);
            secondImg = itemView.findViewById(R.id.item_second_img);
            thirdImg = itemView.findViewById(R.id.item_third_img);

            nameText = itemView.findViewById(R.id.item_name);
            contentsText = itemView.findViewById(R.id.item_contents_text);
            regText = itemView.findViewById(R.id.item_time);
            replyText = itemView.findViewById(R.id.item_review_text);

            nameReplyText = itemView.findViewById(R.id.buyer_item_name);
            contentsReplyText = itemView.findViewById(R.id.buyer_item_contents_text);
            regReplyText = itemView.findViewById(R.id.buyer_item_time);
//            detailLayout = (RelativeLayout) itemView.findViewById(R.id.detail_layout);

            customerLayout = itemView.findViewById(R.id.customer_layout);
            buyerLayout = itemView.findViewById(R.id.buyer_layout);
        }
    }

}


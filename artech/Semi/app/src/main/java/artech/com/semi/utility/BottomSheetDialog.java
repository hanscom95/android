package artech.com.semi.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import artech.com.semi.R;
import artech.com.semi.normal.BuyingCheckActivity;
import artech.com.semi.normal.item.ProductManagementItem;

/**
 * Created by moon on 2018-07-30.
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {

    static Context mContext;

    ImageView mPlusImg, mMinusImg, mCloseImg;
    TextView mTitleText, mValueText, mDefaultPriceText, mPriceText, mDiscountText;
    Button mBuyingButton;

    int mValue = 0;
    public String mId;
    double mPrice;

    static ProductManagementItem mItems;

    public static BottomSheetDialog getInstance(Context context, ProductManagementItem item) {
        mContext = context;
        mItems = item;
        return new BottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_bottom_product_select, container,false);

        mTitleText = view.findViewById(R.id.product_contents_text);
        mValueText = view.findViewById(R.id.item_value_text);
        mDefaultPriceText = view.findViewById(R.id.item_default_price_text);
        mPriceText = view.findViewById(R.id.item_price_text);
        mDiscountText = view.findViewById(R.id.item_discount_text);

        mPlusImg = view.findViewById(R.id.plus_img);
        mMinusImg = view.findViewById(R.id.minus_img);
        mCloseImg = view.findViewById(R.id.close_img);

        mBuyingButton = view.findViewById(R.id.buying_button);

        mPlusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mValue < 999) {
                    mValue++;
                    mValueText.setText(""+mValue);
                }
            }
        });

        mMinusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mValue > 0) {
                    mValue--;
                    mValueText.setText(""+mValue);
                }
            }
        });

        mCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mBuyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                if(mValue > 0) {
                    Intent intent = new Intent(getContext(), BuyingCheckActivity.class);
                    intent.putExtra("id", mId);
                    intent.putExtra("value", mValue);
                    intent.putExtra("freshness", mItems.getFreshness());
                    intent.putExtra("shop", mItems.getShop());
                    intent.putExtra("price1", mItems.getPrice());//예약금액
                    intent.putExtra("price2", (int) (mItems.getPrice() * (mItems.getSalePrice() * 0.01)));//할인금액
                    intent.putExtra("price3", (int) mPrice);//총 결제금액
                    intent.putExtra("title", mItems.getTitle());
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, "상품 갯수를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPrice = mItems.getPrice()-(mItems.getPrice()*(mItems.getSalePrice()*0.01));

        mTitleText.setText(mItems.getTitle());
        mPriceText.setText(Util.toNumFormat((int) mPrice) + "원");
        mDiscountText.setText(Util.toNumFormat(mItems.getSalePrice()) + "% 할인");
        mDefaultPriceText.setText(Util.toNumFormat(mItems.getPrice()) + "원");

        return view;
    }
}

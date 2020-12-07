package artech.com.semi.normal.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;

import artech.com.semi.R;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.Util;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class ProductDetailBuyingInfoFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    JSONArray mJsonArray;

    RelativeLayout mTermsLayout, mExpandTermsLayout;
    ExpandableRelativeLayout mExpandLayout;
    ProductDetailBuyingInfoFragment mFragment;
    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    public int state = -1;

    @SuppressLint("ValidFragment")
    public ProductDetailBuyingInfoFragment(Context context) {
        // Required empty public constructor
        mContext = context;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);
        state = 0;
    }
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProductDetailBuyingInfoFragment newInstance(Context context, int sectionNumber) {
        ProductDetailBuyingInfoFragment fragment = new ProductDetailBuyingInfoFragment(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflater = inflater;

        View view = inflater.inflate(R.layout.fragment_product_detail_buying_info, container, false);

        mFragment = this;

        mViewGroup = (ViewGroup) view;

        mTermsLayout = view.findViewById(R.id.second_layout);
        mExpandTermsLayout = view.findViewById(R.id.terms_title_layout);
        mExpandLayout = view.findViewById(R.id.expandableLayout);

        mTermsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTermsLayout.setVisibility(View.GONE);
                mExpandLayout.expand();
            }
        });

        mExpandTermsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandLayout.collapse();
                mTermsLayout.setVisibility(View.VISIBLE);
            }
        });

//        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mExpandLayout.move(mTermsLayout.getHeight(), 0, null);
//
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                    mTermsLayout.getViewTreeObserver().removeGlobalOnLayoutListener(mGlobalLayoutListener);
//                } else {
//                    mTermsLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
//                }
//            }
//        };
//        mTermsLayout.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

//        mSelectTask = new SelectTask(mDbManager.selectUserId());
//        mSelectTask.execute();

        return mViewGroup ;
    }

    public void setDetailClick(int i) {
        Log.i("SaleManagementFragment", "setDetailClick=====" + i);
        state = 1;
        View newView = mInflater.inflate(R.layout.fragment_sale_management_detail, mViewGroup, false);
        mViewGroup.removeAllViews();
        mViewGroup.addView(newView);

        TextView regDateText = newView.findViewById(R.id.purchase_date); //구매일
        ImageView purchaseImg = newView.findViewById(R.id.purchase_img); // 이미지
        TextView titleText = newView.findViewById(R.id.purchase_title); // 제목
        TextView orderText = newView.findViewById(R.id.order_number); // 주문번호
        TextView quantityText = newView.findViewById(R.id.quantity); // 수량
        TextView priceText = newView.findViewById(R.id.price); // 가격
        TextView reservationDateText = newView.findViewById(R.id.reservation_date_value); // 예약 날짜
        TextView reservationTimeText = newView.findViewById(R.id.reservation_time_value); // 예약 시간
        TextView reservationNameText = newView.findViewById(R.id.reservation_value); // 수령인
        TextView goodsValueText = newView.findViewById(R.id.goods_value); // 상품금액
        TextView optionValueText = newView.findViewById(R.id.option_value); // 옵션금액
        TextView discountValueText = newView.findViewById(R.id.discount_value); // 할인금액
        TextView pointValueText = newView.findViewById(R.id.point_value); // 포인트 사용금액
        TextView totalValueText = newView.findViewById(R.id.total_payment_value); // 총 결제금액

        Button cancelButton = newView.findViewById(R.id.cancel_button);
        Button successButton = newView.findViewById(R.id.success_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        try {
            regDateText.setText("구매일 " + mJsonArray.getJSONObject(i).getString("reg_dt").substring(0,10));
            purchaseImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
            titleText.setText(mJsonArray.getJSONObject(i).getString("product_name"));
            orderText.setText("주문번호 " + mJsonArray.getJSONObject(i).getString("number"));
            reservationDateText.setText(mJsonArray.getJSONObject(i).getString("receipt_dt").substring(0,10));
            reservationTimeText.setText(mJsonArray.getJSONObject(i).getString("receipt_dt").substring(11,19));
            reservationNameText.setText(mJsonArray.getJSONObject(i).getString("name"));
            quantityText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("quantity")) + "개");
            priceText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("price") )+ "원");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

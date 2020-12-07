package artech.com.semi.business.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.business.QuestionManagementReviewDetailActivity;
import artech.com.semi.business.adpater.QuestionManagementReviewAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class QuestionManagementReviewFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    SelectTask mSelectTask;
    JSONArray mJsonArray;

    RecyclerView mRecyclerView, mRecyclerSecondView;
    RecyclerView.LayoutManager mLayoutManager, mLayoutSecondManager;
    QuestionManagementReviewAdapter mAdapter, mSecondAdapter;
    QuestionManagementReviewFragment mFragment;
    RelativeLayout mNotSearchLayout;
    View mTotalLayout;

    TextView mInfoText;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    public int state = -1;

    @SuppressLint("ValidFragment")
    public QuestionManagementReviewFragment(Context context) {
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
    public static QuestionManagementReviewFragment newInstance(Context context, int sectionNumber) {
        QuestionManagementReviewFragment fragment = new QuestionManagementReviewFragment(context);
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

        View view = inflater.inflate(R.layout.fragment_question_product_management, container, false);

        mNotSearchLayout = view.findViewById(R.id.not_search_layout);
        mTotalLayout = view.findViewById(R.id.total_layout);
        mInfoText = view.findViewById(R.id.info_text);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerSecondView = view.findViewById(R.id.recycler_second_view);
        mRecyclerSecondView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),1);
        mLayoutSecondManager  = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerSecondView.setLayoutManager(mLayoutSecondManager);

        mFragment = this;

        mViewGroup = (ViewGroup) view;

        mSelectTask = new SelectTask(mDbManager.selectUserId());
        mSelectTask.execute();

        return mViewGroup ;
    }

    public void setDetailClick(int i) {
        Log.i("SaleManagementFragment", "setDetailClick=====" + i);
//        state = 1;
//        View newView = mInflater.inflate(R.layout.fragment_question_management_detail, mViewGroup, false);
//        mViewGroup.removeAllViews();
//        mViewGroup.addView(newView);

        try {
            Intent intent = new Intent(mContext, QuestionManagementReviewDetailActivity.class);
            intent.putExtra("id", mJsonArray.getJSONObject(i).getString("review_id"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        TextView regDateText = (TextView) newView.findViewById(R.id.purchase_date); //구매일
//        ImageView purchaseImg = (ImageView) newView.findViewById(R.id.purchase_img); // 이미지
//        TextView titleText = (TextView) newView.findViewById(R.id.purchase_title); // 제목
//        TextView orderText = (TextView) newView.findViewById(R.id.order_number); // 주문번호
//        TextView quantityText = (TextView) newView.findViewById(R.id.quantity); // 수량
//        TextView priceText = (TextView) newView.findViewById(R.id.price); // 가격
//        TextView reservationDateText = (TextView) newView.findViewById(R.id.reservation_date_value); // 예약 날짜
//        TextView reservationTimeText = (TextView) newView.findViewById(R.id.reservation_time_value); // 예약 시간
//        TextView reservationNameText = (TextView) newView.findViewById(R.id.reservation_value); // 수령인
//        TextView goodsValueText = (TextView) newView.findViewById(R.id.goods_value); // 상품금액
//        TextView optionValueText = (TextView) newView.findViewById(R.id.option_value); // 옵션금액
//        TextView discountValueText = (TextView) newView.findViewById(R.id.discount_value); // 할인금액
//        TextView pointValueText = (TextView) newView.findViewById(R.id.point_value); // 포인트 사용금액
//        TextView totalValueText = (TextView) newView.findViewById(R.id.total_payment_value); // 총 결제금액

//        Button cancelButton = (Button) newView.findViewById(R.id.cancel_button);
//        Button successButton = (Button) newView.findViewById(R.id.success_button);


//        try {
//            regDateText.setText("구매일 " + mJsonArray.getJSONObject(i).getString("reg_dt").substring(0,10));
//            purchaseImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
//            titleText.setText(mJsonArray.getJSONObject(i).getString("product_name"));
//            orderText.setText("주문번호 " + mJsonArray.getJSONObject(i).getString("number"));
//            reservationDateText.setText(mJsonArray.getJSONObject(i).getString("receipt_dt").substring(0,10));
//            reservationTimeText.setText(mJsonArray.getJSONObject(i).getString("receipt_dt").substring(11,19));
//            reservationNameText.setText(mJsonArray.getJSONObject(i).getString("name"));
//            quantityText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("quantity")) + "개");
//            priceText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("price") )+ "원");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        String id;

        SelectTask(String _id) {
            id = _id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", id);
                jsonObject.put("state", 0);
                connect = network.reviewMarketSelect(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;
            String path = null;

            if(success) {
                mJsonArray = network.mReviewArray;

                JSONArray firstJsonArray = new JSONArray();
//                JSONArray secondJsonArray = new JSONArray();

//                for(int i = 0; i < mJsonArray.length(); i++) {
//                    try {
//                        if(mJsonArray.getJSONObject(i).getInt("answer") > 0) {
//                            secondJsonArray.put(mJsonArray.getJSONObject(i));
//                        }else {
//                            firstJsonArray.put(mJsonArray.getJSONObject(i));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }

                mInfoText.setText(mJsonArray.length() + "개의 문의글이 있습니다.");
                for(int i = 0; i < mJsonArray.length(); i++) {
                    try {
                        firstJsonArray.put(mJsonArray.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mAdapter = new QuestionManagementReviewAdapter(mFragment, mContext, 0);
//                mSecondAdapter = new QuestionManagementReviewAdapter(mFragment, mContext, 1);
                mAdapter.clear();
//                mSecondAdapter.clear();
                mAdapter.addList(firstJsonArray);
//                mSecondAdapter.addList(secondJsonArray);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerSecondView.setAdapter(mSecondAdapter);
//                Log.d("BusinessMainActivity", "secondJsonArray : " + secondJsonArray.toString());
                Log.d("BusinessMainActivity", "firstJsonArray : " + firstJsonArray.toString());
                //                setDecoded64ImageStringFromBitmap(path);

                if(mJsonArray.length() > 0) {
                    mNotSearchLayout.setVisibility(View.GONE);
                    mTotalLayout.setVisibility(View.VISIBLE);
                }else {
                    mTotalLayout.setVisibility(View.GONE);
                    mNotSearchLayout.setVisibility(View.VISIBLE);
                }

            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
                mTotalLayout.setVisibility(View.GONE);
                mNotSearchLayout.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }
}

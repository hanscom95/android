package artech.com.semi.business.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.business.SaleManagementActivity;
import artech.com.semi.business.adpater.SaleManagementSuccessAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class SaleManagementSuccessFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;

    SelectTask mSelectTask;
    UpdateTask mUpdateTask;

    JSONArray mJsonArray;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    static SaleManagementSuccessAdapter mAdapter;
    static SaleManagementSuccessFragment mFragment;

    RelativeLayout mNotSearchLayout, mCancelLayout, mRemoveLayout;
    TextView mInfoText;
    static LinearLayout mBottomLayout;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    public int state = -1;

    @SuppressLint("ValidFragment")
    public SaleManagementSuccessFragment(Context context) {
        // Required empty public constructor
        mContext = context;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);
    }
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SaleManagementSuccessFragment newInstance(Context context, int sectionNumber) {
        SaleManagementSuccessFragment fragment = new SaleManagementSuccessFragment(context);
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

        View view = inflater.inflate(R.layout.fragment_sale_management_success, container, false);

        mNotSearchLayout = view.findViewById(R.id.not_search_layout);
        mBottomLayout = view.findViewById(R.id.checkbox_bottom_layout);
        mCancelLayout = view.findViewById(R.id.cancel_layout);
        mRemoveLayout = view.findViewById(R.id.remove_layout);

        mInfoText = view.findViewById(R.id.info_text);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAdapter != null) {
                    mAdapter.visible = 0;
                    mAdapter.checked = 0;
                    mAdapter.notifyDataSetChanged();
                }

                setBottomVisibility(false);
            }
        });

        mRemoveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String item = "";
                    for(int i = 0; i < mAdapter.mItems.size(); i++) {
                        if(mAdapter.mItems.get(i).getChecked() == 1) {
                            if(item.length() > 0) {
                                item += ", '" + mAdapter.mItems.get(i).getId() + "'";
                            }else {
                                item += "'" + mAdapter.mItems.get(i).getId() + "'";
                            }
                        }
                    }

                    Log.d("mRemoveLayout", "item : " + item);


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id" ,item);
                    jsonObject.put("state" ,6);

                    Log.d("SaleManagementSuccessFragment", "json : " + jsonObject.toString());
                    mUpdateTask = new UpdateTask(jsonObject);
                    mUpdateTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setBottomVisibility(false);
            }
        });

        mFragment = this;

        mViewGroup = (ViewGroup) view;

        mSelectTask = new SelectTask(mDbManager.selectUserId());
        mSelectTask.execute();

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
//                try{
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("user_id", mDbManager.selectUserId());
//                    IamportUpdateTask mIamportUpdateTask = new IamportUpdateTask(jsonObject);
//                    mIamportUpdateTask.execute();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("id",  mJsonArray.getJSONObject(0).getString("number"));
//                    jsonObject.put("state", 1);
//
//                    mUpdateTask = new UpdateTask(jsonObject);
//                    mUpdateTask.execute();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        try {
            regDateText.setText("구매일 " + mJsonArray.getJSONObject(i).getString("reg_dt").substring(0,10));
            purchaseImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
            titleText.setText(Html.fromHtml(mJsonArray.getJSONObject(i).getString("product_name")).toString());
            orderText.setText("주문번호 " + mJsonArray.getJSONObject(i).getString("number"));
            reservationDateText.setText(mJsonArray.getJSONObject(i).getString("receipt_dt").substring(0,10));
            reservationTimeText.setText(mJsonArray.getJSONObject(i).getString("receipt_dt").substring(11,16));
            reservationNameText.setText(Html.fromHtml(mJsonArray.getJSONObject(i).getString("reservation_name")).toString());
            quantityText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("quantity")) + "개");
            priceText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("price") )+ "원");
            goodsValueText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("product_price") * mJsonArray.getJSONObject(i).getInt("quantity"))+ "원");
            discountValueText.setText(Util.toNumFormat((mJsonArray.getJSONObject(i).getInt("product_price") * mJsonArray.getJSONObject(i).getInt("quantity")) - mJsonArray.getJSONObject(i).getInt("price"))+ "원");
            pointValueText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("point") )+ "원");
            totalValueText.setText(Util.toNumFormat(mJsonArray.getJSONObject(i).getInt("price") )+ "원");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setCheckboxVisible() {
        if(mAdapter != null) {
            mAdapter.visible = 1;
            mAdapter.notifyDataSetChanged();
        }
    }

    public static void setAllChecked() {
        if(mAdapter != null) {
            if (mAdapter.checked == 0) {
                mAdapter.checked = 1;
            } else {
                mAdapter.checked = 0;
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public static void setBottomVisibility(boolean visible) {
        if(visible) {
            mBottomLayout.setVisibility(View.VISIBLE);
        }else {
            mBottomLayout.setVisibility(View.GONE);
        }
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
                jsonObject.put("state", 1);
                jsonObject.put("flag", 1); // 판매자
                connect = network.purchaseSelect(jsonObject);
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
                mJsonArray = network.mPurchaseArray;
                Log.d("salesuccessfragment", " mJsonArray : " + mJsonArray.toString());


//                Log.d("BusinessMainActivity", "mAdminTask : " + mJsonArray.toString());
                //                setDecoded64ImageStringFromBitmap(path);

                if(mJsonArray.length() > 0) {

                    mAdapter = new SaleManagementSuccessAdapter(mFragment, mContext);
                    mAdapter.clear();
                    mAdapter.addList(mJsonArray);
                    mRecyclerView.setAdapter(mAdapter);

                    mInfoText.setText("판매완료 내역 총 " + mJsonArray.length() + "건");

                    mNotSearchLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNotSearchLayout.setVisibility(View.VISIBLE);
                }
            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
                mRecyclerView.setVisibility(View.GONE);
                mNotSearchLayout.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }


    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        UpdateTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.purchaseRemoveUpdate(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;
            String path = null;

            if(success) {
                mAdapter.visible = 0;
                mAdapter.checked = 0;

                mSelectTask = new SelectTask(mDbManager.selectUserId());
                mSelectTask.execute();
                Log.d("BusinessMainActivity", "success mAdminTask : " + network.mResult);
            }else {
                mRecyclerView.setVisibility(View.GONE);
                mNotSearchLayout.setVisibility(View.VISIBLE);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
        }
    }
}

package artech.com.semi.normal.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.normal.ProductDetailActivity;
import artech.com.semi.normal.adapter.ProductReviewAdapter;
import artech.com.semi.normal.item.ProductReviewItem;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class ProductReviewFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    SelectTask mSelectTask;
    InsertTask mInsertTask;
    JSONArray mJsonArray;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProductReviewAdapter mAdapter;
    ProductReviewFragment mFragment;
    RelativeLayout mNotSearchLayout;
    EditText mReviewEdit;
    TextView mReviewInsertText;
    ImageView mCameraImg;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    public int state = -1;
    static String mId;

    @SuppressLint("ValidFragment")
    public ProductReviewFragment(Context context) {
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
    public static ProductReviewFragment newInstance(Context context, int sectionNumber, String id) {
        ProductReviewFragment fragment = new ProductReviewFragment(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        mId = id;
        return fragment;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflater = inflater;

        View view = inflater.inflate(R.layout.fragment_product_review, container, false);
//        mNotSearchLayout = (RelativeLayout) view.findViewById(R.id.not_search_layout);

        mReviewEdit = view.findViewById(R.id.review_edit);
        mReviewInsertText = view.findViewById(R.id.review_insert_text);
        mCameraImg = view.findViewById(R.id.camera_img);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFragment = this;

        mViewGroup = (ViewGroup) view;

        mCameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }

                ((ProductDetailActivity)mContext).selectGallery(0);
            }
        });

        mReviewInsertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mReviewEdit.getText().toString() == "" ||"".equals(mReviewEdit.getText().toString())) {
                    Toast.makeText(mContext, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("product_id", mId);
                    jsonObject.put("user_id", mDbManager.selectUserId());
                    jsonObject.put("content", mReviewEdit.getText().toString());
                    jsonObject.put("state", 0);

                    ArrayList<String> imgArrayList = ((ProductDetailActivity)mContext).mImgArrayList;
                    jsonObject.put("picture_length", imgArrayList.size());
                    for(int i = 0; i < imgArrayList.size(); i++) {
                        jsonObject.put("picture"+(i+1), imgArrayList.get(i));
                    }

                    mInsertTask = new InsertTask(jsonObject);
                    mInsertTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mId);

            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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


    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        SelectTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.reviewSelect(jsonObject);
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

                ArrayList<ProductReviewItem> items = new ArrayList<>();

                for(int i = 0; i < mJsonArray.length(); i++) {
                    try {
                        ProductReviewItem item = new ProductReviewItem();
                        item.setId(mJsonArray.getJSONObject(i).getString("review_id"));
                        item.setName(mJsonArray.getJSONObject(i).getString("review_name"));
                        item.setTitle(mJsonArray.getJSONObject(i).getString("review_title"));
                        item.setContents(mJsonArray.getJSONObject(i).getString("review_content"));
                        item.setDate(mJsonArray.getJSONObject(i).getString("review_reg_dt"));
                        item.setFlag(0);

                        String picture = mJsonArray.getJSONObject(i).getString("picture");
//                        if(picture != null || picture != "") {
                        if(picture != "null") {
                            item.setThumbnail(Util.setDecoded64ImageStringFromBitmap(picture));
                        }

                        if(mJsonArray.getJSONObject(i).getString("picture1") != "null") {
                            item.setFirstImg(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture1")));
                        }

                        if(mJsonArray.getJSONObject(i).getString("picture2") != "null") {
                            item.setSecondImg(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture2")));
                        }

                        if(mJsonArray.getJSONObject(i).getString("picture3") != "null") {
                            item.setThirdImg(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture3")));
                        }

                        if(items.size() > 0) {
                            if (items.get(items.size() - 1).getFlag() == 1 || (items.get(items.size() - 1).getFlag() == 0 && items.get(items.size() - 1).getId() != item.getId())) {
                                items.add(item);
                            }
                        }else {
                            items.add(item);
                        }

                        int cnt = mJsonArray.getJSONObject(i).getInt("cnt");

                        if(cnt > 0 && (mJsonArray.getJSONObject(i).getString("reply_id") != null || mJsonArray.getJSONObject(i).getString("reply_id") != "")) {
                            int j  = 0;
                            for(int k = cnt; k > 0; k--) {
                                item = new ProductReviewItem();
                                item.setId(mJsonArray.getJSONObject(i+j).getString("reply_id"));
                                item.setName(mJsonArray.getJSONObject(i+j).getString("reply_name"));
                                item.setTitle(mJsonArray.getJSONObject(i+j).getString("reply_title"));
                                item.setContents(mJsonArray.getJSONObject(i+j).getString("reply_content"));
                                item.setDate(mJsonArray.getJSONObject(i+j).getString("reply_reg_dt"));
                                item.setFlag(1);

                                items.add(item);
                                j++;
                            }

                            i = cnt-1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for(int j = 0; j < items.size(); j++) {
                    Log.d("ProductReviewFragment", "id : " + items.get(j).getId() + " / name : " + items.get(j).getName() + " / title : " + items.get(j).getTitle()+ " / flag : " + items.get(j).getFlag());
                }

                mAdapter = new ProductReviewAdapter(mFragment, mContext);
                mAdapter.clear();
                mAdapter.addList(items);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("BusinessMainActivity", "mAdminTask : " + mJsonArray.toString());
                //                setDecoded64ImageStringFromBitmap(path);

                /*if(mJsonArray.length() > 0) {
                    mNotSearchLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNotSearchLayout.setVisibility(View.VISIBLE);
                }*/
            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
//                mRecyclerView.setVisibility(View.GONE);
//                mNotSearchLayout.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }

    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        InsertTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.reviewProductInsert(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertTask = null;
            String path = null;

            if(success) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mId);
                    mReviewEdit.setText("");
                    if(((ProductDetailActivity)mContext).mImgArrayList.size() > 0)
                        ((ProductDetailActivity)mContext).mImgArrayList.clear();


                    mSelectTask = new SelectTask(jsonObject);
                    mSelectTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mInsertTask = null;
        }
    }
}

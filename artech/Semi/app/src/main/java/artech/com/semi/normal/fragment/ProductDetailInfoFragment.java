package artech.com.semi.normal.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class ProductDetailInfoFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    JSONObject mJsonObject;

    ProductDetailInfoFragment mFragment;

    TextView mInfoText, mNameText, mPriceText, mAmountText, mUnitText, mSaleText, mFreshnessText, mOriginText;
    ImageView mFirstImg, mSecondImg, mThirdImg;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    SelectTask mSelectTask;

    public int state = -1;
    String mId;

    @SuppressLint("ValidFragment")
    public ProductDetailInfoFragment(Context context, String id) {
        // Required empty public constructor
        mContext = context;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);
        state = 0;
        mId = id;
    }
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProductDetailInfoFragment newInstance(Context context, int sectionNumber, String id) {
        ProductDetailInfoFragment fragment = new ProductDetailInfoFragment(context, id);
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

        View view = inflater.inflate(R.layout.fragment_product_detail_info, container, false);

        mFragment = this;

        mViewGroup = (ViewGroup) view;

        mInfoText = view.findViewById(R.id.info_text);
        mNameText = view.findViewById(R.id.name_text);
        mPriceText = view.findViewById(R.id.price_text);
        mAmountText = view.findViewById(R.id.amount_text);
        mUnitText = view.findViewById(R.id.unit_text);
        mSaleText = view.findViewById(R.id.sale_text);
        mFreshnessText = view.findViewById(R.id.freshness_text);
        mOriginText = view.findViewById(R.id.origin_text);

        mFirstImg = view.findViewById(R.id.first_img);
        mSecondImg = view.findViewById(R.id.second_img);
        mThirdImg = view.findViewById(R.id.third_img);


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

    public void setDetailClick(JSONObject jsonObject) {
        try {
            mInfoText.setText(Html.fromHtml(jsonObject.getString("product_content").replace("\n", "<br>")).toString());
            mFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(jsonObject.get("picture1").toString()));
            mSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(jsonObject.get("picture2").toString()));
            mThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(jsonObject.get("picture3").toString()));
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

                connect = network.productNormalItemDetailSelect(jsonObject);
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
                try {
                    mJsonObject = network.mProductArray.getJSONObject(0);

                    mInfoText.setText(Html.fromHtml(mJsonObject.getString("product_content")).toString());
                    mNameText.setText(Html.fromHtml(mJsonObject.getString("product_name")).toString());
                    mPriceText.setText(Util.toNumFormat(mJsonObject.getInt("price")) + "원");
                    mAmountText.setText(Util.toNumFormat(mJsonObject.getInt("quantity")) + "개");
                    mUnitText.setText(Html.fromHtml(mJsonObject.getString("first_unit")).toString());
                    mSaleText.setText(mJsonObject.getString("sale_price") + "%");

                    if(mJsonObject.getInt("freshness") == 0) {
                        mFreshnessText.setText("하");
                    }else if(mJsonObject.getInt("freshness") == 1) {
                        mFreshnessText.setText("중");
                    }else if(mJsonObject.getInt("freshness") == 2) {
                        mFreshnessText.setText("상");
                    }
                    mOriginText.setText(Html.fromHtml(mJsonObject.getString("origin")).toString());

                    if(mJsonObject.getString("picture1") != "null") {
                        mFirstImg.setVisibility(View.VISIBLE);
                        mFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("picture1")));
                    }

                    if(mJsonObject.getString("picture2") != "null") {
                        mSecondImg.setVisibility(View.VISIBLE);
                        mSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("picture2")));
                    }

                    if(mJsonObject.getString("picture3") != "null") {
                        mThirdImg.setVisibility(View.VISIBLE);
                        mThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("picture3")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("ProdcutDetailinfoFragment", "mJsonObject : " + mJsonObject.toString());
                //                setDecoded64ImageStringFromBitmap(path);

            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }
}

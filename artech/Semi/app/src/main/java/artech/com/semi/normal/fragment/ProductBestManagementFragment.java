package artech.com.semi.normal.fragment;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.business.adpater.SaleManagementAdapter;
import artech.com.semi.normal.ProductDetailActivity;
import artech.com.semi.normal.adapter.ProductBestManagementAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class ProductBestManagementFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    SelectTask mSelectTask;
    JSONArray mJsonArray;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProductBestManagementAdapter mAdapter;
    ProductBestManagementFragment mFragment;
    RelativeLayout mNotSearchLayout, mTopBannerLayout;
    RadioGroup mRadioGroup;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    public int state = -1;

    @SuppressLint("ValidFragment")
    public ProductBestManagementFragment(Context context) {
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
    public static ProductBestManagementFragment newInstance(Context context, int sectionNumber) {
        ProductBestManagementFragment fragment = new ProductBestManagementFragment(context);
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

        View view = inflater.inflate(R.layout.fragment_normal_product_best_management, container, false);

        mNotSearchLayout = view.findViewById(R.id.not_search_layout);
        mTopBannerLayout = view.findViewById(R.id.top_banner_layout);

        mRadioGroup = view.findViewById(R.id.product_management_radio_group);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFragment = this;

        mViewGroup = (ViewGroup) view;

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int flag = -1;
                if(i == R.id.total_radio) {
                    flag = 0;
                    mTopBannerLayout.setVisibility(View.GONE);
                }else if(i == R.id.top_radio) {
                    flag = 1;
                    mTopBannerLayout.setVisibility(View.VISIBLE);
                }else if(i == R.id.freshness_radio) {
                    flag = 2;
                    mTopBannerLayout.setVisibility(View.GONE);
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("flag", flag);

                    mSelectTask = new SelectTask(jsonObject);
                    mSelectTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("flag", 0);

            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        mAdapter = new ProductBestManagementAdapter(mFragment, mContext);
//        mAdapter.clear();
//        mAdapter.addList(mJsonArray);
//        mRecyclerView.setAdapter(mAdapter);

        return mViewGroup ;
    }

    public void setDetailClick(int i) {
        Log.i("SaleManagementFragment", "setDetailClick=====" + i);

        try {
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("id", mJsonArray.getJSONObject(i).getString("product_id"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject mJsonObject;

        SelectTask(JSONObject jsonObject) {
            mJsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.productNormalBestSelect(mJsonObject);
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
                mJsonArray = network.mProductArray;

                mAdapter = new ProductBestManagementAdapter(mFragment, mContext);
                mAdapter.clear();
                mAdapter.addList(mJsonArray);
                try {
                    mAdapter.mFlag = mJsonObject.getInt("flag");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecyclerView.setAdapter(mAdapter);
                Log.d("BusinessMainActivity", "mAdminTask : " + mJsonArray.toString());
                //                setDecoded64ImageStringFromBitmap(path);

                if(mJsonArray.length() > 0) {
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
}

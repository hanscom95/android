package artech.com.semi.normal.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.ProductBestManagementAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class ProductDetailQuestionFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    SelectTask mSelectTask;
    JSONArray mJsonArray;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProductBestManagementAdapter mAdapter;
    ProductDetailQuestionFragment mFragment;
    RelativeLayout mNotSearchLayout;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    public int state = -1;

    @SuppressLint("ValidFragment")
    public ProductDetailQuestionFragment(Context context) {
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
    public static ProductDetailQuestionFragment newInstance(Context context, int sectionNumber) {
        ProductDetailQuestionFragment fragment = new ProductDetailQuestionFragment(context);
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

        View view = inflater.inflate(R.layout.fragment_product_question, container, false);

        mFragment = this;

        mViewGroup = (ViewGroup) view;

//        mSelectTask = new SelectTask(mDbManager.selectUserId());
//        mSelectTask.execute();

        return mViewGroup ;
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
                mJsonArray = network.mProductArray;

//                mAdapter = new ProductBestManagementAdapter(mFragment, mContext);
                mAdapter.clear();
                mAdapter.addList(mJsonArray);
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

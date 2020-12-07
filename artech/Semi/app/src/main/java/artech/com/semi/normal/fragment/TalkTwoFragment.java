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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.TalkDetailActivity;
import artech.com.semi.normal.adapter.TalkAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class TalkTwoFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    SelectTask mSelectTask;
    JSONArray mJsonArray;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    TalkAdapter mAdapter;
    TalkTwoFragment mFragment;
    RelativeLayout mNotSearchLayout, mTotalLayout, mSeaLayout, mFreshWaterLayout;
    TextView mTotalText, mSeaText, mFreshWaterText;
    ImageView mTotalImg, mSeaImg, mFreshWaterImg;
    RadioGroup mRadioGroup;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    private int mState = -1;
    private int mFlag = -1;

    @SuppressLint("ValidFragment")
    public TalkTwoFragment(Context context) {
        // Required empty public constructor
        mContext = context;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);
        mFlag = 0;
    }
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TalkTwoFragment newInstance(Context context, int sectionNumber) {
        TalkTwoFragment fragment = new TalkTwoFragment(context);
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

        View view = inflater.inflate(R.layout.fragment_talk_tab_two, container, false);

        mTotalLayout = view.findViewById(R.id.total_layout);
        mSeaLayout = view.findViewById(R.id.sea_layout);
        mFreshWaterLayout = view.findViewById(R.id.fresh_water_layout);

        mTotalText = view.findViewById(R.id.total_text);
        mSeaText = view.findViewById(R.id.sea_text);
        mFreshWaterText = view.findViewById(R.id.fresh_water_text);

        mTotalImg = view.findViewById(R.id.total_img);
        mSeaImg = view.findViewById(R.id.sea_img);
        mFreshWaterImg = view.findViewById(R.id.fresh_water_img);

        mTotalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = -1;

                mSeaImg.setVisibility(View.INVISIBLE);
                mFreshWaterImg.setVisibility(View.INVISIBLE);

                mSeaText.setTextColor(mContext.getColor(R.color.warm_grey));
                mFreshWaterText.setTextColor(mContext.getColor(R.color.warm_grey));

                mTotalText.setTextColor(mContext.getColor(R.color.black));
                mTotalImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mSeaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = 0;

                mTotalImg.setVisibility(View.INVISIBLE);
                mFreshWaterImg.setVisibility(View.INVISIBLE);

                mTotalText.setTextColor(mContext.getColor(R.color.warm_grey));
                mFreshWaterText.setTextColor(mContext.getColor(R.color.warm_grey));

                mSeaText.setTextColor(mContext.getColor(R.color.black));
                mSeaImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mFreshWaterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = 1;

                mTotalImg.setVisibility(View.INVISIBLE);
                mSeaImg.setVisibility(View.INVISIBLE);

                mTotalText.setTextColor(mContext.getColor(R.color.warm_grey));
                mSeaText.setTextColor(mContext.getColor(R.color.warm_grey));

                mFreshWaterText.setTextColor(mContext.getColor(R.color.black));
                mFreshWaterImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFragment = this;


        mRadioGroup = (RadioGroup)view.findViewById(R.id.sort_radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.one_sort_radio:
                        mFlag = 0;
                        mAdapter.mFlag = mFlag;
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id" , mDbManager.selectUserId());
                            jsonObject.put("notice" , 1);
                            jsonObject.put("category" , mState);


                            mSelectTask = new SelectTask(jsonObject, mFlag);
                            mSelectTask.execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case R.id.two_sort_radio:
                        mFlag = 1;
                        mAdapter.mFlag = mFlag;
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id" , mDbManager.selectUserId());
                            jsonObject.put("notice" , 1);
                            jsonObject.put("category" , mState);


                            mSelectTask = new SelectTask(jsonObject, mFlag);
                            mSelectTask.execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        break;
                }
            }
        });

        mViewGroup = (ViewGroup) view;
        mAdapter = new TalkAdapter(mFragment, mContext, mFlag);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id" , mDbManager.selectUserId());
            jsonObject.put("notice" , 1);
            jsonObject.put("category" , mState);


            mSelectTask = new SelectTask(jsonObject, mFlag);
            mSelectTask.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mViewGroup ;
    }

    public void setDetailClick(int i) {
        Log.i("SaleManagementFragment", "setDetailClick=====" + i);

        try {
            Intent intent = new Intent(mContext, TalkDetailActivity.class);
            intent.putExtra("talk_id", mJsonArray.getJSONObject(i).toString());
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void menuSelect() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id" , mDbManager.selectUserId());
            jsonObject.put("notice" , 1);
            jsonObject.put("category" , mState);


            mSelectTask = new SelectTask(jsonObject, mFlag);
            mSelectTask.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        int flag;

        SelectTask(JSONObject jsonObject, int flag) {
            this.jsonObject = jsonObject;
            this.flag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.talkSelect(jsonObject);
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


            mLayoutManager  = new GridLayoutManager(getActivity(),flag+1);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter.clear();

            if(success) {
                mJsonArray = network.mTalkArray;
                mAdapter.addList(mJsonArray);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("BusinessMainActivity", "mAdminTask : " + mJsonArray.toString());
                //                setDecoded64ImageStringFromBitmap(path);

            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
                mRecyclerView.setAdapter(mAdapter);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }
}

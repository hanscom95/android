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
public class TalkThreeFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    SelectTask mSelectTask;
    JSONArray mJsonArray;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    TalkAdapter mAdapter;
    TalkThreeFragment mFragment;
    RelativeLayout mNotSearchLayout, mTotalLayout, mExpeditionLayout, mPointLayout, mProductLayout, mMethodLayout, mEtcLayout;
    TextView mTotalText, mExpeditionText, mPointText, mProductText, mMethodText, mEtcText;
    ImageView mTotalImg, mExpeditionImg, mPointImg, mProductImg, mMethodImg, mEtcImg;
    RadioGroup mRadioGroup;

    ViewGroup mViewGroup;
    LayoutInflater mInflater;

    private int mState = -1;
    private int mFlag = -1;

    @SuppressLint("ValidFragment")
    public TalkThreeFragment(Context context) {
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
    public static TalkThreeFragment newInstance(Context context, int sectionNumber) {
        TalkThreeFragment fragment = new TalkThreeFragment(context);
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

        View view = inflater.inflate(R.layout.fragment_talk_tab_three, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mTotalLayout = view.findViewById(R.id.total_layout);
        mExpeditionLayout = view.findViewById(R.id.expedition_layout);
        mPointLayout = view.findViewById(R.id.point_layout);
        mProductLayout = view.findViewById(R.id.product_layout);
        mMethodLayout = view.findViewById(R.id.method_layout);
        mEtcLayout = view.findViewById(R.id.etc_layout);


        mTotalText = view.findViewById(R.id.total_text);
        mExpeditionText = view.findViewById(R.id.expedition_text);
        mPointText = view.findViewById(R.id.point_text);
        mProductText = view.findViewById(R.id.product_text);
        mMethodText = view.findViewById(R.id.method_text);
        mEtcText = view.findViewById(R.id.etc_text);

        mTotalImg = view.findViewById(R.id.total_img);
        mExpeditionImg = view.findViewById(R.id.expedition_img);
        mPointImg = view.findViewById(R.id.point_img);
        mProductImg = view.findViewById(R.id.product_img);
        mMethodImg = view.findViewById(R.id.method_img);
        mEtcImg = view.findViewById(R.id.etc_img);



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
                            jsonObject.put("notice" , 2);
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
                            jsonObject.put("notice" , 2);
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

        mTotalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = -1;
                mExpeditionImg.setVisibility(View.INVISIBLE);
                mPointImg.setVisibility(View.INVISIBLE);
                mProductImg.setVisibility(View.INVISIBLE);
                mMethodImg.setVisibility(View.INVISIBLE);
                mEtcImg.setVisibility(View.INVISIBLE);

                mExpeditionText.setTextColor(mContext.getColor(R.color.warm_grey));
                mPointText.setTextColor(mContext.getColor(R.color.warm_grey));
                mProductText.setTextColor(mContext.getColor(R.color.warm_grey));
                mMethodText.setTextColor(mContext.getColor(R.color.warm_grey));
                mEtcText.setTextColor(mContext.getColor(R.color.warm_grey));

                mTotalText.setTextColor(mContext.getColor(R.color.black));
                mTotalImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mExpeditionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = 0;
                mTotalImg.setVisibility(View.INVISIBLE);
                mPointImg.setVisibility(View.INVISIBLE);
                mProductImg.setVisibility(View.INVISIBLE);
                mMethodImg.setVisibility(View.INVISIBLE);
                mEtcImg.setVisibility(View.INVISIBLE);

                mTotalText.setTextColor(mContext.getColor(R.color.warm_grey));
                mPointText.setTextColor(mContext.getColor(R.color.warm_grey));
                mProductText.setTextColor(mContext.getColor(R.color.warm_grey));
                mMethodText.setTextColor(mContext.getColor(R.color.warm_grey));
                mEtcText.setTextColor(mContext.getColor(R.color.warm_grey));


                mExpeditionText.setTextColor(mContext.getColor(R.color.black));
                mExpeditionImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mPointLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = 1;

                mTotalImg.setVisibility(View.INVISIBLE);
                mExpeditionImg.setVisibility(View.INVISIBLE);
                mProductImg.setVisibility(View.INVISIBLE);
                mMethodImg.setVisibility(View.INVISIBLE);
                mEtcImg.setVisibility(View.INVISIBLE);

                mTotalText.setTextColor(mContext.getColor(R.color.warm_grey));
                mExpeditionText.setTextColor(mContext.getColor(R.color.warm_grey));
                mProductText.setTextColor(mContext.getColor(R.color.warm_grey));
                mMethodText.setTextColor(mContext.getColor(R.color.warm_grey));
                mEtcText.setTextColor(mContext.getColor(R.color.warm_grey));


                mPointText.setTextColor(mContext.getColor(R.color.black));
                mPointImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = 2;

                mTotalImg.setVisibility(View.INVISIBLE);
                mExpeditionImg.setVisibility(View.INVISIBLE);
                mPointImg.setVisibility(View.INVISIBLE);
                mMethodImg.setVisibility(View.INVISIBLE);
                mEtcImg.setVisibility(View.INVISIBLE);

                mTotalText.setTextColor(mContext.getColor(R.color.warm_grey));
                mExpeditionText.setTextColor(mContext.getColor(R.color.warm_grey));
                mPointText.setTextColor(mContext.getColor(R.color.warm_grey));
                mMethodText.setTextColor(mContext.getColor(R.color.warm_grey));
                mEtcText.setTextColor(mContext.getColor(R.color.warm_grey));


                mProductText.setTextColor(mContext.getColor(R.color.black));
                mProductImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mMethodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = 3;

                mTotalImg.setVisibility(View.INVISIBLE);
                mExpeditionImg.setVisibility(View.INVISIBLE);
                mPointImg.setVisibility(View.INVISIBLE);
                mProductImg.setVisibility(View.INVISIBLE);
                mEtcImg.setVisibility(View.INVISIBLE);

                mTotalText.setTextColor(mContext.getColor(R.color.warm_grey));
                mExpeditionText.setTextColor(mContext.getColor(R.color.warm_grey));
                mPointText.setTextColor(mContext.getColor(R.color.warm_grey));
                mProductText.setTextColor(mContext.getColor(R.color.warm_grey));
                mEtcText.setTextColor(mContext.getColor(R.color.warm_grey));


                mMethodText.setTextColor(mContext.getColor(R.color.black));
                mMethodImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mEtcLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = 4;

                mTotalImg.setVisibility(View.INVISIBLE);
                mExpeditionImg.setVisibility(View.INVISIBLE);
                mPointImg.setVisibility(View.INVISIBLE);
                mProductImg.setVisibility(View.INVISIBLE);
                mMethodImg.setVisibility(View.INVISIBLE);

                mTotalText.setTextColor(mContext.getColor(R.color.warm_grey));
                mExpeditionText.setTextColor(mContext.getColor(R.color.warm_grey));
                mPointText.setTextColor(mContext.getColor(R.color.warm_grey));
                mProductText.setTextColor(mContext.getColor(R.color.warm_grey));
                mMethodText.setTextColor(mContext.getColor(R.color.warm_grey));


                mEtcText.setTextColor(mContext.getColor(R.color.black));
                mEtcImg.setVisibility(View.VISIBLE);

                menuSelect();
            }
        });

        mFragment = this;

        mViewGroup = (ViewGroup) view;

        mAdapter = new TalkAdapter(mFragment, mContext, 0);


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id" , mDbManager.selectUserId());
            jsonObject.put("notice" , 2);
            jsonObject.put("category" , -1);


            mSelectTask = new SelectTask(jsonObject, 0);
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
            intent.putExtra("jsonObject", mJsonArray.getJSONObject(i).toString());
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void menuSelect() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id" , mDbManager.selectUserId());
            jsonObject.put("notice" , 2);
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

package artech.com.semi.normal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.TalkDetailReviewAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class TalkReviewActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

//    RelativeLayout mNotSearchLayout;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    TalkDetailReviewAdapter mAdapter;

    SelectTask mSelectTask;

    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_talk_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        _id = intent.getStringExtra("id");


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id" ,_id);
            jsonObject.put("state", 0); // 판매중

            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
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

                connect = network.talkReviewSelect(jsonObject);
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
                if(network.mTalkArray != null || network.mTalkArray.length() > 0) {
//                    mNotSearchLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                mAdapter = new TalkDetailReviewAdapter(mContext);
                mAdapter.clear();
                mAdapter.addList(network.mTalkArray);
                mRecyclerView.setAdapter(mAdapter);

                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
            }else {
                mRecyclerView.setVisibility(View.GONE);
//                mNotSearchLayout.setVisibility(View.VISIBLE);
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

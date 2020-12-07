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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.ReviewAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class ReviewActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    TextView mReviewValueText;
    RelativeLayout mReviewWriteLayout;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ReviewAdapter mAdapter;

    SelectTask mSelectTask;

    JSONArray mJsonArray;

    String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_review);
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

        mReviewValueText = findViewById(R.id.review_value_text);
        mReviewWriteLayout = findViewById(R.id.review_write_layout);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mReviewWriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReviewWriteActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        Log.d("ReviewActivity", "startactivity : " + mId);

    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.d("ReviewActivity", "onResume : " + mId);

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mId);
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

                connect = network.reviewMarketMoreSelect(jsonObject);
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

                mReviewValueText.setText(Util.toNumFormat(mJsonArray.length()));


                mAdapter = new ReviewAdapter(mContext);
                mAdapter.clear();
                mAdapter.addList(mJsonArray);
                mRecyclerView.setAdapter(mAdapter);

                Log.d("ReviewActivity", "success mAdminTask : " + network.mResult);
            }else {
                Log.d("ReviewActivity", "not success mAdminTask : " + network.mResult);
                Log.d("ReviewActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }
}

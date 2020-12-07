package artech.com.semi.normal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.CouponAdapter;
import artech.com.semi.normal.adapter.PointAdapter;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class CouponActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    TextView mValueText;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CouponAdapter mAdapter;

    SelectTask mSelectTask;

    JSONArray mJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_coupon);
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

        TextView insertText = findViewById(R.id.app_bar_coupon_insert_text);
        insertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View alertView = inflater.inflate(R.layout.popup_coupon_insert, null);
                builder.setView(alertView);

                RelativeLayout cancelLayout = alertView.findViewById(R.id.cancel_layout);
                RelativeLayout confrimLayout = alertView.findViewById(R.id.confirm_layout);

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                params.height = 196;
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, 1050);

                cancelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                confrimLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mValueText = findViewById(R.id.use_coupon_value_text);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);




        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mDbManager.selectUserId());
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

                connect = network.couponSelect(jsonObject);
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

                mJsonArray = network.mCouponArray;

                mValueText.setText(mJsonArray.length() + "장");

                mAdapter = new CouponAdapter(mContext);
                mAdapter.clear();
                mAdapter.addList(mJsonArray);

                mRecyclerView.setAdapter(mAdapter);

                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
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

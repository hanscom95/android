package artech.com.semi.normal;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.PurchaseHistoryAdapter;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class PurchaseHistoryWaitingActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    RelativeLayout mNotSearchLayout;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    PurchaseHistoryAdapter mAdapter;

    SelectTask mSelectTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_normal_purchase_history_waiting);
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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_my_info);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);


        mNotSearchLayout = findViewById(R.id.not_search_layout);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    protected void onResume(){
        super.onResume();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id" ,mDbManager.selectUserId());
            jsonObject.put("state", "0, second, 4, 5"); // 판매중
            jsonObject.put("flag", 3); // 구매자

            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(mContext, NormalMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_basket:
                    intent = new Intent(mContext, ProductManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_favorites:
                    intent = new Intent(mContext, FavoritesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_my_info:
                    return true;
                case R.id.navigation_more:
                    intent = new Intent(mContext, MoreActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                    return true;
            }
            return false;
        }
    };

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
                if(network.mPurchaseArray != null || network.mPurchaseArray.length() > 0) {
                    mNotSearchLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                Log.d("BusinessMainActivity", "mPurchaseArray : " + network.mPurchaseArray.toString());

                mAdapter = new PurchaseHistoryAdapter(mContext, 0);
                mAdapter.clear();
                mAdapter.addList(network.mPurchaseArray);
                mRecyclerView.setAdapter(mAdapter);

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
            mSelectTask = null;
        }
    }
}

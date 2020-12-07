package artech.com.semi.normal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.RecommendShopAdapter;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class RecommendShopActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    GridLayoutManager mLayoutManager;
    RecommendShopAdapter mAdapter;

    Context mContext;
    TabLayout mTabLayout;
    ImageView mFirstImg, mSecondImg;
    TextView mTitleFirstText, mTitleSecondText, mAddressFirstText, mAddressSecondText;
    RelativeLayout mFirstLayout, mSecondLayout;

    SelectTask mSelectTask;

    String mFirstId, mSecondId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_normal_recommend_shop);
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
        navigation.setSelected(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        mFirstLayout = findViewById(R.id.first_layout);
        mSecondLayout = findViewById(R.id.second_layout);

        mFirstImg = findViewById(R.id.item_img);
        mSecondImg = findViewById(R.id.second_item_img);

        mTitleFirstText = findViewById(R.id.item_title_text);
        mTitleSecondText = findViewById(R.id.second_item_title_text);
        mAddressFirstText = findViewById(R.id.item_address_text);
        mAddressSecondText = findViewById(R.id.second_item_address_text);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mFirstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("id", mFirstId);
                mContext.startActivity(intent);
            }
        });

        mSecondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("id", mSecondId);
                mContext.startActivity(intent);
            }
        });

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("flag", 0);

            mSelectTask = new SelectTask();
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
                    intent = new Intent(mContext, MyInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_more:
                    intent = new Intent(mContext, MoreActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
            }
            return false;
        }
    };



    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONArray mJsonArray;

        SelectTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.marketSelect();
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
                mJsonArray = network.mMarketArray;

                try {
                for(int i = 0; i < mJsonArray.length(); i++){
                        if(i == 0) {
                            mFirstId = mJsonArray.getJSONObject(i).getString("id");
                            mFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
                            mTitleFirstText.setText(Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")));
                            mAddressFirstText.setText(Html.fromHtml(mJsonArray.getJSONObject(i).getString("address")));
                            mFirstLayout.setVisibility(View.VISIBLE);
                        }else if (i == 1){
                            mSecondId = mJsonArray.getJSONObject(i).getString("id");
                            mSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
                            mTitleSecondText.setText(Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")));
                            mAddressSecondText.setText(Html.fromHtml(mJsonArray.getJSONObject(i).getString("address")));
                            mSecondLayout.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter = new RecommendShopAdapter(mContext);
                mAdapter.clear();
                mAdapter.addList(mJsonArray);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("RecommendShopActivity", "success mAdminTask : " + network.mResult);
            }else {
                Log.d("RecommendShopActivity", "not success mAdminTask : " + network.mResult);
                Log.d("RecommendShopActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }
}

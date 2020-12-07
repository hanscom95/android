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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.FavoritesProductAdapter;
import artech.com.semi.normal.adapter.FavoritesShopAdapter;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class FavoritesActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    RelativeLayout mNavBottomtLayout, mProductLayout, mShopLayout, mNotSearchLayout, mCancelLayout, mRemoveLayout;
    LinearLayout mBottomLayout;
    TextView mEditText, mAllCheckedText;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    FavoritesShopAdapter mShopAdapter;
    FavoritesProductAdapter mProductAdapter;

    SelectTask mSelectTask;
    UpdateTask mUpdateTask;
    DeleteTask mDeleteTask;

    JSONArray mJsonArray;

    int mFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_normal_favorites);
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
        navigation.setSelectedItemId(R.id.navigation_favorites);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);


        mNavBottomtLayout = findViewById(R.id.bottom_layout);
        mProductLayout = findViewById(R.id.product_layout);
        mShopLayout = findViewById(R.id.shop_layout);
        mNotSearchLayout = findViewById(R.id.not_search_layout);
        mBottomLayout = findViewById(R.id.checkbox_bottom_layout);
        mCancelLayout = findViewById(R.id.cancel_layout);
        mRemoveLayout = findViewById(R.id.remove_layout);
        mEditText = findViewById(R.id.edit_text);
        mAllCheckedText = findViewById(R.id.all_checked_text);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlag == 0) {
                    mProductAdapter.visible = 1;
                    mProductAdapter.notifyDataSetChanged();
                }else {
                    mShopAdapter.visible = 1;
                    mShopAdapter.notifyDataSetChanged();
                }

                setBottomVisibility(true);
                mEditText.setVisibility(View.GONE);
                mAllCheckedText.setVisibility(View.VISIBLE);
            }
        });

        mAllCheckedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlag == 0) {
                    if(mProductAdapter.checked == 0) {
                        mProductAdapter.checked = 1;
                    }else {
                        mProductAdapter.checked = 0;
                    }

                    mProductAdapter.notifyDataSetChanged();
                }else {
                    if(mShopAdapter.checked == 0) {
                        mShopAdapter.checked = 1;
                    }else {
                        mShopAdapter.checked = 0;
                    }

                    mShopAdapter.notifyDataSetChanged();
                }
            }
        });

        mProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlag == 1) {
                    mShopLayout.setBackgroundResource(0);
                    mProductLayout.setBackgroundResource(R.mipmap.btn_take_press);
                    mFlag = 0;

                    mAllCheckedText.setVisibility(View.GONE);
                    mEditText.setVisibility(View.VISIBLE);


                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("flag" , mFlag);
                        jsonObject.put("id" ,mDbManager.selectUserId());

                        mSelectTask = new SelectTask(jsonObject);
                        mSelectTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mShopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlag == 0) {
                    mProductLayout.setBackgroundResource(0);
                    mShopLayout.setBackgroundResource(R.mipmap.btn_take_press);
                    mFlag = 1;

                    mAllCheckedText.setVisibility(View.GONE);
                    mEditText.setVisibility(View.VISIBLE);

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("flag" , mFlag);
                        jsonObject.put("id" ,mDbManager.selectUserId());

                        mSelectTask = new SelectTask(jsonObject);
                        mSelectTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mRemoveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String item = "";
                    if(mFlag == 0) {
                        for(int i = 0; i < mProductAdapter.mItems.size(); i++) {
                            if(mProductAdapter.mItems.get(i).getChecked() == 1) {
                                if(item.length() > 0) {
                                    item += ", '" + mProductAdapter.mItems.get(i).getFId() + "'";
                                }else {
                                    item += "'" + mProductAdapter.mItems.get(i).getFId() + "'";
                                }
                            }
                        }

                        Log.d("mRemoveLayout", "item : " + item);
                    }else {
                        for(int i = 0; i < mShopAdapter.mItems.size(); i++) {
                            if(mShopAdapter.mItems.get(i).getChecked() == 1) {
                                if(item.length() > 0) {
                                    item += ", '" + mShopAdapter.mItems.get(i).getFId() + "'";
                                }else {
                                    item += "'" + mShopAdapter.mItems.get(i).getFId() + "'";
                                }
                            }
                        }

                        Log.d("mRemoveLayout", "item : " + item);
                    }


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id" ,item);

                    mDeleteTask = new DeleteTask(jsonObject);
                    mDeleteTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setBottomVisibility(false);
            }
        });

        mCancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlag == 0) {
                    mProductAdapter.visible = 0;
                    mProductAdapter.notifyDataSetChanged();
                }else {
                    mShopAdapter.visible = 0;
                    mShopAdapter.notifyDataSetChanged();
                }

                setBottomVisibility(false);
                mAllCheckedText.setVisibility(View.GONE);
                mEditText.setVisibility(View.VISIBLE);
            }
        });

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("flag" , mFlag);
            jsonObject.put("id" ,mDbManager.selectUserId());

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
                    return true;
                case R.id.navigation_my_info:
                    intent = new Intent(mContext, MyInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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

                if(mFlag == 0) {
                    connect = network.favoritesProductSelect(mJsonObject);
                }else {
                    connect = network.favoritesShopSelect(mJsonObject);
                }
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
                if(network.mFavoritesArray != null || network.mFavoritesArray.length() > 0) {
                    mNotSearchLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                mJsonArray = network.mFavoritesArray;

                if(mFlag == 0) {
                    mProductAdapter = new FavoritesProductAdapter(mContext);
                    mProductAdapter.clear();
                    mProductAdapter.addList(network.mFavoritesArray);
                    mRecyclerView.setAdapter(mProductAdapter);
                }else{
                    mShopAdapter = new FavoritesShopAdapter(mContext);
                    mShopAdapter.clear();
                    mShopAdapter.addList(network.mFavoritesArray);
                    mRecyclerView.setAdapter(mShopAdapter);
                }
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
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

    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        UpdateTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.favoritesUpdate(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;
            String path = null;

            if(success) {
                finish();
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
            mUpdateTask = null;
        }
    }

    private class DeleteTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        DeleteTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.favoritesDelete(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;
            String path = null;

            if(success) {
                finish();
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
            mUpdateTask = null;
        }
    }

    public void setBottomVisibility(boolean visible) {
        if(visible) {
            mNavBottomtLayout.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.VISIBLE);
        }else {
            mBottomLayout.setVisibility(View.GONE);
            mNavBottomtLayout.setVisibility(View.VISIBLE);
        }
    }
}

package artech.com.semi.business;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.business.adpater.QuestionManagementDetailAdapter;
import artech.com.semi.normal.item.ProductReviewItem;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class QuestionManagementReviewDetailActivity extends AppCompatActivity {

    Context mContext;

    RelativeLayout mCustomerLayout;
    TextView mCustomerNameText, mInsertText;
    EditText mReviewEdit;
    ImageView mCustomerCloseImg;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    QuestionManagementDetailAdapter mAdapter;

    SelectTask mSelectTask;
    InsertTask mInsertTask;

    JSONArray mJsonArray;

    String mId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_question_management_detail);
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

        mCustomerLayout = findViewById(R.id.customer_layout);
        mCustomerNameText = findViewById(R.id.customer_name_text);
        mInsertText = findViewById(R.id.insert_text);
        mReviewEdit = findViewById(R.id.review_edit);
        mCustomerCloseImg = findViewById(R.id.close_img);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mInsertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("question_id", mId);
                    jsonObject.put("product_id", mJsonArray.getJSONObject(0).getString("product_id"));
                    jsonObject.put("user_id", mJsonArray.getJSONObject(0).getString("review_user_id"));
                    jsonObject.put("title", mReviewEdit.getText().toString());
                    jsonObject.put("content", mReviewEdit.getText().toString());

                    mInsertTask = new InsertTask(jsonObject);
                    mInsertTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");

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

                connect = network.reviewMarketDetailSelect(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;
            if(success) {
                try {
                    mJsonArray = network.mReviewArray;
                    Log.d("BusinessMainActivity", "mAdminTask : " + mJsonArray.toString());


                    ArrayList<ProductReviewItem> items = new ArrayList<>();

                    for(int i = 0; i < mJsonArray.length(); i++) {
                        ProductReviewItem item = new ProductReviewItem();
                        item.setId(mJsonArray.getJSONObject(i).getString("review_id"));
                        item.setName(mJsonArray.getJSONObject(i).getString("review_name"));
//                        item.setTitle(mJsonArray.getJSONObject(i).getString("review_title"));
                        item.setContents(mJsonArray.getJSONObject(i).getString("review_content"));
                        item.setDate(mJsonArray.getJSONObject(i).getString("review_reg_dt"));
                        item.setFlag(0);
                        Log.d("BusinessMainActivity", "review_name : " + Html.fromHtml(item.getName()).toString() + " / size : " + items.size());

                        String picture = mJsonArray.getJSONObject(i).getString("picture");
                        if(picture != null || picture != "") {
                            item.setThumbnail(Util.setDecoded64ImageStringFromBitmap(picture));
                        }

                        if(mJsonArray.getJSONObject(i).getString("picture1") != "null") {
                            item.setFirstImg(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture1")));
                        }

                        if(mJsonArray.getJSONObject(i).getString("picture2") != "null") {
                            item.setSecondImg(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture2")));
                        }

                        if(mJsonArray.getJSONObject(i).getString("picture3") != "null") {
                            item.setThirdImg(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture3")));
                        }

                        if(items.size() > 0) {
                            if (items.get(items.size() - 1).getFlag() == 1 || (items.get(items.size() - 1).getFlag() == 0 && items.get(items.size() - 1).getId() != item.getId())) {
                                items.add(item);
                            }
                        }else {
                            items.add(item);
                        }

                        int cnt = mJsonArray.getJSONObject(i).getInt("cnt");
                        Log.d("BusinessMainActivity", "cnt : " + cnt + " / reply_id : " + mJsonArray.getJSONObject(i).getString("reply_id") + " / items : " + items.size());

                        if(cnt > 0 && (mJsonArray.getJSONObject(i).getString("reply_id") != null || mJsonArray.getJSONObject(i).getString("reply_id") != "")) {
                            int j  = 0;
                            for(int k = cnt; k > 0; k--) {
                                item = new ProductReviewItem();
                                item.setId(mJsonArray.getJSONObject(i+j).getString("reply_id"));
                                item.setName(mJsonArray.getJSONObject(i+j).getString("reply_name"));
//                                item.setTitle(mJsonArray.getJSONObject(i+j).getString("reply_title"));
                                item.setContents(mJsonArray.getJSONObject(i+j).getString("reply_content"));
                                item.setDate(mJsonArray.getJSONObject(i+j).getString("reply_reg_dt"));
                                item.setFlag(1);
                                Log.d("BusinessMainActivity", "reply_name : " + Html.fromHtml(item.getName()).toString() );

                                items.add(item);
                                j++;
                            }

                            i = cnt-1;
                        }
                    }


                    Log.d("BusinessMainActivity", " items : " + items.size());
                    mAdapter = new QuestionManagementDetailAdapter(mContext);
                    mAdapter.clear();
                    mAdapter.addList(items);
                    mRecyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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



    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        InsertTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.reviewProductReplyInsert(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertTask = null;
            String path = null;

            if(success) {
                try {
                    mReviewEdit.setText("");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mId);

                    mSelectTask = new SelectTask(jsonObject);
                    mSelectTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
            }else {
                Toast.makeText(mContext, "댓글을 저장하는데 실패했습니다. 다시 저장해 주세요.", Toast.LENGTH_SHORT).show();
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mInsertTask = null;
        }
    }
}

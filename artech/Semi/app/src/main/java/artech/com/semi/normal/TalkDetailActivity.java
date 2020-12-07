package artech.com.semi.normal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.TalkDetailReviewAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;
import artech.com.semi.utility.ViewPagerAdapter;

public class TalkDetailActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    TextView mNameText, mDateText, mTitleText, mContentsText, mReviewText, mMoreText, mInsertText;
    EditText mReviewEdit;
    ImageView mProfileImg, mMoreImg;

    ViewPager mViewPager;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    TalkDetailReviewAdapter mAdapter;

    SelectTask mSelectTask;
    SelectItemTask mSelectItemTask;
    InsertTask mInsertTask;

    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_talk_detail);
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

        ImageView moreImg = findViewById(R.id.app_bar_more_img);
        moreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] listItems = new String[3];
                listItems[0] = "공유하기";
                listItems[1] = "신고하기";
                listItems[2] = "닫기";

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0) {
                            Toast.makeText(mContext, "공유하기는 준비중인 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        }else if(i == 1) {
                            Toast.makeText(mContext, "신고하기는 준비중인 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        }

                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });


        mProfileImg = findViewById(R.id.app_bar_profile_img);
        mMoreImg = findViewById(R.id.app_bar_more_img);

        mNameText = findViewById(R.id.app_bar_name_text);
        mDateText = findViewById(R.id.date_text);
        mTitleText = findViewById(R.id.title_text);
        mContentsText = findViewById(R.id.contents_text);
        mReviewText = findViewById(R.id.review_value_text);
        mInsertText = findViewById(R.id.review_insert_text);
        mReviewEdit = findViewById(R.id.review_edit);

        mViewPager = findViewById(R.id.viewPager);

        mMoreText = findViewById(R.id.review_more_text);
        mMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, TalkReviewActivity.class);
//                intent.putExtra("id", _id);
//                startActivity(intent);
            }
        });

        mInsertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("talk_id", _id);
                    jsonObject.put("user_id", mDbManager.selectUserId());
                    jsonObject.put("contents", mReviewEdit.getText().toString());


                    mInsertTask = new InsertTask(jsonObject);
                    mInsertTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mRecyclerView = findViewById(R.id.review_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("talk_id", id);
            jsonObject.put("user_id", mDbManager.selectUserId());
            _id = jsonObject.getString("talk_id");


//            mSelectTask = new SelectTask(jsonObject);
//            mSelectTask.execute();


            mSelectItemTask = new SelectItemTask(jsonObject);
            mSelectItemTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private class SelectItemTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        SelectItemTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.talkItemSelect(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectItemTask = null;
            String path = null;

            if(success) {

                try {
                    JSONObject object = network.mTalkArray.getJSONObject(0);

                    mProfileImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(object.getString("profile")));
                    mNameText.setText(Html.fromHtml(object.getString("name")));
                    mDateText.setText(Html.fromHtml(object.getString("date")));
                    mTitleText.setText(Html.fromHtml(object.getString("title")));
                    mContentsText.setText(Html.fromHtml(object.getString("contents")));


                    int bitmap = 0;
                    if(object.getString("picture1") != "null") {
                        bitmap++;
                    }
                    if(object.getString("picture2") !="null") {
                        bitmap++;
                    }
                    if(object.getString("picture3") !="null") {
                        bitmap++;
                    }

                    Bitmap[] bitmaps = new Bitmap[bitmap];

                    for(int j = 0; j < bitmap; j++) {
                        bitmaps[j] = Util.setDecoded64ImageStringFromBitmap(object.getString("picture"+(j+1)));
                        mViewPager.setVisibility(View.VISIBLE);
                    }

                    if(bitmap > 0) {
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, bitmaps);
                        mViewPager.setAdapter(viewPagerAdapter);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }


                mSelectTask = new SelectTask(jsonObject);
                mSelectTask.execute();
                Log.d("TalkDetailActivity", "success mTalkArray : " + network.mTalkArray);
            }else {
                Log.d("TalkDetailActivity", "not success mResult : " + network.mResult);
                Log.d("TalkDetailActivity", "not success mMsg : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectItemTask = null;
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
                mAdapter = new TalkDetailReviewAdapter(mContext);
                mAdapter.clear();
                mAdapter.addList(network.mJSONArray);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("TalkDetailActivity", "success mAdminTask : " + network.mJSONArray);
            }else {
                Log.d("TalkDetailActivity", "not success mResult : " + network.mResult);
                Log.d("TalkDetailActivity", "not success mMsg : " + network.mMsg);
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

                connect = network.talkReplyInsert(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertTask = null;

            Log.d("TalkDetailActivity", "mAdminTask : " + success);
            if(success) {
//                Intent intent = new Intent(mContext, ProductManagementActivity.class);
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//                startActivity(intent);
//                finish();

//                Intent intent = new Intent(mContext, TalkDetailActivity.class);
//                startActivity(intent);
                finish();
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mInsertTask = null;
        }
    }
}

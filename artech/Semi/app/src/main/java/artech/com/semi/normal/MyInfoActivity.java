package artech.com.semi.normal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.login.IntroActivity;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class MyInfoActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    RelativeLayout mUnauthorizedLayout, mPointLayout, mCouponLayout, mPurchaseHistoryWaitingLayout, mPurchaseHistorySuccessLayout, mProductLayout, mNoticeLayout, mEventLayout, mPlannedLayout;
    TextView mLogoutText, mRemoveAccountText, mNameText, mPointText, mCouponText, mPurchaseWaitingText, mPurchaseSuccessText;
    ImageView mProfileImg;

    SelectTask mSelectTask;

    String mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_my_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_my_info);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        mUnauthorizedLayout = findViewById(R.id.unauthorized);
        mPointLayout = findViewById(R.id.point_layout);
        mCouponLayout = findViewById(R.id.coupon_layout);
        mCouponLayout = findViewById(R.id.coupon_layout);
        mPurchaseHistoryWaitingLayout = findViewById(R.id.third_first_layout);
        mPurchaseHistorySuccessLayout = findViewById(R.id.third_second_layout);
        mProductLayout = findViewById(R.id.product_box_detail_layout);
        mNoticeLayout = findViewById(R.id.notice_box_detail_layout);
        mEventLayout = findViewById(R.id.event_box_detail_layout);
        mPlannedLayout = findViewById(R.id.planned_box_detail_layout);

        mLogoutText = findViewById(R.id.logout_text);

        mRemoveAccountText = findViewById(R.id.withdrawal_text);
        mNameText = findViewById(R.id.name_text);
        mPointText = findViewById(R.id.point_value_text);
        mCouponText = findViewById(R.id.coupon_value_text);
        mPurchaseWaitingText = findViewById(R.id.purchase_tobe_value_text);
        mPurchaseSuccessText = findViewById(R.id.purchase_success_value_text);

        mProfileImg = findViewById(R.id.img_thumbnail);

        mUnauthorizedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyInfoUpdateActivity.class);
                startActivity(intent);
            }
        });

        mPointLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PointActivity.class);
                startActivity(intent);
            }
        });

        mCouponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CouponActivity.class);
                startActivity(intent);
            }
        });

        mPurchaseHistoryWaitingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PurchaseHistoryWaitingActivity.class);
                startActivity(intent);
            }
        });

        mPurchaseHistorySuccessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PurchaseHistorySuccessActivity.class);
                startActivity(intent);
            }
        });

        mProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecentlyProductActivity.class);
                startActivity(intent);
            }
        });

        mNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TalkMyActivity.class);
                startActivity(intent);
            }
        });

        mEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EventActivity.class);
                startActivity(intent);
            }
        });

        mPlannedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mLogoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setMessage("로그아웃 하시겠습니까?");
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDbManager.dropDB();
                        dialogInterface.dismiss();

                        Intent intent = new Intent(mContext, IntroActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }
        });

        mLogoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setMessage("로그아웃 하시겠습니까?");
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDbManager.dropDB();
                        dialogInterface.dismiss();

                        Intent intent = new Intent(mContext, IntroActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }
        });


//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", mDbManager.selectUserId());
//            mSelectTask = new SelectTask(jsonObject);
//            mSelectTask.execute();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        Log.d("MyInfoActivity", "onResume");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mDbManager.selectUserId());
            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onResume();
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

                connect = network.accountMyInfoSelect(jsonObject);
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
                    JSONObject jsonObject = network.mAccountInfo.getJSONObject(0);
                    Log.d("BusinessMainActivity", "mAdminTask : " + jsonObject.toString());

                    mNameText.setText(Html.fromHtml(jsonObject.getString("name")));
                    mPointText.setText("("+Util.toNumFormat(jsonObject.getInt("point"))+"원)");
                    mCouponText.setText("("+Util.toNumFormat(jsonObject.getInt("coupon"))+"개)");
                    mPurchaseWaitingText.setText(""+Util.toNumFormat(jsonObject.getInt("waiting"))+"건");
                    mPurchaseSuccessText.setText(""+Util.toNumFormat(jsonObject.getInt("success"))+"건");

//                    if(jsonObject.getString("picture") != "" || !"".equals(jsonObject.getString("picture"))) {
                    if(!"".equals(jsonObject.getString("picture"))) {
                        mProfileImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(jsonObject.getString("picture")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //                setDecoded64ImageStringFromBitmap(path);

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

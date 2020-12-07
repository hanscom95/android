package artech.com.semi.normal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import artech.com.semi.R;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class PurchaseHistoryDetailActivity extends AppCompatActivity {

    Context mContext;
    RelativeLayout mPhoneLayout, mInfoLayout, mRefundLayout, mTermsLayout, mExpandTermsLayout, mVBankLayout, mVBankAccountLayout, mFirstLayout;
    TextView mTitleText, mPriceText, mReceiptDateText, mReceiptTimeText, mNameText, mAmountText, mOrderNumberText, mCompanyText, mOrderDateText, mCouponStateText, mVBankText, mVBankAccountText;
    ImageView mMainImg;
    Button mSuccessButton;
    ExpandableRelativeLayout mExpandLayout;

    JSONObject mJsonObject;
    SelectTask mSelectTask;
    IamportUpdateTask mIamportUpdateTask;
    SelectBankTask mSelectBankTask;
    SelectKeyTask mSelectKeyTask;
    UpdateTask mUpdateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_purchase_history_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_my_info);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mMainImg = (ImageView) findViewById(R.id.main_img);

        mTitleText = (TextView) findViewById(R.id.title_text);
        mPriceText = (TextView) findViewById(R.id.price_text);
        mReceiptDateText = (TextView) findViewById(R.id.reservation_date_value_text);
        mReceiptTimeText = (TextView) findViewById(R.id.reservation_time_value_text);
        mNameText = (TextView) findViewById(R.id.reservation_name_value_text);
        mAmountText = (TextView) findViewById(R.id.reservation_amount_value_text);
        mOrderNumberText = (TextView) findViewById(R.id.reservation_order_number_value_text);
        mCompanyText = (TextView) findViewById(R.id.reservation_shop_value_text);
        mOrderDateText = (TextView) findViewById(R.id.reservation_order_date_value_text);
        mCouponStateText = (TextView) findViewById(R.id.reservation_status_value_text);
        mVBankText = findViewById(R.id.reservation_vbank_value_text);
        mVBankAccountText = findViewById(R.id.reservation_vbank_account_value_text);

        mSuccessButton = (Button) findViewById(R.id.success_button);

        mFirstLayout = findViewById(R.id.first_layout);
        mPhoneLayout = (RelativeLayout) findViewById(R.id.phone_layout);
        mInfoLayout = (RelativeLayout) findViewById(R.id.info_layout);
        mRefundLayout = (RelativeLayout) findViewById(R.id.refund_layout);
        mVBankLayout = findViewById(R.id.reservation_vbank_layout);
        mVBankAccountLayout = findViewById(R.id.reservation_vbank_account_layout);

        mPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String receiver = mJsonObject.getString("phone");

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + receiver));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(mContext, ShopDetailActivity.class);
                    intent.putExtra("id", mJsonObject.getString("market_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

       mRefundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(mJsonObject.getInt("state") == 1  || mJsonObject.getInt("state") == 2  || mJsonObject.getInt("state") == 3) {
                        Toast.makeText(mContext, "환불을 할 수 없는 상태입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View alertView = inflater.inflate(R.layout.popup_product_refund, null);
                builder.setView(alertView);

                TextView titleText = alertView.findViewById(R.id.product_title_text);
                TextView priceText = alertView.findViewById(R.id.product_price_text);

                final EditText noticeText = alertView.findViewById(R.id.notice_edit);

                RelativeLayout cancelLayout = alertView.findViewById(R.id.cancel_layout);
                RelativeLayout confirmLayout = alertView.findViewById(R.id.confirm_layout);

                titleText.setText(mTitleText.getText().toString());
                priceText.setText(mPriceText.getText().toString());

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                params.height = 196;
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, 1800);
//                dialog.getWindow().setAttributes(params);

                cancelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                confirmLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        try {
//                            mJsonObject.put("reason", noticeText.getText().toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
////                        }
//
//                        mSelectKeyTask = new SelectKeyTask(0);
//                        mSelectKeyTask.execute();

                        try {
                            if(mJsonObject.getInt("state") == 4){
                                mSelectKeyTask = new SelectKeyTask(0);
                                mSelectKeyTask.execute();
                            }else if(mJsonObject.getInt("method") == 0 && (mJsonObject.getInt("state") == 0 || mJsonObject.getInt("state") == 5)) {
                                Intent intent = new Intent(mContext, RefundActivity.class);
                                intent.putExtra("id", mJsonObject.getString("number"));
                                startActivity(intent);
                                finish();
                            }else {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("id", mJsonObject.getString("number"));
                                jsonObject.put("state", 2);

                                mUpdateTask = new UpdateTask(jsonObject);
                                mUpdateTask.execute();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

        mSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",  mJsonObject.getString("number"));
                    jsonObject.put("state", 1);

                    mUpdateTask = new UpdateTask(jsonObject);
                    mUpdateTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mTermsLayout = findViewById(R.id.sixth_first_layout);
        mExpandTermsLayout = findViewById(R.id.terms_title_layout);
        mExpandLayout = findViewById(R.id.expandableLayout);

        mTermsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTermsLayout.setVisibility(View.GONE);
                mExpandLayout.expand();
            }
        });

        mExpandTermsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandLayout.collapse();
                mTermsLayout.setVisibility(View.VISIBLE);
            }
        });

        Intent intent = getIntent();
        String _id = intent.getStringExtra("id");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", _id);

            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
        }catch (JSONException e) {
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

                connect = network.purchaseItemSelect(jsonObject);
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

                try {
                    mJsonObject = network.mPurchaseArray.getJSONObject(0);
                    Log.d("ProductDetailActivity", "json : " + mJsonObject.toString());
                    Log.d("ProductDetailActivity", "company : " + mJsonObject.getString("company"));

                    SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 E요일");
                    SimpleDateFormat defaultDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Date date = defaultDt.parse(mJsonObject.getString("receipt_dt"));
                    String dt = df.format(date);


                    Date regDate = defaultDt.parse(mJsonObject.getString("reg_dt"));
                    String regDt = df.format(regDate);

                    SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                    String time = tf.format(date);

                    Log.d("time", "t : " + Integer.parseInt(time.substring(0,2)) + " / t2: " + time.substring(0,2) + " / dt : " + dt);

                    if(Integer.parseInt(time.substring(0,2)) > 12) {
                        time = "오전 "+ time;
                    }else {
                        time = "오후 "+ time;
                    }

                    mNameText.setText(Html.fromHtml(mJsonObject.getString("name")));
                    mAmountText.setText(Util.toNumFormat(mJsonObject.getInt("quantity"))+"개");
                    mOrderNumberText.setText(mJsonObject.getString("merchant_uid"));
                    mTitleText.setText(Html.fromHtml(mJsonObject.getString("product_name")));
                    mPriceText.setText(Util.toNumFormat(mJsonObject.getInt("price"))+"원");
                    mCompanyText.setText(Html.fromHtml(mJsonObject.getString("company")));
                    mReceiptDateText.setText(dt);
                    mReceiptTimeText.setText(time);
                    mOrderDateText.setText(regDt);

                    if(mJsonObject.getInt("state") == 0) {
                        mCouponStateText.setText("사용안함");
                    }else if(mJsonObject.getInt("state") == 1) {
                        mCouponStateText.setText("사용함");
                        mSuccessButton.setVisibility(View.GONE);
                    }else if(mJsonObject.getInt("state") == 3) {
                        mCouponStateText.setText("환불완료");
                        mSuccessButton.setVisibility(View.GONE);
                    }


                    String picture = mJsonObject.getString("picture");

                    if(picture != "null") {
                        mMainImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(picture));
                    }

                    if(mJsonObject.getInt("method") == 0) {
                        mSelectKeyTask = new SelectKeyTask(1);
                        mSelectKeyTask.execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

    private class IamportUpdateTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        IamportUpdateTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

//                connect = network.iamportCancelSelect(jsonObject);
                JSONObject object = new JSONObject();
                object.put("access_token", jsonObject.getJSONObject("response").getString("access_token"));
                object.put("imp_uid", mJsonObject.getString("imp_uid"));
                object.put("merchant_uid", mJsonObject.getString("merchant_uid"));
                object.put("amount", mJsonObject.getString("price"));
                object.put("reason", mJsonObject.getString("reason"));

                connect = network.iamportCancelSelect(object);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mIamportUpdateTask = null;

            Log.d("IamportUpdateTask", "mAdminTask : " + success);
            if(success) {
                JSONObject jsonObject = new JSONObject();
                Log.d("IamportUpdateTask", "mIamportObject : " + network.mIamportObject.toString());
                try {
                    jsonObject.put("id",  mJsonObject.getString("number"));
                    jsonObject.put("state", 3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mUpdateTask = new UpdateTask(jsonObject);
                mUpdateTask.execute();
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mIamportUpdateTask = null;
        }
    }

    private class SelectKeyTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        int flag;

        SelectKeyTask(int flag) {
            this.flag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.iamportKeySelect();
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectKeyTask = null;

            Log.d("MyInfoUpdateActivity", "mAdminTask : " + success);
            if(success) {
                if(flag == 0) {
                    mIamportUpdateTask = new IamportUpdateTask(network.mIamportObject);
                    mIamportUpdateTask.execute();
                }else {
                    mSelectBankTask = new SelectBankTask(network.mIamportObject);
                    mSelectBankTask.execute();
                }
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectKeyTask = null;
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

                connect = network.purchaseUpdate(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;

            Log.d("MyInfoUpdateActivity", "mAdminTask : " + success);
            if(success) {
                finish();
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
        }
    }

    private class SelectBankTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        SelectBankTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

//                connect = network.iamportCancelSelect(jsonObject);
                JSONObject object = new JSONObject();
                object.put("access_token", jsonObject.getJSONObject("response").getString("access_token"));
                object.put("imp_uid", mJsonObject.getString("imp_uid"));
                object.put("merchant_uid", mJsonObject.getString("merchant_uid"));
//                object.put("imp_uid", "imp_718487512782");
//                object.put("merchant_uid", "merchant_1536288489184");

                connect = network.iamportVBankSelect(object);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectBankTask = null;

            Log.d("IamportUpdateTask", "mAdminTask : " + success);
            if(success) {
                JSONObject jsonObject = new JSONObject();
                Log.d("IamportUpdateTask", "mSelectBankTask : " + network.mIamportObject.toString());

                try {
                    mVBankLayout.setVisibility(View.VISIBLE);
                    mVBankAccountLayout.setVisibility(View.VISIBLE);

                    mVBankText.setText(Html.fromHtml(network.mIamportObject.getJSONObject("response").getString("vbank_name")).toString());
                    mVBankAccountText.setText(Html.fromHtml(network.mIamportObject.getJSONObject("response").getString("vbank_num")).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectBankTask = null;
        }
    }
}

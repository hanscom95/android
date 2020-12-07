package artech.com.semi.normal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import artech.com.semi.R;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class PaymentSuccessActivity extends AppCompatActivity {

    Context mContext;

    RelativeLayout mVBankLayout, mVBankAccountLayout;
    ImageView mCloseImg;
    TextView mTitleText, mDateText,mTimeText, mPriceText, mCompanyText, mBookerText, mBookerDateText, mOrderNumberText, mVBankNameText, mVBankAccountText;
    Button mReceiveButton;

    ArrayList<String> mArrayList;

    JSONObject mJsonObject;

    SelectBankTask mSelectBankTask;
    SelectKeyTask mSelectKeyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_payment_success);

        mCloseImg = findViewById(R.id.app_bar_close_img);
        mTitleText = findViewById(R.id.title_text);
        mDateText = findViewById(R.id.date_text);
        mTimeText = findViewById(R.id.time_text);
        mPriceText = findViewById(R.id.price_text);
        mCompanyText = findViewById(R.id.company_text);
        mBookerText = findViewById(R.id.booker_text);
        mBookerDateText = findViewById(R.id.booker_date_text);
        mOrderNumberText = findViewById(R.id.order_number_text);
        mReceiveButton = findViewById(R.id.product_receive_button);
        mVBankNameText = findViewById(R.id.vbank_name_text);
        mVBankAccountText = findViewById(R.id.vbank_account_text);
        mVBankLayout = findViewById(R.id.second_third_vbank_name_layout);
        mVBankAccountLayout = findViewById(R.id.second_third_vbank_account_layout);


        mCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });




        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        mDateText.setText(year+"-" + Util.toDateFormat(month+1)+"-"+Util.toDateFormat(day));
        mTimeText.setText(Util.toDateFormat(hour)+":"+Util.toDateFormat(minute));

        Intent intent = getIntent();
        mArrayList = intent.getStringArrayListExtra("nameList");

        for(int i = 0; i < mArrayList.size(); i++) {
            Log.d("PaymentSuccessActivity", "name : " + mArrayList.get(i));
        }

        mTitleText.setText(mArrayList.get(3));
        mPriceText.setText(Util.toNumFormat(Integer.parseInt(mArrayList.get(4)))+ "원");
        mCompanyText.setText(mArrayList.get(7));

        mBookerText.setText(mArrayList.get(5) + " / " + mArrayList.get(9) + "개");
        mBookerDateText.setText(mArrayList.get(8));

        mOrderNumberText.setText(mArrayList.get(15));


        mSelectKeyTask = new SelectKeyTask();
        mSelectKeyTask.execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, NormalMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
                object.put("imp_uid", mArrayList.get(14));
                object.put("merchant_uid", mArrayList.get(15));
//                object.put("imp_uid", "imp_718487512782");
//                object.put("merchant_uid", "merchant_1536288489184");
//                object.put("imp_uid", mJsonObject.getJSONObject("response").getString("imp_uid"));
//                object.put("merchant_uid", mJsonObject.getJSONObject("response").getString("merchant_uid"));

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
                    if(network.mIamportObject.getJSONObject("response").getString("pay_method") == "vbank" || "vbank".equals(network.mIamportObject.getJSONObject("response").getString("pay_method"))) {
                        mVBankLayout.setVisibility(View.VISIBLE);
                        mVBankAccountLayout.setVisibility(View.VISIBLE);
                    }

                    mVBankNameText.setText(Html.fromHtml(network.mIamportObject.getJSONObject("response").getString("vbank_name")).toString());
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


    private class SelectKeyTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;

        SelectKeyTask() {
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
                Log.d("MyInfoUpdateActivity", "mIamportObject : " + network.mIamportObject);
                mJsonObject = network.mIamportObject;
                mSelectBankTask = new SelectBankTask(network.mIamportObject);
                mSelectBankTask.execute();
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectKeyTask = null;
        }
    }

}

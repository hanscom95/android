package artech.com.semi.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.business.BusinessMainActivity;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class SignupBusinessActivity extends AppCompatActivity {

    Context mContext;
    SignupTask mSignupTask;
    SignupIdCheckTask mSignupIdTask;
    DBManager mDbManager;

    RelativeLayout mIdLayout, mPwLayout, mPwCheckLayout;
    EditText mIdEdit, mPwEdit, mPwCheckEdit, mEmailEdit, mNameEdit;//, mCompanyEdit, mBusinessNumberEdit, mAddressEdit, mBankEdit,  mBankHolderEdit, mAccountNumberEdit;
    Button mSignupButton;

    int mFlag = -1;
    JSONObject mJsonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_business);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView closeImg = findViewById(R.id.app_bar_close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        mFlag = intent.getIntExtra("flag", 0);
        Log.d("Signup", "flag : " + intent.getIntExtra("flag", 0));
        Log.d("Signup", "json : " + intent.getStringExtra("user"));
        mIdLayout = findViewById(R.id.id_layout);
        mPwLayout = findViewById(R.id.pw_layout);
        mPwCheckLayout = findViewById(R.id.pw_check_layout);

        mIdEdit = findViewById(R.id.id_edit);
        mPwEdit = findViewById(R.id.pw_edit);
        mPwCheckEdit = findViewById(R.id.pw_check_edit);
        mEmailEdit = findViewById(R.id.email_edit);
//        mCompanyEdit = (EditText) findViewById(R.id.company_edit);
//        mBusinessNumberEdit = (EditText) findViewById(R.id.regist_edit);
//        mAddressEdit = (EditText) findViewById(R.id.address_edit);
//        mBankEdit = (EditText) findViewById(R.id.bank_edit);
//        mBankHolderEdit = (EditText) findViewById(R.id.account_holder_edit);
//        mAccountNumberEdit = (EditText) findViewById(R.id.account_number_edit);

        mSignupButton = findViewById(R.id.signup_button);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mIdEdit.getText().toString() == "" || "".equals(mIdEdit.getText().toString())) {
                    Toast.makeText(mContext, "아이디를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mEmailEdit.getText().toString() == "" || "".equals(mEmailEdit.getText().toString())) {
                    Toast.makeText(mContext, "이메일을 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!mPwEdit.getText().toString().equals(mPwCheckEdit.getText().toString())) {
                    Toast.makeText(mContext, "확인된 패스워드가 일치 하지 않습니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mPwEdit.getText().length() < 8) {
                    Toast.makeText(mContext, "패스워드는 8자리 이상으로 설정해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mFlag == 0) {
                    try {
                        mJsonData = new JSONObject();
                        mJsonData.put("id", mIdEdit.getText().toString());
                        mJsonData.put("pw", mPwEdit.getText().toString());
                        mJsonData.put("email", mEmailEdit.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        mJsonData.put("pw", mPwEdit.getText().toString());
                        mJsonData.put("email", mEmailEdit.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mSignupIdTask = new SignupIdCheckTask(mJsonData);
                mSignupIdTask.execute();

//                Intent intent = new Intent(getApplicationContext(), SignupBusinessSecondActivity.class);
//                intent.putExtra("user", mJsonData.toString());
//                intent.putExtra("flag", mFlag);
//                startActivity(intent);
//                finish();

                /*try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pw", mPwEdit.getText().toString());
                    jsonObject.put("email", mEmailEdit.getText().toString());
                    jsonObject.put("name", mNameEdit.getText().toString());
                    jsonObject.put("picture", mPwEdit.getText().toString());
                    jsonObject.put("flag", mFlag);
                    jsonObject.put("state", 1);
                    jsonObject.put("auth", 0);
                    jsonObject.put("market_name", mNameEdit.getText().toString());
                    jsonObject.put("company", mCompanyEdit.getText().toString());
                    jsonObject.put("address", mAddressEdit.getText().toString());
                    jsonObject.put("number", mBusinessNumberEdit.getText().toString());
                    jsonObject.put("bank", mBankEdit.getText().toString());
                    jsonObject.put("account_holder", mBankHolderEdit.getText().toString());
                    jsonObject.put("account_number", mAccountNumberEdit.getText().toString());
                    jsonObject.put("id", mJsonData.get("id").toString());
                    jsonObject.put("img", mJsonData.get("img").toString());

                    Log.d("mSignupButton", "mJsonData : " + jsonObject.toString());
                    mSignupTask = new SignupTask(jsonObject);
                    mSignupTask.execute();
//                    mJsonData.put("number_pictrue", mBusinessNumberEdit.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            }
        });

        setInitData(intent);
    }

    private void setInitData(Intent intent) {
        if(mFlag > 0) {
            String result = intent.getStringExtra("user").toString();
            try {
                mJsonData = new JSONObject(result);
                Log.d("SignuBusiness" +
                        "" +
                        "pActivity", "result : " + result);

                if(mFlag == 1) {
                    mIdEdit.setText(mJsonData.getString("id"));
//                    mPwEdit.setText(mJsonData.getString("pw"));
//                    mPwCheckEdit.setText(mJsonData.getString("pw"));
//                    mNameEdit.setText(mJsonData.getString("name"));
                }else if(mFlag == 2) {
                    mIdEdit.setText(mJsonData.getString("id"));
//                    mPwEdit.setText(mJsonData.getString("pw"));
//                    mPwCheckEdit.setText(mJsonData.getString("pw"));
//                    mNameEdit.setText(mJsonData.getString("name"));
                }else if(mFlag == 3) {
//                    JSONObject response = new JSONObject(result);
//                    mJsonData = response.getJSONObject("response");
                    mIdEdit.setText(mJsonData.get("id").toString());
//                    mPwEdit.setText(mJsonData.getString("pw"));
//                    mPwCheckEdit.setText(mJsonData.getString("pw"));
//                    mNameEdit.setText(mJsonData.get("name").toString());
                    mEmailEdit.setText(mJsonData.get("email").toString());
                    mEmailEdit.setEnabled(false);
//                    Log.d("naver", "id : "+ mJsonData.getJSONObject("response").get("id"));
//                    Log.d("naver", "name : "+ mJsonData.getJSONObject("response").get("name"));
                }

                mPwEdit.setText("zxcv1234");
                mPwCheckEdit.setText("zxcv1234");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIdEdit.setEnabled(false);
            mPwEdit.setEnabled(false);
            mPwCheckEdit.setEnabled(false);
        }
    }


    private class SignupTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject mJsonObject;

        SignupTask(JSONObject jsonObject) {
            mJsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.accountInsert(mJsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSignupTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                mDbManager.insertUser(mJsonObject);

                Intent intent = new Intent(getApplicationContext(), BusinessMainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(mContext, "계정을 만들 수 없습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSignupTask = null;
        }
    }

    private class SignupIdCheckTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        SignupIdCheckTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);
                connect = network.accountSelectId(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSignupIdTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                Intent intent = new Intent(getApplicationContext(), SignupBusinessSecondActivity.class);
                intent.putExtra("user", mJsonData.toString());
                intent.putExtra("flag", mFlag);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(mContext, "중복된 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSignupIdTask = null;
        }
    }
}

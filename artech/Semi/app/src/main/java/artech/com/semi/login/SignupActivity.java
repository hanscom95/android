package artech.com.semi.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.NormalMainActivity;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class SignupActivity extends AppCompatActivity {
    Context mContext;
    DBManager mDbManager;
    SignupTask mSignupTask;
    SignupIdCheckTask mSignupIdTask;

    RelativeLayout mNameLayout, mPwLayout, mPwCheckLayout;

    EditText mIdEdit, mPwEdit, mPwCheckEdit, mEmailEdit, mNameEdit;
    Button mSignupButton;

    int mFlag = -1;
    JSONObject mJsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        mNameLayout = findViewById(R.id.name_layout);
        mPwLayout = findViewById(R.id.pw_layout);
        mPwCheckLayout = findViewById(R.id.pw_check_layout);

        mIdEdit = findViewById(R.id.id_edit);
        mPwEdit = findViewById(R.id.pw_edit);
        mPwCheckEdit = findViewById(R.id.pw_check_edit);
        mEmailEdit = findViewById(R.id.email_edit);
        mNameEdit = findViewById(R.id.name_edit);

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

                if(mFlag == 0) {
                    if(mNameEdit.getText().toString() == "" || "".equals(mNameEdit.getText().toString())) {
                        Toast.makeText(mContext, "이름을 입력해 주세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(mPwEdit.getText().length() < 8) {
                        Toast.makeText(mContext, "패스워드는 8자리 이상으로 설정해 주세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        mJsonData = new JSONObject();

                        mJsonData.put("id", mIdEdit.getText().toString());
                        mJsonData.put("pw", mPwEdit.getText().toString());
                        mJsonData.put("email", mEmailEdit.getText().toString());
                        mJsonData.put("name", mNameEdit.getText().toString());
                        mJsonData.put("flag", mFlag); // 가입 sns flag
                        mJsonData.put("state", 0); // 구매자
                        mJsonData.put("auth", 0); // 업체 인증여부


                        mSignupIdTask = new SignupIdCheckTask(mJsonData);
                        mSignupIdTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
//                    mNameLayout.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", mJsonData.get("id").toString()); // 아이디
//                        jsonObject.put("pw", mJsonData.get("pw").toString()); // 패스워드
                        jsonObject.put("pw", "zxcv1234"); // 패스워드
                        jsonObject.put("name", mNameEdit.getText().toString()); // 이름
//                        if(mFlag == 1)
                            mJsonData.put("email", mEmailEdit.getText().toString());
//                        else
//                            jsonObject.put("email", mJsonData.get("email").toString()); // 이메일
                        if(mFlag > 0 && mJsonData.get("img") != null) {
                            jsonObject.put("img", mJsonData.get("img").toString()); // 유저 사진
                        }
                        jsonObject.put("flag", mFlag); // 가입 sns flag
                        jsonObject.put("state", 0); // 구매자
                        jsonObject.put("auth", 0); // 업체 인증여부


                        mSignupIdTask = new SignupIdCheckTask(jsonObject);
                        mSignupIdTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setInitData(intent);
    }


    private void setInitData(Intent intent) {
        if(mFlag > 0) {
            String result = intent.getStringExtra("user").toString();
            Log.d("SignupActivity", "result : " + result);
            try {
                mJsonData = new JSONObject(result);

                if(mFlag == 1) {
                    mIdEdit.setText(mJsonData.getString("id"));
                    mNameEdit.setText(mJsonData.getString("name"));
//                    mPwEdit.setText(mJsonData.getString("pw"));
//                    mPwCheckEdit.setText(mJsonData.getString("pw"));
                }else if(mFlag == 2) {
                    mIdEdit.setText(mJsonData.getString("id"));
                    mNameEdit.setText(mJsonData.getString("name"));
//                    mPwEdit.setText(mJsonData.getString("pw"));
//                    mPwCheckEdit.setText(mJsonData.getString("pw"));
                }else if(mFlag == 3) {
                    mIdEdit.setText(mJsonData.get("id").toString());
                    mNameEdit.setText(mJsonData.getString("name"));
                    mEmailEdit.setText(mJsonData.get("email").toString());
                    mEmailEdit.setEnabled(false);
//                    mPwEdit.setText(mJsonData.getString("pw"));
//                    mPwCheckEdit.setText(mJsonData.getString("pw"));
                }

                mPwEdit.setText("zxcv1234");
                mPwCheckEdit.setText("zxcv1234");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIdEdit.setEnabled(false);
            mPwEdit.setEnabled(false);
            mPwCheckEdit.setEnabled(false);
            //mPwLayout.setVisibility(View.GONE);
            //mPwCheckLayout.setVisibility(View.GONE);
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
                mJsonObject.put("age", 0);

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

                Intent intent = new Intent(getApplicationContext(), NormalMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                mSignupTask = new SignupTask(jsonObject);
                mSignupTask.execute();
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

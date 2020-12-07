package artech.com.semi.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

import artech.com.semi.R;
import artech.com.semi.business.BusinessMainActivity;
import artech.com.semi.normal.NormalMainActivity;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class LoginActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    SessionCallback mKakaocallback;// kakao
    SessionCallback callback; // kakao
    CallbackManager callbackManager; // facebook
    OAuthLogin mOAuthLoginModule; // naver
    InfoTask mInfoTask; // naver

    UserTask mUserTask; // user account
    UserSnsTask mUserSnsTask; // user account

    RelativeLayout mProgressLayout;
    RadioButton mUserRadio, mBusinessRadio;
    ImageView mCheckedImg;
    ImageView mProfileImg;
    EditText mIdEdit, mPwEdit;
    CheckBox mAutoCheck;
    TextView mFindAccountText;
    Button mLoginButton, mAccountButton, mKakaoButton, mFacebookButton, mNaverButton;
    com.kakao.usermgmt.LoginButton mKakaoRealButton;

    int mFlag = 0;

    boolean kakaOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

//        callback = new SessionCallback();
//        Session.getCurrentSession().addCallback(callback);
//        isKakaoLogin();

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

        mProgressLayout = findViewById(R.id.progress_layout);

        mUserRadio = findViewById(R.id.login_user_button);
        mBusinessRadio = findViewById(R.id.login_business_button);

        mCheckedImg = findViewById(R.id.login_checked_img);
        mProfileImg = findViewById(R.id.profile_img);

        mIdEdit = findViewById(R.id.id_text);
        mPwEdit = findViewById(R.id.pw_text);

        mAutoCheck = findViewById(R.id.auto_login_checkbox);

        mFindAccountText = findViewById(R.id.find_account_text);

        mLoginButton = findViewById(R.id.login_button);
        mAccountButton = findViewById(R.id.account_button);
        mKakaoButton = findViewById(R.id.kakao_button);
        mFacebookButton = findViewById(R.id.facebook_button);
        mNaverButton = findViewById(R.id.naver_button);
        mKakaoRealButton = findViewById(R.id.kakao_real_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mIdEdit.getText().toString() == "" || "".equals(mIdEdit.getText().toString())) {
                    Toast.makeText(mContext, "아이디를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mPwEdit.getText().toString() == "" || "".equals(mPwEdit.getText().toString())) {
                    Toast.makeText(mContext, "비밀번호를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mIdEdit.getText().toString());
                    jsonObject.put("pw", mPwEdit.getText().toString());
                    jsonObject.put("state", mFlag);
                    jsonObject.put("join_flag", 0);

                    mUserTask = new UserTask(jsonObject);
                    mUserTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlag == 0) {
                    Intent intent = new Intent(mContext, SignupActivity.class);
                    startActivity(intent);
//                    finish();
                }else if(mFlag == 1) {
                    Intent intent = new Intent(mContext, SignupBusinessActivity.class);
                    startActivity(intent);
//                    finish();
                }
            }
        });

        mKakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressLayout.setVisibility(View.VISIBLE);
                Log.d("LoginActivity", "kakaoLogin button");

//                mKakaoRealButton.callOnClick();
                // 카카오 로그인 요청
                isKakaoLogin();


            }
        });

        mFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackManager = CallbackManager.Factory.create();


                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if(accessToken != null) {
                    Log.d("accessToken" , "accessToken : " + accessToken);
                    RequestData(accessToken);
                }

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                RequestData(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });

                Button facebookButton = findViewById(R.id.re_facebook_button);
                facebookButton.performClick();
            }
        });

        mNaverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNaver();
            }
        });

        mFindAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, IdPwFIndActivity.class);
                startActivity(intent);
            }
        });


        getAppKeyHash();


        RadioGroup radioGroup = findViewById(R.id.login_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.login_user_button:
                        mFlag = 0;

                        mUserRadio.setTextColor(getResources().getColor(R.color.aqua_marine));
                        mBusinessRadio.setTextColor(getResources().getColor(R.color.warm_grey));
                        mCheckedImg.setBackgroundResource(R.mipmap.line_user_press);
                        mIdEdit.setBackgroundResource(R.drawable.login_input_box);
                        mPwEdit.setBackgroundResource(R.drawable.login_input_box);
                        mAutoCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#20D8A0")));
                        mLoginButton.setBackgroundResource(R.drawable.login_button);
                        mAccountButton.setBackgroundResource(R.drawable.login_account_button);
                        mAccountButton.setTextColor(getResources().getColor(R.color.aqua_marine));
                        mKakaoButton.setBackgroundResource(R.drawable.login_user_sns_button);
                        mFacebookButton.setBackgroundResource(R.drawable.login_user_sns_button);
                        mNaverButton.setBackgroundResource(R.drawable.login_user_sns_button);
                        break;
                    case R.id.login_business_button:
                        mFlag = 1;

                        mUserRadio.setTextColor(getResources().getColor(R.color.warm_grey));
                        mBusinessRadio.setTextColor(getResources().getColor(R.color.lavender));
                        mCheckedImg.setBackgroundResource(R.mipmap.line_business_press);
                        mIdEdit.setBackgroundResource(R.drawable.login_business_input_box);
                        mPwEdit.setBackgroundResource(R.drawable.login_business_input_box);
                        mAutoCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#8C37FF")));
                        mLoginButton.setBackgroundResource(R.drawable.login_b_button);
                        mAccountButton.setBackgroundResource(R.drawable.login_business_button);
                        mAccountButton.setTextColor(getResources().getColor(R.color.lavender));
                        mKakaoButton.setBackgroundResource(R.drawable.login_business_sns_button);
                        mFacebookButton.setBackgroundResource(R.drawable.login_business_sns_button);
                        mNaverButton.setBackgroundResource(R.drawable.login_business_sns_button);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LoginActivity", "onActivityResult===============");
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LoginActivity", "onDestroy===============");
        Session.getCurrentSession().removeCallback(mKakaocallback);
    }

    private void getAppKeyHash() {
        try {
            Log.d("LoginActivity", "package : " + getPackageName());
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


    private void isKakaoLogin() {
        Log.d("LoginActivity", "isKakaoLogin===============");
        // 카카오 세션을 오픈한다
        mKakaocallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("LoginActivity", "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
//            KakaorequestMe();
            if (!kakaOpen){
                kakaOpen = true;
                KakaorequestMe();
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("LoginActivity" , "세션 클로즈됨");
            mProgressLayout.setVisibility(View.GONE);
            if(exception != null) {
                Log.d("LoginActivity" , exception.getMessage());
            }


            kakaOpen = false;
        }
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void KakaorequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                mProgressLayout.setVisibility(View.GONE);
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("LoginActivity" , " onFailure 오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                kakaOpen = false;
                mProgressLayout.setVisibility(View.GONE);

                callback = new SessionCallback();
                Session.getCurrentSession().addCallback(callback);
                Log.d("LoginActivity" , "onSessionClosed 오류로 카카오로그인 실패 ");
                Toast.makeText(getApplicationContext(), "카카오톡 오류로 인해 로그인이 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                    String profileUrl = userProfile.getProfileImagePath();
                    Log.d("kakao", "path : " + profileUrl);
                    Log.d("kakao", "getId : " + userProfile.getId());
                    Log.d("kakao", "getNickname : " + userProfile.getNickname());
                    Log.d("kakao", "getUUID : " + userProfile.getUUID());
                    Log.d("kakao", "getProperties : " + userProfile.getProperties().toString());
                    //userId = String.valueOf(userProfile.getId());
                    //userName = userProfile.getNickname();


                    JSONObject sObject = new JSONObject();
                    try {
                        sObject.put("id", userProfile.getId());
                        sObject.put("name", userProfile.getNickname());
                        sObject.put("pw", "zxcv1234");
    //                    sObject.put("img", userProfile.getProfileImagePath());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setLayoutText(profileUrl, sObject, 1);

            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
                mProgressLayout.setVisibility(View.GONE);
                Log.d("LoginActivity" , "자동가입이 아닐경우 동의창");
            }
        });
    }


    private void setLayoutText(String profileUrl, final JSONObject json, final int flag){
            Picasso.with(this).load(profileUrl)
                    .resize(400,400)
                    .into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d("kakao", "setImageBitmap");
//                    mProfileImg.setImageBitmap(bitmap);
                    String img_b64os = Util.getEncoded64ImageStringFromBitmap(bitmap);
//                    String img_b64os2 = getEncoded64ImageStringFromBitmap(mProfileImg.getDrawingCache());
                    Log.d("kakao", "img_b64os : " + img_b64os);

                    try {
                        json.put("img", img_b64os);
                        json.put("flag", flag);
                        json.put("pw", "zxcv1234");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mUserSnsTask = new UserSnsTask(json, flag);
                    mUserSnsTask.execute();


                    /*if(mFlag == 1) {
                        Intent intent = new Intent(getApplicationContext(), SignupBusinessActivity.class);
                        intent.putExtra("user", json.toString());
                        intent.putExtra("flag", flag);
                        startActivity(intent);
//                    finish();
                    }else if(mFlag == 0) {
                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                        intent.putExtra("user", json.toString());
                        intent.putExtra("flag", flag);
                        startActivity(intent);
//                    finish();
                    }*/

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    kakaOpen = false;
                    Toast.makeText(mContext, "회원 정보를 가지고 오는데 실패하였습니다. 다시 한번 로그인해 주세요.", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable){
                    Log.d("kakao", "onPrepareLoad");

                    try {
                        json.put("flag", flag);
                        json.put("pw", "zxcv1234");
                        json.put("img", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mUserSnsTask = new UserSnsTask(json, flag);
                    mUserSnsTask.execute();
                }
            });

        mProgressLayout.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LoginActivity", "onResume===========");
        if(kakaOpen)
            kakaOpen = false;
    }


    private void RequestData(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.d("requestData", "response : " + response.toString());
                Log.d("requestData", "object : " + object.toString());
                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        String text = "Name : "+json.getString("name")+
//                        "Email : "+json.getString("email")+
                                "Profile link : "+json.getString("id");
//                        idText.setText(Html.fromHtml(text));
                        Log.d("requestData", "text : " + text);
                        Log.d("requestData", "id : " + json.getString("id"));
                        Log.d("requestData", "picture : " + json.getString("picture"));
                        Log.d("requestData", "url2 : " + json.getJSONObject("picture").getJSONObject("data").getString("url"));
//                        Log.d("requestData", "url : " + json.getString("url"));
//                        profilePictureView.setProfileId(json.getString("id"));

                        String path = json.getJSONObject("picture").getJSONObject("data").getString("url");
//                        json.put("img", json.getJSONObject("picture").getJSONObject("data").getString("url"));
                        setLayoutText(path, json, 2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }



    private void setNaver() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(this, "n4MZunQRR8wghUZ_aoq4", "eoJl4iFxfe", "semi");

//        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
    }


    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);

                Log.d("naver", " accessToken : " + accessToken);
                Log.d("naver", " refreshToken : " + refreshToken);
                Log.d("naver", " expiresAt : " + String.valueOf(expiresAt));
                Log.d("naver", " tokenType : " + tokenType);
                Log.d("naver", " getState : " + mOAuthLoginModule.getState(mContext).toString());

                mInfoTask = new InfoTask(accessToken);
                mInfoTask.execute((Void) null);

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Log.d("LoginActivity", "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc);
                Toast.makeText(mContext, "로그인에 실패했습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class InfoTask extends AsyncTask<Void, Void, Boolean> {
        String mToken;
        JSONObject mJsonObject;

        InfoTask(String token) {
            mToken = token;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {

                String result = mOAuthLoginModule.requestApi(mContext, mToken, "https://openapi.naver.com/v1/nid/me");
                mJsonObject = new JSONObject(result);
                Log.d("naver", "requestApi : "+ result);
                Log.d("naver", "mJsonObject : "+ mJsonObject.toString());
//                Log.d("naver", "id : "+ mJsonObject.getJSONObject("response").get("id"));
//                Log.d("naver", "profile_image : "+ mJsonObject.getJSONObject("response").get("profile_image"));
//                Log.d("naver", "age : "+ mJsonObject.getJSONObject("response").get("age"));
//                Log.d("naver", "gender : "+ mJsonObject.getJSONObject("response").get("gender"));
//                Log.d("naver", "email : "+ mJsonObject.getJSONObject("response").get("email"));
//                Log.d("naver", "name : "+ mJsonObject.getJSONObject("response").get("name"));
//                mJsonObject.getJSONObject("response").put("img", mJsonObject.getJSONObject("response").get("profile_image"));

                /*try {

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                        String buf = "";
                        JSONObject params = null;

                        while ((buf = reader.readLine()) != null) {
                            params = new JSONObject(buf);
                        }

                        result = params.getString("result");
                        msg = params.getString("msg");
                        mResult = result;

                        mMsg = msg;
                        Log.d("NetworkConnection", "result : " + result);
                        Log.d("NetworkConnection", "msg : " + msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }*/

            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInfoTask = null;
            String profileUrl = null;
            try {
                profileUrl = mJsonObject.getJSONObject("response").get("profile_image").toString();
                Log.d("naver", "profileUrl : " + profileUrl);
//                setLayoutText(profileUrl);
                setLayoutText(profileUrl, mJsonObject.getJSONObject("response"), 3);

//                if(mFlag == 1) {
//                    Intent intent = new Intent(getApplicationContext(), SignupBusinessActivity.class);
//                    intent.putExtra("user", mJsonObject.toString());
//                    intent.putExtra("flag", third);
//                    startActivity(intent);
//                    finish();
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {
            mInfoTask = null;
        }
    }

    private class UserTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        UserTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.accountSelectLogin(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUserTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                try {
                    JSONObject object;
                    object = network.mAccountInfo.getJSONObject(0);
                    mDbManager.insertUser(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(mFlag == 0) {
                    Intent intent = new Intent(getApplicationContext(), NormalMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if (mFlag == 1) {
                    Intent intent = new Intent(getApplicationContext(), BusinessMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else {
                Toast.makeText(mContext, "아이디 또는 비밀번호를 다시 확인하세요. ", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mUserTask = null;
        }
    }

    private class UserSnsTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;
        int state;

        UserSnsTask(JSONObject jsonObject, int state) {
            this.jsonObject = jsonObject;
            this.state = state;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.accountSelectLogin(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUserSnsTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {

                try {
                    JSONObject object;
                    object = network.mAccountInfo.getJSONObject(0);
                    mDbManager.insertUser(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(mFlag == 0) {
                    Intent intent = new Intent(getApplicationContext(), NormalMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if (mFlag == 1) {
                    Intent intent = new Intent(getApplicationContext(), BusinessMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else {
                if(mFlag == 1) {
                    Intent intent = new Intent(getApplicationContext(), SignupBusinessActivity.class);
                    intent.putExtra("user", jsonObject.toString());
                    intent.putExtra("flag", state);
                    startActivity(intent);
//                    finish();
                }else if(mFlag == 0) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    intent.putExtra("user", jsonObject.toString());
                    intent.putExtra("flag", state);
                    startActivity(intent);
//                    finish();
                }
            }

        }

        @Override
        protected void onCancelled() {
            mUserSnsTask = null;
        }
    }

}

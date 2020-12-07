package artech.com.semi.utility.naver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;

public class NaverMainActivity extends AppCompatActivity {

    public static OAuthLogin mOAuthLoginModule;
    OAuthLoginButton mOAuthLoginButton;
    Context mContext;

    ImageView mImageView;
    TextView mOauthAT, mOauthRT, mOauthExpires, mOauthTokenType, mOAuthState;

    InfoTask mInfoTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mImageView = findViewById(R.id.iv_user_profile);

        mOauthAT = findViewById(R.id.oauth_at_text);
        mOauthRT = findViewById(R.id.oauth_rt_text);
        mOauthExpires = findViewById(R.id.oauth_expires_text);
        mOauthTokenType = findViewById(R.id.oauth_oauth_token_text);
        mOAuthState = findViewById(R.id.oauth_oauth_state_text);

        setNaver();
    }


    private void setNaver() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(this, "n4MZunQRR8wghUZ_aoq4", "eoJl4iFxfe", "semi");

        mOAuthLoginButton = findViewById(R.id.button_naverlogin);

        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
//        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);

                mOauthAT.setText(accessToken);
                mOauthRT.setText(refreshToken);
                mOauthExpires.setText(String.valueOf(expiresAt));
                mOauthTokenType.setText(tokenType);
                mOAuthState.setText(mOAuthLoginModule.getState(mContext).toString());

                mInfoTask = new InfoTask(accessToken);
                mInfoTask.execute((Void) null);

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class InfoTask extends AsyncTask<Void, Void, Boolean> {
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
                Log.d("naver", "id : "+ mJsonObject.getJSONObject("response").get("id"));
                Log.d("naver", "profile_image : "+ mJsonObject.getJSONObject("response").get("profile_image"));
                Log.d("naver", "age : "+ mJsonObject.getJSONObject("response").get("age"));
                Log.d("naver", "gender : "+ mJsonObject.getJSONObject("response").get("gender"));
                Log.d("naver", "email : "+ mJsonObject.getJSONObject("response").get("email"));
                Log.d("naver", "name : "+ mJsonObject.getJSONObject("response").get("name"));

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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Picasso.with(mContext)
                    .load(profileUrl)
                    .fit()
                    .into(mImageView);

        }

        @Override
        protected void onCancelled() {
            mInfoTask = null;
        }
    }

}

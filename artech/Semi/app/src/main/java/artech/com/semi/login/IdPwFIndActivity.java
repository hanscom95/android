package artech.com.semi.login;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class IdPwFIndActivity extends AppCompatActivity {
    Context mContext;
    DBManager mDbManager;

    IdCheckTask mIdCheckTask;

    RelativeLayout mTemporaryLayout;

    EditText mIdEdit, mEmailEdit;
    TextView mTemporaryText;
    Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_pw_find);

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

        mTemporaryLayout = findViewById(R.id.temporary_layout);

        mIdEdit = findViewById(R.id.id_edit);
        mEmailEdit = findViewById(R.id.email_edit);
        mTemporaryText = findViewById(R.id.temporary_pw_text);

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


                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mIdEdit.getText().toString()); // 아이디
                    jsonObject.put("email", mEmailEdit.getText().toString());


                    mIdCheckTask = new IdCheckTask(jsonObject);
                    mIdCheckTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private class IdCheckTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        IdCheckTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);
                connect = network.accountSelectIdEmail(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mIdCheckTask = null;

            Log.d("IdPwFindActivity", "mAdminTask : " + success);
            if(success) {
                try {
                    JSONObject jsonObject = network.mJSONObject;
                    Log.d("IdPwFindActivity", "jsonObject : " + jsonObject);
                    mTemporaryText.setText(jsonObject.getString("pw"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mTemporaryLayout.setVisibility(View.VISIBLE);
                mSignupButton.setVisibility(View.GONE);
            }else {
                Toast.makeText(mContext, "아이디와 이메일을 다시 확인 후 입력해주세요.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mIdCheckTask = null;
        }
    }
}

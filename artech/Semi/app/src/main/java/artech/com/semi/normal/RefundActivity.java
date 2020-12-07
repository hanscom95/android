package artech.com.semi.normal;

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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

public class RefundActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    EditText mBankEdit, mAccountEdit, mNameEdit;
    Button mRefundButton;

    UpdateTask mUpdateTask;

    String mId;

    int mPoint = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_refund);
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

        mBankEdit = findViewById(R.id.refund_bank_edit);
        mAccountEdit = findViewById(R.id.refund_account_edit);
        mNameEdit = findViewById(R.id.refund_name_edit);

        mRefundButton = findViewById(R.id.refund_button);

        mRefundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBankEdit.getText().toString() == "" || "".equals(mBankEdit.getText().toString())) {
                    Toast.makeText(mContext, "은행을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mAccountEdit.getText().toString() == "" || "".equals(mAccountEdit.getText().toString())) {
                    Toast.makeText(mContext, "계좌번호 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mNameEdit.getText().toString() == "" || "".equals(mNameEdit.getText().toString())) {
                    Toast.makeText(mContext, "예금주를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", mId);
                    jsonObject.put("state", 2);
                    jsonObject.put("refund_bank",mBankEdit.getText().toString());
                    jsonObject.put("refund_account",mAccountEdit.getText().toString());
                    jsonObject.put("refund_name",mNameEdit.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mUpdateTask = new UpdateTask(jsonObject);
                mUpdateTask.execute();
            }
        });


        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
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

                connect = network.purchaseRefundUpdate(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
//                Intent intent = new Intent(mContext, ProductManagementActivity.class);
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//                startActivity(intent);
//                finish();

                Intent intent = new Intent(mContext, MyInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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


}

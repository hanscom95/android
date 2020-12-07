package artech.com.arcam.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import artech.com.arcam.R;
import artech.com.arcam.utility.DBManager;
import artech.com.arcam.utility.NetworkConnection;


public class SignUpActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    EditText mUserNameEdit, mPasswordEdit, mTeamEdit, mDayEdit, mYearEdit, mEmaillEdit;
    Button mBackButton, mCloseButton, mCancelButton, mInsertButton, mMaleButton, mFemaleButton;
    Spinner mNationalSpinner, mMonthSpinner;

    AdminTask mAdminTask;

    int seInt = 0;

    static final String[] Months = new String[] { "Month", "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "arcam.db", null, 1);

        setContentView(R.layout.content_signup);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
        }

        mBackButton = (Button) findViewById(R.id.back_button);
        mCloseButton = (Button) findViewById(R.id.close_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mInsertButton = (Button) findViewById(R.id.insert_button);
        mMaleButton = (Button) findViewById(R.id.male_button);
        mFemaleButton = (Button) findViewById(R.id.female_button);

        mNationalSpinner = (Spinner) findViewById(R.id.national_spinner);
        mMonthSpinner = (Spinner) findViewById(R.id.month_spinner);

        mUserNameEdit = (EditText) findViewById(R.id.username_text);
        mPasswordEdit = (EditText) findViewById(R.id.password_text);
        mTeamEdit = (EditText) findViewById(R.id.team_text);
        mDayEdit = (EditText) findViewById(R.id.day_text);
        mYearEdit = (EditText) findViewById(R.id.year_text);
        mEmaillEdit = (EditText) findViewById(R.id.email_text);


        String[] locales = Locale.getISOCountries();
        for(int i = 0; i < Locale.getISOCountries().length ; i++) {
            Locale locale = new Locale( "en", locales[i] );
            locales[i] = locale.getDisplayCountry(Locale.ENGLISH);
        }
        Arrays.sort(locales);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, locales);
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mNationalSpinner.setAdapter(adapter);


        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, Months);
        monthAdapter.setDropDownViewResource(R.layout.signup_spinner);
        mMonthSpinner.setAdapter(monthAdapter);

        mMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFemaleButton.setBackgroundResource(R.mipmap.btn_inactive);
                mFemaleButton.setTextColor(getResources().getColor(R.color.skybluefont));
                mMaleButton.setBackgroundResource(R.mipmap.btn_active);
                mMaleButton.setTextColor(getResources().getColor(R.color.whitefont));

                seInt = 1;
        }
        });

        mFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFemaleButton.setBackgroundResource(R.mipmap.btn_active);
                mFemaleButton.setTextColor(getResources().getColor(R.color.whitefont));
                mMaleButton.setBackgroundResource(R.mipmap.btn_inactive);
                mMaleButton.setTextColor(getResources().getColor(R.color.skybluefont));

                seInt = 2;
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(mUserNameEdit.getText().toString())// || "".equals(mPasswordEdit.getText().toString())
                    || seInt == 0 || mMonthSpinner.getSelectedItemPosition() == 0
                    || "".equals(mDayEdit.getText().toString()) || "".equals(mYearEdit.getText().toString())
                    || "".equals(mEmaillEdit.getText().toString())){

                    Toast.makeText(mContext, getString(R.string.signup_insert_error), Toast.LENGTH_SHORT).show();
                }else {
                    ArrayList<Object> arrayList = new ArrayList<>();
                    arrayList.add(mUserNameEdit.getText().toString());
//                    arrayList.add(mPasswordEdit.getText().toString());
                    arrayList.add("1111");
                    arrayList.add(mNationalSpinner.getSelectedItemPosition());
                    arrayList.add(mTeamEdit.getText().toString());
                    arrayList.add(seInt);
                    int month = mMonthSpinner.getSelectedItemPosition();
                    int day = Integer.parseInt(mDayEdit.getText().toString());
                    String date = mYearEdit.getText().toString() + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
                    arrayList.add(date);
                    arrayList.add("");
                    arrayList.add(mEmaillEdit.getText().toString());
                    mAdminTask = new AdminTask(1, arrayList);
                    mAdminTask.execute((Void) null);
                }
            }
        });
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AdminTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        int mFlag = 0;
        ArrayList<Object> mArrayList;

        AdminTask(int flag, ArrayList<Object> arrayList) {
            mFlag = flag;
            mArrayList = arrayList;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);
                if(mFlag == 1) {
                    connect = network.accountSelect(mArrayList.get(0).toString());
                }else if(mFlag == 2){
                    connect = network.accountInsert(mArrayList);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAdminTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(mFlag == 1 && success) {
                mAdminTask = new AdminTask(2, mArrayList);
                mAdminTask.execute((Void) null);
            }else if(mFlag == 2 && success) {
                mDbManager.insertUser(mArrayList);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(mContext, getString(R.string.signup_insert_error_fail), Toast.LENGTH_SHORT).show();
//                mDbManager.insertUser(mArrayList);
//                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
//                startActivity(intent);
//                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAdminTask = null;
        }
    }
}

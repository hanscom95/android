package se.com.band.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.com.band.MainMenuActivity;
import se.com.band.R;
import se.com.band.utility.DBManager;
import se.com.band.utility.NetworkConnection;
import se.com.band.utility.ULog;

public class AccountActivity extends AppCompatActivity {

    Context mContext;
    NetworkConnection network;
    UserAccountTask mAuthTask = null;
    Bundle bundle;
    DBManager dbManager;

    RelativeLayout mBirthLayout;
    Button mCreateButton;
    EditText mPwText, mNameText, mEamilText;
    TextView mBirthText;
    RadioGroup mGenderRadio;
    View mProgressView, mFormView;

    String mDeviceAddr;
    int mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dbManager = new DBManager(getApplicationContext(), "apex.db", null, 1);

        mBirthLayout = (RelativeLayout) findViewById(R.id.birth_layout);
        mPwText = (EditText) findViewById(R.id.password_edit);
        mNameText = (EditText) findViewById(R.id.name_edit);
        mBirthText = (TextView) findViewById(R.id.birth_text);
        mEamilText = (EditText) findViewById(R.id.email_edit);
//        mTallText = (EditText) findViewById(R.id.tall);
//        mWeightText = (EditText) findViewById(R.id.weight);
//        mAgeText = (EditText) findViewById(R.id.age);
        mGenderRadio = (RadioGroup) findViewById(R.id.gender_group);
//        mHandRadio = (RadioGroup) findViewById(R.id.hand_group);
        mCreateButton = (Button) findViewById(R.id.signup_button);
//        mUpdateButton = (Button) findViewById(R.id.account_update);
        mProgressView = (View) findViewById(R.id.account_progress);
        mFormView = (View) findViewById(R.id.account_form_layout);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatus = 0;

                if(!atteptAccount()){
                    return;
                }

                String pw = mPwText.getText().toString();
                String name = mNameText.getText().toString();
                String birth = mBirthText.getText().toString();
                String email = mEamilText.getText().toString();
//                String age = mAgeText.getText().toString();
                int gender = mGenderRadio.indexOfChild(mGenderRadio.findViewById(mGenderRadio.getCheckedRadioButtonId()));
                String device_addr = "01.88.12.64";//mDeviceAddr;

                ArrayList<Object> account = new ArrayList<>();
                account.add(email);
//                account.add(id);
                account.add(pw);
                account.add(name.replaceAll(" ", "%20"));
                account.add(birth);
                account.add('0');//tall
                account.add('0');//weight
                account.add(gender);
                account.add('0');//age
                account.add(device_addr);
                account.add('0');//hand

                showProgress(true);
                mAuthTask = new UserAccountTask(account);
                mAuthTask.execute((Void) null);

//                network.accountCreate(account);
            }
        });

        mBirthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });

        mBirthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dateDialog();
            }
        });

        mGenderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkeditem)
            {
                RadioButton female = (RadioButton)findViewById(R.id.female_button);
                RadioButton male = (RadioButton)findViewById(R.id.male_button);

                switch(checkeditem)
                { // change button style, opacity (gender RadioButton)
                    case R.id.female_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        female.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        male.setTypeface(typeface, Typeface.NORMAL);
                        break;
                    }
                    case R.id.male_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        male.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        female.setTypeface(typeface, Typeface.NORMAL);
                        break;
                    }
                }
            }
        });


        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
        RadioButton female = (RadioButton) findViewById(R.id.female_button) ;
        female.setTypeface(typeface, Typeface.NORMAL);
        RadioButton male = (RadioButton) findViewById(R.id.male_button) ;
        male.setTypeface(typeface, Typeface.NORMAL);
    }

    private boolean atteptAccount() {
        String name = mNameText.getText().toString();
        String password = mPwText.getText().toString();
        String email = mEamilText.getText().toString();
        String birth = mBirthText.getText().toString();

        int gender = mGenderRadio.getCheckedRadioButtonId();
        ULog.d("gender : " + gender);

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email) || TextUtils.isEmpty(birth)) {
            Toast.makeText(mContext, getString(R.string.error_field_required), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    public class UserAccountTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<Object> mAccount;

        UserAccountTask(ArrayList<Object> account) {
            mAccount = account;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean connect = false;
            try {
                // Simulate network access.
                Thread.sleep(1000);
                network = new NetworkConnection(mContext);
                if(mStatus == 0) {
                    connect = network.accountCreate(mAccount);
                }else if(mStatus == 1){
                    connect = network.accountUpdate(mAccount);
                }
            } catch (InterruptedException e) {
                return false;
            }

            if(!connect) {
                return false;
            }else {
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if(success) {
                if(mStatus == 0) {
                    dbManager.dropDB();
                    dbManager.insertUser(mAccount);
                }else if(mStatus == 1){
                    dbManager.updateUser(mAccount);
                }


                Toast.makeText(mContext, "Success to edit account information.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(mContext, "Failed to edit account information.(not success)", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
            Toast.makeText(mContext, "Failed to edit account information.(onCancelled)", Toast.LENGTH_LONG).show();
        }
    }

    private void dateDialog() {
        // Get Current Date
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String month, day;
                        if((monthOfYear + 1) < 10) {
                            month = "0"+(monthOfYear + 1);
                        }else {
                            month = ""+(monthOfYear + 1);
                        }

                        if(dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        }else {
                            day = "" + dayOfMonth;
                        }

                        mBirthText.setText(year + "-" + month + "-" + day);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}

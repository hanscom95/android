package se.com.band;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.com.band.login.AccountActivity;
import se.com.band.login.DeviceConnectActivity;
import se.com.band.login.LoginActivity;
import se.com.band.utility.DBManager;
import se.com.band.utility.NetworkConnection;
import se.com.band.utility.ULog;

import static android.Manifest.permission.READ_CONTACTS;

public class IntroActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    Context mContext;

    UserLoginTask mAuthTask = null;
    DBManager dbManager;

    ScrollView mFormLayout;

    Button mLoginButton, mSignupButton;
    View mProgressView;

    AutoCompleteTextView mIdView;
    EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dbManager = new DBManager(getApplicationContext(), "apex.db", null, 1);
        setContentView(R.layout.content_intro);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED ) {
//
//            // Should we show an explanation?
//            if (shouldShowRequestPermissionRationale(Manifest.permission.MANAGE_DOCUMENTS)) {
//                // Explain to the user why we need to write the permission.
//                Toast.makeText(getApplicationContext(), "MANAGE_DOCUMENTS Admin storage", Toast.LENGTH_SHORT).show();
//            }
//
//            requestPermissions(new String[]{Manifest.permission.MANAGE_DOCUMENTS},
//                    0);
//        }

        mFormLayout = (ScrollView) findViewById(R.id.intro_login_view);
        mProgressView = (View) findViewById(R.id.login_progress);

        mIdView = (AutoCompleteTextView) findViewById(R.id.login_id);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mLoginButton = (Button)  findViewById(R.id.login_button);
        mSignupButton = (Button)  findViewById(R.id.signup_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(false) {
                    Intent intent = new Intent(getApplicationContext(), DeviceConnectActivity.class);
                    startActivity(intent);
                    return;
                }

                attemptLogin();
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
            }
        });
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
//                startActivity(intent);



                ArrayList<Object> mAccount = new ArrayList<>();
                mAccount.add("tester003@gmail.com");
                mAccount.add("zxcv@6825");
                mAccount.add("tester003");
                mAccount.add("1991-03-01");
//                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("email"));
                mAccount.add("188");
                mAccount.add("75");
                mAccount.add("0");
                mAccount.add("26");
                mAccount.add("02:00:CB:DE:88");//"02:00:CB:DE:88"
                mAccount.add("0");
                dbManager.dropDB();
                dbManager.insertUser(mAccount);
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        initLogin();
    }

    private void initLogin() {
        HashMap<String, Object> account = dbManager.selectAccunt();
        if(account.get("id") != null && account.get("id") != "") {
            showProgress(true);
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mIdView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mIdView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String id = mIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(id)) {
            mIdView.setError(getString(R.string.error_field_required));
            focusView = mIdView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mIdView.setError(getString(R.string.error_invalid_email));
            focusView = mIdView;
            cancel = true;
        }*/


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(id, password);
            mAuthTask.execute((Void) null);
        }
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

            mFormLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormLayout.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mFormLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mIdView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mId;
        private final String mPassword;

        boolean mAccConnect = false;
        boolean mFitConnect = false;
        boolean mHeaConnect = false;
        boolean mSleConnect = false;
        boolean mGoaConnect = false;

        NetworkConnection network;

        UserLoginTask(String email, String password) {
            mId = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;
            try {
                // Simulate network access.
                Thread.sleep(1000);
                network = new NetworkConnection(mContext);
                connect = network.login(mId, mPassword);

                if(connect) {
                    mAccConnect = network.activitySelect(mId, "3");
                    mFitConnect = network.fitnessSelect(mId, "3");
                    mHeaConnect = network.heartSelect(mId, "3");
                    mSleConnect = network.sleepSelect(mId, "3");
                    mGoaConnect = network.goalSelect(mId, "");
                }

            } catch (InterruptedException e) {
                return false;
            }

            if(!connect) {
                return false;
            }else {
                // TODO: register the new account here.
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                try {
                    ArrayList<Object> mAccount = new ArrayList<>();
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("id"));
                    mAccount.add(mPassword);
                    mAccount.add(Html.fromHtml(network.mAccountInfo.optJSONObject(0).getString("name")).toString());
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("birth"));
//                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("email"));
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("tall"));
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("weight"));
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("gender"));
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("age"));
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("device_addr"));//"02:00:CB:DE:88"
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("hand"));
                    dbManager.dropDB();
                    dbManager.insertUser(mAccount);


                    if(mAccConnect) {
                        for (int i = 0; i < network.mActivityInfo.length(); i++) {
                            ArrayList<Object> mActivity = new ArrayList<>();
                            mActivity.add(network.mActivityInfo.optJSONObject(i).getString("type"));
                            mActivity.add(network.mActivityInfo.optJSONObject(i).getString("distance"));
                            mActivity.add(network.mActivityInfo.optJSONObject(i).getString("count"));
                            mActivity.add(network.mActivityInfo.optJSONObject(i).getString("kcal"));
                            mActivity.add(network.mActivityInfo.optJSONObject(i).getString("time"));
                            mActivity.add(network.mActivityInfo.optJSONObject(i).getString("date"));
                            mActivity.add(network.mActivityInfo.optJSONObject(i).getString("duration"));
                            dbManager.insertActivity(mActivity);
                        }
                    }

                    if(mFitConnect) {
                        for (int i = 0; i < network.mFitnessInfo.length(); i++) {
                            ArrayList<Object> mFitness = new ArrayList<>();
                            mFitness.add(network.mFitnessInfo.optJSONObject(i).getString("motion"));
                            mFitness.add(network.mFitnessInfo.optJSONObject(i).getString("count"));
                            mFitness.add(network.mFitnessInfo.optJSONObject(i).getString("kcal"));
                            mFitness.add(network.mFitnessInfo.optJSONObject(i).getString("time"));
                            mFitness.add(network.mFitnessInfo.optJSONObject(i).getString("date"));
                            mFitness.add(network.mFitnessInfo.optJSONObject(i).getString("duration"));
                            dbManager.insertFitness(mFitness);
                        }
                    }

                    if(mHeaConnect) {
                        for (int i = 0; i < network.mHeartInfo.length(); i++) {
                            ArrayList<Object> mHeart = new ArrayList<>();
                            mHeart.add(network.mHeartInfo.optJSONObject(i).getString("rate"));
                            mHeart.add(network.mHeartInfo.optJSONObject(i).getString("time"));
                            mHeart.add(network.mHeartInfo.optJSONObject(i).getString("date"));
                            dbManager.insertHeart(mHeart);
                        }
                    }

                    if(mSleConnect) {
                        for (int i = 0; i < network.mSleepInfo.length(); i++) {
                            ArrayList<Object> mSleep = new ArrayList<>();
                            mSleep.add(network.mSleepInfo.optJSONObject(i).getString("type"));
                            mSleep.add(network.mSleepInfo.optJSONObject(i).getString("s_sleep"));
                            mSleep.add(network.mSleepInfo.optJSONObject(i).getString("e_sleep"));
                            mSleep.add(network.mSleepInfo.optJSONObject(i).getString("date"));
                            dbManager.insertSleep(mSleep);
                        }
                    }

                    if(mGoaConnect) {
                        for (int i = 0; i < network.mGoalInfo.length(); i++) {
                            ArrayList<Object> mGoal = new ArrayList<>();
                            mGoal.add(network.mGoalInfo.optJSONObject(i).getString("motion"));
                            mGoal.add(network.mGoalInfo.optJSONObject(i).getString("goal"));
                            mGoal.add(network.mGoalInfo.optJSONObject(i).getString("kcal"));
                            dbManager.insertGoal(mGoal);
                        }
                    }



                    Intent intent;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", network.mAccountInfo.optJSONObject(0).getString("id"));
                    bundle.putString("name", Html.fromHtml(network.mAccountInfo.optJSONObject(0).getString("name")).toString());
                    bundle.putString("birth", network.mAccountInfo.optJSONObject(0).getString("birth"));
                    bundle.putString("email", network.mAccountInfo.optJSONObject(0).getString("email"));
                    bundle.putString("tall", network.mAccountInfo.optJSONObject(0).getString("tall"));
                    bundle.putString("weight", network.mAccountInfo.optJSONObject(0).getString("weight"));
                    bundle.putString("gender", network.mAccountInfo.optJSONObject(0).getString("gender"));
                    bundle.putString("age", network.mAccountInfo.optJSONObject(0).getString("age"));
                    bundle.putString("hand", network.mAccountInfo.optJSONObject(0).getString("hand"));
                    bundle.putString("device_addr", network.mAccountInfo.optJSONObject(0).getString("device_addr"));
                    intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //finish();
            } else {
                showProgress(false);

                if (network.mMsg != null) {
                    mIdView.setError(network.mMsg);
                    mPasswordView.setError(network.mMsg);
                } else {
                    mIdView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                }
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

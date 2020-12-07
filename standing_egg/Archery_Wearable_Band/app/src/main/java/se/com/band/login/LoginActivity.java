package se.com.band.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
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
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import se.com.band.R;
import se.com.band.utility.DBManager;
import se.com.band.utility.NetworkConnection;
import se.com.band.utility.ULog;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private Context mContext;
    DBManager dbManager;


    // UI references.
    private AutoCompleteTextView mIdView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mIdSignInButton, mIdFindButton, mPwFindButton, mAccountCreateButton, mServerSearchButton, mServerInsertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dbManager = new DBManager(getApplicationContext(), "apex.db", null, 1);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mIdView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mIdSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mIdSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mAccountCreateButton = (Button) findViewById(R.id.account_create);
        mAccountCreateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);

            }
        });

        mServerSearchButton = (Button) findViewById(R.id.server_search);
        mServerSearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                showProgress(true);
//                ServerSelectTask mServerSelectTask = new ServerSelectTask("taehoon001", "3");
//                mServerSelectTask.execute((Void) null);



                Cursor cursor1 = dbManager.selectYearlyActivity();
                while (cursor1.moveToNext()) {
                    ULog.d("selectYearlyActivity: count: " + cursor1.getString(0) + "// distance: " + cursor1.getString(1) + "// kcal: " + cursor1.getString(2) + "// type: " + cursor1.getString(3) + "// date: " + cursor1.getString(4));
                }


                Cursor cursor2 = dbManager.selectYearlyFitness();
                while (cursor2.moveToNext()) {
                    ULog.d("selectYearlyFitness motion: " + cursor2.getString(0) + "// count: " + cursor2.getString(1) + "// kcal: " + cursor2.getString(2) + "// date: " + cursor2.getString(3));
                            //+ "// date: " + cursor2.getString(4) + "// date2: " + cursor2.getString(5)+ "// cursor: " + cursor2.getColumnCount());
                }


                Cursor cursor3 = dbManager.selectYearlyHeart();
                while (cursor3.moveToNext()) {
                    ULog.d("selectYearlyHeart rate: " + cursor3.getString(0) + "// time: " + cursor3.getString(1) + "// min: " + cursor3.getString(2) + "// max: " + cursor3.getString(3)
                            + "// cursor: " + cursor3.getColumnCount());
                }


                Cursor cursor4 = dbManager.selectYearlySleep();
                while (cursor4.moveToNext()) {
                    ULog.d("selectYearlySleep type: " + cursor4.getString(0) + "// s_sleep: " + cursor4.getString(1) + "// week: " + cursor4.getString(2) );
                }


                Cursor cursor5 = dbManager.selectGoal();
                while (cursor5.moveToNext()) {
                    ULog.d("selectGoal: " + cursor5.getString(0) + "// motion: " + cursor5.getString(1) + "// goal: " + cursor5.getString(2) + "// kcal: " + cursor5.getString(3)
                            + "// cursor: " + cursor5.getColumnCount());
                }
            }
        });

        mServerInsertButton = (Button) findViewById(R.id.server_insert);
        mServerInsertButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.dropDB();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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

    /**
     * Callback received when a permissions request has been completed.
     */
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
        String email = mIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
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
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
                new ArrayAdapter<>(LoginActivity.this,
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
                Thread.sleep(2000);
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
            showProgress(false);

            if (success) {
                try {
                    ArrayList<Object> mAccount = new ArrayList<>();
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("id"));
                    mAccount.add(mPassword);
                    mAccount.add(Html.fromHtml(network.mAccountInfo.optJSONObject(0).getString("name")).toString());
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("birth"));
                    mAccount.add(network.mAccountInfo.optJSONObject(0).getString("email"));
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
                    intent = new Intent(getApplicationContext(), AccountActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //finish();
            } else {
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



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ServerSelectTask extends AsyncTask<Void, Void, Boolean> {

        private final String mId;
        private final String mType;

        NetworkConnection network;

        ServerSelectTask(String id, String type) {
            mId = id;
            mType = type;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;
            try {
                // Simulate network access.
                Thread.sleep(2000);
                network = new NetworkConnection(mContext);
                connect = network.activitySelect(mId, mType);

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
            showProgress(false);

            ULog.d("success : " + success);
            if (success) {
                try {
                    for(int i = 0; i < network.mAccountInfo.length(); i++) {
                        ArrayList<Object> mAccount = new ArrayList<>();
                        mAccount.add(network.mAccountInfo.optJSONObject(i).getString("type"));
                        mAccount.add(network.mAccountInfo.optJSONObject(i).getString("distance"));
                        mAccount.add(network.mAccountInfo.optJSONObject(i).getString("count"));
                        mAccount.add(network.mAccountInfo.optJSONObject(i).getString("kcal"));
                        mAccount.add(network.mAccountInfo.optJSONObject(i).getString("time"));
                        mAccount.add(network.mAccountInfo.optJSONObject(i).getString("date"));
                        mAccount.add(network.mAccountInfo.optJSONObject(i).getString("duration"));
                        dbManager.insertActivity(mAccount);
                    }

                    Cursor cursor = dbManager.selectYearlyActivity();
                    while (cursor.moveToNext()) {
                        ULog.d("activity: " + cursor.getString(0) + "// type: " + cursor.getString(1) + "// distance: " + cursor.getString(2) + "// count: " + cursor.getString(3)
                                + "// kcal: " + cursor.getString(4) + "// time: " + cursor.getString(5) + "// date: " + cursor.getString(6)
                                + "// cursor: " + cursor.getColumnCount());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //finish();
            } else {
                Toast.makeText(mContext, "error ------", Toast.LENGTH_LONG);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


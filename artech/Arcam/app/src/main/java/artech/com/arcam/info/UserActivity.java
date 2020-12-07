package artech.com.arcam.info;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import artech.com.arcam.R;
import artech.com.arcam.finetuning.FineTuningActivity;
import artech.com.arcam.login.DetailActivity;
import artech.com.arcam.login.IntroActivity;
import artech.com.arcam.login.TermsActivity;
import artech.com.arcam.record.RecordActivity;
import artech.com.arcam.scope.ScopeActivity;
import artech.com.arcam.score.ScoreActivity;
import artech.com.arcam.utility.DBManager;
import artech.com.arcam.utility.NetworkConnection;


public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    DBManager mDbManager;

    EditText mUserNameEdit, mPasswordEdit, mTeamEdit, mEmaillEdit, mCameraUrlEdit;
    Button mCancelButton, mInsertButton, mMaleButton, mFemaleButton;
    Spinner mNationalSpinner;

    AdminTask mAdminTask;

    String mDate = "";

    int seInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
        }

        mContext = this;
        mDbManager = new DBManager(mContext, "arcam.db", null, 1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_wh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_wh);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView navEmailText = (TextView) drawer.findViewById(R.id.email_text);
                String email = mDbManager.selectEmail();
                if(!"".equals(email)) {
                    navEmailText.setText(email);
                }
            }
        };
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(4).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);


//        NavigationView navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
//        navigationRightView.setNavigationItemSelectedListener(this);

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mInsertButton = (Button) findViewById(R.id.insert_button);
        mMaleButton = (Button) findViewById(R.id.male_button);
        mFemaleButton = (Button) findViewById(R.id.female_button);

        mNationalSpinner = (Spinner) findViewById(R.id.national_spinner);

        mUserNameEdit = (EditText) findViewById(R.id.username_text);
        mPasswordEdit = (EditText) findViewById(R.id.password_text);
        mTeamEdit = (EditText) findViewById(R.id.team_text);
        mEmaillEdit = (EditText) findViewById(R.id.user_email_text);
        mCameraUrlEdit = (EditText) findViewById(R.id.camera_text);


        String[] locales = Locale.getISOCountries();
        for(int i = 0; i < Locale.getISOCountries().length ; i++) {
            Locale locale = new Locale( "en", locales[i]);
            locales[i] = locale.getDisplayCountry(Locale.ENGLISH);

            Log.d("UserActivity", "getDisplayName : " + locale.getDisplayName() + " / getDisplayCountry : " + locale.getDisplayCountry());
        }
        Arrays.sort(locales);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, locales);
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mNationalSpinner.setAdapter(adapter);


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

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.add(mNationalSpinner.getSelectedItemPosition());
                arrayList.add(mTeamEdit.getText().toString());
                arrayList.add(seInt);
                arrayList.add(mDate);
                arrayList.add(mCameraUrlEdit.getText().toString());
                arrayList.add(mEmaillEdit.getText().toString());
                mAdminTask = new AdminTask(1, arrayList);
                mAdminTask.execute((Void) null);
            }
        });

        initSetting();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//        }else if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("MainActivity", "id : " + item.getTitle().toString());

        if (id == R.id.nav_finetuning) {
            Intent intent = new Intent(mContext, FineTuningActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_scope) {
            Intent intent = new Intent(mContext, ScopeActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_score) {
            Intent intent = new Intent(mContext, ScoreActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_record) {
            Intent intent = new Intent(mContext, RecordActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_equipment) {
            Intent intent = new Intent(mContext, EquipmentActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_info) {
            Intent intent = new Intent(mContext, InfoActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.logout_message)
                    .setPositiveButton(R.string.logout_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDbManager.dropDB();

                            Intent intent = new Intent(mContext, IntroActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton(R.string.logout_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//        }else if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
        }

        return true;
    }


    private void initSetting() {
        Cursor cursor = mDbManager.selectUser();
        if (cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                mUserNameEdit.setText(cursor.getString(0));
                mNationalSpinner.setSelection(cursor.getInt(2));
                mTeamEdit.setText(cursor.getString(3));

                int sex = cursor.getInt(4);
                if(sex ==  1) {
                    mMaleButton.callOnClick();
                }else if(sex ==  2) {
                    mFemaleButton.callOnClick();
                }

                mDate = cursor.getString(5);

                mCameraUrlEdit.setText(cursor.getString(6));
                mEmaillEdit.setText(cursor.getString(7));
            }
        }
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
                    connect = network.accountUpdate(mArrayList);
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
                mDbManager.updateUser(mArrayList);
                finish();
            }else {
//                Toast.makeText(mContext, getString(R.string.signup_insert_error), Toast.LENGTH_SHORT).show();
                mDbManager.updateUser(mArrayList);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAdminTask = null;
        }
    }
}

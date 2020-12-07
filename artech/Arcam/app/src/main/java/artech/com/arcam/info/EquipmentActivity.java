package artech.com.arcam.info;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import artech.com.arcam.MainActivity;
import artech.com.arcam.R;
import artech.com.arcam.finetuning.FineTuningActivity;
import artech.com.arcam.login.IntroActivity;
import artech.com.arcam.login.SignUpActivity;
import artech.com.arcam.record.RecordActivity;
import artech.com.arcam.scope.ScopeActivity;
import artech.com.arcam.score.ScoreActivity;
import artech.com.arcam.utility.DBManager;
import artech.com.arcam.utility.NetworkConnection;


public class EquipmentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    DBManager mDbManager;

    Button mCancelButton, mInsertButton;

    CheckBox mAutoCheck;

    Spinner mRiserBrandSpinner, mRiserInchSpinner, mLimbsBrandSpinner, mLimbsInchSpinner, mStabilizerInchSpinner,
            mLongstaffInchSpinner, mSidestabiInchSpinner, mFingerTabBrandSpinner, mSightBrandSpinner, mCushionPlungerBrandSpinner, mRestBrandSpinner,
            mArrowBrandSpinner;

    EditText mRiserBrandText, mRiserInchText, mLimbsBrandText, mLimbsInchText, mLimbsPoundText, mStabilizerInchText,
            mLongstaffInchText, mSidestabiInchText, mFingerTabBrandText, mSightBrandText, mCushionPlungerBrandText, mRestBrandText,
            mArrowBrandText;

    EquipmentTask mEquipmentTask;

    int mBraceHigh = 0;
    int mTillerHigh = 0;
    int mNockingPoint = 0;
    int mWeather = 0;
    int mWind = 0;
    int mDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "arcam.db", null, 1);

        setContentView(R.layout.activity_equipment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_wh);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        navigationView.getMenu().getItem(6).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);


//        NavigationView navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
//        navigationRightView.setNavigationItemSelectedListener(this);

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mInsertButton = (Button) findViewById(R.id.insert_button);

        mAutoCheck = (CheckBox) findViewById(R.id.auto_save_checkbox);

        mRiserBrandSpinner = (Spinner) findViewById(R.id.riser_brand_spinner);
        mRiserInchSpinner = (Spinner) findViewById(R.id.riser_inch_spinner);
        mLimbsBrandSpinner = (Spinner) findViewById(R.id.limbs_brand_spinner);
        mLimbsInchSpinner = (Spinner) findViewById(R.id.limbs_inch_spinner);
        mStabilizerInchSpinner = (Spinner) findViewById(R.id.stabilizer_brand_spinner);
        mLongstaffInchSpinner = (Spinner) findViewById(R.id.longstaff_inch_spinner);
        mSidestabiInchSpinner = (Spinner) findViewById(R.id.sidestabi_inch_spinner);
        mFingerTabBrandSpinner = (Spinner) findViewById(R.id.fingertab_brand_spinner);
        mSightBrandSpinner = (Spinner) findViewById(R.id.sight_brand_spinner);
        mCushionPlungerBrandSpinner = (Spinner) findViewById(R.id.cushionplunger_brand_spinner);
        mRestBrandSpinner = (Spinner) findViewById(R.id.rest_brand_spinner);
        mArrowBrandSpinner = (Spinner) findViewById(R.id.arrow_brand_spinner);


        mRiserBrandText = (EditText) findViewById(R.id.riser_brand_edittext);
        mRiserInchText = (EditText) findViewById(R.id.riser_inch_edittext);
        mLimbsBrandText = (EditText) findViewById(R.id.limbs_brand_edittext);
        mLimbsInchText = (EditText) findViewById(R.id.limbs_inch_edittext);
        mLimbsPoundText = (EditText) findViewById(R.id.limbs_pound_edittext);
        mStabilizerInchText = (EditText) findViewById(R.id.stabilizer_brand_edittext);
        mLongstaffInchText = (EditText) findViewById(R.id.longstaff_inch_edittext);
        mSidestabiInchText = (EditText) findViewById(R.id.sidestabi_inch_edittext);
        mFingerTabBrandText = (EditText) findViewById(R.id.fingertab_brand_edittext);
        mSightBrandText = (EditText) findViewById(R.id.sight_brand_edittext);
        mCushionPlungerBrandText = (EditText) findViewById(R.id.cushionplunger_brand_edittext);
        mRestBrandText = (EditText) findViewById(R.id.rest_brand_edittext);
        mArrowBrandText = (EditText) findViewById(R.id.arrow_brand_edittext);


        ArrayAdapter<String> adapter;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.riser));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mRiserBrandSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.riser_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mRiserInchSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.limbs));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mLimbsBrandSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.limbs_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mLimbsInchSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.stabilizer));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mStabilizerInchSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.longstaff_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mLongstaffInchSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.sidestabi_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mSidestabiInchSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.fingertab));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mFingerTabBrandSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.sight));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mSightBrandSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.cushionplunger));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mCushionPlungerBrandSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.rest));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mRestBrandSpinner.setAdapter(adapter);
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.arrow));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mArrowBrandSpinner.setAdapter(adapter);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    ArrayList<Object> arrayList = new ArrayList<>();
                    arrayList.add(mRiserBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mRiserBrandText.getText().toString());
                    arrayList.add(mRiserInchSpinner.getSelectedItemPosition());
                    arrayList.add(mRiserInchText.getText().toString());
                    arrayList.add(mLimbsBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mLimbsBrandText.getText().toString());
                    arrayList.add(mLimbsInchSpinner.getSelectedItemPosition());
                    arrayList.add(mLimbsInchText.getText().toString());
                    arrayList.add(mLimbsPoundText.getText().toString());
                    arrayList.add(mStabilizerInchSpinner.getSelectedItemPosition());
                    arrayList.add(mStabilizerInchText.getText().toString());
                    arrayList.add(mLongstaffInchSpinner.getSelectedItemPosition());
                    arrayList.add(mLongstaffInchText.getText().toString());
                    arrayList.add(mSidestabiInchSpinner.getSelectedItemPosition());
                    arrayList.add(mSidestabiInchText.getText().toString());
                    arrayList.add(mFingerTabBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mFingerTabBrandText.getText().toString());
                    arrayList.add(mSightBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mSightBrandText.getText().toString());
                    arrayList.add(mCushionPlungerBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mCushionPlungerBrandText.getText().toString());
                    arrayList.add(mRestBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mRestBrandText.getText().toString());
                    arrayList.add(mArrowBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mArrowBrandText.getText().toString());
                    if (mAutoCheck.isChecked()) {
                        arrayList.add(1);
                    } else {
                        arrayList.add(0);
                    }
                    arrayList.add(mBraceHigh);
                    arrayList.add(mTillerHigh);
                    arrayList.add(mNockingPoint);
                    arrayList.add(mWeather);
                    arrayList.add(mWind);
                    arrayList.add(mDistance);

//                    mDbManager.updateEquipment(arrayList);

                    mEquipmentTask = new EquipmentTask(arrayList);
                    mEquipmentTask.execute((Void) null);

//                    finish();
                }catch (Exception e) {
                    Toast.makeText(mContext, getText(R.string.detail_insert_error).toString(), Toast.LENGTH_LONG).show();
                }
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
        }else if (id == R.id.nav_user) {
            Intent intent = new Intent(mContext, UserActivity.class);
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
        Cursor cursor = mDbManager.selectEquipment();
        if(cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {

                mInsertButton.setText(R.string.detail_button);


                mRiserBrandSpinner.setSelection(cursor.getInt(0));
                mRiserInchSpinner.setSelection(cursor.getInt(2));
                mLimbsBrandSpinner.setSelection(cursor.getInt(4));
                mLimbsInchSpinner.setSelection(cursor.getInt(6));
                mStabilizerInchSpinner.setSelection(cursor.getInt(9));
                mLongstaffInchSpinner.setSelection(cursor.getInt(11));
                mSidestabiInchSpinner.setSelection(cursor.getInt(13));
                mFingerTabBrandSpinner.setSelection(cursor.getInt(15));
                mSightBrandSpinner.setSelection(cursor.getInt(17));
                mCushionPlungerBrandSpinner.setSelection(cursor.getInt(19));
                mRestBrandSpinner.setSelection(cursor.getInt(21));
                mArrowBrandSpinner.setSelection(cursor.getInt(23));

                mRiserBrandText.setText(cursor.getString(1));
                mRiserInchText.setText(cursor.getString(3));
                mLimbsBrandText.setText(cursor.getString(5));
                mLimbsInchText.setText(cursor.getString(7));
                mLimbsPoundText.setText(cursor.getString(8));
                mStabilizerInchText.setText(cursor.getString(10));
                mLongstaffInchText.setText(cursor.getString(12));
                mSidestabiInchText.setText(cursor.getString(14));
                mFingerTabBrandText.setText(cursor.getString(16));
                mSightBrandText.setText(cursor.getString(18));
                mCushionPlungerBrandText.setText(cursor.getString(20));
                mRestBrandText.setText(cursor.getString(22));
                mArrowBrandText.setText(cursor.getString(24));

                if(cursor.getInt(25) == 1) {
                    mAutoCheck.setChecked(true);
                }else {
                    mAutoCheck.setChecked(false);
                }

                mBraceHigh = cursor.getInt(26);
                mTillerHigh = cursor.getInt(27);
                mNockingPoint = cursor.getInt(28);
                mWeather = cursor.getInt(29);
                mWind = cursor.getInt(30);
                mDistance = cursor.getInt(31);
            }
        }
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class EquipmentTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        ArrayList<Object> mArrayList;

        EquipmentTask(ArrayList<Object> arrayList) {
            mArrayList = arrayList;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.add(mDbManager.selectId());
                for(int i = 0;  i < mArrayList.size(); i++) {
                    arrayList.add(mArrayList.get(i));
                }


                network = new NetworkConnection(mContext);
                connect = network.equipmentUpdate(arrayList);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mEquipmentTask = null;

            mDbManager.updateEquipment(mArrayList);
            finish();
        }

        @Override
        protected void onCancelled() {
            mEquipmentTask = null;
        }
    }
}

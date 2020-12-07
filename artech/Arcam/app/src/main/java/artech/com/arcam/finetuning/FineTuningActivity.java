package artech.com.arcam.finetuning;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import artech.com.arcam.MainActivity;
import artech.com.arcam.R;
import artech.com.arcam.info.EquipmentActivity;
import artech.com.arcam.info.InfoActivity;
import artech.com.arcam.info.UserActivity;
import artech.com.arcam.login.IntroActivity;
import artech.com.arcam.record.RecordActivity;
import artech.com.arcam.scope.ScopeActivity;
import artech.com.arcam.score.ScoreActivity;
import artech.com.arcam.utility.DBManager;


public class FineTuningActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    DBManager mDbManager;

    Button mCancelButton, mSaveButton;

    Spinner mDistanceSpinner;

    EditText mBraceHighEdit, mTillerHighEdit, mNockingPointEdit;

    RadioGroup mWeatherRadioGroup, mWindRadioGroup;

    ImageView mSettingImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "arcam.db", null, 1);

        setContentView(R.layout.activity_fine_tuning);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_wh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_wh);

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
                TextView navEmailText = (TextView) drawer.findViewById(R.id.email_text);
                String email = mDbManager.selectEmail();
                if(!"".equals(email)) {
                    navEmailText.setText(email);
                }

                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);


//        NavigationView navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
//        navigationRightView.setNavigationItemSelectedListener(this);

        mSettingImg = (ImageView) findViewById(R.id.setting_button);

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mDistanceSpinner = (Spinner) findViewById(R.id.distance_spinner);

        ArrayAdapter<String> adapter;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.distance));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mDistanceSpinner.setAdapter(adapter);


        mBraceHighEdit = (EditText) findViewById(R.id.brace_high_edittext);
        mTillerHighEdit = (EditText) findViewById(R.id.tiller_high_edittext);
        mNockingPointEdit = (EditText) findViewById(R.id.nocking_point_edittext);

        mWeatherRadioGroup = (RadioGroup) findViewById(R.id.weather_radio_group);
        mWindRadioGroup = (RadioGroup) findViewById(R.id.wind_radio_group);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<Object> arrayList = new ArrayList<>();

                    if("".equals(mBraceHighEdit.getText().toString())) {
                        arrayList.add(0);
                    }else {
                        arrayList.add(Integer.parseInt(mBraceHighEdit.getText().toString()));
                    }

                    if("".equals(mTillerHighEdit.getText().toString())) {
                        arrayList.add(0);
                    }else {
                        arrayList.add(Integer.parseInt(mTillerHighEdit.getText().toString()));
                    }

                    if("".equals(mNockingPointEdit.getText().toString())) {
                        arrayList.add(0);
                    }else {
                        arrayList.add(Integer.parseInt(mNockingPointEdit.getText().toString()));
                    }

                    int weatherIndex = mWeatherRadioGroup.indexOfChild(mWeatherRadioGroup.findViewById(mWeatherRadioGroup.getCheckedRadioButtonId()));
                    int windIndex = mWindRadioGroup.indexOfChild(mWindRadioGroup.findViewById(mWindRadioGroup.getCheckedRadioButtonId()));
                    arrayList.add(weatherIndex);
                    arrayList.add(windIndex);
                    arrayList.add(mDistanceSpinner.getSelectedItemPosition());

                    mDbManager.updateFineTuning(arrayList);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e) {
                    Toast.makeText(mContext, getText(R.string.detail_insert_error).toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        mSettingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mDbManager.selectUrl();
                final EditText editText = new EditText(mContext);
                if("".equals(url)) {
                    String URL = "rtsp://192.168.0.100:554/ch1/sub/av_stream";//"rtmp://192.168.1.88:1935/flash/11:admin:admin";//"rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
                    editText.setText(URL);
                }else {
                    editText.setText(url);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(getString(R.string.url));
                builder.setView(editText);
                builder.setPositiveButton(getString(R.string.confirm_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String popupUrl = editText.getText().toString();
                        Log.d("MainActivity", "editText url : " + popupUrl);
//                        ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
//                        fragment.mFilePath = popupUrl;
                        mDbManager.updateUrl(popupUrl);
                    }
                }).setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog alertDialog = builder.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });

//        mSkipButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });


//        mHandler.postDelayed(sRunnable, 500);

//        ArrayAdapter<String> adapter;
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.riser));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mRiserBrandSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.riser_inch));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mRiserInchSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.limbs));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mLimbsBrandSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.limbs_inch));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mLimbsInchSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.stabilizer));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mStabilizerInchSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.longstaff_inch));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mLongstaffInchSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sidestabi_inch));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mSidestabiInchSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.fingertab));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mFingerTabBrandSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sight));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mSightBrandSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cushionplunger));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mCushionPlungerBrandSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.rest));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mRestBrandSpinner.setAdapter(adapter);
//        adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrow));
//        adapter.setDropDownViewResource(R.layout.signup_spinner);
//        mArrowBrandSpinner.setAdapter(adapter);

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

        if (id == R.id.nav_scope) {
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
                mBraceHighEdit.setText(""+cursor.getInt(26));
                mTillerHighEdit.setText(""+cursor.getInt(27));
                mNockingPointEdit.setText(""+cursor.getInt(28));

                int weather = cursor.getInt(29);
                int wind = cursor.getInt(30);
                int auto = cursor.getInt(25);

                if(weather == 0) {
                    mWeatherRadioGroup.check(R.id.sunny_radio);
                }else if(weather == 1) {
                    mWeatherRadioGroup.check(R.id.cloud_radio);
                }else if(weather == 2) {
                    mWeatherRadioGroup.check(R.id.fog_radio);
                }else if(weather == 3) {
                    mWeatherRadioGroup.check(R.id.typhoon_radio);
                }else if(weather == 4) {
                    mWeatherRadioGroup.check(R.id.rain_radio);
                }else if(weather == 5) {
                    mWeatherRadioGroup.check(R.id.snow_radio);
                }

                if(wind == 0) {
                    mWindRadioGroup.check(R.id.wind_zero_radio);
                }else if(wind == 1) {
                    mWindRadioGroup.check(R.id.wind_one_radio);
                }else if(wind == 2) {
                    mWindRadioGroup.check(R.id.wind_two_radio);
                }else if(wind == 3) {
                    mWindRadioGroup.check(R.id.wind_three_radio);
                }else if(wind == 4) {
                    mWindRadioGroup.check(R.id.wind_four_radio);
                }

                mDistanceSpinner.setSelection(cursor.getInt(31));
            }
        }
    }
}

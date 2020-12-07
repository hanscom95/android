package artech.com.arcam.scope;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import artech.com.arcam.R;
import artech.com.arcam.autoScoringNativeClass;
import artech.com.arcam.finetuning.FineTuningActivity;
import artech.com.arcam.info.EquipmentActivity;
import artech.com.arcam.info.InfoActivity;
import artech.com.arcam.info.UserActivity;
import artech.com.arcam.login.IntroActivity;
import artech.com.arcam.record.RecordActivity;
import artech.com.arcam.score.ArcheryCameraFragment;
import artech.com.arcam.score.ScoreActivity;
import artech.com.arcam.score.ScorePagerAdapter;
import artech.com.arcam.utility.AutoScoreView;
import artech.com.arcam.utility.DBManager;
import artech.com.arcam.utility.TouchView;

import static org.opencv.imgproc.Imgproc.cvtColor;

public class ScopeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        Context mContext;

        DBManager dbManager;

        View mCameraView;

        TextView mBraceHighText, mTillerHighText, mNockingPointText;
        ImageView mHomeImg, mReflashImg, mZoomInImg, mZoomOutImg;
        ImageView mWeatherImg, mWindImg, mDistanceImg;
        static ImageView mCameraFocusImg;

//        String URL = "rtsp://192.168.0.100:554/ch1/sub/av_stream";
        String URL = "rtsp://192.168.31.25:554/ch1/sub/av_stream";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scope);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
            }

            mContext = this;
            dbManager = new DBManager(mContext, "arcam.db", null, 1);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_menu);
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu);

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
                    String email = dbManager.selectEmail();
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
            navigationView.getMenu().getItem(1).setChecked(true);
            navigationView.setNavigationItemSelectedListener(this);


//            NavigationView navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
//            navigationRightView.setNavigationItemSelectedListener(this);


            URL = dbManager.selectUrl();
            if (null == savedInstanceState) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.archery_video_view, ArcheryCameraFragment.newInstance(URL, "scope"),"scope").commit();
            }

            mCameraView = (View) findViewById(R.id.archery_video_view);

            mBraceHighText = (TextView) findViewById(R.id.first_tab_brace_high_score_text);
            mTillerHighText = (TextView) findViewById(R.id.first_tab_tiller_high_score_text);
            mNockingPointText = (TextView) findViewById(R.id.first_tab_nocking_point_score_text);

            mHomeImg = (ImageView) findViewById(R.id.home_button);
            mReflashImg = (ImageView) findViewById(R.id.back_button);
            mZoomInImg = (ImageView) findViewById(R.id.zoomin_button);
            mZoomOutImg = (ImageView) findViewById(R.id.zoomout_button);
            mWeatherImg = (ImageView) findViewById(R.id.tab_first_weather_img);
            mWindImg = (ImageView) findViewById(R.id.tab_first_wind_img);
            mDistanceImg = (ImageView) findViewById(R.id.tab_first_distance_img);
            mCameraFocusImg = (ImageView) findViewById(R.id.camera_focus_img);

            mHomeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            mReflashImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                    fragment.updateInstance();
                }
            });

            initSetting();
    }

    private void initSetting() {
        int weather = -1;
        int wind = -1;
        int distance = -1;
        Cursor cursor = dbManager.selectTuningInfo();
        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mBraceHighText.setText("" + cursor.getInt(0));
                mTillerHighText.setText("" + cursor.getInt(1));
                mNockingPointText.setText("" + cursor.getInt(2));
                weather = cursor.getInt(3);
                wind = cursor.getInt(4);
                distance = cursor.getInt(5);

                if (weather == 0) {
                    mWeatherImg.setImageResource(R.mipmap.ic_sunny_white);
                } else if (weather == 1) {
                    mWeatherImg.setImageResource(R.mipmap.ic_cloudy_white);
                } else if (weather == 2) {
                    mWeatherImg.setImageResource(R.mipmap.ic_fog_white);
                } else if (weather == 3) {
                    mWeatherImg.setImageResource(R.mipmap.ic_typhoon_non_press);
                } else if (weather == 4) {
                    mWeatherImg.setImageResource(R.mipmap.ic_rain_white);
                } else if (weather == 5) {
                    mWeatherImg.setImageResource(R.mipmap.ic_snow_white);
                }


                if (wind == 0) {
                    mWindImg.setImageResource(R.mipmap.ic_wind_zero_white);
                } else if (wind == 1) {
                    mWindImg.setImageResource(R.mipmap.ic_wind_one_white);
                } else if (wind == 2) {
                    mWindImg.setImageResource(R.mipmap.ic_wind_two_white);
                } else if (wind == 3) {
                    mWindImg.setImageResource(R.mipmap.ic_wind_three_white);
                } else if (wind == 4) {
                    mWindImg.setImageResource(R.mipmap.ic_wind_four_white);
                }


                if (distance == 0) {
                    mDistanceImg.setImageResource(R.mipmap.ic_18msc_press);
                } else if (distance == 1) {
                    mDistanceImg.setImageResource(R.mipmap.ic_30msc_press);
                } else if (distance == 2) {
                    mDistanceImg.setImageResource(R.mipmap.ic_50msc_press);
                } else if (distance == 3) {
                    mDistanceImg.setImageResource(R.mipmap.ic_70msc_press);
                }
            }
        }
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_finetuning) {
            Intent intent = new Intent(mContext, FineTuningActivity.class);
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
                            dbManager.dropDB();

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

    public static void setViewVisibility(boolean visibility) {
        if(visibility) {
            mCameraFocusImg.setVisibility(View.VISIBLE);
        }else {
            mCameraFocusImg.setVisibility(View.GONE);
        }
    }
}

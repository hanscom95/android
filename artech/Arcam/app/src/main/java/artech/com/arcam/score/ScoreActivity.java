package artech.com.arcam.score;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import artech.com.arcam.scope.ScopeActivity;
import artech.com.arcam.utility.AutoScoreView;
import artech.com.arcam.utility.DBManager;
import artech.com.arcam.utility.TouchView;

import static org.opencv.imgproc.Imgproc.cvtColor;

public class ScoreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        Context mContext;

        DBManager dbManager;

        ImageView mSettingButton, mCaptureButton;

        ProgressBar mCameraProgressBar;

        View goalView, mCameraView;
        ViewPager mViewPager;

        ScorePagerAdapter mAdapter;

        CheckBox upLeftCheckbox, upCheckbox, upRightCheckbox, leftCheckbox, rightCheckbox, downLeftCheckbox, downRightCheckbox, downCheckbox;
        CheckBox goalZeroCheckbox, goalOneCheckbox, goalTwoCheckbox, goalThreeCheckbox, goalFourCheckbox, goalFiveCheckbox, goalSixCheckbox, goalSevenCheckbox, goalEightCheckbox, goalNineCheckbox, goalTenCheckbox, goalFullScaleCheckbox;
        CheckBox mAutoCheckbox;

        TextView mArrowText, mTotalText, mBraceHighText, mTillerHighText, mNockingPointText, mDateText;
        TextView mInfoWeatherText, mInfoWindText, mInfoDistanceText;

        Button mTabRound1Button, mTabRound2Button, mTabRound3Button, mTabRound4Button, mTabRound5Button;
        Button mDelButton, mEnterButton, mSaveButton;
        ImageButton mZoominButton, mZoomoutButton, mFullscreenButton;

        RelativeLayout mInfoBraceHighLayout, mInfoTillerHighLayout, mInfoNockingPointLayout;

        ImageView mTabRound2LcokImg, mTabRound3LcokImg, mTabRound4LcokImg, mTabRound5LcokImg;
        ImageView mArcheryImg;

        static TextView mVideoText;
        static ImageView mPlayImg;

        TouchView mTouchView;
        AutoScoreView mAutoResultView, mAutoResult2View, mAutoResult3View;

        ArrayList<Integer> round_score = new ArrayList<>();
        ArrayList<Integer> round_direction = new ArrayList<>();

        TimerTask mTask;
        Timer mTimer;
        Mat frame1_, frame2_, frame1__, debugImg_, input, output, frame1Clone;

        int mWeather = -1;
        int mWind = -1;
        int mDistance = -1;

        int score = 0;
        int round = 0;
        int direction = -1;
        int goal = -1;

        int zeroCount = 0;
        int oneCount = 0;
        int twoCount = 0;
        int threeCount = 0;
        int fourCount = 0;
        int fiveCount = 0;
        int sixCount = 0;
        int sevenCount = 0;
        int eighCount = 0;
        int nineCount = 0;
        int tenCount = 0;
        int xTenCount = 0;

        boolean mPopupBoolean = false;


        //auto scoring variable
        int scoreOut = -1;
        int callCounter = 0;
        int functionCall = 0;
        int firstCall = 0;
        int callCounterLast = 0;

        int oneTime = 0;
        int outputTarget = -1;
        int outputTargetState = 0;
        int isMotionDetected = 0;

        boolean frame1flag = false;
        boolean frame2flag = false;

        boolean mState = false;
        boolean mPosition = true;
        boolean mCount = true;
        boolean mTestBoard = true;
        boolean newScoreFlag = false;
        boolean firstCallFlag = true;

        int[] colorCoors = new int[10];
        float[] foundElipses = new float[20];

        static {
            System.loadLibrary("MyLibs");
            System.loadLibrary("MyOpencvLibs");

            if (OpenCVLoader.initDebug()) {
                // Do nothing to manage the error
            }
        }

//        String URL = "rtsp://192.168.0.100:554/ch1/sub/av_stream";
        String URL = "rtsp://192.168.31.25:554/ch1/sub/av_stream";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_score);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
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
            navigationView.getMenu().getItem(2).setChecked(true);
            navigationView.setNavigationItemSelectedListener(this);


//            NavigationView navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
//            navigationRightView.setNavigationItemSelectedListener(this);


            URL = dbManager.selectUrl();
            if (null == savedInstanceState) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.archery_video_view, ArcheryCameraFragment.newInstance(URL, "score"),"score").commit();
            }
            mCameraView = (View) findViewById(R.id.archery_video_view);

            mTabRound1Button = (Button) findViewById(R.id.tab_round_1_button);
            mTabRound2Button = (Button) findViewById(R.id.tab_round_2_button);
            mTabRound3Button = (Button) findViewById(R.id.tab_round_3_button);
            mTabRound4Button = (Button) findViewById(R.id.tab_round_4_button);
            mTabRound5Button = (Button) findViewById(R.id.tab_round_5_button);

            mDelButton = (Button) findViewById(R.id.number_del_button);
            mEnterButton = (Button) findViewById(R.id.enter_button);
            mSaveButton = (Button) findViewById(R.id.save_button);

            mSettingButton = (ImageView) findViewById(R.id.setting_button);
            mCaptureButton = (ImageView) findViewById(R.id.bell_button);
            mTabRound2LcokImg = (ImageView) findViewById(R.id.tab2_lock_img);
            mTabRound3LcokImg = (ImageView) findViewById(R.id.tab3_lock_img);
            mTabRound4LcokImg = (ImageView) findViewById(R.id.tab4_lock_img);
            mTabRound5LcokImg = (ImageView) findViewById(R.id.tab5_lock_img);
            mArcheryImg = (ImageView) findViewById(R.id.archery_image);

            mViewPager = (ViewPager) findViewById(R.id.board_viewPager);

            mInfoWeatherText = (TextView) findViewById(R.id.info_weather_text);
            mInfoWindText = (TextView) findViewById(R.id.info_wind_text);
            mInfoDistanceText = (TextView) findViewById(R.id.info_distance_text);

            mArrowText = (TextView) findViewById(R.id.arrow_text);
            mTotalText = (TextView) findViewById(R.id.total_text);
            mBraceHighText = (TextView) findViewById(R.id.brace_high_value_text);
            mTillerHighText = (TextView) findViewById(R.id.tiller_high_value_text);
            mNockingPointText = (TextView) findViewById(R.id.nocking_point_value_text);
            mDateText = (TextView) findViewById(R.id.tab1_date_text);

            goalZeroCheckbox = (CheckBox) findViewById(R.id.number_0_check);
            goalOneCheckbox = (CheckBox) findViewById(R.id.number_1_check);
            goalTwoCheckbox = (CheckBox) findViewById(R.id.number_2_check);
            goalThreeCheckbox = (CheckBox) findViewById(R.id.number_3_check);
            goalFourCheckbox = (CheckBox) findViewById(R.id.number_4_check);
            goalFiveCheckbox = (CheckBox) findViewById(R.id.number_5_check);
            goalSixCheckbox = (CheckBox) findViewById(R.id.number_6_check);
            goalSevenCheckbox = (CheckBox) findViewById(R.id.number_7_check);
            goalEightCheckbox = (CheckBox) findViewById(R.id.number_8_check);
            goalNineCheckbox = (CheckBox) findViewById(R.id.number_9_check);
            goalTenCheckbox = (CheckBox) findViewById(R.id.number_10_check);
            goalFullScaleCheckbox = (CheckBox) findViewById(R.id.number_xten_check);

            upLeftCheckbox = (CheckBox) findViewById(R.id.arrow_top_left_check);
            upCheckbox = (CheckBox) findViewById(R.id.arrow_top_check);
            upRightCheckbox = (CheckBox) findViewById(R.id.arrow_top_right_check);
            leftCheckbox = (CheckBox) findViewById(R.id.arrow_left_check);
            rightCheckbox = (CheckBox) findViewById(R.id.arrow_right_check);
            downLeftCheckbox = (CheckBox) findViewById(R.id.arrow_down_left_check);
            downRightCheckbox = (CheckBox) findViewById(R.id.arrow_down_right_check);
            downCheckbox = (CheckBox) findViewById(R.id.arrow_down_check);

            goalZeroCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalOneCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalTwoCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalThreeCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalFourCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalFiveCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalSixCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalSevenCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalEightCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalNineCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalTenCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);
            goalFullScaleCheckbox.setOnCheckedChangeListener(goalNumberCheckedChangeListener);

            upLeftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
            upCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
            upRightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
            leftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
            rightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
            downLeftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
            downRightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
            downCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);

            mAutoCheckbox = (CheckBox) findViewById(R.id.auto_check);

            mVideoText = (TextView) findViewById(R.id.video_text);
            mPlayImg = (ImageView) findViewById(R.id.play_img);
            mPlayImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                    fragment.updateInstance();
                }
            });

            mCameraProgressBar = (ProgressBar) findViewById(R.id.camera_progressBar);

            mInfoBraceHighLayout = (RelativeLayout) findViewById(R.id.brace_high_layout);
            mInfoTillerHighLayout = (RelativeLayout) findViewById(R.id.tiller_high_layout);
            mInfoNockingPointLayout = (RelativeLayout) findViewById(R.id.nocking_point_layout);

            mTouchView = (TouchView) findViewById(R.id.archery_video_touch_view);
            mAutoResultView = (AutoScoreView) findViewById(R.id.archery_auto_score_result_view);
            mAutoResult2View = (AutoScoreView) findViewById(R.id.archery_auto_score_result2_view);
            mAutoResult3View = (AutoScoreView) findViewById(R.id.archery_auto_score_result3_view);

            mAdapter = new ScorePagerAdapter();
            mViewPager.setAdapter(mAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position == 0) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_active);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);
                    }else if(position == 1) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_active);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);
                    }else if(position == 2) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_active);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);
                    }else if(position == 3) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_active);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);
                    }else if(position == 4) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_active);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            LayoutInflater inflater = getLayoutInflater();
            goalView = inflater.inflate(R.layout.content_score_board, null);

            mAdapter.addView (goalView, 0);
            mAdapter.notifyDataSetChanged();



            mCaptureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mArcheryImg.setVisibility(View.VISIBLE);
                    ArcheryCameraFragment fragment = null;
                    fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                    Bitmap cameraBitmap = (Bitmap) fragment.screenShot();
                    mArcheryImg.setImageBitmap(cameraBitmap);


                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    AlertDialog alertDialog = builder.setMessage(R.string.score_screen_capture_message).setNegativeButton(R.string.score_screen_capture_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton(R.string.score_screen_capture_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            View imageDownloadView = getWindow().getDecorView().getRootView();
                            Bitmap bitmap;

                            imageDownloadView.setDrawingCacheEnabled(true);
                                    imageDownloadView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//                            imageDownloadView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            imageDownloadView.layout(0, 0, imageDownloadView.getMeasuredWidth(), imageDownloadView.getMeasuredHeight());
                            imageDownloadView.buildDrawingCache(true);
                            bitmap = Bitmap.createBitmap(imageDownloadView.getDrawingCache());
                            imageDownloadView.setDrawingCacheEnabled(false);


                            String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                            // Get Absolute Path in External Sdcard
                            String foler_name = "/Artech/";
                            String file_name = "artech_screen_capture.jpg";
                            String string_path = ex_storage + foler_name;

                            File file_path;
                            try {
                                file_path = new File(string_path);
                                if (!file_path.isDirectory()) {
                                    file_path.mkdirs();
                                }
                                FileOutputStream out = new FileOutputStream(string_path + file_name);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + string_path + file_name)));
                                out.close();

                            } catch (FileNotFoundException exception) {
                                Log.e("FileNotFoundException", exception.getMessage());
                            } catch (IOException exception) {
                                Log.e("IOException", exception.getMessage());
                            }



                            dialog.dismiss();
                        }
                    }).show();
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mArcheryImg.setVisibility(View.GONE);
                        }
                    });
                }
            });

            mDelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goal = -1;
                    direction = -1;

                    goalZeroCheckbox.setChecked(false);
                    goalOneCheckbox.setChecked(false);
                    goalTwoCheckbox.setChecked(false);
                    goalThreeCheckbox.setChecked(false);
                    goalFourCheckbox.setChecked(false);
                    goalFiveCheckbox.setChecked(false);
                    goalSixCheckbox.setChecked(false);
                    goalSevenCheckbox.setChecked(false);
                    goalEightCheckbox.setChecked(false);
                    goalNineCheckbox.setChecked(false);
                    goalTenCheckbox.setChecked(false);
                    goalFullScaleCheckbox.setChecked(false);

                    upLeftCheckbox.setChecked(false);
                    upCheckbox.setChecked(false);
                    upRightCheckbox.setChecked(false);
                    leftCheckbox.setChecked(false);
                    rightCheckbox.setChecked(false);
                    downLeftCheckbox.setChecked(false);
                    downCheckbox.setChecked(false);
                    downRightCheckbox.setChecked(false);
                }
            });



            mTabRound1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabRound1Button.setBackgroundResource(R.mipmap.tab_active);
                    mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                    mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                    mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                    mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);

                    mViewPager.setCurrentItem(0);
                }
            });

            mTabRound2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(round  > 0) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_active);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);

                        mViewPager.setCurrentItem(1);
                    }
                }
            });

            mTabRound3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(round  > 1) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_active);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);

                        mViewPager.setCurrentItem(2);
                    }
                }
            });

            mTabRound4Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(round  > 2) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_active);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);

                        mViewPager.setCurrentItem(3);
                    }
                }
            });

            mTabRound5Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(round  > 3) {
                        mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                        mTabRound5Button.setBackgroundResource(R.mipmap.tab_active);

                        mViewPager.setCurrentItem(4);
                    }
                }
            });

            mEnterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(score >= 0 && goal >= 0) {
                        goalButtonPush();
                    }
                }
            });

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date tdays = new Date();
                    int tday = tdays.getDay();
                    int ty = tdays.getYear() + 1900;
                    int tmonth = tdays.getMonth() + 1;
                    int tdate = tdays.getDate();

                    int thour = tdays.getHours();
                    int tminute = tdays.getMinutes();
                    int tsecond = tdays.getSeconds();

                    int total = 0;
                    for (int i = 0; i < round_score.size(); i++) {
                        if (round_score.get(i) == 11) {
                            total += 10;
                        } else {
                            total += round_score.get(i);
                        }
                    }

                    ArrayList<Object> arrayList = new ArrayList<>();
                    arrayList.add(ty + "-" +
                            (tmonth < 10 ? "0" + tmonth : tmonth));
                    arrayList.add(total);
                    arrayList.add(round+1);
                    arrayList.add(round_score.size());
                    arrayList.add(zeroCount);
                    arrayList.add(oneCount);
                    arrayList.add(twoCount);
                    arrayList.add(threeCount);
                    arrayList.add(fourCount);
                    arrayList.add(fiveCount);
                    arrayList.add(sixCount);
                    arrayList.add(sevenCount);
                    arrayList.add(eighCount);
                    arrayList.add(nineCount);
                    arrayList.add(tenCount);
                    arrayList.add(xTenCount);
                    arrayList.add(mWeather);
                    arrayList.add(mWind);
                    arrayList.add(mDistance);
                    arrayList.add(Integer.parseInt(mBraceHighText.getText().toString()));
                    arrayList.add(Integer.parseInt(mTillerHighText.getText().toString()));
                    arrayList.add(Integer.parseInt(mNockingPointText.getText().toString()));
                    arrayList.add("");
                    arrayList.add(ty + "-" +
                            (tmonth < 10 ? "0" + tmonth : tmonth) +  "-" +
                            (tdate < 10 ? "0" + tdate : tdate) +  " " +
                            (thour < 10 ? "0" + thour : thour) + ":" +
                            (tminute < 10 ? "0" + tminute : tminute) + ":" +
                            (tsecond < 10 ? "0" + tsecond : tsecond));
                    arrayList.add(tday);


                    String id = dbManager.insertScore(arrayList);
                    Log.d("ScoreActivity", "id : " + id +" /col : " + arrayList.get(0) + " / date : " +  arrayList.get(23)+ " / day : " +  arrayList.get(24)+ " / total : " +  arrayList.get(1) + " /round  : " +  arrayList.get(2) + " / arrow : " +  arrayList.get(3));

                    Intent intent = new Intent(mContext, RecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("col" , id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            });

            mAutoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        frame1flag = true;
                        mCameraProgressBar.setVisibility(View.VISIBLE);

                        if(mTouchView.getP1X() > 0 && mTouchView.getP1Y() > 0 && mTouchView.getP2X() > 0 && mTouchView.getP2Y() > 0 && mTouchView.getP3X() > 0 && mTouchView.getP3Y() > 0 && mTouchView.getP4X() > 0 && mTouchView.getP4Y() > 0 && mTouchView.getP5X() > 0 && mTouchView.getP5Y() > 0) {
                            if (firstCallFlag) {
                                firstCall = 0;
                                foundElipses[0] = 0.0f;
                                firstCallFlag = false;
                            }

                            mPosition = true;
//                            colorCoors[0] = (int)(mTouchView.getP1X() * 1.0273);
//                            colorCoors[1] = (int)(mTouchView.getP1Y() * 0.7839);
//                            colorCoors[2] = (int)(mTouchView.getP2X() * 1.0273);
//                            colorCoors[3] = (int)(mTouchView.getP2Y() * 0.7839);
//                            colorCoors[4] = (int)(mTouchView.getP3X() * 1.0273);
//                            colorCoors[5] = (int)(mTouchView.getP3Y() * 0.7839);
//                            colorCoors[6] = (int)(mTouchView.getP4X() * 1.0273);
//                            colorCoors[7] = (int)(mTouchView.getP4Y() * 0.7839);
//                            colorCoors[8] = (int)(mTouchView.getP5X() * 1.0273);
//                            colorCoors[9] = (int)(mTouchView.getP5Y() * 0.7839);
                            colorCoors[0] = (int)(mTouchView.getP1X() * 1.0452);
                            colorCoors[1] = (int)(mTouchView.getP1Y() * 0.7978);
                            colorCoors[2] = (int)(mTouchView.getP2X() * 1.0452);
                            colorCoors[3] = (int)(mTouchView.getP2Y() * 0.7978);
                            colorCoors[4] = (int)(mTouchView.getP3X() * 1.0452);
                            colorCoors[5] = (int)(mTouchView.getP3Y() * 0.7978);
                            colorCoors[6] = (int)(mTouchView.getP4X() * 1.0452);
                            colorCoors[7] = (int)(mTouchView.getP4Y() * 0.7978);
                            colorCoors[8] = (int)(mTouchView.getP5X() * 1.0452);
                            colorCoors[9] = (int)(mTouchView.getP5Y() * 0.7978);
                            Log.d("ArcheryBoardActivity", "real time : " + mTouchView.getP1X() + " / foundElipses[0] : " + foundElipses[0]);

                            mTask = mTimerTaskMaker();

                            mTimer = new Timer();
                            mTimer.schedule(mTask, 2, 100);
                        }else if(colorCoors[0] > 0 && colorCoors[1] > 0 && colorCoors[2] > 0 && colorCoors[3] > 0 && colorCoors[4] > 0 && colorCoors[5] > 0 && colorCoors[6] > 0 && colorCoors[7] > 0 && colorCoors[8] > 0 && colorCoors[9] > 0) {
                            Log.d("ArcheryBoardActivity", "save time : " + colorCoors[0]);
                            if(firstCallFlag) {
                                firstCall = 20;
                                firstCallFlag = false;
                            }

                            mPosition = true;
                            mTask = mTimerTaskMaker();

                            mTimer = new Timer();
//                        mTimer.schedule(mTask, 2, 60);
                            mTimer.schedule(mTask, 2, 100);
                        }else {
                            mAutoResultView.setVisibility(View.GONE);
                            mAutoResult2View.setVisibility(View.GONE);
                            mAutoResult3View.setVisibility(View.GONE);
                            mCameraProgressBar.setVisibility(View.GONE);
                            mTouchView.clearFlag();

                            Toast.makeText(mContext, getString(R.string.score_auto_error_message),  Toast.LENGTH_LONG).show();
                            mPosition = false;
                            mState = true;
                        }
                    }else {
                        mCount = true;
                        functionCall = 0;

                        if(mTask != null) {
                            mTask.cancel();
                            mTask = null;
                        }

                        if(mTask != null) {
                            mTimer.cancel();
                            mTimer = null;
                        }

                        mTouchView.clearFlag();
                        mAutoResultView.setVisibility(View.GONE);
                        mAutoResult2View.setVisibility(View.GONE);
                        mAutoResult3View.setVisibility(View.GONE);
                        mCameraProgressBar.setVisibility(View.GONE);
                    }
                }
            });

            initSetting();
    }

    private void initSetting() {
        Cursor cursor = dbManager.selectTuningInfo();
        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                if(cursor.getInt(0) > 0) {
                    mInfoBraceHighLayout.setBackgroundResource(R.mipmap.img_info_press);
                }
                if(cursor.getInt(1) > 0) {
                    mInfoTillerHighLayout.setBackgroundResource(R.mipmap.img_info_press);
                }
                if(cursor.getInt(2) > 0) {
                    mInfoNockingPointLayout.setBackgroundResource(R.mipmap.img_info_press);
                }

                mBraceHighText.setText("" + cursor.getInt(0));
                mTillerHighText.setText("" + cursor.getInt(1));
                mNockingPointText.setText("" + cursor.getInt(2));

                mWeather = cursor.getInt(3);
                mWind = cursor.getInt(4);
                mDistance = cursor.getInt(5);

                if (mWeather == 0) {
//                    mInfoWeatherImg.setImageResource(R.mipmap.ic_sunny_press);
                    mInfoWeatherText.setText(getString(R.string.score_info_weather)+"(SUNNY)");
                } else if (mWeather == 1) {
//                    mInfoWeatherImg.setImageResource(R.mipmap.ic_cloudy_press);
                    mInfoWeatherText.setText(getString(R.string.score_info_weather)+"(CLOUDY)");
                } else if (mWeather == 2) {
//                    mInfoWeatherImg.setImageResource(R.mipmap.ic_fog_press);
                    mInfoWeatherText.setText(getString(R.string.score_info_weather)+"(FOG)");
                } else if (mWeather == 3) {
//                    mInfoWeatherImg.setImageResource(R.mipmap.ic_typhoon_press);
                    mInfoWeatherText.setText(getString(R.string.score_info_weather)+"(TYPHOON)");
                } else if (mWeather == 4) {
//                    mInfoWeatherImg.setImageResource(R.mipmap.ic_rain_press);
                    mInfoWeatherText.setText(getString(R.string.score_info_weather)+"(RAIN)");
                } else if (mWeather == 5) {
//                    mInfoWeatherImg.setImageResource(R.mipmap.ic_snow_press);
                    mInfoWeatherText.setText(getString(R.string.score_info_weather)+"(SNOW)");
                }


                if (mWind == 0) {
                    mInfoWindText.setText(getString(R.string.score_info_wind)+"(ZERO)");
//                    mInfoWindImg.setImageResource(R.mipmap.ic_wind_zero_press);
                } else if (mWind == 1) {
                    mInfoWindText.setText(getString(R.string.score_info_wind)+"(ONE)");
//                    mInfoWindImg.setImageResource(R.mipmap.ic_wind_one_press);
                } else if (mWind == 2) {
                    mInfoWindText.setText(getString(R.string.score_info_wind)+"(TWO)");
//                    mInfoWindImg.setImageResource(R.mipmap.ic_wind_two_press);
                } else if (mWind == 3) {
                    mInfoWindText.setText(getString(R.string.score_info_wind)+"(THREE)");
//                    mInfoWindImg.setImageResource(R.mipmap.ic_wind_three_press);
                } else if (mWind == 4) {
                    mInfoWindText.setText(getString(R.string.score_info_wind)+"(FOUR)");
//                    mInfoWindImg.setImageResource(R.mipmap.ic_wind_four_press);
                }


                if (mDistance == 0) {
                    mInfoDistanceText.setText(getString(R.string.score_info_distance)+"(18M)");
//                    mInfoDistanceImg.setImageResource(R.mipmap.img_18m_press);
                } else if (mDistance == 1) {
                    mInfoDistanceText.setText(getString(R.string.score_info_distance)+"(30M)");
//                    mInfoDistanceImg.setImageResource(R.mipmap.img_30m_press);
                } else if (mDistance == 2) {
                    mInfoDistanceText.setText(getString(R.string.score_info_distance)+"(50M)");
//                    mInfoDistanceImg.setImageResource(R.mipmap.img_50m_press);
                } else if (mDistance == 3) {
                    mInfoDistanceText.setText(getString(R.string.score_info_distance)+"(70M)");
//                    mInfoDistanceImg.setImageResource(R.mipmap.img_70m_press);
                }
            }
        }

        Date tdays = new Date();
        int tday = tdays.getDay();
        int ty = tdays.getYear() + 1900;
        int tmonth = tdays.getMonth() + 1;
        int tdate = tdays.getDate();

        String today = "";
        if(tday == 1) {
            today = "("+getString(R.string.monday)+")";
        }else if(tday == 2) {
            today = "("+getString(R.string.tuesday)+")";
        }else if(tday == 3) {
            today = "("+getString(R.string.wednesday)+")";
        }else if(tday == 4) {
            today = "("+getString(R.string.thursday)+")";
        }else if(tday == 5) {
            today = "("+getString(R.string.friday)+")";
        }else if(tday == 6) {
            today = "("+getString(R.string.saturday)+")";
        }else if(tday == 0) {
            today = "("+getString(R.string.sunday)+")";
        }

        mDateText.setText(ty + "-" +
                (tmonth < 10 ? "0" + tmonth : tmonth) +  "-" +
                (tdate < 10 ? "0" + tdate : tdate) +
                today);

    }

    @Override
    protected void onDestroy() {
        if(mTask != null) {
            mTask.cancel();
            mTask = null;
        }

        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d("ScoreActivity", "onPause");
        if(mTask != null) {
            mTask.cancel();
            mTask = null;
        }

        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mAutoCheckbox.setChecked(false);

        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("ScoreActivity", "onResume");
//        ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
//        fragment.updateInstance();
        super.onResume();
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
        }else if (id == R.id.nav_scope) {
            Intent intent = new Intent(mContext, ScopeActivity.class);
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



    private CompoundButton.OnCheckedChangeListener goalNumberCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            goal = -1;

            goalZeroCheckbox.setChecked(false);
            goalOneCheckbox.setChecked(false);
            goalTwoCheckbox.setChecked(false);
            goalThreeCheckbox.setChecked(false);
            goalFourCheckbox.setChecked(false);
            goalFiveCheckbox.setChecked(false);
            goalSixCheckbox.setChecked(false);
            goalSevenCheckbox.setChecked(false);
            goalEightCheckbox.setChecked(false);
            goalNineCheckbox.setChecked(false);
            goalTenCheckbox.setChecked(false);
            goalFullScaleCheckbox.setChecked(false);

            if(isChecked){
                buttonView.setChecked(true);

                switch (buttonView.getId()) {
                    case R.id.number_0_check:
                        goal = 0;
                        break;
                    case R.id.number_1_check:
                        goal = 1;
                        break;
                    case R.id.number_2_check:
                        goal = 2;
                        break;
                    case R.id.number_3_check:
                        goal = 3;
                        break;
                    case R.id.number_4_check:
                        goal = 4;
                        break;
                    case R.id.number_5_check:
                        goal = 5;
                        break;
                    case R.id.number_6_check:
                        goal = 6;
                        break;
                    case R.id.number_7_check:
                        goal = 7;
                        break;
                    case R.id.number_8_check:
                        goal = 8;
                        break;
                    case R.id.number_9_check:
                        goal = 9;
                        break;
                    case R.id.number_10_check:
                        goal = 10;
                        break;
                    case R.id.number_xten_check:
                        goal = 11;
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener directionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            direction = -1;

            upLeftCheckbox.setChecked(false);
            upCheckbox.setChecked(false);
            upRightCheckbox.setChecked(false);
            leftCheckbox.setChecked(false);
            rightCheckbox.setChecked(false);
            downLeftCheckbox.setChecked(false);
            downCheckbox.setChecked(false);
            downRightCheckbox.setChecked(false);

            if(isChecked){
                buttonView.setChecked(true);

                switch (buttonView.getId()) {
                    case R.id.arrow_top_left_check:
                        direction = 1;
                        break;
                    case R.id.arrow_top_check:
                        direction = 2;
                        break;
                    case R.id.arrow_top_right_check:
                        direction = 3;
                        break;
                    case R.id.arrow_left_check:
                        direction = 4;
                        break;
                    case R.id.arrow_right_check:
                        direction = 5;
                        break;
                    case R.id.arrow_down_left_check:
                        direction = 6;
                        break;
                    case R.id.arrow_down_check:
                        direction = 7;
                        break;
                    case R.id.arrow_down_right_check:
                        direction = 8;
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void goalButtonPush() {
        if(score == 12) {
            score = 0;
            ++round;

            if(round == 5) {
                score = -1;
                return;
            }

            goalView.findViewById(R.id.score1_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score2_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score3_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score4_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score5_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score6_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score7_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score8_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score9_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score10_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score11_layout).setOnClickListener(null);
            goalView.findViewById(R.id.score12_layout).setOnClickListener(null);
//            goalView.findViewById(R.id.score13_layout).setOnClickListener(null);
//            goalView.findViewById(R.id.score14_layout).setOnClickListener(null);
//            goalView.findViewById(R.id.score15_layout).setOnClickListener(null);
//            goalView.findViewById(R.id.score16_layout).setOnClickListener(null);
//            goalView.findViewById(R.id.score17_layout).setOnClickListener(null);
//            goalView.findViewById(R.id.score18_layout).setOnClickListener(null);

            LayoutInflater inflater = getLayoutInflater();
            goalView = inflater.inflate(R.layout.content_score_board, null);
            mAdapter.addView(goalView);
            mAdapter.notifyDataSetChanged();

            mViewPager.setCurrentItem(mAdapter.getCount()-1);

            if(round == 1) {
                mTabRound2LcokImg.setVisibility(View.INVISIBLE);
                mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound2Button.setBackgroundResource(R.mipmap.tab_active);
                mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);
            }else if(round == 2) {
                mTabRound3LcokImg.setVisibility(View.INVISIBLE);
                mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound3Button.setBackgroundResource(R.mipmap.tab_active);
                mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);
            }else if(round == 3) {
                mTabRound4LcokImg.setVisibility(View.INVISIBLE);
                mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound4Button.setBackgroundResource(R.mipmap.tab_active);
                mTabRound5Button.setBackgroundResource(R.mipmap.tab_non);
            }else if(round == 4) {
                mTabRound5LcokImg.setVisibility(View.INVISIBLE);
                mTabRound1Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound2Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound3Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound4Button.setBackgroundResource(R.mipmap.tab_non);
                mTabRound5Button.setBackgroundResource(R.mipmap.tab_active);
            }
        }

        if (score == 0) {
            goalDialogShow1(goalView, 0);
        } else if (score == 1) {
            goalDialogShow2(goalView, 0);
        } else if (score == 2) {
            goalDialogShow3(goalView, 0);
        } else if (score == 3) {
            goalDialogShow4(goalView, 0);
        } else if (score == 4) {
            goalDialogShow5(goalView, 0);
        } else if (score == 5) {
            goalDialogShow6(goalView, 0);
            ScrollView scrollView = (ScrollView)goalView.findViewById(R.id.scroll_view);
            scrollView.fullScroll(View.FOCUS_DOWN);
        } else if (score == 6) {
            goalDialogShow7(goalView, 0);
        } else if (score == 7) {
            goalDialogShow8(goalView, 0);
        } else if (score == 8) {
            goalDialogShow9(goalView, 0);
        } else if (score == 9) {
            goalDialogShow10(goalView, 0);
        } else if (score == 10) {
            goalDialogShow11(goalView, 0);
        } else if (score == 11) {
            goalDialogShow12(goalView, 0);
        }/* else if (score == 12) {
            goalDialogShow13(goalView, 0);
        } else if (score == 13) {
            goalDialogShow14(goalView, 0);
        } else if (score == 14) {
            goalDialogShow15(goalView, 0);
        } else if (score == 15) {
            goalDialogShow16(goalView, 0);
        } else if (score == 16) {
            goalDialogShow17(goalView, 0);
        } else if (score == 17) {
            goalDialogShow18(goalView, 0);
        }*/

        if(goal == 0) {
            zeroCount++;
        }else if(goal == 1) {
            oneCount++;
        }else if(goal == 2) {
            twoCount++;
        }else if(goal == 3) {
            threeCount++;
        }else if(goal == 4) {
            fourCount++;
        }else if(goal == 5) {
            fiveCount++;
        }else if(goal == 6) {
            sixCount++;
        }else if(goal == 7) {
            sevenCount++;
        }else if(goal == 8) {
            eighCount++;
        }else if(goal == 9) {
            nineCount++;
        }else if(goal == 10) {
            tenCount++;
        }else if(goal == 11) {
            xTenCount++;
        }



        int total = 0;
        for (int i = 0; i < round_score.size(); i++) {
            if (round_score.get(i) == 11) {
                total += 10;
            } else {
                total += round_score.get(i);
            }
        }

        mArrowText.setText(getString(R.string.score_arrow_text) + " " + round_score.size());
        mTotalText.setText(getString(R.string.score_total_text) + " " + total);

//        goal = -1;
//        direction = -1;
        score++;
    }

    public void goalDialogShow1(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score1_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score1_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score1_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score1_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score1_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score1_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score1_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score1_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score1_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow2(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score2_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score2_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score2_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score2_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score2_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score2_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score2_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score2_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score2_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score2_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow3(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score3_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score3_layout);
        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score3_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score3_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score3_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score3_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score3_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score3_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score3_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score3_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow4(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score4_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score4_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score4_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score4_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score4_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score4_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score4_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score4_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score4_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score4_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow5(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score5_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score5_layout);
        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score5_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score5_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score5_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score5_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score5_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score5_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score5_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score5_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow6(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score6_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score6_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score6_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score6_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score6_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score6_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score6_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score6_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score6_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score6_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow7(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score7_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score7_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score7_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score7_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score7_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score7_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score7_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score7_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score7_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score7_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow8(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score8_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score8_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score8_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score8_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score8_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score8_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score8_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score8_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score8_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score8_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow9(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score9_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score9_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score9_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score9_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score9_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score9_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score9_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score9_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score9_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score9_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow10(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score10_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score10_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score10_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score10_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score10_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score10_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score10_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score10_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score10_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score10_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow11(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score11_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score11_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score11_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score11_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score11_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score11_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score11_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score11_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score11_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score11_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow12(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score12_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score12_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score12_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score12_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score12_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score12_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score12_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score12_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score12_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score12_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    /*public void goalDialogShow13(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score13_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score13_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score13_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score13_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score13_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score13_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score13_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score13_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score13_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score13_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow14(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score14_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score14_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score14_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score14_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score14_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score14_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score14_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score14_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score14_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score14_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow15(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score15_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score15_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score15_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score15_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score15_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score15_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score15_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score15_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score15_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score15_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow16(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score16_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score16_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score16_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score16_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score16_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score16_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score16_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score16_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score16_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score16_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow17(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score17_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score17_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score17_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score17_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score17_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score17_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score17_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score17_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score17_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score17_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }

    public void goalDialogShow18(View view, int layoutValue) {
        //20170919 score++;

        TextView goalText = (TextView) view.findViewById(R.id.score18_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score18_layout);

        if(layoutValue > 0) {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.set(layoutValue-1, 0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.set(layoutValue-1,11);
            } else {
                goalText.setText(goal + "");
                round_score.set(layoutValue-1,goal);
            }
        }else {
            if (goal <= 0) {
                goalText.setText("M");
                round_score.add(0);
            } else if (goal == 11) {
                goalText.setText("X");
                round_score.add(11);
            } else {
                goalText.setText(goal + "");
                round_score.add(goal);
            }
        }
        round_direction.add(direction);

        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score18_circle1);
        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score18_circle2);
        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score18_circle3);
        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score18_circle4);
        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score18_circle5);
        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score18_circle6);
        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score18_circle7);
        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score18_circle8);

        directionImg1.setVisibility(View.INVISIBLE);
        directionImg2.setVisibility(View.INVISIBLE);
        directionImg3.setVisibility(View.INVISIBLE);
        directionImg4.setVisibility(View.INVISIBLE);
        directionImg5.setVisibility(View.INVISIBLE);
        directionImg6.setVisibility(View.INVISIBLE);
        directionImg7.setVisibility(View.INVISIBLE);
        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
        } else if (goal <= 4) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_black));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_blue));
        } else if (goal <= 8) {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_red));
        } else {
//            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_yellow));
        }

        if (direction == 1) {
            directionImg1.setVisibility(View.VISIBLE);
        } else if (direction == 2) {
            directionImg2.setVisibility(View.VISIBLE);
        } else if (direction == 3) {
            directionImg3.setVisibility(View.VISIBLE);
        } else if (direction == 4) {
            directionImg4.setVisibility(View.VISIBLE);
        } else if (direction == 5) {
            directionImg5.setVisibility(View.VISIBLE);
        } else if (direction == 6) {
            directionImg6.setVisibility(View.VISIBLE);
        } else if (direction == 7) {
            directionImg7.setVisibility(View.VISIBLE);
        } else if (direction == 8) {
            directionImg8.setVisibility(View.VISIBLE);
        }
    }*/

    public void goalDialogShow(final View view) {
        mDelButton.callOnClick();

        TextView valueText = null;
        int layoutValue = 0;
        if (view.getId() == R.id.score1_layout) {
            layoutValue = 1;
            valueText = (TextView) view.findViewById(R.id.score_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score2_layout) {
            layoutValue = 2;
            valueText = (TextView) view.findViewById(R.id.score2_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score3_layout) {
            layoutValue = 3;
            valueText = (TextView) view.findViewById(R.id.score3_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score4_layout) {
            layoutValue = 4;
            valueText = (TextView) view.findViewById(R.id.score4_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score5_layout) {
            layoutValue = 5;
            valueText = (TextView) view.findViewById(R.id.score5_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score6_layout) {
            layoutValue = 6;
            valueText = (TextView) view.findViewById(R.id.score6_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score7_layout) {
            layoutValue = 7;
            valueText = (TextView) view.findViewById(R.id.score7_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score8_layout) {
            layoutValue = 8;
            valueText = (TextView) view.findViewById(R.id.score8_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score9_layout) {
            layoutValue = 9;
            valueText = (TextView) view.findViewById(R.id.score9_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score10_layout) {
            layoutValue = 10;
            valueText = (TextView) view.findViewById(R.id.score10_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score11_layout) {
            layoutValue = 11;
            valueText = (TextView) view.findViewById(R.id.score11_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score12_layout) {
            layoutValue = 12;
            valueText = (TextView) view.findViewById(R.id.score12_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        }/* else if (view.getId() == R.id.score13_layout) {
            layoutValue = 13;
            valueText = (TextView) view.findViewById(R.id.score13_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score14_layout) {
            layoutValue = 14;
            valueText = (TextView) view.findViewById(R.id.score14_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score15_layout) {
            layoutValue = 15;
            valueText = (TextView) view.findViewById(R.id.score15_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score16_layout) {
            layoutValue = 16;
            valueText = (TextView) view.findViewById(R.id.score16_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score17_layout) {
            layoutValue = 17;
            valueText = (TextView) view.findViewById(R.id.score17_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score18_layout) {
            layoutValue = 18;
            valueText = (TextView) view.findViewById(R.id.score18_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        }*/


        if(!mPopupBoolean) {
            mPopupBoolean = !mPopupBoolean;
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.content_score_popup, null);


            final CheckBox pULeftCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_top_left_check);
            final CheckBox pUpCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_top_check);
            final CheckBox pUpRightCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_top_right_check);
            final CheckBox pLeftCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_left_check);
            final CheckBox pRightCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_right_check);
            final CheckBox pDownLeftCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_down_left_check);
            final CheckBox pDownRightCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_down_right_check);
            final CheckBox pDownCheckbox = (CheckBox) dialogView.findViewById(R.id.arrow_down_check);

            final CheckBox pGoalZeroCheckbox = (CheckBox) dialogView.findViewById(R.id.number_0_check);
            final CheckBox pGoalOneCheckbox = (CheckBox) dialogView.findViewById(R.id.number_1_check);
            final CheckBox pGoalTwoCheckbox = (CheckBox) dialogView.findViewById(R.id.number_2_check);
            final CheckBox pGoalThreeCheckbox = (CheckBox) dialogView.findViewById(R.id.number_3_check);
            final CheckBox pGoalFourCheckbox = (CheckBox) dialogView.findViewById(R.id.number_4_check);
            final CheckBox pGoalFiveCheckbox = (CheckBox) dialogView.findViewById(R.id.number_5_check);
            final CheckBox pGoalSixCheckbox = (CheckBox) dialogView.findViewById(R.id.number_6_check);
            final CheckBox pGoalSevenCheckbox = (CheckBox) dialogView.findViewById(R.id.number_7_check);
            final CheckBox pGoalEightCheckbox = (CheckBox) dialogView.findViewById(R.id.number_8_check);
            final CheckBox pGoalNineCheckbox = (CheckBox) dialogView.findViewById(R.id.number_9_check);
            final CheckBox pGoalTenCheckbox = (CheckBox) dialogView.findViewById(R.id.number_10_check);
            final CheckBox pGoalFullScaleCheckbox = (CheckBox) dialogView.findViewById(R.id.number_xten_check);

            pULeftCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     pULeftCheckbox.setChecked(false);
                     pUpCheckbox.setChecked(false);
                     pUpRightCheckbox.setChecked(false);
                     pLeftCheckbox.setChecked(false);
                     pRightCheckbox.setChecked(false);
                     pDownLeftCheckbox.setChecked(false);
                     pDownRightCheckbox.setChecked(false);
                     pDownCheckbox.setChecked(false);

                     if(isChecked) {
                         buttonView.setChecked(true);
                     }

                 }
             });
            pUpCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pULeftCheckbox.setChecked(false);
                    pUpCheckbox.setChecked(false);
                    pUpRightCheckbox.setChecked(false);
                    pLeftCheckbox.setChecked(false);
                    pRightCheckbox.setChecked(false);
                    pDownLeftCheckbox.setChecked(false);
                    pDownRightCheckbox.setChecked(false);
                    pDownCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }

                }
            });
            pUpRightCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pULeftCheckbox.setChecked(false);
                    pUpCheckbox.setChecked(false);
                    pUpRightCheckbox.setChecked(false);
                    pLeftCheckbox.setChecked(false);
                    pRightCheckbox.setChecked(false);
                    pDownLeftCheckbox.setChecked(false);
                    pDownRightCheckbox.setChecked(false);
                    pDownCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }

                }
            });
            pLeftCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pULeftCheckbox.setChecked(false);
                    pUpCheckbox.setChecked(false);
                    pUpRightCheckbox.setChecked(false);
                    pLeftCheckbox.setChecked(false);
                    pRightCheckbox.setChecked(false);
                    pDownLeftCheckbox.setChecked(false);
                    pDownRightCheckbox.setChecked(false);
                    pDownCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }

                }
            });
            pRightCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pULeftCheckbox.setChecked(false);
                    pUpCheckbox.setChecked(false);
                    pUpRightCheckbox.setChecked(false);
                    pLeftCheckbox.setChecked(false);
                    pRightCheckbox.setChecked(false);
                    pDownLeftCheckbox.setChecked(false);
                    pDownRightCheckbox.setChecked(false);
                    pDownCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }

                }
            });
            pDownLeftCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pULeftCheckbox.setChecked(false);
                    pUpCheckbox.setChecked(false);
                    pUpRightCheckbox.setChecked(false);
                    pLeftCheckbox.setChecked(false);
                    pRightCheckbox.setChecked(false);
                    pDownLeftCheckbox.setChecked(false);
                    pDownRightCheckbox.setChecked(false);
                    pDownCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }

                }
            });
            pDownRightCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pULeftCheckbox.setChecked(false);
                    pUpCheckbox.setChecked(false);
                    pUpRightCheckbox.setChecked(false);
                    pLeftCheckbox.setChecked(false);
                    pRightCheckbox.setChecked(false);
                    pDownLeftCheckbox.setChecked(false);
                    pDownRightCheckbox.setChecked(false);
                    pDownCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }

                }
            });
            pDownCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pULeftCheckbox.setChecked(false);
                    pUpCheckbox.setChecked(false);
                    pUpRightCheckbox.setChecked(false);
                    pLeftCheckbox.setChecked(false);
                    pRightCheckbox.setChecked(false);
                    pDownLeftCheckbox.setChecked(false);
                    pDownRightCheckbox.setChecked(false);
                    pDownCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }

                }
            });

            pGoalZeroCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalOneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalTwoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalThreeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalFourCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalFiveCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalSixCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalSevenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalEightCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalNineCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalTenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });
            pGoalFullScaleCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pGoalZeroCheckbox.setChecked(false);
                    pGoalOneCheckbox.setChecked(false);
                    pGoalTwoCheckbox.setChecked(false);
                    pGoalThreeCheckbox.setChecked(false);
                    pGoalFourCheckbox.setChecked(false);
                    pGoalFiveCheckbox.setChecked(false);
                    pGoalSixCheckbox.setChecked(false);
                    pGoalSevenCheckbox.setChecked(false);
                    pGoalEightCheckbox.setChecked(false);
                    pGoalNineCheckbox.setChecked(false);
                    pGoalTenCheckbox.setChecked(false);
                    pGoalFullScaleCheckbox.setChecked(false);

                    if(isChecked) {
                        buttonView.setChecked(true);
                    }
                }
            });


            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("Goal Setting");
            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.show();
            alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.getWindow().setLayout(1245, 623);
            alertDialog.getWindow().setLayout(670, 360);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    direction = -1;
                    goal = -1;
                }
            });

            Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_button);
            Button confirmButton = (Button) dialogView.findViewById(R.id.save_button);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupBoolean = false;
                    alertDialog.dismiss();
                }
            });

            final int finalLayoutValue = layoutValue;
            final TextView finalValueText = valueText;
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupBoolean = false;
                    direction = -1;
                    goal = -1;
                    //20170919 score--;

                    int minusGoal = Integer.parseInt(finalValueText.getText().toString());

                    if(minusGoal == 0) {
                        zeroCount--;
                    }else if(minusGoal == 1) {
                        oneCount--;
                    }else if(minusGoal == 2) {
                        twoCount--;
                    }else if(minusGoal == 3) {
                        threeCount--;
                    }else if(minusGoal == 4) {
                        fourCount--;
                    }else if(minusGoal == 5) {
                        fiveCount--;
                    }else if(minusGoal == 6) {
                        sixCount--;
                    }else if(minusGoal == 7) {
                        sevenCount--;
                    }else if(minusGoal == 8) {
                        eighCount--;
                    }else if(minusGoal == 9) {
                        nineCount--;
                    }else if(minusGoal == 10) {
                        tenCount--;
                    }else if(minusGoal == 11) {
                        xTenCount--;
                    }



                    if (pULeftCheckbox.isChecked()) {
                        direction = 1;
                    } else if (pUpCheckbox.isChecked()) {
                        direction = 2;
                    } else if (pUpRightCheckbox.isChecked()) {
                        direction = 3;
                    } else if (pLeftCheckbox.isChecked()) {
                        direction = 4;
                    } else if (pRightCheckbox.isChecked()) {
                        direction = 5;
                    } else if (pDownLeftCheckbox.isChecked()) {
                        direction = 6;
                    } else if (pDownCheckbox.isChecked()) {
                        direction = 7;
                    } else if (pDownRightCheckbox.isChecked()) {
                        direction = 8;
                    }


                    if (pGoalZeroCheckbox.isChecked()) {
                        goal = 0;
                    } else if (pGoalOneCheckbox.isChecked()) {
                        goal = 1;
                    } else if (pGoalTwoCheckbox.isChecked()) {
                        goal = 2;
                    } else if (pGoalThreeCheckbox.isChecked()) {
                        goal = 3;
                    } else if (pGoalFourCheckbox.isChecked()) {
                        goal = 4;
                    } else if (pGoalFiveCheckbox.isChecked()) {
                        goal = 5;
                    } else if (pGoalSixCheckbox.isChecked()) {
                        goal = 6;
                    } else if (pGoalSevenCheckbox.isChecked()) {
                        goal = 7;
                    } else if (pGoalEightCheckbox.isChecked()) {
                        goal = 8;
                    } else if (pGoalNineCheckbox.isChecked()) {
                        goal = 9;
                    } else if (pGoalTenCheckbox.isChecked()) {
                        goal = 10;
                    } else if (pGoalFullScaleCheckbox.isChecked()) {
                        goal = 11;
                    }

                    if (view.getId() == R.id.score1_layout) {
                        goalDialogShow1(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score2_layout) {
                        goalDialogShow2(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score3_layout) {
                        goalDialogShow3(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score4_layout) {
                        goalDialogShow4(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score5_layout) {
                        goalDialogShow5(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score6_layout) {
                        goalDialogShow6(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score7_layout) {
                        goalDialogShow7(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score8_layout) {
                        goalDialogShow8(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score9_layout) {
                        goalDialogShow9(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score10_layout) {
                        goalDialogShow10(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score11_layout) {
                        goalDialogShow11(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score12_layout) {
                        goalDialogShow12(goalView, finalLayoutValue);
                    }/* else if (view.getId() == R.id.score13_layout) {
                        goalDialogShow13(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score14_layout) {
                        goalDialogShow14(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score15_layout) {
                        goalDialogShow15(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score16_layout) {
                        goalDialogShow16(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score17_layout) {
                        goalDialogShow17(goalView, finalLayoutValue);
                    } else if (view.getId() == R.id.score18_layout) {
                        goalDialogShow18(goalView, finalLayoutValue);
                    }*/

                    if(goal == 0) {
                        zeroCount++;
                    }else if(goal == 1) {
                        oneCount++;
                    }else if(goal == 2) {
                        twoCount++;
                    }else if(goal == 3) {
                        threeCount++;
                    }else if(goal == 4) {
                        fourCount++;
                    }else if(goal == 5) {
                        fiveCount++;
                    }else if(goal == 6) {
                        sixCount++;
                    }else if(goal == 7) {
                        sevenCount++;
                    }else if(goal == 8) {
                        eighCount++;
                    }else if(goal == 9) {
                        nineCount++;
                    }else if(goal == 10) {
                        tenCount++;
                    }else if(goal == 11) {
                        xTenCount++;
                    }

                    int total = 0;
                    for (int i = 0; i < round_score.size(); i++) {
                        if (round_score.get(i) == 11) {
                            total += 10;
                        } else {
                            total += round_score.get(i);
                        }
                    }

//                    totalValue = total;
//                    totalArrow = round_score.size();


                    mArrowText.setText(getString(R.string.score_arrow_text) + " " + round_score.size());
                    mTotalText.setText(getString(R.string.score_total_text) + " " + total);

                    alertDialog.dismiss();
                }
            });
        }
    }





    Mat convertBitMap2Mat(Bitmap rbgaImage){
        // convert Java Bitmap into Opencv Mat
        Mat rgbaMat = new Mat(rbgaImage.getHeight(), rbgaImage.getWidth(), CvType.CV_8UC4);
        // Converts the image to the format expected
        Bitmap bmp32 = rbgaImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, rgbaMat);

        // converts RGBA Mat into BGR Mat
        Mat rgbMat = new Mat(rbgaImage.getHeight(), rbgaImage.getWidth(), CvType.CV_8UC3); // 8 bits per component 3 layers
        cvtColor(rgbaMat, rgbMat, Imgproc.COLOR_RGBA2BGR, 3);

        return rgbMat;
    }

    Bitmap convertMat2Bitmap(Mat img){
        int width = img.width();
        int height = img.height();
        Log.d("ScoreActivity", "convertMat2Bitmap w : " + width + " / h : " + height);

        Bitmap bmp;
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Mat tmp;
        tmp = img.channels()==1? new Mat (width, height, CvType.CV_8UC1, new Scalar(1)): new Mat (width, height, CvType.CV_8UC3, new Scalar(3));

        Log.d("ScoreActivity", "channels : " + img.channels());
        try {
            if (img.channels() == 3)
                cvtColor(img, tmp, Imgproc.COLOR_RGB2BGRA);
            else if (img.channels() == 1)
                cvtColor(img, tmp, Imgproc.COLOR_GRAY2RGBA);
            Utils.matToBitmap(tmp, bmp, true);
        }
        catch(CvException e){
            Log.d("Exception", e.getMessage());
        }

        return bmp;

    }

    TimerTask mTimerTaskMaker() {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        Log.d("archery", "ss timer : " + callCounter + " functionCall : " + functionCall);
                        ArcheryCameraFragment fragment = null;
                        fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                        Bitmap cameraBitmap = (Bitmap) fragment.screenShot();

//                        mCameraView.buildDrawingCache();
//                        Bitmap cameraBitmap = mCameraView.getDrawingCache();
                        if(null != cameraBitmap) {
                            input = convertBitMap2Mat(cameraBitmap);
                        }

                        if (!input.empty()) {
                            if (oneTime == 0) {
                                debugImg_ = input.clone();
                                oneTime = 1;

                                Log.d("ArcheryBoardActivity", "1111 outputTarget : " + outputTarget + " / foundElipses [0]: " + foundElipses[0]+ "  +  [1]"  + foundElipses[1]+ "  + [2]" + foundElipses[2]);
                                // Do Segmentation Once
                                foundElipses = autoScoringNativeClass.autoScoringSegmentation(debugImg_ .getNativeObjAddr(), colorCoors, foundElipses);
                                Log.d("ArcheryBoardActivity", "2222 outputTarget : " + outputTarget + " / foundElipses [0]: " + foundElipses[0]+ "  +  [1]"  + foundElipses[1]+ "  + [2]" + foundElipses[2]
                                        + "  + [3]" + foundElipses[3]+ "  + [4]" + foundElipses[4]+ "  + [5]" + foundElipses[5]+ "  + [6]" + foundElipses[6]
                                        + "  + [7]" + foundElipses[7]+ "  + [8]" + foundElipses[8]+ "  + [9]" + foundElipses[9]+ "  + [10]" + foundElipses[10]
                                        + "  + [11]" + foundElipses[11]+ "  + [12]" + foundElipses[12]+ "  + [13]" + foundElipses[13]+ "  + [14]" + foundElipses[14]
                                        + "  + [15]" + foundElipses[15]+ "  + [16]" + foundElipses[16]+ "  + [17]" + foundElipses[17]+ "  + [18]" + foundElipses[18]
                                        + "  + [19]" + foundElipses[19]);
                            }else if((foundElipses[0] == 0) && (outputTargetState < 3)) {
                                Log.d("ArcheryBoardActivity", "outputTarget : " + outputTarget + " / outputTargetState : " + outputTargetState);
                                oneTime = 0;
                                outputTargetState++;

                                mTouchView.clearFlag();
                            }else if ((foundElipses[0] != 0) && (frame1flag == true)) { // Input Frame 1
                                frame1_ = input.clone();
                                frame1__ = input.clone();

                                Log.i("Gus", "frame1 saved!");

                                frame1flag = false;
                                frame2flag = true;

                                callCounter = 0;
                            }
                            else if(frame2flag == true){ // Input Frame 2
                                if (callCounter == 10) { // // Check for movement every second
                                    callCounterLast = 0;

                                    frame1Clone = frame1_.clone();
                                    frame2_ = input.clone();


                                    // Call Detect Motion Function
                                    Log.d("isMotionDetected", "isMotionDetected start : " + isMotionDetected);
                                    isMotionDetected = autoScoringNativeClass.detectMotion(frame1Clone.getNativeObjAddr(), frame2_.getNativeObjAddr());
                                    Log.d("isMotionDetected", "isMotionDetected end : " + isMotionDetected);
                                    // Continue Searching for motion every 1 sec if no motion detected
                                    if (isMotionDetected == 0) { // 0 is false
                                        callCounter = 0;
                                    }
                                    else {
                                        Log.i("Gus", "MOTION DETECTED!");
                                    }

                                    if(mPosition)
                                        mPosition = false;
                                }
                                else if (callCounter >= 30) { // after 2 seconds from Motion Detection ScoreArrow
                                    frame2_ = input.clone();

                                    Log.i("Gus", "frame2 saved!");

                                    scoreOut = autoScoringNativeClass.autoScoringEvaluation(frame1__.getNativeObjAddr(), frame2_.getNativeObjAddr(), debugImg_ .getNativeObjAddr());

                                    if(scoreOut != -1){
                                        newScoreFlag = true;
                                    }


                                    // display output
                                    if (scoreOut != -1) { // new score detected
                                        Log.i("Gus", "SCORE: " + scoreOut);
                                    }

                                    // avoids to score again until frame 1 is changed
                                    frame2flag = false;
                                }

                                callCounter++;
                            }

                            if(functionCall > 0){
                                functionCall++;
                            }


                            if((callCounter == 31) && (functionCall == 0)) {
                                if(callCounterLast == 20) {
                                    callCounterLast = 0;
                                    callCounter = 0;
                                    frame1flag = true;
                                }
                                callCounterLast++;
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(outputTargetState == 3) {
                                        Log.d("ArcheryBoardActivity", "outputTarget : " + outputTarget + " / outputTargetState : " + outputTargetState);
                                        outputTargetState = 0;
                                        oneTime = 0;
                                        Toast.makeText(mContext, R.string.score_target_setting_message, Toast.LENGTH_LONG).show();

//                                        mAutoCheckbox.callOnClick();
                                        mCameraProgressBar.setVisibility(View.GONE);
                                        mAutoCheckbox.setChecked(false);
                                        return;
                                    }

//                                    if(frame2flag) {
//                                        mVideoProgressbarLayout.setVisibility(View.GONE);
//                                    }


                                    if ((callCounter == 10) && isMotionDetected == 0) {
                                        if(mTestBoard) {
                                            Log.i("Gus", "runOnUiThread : " + callCounter + " width : " + debugImg_.width());
                                            mAutoResultView.setVisibility(View.VISIBLE);
                                            mAutoResult2View.setVisibility(View.VISIBLE);
                                            mAutoResult3View.setVisibility(View.VISIBLE);
                                            mTouchView.setVisibility(View.VISIBLE);

                                            Bitmap outBitmap1 = null, outBitmap2 = null, outBitmap3 = null;

                                            if(null != debugImg_ && debugImg_.width() == 600) {
                                                outBitmap1 = convertMat2Bitmap(debugImg_);
                                                outBitmap1 = Bitmap.createScaledBitmap(outBitmap1, 240, 180, true);
                                            }
                                            if(null != frame1Clone && frame1Clone.width() == 600) {
                                                outBitmap2 = convertMat2Bitmap(frame1Clone);
                                                outBitmap2 = Bitmap.createScaledBitmap(outBitmap2, 240, 180, true);
                                            }
                                            if(null != frame2_ && frame2_.width() == 600) {
                                                outBitmap3 = convertMat2Bitmap(frame2_);
                                                outBitmap3 = Bitmap.createScaledBitmap(outBitmap3, 240, 180, true);
                                            }

                                            if(outBitmap1 != null)
                                                mAutoResultView.setContent(outBitmap1);

                                            if(outBitmap2 != null)
                                                mAutoResult2View.setContent(outBitmap2);

                                            if(outBitmap3 != null)
                                                mAutoResult3View.setContent(outBitmap3);

                                        }else {
                                            mAutoResultView.setVisibility(View.GONE);
                                            mAutoResult2View.setVisibility(View.GONE);
                                            mAutoResult3View.setVisibility(View.GONE);
                                            mTouchView.setVisibility(View.GONE);
                                        }


                                    }

                                    if(newScoreFlag){// new score detected
                                        newScoreFlag = false;
                                        goal = scoreOut;
                                        goalButtonPush();
                                        mCount = true;
                                    }else if((mCount && callCounter == 10) || (mCount && callCounter == 31)){
                                        Log.d("ArcheryBoardActivity", "mCount : " + mCount + ";; callCounter : " + callCounter);
                                        mCount = false;
                                        functionCall++;
                                    }else if(functionCall == (25+firstCall)){
                                        Log.d("ArcheryBoardActivity", "middle functionCall : " + functionCall + ";; callCounter : " + callCounter);
                                    }else if(functionCall == (70+firstCall)){
                                        Log.d("ArcheryBoardActivity", "last functionCall : " + functionCall + ";; callCounter : " + callCounter);
                                        functionCall = 0;
                                        firstCall = 0;

                                        outputTarget = 1;
                                        frame1flag = true;
                                    }

                                }
                            });

                        }


                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        return timerTask;
    }


    public static void setViewVisibility(boolean visibility) {
        if(visibility) {
            mPlayImg.setVisibility(View.VISIBLE);
//            mVideoText.setVisibility(View.VISIBLE);
        }else {
            mPlayImg.setVisibility(View.GONE);
//            mVideoText.setVisibility(View.GONE);
        }
    }
}

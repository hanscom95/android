package artech.com.fivics.score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.vipul.hp_hp.library.Layout_to_Image;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import artech.com.fivics.IntroActivity;
import artech.com.fivics.MainActivity;
import artech.com.fivics.R;
import artech.com.fivics.autoScoringNativeClass;
import artech.com.fivics.ranking.RankingActivity;
import artech.com.fivics.utility.AutoScoreView;
import artech.com.fivics.utility.Mail;
import artech.com.fivics.utility.NetworkConnection;
import artech.com.fivics.utility.Preferences;
import artech.com.fivics.utility.SendEmailAsyncTask;
import artech.com.fivics.utility.TouchView;

import static org.opencv.imgproc.Imgproc.cvtColor;

public class ArcheryBoardActivity extends AppCompatActivity {

    Context mContext;
    static Context mSContext;
    Preferences mPreference;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    RelativeLayout mBeforeLayout, mNextLayout, mExplanationLayout, mSignalLayout, mVideoProgressbarLayout;
    static RelativeLayout mEmailLayout, mMainLayout;
    ViewPager mViewPager;
    Button mInputButton, mSendButton, mAutoScoringButton;
    View goalView, mCameraView;
    TextView mArrowText, mTotalText, mNameText, mTimeText, mBoardSettingText;
    ImageView mRefreshImg, mHomeImg, msSettingImg, mLogoImg, mEmailCloseImg, mAutoScoreImg, mSignalImg;
    ProgressBar mProgressBar;
    EditText mEmailEdit;

    TouchView mTouchView;
    AutoScoreView mAutoResultView;
    AutoScoreView mAutoResult2View, mAutoResult3View;

//    CheckBox upLeftCheckbox, upCheckbox, upRightCheckbox, leftCheckbox, rightCheckbox, downLeftCheckbox, downRightCheckbox, downCheckbox;
    CheckBox goalZeroCheckbox, goalOneCheckbox, goalTwoCheckbox, goalThreeCheckbox, goalFourCheckbox, goalFiveCheckbox, goalSixCheckbox, goalSevenCheckbox, goalEightCheckbox, goalNineCheckbox, goalTenCheckbox, goalFullScaleCheckbox;

    ArcheryPagerAdapter mAdapter;

    int score = 0;
    int round = 0;
//    int direction = 0;
    int goal = 0;
    int timer = 0;

    String mPhone = "", mName = "";
    int mMember = 0;
    int mMenu = 0;
    int mFlag = 0;

    String URL = "rtsp://192.168.2.169:554/ch1/sub/av_stream";//"rtmp://192.168.1.88:1935/flash/11:admin:admin";//"rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";

    ArrayList<Integer> round_score = new ArrayList<>();
//    ArrayList<Integer> round_direction = new ArrayList<>();

    TimerTask mTask, mCameraTask;
    Timer mTimer, mCameraTimer;

    ScoreInsertTask scoreInsertTask = null;

    SoundPool mSound = null;

    TextView mBannelShopText;
    ViewFlipper mBannelInfoText;

    String mBannelNumber = "";
    String[] mBannelInfo;

    int mFinishTime = 0, mStartTime = 0;
    int mFinish15Arrow = 0, mFinish20Arrow = 0;
    int mSoundId_0 = 0, mSoundId_1 = 0, mSoundId_2 = 0, mSoundId_3 = 0, mSoundId_4 = 0, mSoundId_5 = 0, mSoundId_6 = 0, mSoundId_7 = 0, mSoundId_8 = 0, mSoundId_9 = 0, mSoundId_10 = 0, mSoundId_10x = 0, mCountDown = 0, mAutoScoring = 0, mWaiting = 0;

    boolean mPopupBoolean = false;

//    Mat frame1_, frame2_, debugImg, input, output;
    Mat frame1_, frame2_, frame1__, debugImg_, input, output, frame1Clone;

    int scoreOut = -1;
    int callCounter = 0;
    int functionCall = 0;
    int firstCall = 0;
    int callCounterLast = 0;

    int oneTime = 0;
    int outputTarget = -1;
    int outputTargetState = 0;

    boolean frame1flag = false;
    boolean frame2flag = false;
    int isMotionDetected = 0;

    int[] colorCoors;
    float[] foundElipses;

    boolean mState = false;
    boolean mPosition = true;
    boolean mCount = true;
    boolean mTestBoard = false;
    boolean newScoreFlag = false;
    boolean firstCallFlag = true;

    static {
        System.loadLibrary("MyLibs");
        System.loadLibrary("MyOpencvLibs");

        if (OpenCVLoader.initDebug()) {
            // Do nothing to manage the error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mSContext = this;
        mPreference = Preferences.getInstance(mContext);
        URL = mPreference.getURL();
        colorCoors = mPreference.getLocation();
        foundElipses = mPreference.getElipses();

        setContentView(R.layout.content_archery_board);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            }
        });

        Configuration config = new Configuration();
        config.setLocale(Locale.CHINESE);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());



        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.archery_video_view, ArcheryCameraFragment.newInstance(URL),"archery")
                    .commit();
        }

        mCameraView = (View) findViewById(R.id.archery_video_view);

        mBeforeLayout = (RelativeLayout) findViewById(R.id.before_layout);
        mNextLayout = (RelativeLayout) findViewById(R.id.next_layout);
        mExplanationLayout = (RelativeLayout) findViewById(R.id.explanation_layout);

        mArrowText = (TextView) findViewById(R.id.arrow_text);
        mTotalText = (TextView) findViewById(R.id.total_text);
        mNameText = (TextView) findViewById(R.id.board_user_name_text);
        mTimeText = (TextView) findViewById(R.id.board_time_text);
        mBoardSettingText = (TextView) findViewById(R.id.board_date_text);

        mInputButton = (Button) findViewById(R.id.score_input_button);

        mEmailLayout = (RelativeLayout) findViewById(R.id.email_layout);
        mMainLayout = (RelativeLayout) findViewById(R.id.archery_main_layout);
        mEmailEdit = (EditText) findViewById(R.id.email_edit);
        mSendButton = (Button) findViewById(R.id.send_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mViewPager = (ViewPager) findViewById(R.id.board_viewPager) ;

        mLogoImg = (ImageView) findViewById(R.id.archery_logo);
        mHomeImg = (ImageView) findViewById(R.id.home_button);
        mRefreshImg = (ImageView) findViewById(R.id.refresh_button);
        msSettingImg = (ImageView) findViewById(R.id.setting_button);
        mEmailCloseImg = (ImageView) findViewById(R.id.email_close_img);
        mAutoScoreImg = (ImageView) findViewById(R.id.auto_score_img);


        mTouchView = (TouchView) findViewById(R.id.archery_video_touch_view);
        mAutoResultView = (AutoScoreView) findViewById(R.id.archery_auto_score_result_view);
        mAutoResult2View = (AutoScoreView) findViewById(R.id.archery_auto_score_result2_view);
        mAutoResult3View = (AutoScoreView) findViewById(R.id.archery_auto_score_result3_view);
//        mAutoResultView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mAutoScoringButton = (Button) findViewById(R.id.auto_scoring_button);

        mBannelShopText = (TextView) findViewById(R.id.main_title);
        mBannelInfoText = (ViewFlipper) findViewById(R.id.main_sub_title);

        mSignalLayout = (RelativeLayout) findViewById(R.id.signal_layout);
        mSignalImg = (ImageView) findViewById(R.id.signal_img);

        mVideoProgressbarLayout = (RelativeLayout) findViewById(R.id.video_progressbar_layout);

        itemSelected();

        mRefreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                fragment.updateInstance();
            }
        });

        mHomeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorCoors[0] = mTouchView.getP1X();
                colorCoors[1] = mTouchView.getP1Y();
                colorCoors[2] = mTouchView.getP2X();
                colorCoors[3] = mTouchView.getP2Y();
                colorCoors[4] = mTouchView.getP3X();
                colorCoors[5] = mTouchView.getP3Y();
                colorCoors[6] = mTouchView.getP4X();
                colorCoors[7] = mTouchView.getP4Y();
                colorCoors[8] = mTouchView.getP5X();
                colorCoors[9] = mTouchView.getP5Y();
//
//                mPreference.setLocation(colorCoors);
//
//                mPosition = true;


                if(colorCoors[0] > 0 && colorCoors[1] > 0 && colorCoors[2] > 0 && colorCoors[3] > 0 && colorCoors[4] > 0 && colorCoors[5] > 0 && colorCoors[6] > 0 && colorCoors[7] > 0 && colorCoors[8] > 0 && colorCoors[9] > 0) {
                    mPreference.setLocation(colorCoors);
                    mPreference.setElipses(foundElipses);
                    Toast.makeText(mContext, "오토 스코어 포지션 데이터를 저장합니다.", Toast.LENGTH_LONG).show();
                }else {
                    mTouchView.clearFlag();
                }
            }
        });

        msSettingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestBoard = !mTestBoard;

                outputTarget = 0;
                frame1flag = true;

                String txt = "과녁을 보여 줍니다. " + mTestBoard;
                Toast.makeText(mContext, txt, Toast.LENGTH_LONG).show();
            }
        });

        /*msSettingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = new EditText(mContext);
                text.setText(URL);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Camera 주소");
                builder.setView(text);
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        URL = text.getText().toString();
                        ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                        fragment.mFilePath = URL;
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.show();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                    }
                });
            }
        });*/

        mLogoImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        mEmailCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailLayout.setVisibility(View.GONE);
                mMainLayout.setVisibility(View.VISIBLE);
            }
        });

        mBeforeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMember);
                bundle.putInt("menu", mMenu);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        mNextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mAutoScoringButton.setBackgroundResource(R.mipmap.btn_autoscoring2_nonpress);
//                mAutoResultView.setVisibility(View.GONE);

//                mTask.cancel();
//                mTask = null;

//                mTimer.cancel();
//                mTimer = null;

//                mTouchView.clearFlag();
//                mState = false;

                finishBoardActivity(0);

//            mNextLayout.setVisibility(View.VISIBLE);

                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate (R.layout.content_archery_finish_popup, null);
                alertDialogBuilder.setView(dialogView);
                AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().setLayout(1190, 595);
                alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                    }
                });


                TextView nowRoundText = (TextView) dialogView.findViewById(R.id.now_round_text);
                TextView nextPleaseRoundText = (TextView) dialogView.findViewById(R.id.next_round_please_text);
                TextView nextRoundText = (TextView) dialogView.findViewById(R.id.next_round_text);
                TextView finishRoundText = (TextView) dialogView.findViewById(R.id.finish_round_text);

                nowRoundText.setText((round+1)+"ROUND 를 완료하였습니다.");
                nextPleaseRoundText.setText("추가결제 후 "+(round+2)+" 를 진행하시겠습니까?");

                nextRoundText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNextLayout.setVisibility(View.INVISIBLE);
                        alertDialog.dismiss();

                        score = 0;
                        ++round;

                        LayoutInflater scoreInflater = getLayoutInflater();
                        if(mFlag == 4) {
                            goalView = scoreInflater.inflate (R.layout.content_score_board_two, null);
                        }else {
                            goalView = scoreInflater.inflate (R.layout.content_score_board, null);
                        }
                        mAdapter.addView (goalView, 0);
                        mAdapter.notifyDataSetChanged();

                        TextView roundText = (TextView) goalView.findViewById(R.id.score_round_text);
                        roundText.setText((round+1) + "\nR");

                        mViewPager.setCurrentItem(0);
                    }
                });

                finishRoundText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

//                            ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
//                            Bitmap bitmap = fragment.screenShot();
//                            ImageView camerImg = (ImageView) resultDialogView.findViewById(R.id.camera_img);
//                            camerImg.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));

                        AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(mContext);
                        LayoutInflater inflater = getLayoutInflater();
                        View resultDialogView = inflater.inflate (R.layout.content_archery_finish_result_popup, null);
                        resultDialogBuilder.setView(resultDialogView);
                        AlertDialog resultDialog = resultDialogBuilder.show();
                        resultDialog.getWindow().setLayout(1572, 786);
                        resultDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());


                        TextView noText = (TextView) resultDialogView.findViewById(R.id.no_text);
                        noText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultDialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("phone", mPhone);
                                bundle.putString("name", mName);
                                bundle.putInt("member", mMember);
                                bundle.putInt("menu", mMenu);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        });

                        TextView yesText = (TextView) resultDialogView.findViewById(R.id.yes_text);
                        yesText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultDialog.dismiss();

                                AlertDialog.Builder nfcDialogBuilder = new AlertDialog.Builder(mContext);
                                View nfcView = new View(mContext);
                                nfcView.setBackgroundResource(R.mipmap.nfc_popup);
                                nfcDialogBuilder.setView(nfcView);
                                AlertDialog nfcDialog = nfcDialogBuilder.show();
                                nfcDialog.getWindow().setLayout(1572, 786);
                                nfcDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());

                                nfcView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone", mPhone);
                                        bundle.putString("name", mName);
                                        bundle.putInt("member", mMember);
                                        bundle.putInt("menu", mMenu);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        });
                    }
                });*/

            }
        });

        mExplanationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExplanationLayout.setVisibility(View.GONE);
            }
        });

//        mInputButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                LayoutInflater inflater = getLayoutInflater();
////                View layout = (View) inflater.inflate (R.layout.content_score_board, null);
////                mAdapter.addView (layout, 0);
////                mAdapter.notifyDataSetChanged();
//
//                int socreFlag = 0;
//                if(mFlag == 4) {
//                    socreFlag = 20;
//                }else {
//                    socreFlag = 15;
//                }
//
//                if(score == socreFlag){
//                    mNextLayout.setVisibility(View.VISIBLE);
//
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
//                    LayoutInflater inflater = getLayoutInflater();
//                    View dialogView = inflater.inflate (R.layout.content_archery_finish_popup, null);
//                    alertDialogBuilder.setView(dialogView);
//                    AlertDialog alertDialog = alertDialogBuilder.show();
//                    alertDialog.getWindow().setLayout(1190, 595);
//                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
//                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                        @Override
//                        public void onShow(DialogInterface dialog) {
//                            alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
//                        }
//                    });
//
//
//                    TextView nowRoundText = (TextView) dialogView.findViewById(R.id.now_round_text);
//                    TextView nextPleaseRoundText = (TextView) dialogView.findViewById(R.id.next_round_please_text);
//                    TextView nextRoundText = (TextView) dialogView.findViewById(R.id.next_round_text);
//                    TextView finishRoundText = (TextView) dialogView.findViewById(R.id.finish_round_text);
//
//                    nowRoundText.setText((round+1)+"ROUND 를 완료하였습니다.");
//                    nextPleaseRoundText.setText("추가결제 후 "+(round+2)+" 를 진행하시겠습니까?");
//
//                    nextRoundText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//
//                            score = 0;
//                            ++round;
//
//                            LayoutInflater scoreInflater = getLayoutInflater();
//                            if(mFlag == 4) {
//                                goalView = scoreInflater.inflate (R.layout.content_score_board_two, null);
//                            }else {
//                                goalView = scoreInflater.inflate (R.layout.content_score_board, null);
//                            }
//                            mAdapter.addView (goalView, 0);
//                            mAdapter.notifyDataSetChanged();
//
//                            TextView roundText = (TextView) goalView.findViewById(R.id.score_round_text);
//                            roundText.setText((round+1) + "R");
//
//                            mViewPager.setCurrentItem(0);
//                        }
//                    });
//
//                    finishRoundText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//
////                            ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
////                            Bitmap bitmap = fragment.screenShot();
////                            ImageView camerImg = (ImageView) resultDialogView.findViewById(R.id.camera_img);
////                            camerImg.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
//
//                            AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(mContext);
//                            LayoutInflater inflater = getLayoutInflater();
//                            View resultDialogView = inflater.inflate (R.layout.content_archery_finish_result_popup, null);
//                            resultDialogBuilder.setView(resultDialogView);
//                            AlertDialog resultDialog = resultDialogBuilder.show();
//                            resultDialog.getWindow().setLayout(1572, 786);
//                            resultDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
//
//
//                            TextView noText = (TextView) resultDialogView.findViewById(R.id.no_text);
//                            noText.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    resultDialog.dismiss();
//
//                                    Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("phone", mPhone);
//                                    bundle.putString("name", mName);
//                                    bundle.putInt("member", mMember);
//                                    bundle.putInt("menu", mMenu);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });
//
//                            TextView yesText = (TextView) resultDialogView.findViewById(R.id.yes_text);
//                            yesText.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    resultDialog.dismiss();
//
//                                    AlertDialog.Builder nfcDialogBuilder = new AlertDialog.Builder(mContext);
//                                    View nfcView = new View(mContext);
//                                    nfcView.setBackgroundResource(R.mipmap.nfc_popup);
//                                    nfcDialogBuilder.setView(nfcView);
//                                    AlertDialog nfcDialog = nfcDialogBuilder.show();
//                                    nfcDialog.getWindow().setLayout(1572, 786);
//                                    nfcDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
//
//                                    nfcView.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("phone", mPhone);
//                                            bundle.putString("name", mName);
//                                            bundle.putInt("member", mMember);
//                                            bundle.putInt("menu", mMenu);
//                                            intent.putExtras(bundle);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    });
//
//                                }
//                            });
//                        }
//                    });
//
//
//
//                }else {
//                    mNextLayout.setVisibility(View.INVISIBLE);
//
//                    LayoutInflater inflater = getLayoutInflater();
//                    View dialogView = inflater.inflate(R.layout.content_archery_goal, null);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
////        builder.setTitle("Goal Setting");
//                    builder.setView(dialogView);
//
//                    AlertDialog alertDialog = builder.show();
//                    alertDialog.getWindow().setLayout(400, 500);
//                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
//                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                        @Override
//                        public void onShow(DialogInterface dialog) {
//                            alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
//                        }
//                    });
//
//                    TextView cancelText = (TextView) dialogView.findViewById(R.id.cancel_text);
//                    TextView confirmText = (TextView) dialogView.findViewById(R.id.confirm_text);
//
//                    cancelText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                    confirmText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            direction = 0;
//                            goal = 0;
//
//                            if (upLeftCheckbox.isChecked()) {
//                                direction = 1;
//                            } else if (upCheckbox.isChecked()) {
//                                direction = 2;
//                            } else if (upRightCheckbox.isChecked()) {
//                                direction = 3;
//                            } else if (leftCheckbox.isChecked()) {
//                                direction = 4;
//                            } else if (rightCheckbox.isChecked()) {
//                                direction = 5;
//                            } else if (downLeftCheckbox.isChecked()) {
//                                direction = 6;
//                            } else if (downCheckbox.isChecked()) {
//                                direction = 7;
//                            } else if (downRightCheckbox.isChecked()) {
//                                direction = 8;
//                            }
//
//
//                            if (goalZeroCheckbox.isChecked()) {
//                                goal = 0;
//                            } else if (goalOneCheckbox.isChecked()) {
//                                goal = 1;
//                            } else if (goalTwoCheckbox.isChecked()) {
//                                goal = 2;
//                            } else if (goalThreeCheckbox.isChecked()) {
//                                goal = 3;
//                            } else if (goalFourCheckbox.isChecked()) {
//                                goal = 4;
//                            } else if (goalFiveCheckbox.isChecked()) {
//                                goal = 5;
//                            } else if (goalSixCheckbox.isChecked()) {
//                                goal = 6;
//                            } else if (goalSevenCheckbox.isChecked()) {
//                                goal = 7;
//                            } else if (goalEightCheckbox.isChecked()) {
//                                goal = 8;
//                            } else if (goalNineCheckbox.isChecked()) {
//                                goal = 9;
//                            } else if (goalTenCheckbox.isChecked()) {
//                                goal = 10;
//                            } else if (goalFullScaleCheckbox.isChecked() || goal == 11) {
//                                goal = 11;
//                            }
//
//                            if(mFlag == 4){
//                                if (score == 0) {
//                                    goalDialogShow1(goalView);
//                                } else if (score == 1) {
//                                    goalDialogShow2(goalView);
//                                } else if (score == 2) {
//                                    goalDialogShow3(goalView);
//                                } else if (score == 3) {
//                                    goalDialogShow4(goalView);
//                                } else if (score == 4) {
//                                    goalDialogShow5(goalView);
//                                } else if (score == 5) {
//                                    goalDialogShow6(goalView);
//                                } else if (score == 6) {
//                                    goalDialogShow7(goalView);
//                                } else if (score == 7) {
//                                    goalDialogShow8(goalView);
//                                } else if (score == 8) {
//                                    goalDialogShow9(goalView);
//                                } else if (score == 9) {
//                                    goalDialogShow10(goalView);
//                                } else if (score == 10) {
//                                    goalDialogShow11(goalView);
//                                } else if (score == 11) {
//                                    goalDialogShow12(goalView);
//                                } else if (score == 12) {
//                                    goalDialogShow13(goalView);
//                                } else if (score == 13) {
//                                    goalDialogShow14(goalView);
//                                } else if (score == 14) {
//                                    goalDialogShow15(goalView);
//                                } else if (score == 15) {
//                                    goalDialogShow16(goalView);
//                                } else if (score == 16) {
//                                    goalDialogShow17(goalView);
//                                } else if (score == 17) {
//                                    goalDialogShow18(goalView);
//                                } else if (score == 18) {
//                                    goalDialogShow19(goalView);
//                                } else if (score == 19) {
//                                    goalDialogShow20(goalView);
//                                }
//                            }else {
//                                if (score == 0) {
//                                    goalDialogShow1(goalView);
//                                } else if (score == 1) {
//                                    goalDialogShow2(goalView);
//                                } else if (score == 2) {
//                                    goalDialogShow3(goalView);
//                                } else if (score == 3) {
//                                    goalDialogShow4(goalView);
//                                } else if (score == 4) {
//                                    goalDialogShow5(goalView);
//                                } else if (score == 5) {
//                                    goalDialogShow6(goalView);
//                                } else if (score == 6) {
//                                    goalDialogShow7(goalView);
//                                } else if (score == 7) {
//                                    goalDialogShow8(goalView);
//                                } else if (score == 8) {
//                                    goalDialogShow9(goalView);
//                                } else if (score == 9) {
//                                    goalDialogShow10(goalView);
//                                } else if (score == 10) {
//                                    goalDialogShow11(goalView);
//                                } else if (score == 11) {
//                                    goalDialogShow12(goalView);
//                                } else if (score == 12) {
//                                    goalDialogShow13(goalView);
//                                } else if (score == 13) {
//                                    goalDialogShow14(goalView);
//                                } else if (score == 14) {
//                                    goalDialogShow15(goalView);
//                                }
//                            }
//
//                            int total = 0;
//                            for(int i = 0; i < round_score.size(); i++) {
//                                total += round_score.get(i);
//                            }
//
//                            mArrowText.setText(round_score.size()+"");
//                            mTotalText.setText(total + "");
//
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                    upLeftCheckbox = (CheckBox) dialogView.findViewById(R.id.up_left_checkbox);
//                    upCheckbox = (CheckBox) dialogView.findViewById(R.id.up_checkbox);
//                    upRightCheckbox = (CheckBox) dialogView.findViewById(R.id.up_right_checkbox);
//                    leftCheckbox = (CheckBox) dialogView.findViewById(R.id.left_checkbox);
//                    rightCheckbox = (CheckBox) dialogView.findViewById(R.id.right_checkbox);
//                    downLeftCheckbox = (CheckBox) dialogView.findViewById(R.id.down_left_checkbox);
//                    downRightCheckbox = (CheckBox) dialogView.findViewById(R.id.down_right_checkbox);
//                    downCheckbox = (CheckBox) dialogView.findViewById(R.id.down_checkbox);
//
//                    goalZeroCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_zero);
//                    goalOneCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_one);
//                    goalTwoCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_two);
//                    goalThreeCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_three);
//                    goalFourCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_four);
//                    goalFiveCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_five);
//                    goalSixCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_six);
//                    goalSevenCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_seven);
//                    goalEightCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_eight);
//                    goalNineCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_nine);
//                    goalTenCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_ten);
//                    goalFullScaleCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_full_scale);
//
//                    upLeftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//                    upCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//                    upRightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//                    leftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//                    rightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//                    downLeftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//                    downRightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//                    downCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//
//                    goalZeroCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalOneCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalTwoCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalThreeCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalFourCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalFiveCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalSixCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalSevenCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalEightCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalNineCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalTenCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                    goalFullScaleCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
//                }
//            }
//        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);


                String[] recipients = { mEmailEdit.getText().toString() };
                SendEmailAsyncTask email = new SendEmailAsyncTask(mContext, mProgressBar);
//                email.m = new Mail(user.getText().toString(), pass.getText()
//                        .toString());
                email.m = new Mail();
//                email.m.set_from("");
                email.m.setBody("슈팅존을 이용해 주셔서 감사합니다.");
                email.m.setPath("fivics_popup.jpg");
                email.m.set_to(recipients);
                email.m.set_subject("SHOOTING ZONE");
                email.execute();

                ArrayList<Object> array = new ArrayList<Object>();
                if(mName != null || !"".equals(mName)) {
                    array.add(mName);
                    array.add(mPhone);
                    array.add(mTotalText.getText().toString());
                    array.add(mArrowText.getText().toString());
                    array.add(mMember);
                    array.add("수원권선점");

                    scoreInsertTask = new ScoreInsertTask(array, 1);
                    scoreInsertTask.execute();
                }

            }
        });



        mAutoScoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mState) {
                    frame1flag = true;
                    mAutoScoringButton.setBackgroundResource(R.mipmap.btn_autoscoring2_press);
                    mVideoProgressbarLayout.setVisibility(View.VISIBLE);
//                    mAutoResultView.setVisibility(View.VISIBLE);

                    String TAG = "Color";

                    if(mTouchView.getP1X() > 0 && mTouchView.getP1Y() > 0 && mTouchView.getP2X() > 0 && mTouchView.getP2Y() > 0 && mTouchView.getP3X() > 0 && mTouchView.getP3Y() > 0 && mTouchView.getP4X() > 0 && mTouchView.getP4Y() > 0 && mTouchView.getP5X() > 0 && mTouchView.getP5Y() > 0) {
                        if(firstCallFlag) {
                            firstCall = 0;
                            foundElipses[0] = 0.0f;
                            mSound.play(mStartTime, 1.0F, 1.0F, 1, 0, 1.0F);
                            firstCallFlag = false;
                        }

                        mPosition = true;
                        colorCoors[0] = (int)(mTouchView.getP1X() * 0.7692);
                        colorCoors[1] = (int)(mTouchView.getP1Y() * 0.7692);
                        colorCoors[2] = (int)(mTouchView.getP2X() * 0.7692);
                        colorCoors[3] = (int)(mTouchView.getP2Y() * 0.7692);
                        colorCoors[4] = (int)(mTouchView.getP3X() * 0.7692);
                        colorCoors[5] = (int)(mTouchView.getP3Y() * 0.7692);
                        colorCoors[6] = (int)(mTouchView.getP4X() * 0.7692);
                        colorCoors[7] = (int)(mTouchView.getP4Y() * 0.7692);
                        colorCoors[8] = (int)(mTouchView.getP5X() * 0.7692);
                        colorCoors[9] = (int)(mTouchView.getP5Y() * 0.7692);

                        mCameraTask = mTimerTaskMaker();

                        mCameraTimer = new Timer();
                        mCameraTimer.schedule(mCameraTask, 2, 100);
                    }else if(colorCoors[0] > 0 && colorCoors[1] > 0 && colorCoors[2] > 0 && colorCoors[3] > 0 && colorCoors[4] > 0 && colorCoors[5] > 0 && colorCoors[6] > 0 && colorCoors[7] > 0 && colorCoors[8] > 0 && colorCoors[9] > 0) {
                        if(firstCallFlag) {
                            firstCall = 20;
                            mSound.play(mStartTime, 1.0F, 1.0F, 1, 0, 1.0F);
                            firstCallFlag = false;
                        }

                        mPosition = true;
                        mCameraTask = mTimerTaskMaker();

                        mCameraTimer = new Timer();
                        mCameraTimer.schedule(mCameraTask, 2, 100);
                    } else {
                        mAutoScoringButton.setBackgroundResource(R.mipmap.btn_autoscoring2_nonpress);
                        mAutoResultView.setVisibility(View.GONE);
                        mAutoResult2View.setVisibility(View.GONE);
                        mAutoResult3View.setVisibility(View.GONE);
                        mTouchView.clearFlag();

                        Toast.makeText(mContext, "저장된 데이터나 선택된 데이터가 없습니다.", Toast.LENGTH_LONG).show();
                        mPosition = false;
                        mState = true;
                    }
                } else {
                    mAutoScoringButton.setBackgroundResource(R.mipmap.btn_autoscoring2_nonpress);
                    mAutoResultView.setVisibility(View.GONE);
                    mAutoResult2View.setVisibility(View.GONE);
                    mAutoResult3View.setVisibility(View.GONE);
                    mVideoProgressbarLayout.setVisibility(View.GONE);

                    mCount = true;
                    functionCall = 0;

                    if (mCameraTask != null) {
                        mCameraTask.cancel();
                        mCameraTask = null;
                    }

                    if (mCameraTask != null) {
                        mCameraTimer.cancel();
                        mCameraTimer = null;
                    }

                    mTouchView.clearFlag();
                }

                mState = !mState;
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPhone = bundle.getString("phone");
        mName = bundle.getString("name");
        mFlag = bundle.getInt("flag");
        mMember = bundle.getInt("member");
        mMenu = bundle.getInt("menu");
        mBannelNumber = bundle.getString("bannelNummber");
        mBannelInfo = bundle.getStringArray("bannelInfo");


        if(!"".equals(mBannelNumber) && mBannelNumber != null) {
            mBannelShopText.setText(mBannelNumber);
            for (String string : mBannelInfo) {
                TextView textView = new TextView(mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                textView.setLayoutParams(params);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setIncludeFontPadding(false);
                textView.setLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setSingleLine();
                textView.setFocusable(true);
                textView.setFocusableInTouchMode(true);
                textView.setTextSize(22);
                textView.setTextColor(getResources().getColor(R.color.main_grey));
//                        textView.setFont("NotoSansCJKkr_Medium.otf");
                textView.setText(string);
                mBannelInfoText.addView(textView);
            }
        }

        Log.d("ARcheryBoard", "mName : " + mName);
        if(mName != null && !"".equals(mName)) {
            String name = "";
            name = mName+"("+mPhone.substring(mPhone.length()-4,mPhone.length())+")";
            mNameText.setText(name);
        }

        Log.d("ArcheryBoardActivity", "flag : " + mFlag);
        if(mMember == 1) {
            if (mFlag == 1) {
                mBoardSettingText.setText("15발");
            } else if (mFlag == 2) {
                mBoardSettingText.setText("30분");
            } else if (mFlag == 3) {
                mBoardSettingText.setText("1시간");
            } else if (mFlag == 4) {
                mBoardSettingText.setText("20발");
            } else if (mFlag == 5) {
                mBoardSettingText.setText("30분");
            } else if (mFlag == 6) {
                mBoardSettingText.setText("야외체험");
            }
        }else{
            if (mFlag == 1) {
                mBoardSettingText.setText("12발");
            }else if(mFlag == 2) {
                mBoardSettingText.setText("30발");
            }else if(mFlag == 3) {
                mBoardSettingText.setText("36발");
            }else {
                mBoardSettingText.setText("연습");
            }
        }

//        Date from = new Date();
//        SimpleDateFormat transFormat = new SimpleDateFormat("HH:mm:ss");
//        String date = transFormat.format(from);
        mTimeText.setText("");

        mAdapter = new ArcheryPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        // Create an initial view to display; must be a subclass of FrameLayout.
        LayoutInflater inflater = getLayoutInflater();
        if(mFlag == 4 || (mMember == 2 && mFlag == 3)) {
            goalView = inflater.inflate(R.layout.content_score_board_two, null);
        }else {
            goalView = inflater.inflate(R.layout.content_score_board, null);
        }
        mAdapter.addView (goalView, 0);
        mAdapter.notifyDataSetChanged();

        mSound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        mSoundId_0 = mSound.load(mContext, R.raw.number_0, 1);
        mSoundId_1 = mSound.load(mContext, R.raw.number_1, 1);
        mSoundId_2 = mSound.load(mContext, R.raw.number_2, 1);
        mSoundId_3 = mSound.load(mContext, R.raw.number_3, 1);
        mSoundId_4 = mSound.load(mContext, R.raw.number_4, 1);
        mSoundId_5 = mSound.load(mContext, R.raw.number_5, 1);
        mSoundId_6 = mSound.load(mContext, R.raw.number_6, 1);
        mSoundId_7 = mSound.load(mContext, R.raw.number_7, 1);
        mSoundId_8 = mSound.load(mContext, R.raw.number_8, 1);
        mSoundId_9 = mSound.load(mContext, R.raw.number_9, 1);
        mSoundId_10 = mSound.load(mContext, R.raw.number_10, 1);
        mSoundId_10x = mSound.load(mContext, R.raw.number_10_x, 1);
        mFinishTime = mSound.load(mContext, R.raw.finish_voice, 1);
        mStartTime = mSound.load(mContext, R.raw.start_voice, 1);
        mCountDown = mSound.load(mContext, R.raw.countdown, 1);
        mAutoScoring = mSound.load(mContext, R.raw.auto_scoring_voice, 1);
        mWaiting = mSound.load(mContext, R.raw.waiting_sound, 1);

        mFinish15Arrow = mSound.load(mContext, R.raw.finish_voice_15, 1);
        mFinish20Arrow = mSound.load(mContext, R.raw.finish_voice_20, 1);


        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          timer++;

                          if(mMember==1 && (mFlag == 2 || mFlag == 5)) {
                              int minute_30 = 1800-timer;
                              int second = minute_30%3600%60;
                              int minute = minute_30/60%60;
                              int hour = minute_30/3600;
                              mTimeText.setText((hour < 10 ? "0" + hour : hour)+":"+(minute < 10 ? "0" + minute : minute)+":"+(second < 10 ? "0" + second : second));

                              if(minute_30 == 0) {
                                  mSound.play(mFinishTime, 1.0F, 1.0F,  0,  0,  1.0F);
                                  finishBoardActivity(1);
                                  mTimer.cancel();
                              }
                          }else if(mMember==1 && (mFlag == 3)) {
                              int hour_1 = 3600-timer;
                              int second = hour_1%3600%60;
                              int minute = hour_1/60%60;
                              int hour = hour_1/3600;
                              mTimeText.setText((hour < 10 ? "0" + hour : hour)+":"+(minute < 10 ? "0" + minute : minute)+":"+(second < 10 ? "0" + second : second));

                              if(hour_1 == 0) {
                                  mSound.play(mFinishTime, 1.0F, 1.0F,  0,  0,  1.0F);
                                  finishBoardActivity(1);
                                  mTimer.cancel();
                              }
                          }else {
                              int hour = timer/3600;
                              int second = timer%3600%60;
                              int minute = timer/60%60;
                              mTimeText.setText((hour < 10 ? "0" + hour : hour)+":"+(minute < 10 ? "0" + minute : minute)+":"+(second < 10 ? "0" + second : second));
//                              Date from = new Date();
//                              SimpleDateFormat transFormat = new SimpleDateFormat("HH:mm:ss");
//                              String date = transFormat.format(from);
//                              mTimeText.setText(date);
                          }
                      }
                  });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 1000, 1000);
    }

//    private CompoundButton.OnCheckedChangeListener directionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            upLeftCheckbox.setChecked(false);
//            upCheckbox.setChecked(false);
//            upRightCheckbox.setChecked(false);
//            leftCheckbox.setChecked(false);
//            rightCheckbox.setChecked(false);
//            downLeftCheckbox.setChecked(false);
//            downCheckbox.setChecked(false);
//            downRightCheckbox.setChecked(false);
//
//            if(isChecked){
//                buttonView.setChecked(true);
//            }
//        }
//    };

    private CompoundButton.OnCheckedChangeListener goalCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            }
        }
    };

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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score1_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score1_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score1_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score1_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score1_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score1_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score1_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score1_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score2_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score2_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score2_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score2_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score2_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score2_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score2_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score2_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score3_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score3_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score3_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score3_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score3_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score3_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score3_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score3_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score4_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score4_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score4_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score4_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score4_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score4_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score4_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score4_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score5_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score5_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score5_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score5_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score5_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score5_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score5_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score5_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score6_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score6_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score6_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score6_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score6_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score6_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score6_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score6_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score7_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score7_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score7_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score7_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score7_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score7_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score7_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score7_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score8_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score8_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score8_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score8_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score8_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score8_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score8_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score8_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score9_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score9_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score9_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score9_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score9_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score9_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score9_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score9_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score10_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score10_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score10_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score10_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score10_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score10_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score10_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score10_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score11_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score11_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score11_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score11_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score11_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score11_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score11_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score11_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score12_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score12_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score12_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score12_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score12_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score12_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score12_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score12_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
    }

    public void goalDialogShow13(View view, int layoutValue) {
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score13_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score13_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score13_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score13_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score13_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score13_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score13_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score13_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);
//
//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score14_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score14_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score14_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score14_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score14_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score14_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score14_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score14_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score15_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score15_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score15_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score15_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score15_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score15_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score15_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score15_circle8);

//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
    }

    public void goalDialogShow16(View view, int layoutValue) {
        //20170919 score++;score++;

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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score16_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score16_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score16_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score16_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score16_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score16_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score16_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score16_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
    }

    public void goalDialogShow17(View view, int layoutValue) {
        //20170919 score++;score++;

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
//        round_direction.add(direction);
//
//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score17_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score17_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score17_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score17_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score17_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score17_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score17_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score17_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
    }

    public void goalDialogShow18(View view, int layoutValue) {
        //20170919 score++;score++;

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
//        round_direction.add(direction);
//
//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score18_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score18_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score18_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score18_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score18_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score18_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score18_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score18_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
    }

    public void goalDialogShow19(View view, int layoutValue) {
        //20170919 score++;score++;

        TextView goalText = (TextView) view.findViewById(R.id.score19_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score19_layout);

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
//        round_direction.add(direction);
//
//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score19_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score19_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score19_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score19_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score19_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score19_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score19_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score19_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
    }

    public void goalDialogShow20(View view, int layoutValue) {
        //20170919 score++;score++;

        TextView goalText = (TextView) view.findViewById(R.id.score20_value);
        RelativeLayout goalLayout = (RelativeLayout) view.findViewById(R.id.score20_layout);

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
//        round_direction.add(direction);

//        ImageView directionImg1 = (ImageView) view.findViewById(R.id.score20_circle1);
//        ImageView directionImg2 = (ImageView) view.findViewById(R.id.score20_circle2);
//        ImageView directionImg3 = (ImageView) view.findViewById(R.id.score20_circle3);
//        ImageView directionImg4 = (ImageView) view.findViewById(R.id.score20_circle4);
//        ImageView directionImg5 = (ImageView) view.findViewById(R.id.score20_circle5);
//        ImageView directionImg6 = (ImageView) view.findViewById(R.id.score20_circle6);
//        ImageView directionImg7 = (ImageView) view.findViewById(R.id.score20_circle7);
//        ImageView directionImg8 = (ImageView) view.findViewById(R.id.score20_circle8);
//
//        directionImg1.setVisibility(View.INVISIBLE);
//        directionImg2.setVisibility(View.INVISIBLE);
//        directionImg3.setVisibility(View.INVISIBLE);
//        directionImg4.setVisibility(View.INVISIBLE);
//        directionImg5.setVisibility(View.INVISIBLE);
//        directionImg6.setVisibility(View.INVISIBLE);
//        directionImg7.setVisibility(View.INVISIBLE);
//        directionImg8.setVisibility(View.INVISIBLE);

        goalText.setTextColor(getResources().getColor(R.color.archery_black));

        if (goal <= 2) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_grey_bg);
        } else if (goal <= 4) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_black_bg);
            goalText.setTextColor(getResources().getColor(R.color.archery_grey));
//            directionImg1.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg2.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg3.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg4.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg5.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg6.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg7.setImageResource(R.drawable.acrchery_grey_circle);
//            directionImg8.setImageResource(R.drawable.acrchery_grey_circle);
        } else if (goal <= 6) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_blue_bg);
        } else if (goal <= 8) {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_red_bg);
        } else {
            goalLayout.setBackgroundResource(R.drawable.table_cell_round_yellow_bg);
        }

//        if (direction == 1) {
//            directionImg1.setVisibility(View.VISIBLE);
//        } else if (direction == 2) {
//            directionImg2.setVisibility(View.VISIBLE);
//        } else if (direction == 3) {
//            directionImg3.setVisibility(View.VISIBLE);
//        } else if (direction == 4) {
//            directionImg4.setVisibility(View.VISIBLE);
//        } else if (direction == 5) {
//            directionImg5.setVisibility(View.VISIBLE);
//        } else if (direction == 6) {
//            directionImg6.setVisibility(View.VISIBLE);
//        } else if (direction == 7) {
//            directionImg7.setVisibility(View.VISIBLE);
//        } else if (direction == 8) {
//            directionImg8.setVisibility(View.VISIBLE);
//        }
    }

    public void goalDialogShow(final View view) {
        TextView valueText;
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
        } else if (view.getId() == R.id.score13_layout) {
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
        } else if (view.getId() == R.id.score19_layout) {
            layoutValue = 19;
            valueText = (TextView) view.findViewById(R.id.score19_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        } else if (view.getId() == R.id.score20_layout) {
            layoutValue = 20;
            valueText = (TextView) view.findViewById(R.id.score20_value);
            if("".equals(valueText.getText().toString())) {
                return;
            }
        }


        if(!mPopupBoolean) {
            mPopupBoolean = !mPopupBoolean;
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.content_archery_goal, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("Goal Setting");
            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.show();
            alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.getWindow().setLayout(1245, 623);
            alertDialog.getWindow().setLayout(400, 500);
            alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            });

            TextView cancelText = (TextView) dialogView.findViewById(R.id.cancel_text);
            TextView confirmText = (TextView) dialogView.findViewById(R.id.confirm_text);

            cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupBoolean = false;
                    alertDialog.dismiss();
                }
            });

            final int finalLayoutValue = layoutValue;
            confirmText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupBoolean = false;
//                direction = 0;
                    goal = 0;
                    //20170919 score--;

                /*if (upLeftCheckbox.isChecked()) {
                    direction = 1;
                } else if (upCheckbox.isChecked()) {
                    direction = 2;
                } else if (upRightCheckbox.isChecked()) {
                    direction = 3;
                } else if (leftCheckbox.isChecked()) {
                    direction = 4;
                } else if (rightCheckbox.isChecked()) {
                    direction = 5;
                } else if (downLeftCheckbox.isChecked()) {
                    direction = 6;
                } else if (downCheckbox.isChecked()) {
                    direction = 7;
                } else if (downRightCheckbox.isChecked()) {
                    direction = 8;
                }*/


                    if (goalZeroCheckbox.isChecked()) {
                        goal = 0;
                    } else if (goalOneCheckbox.isChecked()) {
                        goal = 1;
                    } else if (goalTwoCheckbox.isChecked()) {
                        goal = 2;
                    } else if (goalThreeCheckbox.isChecked()) {
                        goal = 3;
                    } else if (goalFourCheckbox.isChecked()) {
                        goal = 4;
                    } else if (goalFiveCheckbox.isChecked()) {
                        goal = 5;
                    } else if (goalSixCheckbox.isChecked()) {
                        goal = 6;
                    } else if (goalSevenCheckbox.isChecked()) {
                        goal = 7;
                    } else if (goalEightCheckbox.isChecked()) {
                        goal = 8;
                    } else if (goalNineCheckbox.isChecked()) {
                        goal = 9;
                    } else if (goalTenCheckbox.isChecked()) {
                        goal = 10;
                    } else if (goalFullScaleCheckbox.isChecked()) {
                        goal = 11;
                    }

                    if (mFlag == 4) {
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
                        } else if (view.getId() == R.id.score13_layout) {
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
                        } else if (view.getId() == R.id.score19_layout) {
                            goalDialogShow19(goalView, finalLayoutValue);
                        } else if (view.getId() == R.id.score20_layout) {
                            goalDialogShow20(goalView, finalLayoutValue);
                        }
                    } else {
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
                        } else if (view.getId() == R.id.score13_layout) {
                            goalDialogShow13(goalView, finalLayoutValue);
                        } else if (view.getId() == R.id.score14_layout) {
                            goalDialogShow14(goalView, finalLayoutValue);
                        } else if (view.getId() == R.id.score15_layout) {
                            goalDialogShow15(goalView, finalLayoutValue);
                        }
                    }

                    int total = 0;
                    for (int i = 0; i < round_score.size(); i++) {
                        if (round_score.get(i) == 11) {
                            total += 10;
                        } else {
                            total += round_score.get(i);
                        }
                    }

                    mArrowText.setText(round_score.size() + "");
                    mTotalText.setText(total + "");

                    alertDialog.dismiss();
                }
            });

//        upLeftCheckbox = (CheckBox) dialogView.findViewById(R.id.up_left_checkbox);
//        upCheckbox = (CheckBox) dialogView.findViewById(R.id.up_checkbox);
//        upRightCheckbox = (CheckBox) dialogView.findViewById(R.id.up_right_checkbox);
//        leftCheckbox = (CheckBox) dialogView.findViewById(R.id.left_checkbox);
//        rightCheckbox = (CheckBox) dialogView.findViewById(R.id.right_checkbox);
//        downLeftCheckbox = (CheckBox) dialogView.findViewById(R.id.down_left_checkbox);
//        downRightCheckbox = (CheckBox) dialogView.findViewById(R.id.down_right_checkbox);
//        downCheckbox = (CheckBox) dialogView.findViewById(R.id.down_checkbox);

            goalZeroCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_zero);
            goalOneCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_one);
            goalTwoCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_two);
            goalThreeCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_three);
            goalFourCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_four);
            goalFiveCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_five);
            goalSixCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_six);
            goalSevenCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_seven);
            goalEightCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_eight);
            goalNineCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_nine);
            goalTenCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_ten);
            goalFullScaleCheckbox = (CheckBox) dialogView.findViewById(R.id.archery_goal_full_scale);

//        upLeftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//        upCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//        upRightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//        leftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//        rightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//        downLeftCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//        downRightCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);
//        downCheckbox.setOnCheckedChangeListener(directionCheckedChangeListener);

            goalZeroCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalOneCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalTwoCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalThreeCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalFourCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalFiveCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalSixCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalSevenCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalEightCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalNineCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalTenCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
            goalFullScaleCheckbox.setOnCheckedChangeListener(goalCheckedChangeListener);
        }
    }

    public void goalButtonPush(View view) {
        int socreFlag = 0;
        Log.d("ArcheryBoardActivity" , "mFlag : " + mFlag + "/ mMember : " + mMember + "/ score : " + score + "/ getCount : " + mAdapter.getCount());
        if(mFlag == 4 || (mMember == 2 && mFlag == 3)) {
            socreFlag = 20;
        }else {
            socreFlag = 15;
        }

        if((mMember == 2 && mFlag == 3 && mAdapter.getCount()%2 == 0 && score ==16) || (mMember == 2 && mFlag == 1 && score == 12)) {
            mNextLayout.setVisibility(View.VISIBLE);
            finishBoardActivity(0);
        }else if(score == socreFlag){
//            mNextLayout.setVisibility(View.VISIBLE);

            if(mMember == 1) {
                finishBoardActivity(0);
            }else {
                if(mFlag == 1 || (mFlag == 2 && mAdapter.getCount()%2 == 0)){
                    finishBoardActivity(0);
                }else {
                    score = 0;

                    LayoutInflater scoreInflater = getLayoutInflater();
                    if (mFlag == 3) {
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
                        goalView.findViewById(R.id.score13_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score14_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score15_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score16_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score17_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score18_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score19_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score20_layout).setOnClickListener(null);

                        goalView = scoreInflater.inflate(R.layout.content_score_board_two, null);
                    } else {
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
                        goalView.findViewById(R.id.score13_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score14_layout).setOnClickListener(null);
                        goalView.findViewById(R.id.score15_layout).setOnClickListener(null);

                        goalView = scoreInflater.inflate(R.layout.content_score_board, null);
                    }
                    mAdapter.addView(goalView);
                    mAdapter.notifyDataSetChanged();

                    TextView roundText = (TextView) goalView.findViewById(R.id.score_round_text);
                    roundText.setText((round + 1) + "\nR");

                    mViewPager.setCurrentItem(mAdapter.getCount() - 1);
                }
            }
            /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate (R.layout.content_archery_finish_popup, null);
            alertDialogBuilder.setView(dialogView);
            AlertDialog alertDialog = alertDialogBuilder.show();
            alertDialog.getWindow().setLayout(1190, 595);
            alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            });


            TextView nowRoundText = (TextView) dialogView.findViewById(R.id.now_round_text);
            TextView nextPleaseRoundText = (TextView) dialogView.findViewById(R.id.next_round_please_text);
            TextView nextRoundText = (TextView) dialogView.findViewById(R.id.next_round_text);
            TextView finishRoundText = (TextView) dialogView.findViewById(R.id.finish_round_text);

            nowRoundText.setText((round+1)+"ROUND 를 완료하였습니다.");
            nextPleaseRoundText.setText("추가결제 후 "+(round+2)+" ROUND 를 진행하시겠습니까?");
            nextRoundText.setText((round+2)+"R 계속하기");

            nextRoundText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNextLayout.setVisibility(View.INVISIBLE);
                    alertDialog.dismiss();

                    score = 0;
                    ++round;

                    LayoutInflater scoreInflater = getLayoutInflater();
                    if(mFlag == 4) {
                        goalView = scoreInflater.inflate (R.layout.content_score_board_two, null);
                    }else {
                        goalView = scoreInflater.inflate (R.layout.content_score_board, null);
                    }
                    mAdapter.addView (goalView);
                    mAdapter.notifyDataSetChanged();

                    TextView roundText = (TextView) goalView.findViewById(R.id.score_round_text);
                    roundText.setText((round+1) + "\nR");

                    mViewPager.setCurrentItem(mAdapter.getCount()-1);
                }
            });

            finishRoundText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();

//                            ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
//                            Bitmap bitmap = fragment.screenShot();
//                            ImageView camerImg = (ImageView) resultDialogView.findViewById(R.id.camera_img);
//                            camerImg.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));

                    AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = getLayoutInflater();
                    View resultDialogView = inflater.inflate (R.layout.content_archery_finish_result_popup, null);
                    resultDialogBuilder.setView(resultDialogView);
                    AlertDialog resultDialog = resultDialogBuilder.show();
                    resultDialog.getWindow().setLayout(1572, 786);
                    resultDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());


                    TextView noText = (TextView) resultDialogView.findViewById(R.id.no_text);
                    noText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resultDialog.dismiss();

                            Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", mPhone);
                            bundle.putString("name", mName);
                            bundle.putInt("member", mMember);
                            bundle.putInt("menu", mMenu);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });

                    TextView yesText = (TextView) resultDialogView.findViewById(R.id.yes_text);
                    yesText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resultDialog.dismiss();

                            AlertDialog.Builder nfcDialogBuilder = new AlertDialog.Builder(mContext);
                            View nfcView = new View(mContext);
                            nfcView.setBackgroundResource(R.mipmap.nfc_popup);
                            nfcDialogBuilder.setView(nfcView);
                            AlertDialog nfcDialog = nfcDialogBuilder.show();
                            nfcDialog.getWindow().setLayout(1572, 786);
                            nfcDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());

                            nfcView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phone", mPhone);
                                    bundle.putString("name", mName);
                                    bundle.putInt("member", mMember);
                                    bundle.putInt("menu", mMenu);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    });
                }
            });*/



        }else {

//            direction = 0;
            goal = 0;

            if (view.getId() == R.id.score0_button) {
                mSound.play(mSoundId_0, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 0;
            } else if (view.getId() == R.id.score1_button) {
                mSound.play(mSoundId_1, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 1;
            } else if (view.getId() == R.id.score2_button) {
                mSound.play(mSoundId_2, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 2;
            } else if (view.getId() == R.id.score3_button) {
                mSound.play(mSoundId_3, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 3;
            } else if (view.getId() == R.id.score4_button) {
                mSound.play(mSoundId_4, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 4;
            } else if (view.getId() == R.id.score5_button) {
                mSound.play(mSoundId_5, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 5;
            } else if (view.getId() == R.id.score6_button) {
                mSound.play(mSoundId_6, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 6;
            } else if (view.getId() == R.id.score7_button) {
                mSound.play(mSoundId_7, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 7;
            } else if (view.getId() == R.id.score8_button) {
                mSound.play(mSoundId_8, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 8;
            } else if (view.getId() == R.id.score9_button) {
                mSound.play(mSoundId_9, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 9;
            } else if (view.getId() == R.id.score10_button) {
                mSound.play(mSoundId_10, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 10;
            } else if (view.getId() == R.id.score11_button) {
                mSound.play(mSoundId_10x, 1.0F, 1.0F,  0,  0,  1.0F);
                goal = 11;
            }

            if ((mFlag == 4) || (mMember==2 && mFlag == 3)) {
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
                    ScrollView scrollView = (ScrollView)goalView.findViewById(R.id.scroll_view);
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    goalDialogShow12(goalView, 0);
                } else if (score == 12) {
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
                } else if (score == 18) {
                    goalDialogShow19(goalView, 0);
                } else if (score == 19) {
                    goalDialogShow20(goalView, 0);
                    mNextLayout.setVisibility(View.VISIBLE);
                }
            }else {
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
                } else if (score == 12) {
                    goalDialogShow13(goalView, 0);
                } else if (score == 13) {
                    goalDialogShow14(goalView, 0);
                } else if (score == 14) {
                    goalDialogShow15(goalView, 0);
                    mNextLayout.setVisibility(View.VISIBLE);
                }
            }

            score++;

            int total = 0;
            for (int i = 0; i < round_score.size(); i++) {
                if (round_score.get(i) == 11) {
                    total += 10;
                } else {
                    total += round_score.get(i);
                }
            }

            mArrowText.setText(round_score.size() + "");
            mTotalText.setText(total + "");
        }
    }

    @Override
    @SuppressLint("NewApi")
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        mSound.release();
        if(mTask != null) {
            mTask.cancel();
            mTask = null;
        }

        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if(mCameraTask != null) {
            mCameraTask.cancel();
            mCameraTask = null;
        }

        if(mCameraTimer != null) {
            mCameraTimer.cancel();
            mCameraTimer = null;
        }
        super.onDestroy();
    }

    public class ScoreInsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        ArrayList<Object> mArray = new ArrayList<Object>();
        int mFlag = 0;

        ScoreInsertTask(ArrayList<Object> array, int flag) {
            mArray = array;
            mFlag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean connect = false;
            try {
                // Simulate network access.
                network = new NetworkConnection(mContext);
                connect = network.rankInsert(mArray);
            } catch (Exception e) {
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
            if(success) {
                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMember);
                bundle.putInt("menu", mMenu);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                if(mFlag == 1) {
                    bundle.putInt("rank", 3);
                }else {
                    bundle.putInt("rank", 2);
                }
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

                scoreInsertTask = null;
            }else {
                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMember);
                bundle.putInt("menu", mMenu);
                bundle.putInt("rank", 2);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            scoreInsertTask = null;
        }
    }

    private void finishBoardActivity(int finish) {

        if(!mPopupBoolean) {
            mPopupBoolean = !mPopupBoolean;
            mSound.play(mFinishTime, 1.0F, 1.0F,  1,  0,  1.0F);

            if(mState) {

                if(mCameraTask != null) {
                    mCameraTask.cancel();
                    mCameraTask = null;
                }

                if(mCameraTimer != null) {
                    mCameraTimer.cancel();
                    mCameraTimer = null;
                }
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.content_archery_finish_popup, null);
            alertDialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = alertDialogBuilder.show();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setLayout(1190, 595);
            alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            });


            TextView nowRoundText = (TextView) dialogView.findViewById(R.id.now_round_text);
            TextView nextPleaseRoundText = (TextView) dialogView.findViewById(R.id.next_round_please_text);
            TextView nextRoundText = (TextView) dialogView.findViewById(R.id.next_round_text);
            TextView finishRoundText = (TextView) dialogView.findViewById(R.id.finish_round_text);


            if (finish == 1) {
                nowRoundText.setText("시간이 종료되어");
                nextPleaseRoundText.setText("체험을 완료합니다.");

                nextRoundText.setVisibility(View.GONE);
                finishRoundText.setText("확인");
            } else {
                if (mMember == 1) {
                    if (mFlag == 1) {
                        nowRoundText.setText("15발이 완료되었습니다.");
                        nextPleaseRoundText.setText("계속진행하시려면 추가결제를 해주세요.");
                        mSound.play(mFinish15Arrow, 1.0F, 1.0F, 0, 0, 1.0F);
                    } else if (mFlag == 2) {
                        nowRoundText.setText((round + 1) + "ROUND 를 완료하였습니다.");
                        nextPleaseRoundText.setText("추가결제 후 " + (round + 2) + " 를 진행하시겠습니까?");
                        nextRoundText.setText((round + 2) + "R 계속하기");
                    } else if (mFlag == 3) {
                        nowRoundText.setText((round + 1) + "ROUND 를 완료하였습니다.");
                        nextPleaseRoundText.setText("추가결제 후 " + (round + 2) + " 를 진행하시겠습니까?");
                        nextRoundText.setText((round + 2) + "R 계속하기");
                    } else if (mFlag == 4) {
                        nowRoundText.setText("20발이 완료되었습니다.");
                        nextPleaseRoundText.setText("계속진행하시려면 추가결제를 해주세요.");
                        mSound.play(mFinish20Arrow, 1.0F, 1.0F, 0, 0, 1.0F);
                    } else if (mFlag == 5) {
                        nowRoundText.setText((round + 1) + "ROUND 를 완료하였습니다.");
                        nextPleaseRoundText.setText("추가결제 후 " + (round + 2) + " 를 진행하시겠습니까?");
                        nextRoundText.setText((round + 2) + "R 계속하기");
                    } else if (mFlag == 6) {
                        nowRoundText.setText((round + 1) + "ROUND 를 완료하였습니다.");
                        nextPleaseRoundText.setText("추가결제 후 " + (round + 2) + " 를 진행하시겠습니까?");
                        nextRoundText.setText((round + 2) + "R 계속하기");
                    }
                } else {
                    if (mFlag == 1) {
                        nowRoundText.setText("12발이 완료되었습니다.");
                    } else if (mFlag == 2) {
                        nowRoundText.setText("30발이 완료되었습니다.");
                    } else if (mFlag == 3) {
                        nowRoundText.setText("36발이 완료되었습니다.");
                    } else {
                        nowRoundText.setText("연습라운드를 완료하였습니다.");
                    }
                    nextPleaseRoundText.setText("계속 연습하시겠습니까?");
                    nextRoundText.setText("계속하기");
                    finishRoundText.setText("끝내기");
                }
            }

            nextRoundText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupBoolean = false;

                    mNextLayout.setVisibility(View.INVISIBLE);
                    alertDialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.content_step_next_popup, null);
                    builder.setView(dialogView);
                    final AlertDialog aDialog = builder.show();
                    aDialog.setCanceledOnTouchOutside(false);
                    aDialog.getWindow().setLayout(1190, 595);
                    aDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());

                    aDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            aDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                        }
                    });

                    TextView nextRoundText = (TextView) dialogView.findViewById(R.id.next_round_text);
                    TextView finishRoundText = (TextView) dialogView.findViewById(R.id.finish_round_text);

                    final EditText confirmEdit = (EditText) dialogView.findViewById(R.id.third_finish_text);


                    nextRoundText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ("0388".equals(confirmEdit.getText().toString())) {

                                mPopupBoolean = false;
                                aDialog.dismiss();

                                score = 0;
                                ++round;

                                LayoutInflater scoreInflater = getLayoutInflater();
                                if ((mFlag == 4) || (mMember == 2 && mFlag == 3)) {
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
                                    goalView.findViewById(R.id.score13_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score14_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score15_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score16_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score17_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score18_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score19_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score20_layout).setOnClickListener(null);

                                    goalView = scoreInflater.inflate(R.layout.content_score_board_two, null);
                                } else {
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
                                    goalView.findViewById(R.id.score13_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score14_layout).setOnClickListener(null);
                                    goalView.findViewById(R.id.score15_layout).setOnClickListener(null);

                                    goalView = scoreInflater.inflate(R.layout.content_score_board, null);
                                }
                                mAdapter.addView(goalView);
                                mAdapter.notifyDataSetChanged();

                                TextView roundText = (TextView) goalView.findViewById(R.id.score_round_text);
                                roundText.setText((round + 1) + "\nR");

                                if(mMember == 1) {
                                    if (mFlag == 1) {
                                        mBoardSettingText.setText("15발 +" + (round));
                                    } else if (mFlag == 2) {
                                        mBoardSettingText.setText("30분 +" + (round));
                                    } else if (mFlag == 3) {
                                        mBoardSettingText.setText("1시간 +" + (round));
                                    } else if (mFlag == 4) {
                                        mBoardSettingText.setText("20발 +" + (round));
                                    } else if (mFlag == 5) {
                                        mBoardSettingText.setText("30분 +" + (round));
                                    }
                                }

                                mViewPager.setCurrentItem(mAdapter.getCount() - 1);


                                if(mState) {
                                    mCameraTask = mTimerTaskMaker();

                                    mCameraTimer = new Timer();
                                    mCameraTimer.schedule(mCameraTask, 2, 100);
                                }
                            }else {
                                Toast.makeText(mContext, "관리자 패스워드가 틀렸습니다.", Toast.LENGTH_LONG).show();
                            }


                        }
                    });


                    finishRoundText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupBoolean = false;
                            aDialog.dismiss();
                        }
                    });
                }
            });

            finishRoundText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupBoolean = false;

                    alertDialog.dismiss();

//                            ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
//                            Bitmap bitmap = fragment.screenShot();
//                            ImageView camerImg = (ImageView) resultDialogView.findViewById(R.id.camera_img);
//                            camerImg.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));

                    if(!mPopupBoolean) {
                        mPopupBoolean = !mPopupBoolean;

                        AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(mContext);
                        LayoutInflater inflater = getLayoutInflater();
                        View resultDialogView = inflater.inflate(R.layout.content_archery_finish_result_popup, null);
                        TextView nameText = (TextView) resultDialogView.findViewById(R.id.popup_name_text);
                        TextView dateText = (TextView) resultDialogView.findViewById(R.id.popup_date_text);
                        TextView arrowText = (TextView) resultDialogView.findViewById(R.id.popup_arrow_text);
                        TextView totalText = (TextView) resultDialogView.findViewById(R.id.popup_total_text);

                        nameText.setText(mNameText.getText().toString());
                        Date from = new Date();
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = transFormat.format(from);
                        dateText.setText(date);
                        arrowText.setText(mArrowText.getText().toString());
                        totalText.setText(mTotalText.getText().toString());


                        resultDialogBuilder.setView(resultDialogView);
                        final AlertDialog resultDialog = resultDialogBuilder.show();
                        resultDialog.setCanceledOnTouchOutside(false);
                        resultDialog.getWindow().setLayout(1572, 786);
                        resultDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());


                        View downloadView = (View) resultDialogView.findViewById(R.id.popup_download_layout);
                        Layout_to_Image layout_to_image, camer_to_image;
                        Bitmap bitmap, cameraBitmap;

                        ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                        cameraBitmap = (Bitmap) fragment.screenShot();

//                camer_to_image = new Layout_to_Image(mContext, fragment.getView());
//                cameraBitmap = camer_to_image.convert_layout();

                        ImageView popupCameraView = (ImageView) resultDialogView.findViewById(R.id.popup_camera_img);
                        popupCameraView.setImageBitmap(cameraBitmap);

                        layout_to_image = new Layout_to_Image(mContext, downloadView);
                        bitmap = layout_to_image.convert_layout();

                        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                        // Get Absolute Path in External Sdcard
                        String foler_name = "/fivics/";
                        String file_name = "fivics_popup.jpg";
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


                        TextView noText = (TextView) resultDialogView.findViewById(R.id.no_text);
                        noText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupBoolean = false;

                                resultDialog.dismiss();

                                ArrayList<Object> array = new ArrayList<Object>();
                                if (mName != null || !"".equals(mName)) {
                                    array.add(mName);
                                    array.add(mPhone);
                                } else {
                                    array.add("");
                                    array.add("");
                                }

                                array.add(mTotalText.getText().toString());
                                array.add(mArrowText.getText().toString());
                                array.add(mMember);
                                array.add("수원권선점");

                                scoreInsertTask = new ScoreInsertTask(array, 0);
                                scoreInsertTask.execute();
//                        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("phone", mPhone);
//                        bundle.putString("name", mName);
//                        bundle.putInt("member", mMember);
//                        bundle.putInt("menu", mMenu);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        finish();
                            }
                        });

                        TextView yesText = (TextView) resultDialogView.findViewById(R.id.yes_text);
                        yesText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupBoolean = false;

                                resultDialog.dismiss();

                                mEmailLayout.setVisibility(View.VISIBLE);
                                mMainLayout.setVisibility(View.GONE);

                        /*AlertDialog.Builder nfcDialogBuilder = new AlertDialog.Builder(mContext);
                        View nfcView = new View(mContext);
                        nfcView.setBackgroundResource(R.mipmap.nfc_popup);
                        nfcDialogBuilder.setView(nfcView);
                        AlertDialog nfcDialog = nfcDialogBuilder.show();
                        nfcDialog.getWindow().setLayout(1572, 786);
                        nfcDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());

                        nfcView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("phone", mPhone);
//                                bundle.putString("name", mName);
//                                bundle.putInt("member", mMember);
//                                bundle.putInt("menu", mMenu);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                                finish();


                                ArrayList<Object> array = new ArrayList<Object>();
                                if(mName != null || !"".equals(mName)) {
                                    array.add(mName);
                                    array.add(mPhone);
                                }else {
                                    array.add("");
                                    array.add("");
                                }

                                array.add(mTotalText.getText().toString());
                                array.add(mArrowText.getText().toString());
                                array.add(mMember);
                                array.add("수원권선점");

                                scoreInsertTask = new ScoreInsertTask(array);
                                scoreInsertTask.execute();
                            }
                        });*/

                            }
                        });
                    }
                }
            });
        }
    }

    public static void setVisibleEmailView() {
        mEmailLayout.setVisibility(View.GONE);
    }

    public static void setEmailSendFail() {
        Toast.makeText(mSContext, "Email을 보내는데 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
    }


    private void itemSelected() {
        mBannelInfoText.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_up_in));
        mBannelInfoText.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_up_out));
        mBannelInfoText.startFlipping();
    };




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

        Bitmap bmp;
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Mat tmp;
        tmp = img.channels()==1? new Mat (width, height, CvType.CV_8UC1, new Scalar(1)): new Mat (width, height, CvType.CV_8UC3, new Scalar(3));
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

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                        ArcheryCameraFragment fragment = null;
                        fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
                        Bitmap cameraBitmap = (Bitmap) fragment.screenShot();
//                    cameraBitmap = Bitmap.createScaledBitmap(cameraBitmap, 600, 450, true);
                        if(null != cameraBitmap) {
                            input = convertBitMap2Mat(cameraBitmap);
                        }
                        Log.d("archery", "timer : " + callCounter);

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
//                            else if ((outputTarget == 0) && (frame1flag == true)) { // Input Frame 1
                            }else if ((foundElipses[0] != 0) && (frame1flag == true)) { // Input Frame 1
                                frame1_ = input.clone();
                                frame1__ = input.clone();

                                Log.i("Gus", "frame1 saved!");

                                frame1flag = false;
                                frame2flag = true;

                                callCounter = 0;
                            }
                            else if(frame2flag == true){ // Input Frame 2
//                                if (callCounter == 30) { // // Check for movement every second
//                                if (callCounter == 15) { // // Check for movement every second
                                if (callCounter == 10) { // // Check for movement every second
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
//                                else if (callCounter >= 60) { // after 2 seconds from Motion Detection ScoreArrow
//                                else if (callCounter >= 45) { // after 2 seconds from Motion Detection ScoreArrow
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
                                        Toast.makeText(mContext, "오토 스코어링 타겟 이미지를 다시 맞춰 주세요.", Toast.LENGTH_LONG).show();
                                        mAutoScoringButton.callOnClick();
                                        return;
                                    }

                                    if(frame2flag) {
                                        mVideoProgressbarLayout.setVisibility(View.GONE);
                                    }

                                    if(isMotionDetected != 0 && callCounter == 11) {
                                        mSound.play(mWaiting, 0.6F, 0.6F, 0, 0, 1.0F);
                                    }


//                                    if ((callCounter == 30) && isMotionDetected == 0) {
//                                    if ((callCounter == 15) && isMotionDetected == 0) {
                                    if ((callCounter == 10) && isMotionDetected == 0) {
                                        if(mTestBoard) {
                                            Log.i("Gus", "runOnUiThread : " + callCounter);
                                            mAutoResultView.setVisibility(View.VISIBLE);
                                            mAutoResult2View.setVisibility(View.VISIBLE);
                                            mAutoResult3View.setVisibility(View.VISIBLE);
                                            mTouchView.setVisibility(View.VISIBLE);

                                            Bitmap outBitmap1 = null, outBitmap2 = null, outBitmap3 = null;

                                            if(null != debugImg_ && debugImg_.width() == 600) {
                                                outBitmap1 = convertMat2Bitmap(debugImg_);
                                                outBitmap1 = Bitmap.createScaledBitmap(outBitmap1, 300, 225, true);
                                            }
                                            if(null != frame1Clone && frame1Clone.width() == 600) {
                                                outBitmap2 = convertMat2Bitmap(frame1Clone);
                                                outBitmap2 = Bitmap.createScaledBitmap(outBitmap2, 300, 225, true);
                                            }
                                            if(null != frame2_ && frame2_.width() == 600) {
                                                outBitmap3 = convertMat2Bitmap(frame2_);
                                                outBitmap3 = Bitmap.createScaledBitmap(outBitmap3, 300, 225, true);
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


                                        mAutoScoreImg.setVisibility(View.GONE);
                                    }




                                    if(newScoreFlag){// new score detected
                                        newScoreFlag = false;
                                        mSignalLayout.setVisibility(View.GONE);

                                        View scoreButtonView = null;
                                        if (scoreOut == 0) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_zeropoint);
                                            scoreButtonView = (View) findViewById(R.id.score0_button);
                                        } else if (scoreOut == 1) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_onepoint);
                                            scoreButtonView = (View) findViewById(R.id.score1_button);
                                        } else if (scoreOut == 2) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_twopoint);
                                            scoreButtonView = (View) findViewById(R.id.score2_button);
                                        } else if (scoreOut == 3) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_threepoint);
                                            scoreButtonView = (View) findViewById(R.id.score3_button);
                                        } else if (scoreOut == 4) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_fourpoint);
                                            scoreButtonView = (View) findViewById(R.id.score4_button);
                                        } else if (scoreOut == 5) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_fivepoint);
                                            scoreButtonView = (View) findViewById(R.id.score5_button);
                                        } else if (scoreOut == 6) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_sixpoint);
                                            scoreButtonView = (View) findViewById(R.id.score6_button);
                                        } else if (scoreOut == 7) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_sevenpoint);
                                            scoreButtonView = (View) findViewById(R.id.score7_button);
                                        } else if (scoreOut == 8) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_eightpoint);
                                            scoreButtonView = (View) findViewById(R.id.score8_button);
                                        } else if (scoreOut == 9) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_ninepoint);
                                            scoreButtonView = (View) findViewById(R.id.score9_button);
                                        } else if (scoreOut == 10) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_tenpoint);
                                            scoreButtonView = (View) findViewById(R.id.score10_button);
                                        } else if (scoreOut == 11) {
                                            mAutoScoreImg.setBackgroundResource(R.mipmap.img_xtenpoint);
                                            scoreButtonView = (View) findViewById(R.id.score11_button);
                                        }
                                        mAutoScoreImg.setVisibility(View.VISIBLE);

                                        goalButtonPush(scoreButtonView);
                                        mCount = true;
//                                    }else if((mCount && callCounter == 30) || (mCount && callCounter == 61)){
//                                    }else if((mCount && callCounter == 15) || (mCount && callCounter == 46)){
                                    }else if((mCount && callCounter == 10) || (mCount && callCounter == 31)){
                                        Log.d("ArcheryBoardActivity", "mCount : " + mCount + ";; callCounter : " + callCounter);
//                                        mSound.play(mCountDown, 1.0F, 1.0F, 0, 0, 1.0F);
                                        mCount = false;
                                        functionCall++;
//                                    }else if(functionCall == 25){
                                    }else if(functionCall == (25+firstCall)){
                                        mAutoScoreImg.setVisibility(View.GONE);
                                        mSound.play(mCountDown, 1.0F, 1.0F, 0, 0, 1.0F);
                                        mSignalLayout.setVisibility(View.VISIBLE);
                                        mSignalImg.setImageResource(R.mipmap.img_signal1);
//                                    }else if(functionCall == 76){
//                                    }else if(functionCall == (75+firstCall)){
                                    }else if(functionCall == (70+firstCall)){
                                        functionCall = 0;
                                        firstCall = 0;

//                                        outputTarget = 0;
                                        outputTarget = 1;
                                        frame1flag = true;
                                        mSignalLayout.setVisibility(View.GONE);
                                    }

//                                    if(functionCall == 33) {
//                                    if(functionCall == (35+firstCall)) {
                                    if(functionCall == (32+firstCall)) {
//                                    if(functionCall == 38) {
                                        mSignalImg.setImageResource(R.mipmap.img_signal2);
//                                    }else if(functionCall == 42) {
//                                    }else if(functionCall == (45+firstCall)) {
                                    }else if(functionCall == (40+firstCall)) {
//                                    }else if(functionCall == 49) {
                                        mSignalImg.setImageResource(R.mipmap.img_signal3);
//                                    }else if(functionCall == 50) {
//                                    }else if(functionCall == (55+firstCall)) {
                                    }else if(functionCall == (50+firstCall)) {
//                                    }else if(functionCall == 62) {
                                        mSignalImg.setImageResource(R.mipmap.img_signal4);
//                                    }else if(functionCall == 60) {
//                                    }else if(functionCall == (65+firstCall)) {
                                    }else if(functionCall == (60+firstCall)) {
//                                    }else if(functionCall == 75) {
                                        mSignalImg.setImageResource(R.mipmap.img_signalshoot);
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
}

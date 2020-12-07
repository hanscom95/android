package artech.com.fivics.score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import artech.com.fivics.IntroActivity;
import artech.com.fivics.MainActivity;
import artech.com.fivics.R;

/**
 * Created by Michael on 10-Mar-17.
 */

public class ArcherySettingActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    ImageView mLogoImg;
    TextView mDayLayout, mWeekLayout, mSecondTitleText;
    LinearLayout mSecondLayout, mThirdLayout, mFourthLayout;
    RelativeLayout m15ArrowLayout, m20ArrowLayout, m30MinDayLayout, m30MinWeekLayout, m1HourLayout, mOutterLayout;
    TextView m15ArrowText, m20ArrowText, m30MinDayText, m30MinWeekText, m1HourText, mOutterText, m15ArrowPriceText, m20ArrowPriceText, m30MinDayPrieText, m30MinWeekPriceText, m1HourPriceText, mOutterPriceText;
    RelativeLayout mBeforeLayout, mNextLayout;

    int firstState = 0;
    int mArrowFlag = 0;
    boolean secondBoolean = false;
    String mPhone = "", mName = "";
    int mMember = 0;
    int mMenu = 0;
    int mSetting = 0;

    String mBannelNumber = "";
    String[] mBannelInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_archery_board_setting);
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

        mContext = this;

        mDayLayout = (TextView) findViewById(R.id.day_text);
        mWeekLayout = (TextView) findViewById(R.id.week_text);
        m15ArrowLayout = (RelativeLayout) findViewById(R.id.setting_15_arrow_layout);
        m20ArrowLayout = (RelativeLayout) findViewById(R.id.setting_20_arrow_layout);
        m30MinDayLayout = (RelativeLayout) findViewById(R.id.setting_30_min_day_layout);
        m30MinWeekLayout = (RelativeLayout) findViewById(R.id.setting_30_min_week_layout);
        m1HourLayout = (RelativeLayout) findViewById(R.id.setting_1_hour_layout);
        mOutterLayout = (RelativeLayout) findViewById(R.id.setting_outter_arrow_layout);

        mSecondLayout = (LinearLayout) findViewById(R.id.second_button_layout);
        mThirdLayout = (LinearLayout) findViewById(R.id.third_button_layout);
        mFourthLayout = (LinearLayout) findViewById(R.id.four_button_layout);

        mSecondTitleText = (TextView) findViewById(R.id.second_title_text);

        m15ArrowText = (TextView) findViewById(R.id.setting_15_arrow_text);
        m15ArrowPriceText = (TextView) findViewById(R.id.setting_15_price_text);
        m20ArrowText = (TextView) findViewById(R.id.setting_20_arrow_text);
        m20ArrowPriceText = (TextView) findViewById(R.id.setting_20_price_text);
        m30MinDayText = (TextView) findViewById(R.id.setting_30_min_day_text);
        m30MinDayPrieText = (TextView) findViewById(R.id.setting_30_min_day_price_text);
        m30MinWeekText = (TextView) findViewById(R.id.setting_30_min_week_text);
        m30MinWeekPriceText = (TextView) findViewById(R.id.setting_30_min_week_price_text);
        m1HourText = (TextView) findViewById(R.id.setting_1_hour_text);
        m1HourPriceText = (TextView) findViewById(R.id.setting_1_hour_price_text);
        mOutterText = (TextView) findViewById(R.id.setting_outter_text);
        mOutterPriceText = (TextView) findViewById(R.id.setting_outter_price_text);

        mDayLayout.setOnClickListener(this);
        mWeekLayout.setOnClickListener(this);
        m15ArrowLayout.setOnClickListener(this);
        m20ArrowLayout.setOnClickListener(this);
        m30MinDayLayout.setOnClickListener(this);
        m30MinWeekLayout.setOnClickListener(this);
        m1HourLayout.setOnClickListener(this);
        mOutterLayout.setOnClickListener(this);

        mBeforeLayout = (RelativeLayout) findViewById(R.id.before_layout);
        mNextLayout = (RelativeLayout) findViewById(R.id.next_layout);


        mLogoImg = (ImageView) findViewById(R.id.archery_logo);
        mLogoImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
                return false;
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
//                if(secondBoolean) {
//                    //Todo: Enter archery main screen
                    Intent intent = new Intent(getApplicationContext(), ArcheryBoardActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", mPhone);
                    bundle.putString("name", mName);
                    bundle.putInt("member", mMember);
                    bundle.putInt("menu", mMenu);
                    bundle.putInt("flag", mSetting);
                    bundle.putString("bannelNummber", mBannelNumber);
                    bundle.putStringArray("bannelInfo", mBannelInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
//                }
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPhone = bundle.getString("phone");
        mName = bundle.getString("name");
        mMember = bundle.getInt("member");
        mMenu = bundle.getInt("menu");
        mBannelNumber = bundle.getString("bannelNummber");
        mBannelInfo = bundle.getStringArray("bannelInfo");

        mMenu = 4;

        if(mMember == 2) {
            TextView recordText = (TextView) mDayLayout.findViewById(R.id.day_text);
//            recordText.setText("기록");
            recordText.setText("记录");
            TextView practiceText = (TextView) mWeekLayout.findViewById(R.id.week_text);
//            practiceText.setText("연습");
            practiceText.setText("实践");
//            mSecondTitleText.setText("기록, 연습 중 선택해주세요.");
            mSecondTitleText.setText("记录，练习请选择.");

//            m15ArrowText.setText("12발");
//            m30MinDayText.setText("30발");
//            m1HourText.setText("36발");
            m15ArrowText.setText("12发");
            m30MinDayText.setText("30发");
            m1HourText.setText("36发");

            m15ArrowPriceText.setText("");
            m30MinDayPrieText.setText("");
            m1HourPriceText.setText("");

            m20ArrowLayout.setVisibility(View.INVISIBLE);
            m30MinWeekLayout.setVisibility(View.INVISIBLE);
            mOutterLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.day_text || v.getId() == R.id.week_text) {
            mNextLayout.setVisibility(View.INVISIBLE);
            if(firstState > 0) {
                mSecondLayout.setVisibility(View.INVISIBLE);
                mThirdLayout.setVisibility(View.INVISIBLE);
                mFourthLayout.setVisibility(View.INVISIBLE);
//                mSecondTitleText.setText("기록, 연습 중 선택해주세요.");
                mSecondTitleText.setText("记录，练习请选择.");

                firstState = 0;
                secondBoolean = false;
                mDayLayout.setBackgroundResource(R.mipmap.btn_week_in);
                mDayLayout.setTextColor(getResources().getColor(R.color.archery_board_none));
                mWeekLayout.setBackgroundResource(R.mipmap.btn_week_in);
                mWeekLayout.setTextColor(getResources().getColor(R.color.archery_board_none));

                m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m1HourLayout.setBackgroundResource(R.mipmap.btn_content_off);
                mOutterLayout.setBackgroundResource(R.mipmap.btn_content_off);

                m15ArrowText.setTextColor(getResources().getColor(android.R.color.white));
                m15ArrowPriceText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinDayText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinDayPrieText.setTextColor(getResources().getColor(android.R.color.white));
                m1HourText.setTextColor(getResources().getColor(android.R.color.white));
                m1HourPriceText.setTextColor(getResources().getColor(android.R.color.white));
                m20ArrowText.setTextColor(getResources().getColor(android.R.color.white));
                m20ArrowPriceText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinWeekText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinWeekPriceText.setTextColor(getResources().getColor(android.R.color.white));
                mOutterText.setTextColor(getResources().getColor(android.R.color.white));
                mOutterPriceText.setTextColor(getResources().getColor(android.R.color.white));
            }else {

                mSecondLayout.setVisibility(View.VISIBLE);
                mThirdLayout.setVisibility(View.VISIBLE);
                mFourthLayout.setVisibility(View.VISIBLE);
//                mSecondTitleText.setText("선택2");
                mSecondTitleText.setText("选择2");


                if (v.getId() == R.id.day_text) {
                    firstState = 1;
                    mDayLayout.setBackgroundResource(R.mipmap.btn_week_on);
                    mDayLayout.setTextColor(getResources().getColor(R.color.archery_board_select));
                    mWeekLayout.setBackgroundResource(R.mipmap.btn_week_off);
                    mWeekLayout.setTextColor(getResources().getColor(R.color.archery_board_text));

                    m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_in);
                    m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_in);
                    m1HourLayout.setBackgroundResource(R.mipmap.btn_content_in);
                    m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                    m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_off);
                    mOutterLayout.setBackgroundResource(R.mipmap.btn_content_off);

                    m15ArrowText.setTextColor(getResources().getColor(android.R.color.white));
                    m15ArrowPriceText.setTextColor(getResources().getColor(android.R.color.white));
                    m30MinDayText.setTextColor(getResources().getColor(android.R.color.white));
                    m30MinDayPrieText.setTextColor(getResources().getColor(android.R.color.white));
                    m1HourText.setTextColor(getResources().getColor(android.R.color.white));
                    m1HourPriceText.setTextColor(getResources().getColor(android.R.color.white));

                    m20ArrowText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m20ArrowPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m30MinWeekText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m30MinWeekPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    mOutterText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    mOutterPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
                } else if (v.getId() == R.id.week_text) {
                    if(mMember == 2) {
                        mNextLayout.setVisibility(View.VISIBLE);
                    }

                    firstState = 2;
                    mDayLayout.setBackgroundResource(R.mipmap.btn_week_off);
                    mDayLayout.setTextColor(getResources().getColor(R.color.archery_board_text));
                    mWeekLayout.setBackgroundResource(R.mipmap.btn_week_on);
                    mWeekLayout.setTextColor(getResources().getColor(R.color.archery_board_select));

                    m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                    m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_off);
                    m1HourLayout.setBackgroundResource(R.mipmap.btn_content_off);
                    m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_in);
                    m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_in);
                    mOutterLayout.setBackgroundResource(R.mipmap.btn_content_in);

                    m15ArrowText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m15ArrowPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m30MinDayText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m30MinDayPrieText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m1HourText.setTextColor(getResources().getColor(R.color.archery_board_text));
                    m1HourPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));

                    m20ArrowText.setTextColor(getResources().getColor(android.R.color.white));
                    m20ArrowPriceText.setTextColor(getResources().getColor(android.R.color.white));
                    m30MinWeekText.setTextColor(getResources().getColor(android.R.color.white));
                    m30MinWeekPriceText.setTextColor(getResources().getColor(android.R.color.white));
                    mOutterText.setTextColor(getResources().getColor(android.R.color.white));
                    mOutterPriceText.setTextColor(getResources().getColor(android.R.color.white));
                }
            }
        }else if(secondBoolean){
            mNextLayout.setVisibility(View.INVISIBLE);
            secondBoolean = false;

            if (firstState == 1) {
                m15ArrowText.setTextColor(getResources().getColor(android.R.color.white));
                m15ArrowPriceText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinDayText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinDayPrieText.setTextColor(getResources().getColor(android.R.color.white));
                m1HourText.setTextColor(getResources().getColor(android.R.color.white));
                m1HourPriceText.setTextColor(getResources().getColor(android.R.color.white));

                m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m1HourLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_off);
                mOutterLayout.setBackgroundResource(R.mipmap.btn_content_off);

                m20ArrowText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m20ArrowPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m30MinWeekText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m30MinWeekPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
                mOutterText.setTextColor(getResources().getColor(R.color.archery_board_text));
                mOutterPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
            } else if (firstState == 2) {
                m15ArrowText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m15ArrowPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m30MinDayText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m30MinDayPrieText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m1HourText.setTextColor(getResources().getColor(R.color.archery_board_text));
                m1HourPriceText.setTextColor(getResources().getColor(R.color.archery_board_text));

                m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m1HourLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_in);
                mOutterLayout.setBackgroundResource(R.mipmap.btn_content_in);

                m20ArrowText.setTextColor(getResources().getColor(android.R.color.white));
                m20ArrowPriceText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinWeekText.setTextColor(getResources().getColor(android.R.color.white));
                m30MinWeekPriceText.setTextColor(getResources().getColor(android.R.color.white));
                mOutterText.setTextColor(getResources().getColor(android.R.color.white));
                mOutterPriceText.setTextColor(getResources().getColor(android.R.color.white));
            }

        }else if(firstState > 0){
            mNextLayout.setVisibility(View.VISIBLE);
            secondBoolean = true;

            if(firstState == 1) {
                m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m1HourLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_off);
                mOutterLayout.setBackgroundResource(R.mipmap.btn_content_off);

                if(v.getId() == R.id.setting_15_arrow_layout) {
                    mSetting = 1;
                    m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_on);
                    m15ArrowText.setTextColor(getResources().getColor(R.color.archery_board_select));
                    m15ArrowPriceText.setTextColor(getResources().getColor(R.color.archery_board_select));
                }else if(v.getId() == R.id.setting_30_min_day_layout){
                    mSetting = 2;
                    m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_on);
                    m30MinDayText.setTextColor(getResources().getColor(R.color.archery_board_select));
                    m30MinDayPrieText.setTextColor(getResources().getColor(R.color.archery_board_select));
                }else if(v.getId() == R.id.setting_1_hour_layout){
                    mSetting = 3;
                    m1HourLayout.setBackgroundResource(R.mipmap.btn_content_on);
                    m1HourText.setTextColor(getResources().getColor(R.color.archery_board_select));
                    m1HourPriceText.setTextColor(getResources().getColor(R.color.archery_board_select));
                }
            }else if(firstState == 2) {
                m15ArrowLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m30MinDayLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m1HourLayout.setBackgroundResource(R.mipmap.btn_content_off);
                m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_in);
                m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_in);
                mOutterLayout.setBackgroundResource(R.mipmap.btn_content_in);

                if(v.getId() == R.id.setting_20_arrow_layout){
                    mSetting = 4;
                    m20ArrowLayout.setBackgroundResource(R.mipmap.btn_content_on);
                    m20ArrowText.setTextColor(getResources().getColor(R.color.archery_board_select));
                    m20ArrowPriceText.setTextColor(getResources().getColor(R.color.archery_board_select));
                }else if(v.getId() == R.id.setting_30_min_week_layout){
                    mSetting = 5;
                    m30MinWeekLayout.setBackgroundResource(R.mipmap.btn_content_on);
                    m30MinWeekText.setTextColor(getResources().getColor(R.color.archery_board_select));
                    m30MinWeekPriceText.setTextColor(getResources().getColor(R.color.archery_board_select));
                }else if(v.getId() == R.id.setting_outter_arrow_layout){
                    mSetting = 6;
                    mOutterLayout.setBackgroundResource(R.mipmap.btn_content_on);
                    mOutterText.setTextColor(getResources().getColor(R.color.archery_board_select));
                    mOutterPriceText.setTextColor(getResources().getColor(R.color.archery_board_select));
                }
            }
        }

        if(v.getId() == R.id.setting_20_arrow_layout) {
            mArrowFlag = 2;
        }else {
            mArrowFlag = 1;
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
}

package artech.com.fivics.guide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Locale;

import artech.com.fivics.IntroActivity;
import artech.com.fivics.MainActivity;
import artech.com.fivics.R;
import artech.com.fivics.score.ArcherySettingActivity;

public class GuideActivity extends AppCompatActivity {

    Context mContext;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    RelativeLayout mBeforeLayout, mNextLayout;
    ImageView mStepNumberImg, mNextImg, mViewNextImg, mViewBeforeImg, mLogoImg;
    ViewPager mViewPager;

    GuidePagerAdapter mAdapter;

    String mPhone = "", mName = "";
    int mMemeber = 0 ;
    int mMenu = 0 ;

    String mBannelNumber = "";
    String[] mBannelInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.content_guide);
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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPhone = bundle.getString("phone");
        mName = bundle.getString("name");
        mMemeber = bundle.getInt("member");
        mMenu = bundle.getInt("menu");
        mBannelNumber = bundle.getString("bannelNummber");
        mBannelInfo = bundle.getStringArray("bannelInfo");

        mLogoImg = (ImageView) findViewById(R.id.guide_logo);
        mLogoImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        mBeforeLayout = (RelativeLayout) findViewById(R.id.before_layout);
        mNextLayout = (RelativeLayout) findViewById(R.id.next_layout);

        mStepNumberImg = (ImageView) findViewById(R.id.step_number_img);
        mNextImg = (ImageView) findViewById(R.id.next_step_img);

        mViewNextImg = (ImageView) findViewById(R.id.next_view_img);
        mViewBeforeImg = (ImageView) findViewById(R.id.before_view_img);

        mViewNextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem() < 15) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                }else {
                    mViewPager.setCurrentItem(0);
                }
            }
        });

        mViewBeforeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem() > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                }else {
                    mViewPager.setCurrentItem(15);
                }
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.guide_viewPager) ;

        mBeforeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMemeber);
                bundle.putInt("menu", mMenu);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
//                if(mViewPager.getCurrentItem() > 0) {
//                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
//                }else {
//                    mViewPager.setCurrentItem(3);
//                }
            }
        });

        mNextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArcherySettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMemeber);
                bundle.putInt("menu", mMenu);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
//                if(mViewPager.getCurrentItem() < 7) {
//                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
//                }else {
//                    mViewPager.setCurrentItem(0);
//                }
            }
        });

        mNextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArcherySettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMemeber);
                bundle.putInt("menu", mMenu);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        mAdapter = new GuidePagerAdapter(getSupportFragmentManager(), 16);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNextLayout.setVisibility(View.INVISIBLE);
                mViewNextImg.setVisibility(View.VISIBLE);
                mViewBeforeImg.setVisibility(View.VISIBLE);
//                mNextImg.setVisibility(View.INVISIBLE);
                if((position == 0) || (position == 1) || (position == 2) || (position == 3) || (position == 4)) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number1);
                    mViewBeforeImg.setVisibility(View.INVISIBLE);
                }else if((position == 5) || (position == 6) || (position == 7)) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number2);
                }else if(position == 8) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number3);
                }else if((position == 9) || (position == 10)) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number4);
                }else if(position == 11) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number5);
                }else if(position == 12) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number6);
                }else if((position == 13) || (position == 14)) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number7);
                }else if(position == 15) {
                    mStepNumberImg.setImageResource(R.mipmap.guide_number8);
                    mViewNextImg.setVisibility(View.GONE);
                    mNextLayout.setVisibility(View.VISIBLE);
//                    mNextImg.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(mAdapter);

        if(mMemeber == 1) {
            mNextImg.setVisibility(View.INVISIBLE);;
        }else {
            mNextImg.setVisibility(View.VISIBLE);
        }

        if(mMenu == 2) {
            mMenu = 3;
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

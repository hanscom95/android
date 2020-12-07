package artech.com.fivics.prohibition;

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
import artech.com.fivics.guide.GuideActivity;

public class ProhibitionActivity extends AppCompatActivity {

    Context mContext;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    RelativeLayout mBeforeLayout, mNextLayout;
    ImageView mStepNumberImg, mNextImg, mViewNextImg, mViewBeforeImg, mLogoImg;
    ViewPager mViewPager;

    ProhibitionPagerAdapter mAdapter;

    String mPhone = "", mName = "";
    int mMemeber = 0;
    int mMenu = 0;

    String mBannelNumber = "";
    String[] mBannelInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.content_prohibition);
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


        mLogoImg = (ImageView) findViewById(R.id.prohibition_logo);
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
                if(mViewPager.getCurrentItem() < 3) {
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
                    mViewPager.setCurrentItem(3);
                }
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.prohibition_viewPager) ;

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
                Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
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
//                Log.d("ProhibitionAcitivity", "position : " + mAdapter.getPosition() + ";; current : " + mViewPager.getCurrentItem());
//                if(mViewPager.getCurrentItem() < 3) {
//                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
//                }else {
//                    mViewPager.setCurrentItem(0);
//                }
            }
        });

        mNextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
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

        mAdapter = new ProhibitionPagerAdapter(getSupportFragmentManager(), 4);
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
                if(position == 0) {
                    mStepNumberImg.setImageResource(R.mipmap.number_first);
                    mViewBeforeImg.setVisibility(View.INVISIBLE);
                }else if(position == 1) {
                    mStepNumberImg.setImageResource(R.mipmap.number_second);
                }else if(position == 2) {
                    mStepNumberImg.setImageResource(R.mipmap.number_third);
                }else if(position == 3) {
                    mStepNumberImg.setImageResource(R.mipmap.number_fourth);
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
            mNextImg.setVisibility(View.INVISIBLE);
        }else {
            mNextImg.setVisibility(View.VISIBLE);
        }

        if(mMenu == 1) {
            mMenu = 2;
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

package artech.com.semi.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import artech.com.semi.R;
import artech.com.semi.utility.ViewPagerAdapter;

public class UsingActivity extends AppCompatActivity {

    Context mContext;

    ViewPager mViewPager;

    ImageView mFirstPagerImg, mSecondPagerImg, mThirdPagerImg, mFourthPagerImg, mFifthPagerImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_using);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mViewPager = findViewById(R.id.view_flipper);

        mFirstPagerImg = findViewById(R.id.first_pager_img);
        mSecondPagerImg = findViewById(R.id.second_pager_img);
        mThirdPagerImg = findViewById(R.id.third_pager_img);
        mFourthPagerImg = findViewById(R.id.fourth_pager_img);
        mFifthPagerImg = findViewById(R.id.fifth_pager_img);


        Bitmap[] bitmaps = new Bitmap[5];
        bitmaps[0] = ((BitmapDrawable) getDrawable(R.mipmap.using_img_1)).getBitmap();
        bitmaps[1] = ((BitmapDrawable) getDrawable(R.mipmap.using_img_2)).getBitmap();
        bitmaps[2] = ((BitmapDrawable) getDrawable(R.mipmap.using_img_3)).getBitmap();
        bitmaps[3] = ((BitmapDrawable) getDrawable(R.mipmap.using_img_4)).getBitmap();
        bitmaps[4] = ((BitmapDrawable) getDrawable(R.mipmap.using_img_5)).getBitmap();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, bitmaps);


        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mFirstPagerImg.setImageResource(R.mipmap.img_dotpage_b_nonpress);
                mSecondPagerImg.setImageResource(R.mipmap.img_dotpage_b_nonpress);
                mThirdPagerImg.setImageResource(R.mipmap.img_dotpage_b_nonpress);
                mFourthPagerImg.setImageResource(R.mipmap.img_dotpage_b_nonpress);
                mFifthPagerImg.setImageResource(R.mipmap.img_dotpage_b_nonpress);

                if(position == 0) {
                    mFirstPagerImg.setImageResource(R.mipmap.img_dotpage_g_press);
                }else if(position == 1) {
                    mSecondPagerImg.setImageResource(R.mipmap.img_dotpage_g_press);
                }else if(position == 2) {
                    mThirdPagerImg.setImageResource(R.mipmap.img_dotpage_g_press);
                }else if(position == 3) {
                    mFourthPagerImg.setImageResource(R.mipmap.img_dotpage_g_press);
                }else if(position == 4) {
                    mFifthPagerImg.setImageResource(R.mipmap.img_dotpage_g_press);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

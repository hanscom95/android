package artech.com.semi.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

import artech.com.semi.R;

public class TestBannerActivity extends AppCompatActivity {

    Context mContext;

    ViewPager mViewPager;
    ViewFlipper mViewFlipper;

    SelectTask mSelectTask;
    JSONArray mJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext = this;

        setContentView(R.layout.activity_test_banner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
//        flipperImages(R.mipmap.img_appbanner);
//        flipperImages(R.mipmap.img_hook);
//        flipperImages(R.mipmap.img_hourglass);
//        flipperImages(R.mipmap.img_photo_1);

        mViewPager = findViewById(R.id.viewPager);

        mSelectTask = new SelectTask();
        mSelectTask.execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if((mViewPager.getAdapter().getCount()-1) == mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(0);
            }else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            }
        }
    }


    private void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        mViewFlipper.addView(imageView);
        mViewFlipper.setFlipInterval(1000);
        mViewFlipper.setAutoStart(true);


        mViewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        mViewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }


    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;

        SelectTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.bannerSelect();
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;

            if(success) {
                mJsonArray = network.mBannerArray;
                try {

                    if(mJsonArray.length() > 0) {
                        Bitmap[] bitmaps = new Bitmap[mJsonArray.length()];

                        for(int i = 0; i < mJsonArray.length(); i++) {
                            bitmaps[i] = Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture"));
                        }

                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, bitmaps);

                        mViewPager.setAdapter(viewPagerAdapter);


                        if(mJsonArray.length() > 1) {
                            Timer timer = new Timer();
                            timer.schedule(new MyTimerTask(), 2000, 4000);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("BusinessMainActivity", "mAdminTask : " + mJsonArray.toString());
                //                setDecoded64ImageStringFromBitmap(path);
            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }
}

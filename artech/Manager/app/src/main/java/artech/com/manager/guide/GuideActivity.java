package artech.com.manager.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import artech.com.manager.MainActivity;
import artech.com.manager.R;
import artech.com.manager.admin.AdminListActivity;
import artech.com.manager.booking.BookingListActivity;
import artech.com.manager.group.GroupActivity;
import artech.com.manager.tournament.Tournament16Activity;
import artech.com.manager.tournament.Tournament8Activity;

public class GuideActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    ImageView mViewNextImg, mViewBeforeImg;
    TextView mGuideTitleText;
    Button mExitButton;
    ViewPager mViewPager;

    GuidePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mViewNextImg = (ImageView) findViewById(R.id.next_img);
        mViewBeforeImg = (ImageView) findViewById(R.id.before_img);
        mGuideTitleText = (TextView) findViewById(R.id.guide_title_text);
        mExitButton = (Button) findViewById(R.id.exit_button);

        mViewNextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem() < 15) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                }else {
//                    mViewPager.setCurrentItem(0);
                }
            }
        });

        mViewBeforeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem() > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                }else {
//                    mViewPager.setCurrentItem(11);
                }
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.guide_view) ;

        mAdapter = new GuidePagerAdapter(getSupportFragmentManager(), 16);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mExitButton.setVisibility(View.GONE);
                mViewNextImg.setVisibility(View.VISIBLE);
                mViewBeforeImg.setVisibility(View.VISIBLE);

                if(position == 0) {
                    mViewBeforeImg.setVisibility(View.INVISIBLE);
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_1_title));
                }else if(position == 1) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_2_title));
                }else if((position == 2) || (position == 3) || (position == 4)) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_3_title));
                }else if((position == 5) || (position == 6) || (position == 7)) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_4_title));
                }else if(position == 8) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_5_title));
                }else if((position == 9) || (position == 10)) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_6_title));
                }else if(position == 11) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_7_title));
                }else if(position == 12) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_8_title));
                }else if((position == 13) || (position == 14)) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_9_title));
                }else if(position == 15) {
                    mGuideTitleText.setText(getResources().getText(R.string.guide_step2_10_title));
                    mViewNextImg.setVisibility(View.INVISIBLE);
                    mExitButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_main) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_guide) {
        } else if (id == R.id.nav_ranking) {
            Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_8tournament) {
            Intent intent = new Intent(getApplicationContext(), Tournament8Activity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("nameArray", new ArrayList<String>());
            bundle.putString("name", "");
            bundle.putString("email", "");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_16tournament) {
            Intent intent = new Intent(getApplicationContext(), Tournament16Activity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("nameArray", new ArrayList<String>());
            bundle.putString("name", "");
            bundle.putString("email", "");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplicationContext(), BookingListActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_admin) {
            Intent intent = new Intent(getApplicationContext(), AdminListActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package artech.com.semi.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import artech.com.semi.R;
import artech.com.semi.business.fragment.SaleManagementCancelFragment;
import artech.com.semi.business.fragment.SaleManagementFragment;
import artech.com.semi.business.fragment.SaleManagementNewFragment;
import artech.com.semi.business.fragment.SaleManagementSuccessFragment;
import artech.com.semi.utility.BottomNavigationViewHelper;

public class SaleManagementActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Context mContext;
    TabLayout mTabLayout;


    RelativeLayout mBottomLayout;
    TextView mEditText, mAllCheckText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_sale_management);

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

        BottomNavigationView navigation = findViewById(R.id.navigation);
//        navigation.setSelectedItemId(R.id.navigation_sale);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        mEditText = findViewById(R.id.edit_text);
        mAllCheckText = findViewById(R.id.all_checked_text);

        mTabLayout = findViewById(R.id.tab_layout);

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaleManagementSuccessFragment fragment = (SaleManagementSuccessFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
                fragment.setCheckboxVisible();
                fragment.setBottomVisibility(true);
            }
        });

        mAllCheckText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaleManagementSuccessFragment fragment = (SaleManagementSuccessFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
                fragment.setAllChecked();
            }
        });


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 2) {
                    mEditText.setVisibility(View.VISIBLE);
                }else {
                    mEditText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(mContext, BusinessMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_product_insert:
                    intent = new Intent(mContext, ProductInsertActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_product_manage:
                    intent = new Intent(mContext, ProductManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
//                case R.id.navigation_sale:
//                    intent = new Intent(mContext, SaleManagementActivity.class);
//                    startActivity(intent);
//                    finish();
//                    return true;
                case R.id.navigation_question:
                    intent = new Intent(mContext, QuestionManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return SaleManagementNewFragment.newInstance(mContext, position + 1);
            }else if(position == 1) {
                return SaleManagementFragment.newInstance(mContext, position + 1);
            }else if(position == 2) {
                return SaleManagementSuccessFragment.newInstance(mContext, position + 1);
            }else if(position == 3) {
                return SaleManagementCancelFragment.newInstance(mContext, position + 1);
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}

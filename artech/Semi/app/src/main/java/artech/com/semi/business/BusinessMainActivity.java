package artech.com.semi.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import artech.com.semi.R;
import artech.com.semi.normal.EventActivity;
import artech.com.semi.normal.NormalMainActivity;
import artech.com.semi.normal.NoticeActivity;
import artech.com.semi.normal.PrivacyActivity;
import artech.com.semi.normal.SettingActivity;
import artech.com.semi.normal.TermsElectronicActivity;
import artech.com.semi.normal.TermsLocationActivity;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

import static android.os.Build.VERSION_CODES.M;

public class BusinessMainActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    TextView mNameText, mUnauthorizedText;
    ImageView mMenuImg, mThumbnailImg, mFirstRefreshImg, mSecondRefreshImg, mThirdRefreshImg;

    RelativeLayout mMyInfoLayout, mProductManageLayout, mSaleManageLayout, mNoticeManageLayout, mProductInsertLayout, mSaleInfoLayout, mFriendShareLayout, mUnauthorizedLayout, mProductBoxLayout, mCustomerBoxLayout, mNewOrderBoxLayout, mNoticeLayout, mEventLayout, mAdminLayout, mSettingLayout, mProductQuestionLayout, mCustomerQuestionLayout, mTopScrollLayout, mProductLayout, mReviewLayout, mUsingLayout;
    TextView mTermsUseText, mPrivacyText, mTermsElectronicText, mTermsLocationText, mWaitingValueText, mNewOrderValueText, mCancelValueText, mSaleValueText, mSuccessValueText, mCalculateDateText, mCalculateValueText, mReservesValueText, mProductQuestionValueText, mCustomerValueText, mNewOrderNavText, mNavProductQuestionValueText, mNavCustomerValueText;
    Button mMainButton, mProductInsertButton;

    SelectTask mSelectTask;
    JSONObject mJsonObject;

    String mDate = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    intent = new Intent(mContext, MapsActivity.class);
//                    intent = new Intent(mContext, ImportActivity.class);
//                    intent = new Intent(mContext, TestBannerActivity.class);
//                    intent = new Intent(mContext, AddressSearchActivity.class);
//                    intent = new Intent(mContext, NormalMainActivity.class);
//                    intent = new Intent(mContext, SignupBusinessSecondActivity.class);
//                    intent = new Intent(mContext, PaymentActivity.class);
//                    startActivity(intent);
                    return true;
                case R.id.navigation_product_insert:
                    try {
                        if(mJsonObject.getInt("auth_enabled") == 1) {
                            intent = new Intent(mContext, ProductInsertActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }else {
                            Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    finish();
                    return true;
                case R.id.navigation_product_manage:
                    try {
                        if(mJsonObject.getInt("auth_enabled") == 1) {
                            intent = new Intent(mContext, ProductManagementActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }else {
                            Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    finish();
                    return true;
//                case R.id.navigation_sale:
//                    try {
//                        if(mJsonObject.getInt("auth_enabled") == 1) {
//                            intent = new Intent(mContext, SaleManagementActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        }else {
//                            Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    finish();
//                    return true;
                case R.id.navigation_question:
                    try {
                        if(mJsonObject.getInt("auth_enabled") == 1) {
                            intent = new Intent(mContext, QuestionManagementActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }else {
                            Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);


        setContentView(R.layout.activity_business_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.line_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageView closeImg = navigationView.findViewById(R.id.nav_close_img);
        mThumbnailImg = navigationView.findViewById(R.id.nav_img_thumbnail);
        mNameText = navigationView.findViewById(R.id.nav_name_text);
        mUnauthorizedText = navigationView.findViewById(R.id.nav_unauthorized_text);

        mMyInfoLayout = navigationView.findViewById(R.id.nav_info_layout);
        mProductManageLayout = navigationView.findViewById(R.id.nav_product_management_box_detail_layout);
        mSaleManageLayout = navigationView.findViewById(R.id.nav_sale_management_box_detail_layout);
        mNoticeManageLayout = navigationView.findViewById(R.id.nav_question_management_box_detail_layout);
        mProductInsertLayout = navigationView.findViewById(R.id.nav_product_insert_box_detail_layout);
        mSaleInfoLayout = navigationView.findViewById(R.id.nav_seller_info_box_detail_layout);
        mFriendShareLayout = navigationView.findViewById(R.id.nav_share_friend_box_detail_layout);
        mUnauthorizedLayout = navigationView.findViewById(R.id.nav_unauthorized);
        mProductBoxLayout = navigationView.findViewById(R.id.nav_product_box_layout);
        mCustomerBoxLayout = navigationView.findViewById(R.id.nav_customer_box_layout);
        mNewOrderBoxLayout = navigationView.findViewById(R.id.nav_new_order_box_layout);
        mNoticeLayout = navigationView.findViewById(R.id.nav_notice_box_detail_layout);
        mEventLayout = navigationView.findViewById(R.id.nav_event_box_detail_layout);
        mAdminLayout = navigationView.findViewById(R.id.nav_customer_box_detail_layout);
        mSettingLayout = navigationView.findViewById(R.id.nav_setting_box_detail_layout);
        mUsingLayout = navigationView.findViewById(R.id.nav_using_box_detail_layout);


        mTermsUseText = findViewById(R.id.terms_text);
        mPrivacyText = findViewById(R.id.individual_terms_text);
        mTermsElectronicText = findViewById(R.id.electronic_finance_terms_text);
        mTermsLocationText = findViewById(R.id.location_terms_text);
        mWaitingValueText = findViewById(R.id.waiting_value);
        mNewOrderValueText = findViewById(R.id.new_order_value);
        mCancelValueText = findViewById(R.id.cancel_value);
        mSaleValueText = findViewById(R.id.sale_value);
        mSuccessValueText = findViewById(R.id.sale_complete_value);
        mCalculateDateText = findViewById(R.id.calculate_date_text);
        mCalculateValueText = findViewById(R.id.calculate_value);
        mReservesValueText = findViewById(R.id.reserves_value);
        mNavProductQuestionValueText = findViewById(R.id.nav_product_question_value);
        mNavCustomerValueText = findViewById(R.id.nav_customer_question_value);
        mNewOrderNavText = findViewById(R.id.nav_new_order_question_value);
        mProductQuestionValueText = findViewById(R.id.business_contact_us_value);
        mCustomerValueText = findViewById(R.id.business_user_contact_value);
        mProductQuestionLayout = findViewById(R.id.fifth_first_layout);
        mCustomerQuestionLayout = findViewById(R.id.fifth_second_layout);
        mTopScrollLayout = findViewById(R.id.seventh_layout);
        mProductLayout = findViewById(R.id.second_button_layout);
        mReviewLayout = findViewById(R.id.third_button_layout);

        mFirstRefreshImg = findViewById(R.id.first_refresh_img);
        mSecondRefreshImg = findViewById(R.id.second_refresh_img);
        mThirdRefreshImg = findViewById(R.id.third_refresh_img);

        mMainButton = findViewById(R.id.main_button);
        mProductInsertButton = findViewById(R.id.product_insert_button);



        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalMainActivity.class);
                startActivity(intent);
            }
        });

        mProductInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, ProductInsertActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mTermsUseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TermsUseActivity.class);
                startActivity(intent);
            }
        });


        mPrivacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PrivacyActivity.class);
                startActivity(intent);
            }
        });


        mTermsElectronicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TermsElectronicActivity.class);
                startActivity(intent);
            }
        });

        mTermsLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TermsLocationActivity.class);
                startActivity(intent);
            }
        });



        mMyInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MyInfoUpdateActivity.class);
                Intent intent = new Intent(mContext, UpdateBusinessInfoActivity.class);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivity(intent);
            }
        });

        mThumbnailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MyInfoUpdateActivity.class);
                Intent intent = new Intent(mContext, UpdateBusinessInfoActivity.class);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivity(intent);
            }
        });

        mNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MyInfoUpdateActivity.class);
                Intent intent = new Intent(mContext, UpdateBusinessInfoActivity.class);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivity(intent);
            }
        });

        mProductManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, ProductManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                finish();
            }
        });

        mSaleManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, SaleManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                finish();
            }
        });

        mNoticeManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, QuestionManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                finish();
            }
        });

        mProductInsertLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, ProductInsertActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                finish();
            }
        });

        mSaleInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MyInfoUpdateActivity.class);
                Intent intent = new Intent(mContext, UpdateBusinessInfoActivity.class);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivity(intent);
            }
        });

        mFriendShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "준비중인 서비스 입니다.", Toast.LENGTH_SHORT).show();
                Util.shareKakao(mContext);
            }
        });


        mProductBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, QuestionManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        finish();
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mCustomerBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, QuestionManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        finish();
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mNewOrderBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, SaleManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        finish();
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoticeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        mEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EventActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        mAdminLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, QuestionManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        finish();
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        mUsingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UsingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        mProductQuestionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, QuestionManagementActivity.class);
                        intent.putExtra("flag", 0);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        finish();
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mCustomerQuestionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, QuestionManagementActivity.class);
                        intent.putExtra("flag", 1);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        finish();
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        mProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpdateBusinessInfoActivity.class);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivity(intent);
            }
        });


        mReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mJsonObject.getInt("auth_enabled") == 1) {
                        Intent intent = new Intent(mContext, ProductManagementActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        finish();
                    }else {
                        Toast.makeText(mContext, "계정 인증후 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerVisible(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        mMenuImg = findViewById(R.id.app_bar_menu_img);
        mMenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerVisible(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });


        mTopScrollLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mScrollView.pageScroll(View.FOCUS_UP);
//                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        mFirstRefreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectTask = new SelectTask(mDbManager.selectUserId(), mDate);
                mSelectTask.execute();
            }
        });

        mSecondRefreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectTask = new SelectTask(mDbManager.selectUserId(), mDate);
                mSelectTask.execute();
            }
        });

        mThirdRefreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectTask = new SelectTask(mDbManager.selectUserId(), mDate);
                mSelectTask.execute();
            }
        });

        Calendar now = Calendar.getInstance();
        mDate = now.get(Calendar.YEAR) + "-" + ((now.getTime().getMonth()+1)<10?"0"+(now.getTime().getMonth()+1):now.getTime().getMonth());
        String date = (now.get(Calendar.YEAR)+"").substring(2,4) + "-" + ((now.getTime().getMonth()+1)<10?"0"+(now.getTime().getMonth()+1):now.getTime().getMonth());
        mCalculateDateText.setText(date+"-01~" + date + "말일" );


        mSelectTask = new SelectTask(mDbManager.selectUserId(), mDate);
        mSelectTask.execute();

    }

    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        String id, date;

        SelectTask(String _id, String _date) {
            id = _id;
            date = _date;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.accountBusinessSelect(id, date);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;
            String path = null;

            if(success) {
                try {
                    path = network.mAccountInfo.getJSONObject(0).getString("picture");
                    mJsonObject = network.mAccountInfo.getJSONObject(0);
                    mNameText.setText(Html.fromHtml(network.mAccountInfo.getJSONObject(0).getString("name")).toString());
                    if(network.mAccountInfo.getJSONObject(0).getInt("auth_enabled") == 1) {
                        mUnauthorizedLayout.setBackgroundResource(R.mipmap.line_confirm_press);
                        mUnauthorizedText.setText("인증업체");
                        mUnauthorizedText.setTextColor(getColor(R.color.lavender));
                    }

                    mWaitingValueText.setText(mJsonObject.getInt("waiting")+"");
                    mNewOrderValueText.setText(mJsonObject.getInt("new")+"");
                    mSuccessValueText.setText(mJsonObject.getInt("success")+"");
                    mCancelValueText.setText(mJsonObject.getInt("cancel_waiting")+"");
                    mSaleValueText.setText(mJsonObject.getInt("new")+"");
                    mNewOrderNavText.setText(mJsonObject.getInt("new")+"");
                    mProductQuestionValueText.setText(mJsonObject.getInt("product_question")+"");
                    mCustomerValueText.setText(mJsonObject.getInt("market_question")+"");
                    mNavProductQuestionValueText.setText(mJsonObject.getInt("product_question")+"");
                    mNavCustomerValueText.setText(mJsonObject.getInt("market_question")+"");
//                    mCalculateValueText.setText(Util.toNumFormat(mJsonObject.getInt("calculate")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(path.length() > 0) {
                    mThumbnailImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(path));
                }
            }else {
                Log.d("BusinessMainActivity", "mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }



    @SuppressLint("WrongConstant")
    private void checkPermissionF() {

        if (android.os.Build.VERSION.SDK_INT >= M) {
            // only for LOLLIPOP and newer versions
            int permissionResult = 0;
            permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionResult == getPackageManager().PERMISSION_DENIED) {
                //요청한 권한( WRITE_EXTERNAL_STORAGE )이 없을 때..거부일때...
                        /* 사용자가 WRITE_EXTERNAL_STORAGE 권한을 한번이라도 거부한 적이 있는 지 조사한다.
                        * 거부한 이력이 한번이라도 있다면, true를 리턴한다.
                        */
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                    if (Build.VERSION.SDK_INT >= M) {
                  /*          requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);*/
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }


                    //최초로 권한을 요청할 때.
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    //        getThumbInfo();
                }
            }else{
                //권한이 있을 때.
                //       getThumbInfo();
            }

        } else {
            //   getThumbInfo();
        }

    }

    /**
     * 사용자가 권한을 허용했는지 거부했는지 체크
     * @param requestCode   1번
     * @param permissions   개발자가 요청한 권한들
     * @param grantResults  권한에 대한 응답들
     *                    permissions와 grantResults는 인덱스 별로 매칭된다.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            /* 요청한 권한을 사용자가 "허용"했다면 인텐트를 띄워라
                내가 요청한 게 하나밖에 없기 때문에. 원래 같으면 for문을 돈다.*/
/*            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);*/

            for(int i = 0 ; i < permissions.length ; i++) {
                if (grantResults.length > 0 && grantResults[i] == getPackageManager().PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED) {
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != getPackageManager().PERMISSION_GRANTED) {
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != getPackageManager().PERMISSION_GRANTED) {
                    }
          /*          if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_PHONE_STATE ( 권한 성공 ) ");
                    }*/
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED) {
                    }
                }


            }

        } else {
            Toast.makeText(mContext, "권한설정 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }

    }
}

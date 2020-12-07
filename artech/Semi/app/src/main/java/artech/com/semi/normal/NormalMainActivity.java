package artech.com.semi.normal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

import static android.os.Build.VERSION_CODES.M;

public class NormalMainActivity extends AppCompatActivity {

    Context mContext;

    SelectTask mSelectTask;
    SelectNormalTask mSelectNormalTask;

    private TextView mTextMessage;
    RelativeLayout mTopScrollLayout, mLocationLayout, mBaitLayout, mRecommendationFirstLayout, mRecommendationSecondLayout, mRecommendationThirdLayout, mBestLayout, mHotDealLayout, mSeaLayout, mBestShopLayout, mTalkLayout, mRecommendShopLayout, mTermsUseLayout, mPrivacyLayout, mTermsElectronicLayout, mTermsLocationLayout, mCompanyInfoLayout, mTermsCompanyInfoLayout, mBaitBestFirstLayout, mBaitBestSecondLayout, mBaitBestThirdLayout, mShopBestFirstLayout, mShopBestSecondLayout, mShopBestThirdLayout, mFirstEventLayout, mSecondEventLayout, mThirdEventLayout;
    NestedScrollView mNestedScrollView;

    ImageView mRecommendationFirstImg, mRecommendationSecondImg, mRecommendationThirdImg, mSearchImg, mCompanyInfoFoldImg;
    TextView mRecommendationFirstTitleText, mRecommendationSecondTitleText, mRecommendationThirdTitleText, mRecommendationFirstDetailText, mRecommendationSecondDetailText, mRecommendationThirdDetailText, mRecommendationMoreText, mBaitBestFirstText, mBaitBestSecondText, mBaitBestThirdText, mShopBestFirstText, mShopBestSecondText, mShopBestThirdText;

    ArrayList<String> mIdArrayList = new ArrayList<>();

    JSONArray mBestJSONArray;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.nav_home);
                    return true;
                case R.id.navigation_basket:
                    intent = new Intent(mContext, ProductManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    finish();
                    return true;
                case R.id.navigation_favorites:
                    intent = new Intent(mContext, FavoritesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    finish();
                    return true;
                case R.id.navigation_my_info:
                    intent = new Intent(mContext, MyInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    finish();
                    return true;
                case R.id.navigation_more:
                    intent = new Intent(mContext, MoreActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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

        setContentView(R.layout.activity_normal_main);

        checkPermissionF();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        mNestedScrollView = findViewById(R.id.nested_scroll_view);

        mLocationLayout = findViewById(R.id.local_search_layout);
        mBaitLayout = findViewById(R.id.bait_search_layout);
        mTopScrollLayout = findViewById(R.id.eighth_layout);
        mBestLayout = findViewById(R.id.best_bait_layout);
        mHotDealLayout = findViewById(R.id.hot_deal_layout);
        mSeaLayout = findViewById(R.id.sea_layout);
        mBestShopLayout = findViewById(R.id.fishing_room_layout);
        mTalkLayout = findViewById(R.id.talk_layout);
        mRecommendShopLayout = findViewById(R.id.recommendation_layout);
        mTermsUseLayout = findViewById(R.id.ninth_second_first_layout);
        mPrivacyLayout = findViewById(R.id.ninth_second_second_layout);
        mTermsElectronicLayout = findViewById(R.id.ninth_second_third_layout);
        mTermsLocationLayout = findViewById(R.id.ninth_second_fourth_layout);
        mCompanyInfoLayout = findViewById(R.id.company_info_layout);
        mTermsCompanyInfoLayout = findViewById(R.id.terms_comapny_info_layout);
        mBaitBestFirstLayout = findViewById(R.id.bait_best_first_layout);
        mBaitBestSecondLayout = findViewById(R.id.bait_best_second_layout);
        mBaitBestThirdLayout = findViewById(R.id.bait_best_third_layout);
        mShopBestFirstLayout = findViewById(R.id.shop_best_first_layout);
        mShopBestSecondLayout = findViewById(R.id.shop_best_second_layout);
        mShopBestThirdLayout = findViewById(R.id.shop_best_third_layout);
        mFirstEventLayout = findViewById(R.id.first_event_layout);
        mSecondEventLayout = findViewById(R.id.second_event_layout);
        mThirdEventLayout = findViewById(R.id.third_event_layout);

        mRecommendationFirstImg = findViewById(R.id.recommendation_first_img);
        mRecommendationSecondImg = findViewById(R.id.recommendation_second_img);
        mRecommendationThirdImg = findViewById(R.id.recommendation_third_img);
        mSearchImg = findViewById(R.id.app_bar_menu_img);
        mCompanyInfoFoldImg = findViewById(R.id.company_info_fold_img);


        mRecommendationFirstTitleText = findViewById(R.id.recommendation_title_text);
        mRecommendationSecondTitleText = findViewById(R.id.recommendation_title_second_text);
        mRecommendationThirdTitleText = findViewById(R.id.recommendation_title_third_text);
        mRecommendationFirstDetailText = findViewById(R.id.recommendation_sub_text);
        mRecommendationSecondDetailText = findViewById(R.id.recommendation_sub_second_text);
        mRecommendationThirdDetailText = findViewById(R.id.recommendation_sub_third_text);
        mRecommendationMoreText = findViewById(R.id.sixth_more_text);
        mBaitBestFirstText = findViewById(R.id.bait_best_first_text);
        mBaitBestSecondText = findViewById(R.id.bait_best_second_text);
        mBaitBestThirdText = findViewById(R.id.bait_best_third_text);
        mShopBestFirstText = findViewById(R.id.shop_best_first_text);
        mShopBestSecondText = findViewById(R.id.shop_best_second_text);
        mShopBestThirdText = findViewById(R.id.shop_best_third_text);

        mRecommendationFirstLayout = findViewById(R.id.recommendation_first_layout);
        mRecommendationSecondLayout = findViewById(R.id.recommendation_second_layout);
        mRecommendationThirdLayout = findViewById(R.id.recommendation_third_layout);

        mSearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchLocationActivity.class);
                intent.putExtra("flag" , 2);
                startActivity(intent);
            }
        });

        mLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchLocationActivity.class);
                intent.putExtra("flag" , 0);
                startActivity(intent);
            }
        });

        mBaitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchLocationActivity.class);
                intent.putExtra("flag" , 1);
                startActivity(intent);
            }
        });

        mBestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductManagementActivity.class);
                startActivity(intent);
            }
        });

        mHotDealLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductManagementActivity.class);
                intent.putExtra("flag" , 1);
                startActivity(intent);
            }
        });

        mSeaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductManagementActivity.class);
                intent.putExtra("flag" , 2);
                startActivity(intent);
            }
        });

        mBestShopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BestShopActivity.class);
                startActivity(intent);
            }
        });

        mRecommendShopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecommendShopActivity.class);
                startActivity(intent);
            }
        });

        mRecommendationMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecommendShopActivity.class);
                startActivity(intent);
            }
        });

        mTalkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TalkActivity.class);
                startActivity(intent);
            }
        });

        mTopScrollLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mNestedScrollView.fullScroll(View.FOCUS_UP);
//                mNestedScrollView.smoothScrollTo(0,0);
//                mNestedScrollView.scrollTo(0,0);
                mNestedScrollView.pageScroll(View.FOCUS_UP);
                mNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        mRecommendationFirstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("id", mIdArrayList.get(0));
                startActivity(intent);
            }
        });

        mRecommendationSecondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("id", mIdArrayList.get(1));
                startActivity(intent);
            }
        });

        mRecommendationThirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("id", mIdArrayList.get(2));
                startActivity(intent);
            }
        });

        mTermsUseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TermsUseActivity.class);
                startActivity(intent);
            }
        });


        mPrivacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PrivacyActivity.class);
                startActivity(intent);
            }
        });


        mTermsElectronicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TermsElectronicActivity.class);
                startActivity(intent);
            }
        });

        mTermsLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TermsLocationActivity.class);
                startActivity(intent);
            }
        });

        mCompanyInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTermsCompanyInfoLayout.getVisibility() == View.VISIBLE) {
                    mTermsCompanyInfoLayout.setVisibility(View.GONE);
                    mCompanyInfoFoldImg.setRotation(0);
                }else {
                    mTermsCompanyInfoLayout.setVisibility(View.VISIBLE);
                    mCompanyInfoFoldImg.setRotation(180);
                }
            }
        });

        mBaitBestFirstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBestJSONArray != null) {
                    try {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("id", mBestJSONArray.getJSONObject(0).getString("product_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mBaitBestSecondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBestJSONArray != null) {
                    try {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("id", mBestJSONArray.getJSONObject(1).getString("product_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mBaitBestThirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBestJSONArray != null) {
                    try {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("id", mBestJSONArray.getJSONObject(2).getString("product_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mShopBestFirstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBestJSONArray != null) {
                    try {
                        Intent intent = new Intent(mContext, ShopDetailActivity.class);
                        intent.putExtra("id", mBestJSONArray.getJSONObject(0).getString("user_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mShopBestSecondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBestJSONArray != null) {
                    try {
                        Intent intent = new Intent(mContext, ShopDetailActivity.class);
                        intent.putExtra("id", mBestJSONArray.getJSONObject(1).getString("user_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mShopBestThirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBestJSONArray != null) {
                    try {
                        Intent intent = new Intent(mContext, ShopDetailActivity.class);
                        intent.putExtra("id", mBestJSONArray.getJSONObject(2).getString("user_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mFirstEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoticeActivity.class);
                startActivity(intent);
            }
        });

        mSecondEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("https://semiapp.modoo.at");
                i.setData(u);
                startActivity(i);
            }
        });

        mThirdEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Play 스토어");
                alertDialog.setMessage("Play 스토어에 접속하시겠습니까?");
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=artech.com.semi.free")));
                    }
                }).show();
            }
        });

        mSelectTask = new SelectTask();
        mSelectTask.execute();
    }

    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        String id;

        SelectTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.marketRecommendSelect();
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
            String title = null;
            String detail = null;

            if(success) {
                try {
                    for(int i = 0; i < network.mMarketArray.length(); i++) {
                        path = network.mMarketArray.getJSONObject(i).getString("picture");
                        title = Html.fromHtml(network.mMarketArray.getJSONObject(i).getString("company")).toString();
                        detail = Html.fromHtml(network.mMarketArray.getJSONObject(i).getString("address")).toString();
                        mIdArrayList.add(network.mMarketArray.getJSONObject(i).getString("id"));

                        if(i == 0) {
                            mRecommendationFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(path));
                            mRecommendationFirstTitleText.setText(title);
                            mRecommendationFirstDetailText.setText(detail);
                        }else if(i == 1) {
                            mRecommendationSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(path));
                            mRecommendationSecondTitleText.setText(title);
                            mRecommendationSecondDetailText.setText(detail);
                        }else if(i == 2) {
                            mRecommendationThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(path));
                            mRecommendationThirdTitleText.setText(title);
                            mRecommendationThirdDetailText.setText(detail);
                        }
                    }

                    if(network.mMarketArray.length() == 1) {
                        mRecommendationSecondLayout.setVisibility(View.GONE);
                        mRecommendationThirdLayout.setVisibility(View.GONE);
                    }else if(network.mMarketArray.length() == 2) {
                        mRecommendationThirdLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mSelectNormalTask = new SelectNormalTask();
                mSelectNormalTask.execute();
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

    private class SelectNormalTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        String id;

        SelectNormalTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.normalSelect();
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectNormalTask = null;

            if(success) {
                try {
                    for(int i = 0; i < network.mJSONArray.length(); i++) {
                        mBestJSONArray = network.mJSONArray;
                        if(i == 0) {
                            mBaitBestFirstText.setText(Html.fromHtml(network.mJSONArray.getJSONObject(i).getString("product_name")).toString());
                            mShopBestFirstText.setText(Html.fromHtml(network.mJSONArray.getJSONObject(i).getString("company")).toString());
                        }else if(i == 1) {
                            mBaitBestSecondText.setText(Html.fromHtml(network.mJSONArray.getJSONObject(i).getString("product_name")).toString());
                            mShopBestSecondText.setText(Html.fromHtml(network.mJSONArray.getJSONObject(i).getString("company")).toString());
                        }else if(i == 2) {
                            mBaitBestThirdText.setText(Html.fromHtml(network.mJSONArray.getJSONObject(i).getString("product_name")).toString());
                            mShopBestThirdText.setText(Html.fromHtml(network.mJSONArray.getJSONObject(i).getString("company")).toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.d("BusinessMainActivity", "mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectNormalTask = null;
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

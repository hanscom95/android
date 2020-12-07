package artech.com.semi.normal;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.normal.fragment.ProductDetailInfoFragment;
import artech.com.semi.normal.fragment.ProductReviewFragment;
import artech.com.semi.normal.item.ProductManagementItem;
import artech.com.semi.utility.BottomSheetDialog;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;
import artech.com.semi.utility.ViewFlipperAction;
import artech.com.semi.utility.ViewPagerAdapter;

public class ProductDetailActivity extends AppCompatActivity implements ViewFlipperAction.ViewFlipperCallback {

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
    DBManager mDbManager;
    TabLayout mTabLayout;

    TextView mTitleText, mPriceText, mPageText, mShopNameText, mShopAddressText, mShopCallText;
    RelativeLayout mReviewImgLayout, mTermsUseLayout, mPrivacyLayout, mTermsElectronicLayout, mTermsLocationLayout;
    ImageView mReviewFirstImg, mReviewSecondImg, mReviewThirdImg, mBackImg, mHeartImg, mShareImg;

    Button mBuyingButton;

    SelectTask mSelectTask;
    UpdateTask mUpdateTask;
    InsertTask mInsertTask;
    InsertRecentlyTask mInsertRecentlyTask;

    ViewPager mBannerPager;


    JSONObject mJsonObject;

    public ArrayList<String> mImgArrayList = new ArrayList<>();

    String mId;
    int mPosition = 0;
    int mHeartUse = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mBannerPager = findViewById(R.id.banner_view_flipper);

        mTabLayout = findViewById(R.id.tab_layout);
        mTermsUseLayout = findViewById(R.id.ninth_second_first_layout);
        mPrivacyLayout = findViewById(R.id.ninth_second_second_layout);
        mTermsElectronicLayout = findViewById(R.id.ninth_second_third_layout);
        mTermsLocationLayout = findViewById(R.id.ninth_second_fourth_layout);

        mBackImg = findViewById(R.id.app_bar_back_img);
        mHeartImg = findViewById(R.id.app_bar_heart_menu_img);
        mShareImg = findViewById(R.id.app_bar_share_menu_img);

        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitleText = findViewById(R.id.title_text);
        mPriceText = findViewById(R.id.price_text);
        mPageText = findViewById(R.id.banner_page_text);
        mShopNameText = findViewById(R.id.shop_info_text);
        mShopAddressText = findViewById(R.id.shop_address_text);
        mShopCallText = findViewById(R.id.shop_call_text);

        mBuyingButton = findViewById(R.id.buying_button);

        mReviewImgLayout = findViewById(R.id.review_img_layout);
        mReviewFirstImg = findViewById(R.id.first_circle_Img);
        mReviewSecondImg = findViewById(R.id.second_circle_Img);
        mReviewThirdImg = findViewById(R.id.third_circle_Img);

        mHeartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHeartUse == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("product_id", mId);
                        jsonObject.put("user_id", mDbManager.selectUserId());
                        jsonObject.put("flag", 0);
                        jsonObject.put("use_yn", 1);

                        mInsertTask = new InsertTask(jsonObject);
                        mInsertTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mBuyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", mId);

                        mUpdateTask = new UpdateTask(jsonObject);
                        mUpdateTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String receiver = mJsonObject.getString("phone");

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + receiver));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(false) {
                    ProductManagementItem item = new ProductManagementItem();
                    try {
                        item.setId(mId);
                        item.setTitle(Html.fromHtml(mJsonObject.getString("product_name")).toString());
                        item.setContents(Html.fromHtml(mJsonObject.getString("product_content")).toString());
                        item.setShop(Html.fromHtml(mJsonObject.getString("company")).toString());
                        item.setState(mJsonObject.getInt("state"));
                        item.setPrice(mJsonObject.getInt("price"));
                        item.setSalePrice(mJsonObject.getInt("sale_price"));
                        item.setRecommand(mJsonObject.getInt("recommend"));
                        item.setFreshness(mJsonObject.getInt("freshness"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    BottomSheetDialog dialog = BottomSheetDialog.getInstance(mContext, item);
                    dialog.mId = mId;
                    dialog.show(getSupportFragmentManager(), "bottomSheet");
                }
            }
        });


        mShopCallText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String receiver = mJsonObject.getString("phone");

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + receiver));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

//                if(tab.getPosition() < 2) {
                if(tab.getPosition() < 1) {
                    mReviewImgLayout.setVisibility(View.GONE);
                }else {
                    mReviewImgLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        mShareImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Util.shareKakao(mContext);
            }
        });


        try {
            Intent intent = getIntent();
            mId = intent.getStringExtra("id");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mId);
            jsonObject.put("user_id", mDbManager.selectUserId());

//            mSelectTask = new SelectTask(jsonObject);
//            mSelectTask.execute();
            mInsertRecentlyTask = new InsertRecentlyTask(jsonObject);
            mInsertRecentlyTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //인덱스 업데이트
    @Override
    public void onFlipperActionCallback(int position) {
        Log.d("ddd", ""+position);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return ProductDetailInfoFragment.newInstance(mContext, position + 1, mId);
//            }else if(position == 1) {
//                return ProductDetailBuyingInfoFragment.newInstance(mContext, position + 1);
//            }else if(position == 2) {
            }else if(position == 1) {
                return ProductReviewFragment.newInstance(mContext, position + 1, mId);
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }


    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        SelectTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.productNormalItemSelect(jsonObject);
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
                    mJsonObject = network.mProductArray.getJSONObject(0);
                    Log.d("ProductDetailActivity", "json : " + mJsonObject.toString());

                    mTitleText.setText(Html.fromHtml(mJsonObject.getString("product_name")));
                    mShopNameText.setText("* 업체정보 : " + Html.fromHtml(mJsonObject.getString("company")));
                    mShopAddressText.setText("* 업체주소 : " + Html.fromHtml(mJsonObject.getString("address")));
                    mShopCallText.setText("* 업체문의 : " + Html.fromHtml(mJsonObject.getString("phone")));


                    double price = mJsonObject.getInt("price")-(mJsonObject.getInt("price")*(mJsonObject.getInt("sale_price")*0.01));

                    mPriceText.setText(Util.toNumFormat((int)price)+"원");

                    mHeartUse = mJsonObject.getInt("favorites");

                    if(mHeartUse == 1) {
                        mHeartImg.setImageResource(R.mipmap.ic_img_favorite_2_heart_press);
                    }



//                    Log.d("ProductDetailActivity", "json : " + mJsonObject.toString());
                    String picture1 = mJsonObject.getString("picture1");
                    String picture2 = mJsonObject.getString("picture2");
                    String picture3 = mJsonObject.getString("picture3");
//                    Log.d("ProductDetailActivity", "picture1 : " + picture1);
//                    Log.d("ProductDetailActivity", "picture2 : " + picture2);
//                    Log.d("ProductDetailActivity", "picture3 : " + picture3);
//                    Log.d("ProductDetailActivity", "company : " + mJsonObject.getString("company"));

                    if(picture1 != "null") {
                        mPosition++;
                    }
//                    if(picture2 != "null") {
//                        mPosition++;
//                    }
//                    if(picture3 != "null") {
//                        mPosition++;
//                    }

                    Bitmap[] bitmaps = new Bitmap[mPosition];
                    for(int i = 0; i < mPosition; i++) {
                        bitmaps[i] = Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("picture"+(i+1)));
                    }

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, bitmaps);

                    mBannerPager.setAdapter(viewPagerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("ProductDetailActivity", "success mAdminTask : " + network.mResult);
            }else {
                Log.d("ProductDetailActivity", "not success mAdminTask : " + network.mResult);
                Log.d("ProductDetailActivity", "not success mAdminTask : " + network.mMsg);
                finish();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }


    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        UpdateTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.productRecommendUpdate(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {

            }else {
                Toast.makeText(mContext, "전화번호를 호출 하는데 실패했습니다. 다시 클릭해 주세요.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
        }
    }


    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        InsertTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.favoritesInsert(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                mHeartUse = 1;
                mHeartImg.setImageResource(R.mipmap.ic_img_favorite_2_heart_press);
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("id", mId);
//
//                    mSelectTask = new SelectTask(jsonObject);
//                    mSelectTask.execute();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mInsertTask = null;
        }
    }


    private class InsertRecentlyTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        InsertRecentlyTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.recentlyViewInsert(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertRecentlyTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();

        }

        @Override
        protected void onCancelled() {
            mInsertRecentlyTask = null;
        }
    }



    public void selectGallery(int flag) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, flag);
    }


    private String sendPicture(Uri imgUri) {

        String imagePath = Util.getRealPathFromURI(mContext, imgUri); // path 경로
        Log.d("sendPicture", "imagePath : " + imagePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);//경로를 통해 비트맵으로 전환


        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = Util.exifOrientationToDegrees(exifOrientation);
            bitmap = Util.rotate(bitmap, exifDegree);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap != null) {
            return Util.getEncoded64ImageStringFromBitmap(bitmap);
        }

        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(mImgArrayList.size() > 0)
                mImgArrayList.clear();

            mReviewImgLayout.setVisibility(View.VISIBLE);

            if(data.getClipData() == null) {
                mImgArrayList.add(sendPicture(data.getData())); //갤러리에서 가져오기
            }else {
                ClipData clipData = data.getClipData();

                if (clipData.getItemCount() > 3){
                    Toast.makeText(mContext, "사진은 3개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 멀티 선택에서 하나만 선택했을 경우
                else if (clipData.getItemCount() == 1) {
                    String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                    Uri uri = Uri.parse(clipData.getItemAt(0).getUri().getPath());
//                            mImgArrayList.add(sendPicture(clipData.getItemAt(0).getUri()));
                    mImgArrayList.add(sendPicture(clipData.getItemAt(0).getUri()));

                } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = Uri.parse(clipData.getItemAt(i).getUri().getPath());
                        mImgArrayList.add(sendPicture(clipData.getItemAt(i).getUri()));
                    }
                }
            }



            if(mImgArrayList.size() > 0) {
                for(int i = 0; i < mImgArrayList.size(); i++) {
                    if(i == 0) {
                        mReviewFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                    }else if(i == 1) {
                        mReviewSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                    }else if(i == 2) {
                        mReviewThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                    }

                }
            }
        }
    }
}

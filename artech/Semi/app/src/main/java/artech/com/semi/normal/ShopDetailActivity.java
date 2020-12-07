package artech.com.semi.normal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.ShopTagAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;
import artech.com.semi.utility.google.CustomClusterItem;
import artech.com.semi.utility.google.CustomClusterRenderer;

public class ShopDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    Context mContext;
    DBManager mDbManager;

    CollapsingToolbarLayout mToolbarLayout;

    Toolbar mToolbar;

    RelativeLayout mAddressLayout, mAddressCopyLayout, mReviewWriteLayout, mReviewLayout, mServiceParkingLayout, mServiceWifiLayout, mServiceParcelLayout, mServiceAsLayout, mServiceAlldayLayout, mServiceShopLayout, mServiceInfoLayout, mServiceBathroomLayout, mServiceProductLayout, mCallLayout, mProductFirstLayout, mProductSecondLayout, mProductThirdLayout, mProductLayout, mProductDefaultPriceFirstLayout, mProductDefaultPriceSecondLayout, mProductDefaultPriceThirdLayout;
    TextView mNameText, mOrderText, mFavoritesText, mEventText, mCommentText, mReservationText, mInfoText, mReviewTitleText, mReviewDateText, mReviewContentsText, mHeartValueText, mAddressText, mProductViewText, mProductTitleFirstText, mProductContentsFirstText, mProductPriceFirstText, mProductDefaultPriceFirstText, mProductTitleSecondText, mProductContentsSecondText, mProductPriceSecondText, mProductDefaultPriceSecondText, mProductTitleThirdText, mProductContentsThirdText, mProductPriceThirdText, mProductDefaultPriceThirdText, mProductStatusFirstText, mProductStatusSecondText, mProductStatusThirdText, mReviewMoreText;
    ImageView mBoardImg, mRankingImg, mReviewProfileImg, mReviewFirstImg, mReviewSecondImg, mReviewThirdImg, mReviewFourthImg, mReviewFifthImg, mHeartImg, mProductFirstImg, mProductSecondImg, mProductThirdImg, mShareImg;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ShopTagAdapter mAdapter;


    GoogleMap mMap;
    Geocoder mGeocoder;

    SelectTask mSelectTask;
    InsertTask mInsertTask;

    JSONObject mJsonObject;

    String mId;
    int mFavoritesYn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_shop_detail);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mToolbarLayout = findViewById(R.id.collapsingToolbarLayout01);
        mToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.white));
        mToolbarLayout.setExpandedTitleColor(getColor(android.R.color.transparent));

        mAddressLayout = findViewById(R.id.address_layout);
        mAddressCopyLayout = findViewById(R.id.address_copy_layout);
        mReviewLayout = findViewById(R.id.review_detail_layout);
        mReviewWriteLayout = findViewById(R.id.review_write_layout);
        mServiceParkingLayout = findViewById(R.id.service_parking_layout);
        mServiceWifiLayout = findViewById(R.id.service_wifi_layout);
        mServiceParcelLayout = findViewById(R.id.service_parcel_layout);
        mServiceAsLayout = findViewById(R.id.service_as_layout);
        mServiceAlldayLayout = findViewById(R.id.service_all_day_layout);
        mServiceShopLayout = findViewById(R.id.service_shop_layout);
        mServiceInfoLayout = findViewById(R.id.service_info_layout);
        mServiceBathroomLayout = findViewById(R.id.service_bathroom_layout);
        mServiceProductLayout = findViewById(R.id.service_product_layout);
        mCallLayout = findViewById(R.id.call_layout);
        mProductFirstLayout = findViewById(R.id.product_first_layout);
        mProductSecondLayout = findViewById(R.id.product_second_layout);
        mProductThirdLayout = findViewById(R.id.product_third_layout);
        mProductLayout = findViewById(R.id.fourth_second_layout);
        mProductDefaultPriceFirstLayout = findViewById(R.id.product_first_default_price_layout);
        mProductDefaultPriceSecondLayout = findViewById(R.id.product_second_default_price_layout);
        mProductDefaultPriceThirdLayout = findViewById(R.id.product_third_default_price_layout);

        mNameText = findViewById(R.id.title_text);
        mOrderText = findViewById(R.id.order_number_text);
        mFavoritesText = findViewById(R.id.favorites_text);
        mCommentText = findViewById(R.id.shop_detail_text);
        mEventText = findViewById(R.id.event_text);
        mReservationText = findViewById(R.id.reservation_text);
        mInfoText = findViewById(R.id.shop_info_text);
        mReviewMoreText = findViewById(R.id.review_more_text);
        mReviewTitleText = findViewById(R.id.nickname_text);
        mReviewDateText = findViewById(R.id.date_text);
        mReviewContentsText = findViewById(R.id.review_detail_text);
        mHeartValueText = findViewById(R.id.heart_value_text);
        mAddressText = findViewById(R.id.address_text);
        mProductViewText = findViewById(R.id.product_view_text);
        mProductTitleFirstText = findViewById(R.id.product_first_title_text);
        mProductContentsFirstText= findViewById(R.id.product_first_contents_text);
        mProductPriceFirstText = findViewById(R.id.product_first_price_text);
        mProductDefaultPriceFirstText = findViewById(R.id.product_first_default_price_text);
        mProductTitleSecondText = findViewById(R.id.product_second_title_text);
        mProductContentsSecondText= findViewById(R.id.product_second_contents_text);
        mProductPriceSecondText = findViewById(R.id.product_second_price_text);
        mProductDefaultPriceSecondText = findViewById(R.id.product_second_default_price_text);
        mProductTitleThirdText = findViewById(R.id.product_third_title_text);
        mProductContentsThirdText= findViewById(R.id.product_third_contents_text);
        mProductPriceThirdText = findViewById(R.id.product_third_price_text);
        mProductDefaultPriceThirdText = findViewById(R.id.product_third_default_price_text);
        mProductStatusFirstText= findViewById(R.id.product_first_status_text);
        mProductStatusSecondText = findViewById(R.id.product_second_status_text);
        mProductStatusThirdText = findViewById(R.id.product_third_status_text);

        mBoardImg = findViewById(R.id.board_img);
        mRankingImg = findViewById(R.id.ranking_img);
        mReviewProfileImg = findViewById(R.id.profile_img);
        mReviewFirstImg = findViewById(R.id.first_fish_img);
        mReviewFirstImg = findViewById(R.id.first_fish_img);
        mReviewSecondImg = findViewById(R.id.second_fish_img);
        mReviewThirdImg = findViewById(R.id.third_fish_img);
        mReviewFourthImg = findViewById(R.id.fourth_fish_img);
        mReviewFifthImg = findViewById(R.id.fifth_fish_img);
        mHeartImg = findViewById(R.id.heart_img);
        mProductFirstImg = findViewById(R.id.product_first_img);
        mProductSecondImg = findViewById(R.id.product_second_img);
        mProductThirdImg = findViewById(R.id.product_third_img);
        mShareImg = findViewById(R.id.share_img);


        mRecyclerView = findViewById(R.id.list_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mHeartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFavoritesYn == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("market_id", mId);
                        jsonObject.put("user_id", mDbManager.selectUserId());
                        jsonObject.put("flag", 1);
                        jsonObject.put("use_yn", 1);

                        mInsertTask = new InsertTask(jsonObject);
                        mInsertTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("address", Html.fromHtml(mJsonObject.getString("address")).toString());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(mContext, "주소가 복사됬습니다.", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mAddressCopyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("address", Html.fromHtml(mJsonObject.getString("address")).toString());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(mContext, "주소가 복사됬습니다.", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mReviewWriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReviewWriteActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
//                finish();
            }
        });

        mCallLayout.setOnClickListener(new View.OnClickListener() {
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

        mReviewMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReviewActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        mProductViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopProductActivity.class);
                intent.putExtra("id", mId);
                try {
                    intent.putExtra("name", Html.fromHtml(mJsonObject.getString("company")).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        mProductFirstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                try {
                    intent.putExtra("id", mJsonObject.getString("product_id1"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        mProductSecondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                try {
                    intent.putExtra("id", mJsonObject.getString("product_id2"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        mProductThirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                try {
                    intent.putExtra("id", mJsonObject.getString("product_id3"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        mShareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.shareKakao(mContext);
            }
        });

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", mId);
            jsonObject.put("user_id", mDbManager.selectUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSelectTask = new SelectTask(jsonObject);
        mSelectTask.execute();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setLatLngBoundsForCameraTarget(latLngBounds);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(false);

            mMap.setMinZoomPreference(10.0f);
            mMap.setMaxZoomPreference(17.0f);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

            LatLng sydney = new LatLng(mJsonObject.getDouble("lat"), mJsonObject.getDouble("lon"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.visible(true).position(sydney).title(Html.fromHtml(mJsonObject.getString("company")).toString()).snippet(Html.fromHtml(mJsonObject.getString("address")).toString())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_point));

            ClusterManager<CustomClusterItem> mClusterManager = new ClusterManager<>(this, mMap);

            mClusterManager.addItem(new CustomClusterItem(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude, markerOptions.getTitle(), markerOptions.getSnippet()));

            mClusterManager.setRenderer(new CustomClusterRenderer(mContext, googleMap, mClusterManager));
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);

            mClusterManager.addItem(new CustomClusterItem(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude, markerOptions.getTitle(), markerOptions.getSnippet()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;
        String id;

        SelectTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.marketItemSelect(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;

            Log.d("ShopDetailActivity", "success : " + success);
            if(success) {
                try {
                    mJsonObject = network.mMarketArray.getJSONObject(0);
                    Log.d("ShopDetailActivity", "mAdminTask : " + network.mMarketArray.getJSONObject(0).toString());

                    mNameText.setText(Html.fromHtml(mJsonObject.getString("company")));
                    mToolbar.setTitle(Html.fromHtml(mJsonObject.getString("company")));
                    mToolbarLayout.setTitle(Html.fromHtml(mJsonObject.getString("company")));
                    mFavoritesYn = mJsonObject.getInt("favorites_yn");
                    Log.d("ShopDetailActivity", "mFavoritesYn : " + mFavoritesYn);
                    if(mFavoritesYn == 1) {
                        mHeartImg.setImageResource(R.mipmap.ic_img_favorite_2_heart_press);
                    }

                    mOrderText.setText("주문수 "+mJsonObject.getInt("cnt"));
                    mFavoritesText.setText("즐겨찾기 " + mJsonObject.getInt("favorites"));
                    mHeartValueText.setText(mJsonObject.getInt("favorites")+"");
                    mCommentText.setText(Html.fromHtml(mJsonObject.getString("comment").replace("\n", "<br>")));
//                    mInfoText.setText(Html.fromHtml(mJsonObject.getString("info").replace("\n", "<br>")));
                    mReservationText.setText(Html.fromHtml(mJsonObject.getString("info").replace("\n", "<br>")));
                    mAddressText.setText(Html.fromHtml(mJsonObject.getString("address").replace("\n", "<br>")));
                    if(mJsonObject.getString("reg_dt") == "null" || mJsonObject.getString("reg_dt") == ""|| mJsonObject.getString("reg_dt") == null) {
                        mReviewLayout.setVisibility(View.INVISIBLE);
                    }else {
                        mReviewTitleText.setText(Html.fromHtml(mJsonObject.getString("reviewer_name")));
                        mReviewDateText.setText(mJsonObject.getString("reg_dt"));
                        mReviewContentsText.setText(Html.fromHtml(mJsonObject.getString("review").replace("\n", "<br>")));

                        int point = mJsonObject.getInt("point");

                        if (point == 0) {
                            mReviewFirstImg.setVisibility(View.GONE);
                            mReviewSecondImg.setVisibility(View.GONE);
                            mReviewThirdImg.setVisibility(View.GONE);
                            mReviewFourthImg.setVisibility(View.GONE);
                            mReviewFifthImg.setVisibility(View.GONE);
                        } else if (point == 1) {
                            mReviewFirstImg.setVisibility(View.VISIBLE);
                            mReviewSecondImg.setVisibility(View.GONE);
                            mReviewThirdImg.setVisibility(View.GONE);
                            mReviewFourthImg.setVisibility(View.GONE);
                            mReviewFifthImg.setVisibility(View.GONE);
                        } else if (point == 2) {
                            mReviewFirstImg.setVisibility(View.VISIBLE);
                            mReviewSecondImg.setVisibility(View.VISIBLE);
                            mReviewThirdImg.setVisibility(View.GONE);
                            mReviewFourthImg.setVisibility(View.GONE);
                            mReviewFifthImg.setVisibility(View.GONE);
                        } else if (point == 3) {
                            mReviewFirstImg.setVisibility(View.VISIBLE);
                            mReviewSecondImg.setVisibility(View.VISIBLE);
                            mReviewThirdImg.setVisibility(View.VISIBLE);
                            mReviewFourthImg.setVisibility(View.GONE);
                            mReviewFifthImg.setVisibility(View.GONE);
                        } else if (point == 4) {
                            mReviewFirstImg.setVisibility(View.VISIBLE);
                            mReviewSecondImg.setVisibility(View.VISIBLE);
                            mReviewThirdImg.setVisibility(View.VISIBLE);
                            mReviewFourthImg.setVisibility(View.VISIBLE);
                            mReviewFifthImg.setVisibility(View.GONE);
                        } else if (point == 5) {
                            mReviewFirstImg.setVisibility(View.VISIBLE);
                            mReviewSecondImg.setVisibility(View.VISIBLE);
                            mReviewThirdImg.setVisibility(View.VISIBLE);
                            mReviewFourthImg.setVisibility(View.VISIBLE);
                            mReviewFifthImg.setVisibility(View.VISIBLE);
                        }
                    }

                    int service = mJsonObject.getInt("service");
                    int[] number =  Util.toNumberFormat(service);
                    if(number[1] == 1) {
                        mServiceParkingLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceParkingLayout.setVisibility(View.GONE);
                    }

                    if(number[2] == 1) {
                        mServiceWifiLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceWifiLayout.setVisibility(View.GONE);
                    }

                    if(number[3] == 1) {
                        mServiceParcelLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceParcelLayout.setVisibility(View.GONE);
                    }

                    if(number[4] == 1) {
                        mServiceAsLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceAsLayout.setVisibility(View.GONE);
                    }

                    if(number[5] == 1) {
                        mServiceAlldayLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceAlldayLayout.setVisibility(View.GONE);
                    }

                    if(number[6] == 1) {
                        mServiceShopLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceShopLayout.setVisibility(View.GONE);
                    }

                    if(number[7] == 1) {
                        mServiceInfoLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceInfoLayout.setVisibility(View.GONE);
                    }

                    if(number[8] == 1) {
                        mServiceBathroomLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceBathroomLayout.setVisibility(View.GONE);
                    }

                    if(number[9] == 1) {
                        mServiceProductLayout.setVisibility(View.VISIBLE);
                    }else {
                        mServiceProductLayout.setVisibility(View.GONE);
                    }

                    String path = mJsonObject.getString("picture1");
                    String profile = mJsonObject.getString("review_profile");
                    mBoardImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(path));
                    mReviewProfileImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(profile));


                    if(mJsonObject.getInt("product_cnt") > 0) {
                        mProductTitleFirstText.setText(Html.fromHtml(mJsonObject.getString("product_title1")).toString());
                        mProductContentsFirstText.setText(Html.fromHtml(mJsonObject.getString("product_content1")).toString());


                        double price1 = mJsonObject.getInt("product_price1")-(mJsonObject.getInt("product_price1")*(mJsonObject.getInt("product_sale_price1")*0.01));

                        if(mJsonObject.getInt("product_sale_price1") == 0) {
                            mProductDefaultPriceFirstLayout.setVisibility(View.GONE);
                        }

                        mProductPriceFirstText.setText(Util.toNumFormat((int) price1) + "원");
                        mProductDefaultPriceFirstText.setText(Util.toNumFormat(mJsonObject.getInt("product_price1")) + "원");
//                        mProductPriceFirstText.setText(Util.toNumFormat(mJsonObject.getInt("product_price1")) + "원");
//                        mProductDefaultPriceFirstText.setText(Util.toNumFormat(mJsonObject.getInt("product_sale_price1")) + "원");

                        if(mJsonObject.getInt("product_state1") == 0){
                            mProductStatusFirstText.setBackgroundResource(R.mipmap.img_state_ing);
                            mProductStatusFirstText.setText("판매중");
                        }else{
                            mProductStatusFirstText.setBackgroundResource(R.mipmap.img_state_end);
                            if(mJsonObject.getInt("product_state1") == 1) {
                                mProductStatusFirstText.setText("재고없음");
                            }else if(mJsonObject.getInt("product_state1") == 2) {
                                mProductStatusFirstText.setText("판매종료");
                            }
                        }

                        if(!mJsonObject.getString("product_picture1").equals("null")) {
                            mProductFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("product_picture1")));
                        }

                        if(mJsonObject.getInt("product_cnt") > 1) {

                            mProductTitleSecondText.setText(Html.fromHtml(mJsonObject.getString("product_title2")).toString());
                            mProductContentsSecondText.setText(Html.fromHtml(mJsonObject.getString("product_content2")).toString());

                            if(mJsonObject.getInt("product_sale_price2") == 0) {
                                mProductDefaultPriceSecondLayout.setVisibility(View.GONE);
                            }

                            double price2 = mJsonObject.getInt("product_price2")-(mJsonObject.getInt("product_price2")*(mJsonObject.getInt("product_sale_price2")*0.01));
                            mProductPriceSecondText.setText(Util.toNumFormat((int) price2) + "원");
                            mProductDefaultPriceSecondText.setText(Util.toNumFormat(mJsonObject.getInt("product_price2")) + "원");


                            if(mJsonObject.getInt("product_state2") == 0){
                                mProductStatusSecondText.setBackgroundResource(R.mipmap.img_state_ing);
                                mProductStatusSecondText.setText("판매중");
                            }else{
                                mProductStatusSecondText.setBackgroundResource(R.mipmap.img_state_end);
                                if(mJsonObject.getInt("product_state2") == 1) {
                                    mProductStatusSecondText.setText("재고없음");
                                }else if(mJsonObject.getInt("product_state2") == 2) {
                                    mProductStatusSecondText.setText("판매종료");
                                }
                            }

                            if(!mJsonObject.getString("product_picture2").equals("null")) {
                                mProductSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("product_picture2")));
                            }
                        }else {
                            mProductSecondLayout.setVisibility(View.GONE);
                            mProductThirdLayout.setVisibility(View.GONE);
                        }

                        if(mJsonObject.getInt("product_cnt") > 2) {
                            mProductTitleThirdText.setText(Html.fromHtml(mJsonObject.getString("product_title3")).toString());
                            mProductContentsThirdText.setText(Html.fromHtml(mJsonObject.getString("product_content3")).toString());
//                            mProductPriceThirdText.setText(Util.toNumFormat(mJsonObject.getInt("product_price3")) + "원");
//                            mProductDefaultPriceThirdText.setText(Util.toNumFormat(mJsonObject.getInt("product_sale_price3")) + "원");

                            if(mJsonObject.getInt("product_sale_price3") == 0) {
                                mProductDefaultPriceThirdLayout.setVisibility(View.GONE);
                            }

                            double price3 = mJsonObject.getInt("product_price3")-(mJsonObject.getInt("product_price3")*(mJsonObject.getInt("product_sale_price3")*0.01));
                            mProductPriceThirdText.setText(Util.toNumFormat((int) price3) + "원");
                            mProductDefaultPriceThirdText.setText(Util.toNumFormat(mJsonObject.getInt("product_price3")) + "원");


                            if(mJsonObject.getInt("product_state3") == 0){
                                mProductStatusThirdText.setBackgroundResource(R.mipmap.img_state_ing);
                                mProductStatusThirdText.setText("판매중");
                            }else{
                                mProductStatusThirdText.setBackgroundResource(R.mipmap.img_state_end);
                                if(mJsonObject.getInt("product_state3") == 1) {
                                    mProductStatusThirdText.setText("재고없음");
                                }else if(mJsonObject.getInt("product_state3") == 2) {
                                    mProductStatusThirdText.setText("판매종료");
                                }
                            }

                            if(!mJsonObject.getString("product_picture3").equals("null")) {
                                mProductThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("product_picture3")));
                            }
                        }else {
                            mProductThirdLayout.setVisibility(View.GONE);
                        }


                    }else {
                        mRankingImg.setVisibility(View.GONE);
                        mProductLayout.setVisibility(View.GONE);
                    }


                    mAdapter = new ShopTagAdapter(mContext);
                    mAdapter.clear();
                    mAdapter.addList(mJsonObject.getString("tag"));
                    mRecyclerView.setAdapter(mAdapter);


                    try {
                        FragmentManager fm = getSupportFragmentManager();
                        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                        mapFragment.getMapAsync((OnMapReadyCallback) mContext);
                        fm.beginTransaction().replace(R.id.map, mapFragment).commit();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                    mGeocoder = new Geocoder(mContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.d("ShopDetailActivity", "mAdminTask : " + network.mResult);
                Log.d("ShopDetailActivity", "mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }





    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject mJsonObject;

        InsertTask(JSONObject jsonObject) {
            mJsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.favoritesInsert(mJsonObject);
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
}

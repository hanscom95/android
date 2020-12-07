package artech.com.semi.normal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.ProductShopSearchAdapter;
import artech.com.semi.normal.fragment.SearchBaitFragment;
import artech.com.semi.normal.fragment.SearchLocationFragment;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.google.CustomClusterItem;
import artech.com.semi.utility.google.CustomClusterRenderer;

public class SearchLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    Context mContext;

    Button mLocationButton, mBaitButton;
    RelativeLayout mListLayout, mFragmentLayout;//, mMyLocationLayout;

    GoogleMap mMap;
    Geocoder mGeocoder;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProductShopSearchAdapter mAdapter;

    SelectTask mSelectTask;

    JSONArray mJsonArray;

    int zoomFlag = -1;

    int mFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_search_location);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageView closeImg = findViewById(R.id.app_bar_close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLocationButton = findViewById(R.id.location_button);
        mBaitButton = findViewById(R.id.bait_button);

        mListLayout = findViewById(R.id.list_layout);
        mFragmentLayout = findViewById(R.id.fragment_layout);
//        mMyLocationLayout = (RelativeLayout) findViewById(R.id.my_location_layout);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.map);
//                mapFragment.getMapAsync((OnMapReadyCallback) mContext);
                if (mFlag != 0) {
                    mListLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mFragmentLayout.setVisibility(View.VISIBLE);
//                    FragmentManager fm = getSupportFragmentManager();
//                    SupportMapFragment mapFragment = SupportMapFragment.newInstance();
//                    mapFragment.getMapAsync((OnMapReadyCallback) mContext);
//                    fm.beginTransaction().replace(R.id.map, mapFragment).commit();
                    Fragment fragment = new SearchLocationFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();

                    mFlag = 0;
                }
            }
        });

        mBaitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFlag != 1) {
                    mListLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mFragmentLayout.setVisibility(View.VISIBLE);

                    Fragment fragment = new SearchBaitFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();

                    mFlag = 1;
                }
            }
        });



        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*mMyLocationLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
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



                Criteria criteria = new Criteria();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(criteria, false);
                Location location = locationManager.getLastKnownLocation(provider);
                double lat =  location.getLatitude();
                double lng = location.getLongitude();


                Log.d("SearchLocation", "lat : " + lat + " / lon : " + lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            }
        });*/


//        FragmentManager fm = getSupportFragmentManager();
//        SupportMapFragment  mapFragment = SupportMapFragment.newInstance();
//        mapFragment.getMapAsync(this);
//        fm.beginTransaction().replace(R.id.map, mapFragment).commit();
//
//
//        mGeocoder = new Geocoder(mContext);



        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag", 0);

        if(flag == 0) {
            mListLayout.setVisibility(View.GONE);
            Fragment fragment = new SearchLocationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();

            mFlag = 0;
        }else if(flag == 1) {
            mListLayout.setVisibility(View.GONE);

            Fragment fragment = new SearchBaitFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();

            mFlag = 1;
        }else {
            JSONObject jsonObject = new JSONObject();
            mSelectTask = new SelectTask(jsonObject, 0);
            mSelectTask.execute();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mFlag = -1;

        mMap = googleMap;


        mMap.setMinZoomPreference(6.9f);
        mMap.setMaxZoomPreference(15.0f);
//        mMap.setLatLngBoundsForCameraTarget(latLngBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(35.798, 127.7)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6.9f));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Log.d("getInfoContnts", "marker.getTitle()" +marker.getTitle() );
                if(marker.getTitle() == null) {
                    return null;
                }

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setText(marker.getSnippet());
                snippet.setVisibility(View.GONE);

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });


        // 클러스터 매니저 생성
        ClusterManager<CustomClusterItem> mClusterManager = new ClusterManager<>(this, mMap);


        try {
            if(mJsonArray != null) {
                for (int i = 0; i < mJsonArray.length(); i++) {
                    LatLng sydney = new LatLng(mJsonArray.getJSONObject(i).getDouble("lat"), mJsonArray.getJSONObject(i).getDouble("lon"));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(sydney).title("["+Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")).toString() +  "]" + Html.fromHtml(mJsonArray.getJSONObject(i).getString("address")).toString()).snippet(mJsonArray.getJSONObject(i).getString("id"))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_point));

//                    mMap.addMarker(markerOptions);

                    Log.d("SearchLocationActivity", "company : " + Html.fromHtml(mJsonArray.getJSONObject(i).getString("company")).toString() + " / lat : " + mJsonArray.getJSONObject(i).get("lat") + " / lon : " + mJsonArray.getJSONObject(i).get("lon"));
                    mClusterManager.addItem(new CustomClusterItem(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude, markerOptions.getTitle(), markerOptions.getSnippet()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mClusterManager.setRenderer(new CustomClusterRenderer(mContext, googleMap, mClusterManager));
//        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<CustomClusterItem>() {
//            @Override
//            public boolean onClusterItemClick(CustomClusterItem customClusterItem) {
//                Intent intent = new Intent(mContext, ShopDetailActivity.class);
//                intent.putExtra("id", customClusterItem.getSnippet());
//                startActivity(intent);
//                return false;
//            }
//        });
        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<CustomClusterItem>() {
            @Override
            public void onClusterItemInfoWindowClick(CustomClusterItem customClusterItem) {
                Log.d("SearchLocationActivity", "onClusterItemInfoWindowClick");
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("id", customClusterItem.getSnippet());
                startActivity(intent);
            }
        });

        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(37.5668367, 126.9785728);
//        LatLng sydney2 = new LatLng(33.263684, 126.613396);
//        LatLng sydney3 = new LatLng(35.115645, 129.081077);
//        LatLng sydney4 = new LatLng(34.353104, 126.747875);
//        LatLng sydney5 = new LatLng(37.483862, 130.897962);
//        LatLng sydney6 = new LatLng(37.774163, 128.928807);
//        LatLng sydney7 = new LatLng(37.533277, 129.096906);
//        LatLng sydney8 = new LatLng(37.533277 + (1 / 200d), 129.096906 + (1 / 200d));
//        LatLng sydney9 = new LatLng(37.533277 + (second / 200d), 129.096906 + (second / 200d));
//        LatLng sydney10 = new LatLng(37.533277 + (third / 200d), 129.096906 + (third / 200d));
//        MarkerOptions markerOptions = new MarkerOptions();
//        MarkerOptions markerOptions2 = new MarkerOptions();
//        MarkerOptions markerOptions3 = new MarkerOptions();
//        MarkerOptions markerOptions4 = new MarkerOptions();
//        MarkerOptions markerOptions5 = new MarkerOptions();
//        MarkerOptions markerOptions6 = new MarkerOptions();
//        MarkerOptions markerOptions7 = new MarkerOptions();
//        MarkerOptions markerOptions8 = new MarkerOptions();
//        MarkerOptions markerOptions9 = new MarkerOptions();
//        MarkerOptions markerOptions10 = new MarkerOptions();
//        markerOptions.position(sydney).title("서울특별시 서울시청");
//        markerOptions2.position(sydney2).title("제주특별시 서귀포시");
//        markerOptions3.position(sydney3).title("부산특별시 해운대");
//        markerOptions4.position(sydney4).title("전라남도 완도");
//        markerOptions5.position(sydney5).title("경상북도 울릉군");
//        markerOptions6.position(sydney6).title("강원도 강릉시");
//        markerOptions7.position(sydney7).title("강원도 동해시");
//        markerOptions8.position(sydney8).title("강원도 동해시 1 ");
//        markerOptions9.position(sydney9).title("강원도 동해시 second ");
//        markerOptions10.position(sydney10).title("강원도 동해시 3 ");



//        LatLngBounds latLngBounds = new LatLngBounds(
//                new LatLng(35.5, 127.4), new LatLng(37, 130));


//        mMap.addMarker(markerOptions);
//        mMap.addMarker(markerOptions2);
//        mMap.addMarker(markerOptions3);
//        mMap.addMarker(markerOptions4);
//        mMap.addMarker(markerOptions5);
//        mMap.addMarker(markerOptions6);
//        mMap.addMarker(markerOptions7);
//        mMap.addMarker(markerOptions8);
//        mMap.addMarker(markerOptions9);
//        mMap.addMarker(markerOptions10);




//        for(int i=0; i<10; i++) {
//            double lat = latLngBounds.getCenter().latitude + (i / 200d);
//            double lng = latLngBounds.getCenter().longitude + (i / 200d);
//            mClusterManager.addItem(new CustomClusterItem(lat, lng, "House"+i));
//        }
//        mClusterManager.addItem(new CustomClusterItem(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude, markerOptions.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions2.getPosition().latitude, markerOptions2.getPosition().longitude, markerOptions2.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions3.getPosition().latitude, markerOptions3.getPosition().longitude, markerOptions3.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions4.getPosition().latitude, markerOptions4.getPosition().longitude, markerOptions4.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions5.getPosition().latitude, markerOptions5.getPosition().longitude, markerOptions5.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions6.getPosition().latitude, markerOptions6.getPosition().longitude, markerOptions6.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions7.getPosition().latitude, markerOptions7.getPosition().longitude, markerOptions7.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions8.getPosition().latitude, markerOptions8.getPosition().longitude, markerOptions8.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions9.getPosition().latitude, markerOptions9.getPosition().longitude, markerOptions9.getTitle()));
//        mClusterManager.addItem(new CustomClusterItem(markerOptions10.getPosition().latitude, markerOptions10.getPosition().longitude, markerOptions10.getTitle()));


        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<CustomClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<CustomClusterItem> cluster) {
                Log.d("mClusterManager", "zoomFlag : " + zoomFlag);
                if(zoomFlag < 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 9));
                }else if(zoomFlag == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 10));
                }else if(zoomFlag == 1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 11));
                }else if(zoomFlag == 2) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 12));
                }else if(zoomFlag == 3) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 13));
                    zoomFlag = 0;
                }

                zoomFlag++;
                return false;
            }
        });




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
        JSONObject mJsonObject;
        int flag = -1;

        SelectTask(JSONObject jsonObject, int i) {
            mJsonObject = jsonObject;
            flag = i;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                if(flag == 0) {
                    connect = network.mapSelect(mJsonObject);
                }else if(flag == 1) {
                    connect = network.mapBaitSelect(mJsonObject);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                mJsonArray  = network.mMarketArray;
            }else {
                mJsonArray = null;
                Toast.makeText(mContext, "검색에 실패 하였습니다.", Toast.LENGTH_SHORT).show();
            }

            if(flag == 0) {
                FragmentManager fm = getSupportFragmentManager();
                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                mapFragment.getMapAsync((OnMapReadyCallback) mContext);
                fm.beginTransaction().replace(R.id.map, mapFragment).commit();


                mGeocoder = new Geocoder(mContext);
            }else {
                mFragmentLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                if(mJsonArray != null &&mJsonArray.length() > 0) {
                    mAdapter = new ProductShopSearchAdapter(mContext);
                    mAdapter.clear();
                    mAdapter.addList(mJsonArray);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }

    public void mapCall(String checked, int i) {
        Log.d("SearchLocationActivity", "mapCall");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("address", checked);
            jsonObject.put("length", checked.length());

            mSelectTask = new SelectTask(jsonObject, i);
            mSelectTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

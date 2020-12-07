package artech.com.semi.utility.google;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.List;

import artech.com.semi.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder mGeocoder;


    EditText mSearchEdit;
    Button mSearchButton;

    int zoomFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGeocoder = new Geocoder(this);

        mSearchEdit = findViewById(R.id.address_search_text);
        mSearchButton = findViewById(R.id.search_button);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mSearchEdit.getText().toString();
                List<Address> list = null;

                try{
                    list = mGeocoder.getFromLocationName(str,10);
                }catch (IOException e) {
                    e.printStackTrace();
                }

                if(list != null) {
                    if (list.size() == 0) {
                        Log.d("MapsActivity","해당되는 주소 정보는 없습니다");
                    } else {
                        // 해당되는 주소로 인텐트 날리기
                        Address addr = list.get(0);
                        double lat = addr.getLatitude();
                        double lon = addr.getLongitude();

                        String sss = String.format("geo:%f,%f", lat, lon);
                        Log.d("MapsActivity","sss : " + sss + " /list :" + list.size() + " /list get:" + list.get(0).toString());
                        Log.d("MapsActivity","getAddressLine  : " + addr.getAddressLine(0) + " / addr : "  +addr.toString() + " / getLocality : " + addr.getLocality());

                        LatLng point = new LatLng(lat, lon);
                        // 마커 생성
                        MarkerOptions mOptions2 = new MarkerOptions();
                        mOptions2.title(addr.getFeatureName());
                        mOptions2.snippet(addr.getAddressLine(0));
                        mOptions2.position(point);
                        // 마커 추가
                        mMap.addMarker(mOptions2);
                        // 해당 좌표로 화면 줌
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

                    }
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(37.5668367, 126.9785728);
        LatLng sydney2 = new LatLng(33.263684, 126.613396);
        LatLng sydney3 = new LatLng(35.115645, 129.081077);
        LatLng sydney4 = new LatLng(34.353104, 126.747875);
        LatLng sydney5 = new LatLng(37.483862, 130.897962);
        LatLng sydney6 = new LatLng(37.774163, 128.928807);
        LatLng sydney7 = new LatLng(37.533277, 129.096906);
        LatLng sydney8 = new LatLng(37.533277 + (1 / 200d), 129.096906 + (1 / 200d));
        LatLng sydney9 = new LatLng(37.533277 + (2 / 200d), 129.096906 + (2 / 200d));
        LatLng sydney10 = new LatLng(37.533277 + (3 / 200d), 129.096906 + (3 / 200d));
        MarkerOptions markerOptions = new MarkerOptions();
        MarkerOptions markerOptions2 = new MarkerOptions();
        MarkerOptions markerOptions3 = new MarkerOptions();
        MarkerOptions markerOptions4 = new MarkerOptions();
        MarkerOptions markerOptions5 = new MarkerOptions();
        MarkerOptions markerOptions6 = new MarkerOptions();
        MarkerOptions markerOptions7 = new MarkerOptions();
        MarkerOptions markerOptions8 = new MarkerOptions();
        MarkerOptions markerOptions9 = new MarkerOptions();
        MarkerOptions markerOptions10 = new MarkerOptions();
        markerOptions.position(sydney).title("서울특별시 서울시청");
        markerOptions2.position(sydney2).title("제주특별시 서귀포시");
        markerOptions3.position(sydney3).title("부산특별시 해운대");
        markerOptions4.position(sydney4).title("전라남도 완도");
        markerOptions5.position(sydney5).title("경상북도 울릉군");
        markerOptions6.position(sydney6).title("강원도 강릉시");
        markerOptions7.position(sydney7).title("강원도 동해시");
        markerOptions8.position(sydney8).title("강원도 동해시 1 ");
        markerOptions9.position(sydney9).title("강원도 동해시 2 ");
        markerOptions10.position(sydney10).title("강원도 동해시 3 ");



        LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng(35.5, 127.4), new LatLng(37, 130));


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
        mMap.setMinZoomPreference(6.9f);
        mMap.setMaxZoomPreference(15.0f);
//        mMap.setLatLngBoundsForCameraTarget(latLngBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(35.798, 127.7)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6.9f));


        // 클러스터 매니저 생성
        ClusterManager<CustomClusterItem> mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        for(int i=0; i<10; i++) {
            double lat = latLngBounds.getCenter().latitude + (i / 200d);
            double lng = latLngBounds.getCenter().longitude + (i / 200d);
            mClusterManager.addItem(new CustomClusterItem(lat, lng, "House"+i));
        }
        mClusterManager.addItem(new CustomClusterItem(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude, markerOptions.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions2.getPosition().latitude, markerOptions2.getPosition().longitude, markerOptions2.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions3.getPosition().latitude, markerOptions3.getPosition().longitude, markerOptions3.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions4.getPosition().latitude, markerOptions4.getPosition().longitude, markerOptions4.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions5.getPosition().latitude, markerOptions5.getPosition().longitude, markerOptions5.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions6.getPosition().latitude, markerOptions6.getPosition().longitude, markerOptions6.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions7.getPosition().latitude, markerOptions7.getPosition().longitude, markerOptions7.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions8.getPosition().latitude, markerOptions8.getPosition().longitude, markerOptions8.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions9.getPosition().latitude, markerOptions9.getPosition().longitude, markerOptions9.getTitle()));
        mClusterManager.addItem(new CustomClusterItem(markerOptions10.getPosition().latitude, markerOptions10.getPosition().longitude, markerOptions10.getTitle()));

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<CustomClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<CustomClusterItem> cluster) {
                Log.d("mClusterManager", "zoomFlag : " + zoomFlag);
                if(zoomFlag == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 9));
                }else if(zoomFlag == 1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 10));
                }else if(zoomFlag == 2) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 11));
                }else if(zoomFlag == 3) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 12));
                }else if(zoomFlag == 4) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), 13));
                    zoomFlag = 0;
                }

                zoomFlag++;
                return false;
            }
        });

        //mMap.getUiSettings().setAllGesturesEnabled(false);
    }
}

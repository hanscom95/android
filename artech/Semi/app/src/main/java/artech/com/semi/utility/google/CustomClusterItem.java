package artech.com.semi.utility.google;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by moon on 2018-06-22.
 */

public class CustomClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;

    public CustomClusterItem(LatLng position, String s) {
        mPosition = position;
        mTitle = s;
        mSnippet = null;
    }


    public CustomClusterItem(double lat, double lng, String s) {
        mPosition = new LatLng(lat, lng);
        mTitle = s;
        mSnippet = null;
    }

    public CustomClusterItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}

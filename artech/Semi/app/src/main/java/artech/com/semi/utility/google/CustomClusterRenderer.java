package artech.com.semi.utility.google;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import artech.com.semi.R;

/**
 * Created by moon on 2018-07-16.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

    private Context mContext;

    private final IconGenerator mClusterIconGenerator;

    private int  mDensity;

    public CustomClusterRenderer(Context context, GoogleMap map,
                             ClusterManager<CustomClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        mClusterIconGenerator = new IconGenerator(mContext);
        mClusterIconGenerator.setContentView(makeSquareTextView(mContext));
        mDensity = (int) context.getResources().getDisplayMetrics().density;

    }

    @Override
    protected void onBeforeClusterItemRendered(CustomClusterItem item,
                                               MarkerOptions markerOptions) {

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_point)).snippet(item.getSnippet()).title(item.getTitle());
    }


    // the oval shape of cluster icon size depends mostly on the text and padding size
    private SquareTextView makeSquareTextView(Context context) {
        SquareTextView squareTextView = new SquareTextView(context);
        // added the following 3 lines to change text size, color and bold
        // I make the text smaller, color and bold is required since we removed R.style.amu_ClusterIcon_TextAppearance
        squareTextView.setTextSize(40);
        squareTextView.setTextColor(Color.WHITE);
        squareTextView.setTypeface(null, Typeface.BOLD);
        squareTextView.setGravity(Gravity.CENTER);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        squareTextView.setLayoutParams(layoutParams);
        squareTextView.setId(R.id.amu_text);
        int twelveDpi = 12 * mDensity;
        // initial is 12dp, I resize the round shape padding to 6dp
        squareTextView.setPadding(twelveDpi , twelveDpi , twelveDpi , twelveDpi );
        return squareTextView;
    }

    @Override
    protected void onClusterItemRendered(CustomClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<CustomClusterItem> cluster, MarkerOptions markerOptions){

//        final Drawable clusterIcon = mContext.getResources().getDrawable(R.mipmap.img_bubblesizem);
//        clusterIcon.setColorFilter(mContext.getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);

//        mClusterIconGenerator.setBackground(clusterIcon);
        mClusterIconGenerator.setBackground(mContext.getResources().getDrawable(R.mipmap.img_bubblesizem));
        mClusterIconGenerator.setTextAppearance(R.style.CustomTextAppearance);

        //modify padding for one or two digit numbers
        int twelveDpi = 14 * mDensity;
        if (cluster.getSize() < 10) {
            mClusterIconGenerator.setContentPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        }
        else {
            mClusterIconGenerator.setContentPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        }


//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_bubblesizem));
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster
                .getSize()));

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }
}
package artech.com.semi.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import artech.com.semi.R;

/**
 * Created by moon on 2018-06-27.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
//    private Integer [] images = {R.mipmap.img_appbanner, R.mipmap.img_photo_1, R.mipmap.img_profilephoto};
    private Bitmap[] bitmaps;

    public ViewPagerAdapter (Context context, Bitmap[] bitmaps){
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
//        return images.length;
        return bitmaps.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_banner, null);
        ImageView imageView = view.findViewById(R.id.banner_img);
//        imageView.setImageResource(images[position]);
        imageView.setImageBitmap(bitmaps[position]);

        ViewGroup vp = container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }


}

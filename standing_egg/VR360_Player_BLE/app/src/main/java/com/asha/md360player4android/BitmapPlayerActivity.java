package com.asha.md360player4android;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.asha.md360player4android.vrlib.MDVRLibrary;
import com.asha.md360player4android.vrlib.texture.MD360BitmapTexture;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class BitmapPlayerActivity extends MD360PlayerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancelBusy();
    }

    private Target mTarget;// keep the reference for picasso.

    private void loadImage(Uri uri, final MD360BitmapTexture.Callback callback){
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                callback.texture(bitmap);
                cancelBusy();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getApplicationContext()).load(uri).into(mTarget);
    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        MDVRLibrary.address(mBleAddress);
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .address(mBleAddress)
                .bitmap(new MDVRLibrary.IBitmapProvider() {
                    @Override
                    public void onProvideBitmap(final MD360BitmapTexture.Callback callback) {
                        loadImage(getUri(),callback);
                    }
                })
                .gesture(new MDVRLibrary.IGestureListener() {
                    @Override
                    public void onClick(MotionEvent e) {
                        Toast.makeText(BitmapPlayerActivity.this, "onClick!", Toast.LENGTH_SHORT).show();
                    }
                })
                .pinchEnabled(true)
                .build(R.id.surface_view1,R.id.surface_view2);
    }
}

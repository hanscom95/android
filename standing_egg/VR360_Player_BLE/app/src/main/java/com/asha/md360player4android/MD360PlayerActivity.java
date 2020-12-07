package com.asha.md360player4android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.asha.md360player4android.vrlib.MDVRLibrary;

public abstract class MD360PlayerActivity extends Activity {
    private static Context mContext;
    private static int view = 0;
    public static String mBleAddress;

    public static void startVideo(Context context, int flag){
        mContext = context;
        view = 0;
        start(context, flag, VideoPlayerActivity.class);
    }

    public static void startVideo(Context context, int flag, String address){
        mContext = context;
        view = 0;
        mBleAddress = address;
        start(context, flag, VideoPlayerActivity.class);
    }

    public static void startBitmap(Context context, Uri uri){
        mContext = context;
        view = 2;
        start(context, uri, BitmapPlayerActivity.class);
    }

    public static void startBitmap(Context context, Uri uri, String address){
        mContext = context;
        view = 2;
        mBleAddress = address;
        start(context, uri, BitmapPlayerActivity.class);
    }

    private static void start(Context context, Uri uri, Class<? extends Activity> clz){
        Intent i = new Intent(context,clz);


        Log.e("URL" , " urls ==== > " + uri);
        Log.e("URL" , " urls ==== > " + uri.toString());
        Log.e("URL" , " addr ==== > " + mBleAddress);
        if(mBleAddress != null) {
            i.putExtra("addr", mBleAddress);
        }
        i.setData(uri);
        context.startActivity(i);
    }

    private static void start(Context context, int video, Class<? extends Activity> clz){
        Intent i = new Intent(context,clz);


        Log.e("URL" , " urls ==== > " + video);
        Log.e("URL" , " urls ==== > " + video);
        Log.e("URL" , " addr ==== > " + mBleAddress);
        if(mBleAddress != null) {
            i.putExtra("addr", mBleAddress);
        }
        i.setFlags(video);
        context.stopService(i);
        context.startActivity(i);
    }

    private MDVRLibrary mVRLibrary;
    private Spinner sensorSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set content view
        setContentView(R.layout.activity_md_multi);

        // init VR Library
        mVRLibrary = createVRLibrary();



        Log.e("MD360Player" , " mBleAddress ==== > " + mBleAddress);
        if (mBleAddress != null){
            mVRLibrary.address(mBleAddress);
        }

        // interactive mode switcher
        final Button interactiveModeSwitcher = (Button) findViewById(R.id.button_interactive_mode_switcher);
        interactiveModeSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVRLibrary.switchInteractiveMode(MD360PlayerActivity.this);
                updateInteractiveModeText(interactiveModeSwitcher);
            }
        });
        updateInteractiveModeText(interactiveModeSwitcher);

        // display mode switcher
        final Button displayModeSwitcher = (Button) findViewById(R.id.button_display_mode_switcher);
        displayModeSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVRLibrary.switchDisplayMode(MD360PlayerActivity.this);
                updateDisplayModeText(displayModeSwitcher);
            }
        });
        updateDisplayModeText(displayModeSwitcher);

        findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVRLibrary.resetPinch();
                // reset touch
                if (mVRLibrary.getInteractiveMode() == MDVRLibrary.INTERACTIVE_MODE_TOUCH){
                    mVRLibrary.resetTouch();
                }
            }
        });

        String[] sensor_list = {
                "6-AXIS", "9-AXIS"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                sensor_list);

        sensorSelect = (Spinner) findViewById(R.id.sensor_select);
        sensorSelect.setAdapter(adapter);
        sensorSelect.setSelection(0);
        sensorSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mVRLibrary.sensorChange(position+7);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sensorSelect.setVisibility(View.INVISIBLE);

        if(view == 0) {
            String[] video_list = {
                    "flight", "drop", "racing", "Voltaj", "exid", "4MINUTE"
            };

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                    this,
                    R.layout.support_simple_spinner_dropdown_item,
                    video_list);

            final Spinner imgSelect = (Spinner) findViewById(R.id.image_select);
            imgSelect.setAdapter(adapter2);
            //imgSelect.setSelection(0);
            imgSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int video = 0;
                    if(position == 1 ){
                        video = 1; // drop
                        startVideo(view.getContext(), video);
                    }else if(position == 2 ){
                        video = 2; // drop2
                        startVideo(view.getContext(), video);
                    }else if(position == 3 ){
                        video = 3; // clash
                        startVideo(view.getContext(), video);
                    }else if(position == 4 ){
                        video = 4; // exid
                        startVideo(view.getContext(), video);
                    }else if(position == 5 ){
                        video = 5; // flight
                        startVideo(view.getContext(), video);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            imgSelect.setVisibility(View.GONE);
        }
        else if(view == 2) {
            String[] img_list = {
                    "default",  "house", "train", "mountain", "africa", "pension", "india"
            };

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                    this,
                    R.layout.support_simple_spinner_dropdown_item,
                    img_list);

            final Spinner imgSelect = (Spinner) findViewById(R.id.image_select);
            imgSelect.setAdapter(adapter2);
            imgSelect.setSelection(0);
            imgSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Uri uri;
                    if(position == 1 ){
                        uri = getDrawableUri(R.drawable.car);
                        finish();
                        start(mContext, uri, BitmapPlayerActivity.class);
                    }else if(position == 2 ){
                        uri = getDrawableUri(R.drawable.train);
                        finish();
                        start(mContext, uri, BitmapPlayerActivity.class);
                    }else if(position == 3 ){
                        uri = getDrawableUri(R.drawable.mountain);
                        finish();
                        start(mContext, uri, BitmapPlayerActivity.class);
                    }else if(position == 4 ){
                        uri = getDrawableUri(R.drawable.africa);
                        finish();
                        start(mContext, uri, BitmapPlayerActivity.class);
                    }else if(position == 5 ){
                        uri = getDrawableUri(R.drawable.pension);
                        finish();
                        start(mContext, uri, BitmapPlayerActivity.class);
                    }else if(position == 6 ){
                        uri = getDrawableUri(R.drawable.india);
                        finish();
                        start(mContext, uri, BitmapPlayerActivity.class);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        //mVRLibrary.sensorChange(2);
        sensorSelect.setSelection(1);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mVRLibrary.handleTouchEvent(event) || super.onTouchEvent(event);
    }

    private void sensorSelected(int position){
        switch (position){
            case MDVRLibrary.SENSOR_VIRTUAL_GYRO:

        }
    }

    private void updateDisplayModeText(Button button) {
        String text = null;
        switch (mVRLibrary.getDisplayMode()){
            case MDVRLibrary.DISPLAY_MODE_NORMAL:
                text = "NORMAL";
                break;
            case MDVRLibrary.DISPLAY_MODE_GLASS:
                text = "GLASS";
                break;
        }
        if (!TextUtils.isEmpty(text)) button.setText(text);
    }

    private void updateInteractiveModeText(Button button){
        String text = null;
        switch (mVRLibrary.getInteractiveMode()){
            case MDVRLibrary.INTERACTIVE_MODE_MOTION:
                text = "Tablet";
                if(sensorSelect != null) {
                    sensorSelect.setVisibility(View.INVISIBLE);
                }
                break;
            case MDVRLibrary.INTERACTIVE_MODE_TOUCH:
                text = "TOUCH";
                break;
            case MDVRLibrary.INTERACTIVE_MODE_SENSOR:
                text = "SGO100";
                if(sensorSelect != null) {
                    sensorSelect.setVisibility(View.VISIBLE);
                }
                //sensorSelect.setSelection(8);
                break;

        }
        if (!TextUtils.isEmpty(text)) button.setText(text);
    }

    abstract protected MDVRLibrary createVRLibrary();

    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
    }

    protected Uri getUri() {
        Intent i = getIntent();
        if (i == null || i.getData() == null){
            return null;
        }
        return i.getData();
    }

    protected int getFlag() {
        Intent i = getIntent();
        if (i == null || i.getFlags() == 0){
            return 0;
        }
        return i.getFlags();
    }

    public void cancelBusy(){
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    private Uri getDrawableUri(@DrawableRes int resId){
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }
}
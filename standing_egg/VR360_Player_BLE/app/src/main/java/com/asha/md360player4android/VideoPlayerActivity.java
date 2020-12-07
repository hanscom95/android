package com.asha.md360player4android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.asha.md360player4android.vrlib.MDVRLibrary;

import tv.danmaku.ijk.media.player.IMediaPlayer;


public class VideoPlayerActivity extends MD360PlayerActivity {

    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();

    String mAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("VideoPlayerActivity", "start : " + mAddress);
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                cancelBusy();
            }
        });

        //Uri uri = getUri();

        //if (uri != null){
        int flags = getFlag();
        if(flags == 0){
            //mMediaPlayerWrapper.openAssetFile(this, "flight.mp4");
            Uri uri = Uri.parse("file:///mnt/sdcard/vr/Flight.mp4");
            mMediaPlayerWrapper.openRemoteFile("file:///mnt/sdcard/vr/Flight.mp4");
            mMediaPlayerWrapper.prepare();
        }else if(flags == 1){
            //mMediaPlayerWrapper.openAssetFile(this, "drop.mp4");
            mMediaPlayerWrapper.openRemoteFile("file:///mnt/sdcard/vr/drop.mp4");
            mMediaPlayerWrapper.prepare();
        }else if(flags == 2){
            //mMediaPlayerWrapper.openAssetFile(this, "lg_drop.mp4");
            mMediaPlayerWrapper.openRemoteFile("file:///mnt/sdcard/vr/gtr.mp4");
            mMediaPlayerWrapper.prepare();
        }if(flags == 3){
            //mMediaPlayerWrapper.openAssetFile(this, "clash_of_clan.mp4");
            mMediaPlayerWrapper.openRemoteFile("file:///mnt/sdcard/vr/Voltaj.mp4");
            mMediaPlayerWrapper.prepare();
        }if(flags == 4){
            //mMediaPlayerWrapper.openAssetFile(this, "exid.mp4");
            mMediaPlayerWrapper.openRemoteFile("file:///mnt/sdcard/vr/exid.mp4");
            mMediaPlayerWrapper.prepare();
        }if(flags == 5){
            //mMediaPlayerWrapper.openAssetFile(this, "trinity_360.mp4");
            mMediaPlayerWrapper.openRemoteFile("file:///mnt/sdcard/vr/4MINUTE.mp4");
            mMediaPlayerWrapper.prepare();
        }
    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        //startSocket();
        Intent intent = getIntent();
        mAddress = intent.getStringExtra("addr");
        Log.d("VideoPlayer", "mBleAddress : " + mAddress);
        MDVRLibrary.address(mAddress);
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .address(mAddress)
                .video(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mMediaPlayerWrapper.getPlayer().setSurface(surface);
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                        Toast.makeText(VideoPlayerActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .pinchEnabled(true)

                .build(R.id.surface_view1,R.id.surface_view2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayerWrapper.onDestroy();
    }





    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayerWrapper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayerWrapper.onResume();
    }




}

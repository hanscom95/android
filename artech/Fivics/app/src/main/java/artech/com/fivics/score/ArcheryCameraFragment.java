package artech.com.fivics.score;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import artech.com.fivics.R;
import artech.com.fivics.TCPClient;

public class ArcheryCameraFragment extends Fragment implements IVLCVout.Callback, LibVLC.HardwareAccelerationError, TextureView.SurfaceTextureListener {

    //기본 위젯 설정및 변수 설정
    private LibVLC libvlc;

    private MediaPlayer mMediaPlayer = null;

//    public String mFilePath = "/sdcard/practice/video.mp4";
    public static String mFilePath = "rtsp://192.168.0.0:554/ch1/main/av_stream";//"rtmp://192.168.1.88:1935/flash/11:admin:admin";//="rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
    //파일위치는 /sdcard/practice/video.mp4 다음과 같이 지정한다.

    private int mVideoWidth;
    private int mVideoHeight;

    private TextureView mTexture;

    Button mZoomInButton, mZoomOutButton;

    private TCPClient mTcpClient;
    connectTask mConnectTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("ArcheryCameraFragment", "onCreateView");
        return inflater.inflate(R.layout.fragment_camera2_video, container, false);
    }

//    private TextureView mTextureView;
    private Button mCameraButton;
    private TextView mCameraText;
    private TextView mCameraTimerText;

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

//        mTextureView = (TextureView) view.findViewById(R.id.texture);
//        mTextureView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mMediaPlayer == null) {
//                    createPlayer(mFilePath);
////                    mSeekBar.setMax((int) mMediaPlayer.getLength());
//                } else {
//                    int state = mMediaPlayer.getPlayerState();
//                    switch (state) {
//                        case 4:  // stopping
//                        case 3:  // playing
//                            mMediaPlayer.pause();
//                    }
//                }
//            }
//        });

        mCameraButton = (Button) view.findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer == null) {
                    createPlayer(mFilePath);
//                    mSeekBar.setMax((int) mMediaPlayer.getLength());
                } else {
                    int state = mMediaPlayer.getPlayerState();
                    switch (state) {
                        case 4:  // stopping
                        case 3:  // playing
                            mMediaPlayer.pause();
                    }
                }
            }
        });

//        mCameraImg = (ImageView) view.findViewById(R.id.camera_img);
        mCameraText = (TextView) view.findViewById(R.id.camera_text);
        mCameraTimerText = (TextView) view.findViewById(R.id.camera_timer_text);

        //Video on View
        mTexture = (TextureView) view.findViewById(R.id.texture);
//        mTexture.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mTexture.setSurfaceTextureListener(this);
        mTexture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (mMediaPlayer == null) {
                    createPlayer(mFilePath);
//                    mSeekBar.setMax((int) mMediaPlayer.getLength());
                } else {
                    int state = mMediaPlayer.getPlayerState();
                    switch (state) {
                        case 4:  // stopping
                        case 3:  // playing
                            mMediaPlayer.pause();
                    }
                }*/
            }
        });
//        mButtonVideo = (Button) view.findViewById(R.id.video);
//        mButtonVideo.setOnClickListener(this);
//        view.findViewById(R.id.info).setOnClickListener(this);

//        mCameraAsyncTask = new CameraAsyncTask();

        mZoomInButton = (Button) view.findViewById(R.id.zoomin_button);
        mZoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mConnectTask = new connectTask();
//                mConnectTask.execute("","");
                Log.d("ArcheryCameraFragment", "mZoomInButton");
//                mTcpClient.sendMessage("aaa");
//            NetworkThread02 = new udpSocketTask();
//            NetworkThread02.execute();
            }
        });
        mZoomOutButton = (Button) view.findViewById(R.id.zoomout_button);
        mZoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("ArcheryCameraFragment", "mZoomOutButton");
//                mConnectTask = new connectTask();
//                mConnectTask.execute("","");
//                mTcpClient.sendMessage("aaa");
//                mConnectTask = new connectTask();
//                mConnectTask.execute("","");
//                NetworkThread02 = new udpSocketTask();
//                NetworkThread02.execute();
            }
        });

        Log.d("ArcheryCameraFragment", "onViewCreated");



        if (bThreadSwitch01 == false) {

            bThreadSwitch01 = true;
            cThreadSwitch01 = true;



//            if (android.os.Build.VERSION.SDK_INT > 9) {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//            }

            // connect to the server
//            NetworkThread02 = new udpSocketTask();
//            NetworkThread02.execute();
        }

//        createPlayer(mFilePath);
    }


    @Override //VLC 레이아웃 설정
    public void onNewLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        if (width * height == 0)
            return;
        // store video size
        mVideoWidth = width;
        mVideoHeight = height;
        setSize(mVideoWidth, mVideoHeight);
        Log.d("ArcheryCameraFragment", "onNewLayout");
    }

    @Override
    public void onSurfacesCreated(IVLCVout ivlcVout) {
        Log.d("ArcheryCameraFragment", "onSurfacesCreated");
        if (mMediaPlayer == null) {
//            mFilePath = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
            createPlayer(mFilePath);
//                    mSeekBar.setMax((int) mMediaPlayer.getLength());
        } else {
            int state = mMediaPlayer.getPlayerState();
            switch (state) {
                case 4:  // stopping
                case 3:  // playing
                    mMediaPlayer.pause();
            }
        }
    }

    @Override
    public void onSurfacesDestroyed(IVLCVout ivlcVout) {
        Log.d("ArcheryCameraFragment", "onSurfacesDestroyed");
    }

    @Override  //하드웨어 가속 에러시 플레이어 종료
    public void eventHardwareAccelerationError() {
        releasePlayer();
        Log.e("error11111111111111" , "Error with hardware acceleration");
//        Toast.makeText(this, "Error with hardware acceleration", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d("ArcheryCameraFragment", "onSurfaceTextureAvailable");
        createPlayer(mFilePath);
    }

    @Override  //SurfaceTexture 화면(동영상 해상도 및 사이즈)에 따라 크기 변경
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d("ArcheryCameraFragment", "onSurfaceTextureSizeChanged");
        mVideoWidth = width;
        mVideoHeight = height;
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override  //SurfaceTexture 화면이 종료되었을때 종료
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d("ArcheryCameraFragment", "onSurfaceTextureDestroyed");
        if(!(mMediaPlayer == null))
            mMediaPlayer.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.d("ArcheryCameraFragment", "onSurfaceTextureUpdated");
    }

    public static ArcheryCameraFragment newInstance(String url) {
        if(url != null || !"".equals(url)) {
            mFilePath = url;
        }
        return new ArcheryCameraFragment();
    }

    //미디어 플레이어 리스너 클래스
    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<ArcheryCameraFragment> mOwner;

        //액티비티 변수를 받아오기 위하여 지정
        private MyPlayerListener(ArcheryCameraFragment owner) {
            mOwner = new WeakReference<>(owner);
            Log.d("ArcheryCameraFragment", "MyPlayerListener");
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            ArcheryCameraFragment player = mOwner.get();

            switch(event.type) {
                case MediaPlayer.Event.EndReached:
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
//                    player.mSeekBar.setMax((int) player.mMediaPlayer.getLength());
//                    player.mSeekBar.setOnSeekBarChangeListener(player.mSeekListener);
                    break;
                case MediaPlayer.Event.Paused:
                    break;
                case MediaPlayer.Event.Stopped:
                    break;
                case MediaPlayer.Event.PositionChanged:
//                    player.mSeekBar.setProgress((int)player.mMediaPlayer.getTime());
                default:
                    break;
            }
        }
    }

    private void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        if(mTexture == null)
            return;

        //화면사이즈
        int w = getActivity().getWindow().getDecorView().getWidth();
        int h = getActivity().getWindow().getDecorView().getHeight();

        //Orientation 계산
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        ViewGroup.LayoutParams lp = mTexture.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mTexture.setLayoutParams(lp);
        mTexture.invalidate();
    }

    //VLC 플레이어 실행
    public void createPlayer(String media) {
        releasePlayer();
        try {
            if (media.length() > 0) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), media, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
                toast.show();
            }

            // Create LibVLC
            ArrayList<String> options = new ArrayList<>();
            //options.add("--subsdec-encoding <encoding>");
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            libvlc = new LibVLC(options);
            libvlc.setOnHardwareAccelerationError(this);

            mTexture.setKeepScreenOn(true);

            // Create media player
            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);

            // Set up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mTexture);
            //vout.setSubtitlesView(mSurfaceSubtitles);
            vout.addCallback(this);
            vout.attachViews();

            Media m = new Media(libvlc, Uri.parse(media));
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error creating player!", Toast.LENGTH_LONG).show();
        }
    }

    //플레이어 종료
    private void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                mMediaPlayer.setTime(progress);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ArcheryCameraFragment", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ArcheryCameraFragment", "onPause");
        releasePlayer();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("ArcheryCameraFragment", "onDestroy");
        releasePlayer();
    }

    public View screenLayout() {
        if(mTexture != null) {
            return mTexture;
        }else {
            return null;
        }
    }

    public Bitmap screenShot() {
        if(mTexture != null) {
            return mTexture.getBitmap();
        }else {
            return null;
        }
    }

    public void updateInstance() {
        if(!(mMediaPlayer == null))
            releasePlayer();

        createPlayer(mFilePath);
    }

    public void setUrl(String url) {
        mFilePath = url;
    }



    boolean bThreadSwitch01 = false, cThreadSwitch01 = false;
    udpSocketTask NetworkThread02;

    DatagramPacket outPacket;
    DatagramPacket inPacket;
    DatagramSocket dSock;

    private class udpSocketTask  extends AsyncTask<Integer, Integer, Integer> {
        private final String serverIP = "192.168.2.169"; // ex: 192.168.1.31
        private final int serverPort = 8100; // ex: 48601

        InetAddress serverAddr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (dSock != null) {
                    dSock.close();
                }


                serverAddr = InetAddress.getByName(serverIP);
                dSock = new DatagramSocket(serverPort);

                Log.d("serveraddr", "getAddress : "  + serverAddr.getAddress() + "/ getHostAddress : "  + serverAddr.getHostAddress() + "/ getHostName : "  + serverAddr.getHostName() + "/ toString : "  + serverAddr.toString() + " / port : "  + dSock.getLocalPort());

                int len = 1024;
                byte[] data = new byte[len];
                outPacket = new DatagramPacket(data, data.length, serverAddr, serverPort);
                inPacket = new DatagramPacket(data, len);
                byte[] message = new byte[12];
                message[0] = (byte) 0x00;
                message[1] = (byte) 0x00;
                message[2] = (byte) 0x00;
                message[3] = (byte) 0x00;
                message[4] = (byte) 0x00;
                message[5] = (byte) 0x00;
                message[6] = (byte) 0x22;
                message[7] = (byte) 0x31;
                message[8] = (byte) 0x03;
                message[9] = (byte) 0x11;
                message[10] = (byte) 0x00;
                message[11] = (byte) 0x01;
//                message[1] = (byte) 0x80 | 0x40 | 0x20 | 0x10 | 0x02;
//                message[3] = 0;
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                outPacket.setData(message);
                dSock.connect(serverAddr, serverPort);
                dSock.setSoTimeout(1000);
                dSock.send(outPacket);

            }catch(UnknownHostException e){
                e.getStackTrace();
            }catch(SocketException se){
                se.getStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            Log.d("udpSocketTask", "doInBackground");
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("udpSocketTask", "onProgressUpdate");
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("udpSocketTask", "onCancelled");
            bThreadSwitch01 = false;

            if(dSock != null) {
                dSock.close();
            }else {
//                handler.sendEmptyMessage(0);
            }

            if(cThreadSwitch01==true){
                if(NetworkThread02 != null) {
                    bThreadSwitch01 = true;
                    NetworkThread02 = new udpSocketTask();
                    NetworkThread02.execute();
                }
            }
        }

    }



    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    Log.d("ArcheryCameraFragment", "connectTask messageReceived");
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("ArcheryCameraFragment", "connectTask onProgressUpdate");

        }
    }
}

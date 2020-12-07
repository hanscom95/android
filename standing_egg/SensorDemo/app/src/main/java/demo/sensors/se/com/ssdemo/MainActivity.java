package demo.sensors.se.com.ssdemo;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import demo.sensors.se.com.ssdemo.StnPacket.Float3;
import demo.sensors.se.com.ssdemo.StnPacket.Float4;
import demo.sensors.se.com.ssdemo.StnPacket.StnEggPacket;

public class MainActivity extends AppCompatActivity{

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;
    Fragment introFragment;
    NavigationView nvDrawer;
    static TextView textLog;


    private float mPreviousX = 0;
    private float mPreviousY = 0;

    static public String LOG_TAG = null; // This string is used to uniquely
    // identify Android log messages
    public LocalSensors localSensors = null; // Pointer to object for managing
    // input from sensors local to
    // your Android device
    public DataSelector dataSelector = null; // Pointer to object which selects
    // one of several different
    // sensor sources
    static private DataLogger dataLogger = null; // Messages to the transcript
    // window are logged through
    // this object
    public MyUtils myUtils = null; // This class is used as a "home" for misc.
    // utility functions
    //public Wifi wifi = null; // The Wifi class encapsulates Wifi boards from
    // Avnet, plus the WiFi communications for same

    public enum GuiState { // GuiState defines the different states that the
        // user interface can take on
        LOGGING, DEVICE, PANORAMA, GRAPH, NOISE
    }

    public enum DataSource { // DataSource defines the various "sources" of
        // sensor data
        STOPPED, LOCAL, WIFI, FIXED//, TOUCH
    }


    public enum Algorithm { // These are the choices of algorithms that are
        // supported by the embedded code
        NONE, VR_GYRO, MAG_ONLY, GYRO_ONLY, ACC_MAG, ACC_GYRO, NINE_AXIS, SIX_AXIS, THREE_AXIS
    }

    public enum DevelopmentBoard { // List of supported development boards. Note
        // that KL16Z is a placeholder.
        REV5, WIFI
    }

    // Centralized state variables
    public GuiState guiState = GuiState.DEVICE;
    public DataSource dataSource = DataSource.STOPPED;
    public Algorithm algorithm = Algorithm.NONE;
    public DevelopmentBoard developmentBoard = DevelopmentBoard.REV5;

    // some handy constants
    private final String GUI_STATE = "GuiState";
    private final String DATA_SOURCE = "DataSource";
    private final String ALGORITHM = "Algorithm";
    private final String DEVBOARD = "DevtBoard";

    public int statsSampleSize = 100; // This is the number of sensor samples
    // used to calculate sensor statistics
    public boolean statsOneShot = false; // control variable for the stats view
    static public TextView tv1 = null; // TextView variables are global pointers
    // to fields in the GUI which can be
   // static private TextView stsTextView = null; // access via various utility
    // functions.
    static private TextView console2 = null;
    static private TextView console3 = null;
    static private TextView console4 = null;
    //static private TextView numMsgsField = null;

    static public ScrollView loggingWindow = null;
    static private int fileLoggingEnabled = 0; // functional a boolean, but must
    // be int for messaging purposes
    private boolean absoluteRemoteView = false; // set to true to NOT take into
    // account Android device view
    // when using remote IMU
    public boolean legacyDumpEnabled = true; // set to true to enable legacy
    // dump

    static public boolean console2IsStale = true; // used to throttle console2
    // updates to protect
    // performance - debug
    static public boolean console3IsStale = true; // used to throttle console3
    // updates to protect
    // performance - virtual
    // gyro
    static public boolean console4IsStale = true; // used to throttle console4
    // updates to protect
    // performance - roll pitch
    // compass
    public boolean zeroPending = false; // handshaking control for "Zero"
    // function in the device view
    public boolean zeroed = false;
    public boolean splitScreen = false; // Boolean for turning on/off the split
    // screen mode
    public boolean dumpScreen = false; // Set to true to request render to
    // create a screen dump. Will be reset
    // to false by render after done.

    // Options Menu definitions
    private final int TOGGLE_MENU_ITEM = Menu.FIRST;
    private final int CLS_MENU_ITEM = TOGGLE_MENU_ITEM + 1;
    private final int IP_SELECT_MENU_ITEM = CLS_MENU_ITEM + 1;

    public final String PREF_NAME = "A_FSL_Sensor_Demo"; // String for
    // retrieving shared
    // preferences
    public SharedPreferences myPrefs; // Structure for preferences
    private float filterCoefficient = 0; // Used to control low pass filtering
    public ToneGenerator toneGenerator = null; // for sound effects

    static public MainActivity self = null;
    // The self pointer is used in the body of one of the
    // listener functions, where "this" points to the
    // listener, not the demo itself.


    // texture front/left/back/right/top/bottom
    // Definition for graphics files used to render various 3D displays
    public TextureCubeRenderer pcbRenderer = null;
    final int roomSurfaces[] = {R.drawable.front_wall, R.drawable.left_wall_with_door, R.drawable.back_wall,
            R.drawable.right_wall_with_door2, R.drawable.roof3, R.drawable.floor2};

    private boolean menuOnOff = true;

    final int pcbSurfaces[] = {R.drawable.se_front_0, R.drawable.se_left_0, R.drawable.se_back_0, R.drawable.se_right_0,
            R.drawable.se_top_0, R.drawable.se_bottom_0};

    // define standard dimensions for the graphics files above
    final float roomDimensions[] = {4.8f, 4.8f, 1.456f, 0.0f}; // width,
    // length,
    // height and Z
    // offset. Room
    // dimensions are
    // twice these
    // numbers
    final float pcbDimensions[] = {1.5f, 0.87f, 0.5f, -2.5f};
    protected TimedQuaternion quaternion = null;


    RealTimeNoiseChart rtn02;


    RelativeLayout noiseView;
    LinearLayout graphView;

    RealTimeChart rtc01;
    RealTimeChart rtc02;
    RealTimeChart rtc03;


    LinearLayout NISchartLayout, NISnoiseLayout;
    Button NISnoiseButton;


    TextView NISTexttest04, NISTexttest05, NISTexttest06;
    TextView NISTexttest10, NISTexttest11, NISTexttest12;
    TextView NISTexttest16, NISTexttest17, NISTexttest18;
    TextView NISTexttest22, NISTexttest23, NISTexttest24;


    private static LinearLayout chartLayout;
    private static LinearLayout tilLayout;

    TextView Texttest01, Texttest02, Texttest03, Texttest04, Texttest05, Texttest06;
    TextView Texttest07, Texttest08, Texttest09, Texttest10, Texttest11, Texttest12;
    TextView Texttest13, Texttest14, Texttest15, Texttest16, Texttest17, Texttest18;
    LinearLayout sgaLayout, bmaLayout, lisLayout, textLayout, checkLayout;
    static CheckBox intervalCheckbox, sgaCheckBox, bma250CheckBox, lis331dlhCheckBox;//, mpu6500CheckBox;


    private Boolean load = false;

Button startBtn ;
    RelativeLayout startLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.navigation_view);
    startBtn = (Button) findViewById(R.id.start) ;
        startLayout  = (RelativeLayout) findViewById(R.id.main_cor);

        textLog = (TextView)findViewById(R.id.textlog);



        noiseView  = (RelativeLayout) findViewById(R.id.noise_view);
        graphView = (LinearLayout) findViewById(R.id.graph_view);
        rtn02 = new RealTimeNoiseChart(this);

        LinearLayout NISrealTimeLayout = (LinearLayout)findViewById(R.id.noise_RealTimelayout);

        NISrealTimeLayout.addView(rtn02); // RealTimeChart �並 ȭ�鿡 �߰�
        rtn02.Initialize(700, 300);

        NISchartLayout = (LinearLayout)findViewById(R.id.noise_chartLayout);
        NISnoiseLayout = (LinearLayout)findViewById(R.id.noise_noiseLayout);

        NISnoiseButton = (Button)findViewById(R.id.noise_bt_ok2);

        NISnoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NISnoiseLayout.getVisibility() == View.GONE) {
                    NISnoiseLayout.setVisibility(View.VISIBLE);
                }else {
                    NISnoiseLayout.setVisibility(View.GONE);
                }

            }
        });

        //sga
        NISTexttest04 = (TextView)findViewById(R.id.noise_Texttest04);
        NISTexttest05 = (TextView)findViewById(R.id.noise_Texttest05);
        NISTexttest06 = (TextView)findViewById(R.id.noise_Texttest06);

        //bma
        NISTexttest10 = (TextView)findViewById(R.id.noise_Texttest10);
        NISTexttest11 = (TextView)findViewById(R.id.noise_Texttest11);
        NISTexttest12 = (TextView)findViewById(R.id.noise_Texttest12);

        //lis
        NISTexttest16 = (TextView)findViewById(R.id.noise_Texttest16);
        NISTexttest17 = (TextView)findViewById(R.id.noise_Texttest17);
        NISTexttest18 = (TextView)findViewById(R.id.noise_Texttest18);

        //mpu
        NISTexttest22 = (TextView)findViewById(R.id.noise_Texttest22);
        NISTexttest23 = (TextView)findViewById(R.id.noise_Texttest23);
        NISTexttest24 = (TextView)findViewById(R.id.noise_Texttest24);




        rtc01 = new RealTimeChart(this);
        rtc02 = new RealTimeChart(this);
        rtc03 = new RealTimeChart(this);

        LinearLayout realTimeLayout = (LinearLayout) findViewById(R.id.RealTimelayout);
        LinearLayout realTimeLayout2 = (LinearLayout) findViewById(R.id.RealTimelayout2);
        LinearLayout realTimeLayout3 = (LinearLayout) findViewById(R.id.RealTimelayout3);

        realTimeLayout.addView(rtc01);
        realTimeLayout2.addView(rtc02);
        realTimeLayout3.addView(rtc03);

        rtc01.Initialize(700, 200, 1);
        rtc02.Initialize(700, 200, 2);
        rtc03.Initialize(700, 200, 3);

        chartLayout = (LinearLayout) findViewById(R.id.chartLayout);
        tilLayout = (LinearLayout) findViewById(R.id.tiltLayout);
        sgaLayout = (LinearLayout) findViewById(R.id.sgaLayout);
        bmaLayout = (LinearLayout) findViewById(R.id.bmaLayout);
        lisLayout = (LinearLayout) findViewById(R.id.lisLayout);

        //sga
        Texttest01 = (TextView) findViewById(R.id.Texttest01);
        Texttest02 = (TextView) findViewById(R.id.Texttest02);
        Texttest03 = (TextView) findViewById(R.id.Texttest03);
        Texttest04 = (TextView) findViewById(R.id.Texttest04);
        Texttest05 = (TextView) findViewById(R.id.Texttest05);
        Texttest06 = (TextView) findViewById(R.id.Texttest06);

        //bma
        Texttest07 = (TextView) findViewById(R.id.Texttest07);
        Texttest08 = (TextView) findViewById(R.id.Texttest08);
        Texttest09 = (TextView) findViewById(R.id.Texttest09);
        Texttest10 = (TextView) findViewById(R.id.Texttest10);
        Texttest11 = (TextView) findViewById(R.id.Texttest11);
        Texttest12 = (TextView) findViewById(R.id.Texttest12);

        //lis
        Texttest13 = (TextView) findViewById(R.id.Texttest13);
        Texttest14 = (TextView) findViewById(R.id.Texttest14);
        Texttest15 = (TextView) findViewById(R.id.Texttest15);
        Texttest16 = (TextView) findViewById(R.id.Texttest16);
        Texttest17 = (TextView) findViewById(R.id.Texttest17);
        Texttest18 = (TextView) findViewById(R.id.Texttest18);

        textLayout = (LinearLayout) findViewById(R.id.textLayout);
        checkLayout = (LinearLayout) findViewById(R.id.checkLayout);

        intervalCheckbox = (CheckBox) findViewById(R.id.intervalCheckBox);
        sgaCheckBox = (CheckBox) findViewById(R.id.sgaCheckBox);
        bma250CheckBox = (CheckBox) findViewById(R.id.bma250CheckBox);
        lis331dlhCheckBox = (CheckBox) findViewById(R.id.lis331dlhCheckBox);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);

        setupDrawerContent(nvDrawer);


        myPrefs = getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        int orientationChoice = myPrefs.getInt("orientation", 0);
        if (orientationChoice == 1) {
            //int currentOrientation = getResources().getConfiguration().orientation; //가로모드 세로모드
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (savedInstanceState != null) {
            guiState = GuiState.values()[savedInstanceState.getInt(GUI_STATE)];
            dataSource = DataSource.values()[savedInstanceState.getInt(DATA_SOURCE)];
            algorithm = Algorithm.values()[savedInstanceState.getInt(ALGORITHM)];
            developmentBoard = DevelopmentBoard.values()[savedInstanceState.getInt(DEVBOARD)];
        }

        self = this;

        myUtils = new MyUtils(this); // Register utility class
        dataLogger = new DataLogger(this, logHandler);
        dataLogger.setPriority(3); // slightly lower than default
        dataLogger.setDaemon(true); // will cause thread to be killed when the
        // main app thread is killed
        dataLogger.start();
        localSensors = new LocalSensors(this);
        dataSelector = new DataSelector(this);

        LOG_TAG = getString(R.string.log_tag);

        loggingWindow = (ScrollView) findViewById(R.id.listingScrollView); // must
        tv1 = (TextView) findViewById(R.id.console1);
        console2 = (TextView) findViewById(R.id.console2);
        console3 = (TextView) findViewById(R.id.console3);
        console4 = (TextView) findViewById(R.id.console4);

        this.toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 33);


        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenRotation = display.getRotation();

        final GLSurfaceView pcbGlview = (GLSurfaceView) findViewById(R.id.pcb_glview);
        final GLSurfaceView roomGlview = (GLSurfaceView) findViewById(R.id.room_glview);

        pcbRenderer = new TextureCubeRenderer(this, screenRotation);
        pcbRenderer.addCube(pcbSurfaces, pcbDimensions, "board");
        pcbGlview.setRenderer(pcbRenderer);

        quaternion = new TimedQuaternion();

        pcbGlview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                switch (e.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - mPreviousX;
                        float dy = y - mPreviousY;
                        pcbRenderer.mAngleX += dx * pcbRenderer.TOUCH_SCALE_FACTOR;
                        pcbRenderer.mAngleY += dy * pcbRenderer.TOUCH_SCALE_FACTOR;
                        pcbGlview.requestRender();
                }
                mPreviousX = x;
                mPreviousY = y;
                return true;


            }
        });


        final TextureCubeRenderer roomRenderer = new TextureCubeRenderer(this, screenRotation);
        roomRenderer.addCube(roomSurfaces, roomDimensions, "room view");
        roomGlview.setRenderer(roomRenderer);

        roomGlview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                switch (e.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - mPreviousX;
                        float dy = y - mPreviousY;
                        roomRenderer.mAngleX += dx * roomRenderer.TOUCH_SCALE_FACTOR;
                        roomRenderer.mAngleY += dy * roomRenderer.TOUCH_SCALE_FACTOR;
                        roomGlview.requestRender();
                }
                mPreviousX = x;
                mPreviousY = y;
                return true;


            }
        });

        final android.app.ActionBar actionBar = getActionBar();

        LinearLayout graphicFrame = (LinearLayout) findViewById(R.id.graphicFrame);


        setFragment(1);



        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(0);

                run();

            }
        });

    }

    public void run(){

        if(!exeFlag01){
            try {

                ConnectivityManager conMan = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState(); // wifi

                if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {

                    WifiManager wm = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    DhcpInfo dhcpInfo = wm.getDhcpInfo() ;

                    clientIp = dhcpInfo.gateway;

                    ipAddress = String.format(

                            "%d.%d.%d.%d", (clientIp & 0xff), (clientIp >> 8 & 0xff),(clientIp >> 16 & 0xff),(clientIp >> 24 & 0xff));

                    if(ipAddress.equals(serverIps)){
                        exeFlag01=true;

                        if(bThreadSwitch01==false){

                            bThreadSwitch01=true;
                            cThreadSwitch01=true;

                            if(android.os.Build.VERSION.SDK_INT > 9) {

                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                                StrictMode.setThreadPolicy(policy);

                            }

                            NetworkThread02 = new udpSocketTask();
                            NetworkThread02.execute();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Check the server connection status", Toast.LENGTH_SHORT).show();
                        exeFlag01=false;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please check the wifi connection", Toast.LENGTH_SHORT).show();
                    exeFlag01=false;
                }
            } catch (NullPointerException e) {
            }
        }else{

            if(bThreadSwitch01==true){

                bThreadSwitch01=false;
                cThreadSwitch01=false;

                NetworkThread02.cancel(true);
//					NetworkThread01.cancel(true);
            }

            exeFlag01=false;
        }

    }
    boolean exeFlag01 = false;
    boolean  bThreadSwitch01 = false, cThreadSwitch01 = false;
    //socketTask NetworkThread01;
    udpSocketTask NetworkThread02;
    String ipAddress;
    int clientIp;
    int serverPorts = 48555;
    String serverIps = "192.168.1.118";

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        nvDrawer.bringToFront();
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                setFragment(1);
                setTitle(menuItem.getTitle());
                break;
            case R.id.vr_player:
                try{
                    ComponentName compName = new ComponentName("com.asha.md360player4android","com.asha.md360player4android.DemoActivity");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    startActivity(intent);
                }catch (Exception e){
                }

                break;
            case R.id.nav_help:
                setTitle(menuItem.getTitle());
                configureApplicationViews(GuiState.GRAPH);

                break;
            case R.id.nav_cube:
                setTitle(menuItem.getTitle());
                configureApplicationViews(GuiState.DEVICE);
                break;
            case R.id.nav_panorama:
                setTitle(menuItem.getTitle());
                configureApplicationViews(GuiState.PANORAMA);
                break;
            case R.id.nav_log:
                setTitle(menuItem.getTitle());
                configureApplicationViews(GuiState.LOGGING);
                break;
        }

        menuItem.setChecked(true);
        dlDrawer.closeDrawers();
    }


    boolean mainShow = false;

    public void setFragment(int flag) {


        if (flag == 0) {
            dlDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mainShow=false;
            startLayout.setVisibility(View.GONE);

            dataSource = DataSource.WIFI;
            algorithm = Algorithm.SIX_AXIS;
            nvDrawer.bringToFront();

            updateSensors(); // dataSource use
            configureApplicationViews(guiState);

        } else {
            dlDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mainShow= true;
            startLayout.setVisibility(View.VISIBLE);
            dataSource = DataSource.FIXED;
            algorithm = Algorithm.NONE;
            guiState = GuiState.DEVICE;

            updateSensors(); // dataSource use
            configureApplicationViews(guiState);

        }
    }


    static public void write(Boolean always, String str) {
        Message msg = Message.obtain();
        if (always) {
            msg.what = 1; // unconditional
        } else {
            msg.what = 2; // only posts if log window is visible
        }
        msg.obj = str;
        msg.arg1 = fileLoggingEnabled; // 0=no, 1=yes
        if (dataLogger != null) {
            dataLogger.getHandler().sendMessage(msg);
        } else {
            Log.e(LOG_TAG, "Null datalogger pointer found in write() function.\n");
        }
    }


    /**
     * utility library used to quickly determine whether or not we should be
     * sampling sensor data.
     *
     * @return true unless the current data source is specified to be FIXED or
     * STOPPED.
     */
    public boolean dataIsLive() {
        return ((dataSource != DataSource.FIXED) && (dataSource != DataSource.STOPPED)/* && (dataSource != DataSource.TOUCH)*/);
    }


    public boolean dualModeRequired() {
        boolean sts = (dataSource == DataSource.WIFI)
                && (guiState == GuiState.DEVICE) && (absoluteRemoteView == false);
        return (sts);
    }


    public boolean absoluteModeRequired() {
        boolean sts = (dataSource == DataSource.WIFI)
                && (absoluteRemoteView == true);
        return (sts);
    }

    public void setConsole2(String msg) {
        if (guiState == GuiState.DEVICE) {
            if (console2IsStale && (console2 != null)) {
                console2.setText(msg);
                console2IsStale = false;
            }
        }
    }

    public void clearConsole2() {
        if (console2 != null) {
            console2.setText("");
        }
    }

//    public void setConsole3(String msg) {
//        if (guiState == GuiState.DEVICE) {
//            // only visible in device view
//            if (console3IsStale && (console3 != null)) {
//                console3.setText(msg);
//                console3IsStale = false;
//            }
//        }
//    }
//
//    public void clearConsole3() {
//        if (console3 != null) {
//            console3.setText("");
//        }
//    }
//
//    public void setConsole4(String msg) {
//        if (guiState == GuiState.DEVICE) {
//            // only visible in device view
//            if (console4IsStale && (console4 != null)) {
//                console4.setText(msg);
//                console4IsStale = false;
//            }
//        }
//    }
//
//    public void clearConsole4() {
//        if (console4 != null) {
//            console4.setText("");
//        }
//    }

    public void makeConsolesStale() {
        console2IsStale = true;
        console3IsStale = true;
        console4IsStale = true;
    }


    public String appVersion() {
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Throwable t) {
            versionName = "unknown";
        }
        return (versionName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        boolean sts = true;
        //setSts(""); // clear status field

        int itemId = item.getItemId();
        if (itemId == R.id.action_stop) {
            dataSource = DataSource.STOPPED;
            algorithm = Algorithm.NONE;
        } else if (itemId == R.id.action_vr) {
            dataSource = DataSource.WIFI;
            algorithm = Algorithm.VR_GYRO;

        }  else if (itemId == R.id.action_6axis) {

           // wifi.reset();
            dataSource = DataSource.WIFI;
            algorithm = Algorithm.SIX_AXIS;
        }else if (itemId == R.id.action_9axis) {


           // wifi.reset();
            dataSource = DataSource.WIFI;
            algorithm = Algorithm.NINE_AXIS;
        } else if (itemId == R.id.action_fixed) {
            dataSource = DataSource.FIXED;
            algorithm = Algorithm.NONE;

        } else {
            sts = false;
        }

        updateSensors(); // dataSource use
        configureApplicationViews(guiState);

        return (sts);
    }


//    /**
//     * clears the logging window. Also sends a message to the dataLogger class
//     * telling it to close the log file (if open).
//     *
//     * @return tv1 pointer to the console TextView
//     */
//    public TextView cls() {
//        Message msg = Message.obtain(dataLogger.getHandler(), 3, 0, 0, null);
//        dataLogger.getHandler().sendMessage(msg); // clear the ArrayList backing
//        // the log window
//        if (tv1 != null) {
//            tv1.setText("");
//        }
//        return (tv1);
//    }

    public void configureConsoles(boolean show) {
        TextView console2 = (TextView) findViewById(R.id.console2); // console2
        TextView console3 = (TextView) findViewById(R.id.console3); // console3
        // = virtual
        // gyro
        TextView console4 = (TextView) findViewById(R.id.console4); // console4
        // = roll
        // pitch
        // compass
        boolean debugEnabled = myPrefs.getBoolean("enable_device_debug", false);
        boolean virtualGyroEnabled = myPrefs.getBoolean("enable_virtual_gyro", false);
        boolean rpcEnabled = myPrefs.getBoolean("enable_rpc", false);
        if (show && debugEnabled)
            console2.setVisibility(View.VISIBLE);
        else
            console2.setVisibility(View.GONE);
        if (show && virtualGyroEnabled)
            console3.setVisibility(View.VISIBLE);
        else
            console3.setVisibility(View.GONE);
        if (show && rpcEnabled)
            console4.setVisibility(View.VISIBLE);
        else
            console4.setVisibility(View.GONE);
    }

    /**
     * top level function used to configure visibility of various GUI
     * components.
     * <p/>
     * The application is state-based, and what is visible or not is a functions
     * of those states.
     *
     * @param guiState the state of the application which we are about to configure.
     */
    public void configureApplicationViews(GuiState guiState) {

        LinearLayout graphicFrame = (LinearLayout) findViewById(R.id.graphicFrame);
        GLSurfaceView pcbGlview = (GLSurfaceView) findViewById(R.id.pcb_glview);
        GLSurfaceView roomGlview = (GLSurfaceView) findViewById(R.id.room_glview);
        clearConsole2();
        this.guiState = guiState;
        switch (guiState) {
            case GRAPH:
                checkLayout.setVisibility(View.VISIBLE);
                textLayout.setVisibility(View.VISIBLE);
                if (sgaCheckBox.isChecked()) {
                    sgaLayout.setVisibility(View.VISIBLE);
                } else {
                    sgaLayout.setVisibility(View.GONE);
                }

                if (bma250CheckBox.isChecked()) {
                    bmaLayout.setVisibility(View.VISIBLE);
                } else {
                    bmaLayout.setVisibility(View.GONE);
                }

                if (lis331dlhCheckBox.isChecked()) {
                    lisLayout.setVisibility(View.VISIBLE);
                } else {
                    lisLayout.setVisibility(View.GONE);
                }
                pcbGlview.setVisibility(View.GONE);
                roomGlview.setVisibility(View.GONE);
                graphicFrame.setVisibility(View.GONE);
                loggingWindow.setVisibility(View.GONE);

                graphView.setVisibility(View.VISIBLE);
                noiseView.setVisibility(View.GONE);
                if (localSensors != null) {
                    localSensors.clear(); // zero out acc, mag & gyro settings
                }
                configureConsoles(false);

                break;
            case NOISE:

                noiseView.setVisibility(View.VISIBLE);
                graphView.setVisibility(View.GONE);
                pcbGlview.setVisibility(View.GONE);
                roomGlview.setVisibility(View.GONE);
                graphicFrame.setVisibility(View.GONE);
                loggingWindow.setVisibility(View.GONE);


                if (localSensors != null) {
                    localSensors.clear(); // zero out acc, mag & gyro settings
                }
                configureConsoles(false);


                break;
            case LOGGING:
                graphView.setVisibility(View.GONE);
                pcbGlview.setVisibility(View.GONE);
                roomGlview.setVisibility(View.GONE);
                graphicFrame.setVisibility(View.GONE);
                loggingWindow.setVisibility(View.VISIBLE);
                noiseView.setVisibility(View.GONE);

                if (localSensors != null) {
                    localSensors.clear(); // zero out acc, mag & gyro settings
                }
                configureConsoles(false);

                break;

            case DEVICE:
                graphView.setVisibility(View.GONE);
                pcbGlview.setVisibility(View.VISIBLE);
                roomGlview.setVisibility(View.GONE);
                graphicFrame.setVisibility(View.VISIBLE);

                noiseView.setVisibility(View.GONE);
                loggingWindow.setVisibility(View.GONE);


                configureConsoles(false);  //(this.dataSource == DataSource.REMOTE)

                break;
            case PANORAMA:
                graphView.setVisibility(View.GONE);
                pcbGlview.setVisibility(View.GONE);
                roomGlview.setVisibility(View.VISIBLE);
                graphicFrame.setVisibility(View.VISIBLE);
                loggingWindow.setVisibility(View.GONE);
                noiseView.setVisibility(View.GONE);


                configureConsoles(false);
                break;
        }
        updateSensors();
    }


    /**
     * Update both local and remote sensors based upon the current state of the
     * application. May also be called when returning from STOP mode.
     */
    public void updateSensors() {
        switch (dataSource) {
            case LOCAL:
                localSensors.run();

                pcbRenderer.selectCube(0);
                break;

            case WIFI:

                pcbRenderer.selectCube(0);
                break;

            case STOPPED:
            case FIXED:
                pcbRenderer.selectCube(0);
                break;
        }
        dataSelector.updateSelection();
    }


    public MyHandler logHandler = new MyHandler(this);
    static public Handler alertHandler = new Handler() {
        // Messages to this handler are sent by class Wifi.
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // the Logging Window needs an update
                MyUtils.alert("External WiFi not visible!", msg.obj.toString());
            }
        }
    };

    static private class MyHandler extends Handler {
        @SuppressWarnings("unused")
        private final WeakReference<MainActivity> myActivity;

        public MyHandler(MainActivity activity) {
            myActivity = new WeakReference<MainActivity>(activity);
            // this construct is used to help the JAVA garbage collector.
            // making the handler static and using a weak reference to the
            // activity is supposed to make it easier to recycle objects
            // that are no longer needed.
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) { // the Logging Window needs an update

                Log.i("MSGLOG", msg.obj.toString());
                tv1.setText(msg.obj.toString());
                loggingWindow.scrollTo(0, tv1.getBottom());
            }
            // arg1 = int upper = (int) numMsgsLoggedToFile/1024;
            // arg2 = int lower = (int) numMsgsLoggedToFile - 1024*upper;
            long numLogged = 1024 * msg.arg1 + msg.arg2;
            setNumMsgs(numLogged);
        }
    }

    static public void setNumMsgs(Long num) {
        //numMsgsField.setText(String.format(" (%d) ", num));
    }


    static public void setLog(String str ) {
        textLog.setText(str);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!load) configureApplicationViews(guiState); // Override default set in
    }



    /**
     * "Standard" Android life cycle startfunction. Saves primary application states.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // dumpStates("calling onSaveInstanceState()");
        savedInstanceState.putInt(GUI_STATE, guiState.ordinal());
        savedInstanceState.putInt(DATA_SOURCE, dataSource.ordinal());
        savedInstanceState.putInt(ALGORITHM, algorithm.ordinal());
        savedInstanceState.putInt(DEVBOARD, developmentBoard.ordinal());
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * "Standard" Android life cycle function. Disables Bluetooth if the
     * preferences have been set for that option. Also disables local (to the
     * Android device) sensors.
     */
    @Override
    public void onStop() {
        localSensors.stop();

        // Threads are NOT stopped, as the settings shown keep things working
        // after
        // accessing the preferences screen.
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * "Standard" Android life cycle function. We did not really need to
     * override it for this application, but doing so allows us to monitor
     * application life cycle events in the debugger.
     */
    @Override
    public void onPause() {
        super.onPause();

        if(bThreadSwitch01==true){

            bThreadSwitch01=false;
            cThreadSwitch01=false;

            NetworkThread02.cancel(true);
        }

        exeFlag01=false;
    }
    private class udpSocketTask  extends AsyncTask<Integer, Integer, Integer> {
        private final String serverIP = serverIps; // ex: 192.168.1.31
        private final int serverPort = serverPorts; // ex: 48601
        private int  accValueX = 0, accValueY = 0, accValueZ = 0;
        private int  gyroValueX = 0, gyroValueY = 0, gyroValueZ = 0;
        private int  magValueX = 0, magValueY = 0, magValueZ = 0;
        private int x = 0;

        InetAddress serverAddr;
        DatagramPacket outPacket;
        DatagramPacket inPacket;
        DatagramSocket dSock;

        List<Integer> NISaccValueX = new ArrayList<Integer>();
        List<Integer> NISaccValueY = new ArrayList<Integer>();
        List<Integer> NISaccValueZ = new ArrayList<Integer>();
        List<Integer> NISgyroValueX = new ArrayList<Integer>();
        List<Integer> NISgyroValueY = new ArrayList<Integer>();
        List<Integer> NISgyroValueZ = new ArrayList<Integer>();
        List<Integer> NISlisValueX = new ArrayList<Integer>();
        List<Integer> NISlisValueY = new ArrayList<Integer>();
        List<Integer> NISlisValueZ = new ArrayList<Integer>();
        List<Integer> NISmpuValueX = new ArrayList<Integer>();
        List<Integer> NISmpuValueY = new ArrayList<Integer>();
        List<Integer> NISmpuValueZ = new ArrayList<Integer>();

        float sgaNoiseX, sgaNoiseY, sgaNoiseZ, bmaNoiseX, bmaNoiseY, bmaNoiseZ, lisNoiseX, lisNoiseY, lisNoiseZ, mpuNoiseX, mpuNoiseY, mpuNoiseZ = 0;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                if(dSock != null) {
                    dSock.close();
                }

                serverAddr = InetAddress.getByName(serverIP);
                dSock = new DatagramSocket(serverPort);

                int len = 1024;
                byte[] data = new byte[len];
                outPacket = new DatagramPacket(data, data.length, serverAddr, serverPort);
                inPacket = new DatagramPacket(data, len);
                byte[] message = new byte[4];
                message[0] = (byte) 0xFE;
                message[1] = (byte) 0x80 | 0x40 | 0x20 | 0x10 | 0x02;
                message[2] = 0;
                message[3] = 0;
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
            x = 0;
            // TODO Auto-generated method stub
            while(bThreadSwitch01){
                try {
                    dSock.receive(inPacket);
                 //   dSock.setSoTimeout(5000);
                } catch (IOException e) {

                    e.printStackTrace();
                }

                String value = new String(inPacket.getData());



                Log.e("WIFI", "inPacket.getLength(): " + inPacket.getLength());


                byte[] resultBuffer = new byte[inPacket.getLength()];
                //ByteBuffer bytebuff = ByteBuffer.wrap(resultBuffer);
                //bytebuff.rewind();

                System.arraycopy(inPacket.getData(), 0, resultBuffer, 0, inPacket.getLength());



                int contentStart = 0, contentEnd = 0;

                for (int i = 0; i < inPacket.getLength() - 1; i++)
                {
                    if (resultBuffer[i] == 'S' && resultBuffer[i + 1] == 'E')
                    {
                        contentStart = i + 4;
                    }
                    if (resultBuffer[i] == '\r' && resultBuffer[i + 1] == '\n')
                    {
                        contentEnd = i-1;
                        break;
                    }
                }


                if(contentEnd > 0 ) {
                    Log.e("WIFI", "contentEnd index : " +contentEnd);
                    Log.e("WIFI", "contentStart index : " +contentStart);
                    Log.e("WIFI" , " Real Data length -- > " + (contentEnd - contentStart));

                    byte[] resultBuffer2 = new byte[contentEnd  - contentStart+1];
                    System.arraycopy(resultBuffer, contentStart, resultBuffer2, 0, contentEnd  - contentStart+1 );


                    try {
                        build = StnEggPacket.parseFrom(resultBuffer2).toBuilder();



                        if(algorithm == Algorithm.VR_GYRO) {

                        }
                        if(algorithm == Algorithm.SIX_AXIS) {
                        }


                        developmentBoard = MainActivity.DevelopmentBoard.WIFI;


                        try {
                            motion = build.getMotion();
                        } catch (NumberFormatException e) {
                            motion = 0;
                        }

                        Float3 accRaw = build.getAccRAWG();
                        accX = accRaw.getF1();
                        accY = accRaw.getF2();
                        accZ = accRaw.getF3();

                        Float3 gyroRaw = build.getGyrRAWDps();
                        gyroX = gyroRaw.getF1();
                        gyroY = gyroRaw.getF2();
                        gyroZ = gyroRaw.getF3();

                        Float3 magRaw = build.getMagRAWUT();
                        magX = magRaw.getF1();
                        magY = magRaw.getF2();
                        magZ = magRaw.getF3();


                        Float3 VGyro_Spd_rps = build.getVGYROSpeedRps();
                        VGyro_Speed1 = VGyro_Spd_rps.getF1();
                        VGyro_Speed2 = VGyro_Spd_rps.getF2();
                        VGyro_Speed3 = VGyro_Spd_rps.getF3();

                        Float3 mouse3D_euler = build.getMouse3DEuler();
                        Mouse3D_euler1 = mouse3D_euler.getF1();
                        Mouse3D_euler2 = mouse3D_euler.getF2();
                        Mouse3D_euler3 = mouse3D_euler.getF3();

                        int a, b, c, d;
                        switch (algorithm) {
                            case VR_GYRO:
                                Float4 vrGyro = build.getVGYROQuat();
                                quat[0] = vrGyro.getF1();
                                quat[1] = vrGyro.getF2();
                                quat[2] = vrGyro.getF3();
                                quat[3] = vrGyro.getF4();
                                quaternion.set(quat);
                                MainActivity.write(false, "VR GYRO Q0: " + quat[0] + ", Q1: " + quat[1] + ", Q2: " + quat[2] + ", Q3: " + quat[3] + "\n");
                                break;
                            case ACC_MAG:
                                break;
                            case ACC_GYRO:
                                break;

                            case SIX_AXIS:
                                Float4 six_axis = build.getKalman9AXISQuat();

                                quat[0] = six_axis.getF1();
                                quat[1] = six_axis.getF2();
                                quat[2] = six_axis.getF3();
                                quat[3] = six_axis.getF4();
                                quaternion.set(quat);
                                MainActivity.write(false, "6-Axis Q0: " + quat[0] + ", Q1: " + quat[1] + ", Q2: " + quat[2] + ", Q3: " + quat[3] + "\n");
                                //demo.write(false, "Sensor motion: " + motionString);
                                break;
                            case NINE_AXIS:
                                Float4 nine_axis = build.getKalman9AXISQuat();
                                quat[0] = nine_axis.getF1();
                                quat[1] = nine_axis.getF2();
                                quat[2] = nine_axis.getF3();
                                quat[3] = nine_axis.getF4();
                                quaternion.set(quat);
                                MainActivity.write(false, "9-Axis Q0: " + quat[0] + ", Q1: " + quat[1] + ", Q2: " + quat[2] + ", Q3: " + quat[3] + "\n");
                                break;
                            default:
                                break;
                        }



                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }



                }



                try {
                    int sgaValue01, sgaValue02, sgaValue03, sgaValue04, sgaValue05, sgaValue06;
                    int bmaValue01, bmaValue02, bmaValue03, bmaValue04, bmaValue05, bmaValue06;
                    int lisValue01, lisValue02, lisValue03, lisValue04, lisValue05, lisValue06;
                    int mpuValue01, mpuValue02, mpuValue03, mpuValue04, mpuValue05, mpuValue06;
//                if ("PKST".equals(value.substring(0, 4))) {
//                    // SGA100 X, Y, Z setting

                    accValueX = (int) (500*MainActivity.accX);
                    accValueY = (int) (500*MainActivity.accY);
                    accValueZ = (int) (500*MainActivity.accZ);

                    gyroValueX = (int) MainActivity.gyroX;
                    gyroValueY = (int) MainActivity.gyroY;
                    gyroValueZ = (int) MainActivity.gyroZ;


                    magValueX = (int) MainActivity.magX;
                    magValueY = (int) MainActivity.magY;
                    magValueZ = (int) MainActivity.magZ;


                    if (intervalCheckbox.isChecked()) {
                        if (!RealTimeChart.bDrawing) {
                            rtc01.addData(x++, accValueX + 200, gyroValueX + 80, magValueX - 80,0);
                            rtc02.addData(x, accValueY + 200, gyroValueY + 80, magValueY - 80,0);
                            rtc03.addData(x, accValueZ + 40, gyroValueZ - 80, magValueZ - 240,0);
                        }
                    } else {
                        if (!RealTimeChart.bDrawing) {
                            rtc01.addData(x++, accValueX, gyroValueX, magValueX, 0);
                            rtc02.addData(x, accValueY, gyroValueY, magValueY,0);
                            rtc03.addData(x,accValueZ ,gyroValueZ , magValueZ,0);
                        }
                    }
                }catch (Exception e){
                        e.printStackTrace();
                }


                try {
                    if(NISaccValueX.size() == 150) {// 4�ʿ� 4800byte data �޾ƿ´�. 75

                        sgaNoiseX = MainActivity.accX;
                        sgaNoiseY = MainActivity.accY ;
                        sgaNoiseZ = MainActivity.accY;
                        bmaNoiseX = MainActivity.gyroX;
                        bmaNoiseY = MainActivity.gyroY;
                        bmaNoiseZ = MainActivity.gyroZ;
                        lisNoiseX = MainActivity.magX;
                        lisNoiseY = MainActivity.magY;
                        lisNoiseZ = MainActivity.magZ;

                        if(!RealTimeChart.bDrawing){
                            rtn02.addData(0, (int)sgaNoiseX, (int)bmaNoiseX, (int)lisNoiseX, (int)lisNoiseX);
                            rtn02.addData(1, (int)sgaNoiseY, (int)bmaNoiseY, (int)lisNoiseY, (int)lisNoiseX);
                            rtn02.addData(2, (int)sgaNoiseZ, (int)bmaNoiseZ, (int)lisNoiseZ, (int)lisNoiseX);
                        }

                        publishProgress();

                        if(bThreadSwitch01==true){

                            bThreadSwitch01=false;
                            cThreadSwitch01=false;

                            NetworkThread02.cancel(true);
                        }
                        exeFlag01=false;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }







                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            float acc_magnitude = (float) Math.sqrt((accValueX * accValueX) + (accValueY * accValueY) + (accValueZ * accValueZ));
            float tempX = (float) (Math.asin(accValueX / acc_magnitude) * (180 / 3.14159265358979323846264338327));
            float tempY = (float) (Math.asin(accValueY / acc_magnitude) * (180 / 3.14159265358979323846264338327));
            float tempZ = (float) (Math.acos(accValueZ / acc_magnitude) * (180 / 3.14159265358979323846264338327));
            Texttest01.setText("X-axis : " + String.format("%.3f", accX));
            Texttest02.setText("Y-axis : " + String.format("%.3f", accY));
            Texttest03.setText("Z-axis : " + String.format("%.3f", accZ));

            float acc_magnitude2 = (float) Math.sqrt((gyroValueX * gyroValueX) + (gyroValueY * gyroValueY) + (gyroValueZ * gyroValueZ));
            float temp2X = (float) (Math.asin(gyroValueX / acc_magnitude2) * (180 / 3.14159265358979323846264338327));
            float temp2Y = (float) (Math.asin(gyroValueY / acc_magnitude2) * (180 / 3.14159265358979323846264338327));
            float temp2Z = (float) (Math.acos(gyroValueZ / acc_magnitude2) * (180 / 3.14159265358979323846264338327));
            Texttest07.setText("X-axis : " + gyroValueX);
            Texttest08.setText("Y-axis : " + gyroValueY);
            Texttest09.setText("Z-axis : " + gyroValueZ);

            float acc_magnitude3 = (float) Math.sqrt((magValueX * magValueX) + (magValueY * magValueY) + (magValueZ * magValueZ));
            float temp3X = (float) (Math.asin(magValueX / acc_magnitude3) * (180 / 3.14159265358979323846264338327));
            float temp3Y = (float) (Math.asin(magValueY / acc_magnitude3) * (180 / 3.14159265358979323846264338327));
            float temp3Z = (float) (Math.acos(magValueZ / acc_magnitude3) * (180 / 3.14159265358979323846264338327));
            Texttest13.setText("X-axis : " + magValueX);
            Texttest14.setText("Y-axis : " + magValueY);
            Texttest15.setText("Z-axis : " + magValueZ);

            if (x % 10 == 0) {
                Texttest04.setText(String.format("%.1f", tempX) + "");
                Texttest05.setText(String.format("%.1f", tempY) + "");
                Texttest06.setText(String.format("%.1f", tempZ) + "");

                Texttest10.setText(String.format("%.1f", temp2X) + "");
                Texttest11.setText(String.format("%.1f", temp2Y) + "");
                Texttest12.setText(String.format("%.1f", temp2Z) + "");

                Texttest16.setText(String.format("%.1f", temp3X) + "");
                Texttest17.setText(String.format("%.1f", temp3Y) + "");
                Texttest18.setText(String.format("%.1f", temp3Z) + "");

            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            bThreadSwitch01 = false;

            if(dSock != null) {
                dSock.close();
            }else {
                handler.sendEmptyMessage(0);
            }

            if(cThreadSwitch01==true){
                if(NetworkThread02 != null) {
                    bThreadSwitch01 = true;
                    NetworkThread02 = new udpSocketTask();
                    NetworkThread02.execute();
                }
            }
        }

        private Handler handler = new Handler() {
            public void hanleMessage(Message msg) {
                Toast.makeText(getApplicationContext(), "The wifi connection is unstable. Please reboot the server", Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            }
        };

    }

    int motion = 0;
    /**
     * "Standard" Android life cycle function. The call to updateSensors() in
     * this function is important to maintain continuity when returning from the
     * Preferences screen.
     */
    @Override
    public void onResume() {
        // dumpStates("calling onResume(), updating sensors.");
        if (!load) updateSensors(); // sensors are containerdisabled onStop(). This gets them back.
        super.onResume();
    }

    /**
     * "Standard" Android life cycle function. Restores previously saved
     * application states.
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            guiState = GuiState.values()[savedInstanceState.getInt(GUI_STATE)];
            dataSource = DataSource.values()[savedInstanceState.getInt(DATA_SOURCE)];
            algorithm = Algorithm.values()[savedInstanceState.getInt(ALGORITHM)];
            developmentBoard = DevelopmentBoard.values()[savedInstanceState.getInt(DEVBOARD)];
        }
    }

    void computeQuaternion(DemoQuaternion result, Algorithm algorithm) {
        switch (algorithm) {
            case VR_GYRO:

                DemoQuaternion qut2 = new DemoQuaternion();
                qut2.q0 = quat[0];
                qut2.q1 = quat[1];
                qut2.q2 = quat[2];
                qut2.q3 = quat[3];
                result.set(qut2);

                if(this.guiState == MainActivity.GuiState.PANORAMA) result.reverse();
                break;
            case NINE_AXIS:
                DemoQuaternion qut3 = new DemoQuaternion();
                qut3.q0 = quat[0];
                qut3.q1 = quat[1];
                qut3.q2 = quat[2];
                qut3.q3 = quat[3];
                result.set(qut3);

                if(this.guiState == MainActivity.GuiState.PANORAMA) result.reverse();
                break;
            case SIX_AXIS:
                DemoQuaternion qut = new DemoQuaternion();
                qut.q0 = quat[0];
                qut.q1 = quat[1];
                qut.q2 = quat[2];
                qut.q3 = quat[3];
                result.set(qut);

                if(this.guiState == MainActivity.GuiState.PANORAMA) result.reverse();
                break;
            default:
        }
    }

    public static float[] quat = new float[4];
    public static float accX, accY, accZ;
    public static float gyroX, gyroY, gyroZ;
    public static float magX, magY, magZ;
    public static float VGyro_Speed1, VGyro_Speed2, VGyro_Speed3;
    public static float Mouse3D_euler1, Mouse3D_euler2, Mouse3D_euler3;

    StnEggPacket.Builder build;


}




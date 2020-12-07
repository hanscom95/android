package com.asha.md360player4android.vrlib.strategy.interactive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.asha.md360player4android.ble.BluetoothLeService;
import com.asha.md360player4android.vrlib.common.VRUtil;
import com.asha.md360player4android.vrlib.MD360Director;
import com.asha.md360player4android.vrlib.MDVRLibrary;
import com.asha.md360player4android.vrlib.StnEggPkt;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UninitializedMessageException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static com.asha.md360player4android.ble.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static com.asha.md360player4android.ble.BluetoothLeService.ACTION_GATT_CONNECTED;
import static com.asha.md360player4android.ble.BluetoothLeService.ACTION_GATT_DISCONNECTED;
import static com.asha.md360player4android.ble.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED;
import static com.asha.md360player4android.ble.BluetoothLeService.EXTRA_CHARACTER_DATA;
import static com.asha.md360player4android.ble.BluetoothLeService.EXTRA_DATA;

/**
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SensorStrategy extends AbsInteractiveStrategy {
    private static final String TAG = "MotionStrategy";
    private int mDeviceRotation;
    private float[] mSensorMatrix = new float[16];
    private boolean mRegistered = false;
    private Boolean mIsSupport = null;
    Boolean is6KlmanAxis = false;
    Boolean is9KlmanAxis = true;
    int mStatus;


    Activity mActivity;

    boolean exeFlag01 = false;
    boolean  bThreadSwitch01 = false, cThreadSwitch01 = false;
    //socketTask NetworkThread01;
    udpSocketTask NetworkThread02;
    String ipAddress;
    int clientIp;
    int serverPorts = 48555;
    String serverIps = "192.168.1.118";



    int VRVYRO = 1;
    int SIXAXIS = 2;
    int NINEAXIS = 3;


    public SensorStrategy(InteractiveModeManager.Params params) {
        super(params);
        mStatus = params.mStatus;
        mDeviceAddress = params.mAddress;

        if(mDeviceAddress != null) {
            mStatus = 1;
        }
//        Log.d("SensorStrategy" , "mStatus : " + mStatus +"/// mDeviceAddress : " + mDeviceAddress);
    }

    @Override
    public void onResume(Context context) {
       // registerSensor(context);
    }

    @Override
    public void onPause(Context context) {
        if(mStatus == 0) {
            //unregisterSensor(context);
            if (bThreadSwitch01 == true) {

                bThreadSwitch01 = false;
                cThreadSwitch01 = false;

                NetworkThread02.cancel(true);
            }

            exeFlag01 = false;
        }else {
            if(mBluetoothLeService != null && mServiceConnection !=null) {
                if(mGattUpdateReceiver != null) {
                    mActivity.getApplicationContext().unregisterReceiver(mGattUpdateReceiver);
                }

                mBluetoothLeService.disconnect();
                mBluetoothLeService.close();
                mActivity.getApplicationContext().unbindService(mServiceConnection);
//            mBluetoothLeService.unbindService(mServiceConnection);
                mBluetoothLeService = null;
            }
        }
    }

    @Override
    public boolean handleDrag(int distanceX, int distanceY) {


//        for (MD360Director director : getDirectorList()){
//            director.setDeltaX(director.getDeltaX() - distanceX / sDensity * sDamping);
//            director.setDeltaY(director.getDeltaY() - distanceY / sDensity * sDamping);
//        }

//
//        VRUtil.sensorRotationVector2Matrix( accX,accY,quat, mDeviceRotation, mSensorMatrix);
//        for (MD360Director director : getDirectorList()){
//            for(int i =0; i < 16; i++)
//                Log.d("SensorStrategy" , "matrix "+i+ ": " + mSensorMatrix[i]);
//            director.updateSensorMatrix(mSensorMatrix);
//            // if (mDisplayMode == DISPLAY_MODE_NORMAL) break;
//        }
//



        return false;
    }

    @Override
    public void on(Activity activity) {
        mDeviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        mActivity = activity;

        if(mStatus == 0) {
            startSocket();
        }else {
            /// 여기서부터 작업 시작!!!!!
            bleDeviceControl(mDeviceAddress);
        }


        for (MD360Director director : getDirectorList()){
            director.reset();
        }
    }

    @Override
    public void off(Activity activity) {
        if(mStatus == 0) {
            if (bThreadSwitch01 == true) {

                bThreadSwitch01 = false;
                cThreadSwitch01 = false;

                NetworkThread02.cancel(true);
            }

            exeFlag01 = false;

//        unregisterSensor(activity);
        }else {
            if(mBluetoothLeService != null && mServiceConnection !=null) {
                if(mGattUpdateReceiver != null) {
                    mActivity.getApplicationContext().unregisterReceiver(mGattUpdateReceiver);
                }

                mBluetoothLeService.disconnect();
                mBluetoothLeService.close();
                mActivity.getApplicationContext().unbindService(mServiceConnection);
                mBluetoothLeService = null;
            }
        }
    }

    @Override
    public boolean isSupport(Activity activity) {
//        if (mIsSupport == null){
//            SensorManager mSensorManager = (SensorManager) activity
//                    .getSystemService(Context.SENSOR_SERVICE);
//            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//            mIsSupport = (sensor != null);
//        }
//        return mIsSupport;

        return true;
    }

//    protected void registerSensor(Context context){
//        if (mRegistered) return;
//
//        SensorManager mSensorManager = (SensorManager) context
//                .getSystemService(Context.SENSOR_SERVICE);
//        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//
//        if (sensor == null){
//            Log.e(TAG,"TYPE_ROTATION_VECTOR sensor not support!");
//            return;
//        }
//
//        mSensorManager.registerListener(this, sensor, getParams().mMotionDelay);
//
//        mRegistered = true;
//    }

//    protected void unregisterSensor(Context context){
//        if (!mRegistered) return;
//
//        SensorManager mSensorManager = (SensorManager) context
//                .getSystemService(Context.SENSOR_SERVICE);
//        mSensorManager.unregisterListener(this);
//
//        mRegistered = false;
//    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.accuracy != 0){
//            if (getParams().mSensorListener != null){
//                getParams().mSensorListener.onSensorChanged(event);
//            }
//
//            int type = event.sensor.getType();
//            switch (type){
//                case Sensor.TYPE_ROTATION_VECTOR:
//                    VRUtil.sensorRotationVector2Matrix(event, mDeviceRotation, mSensorMatrix);
//                    for (MD360Director director : getDirectorList()){
//                        director.updateSensorMatrix(mSensorMatrix);
//                        // if (mDisplayMode == DISPLAY_MODE_NORMAL) break;
//                    }
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        if (getParams().mSensorListener != null){
//            getParams().mSensorListener.onAccuracyChanged(sensor,accuracy);
//        }
//    }

    public void startSocket(){
        Log.d("SensorStrategy", "startSocket");
        if(!exeFlag01){
            try {

                ConnectivityManager conMan = (ConnectivityManager)mActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState(); // wifi

                if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {

                    //Toast.makeText(getApplicationContext(), "�������̿���", Toast.LENGTH_SHORT).show();

                    WifiManager wm = (WifiManager)mActivity.getApplicationContext().getSystemService("wifi");

                    DhcpInfo dhcpInfo = wm.getDhcpInfo() ;

                    clientIp = dhcpInfo.gateway;

                    ipAddress = String.format(

                            "%d.%d.%d.%d", (clientIp & 0xff), (clientIp >> 8 & 0xff),(clientIp >> 16 & 0xff),(clientIp >> 24 & 0xff));

                    if(ipAddress.equals(serverIps)) {
                        exeFlag01 = true;

                        if (bThreadSwitch01 == false) {

                            bThreadSwitch01 = true;
                            cThreadSwitch01 = true;

                            if (android.os.Build.VERSION.SDK_INT > 9) {

                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                                StrictMode.setThreadPolicy(policy);

                            }


                            NetworkThread02 = new udpSocketTask();
                            NetworkThread02.execute();
                        }
                    }

                }else{
                    Toast.makeText(mActivity.getApplicationContext(), "Please check the wifi connection", Toast.LENGTH_SHORT).show();
                    exeFlag01=false;
                }
            } catch (NullPointerException e) {
            }
        }else{

            if(bThreadSwitch01==true){

                bThreadSwitch01=false;
                cThreadSwitch01=false;

                NetworkThread02.cancel(true);
            }

            exeFlag01=false;
        }

    }


    private class udpSocketTask  extends AsyncTask<Integer, Integer, Integer> {
        private final String serverIP = serverIps; // ex: 192.168.1.31
        private final int serverPort = serverPorts; // ex: 48601
        private int  sgaValueX = 0, sgaValueY = 0, sgaValueZ = 0;
        private int  bmaValueX = 0, bmaValueY = 0, bmaValueZ = 0;
        private int  bmaCValueX = 0, bmaCValueY = 0, bmaCValueZ = 0;
        private int  lisValueX = 0, lisValueY = 0, lisValueZ = 0;
        private int  mpuValueX = 0, mpuValueY = 0, mpuValueZ = 0;
        private int x = 0;

        InetAddress serverAddr;
        DatagramPacket outPacket;
        DatagramPacket inPacket;
        DatagramSocket dSock;


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

                    try {

                        byte[] resultBuffer2 = new byte[contentEnd  - contentStart+1];
                        System.arraycopy(resultBuffer, contentStart, resultBuffer2, 0, contentEnd  - contentStart+1 );


                        build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer2);

                        accX = build.getAcceleroDataG().getF1();
                        accY = build.getAcceleroDataG().getF2();
                        accZ = build.getAcceleroDataG().getF3();

                        gyroX = build.getGyroDataDps().getF1();
                        gyroY = build.getGyroDataDps().getF2();
                        gyroZ = build.getGyroDataDps().getF3();

                        magX = build.getMagDataUT().getF1();
                        magY = build.getMagDataUT().getF2();
                        magZ = build.getMagDataUT().getF3();


                        VGyro_Speed1 = build.getVirtualGyroSpeedRps().getF1();
                        VGyro_Speed2 = build.getVirtualGyroSpeedRps().getF2();
                        VGyro_Speed3 = build.getVirtualGyroSpeedRps().getF3();

                        Mouse3D_euler1 = build.getMouse3DEuler().getF1();
                        Mouse3D_euler2 = build.getMouse3DEuler().getF2();
                        Mouse3D_euler3 = build.getMouse3DEuler().getF3();

                        switch (MDVRLibrary.SENSOR_MODE) {
                            case MDVRLibrary.SENSOR_VIRTUAL_GYRO:
                                quat[1] = build.getVirtualGyroQuat().getF1();
                                quat[2] = build.getVirtualGyroQuat().getF2();
                                quat[3] = build.getVirtualGyroQuat().getF3();
                                quat[0] = build.getVirtualGyroQuat().getF4();
//                                Log.d("SENSOR_VIRTUAL_GYRO", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                                break;
                            case MDVRLibrary.SENSOR_SIX_AXIS:
                                quat[1] = build.getKalman6AxesQuat().getF1();
                                quat[2] = build.getKalman6AxesQuat().getF2();
                                quat[3] = build.getKalman6AxesQuat().getF3();
                                quat[0] = build.getKalman6AxesQuat().getF4();
//                                Log.d("SENSOR_SIX_AXIS", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                                break;
                            case MDVRLibrary.SENSOR_NINE_AXIS:
                                quat[1] = build.getKalman9AxesQuat().getF1();
                                quat[2] = build.getKalman9AxesQuat().getF2();
                                quat[3] = build.getKalman9AxesQuat().getF3();
                                quat[0] = build.getKalman9AxesQuat().getF4();
//                                Log.d("SENSOR_NINE_AXIS", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                                break;
                            default:
                                break;
                        }

                        if(quat != null ){
                            VRUtil.sensorRotationVector2Matrix(quat,mSensorMatrix);
                            for (MD360Director director : getDirectorList()){
                                director.updateSensorMatrix(mSensorMatrix);
                            }
                        }




                    }   catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }   catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }


                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


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
               // Toast.makeText(getApplicationContext(), "The wifi connection is unstable. Please reboot the server", Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            }
        };

    }

    public static float[] quat = new float[4];
    public static float accX, accY, accZ;
    public static float gyroX, gyroY, gyroZ;
    public static float magX, magY, magZ;
    public static float VGyro_Speed1, VGyro_Speed2, VGyro_Speed3;
    public static float Mouse3D_euler1, Mouse3D_euler2, Mouse3D_euler3;

    StnEggPkt.StnEggPacket build;


    private BluetoothLeService.LocalBinder mLocalBinder;
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;

    // Code to manage Service lifecycle.    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            SensorStrategy.this.mBluetoothLeService = (((BluetoothLeService.LocalBinder) service)).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("mServiceConnection", "Unable to initialize Bluetooth");
                mActivity.finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };



    private void bleDeviceControl(String address) {
//        Log.d("bleDeviceControl", "address :" + address);
        Intent gattServiceIntent = new Intent(mActivity.getApplicationContext(), BluetoothLeService.class);
        if (mActivity.getApplicationContext().startService(gattServiceIntent) != null) {
            mActivity.getApplicationContext().bindService(gattServiceIntent, mServiceConnection, mActivity.getApplicationContext().BIND_AUTO_CREATE);
        }

        mActivity.getApplicationContext().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(address);
            Log.d("bleDeviceControl", "Connect request result=" + result);
        }

        Toast.makeText(mActivity, "Connecting Bluetooth.", Toast.LENGTH_LONG).show();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
//            Log.d("mGattUpdateReceiver", "onReceive : " + action);
            if (ACTION_GATT_CONNECTED.equals(action)) {
                Log.d("mGattUpdateReceiver", "ACTION_GATT_CONNECTED");
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d("mGattUpdateReceiver", "ACTION_GATT_DISCONNECTED");
                if(mBluetoothLeService != null && mServiceConnection !=null) {
                    if(mGattUpdateReceiver != null) {
                        mActivity.getApplicationContext().unregisterReceiver(mGattUpdateReceiver);
                    }

                    mBluetoothLeService.disconnect();
                    mBluetoothLeService.close();
                    mActivity.getApplicationContext().unbindService(mServiceConnection);
                    mBluetoothLeService = null;
                }

                bleDeviceControl(mDeviceAddress);
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d("mGattUpdateReceiver", "ACTION_GATT_SERVICES_DISCOVERED");
                // Show all the supported services and characteristics on the user interface.
                mBluetoothLeService.displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
//                byte[] data = intent.getByteArrayExtra(EXTRA_CHARACTER_DATA);
                byte[] data = intent.getByteArrayExtra(EXTRA_CHARACTER_DATA);
                serfProtocolCheck(data);
            } else if(EXTRA_CHARACTER_DATA.equals(action)) {
                Log.d("ACTION_DATA_AVAILABLE", "EXTRA_CHARACTER_DATA : " + intent.getStringExtra(EXTRA_DATA));
            }
        }
    };


    int bufferInt = 0;
    public byte[] data1, buffer,  startBuffer, endBuffer;
    public boolean confirmFalg = false;
    boolean flags = false;
    boolean seFlag = false;
    boolean meFlag = false;
    boolean epFlag = false;
    private void serfProtocolCheck(byte[] inputData) {
        if (true) {
            int startInt = 0, endInt = 0;
            if (inputData != null && inputData.length > 0) {
                try {
                    if (bufferInt == 0) {
                        buffer = new byte[140];
                        System.arraycopy(inputData, 0, buffer, 0, inputData.length);
                        bufferInt++;
                    } else if (bufferInt < 7) {
                        System.arraycopy(inputData, 0, buffer, 20 * bufferInt, inputData.length);
                        bufferInt++;
                    } else {
                        for (int i = 0; i < buffer.length; i++) {
                            if (buffer[i] == 'S' && i + 1 >= buffer.length) {
                                return;
                            }

                            if (buffer[i] == 'S' && buffer[i + 1] == 'E') {
                                seFlag = true;
                                startInt = i;
//                        Log.d("serfProtocolCheck", "# startInt : "  + startInt);
                            }
                            if (seFlag) {
                                if (buffer[i] == 'E' && buffer[i + 1] == 'P') {
                                    seFlag = false;
                                    epFlag = true;
                                    endInt = i + 2;
//                                Log.d("serfProtocolCheck", "# endInt : " + endInt);
                                    break;
                                }
                            }
                        }

                        if (epFlag) {
                            data1 = new byte[endInt - startInt];

                            for (int i = 0; i < endInt - startInt; i++) {
                                data1[i] = buffer[startInt + i];
                            }


                            byte[] data = data1;
                            int contentStart = 0, contentEnd = 0;
                            if (data != null && data.length > 0) {
                                for (int j = 0; j < data.length - 1; j++) {
                                    //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                                    if (data[j] == 'S' && data[j + 1] == 'E') {
                                        //20160804 old version(vr) contentStart = i + 4;
                                        contentStart = j + 2;
                                    }
                                    if (data[j] == 'E' && data[j + 1] == 'P') {
                                        //contentEnd = j - 2;
                                        contentEnd = j - 1;
                                        break;
                                    }
                                }
//                        Log.d("serfProtocolCheck", "contentStart : " + contentStart + "////contentEnd : " + contentEnd);

                                if (contentStart > endInt) {
                                    return;
                                }

                                byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                                System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
                                try {
                                    build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);

                                    accX = build.getAcceleroDataG().getF1();
                                    accY = build.getAcceleroDataG().getF2();
                                    accZ = build.getAcceleroDataG().getF3();

                                    gyroX = build.getGyroDataDps().getF1();
                                    gyroY = build.getGyroDataDps().getF2();
                                    gyroZ = build.getGyroDataDps().getF3();

                                    magX = build.getMagDataUT().getF1();
                                    magY = build.getMagDataUT().getF2();
                                    magZ = build.getMagDataUT().getF3();


                                    VGyro_Speed1 = build.getVirtualGyroSpeedRps().getF1();
                                    VGyro_Speed2 = build.getVirtualGyroSpeedRps().getF2();
                                    VGyro_Speed3 = build.getVirtualGyroSpeedRps().getF3();

                                    Mouse3D_euler1 = build.getMouse3DEuler().getF1();
                                    Mouse3D_euler2 = build.getMouse3DEuler().getF2();
                                    Mouse3D_euler3 = build.getMouse3DEuler().getF3();

                                    switch (MDVRLibrary.SENSOR_MODE) {
                                        case MDVRLibrary.SENSOR_VIRTUAL_GYRO:
                                            quat[1] = build.getVirtualGyroQuat().getF1();
                                            quat[2] = build.getVirtualGyroQuat().getF2();
                                            quat[3] = build.getVirtualGyroQuat().getF3();
                                            quat[0] = build.getVirtualGyroQuat().getF4();
//                                    Log.d("SENSOR_VIRTUAL_GYRO", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                                            break;
                                        case MDVRLibrary.SENSOR_SIX_AXIS:
                                            //if (!is6KlmanAxis) {
                                            if(false) {
                                                Log.d("SensorStrategy", "6kalman");
                                                StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
                                                builder.setInputCommandWho(4);
                                                builder.setInputCommandWhat(4);
                                                builder.setInputCommandValue(0);

                                                try {
                                                    byte[] encoded = new byte[builder.build().toByteArray().length + 5];
                                                    byte[] builderTobyte = builder.build().toByteArray();

                                                    encoded[0] = 'S';
                                                    encoded[1] = 'E';
                                                    int i;
                                                    for (i = 0; i < builderTobyte.length; i++) {
                                                        encoded[i + 2] = builderTobyte[i];
                                                    }
                                                    encoded[i + 2] = 'E';
                                                    encoded[i + 3] = '\r';
                                                    encoded[i + 4] = '\n';

                                                    mBluetoothLeService.bleWrite(encoded);

                                                } catch (UninitializedMessageException e) {
                                                    e.printStackTrace();
                                                }

                                                is6KlmanAxis = true;
                                                is9KlmanAxis = false;
                                            }

                                            quat[1] = build.getKalman6AxesQuat().getF1();
                                            quat[2] = build.getKalman6AxesQuat().getF2();
                                            quat[3] = build.getKalman6AxesQuat().getF3();
                                            quat[0] = build.getKalman6AxesQuat().getF4();
//                                    Log.d("SENSOR_SIX_AXIS", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                                            break;
                                        case MDVRLibrary.SENSOR_NINE_AXIS:
                                            //if (!is9KlmanAxis) {
                                            if(false) {
                                                Log.d("SensorStrategy", "9kalman");
                                                StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
                                                builder.setInputCommandWho(4);
                                                builder.setInputCommandWhat(4);
                                                builder.setInputCommandValue(2);

                                                try {
                                                    byte[] encoded = new byte[builder.build().toByteArray().length + 5];
                                                    byte[] builderTobyte = builder.build().toByteArray();

                                                    encoded[0] = 'S';
                                                    encoded[1] = 'E';
                                                    int i;
                                                    for (i = 0; i < builderTobyte.length; i++) {
                                                        encoded[i + 2] = builderTobyte[i];
                                                    }
                                                    encoded[i + 2] = 'E';
                                                    encoded[i + 3] = '\r';
                                                    encoded[i + 4] = '\n';

                                                    mBluetoothLeService.bleWrite(encoded);

                                                } catch (UninitializedMessageException e) {
                                                    e.printStackTrace();
                                                }

                                                is6KlmanAxis = false;
                                                is9KlmanAxis = true;
                                            }

                                            quat[1] = build.getKalman9AxesQuat().getF1();
                                            quat[2] = build.getKalman9AxesQuat().getF2();
                                            quat[3] = build.getKalman9AxesQuat().getF3();
                                            quat[0] = build.getKalman9AxesQuat().getF4();
//                                    Log.d("SENSOR_NINE_AXIS", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                                            break;
                                        default:
                                            break;
                                    }

                                    if (quat != null) {
                                        VRUtil.sensorRotationVector2Matrix(quat, mSensorMatrix);
                                        for (MD360Director director : getDirectorList()) {
                                            director.updateSensorMatrix(mSensorMatrix);
                                        }
                                    }
                                } catch (InvalidProtocolBufferException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        // init
                        buffer = null;
                        bufferInt = 0;
                        startInt = 0;
                        endInt = 0;
                        seFlag = false;
                        epFlag = false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }else {
            byte[] data2;
            if (inputData != null && inputData.length > 0) {
                if(inputData[0] == 'E' && inputData[1] == 'g' && inputData[2] == 'g') {
                    return;
                }

                byte[] test = new byte[1];
                for (int i = 0; i < inputData.length; i++) {
                    test[0] = inputData[i];
                    Log.d(TAG, "REAL RECEIVE MESSAGE ==>" + new String(test));
                    //ULog.d(TAG, "REAL RECEIVE MESSAGE ==>" + test);
                }

                byte[] ddd = new byte[2];
                ddd[0] = inputData[inputData.length - 2];
                ddd[1] = inputData[inputData.length - 1];
                //
                Log.d(TAG, "dd->>" + new String(ddd));

                if (!"\r\n".equals(new String(ddd))) {
                    data2 = inputData;
                    flags = true;

                    return;
                }


                byte[] d = new byte[2];
                d[0] = inputData[0];
                d[1] = inputData[1];

                Log.d(TAG, "d->>" + new String(d));

                String cha = new String(d);
                Log.d(TAG, "d2->>" + cha);


                //20160822 start
                try {
                    int contentStart = 0, contentEnd = 0;
                    if (inputData != null && inputData.length > 0) {
                        Log.d(TAG, "# Read characteristic 20160817: data 있음");

                        for (int i = 0; i < inputData.length - 1; i++) {
                            //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                            if (inputData[i] == 'S' && inputData[i + 1] == 'E') {
                                //20160804 old version(vr) contentStart = i + 4;
                                contentStart = i + 2;
                            }
                            if (inputData[i] == '\r' && inputData[i + 1] == '\n') {
                                contentEnd = i - 2;
                                break;
                            }
                        }
                        Log.d(TAG, "contentStart : " + contentStart + "////contentEnd : " + contentEnd);
                    }

                    if(contentStart == 0) {
                        return;
                    }

                    byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                    System.arraycopy(inputData, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);

                    build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);

                    accX = build.getAcceleroDataG().getF1();
                    accY = build.getAcceleroDataG().getF2();
                    accZ = build.getAcceleroDataG().getF3();

                    gyroX = build.getGyroDataDps().getF1();
                    gyroY = build.getGyroDataDps().getF2();
                    gyroZ = build.getGyroDataDps().getF3();

                    magX = build.getMagDataUT().getF1();
                    magY = build.getMagDataUT().getF2();
                    magZ = build.getMagDataUT().getF3();


                    VGyro_Speed1 = build.getVirtualGyroSpeedRps().getF1();
                    VGyro_Speed2 = build.getVirtualGyroSpeedRps().getF2();
                    VGyro_Speed3 = build.getVirtualGyroSpeedRps().getF3();

                    Mouse3D_euler1 = build.getMouse3DEuler().getF1();
                    Mouse3D_euler2 = build.getMouse3DEuler().getF2();
                    Mouse3D_euler3 = build.getMouse3DEuler().getF3();

                    switch (MDVRLibrary.SENSOR_MODE) {
                        case MDVRLibrary.SENSOR_VIRTUAL_GYRO:
                            quat[1] = build.getVirtualGyroQuat().getF1();
                            quat[2] = build.getVirtualGyroQuat().getF2();
                            quat[3] = build.getVirtualGyroQuat().getF3();
                            quat[0] = build.getVirtualGyroQuat().getF4();
//                                    Log.d("SENSOR_VIRTUAL_GYRO", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                            break;
                        case MDVRLibrary.SENSOR_SIX_AXIS:
                            if (!is6KlmanAxis) {
                                Log.d("SensorStrategy", "6kalman");
                                StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
                                builder.setInputCommandWho(4);
                                builder.setInputCommandWhat(4);
                                builder.setInputCommandValue(0);

                                try {
                                    byte[] encoded = new byte[builder.build().toByteArray().length + 5];
                                    byte[] builderTobyte = builder.build().toByteArray();

                                    encoded[0] = 'S';
                                    encoded[1] = 'E';
                                    int i;
                                    for (i = 0; i < builderTobyte.length; i++) {
                                        encoded[i + 2] = builderTobyte[i];
                                    }
                                    encoded[i + 2] = 'E';
                                    encoded[i + 3] = '\r';
                                    encoded[i + 4] = '\n';

                                    mBluetoothLeService.bleWrite(encoded);

                                } catch (UninitializedMessageException e) {
                                    e.printStackTrace();
                                }

                                is6KlmanAxis = true;
                                is9KlmanAxis = false;
                            }

                            quat[1] = build.getKalman6AxesQuat().getF1();
                            quat[2] = build.getKalman6AxesQuat().getF2();
                            quat[3] = build.getKalman6AxesQuat().getF3();
                            quat[0] = build.getKalman6AxesQuat().getF4();
//                                    Log.d("SENSOR_SIX_AXIS", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                            break;
                        case MDVRLibrary.SENSOR_NINE_AXIS:
                            if (!is9KlmanAxis) {
                                Log.d("SensorStrategy", "9kalman");
                                StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
                                builder.setInputCommandWho(4);
                                builder.setInputCommandWhat(4);
                                builder.setInputCommandValue(2);

                                try {
                                    byte[] encoded = new byte[builder.build().toByteArray().length + 5];
                                    byte[] builderTobyte = builder.build().toByteArray();

                                    encoded[0] = 'S';
                                    encoded[1] = 'E';
                                    int i;
                                    for (i = 0; i < builderTobyte.length; i++) {
                                        encoded[i + 2] = builderTobyte[i];
                                    }
                                    encoded[i + 2] = 'E';
                                    encoded[i + 3] = '\r';
                                    encoded[i + 4] = '\n';

                                    mBluetoothLeService.bleWrite(encoded);

                                } catch (UninitializedMessageException e) {
                                    e.printStackTrace();
                                }

                                is6KlmanAxis = false;
                                is9KlmanAxis = true;
                            }

                            quat[1] = build.getKalman9AxesQuat().getF1();
                            quat[2] = build.getKalman9AxesQuat().getF2();
                            quat[3] = build.getKalman9AxesQuat().getF3();
                            quat[0] = build.getKalman9AxesQuat().getF4();
//                                    Log.d("SENSOR_NINE_AXIS", "q1 : " + quat[1] + "///q2 : " + quat[2] + "///q3 : " + quat[3] + "///q0 : " + quat[0]);
                            break;
                        default:
                            break;
                    }

                    if (quat != null) {
                        VRUtil.sensorRotationVector2Matrix(quat, mSensorMatrix);
                        for (MD360Director director : getDirectorList()) {
                            director.updateSensorMatrix(mSensorMatrix);
                        }
                    }


                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                //20160822 end
            }
        }
    }


}

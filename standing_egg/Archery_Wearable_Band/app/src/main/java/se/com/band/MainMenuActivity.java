package se.com.band;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UninitializedMessageException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import se.com.band.activity.WalkRunActivity;
import se.com.band.ble.BluetoothLeService;
import se.com.band.heart.HeartActivity;
import se.com.band.motion.MotionActivity;
import se.com.band.goal.GoalSettingActivity;
import se.com.band.option.OptionActivity;
import se.com.band.option.ProfileActivity;
import se.com.band.option.TestActivity;
import se.com.band.sleep.SleepActivity;
import se.com.band.utility.Constants;
import se.com.band.utility.DBManager;
import se.com.band.utility.Preferences;
import se.com.band.utility.StnEggPkt;
import se.com.band.utility.ULog;

import static se.com.band.ble.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static se.com.band.ble.BluetoothLeService.ACTION_GATT_CONNECTED;
import static se.com.band.ble.BluetoothLeService.ACTION_GATT_DISCONNECTED;
import static se.com.band.ble.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED;
import static se.com.band.ble.BluetoothLeService.EXTRA_CHARACTER_DATA;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout mProfileLayout, mGoalLayout, mActivityLayout, mMotionLayout, mSleepLayout, mHeartRateLayout, mTestLayout, mOptionLayout, mLogoutLayout;

    Context mContext;
    Preferences mPreference;
    Constants mConstant;

    DBManager mDBManager;

    boolean testLogFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        mPreference = Preferences.getInstance(mContext);
        mDBManager = new DBManager(getApplicationContext(), "apex.db", null, 1);

        mProfileLayout = (RelativeLayout) findViewById(R.id.profile_layout);
        mGoalLayout = (RelativeLayout) findViewById(R.id.goal_layout);
        mActivityLayout = (RelativeLayout) findViewById(R.id.activity_layout);
        mMotionLayout = (RelativeLayout) findViewById(R.id.motion_layout);
        mSleepLayout = (RelativeLayout) findViewById(R.id.sleep_layout);
        mHeartRateLayout = (RelativeLayout) findViewById(R.id.heartrate_layout);
        mTestLayout = (RelativeLayout) findViewById(R.id.test_layout);
        mOptionLayout = (RelativeLayout) findViewById(R.id.option_layout);
        mLogoutLayout = (RelativeLayout) findViewById(R.id.logout_layout);

        mProfileLayout.setOnClickListener(this);
        mGoalLayout.setOnClickListener(this);
        mActivityLayout.setOnClickListener(this);
        mMotionLayout.setOnClickListener(this);
        mSleepLayout.setOnClickListener(this);
        mHeartRateLayout.setOnClickListener(this);
        mTestLayout.setOnClickListener(this);
        mOptionLayout.setOnClickListener(this);
        mLogoutLayout.setOnClickListener(this);

        initBleConnect();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.profile_layout:
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.goal_layout:
//                intent = new Intent(getApplicationContext(), GoalActivity.class);
                intent = new Intent(getApplicationContext(), GoalSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_layout:
                intent = new Intent(getApplicationContext(), WalkRunActivity.class);
                startActivity(intent);
                break;
            case R.id.motion_layout:
                intent = new Intent(getApplicationContext(), MotionActivity.class);
                startActivity(intent);
                break;
            case R.id.sleep_layout:
                intent = new Intent(getApplicationContext(), SleepActivity.class);
                startActivity(intent);
                break;
            case R.id.heartrate_layout:
                intent = new Intent(getApplicationContext(), HeartActivity.class);
                startActivity(intent);
                break;
            case R.id.test_layout:
                testLogFlag = true;
//                intent = new Intent(getApplicationContext(), TestActivity.class);
                intent = new Intent(getApplicationContext(), HelloActivity.class);
                startActivity(intent);
                break;
            case R.id.option_layout:
//                intent = new Intent(getApplicationContext(), NotificationActivity.class);
//                intent = new Intent(getApplicationContext(), UnregisterActivity.class);
                intent = new Intent(getApplicationContext(), OptionActivity.class);
//                intent = new Intent(getApplicationContext(), ArcherySettingActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_layout:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("LOG OUT");
                dialog.setMessage("Do you want to log out?");
                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPreference.resetPreferences();
                        mDBManager.dropDB();

                        Intent logoutIntent = new Intent(getApplicationContext(), IntroActivity.class);
                        startActivity(logoutIntent);
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                dialog.show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        testLogFlag = false;
        // TODO Auto-generated method stub
        super.onResume();
    }



    private String mDeviceAddress;
    private String mDeviceName;
    private static BluetoothLeService mBluetoothLeService;

    private void initBleConnect() {
        if(mPreference.getUserDeviceAdderess() != null && mPreference.getUserDeviceAdderess() != "") {
            bleDeviceControl(mPreference.getUserDeviceAdderess());
        }
    }

    private void bleDeviceControl(String address) {
        ULog.d("bleDeviceControl", "address :" + address);
        mDeviceAddress = address;
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        if (startService(gattServiceIntent) != null) {
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(address);
            ULog.d("bleDeviceControl", "Connect request result=" + result);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                ULog.e("mServiceConnection", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

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
            if (ACTION_GATT_CONNECTED.equals(action)) {
                mPreference.setDeviceConn(true);
//                mConnected = true;
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
                mPreference.setDeviceConn(false);
                mBluetoothLeService = null;
                Toast.makeText(mContext, "The connection to the device has been lost.", Toast.LENGTH_LONG).show();
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                mBluetoothLeService.displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] data = intent.getByteArrayExtra(EXTRA_CHARACTER_DATA);
                bleCommunication(data);
            } else if(EXTRA_CHARACTER_DATA.equals(action)) {
            }
        }
    };

    private void bleCommunication(byte[] data) {
        mPreference.setDeviceConn(true);
        protocolCheck(data);

        if(build != null) {

            // now time setting
            Date tdays = new Date();
            int tday = tdays.getDay();
            int ty = tdays.getYear() + 1900;
            int tmonth = tdays.getMonth() + 1;
            int tdate = tdays.getDate();

            int thour = tdays.getHours();
            int tminute = tdays.getMinutes();
            int tsecond = tdays.getSeconds();



            StnEggPkt.StnEggPacket.Builder sendBuilder = null;

            if(testLogFlag) {
                TestActivity.setCommandText(build.getSerpCommand(), build.getMotion());
            }

            if(build.getSerpCommand() == mConstant.MESSAGE_DATE_TIME_REQUEST) {
                sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                sendBuilder.setSerpCommand(mConstant.MESSAGE_DATE_TIME_CONTENT);

                // date
                sendBuilder.getDateBuilder().setYear(ty);
                sendBuilder.getDateBuilder().setMonth(tmonth);
                sendBuilder.getDateBuilder().setDay(tdate);
                sendBuilder.getDateBuilder().setHour(thour);
                sendBuilder.getDateBuilder().setMinutes(tminute);
                sendBuilder.getDateBuilder().setSeconds(tsecond);
                if (tday == 0) {
                    sendBuilder.getDateBuilder().setWeekDay(7);
                } else {
                    sendBuilder.getDateBuilder().setWeekDay(tday);
                }



            }else if(build.getSerpCommand() == mConstant.MESSAGE_DATE_TIME_RECEIVED) {
                mPreference.setUid(build.getUserUint01());


                sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                sendBuilder.setSerpCommand(mConstant.MESSAGE_PHYSICAL_ATTRIBUTES_CONTENT);
                if(mPreference != null) {
                    HashMap<String, Object> dbAccount = mDBManager.selectAccunt();
                    sendBuilder.getPhysiqueBuilder().setHeight(Integer.parseInt(dbAccount.get("tall").toString()));
                    sendBuilder.getPhysiqueBuilder().setWeight(Integer.parseInt(dbAccount.get("weight").toString()));

                    sendBuilder.getPhysiqueBuilder().setHeartRateHigh(130);
                    sendBuilder.getPhysiqueBuilder().setHeartRateLow(70);

                    sendBuilder.getPhysiqueBuilder().setGender(Integer.parseInt(dbAccount.get("gender").toString()));

                    int birthYear = 0;
                    int birthMonth = 0;
                    int birthDay = 0;
                    birthYear = Integer.parseInt(dbAccount.get("birth").toString().substring(0,4));
                    birthMonth = Integer.parseInt(dbAccount.get("birth").toString().substring(5,7));
                    birthDay = Integer.parseInt(dbAccount.get("birth").toString().substring(8,10));
                    sendBuilder.getDateBuilder().setYear(birthYear);
                    sendBuilder.getDateBuilder().setMonth(birthMonth);
                    sendBuilder.getDateBuilder().setDay(birthDay);


                    Cursor goal = mDBManager.selectGoal();
                    int step = 0;
                    int kcal = 0;
                    while (goal.moveToNext()) {
                        if (goal.getInt(1) == mConstant.ACTIVITY_FLAG) {
                            step = goal.getInt(2);
                            kcal = goal.getInt(3);
                        }
                    }
                    sendBuilder.setCount(step);
                    sendBuilder.setCalories(kcal);
                }





            }else if(build.getSerpCommand() == mConstant.MESSAGE_PHYSICAL_ATTRIBUTES_RECEIVED) {
                sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                sendBuilder.setSerpCommand(mConstant.MESSAGE_PHYSICAL_ATTRIBUTES_ACCEPT);



            }else if(build.getSerpCommand() == mConstant.MESSAGE_PHYSICAL_ATTRIBUTES_RECEIVED) {




            }else if(build.getSerpCommand() == mConstant.MESSAGE_VIBRATION_ACCEPT) {




            }else if(build.getSerpCommand() == mConstant.MESSAGE_HEART_ALARM_REQUEST) {
                int heartRate = build.getHearthRate();
                sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                sendBuilder.setSerpCommand(mConstant.MESSAGE_HEART_ALARM_RECEIVED);

                if(testLogFlag) {
                    TestActivity.setHeartRateText(heartRate);
                }



            }else if(build.getSerpCommand() == mConstant.MESSAGE_HEART_RATE_ACCEPT) {
                int heartRate = build.getHearthRate();
                int max = build.getMax();
                int min = build.getMin();




            }else if(build.getSerpCommand() == mConstant.MESSAGE_SLEEP_STATUS_CONTENT || build.getSerpCommand() == mConstant.MESSAGE_SLEEP_STATUS_RECEIVED) {
                if(testLogFlag) {
                    int status = build.getUserUint01();
                    int sleep = build.getDurationMinutes();
                    String date = build.getDate().getYear() + "-" + build.getDate().getMonth() + "-" + build.getDate().getDay() + "_" + build.getDate().getHour() + ":" + build.getDate().getMinutes() + ":" + build.getDate().getSeconds() ;

                    TestActivity.setSleepText(status, sleep, date);
                }



            }else if(build.getSerpCommand() == mConstant.MESSAGE_SELECT_EXERCISE_REQUEST) {
                int status = build.getUserUint01();



            }else if(build.getSerpCommand() == mConstant.MESSAGE_REAL_TIME_ACTIVITY_REPORT_CONTENT || build.getSerpCommand() == mConstant.MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT) {

                if(build.getMotion() == mConstant.MOTION_WALKING || build.getMotion() == mConstant.MOTION_RUNNING) {
                    ArrayList<Object> activity = new ArrayList<>();
                    activity.add(build.getMotion());
                    activity.add(build.getDistanceMeters());
                    activity.add(build.getCount());
                    activity.add(build.getCalories());
                    activity.add((build.getDate().getHour() < 10 ? "0" + build.getDate().getHour() : build.getDate().getHour()) + ":" +
                            (build.getDate().getMinutes() < 10 ? "0" + build.getDate().getMinutes() : build.getDate().getMinutes()) +  ":" +
                            (build.getDate().getSeconds() < 10 ? "0" + build.getDate().getSeconds() : build.getDate().getSeconds()));
                    activity.add((build.getDate().getYear() + 1900) + "-" +
                            (build.getDate().getMonth() < 10 ? "0" + build.getDate().getMonth() : build.getDate().getMonth()) +  "-" +
                            (build.getDate().getDay() < 10 ? "0" + build.getDate().getDay() : build.getDate().getDay()));
                    activity.add(build.getDurationSeconds());

                    if(build.getCount() > 0) {
                        mDBManager.insertActivity(activity);
                    }

                    if(testLogFlag) {
                        TestActivity.setActivtyText(build.getMotion(), build.getCount(), build.getDistanceMeters(), build.getCalories());
                    }


                }else if(build.getUserInt01() != 2 || build.getUserInt01() != 3 || build.getUserInt01() != 99 || build.getUserInt01() != 97 || build.getUserInt01() != 98 || build.getUserInt01() != 36) {
                    ArrayList<Object> motion = new ArrayList<>();
                    motion.add(build.getMotion());
                    motion.add(build.getCount());
                    motion.add(build.getCalories());
                    motion.add((build.getDate().getHour() < 10 ? "0" + build.getDate().getHour() : build.getDate().getHour()) + ":" +
                            (build.getDate().getMinutes() < 10 ? "0" + build.getDate().getMinutes() : build.getDate().getMinutes()) +  ":" +
                            (build.getDate().getSeconds() < 10 ? "0" + build.getDate().getSeconds() : build.getDate().getSeconds()));
                    motion.add((build.getDate().getYear() + 1900) + "-" +
                            (build.getDate().getMonth() < 10 ? "0" + build.getDate().getMonth() : build.getDate().getMonth()) +  "-" +
                            (build.getDate().getDay() < 10 ? "0" + build.getDate().getDay() : build.getDate().getDay()));
                    motion.add(build.getDurationSeconds());

                    if(build.getCount() > 0 && build.getUserInt02() == 1) {
                        mDBManager.insertFitness(motion);
                    }


                    sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
                    sendBuilder.setSerpCommand(mConstant.MESSAGE_SAVED_ACTIVITY_REPORT_RECEIVED);


                    if(testLogFlag) {
                        TestActivity.setMotionText(build.getMotion(), build.getCount(), build.getCalories());
                    }
                }



            }




            if(sendBuilder != null) {
                try {
                    byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
                    byte[] builderTobyte = sendBuilder.build().toByteArray();

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }
    }



    byte[] data2;
    boolean flags = false;
    StnEggPkt.StnEggPacket build;
    private void protocolCheck(byte[] characteristic) {
        byte[] data = characteristic;
        if (data != null && data.length > 0) {
            String a = "";

            byte[] test = new byte[1];
            for (int i = 0; i < data.length; i++) {
                test[0] = data[i];
                ULog.d("Protocol", "REAL RECEIVE MESSAGE ==>" + new String(test));
            }

            byte[] ddd = new byte[2];
            ddd[0] = data[data.length - 2];
            ddd[1] = data[data.length - 1];
            ULog.d("Protocol", "dd->>" + new String(ddd));

            if (!"\r\n".equals(new String(ddd))) {
                data2 = characteristic;
                flags = true;

                return;
            }

            if (flags) {
                ULog.d("Protocol", " 잘렸던 msg 이어 붙임 ");
                byte[] datas = data;
//                data = new byte[data1.length + data2.length + datas.length];
                data = new byte[data2.length + datas.length];

                ULog.d("Protocol", " DATA LENGTH :" + data.length);
                for (int i = 0; i < data.length; i++) {
                    if (i < data2.length)
                        data[i] = data2[i];
                    else
                        data[i] = datas[(i - data2.length)];
                    /*if(i < data1.length) {
                        data[i] = data1[i];
                    }else if (i < data2.length + data1.length) {
                        data[i] = data2[(i - data1.length)];
                    }else {
                        data[i] = datas[(i - data1.length - data2.length)];
                    }*/
                }
                flags = false;
            }

            byte[] d = new byte[2];
            d[0] = data[0];
            d[1] = data[1];

            ULog.d("Protocol", "d->>" + new String(d));

            String cha = new String(d);
            ULog.d("Protocol", "d2->>" + cha);


            //20160822 start
            try {
                int contentStart = 0, contentEnd = 0;
                if (data != null && data.length > 0) {
                    ULog.d("Protocol", "# Read characteristic 20160817: data 있음");

                    for (int i = 0; i < data.length - 1; i++) {
                        //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                        if (data[i] == 'S' && data[i + 1] == 'E') {
                            //20160804 old version(vr) contentStart = i + 4;
                            contentStart = i + 2;
                        }
                        if (data[i] == '\r' && data[i + 1] == '\n') {
                            contentEnd = i - 2;
                            break;
                        }
                    }
                    ULog.d("Protocol", "contentStart : " + contentStart + "////contentEnd : " + contentEnd);

                    if(contentStart > contentEnd) {
                        return;
                    }
                }
                byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
                build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);

                ULog.d("idtActivity", "getSerpCommand : " + build.getSerpCommand() + " /////getMotion : " + build.getMotion() + " /////getCount : " + build.getCount() + " /////getUserInt01 : " + build.getUserInt01() + " /////getUserInt02 : " + build.getUserInt02() + " /////getUserInt04 : " + build.getUserInt04() +" /////getUserUint01 : " + build.getUserUint01() + " /////getUserUint02 : " + build.getUserUint02() + " /////getUserUint04 : " + build.getUserUint04());

            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            //20160822 end
        }

    }


    public static void heartRateMessageSend() {
        StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
        builder.setSerpCommand(43);

        try {
            byte[] encoded = new byte[builder.build().toByteArray().length+5];
            byte[] builderTobyte = builder.build().toByteArray();

            encoded[0] = 'S';
            encoded[1] = 'E';
            int i;
            for( i = 0; i < builderTobyte.length; i++) {
                encoded[i+2] = builderTobyte[i];
            }
            encoded[i+2] = 'E';
            encoded[i+3] = '\r';
            encoded[i+4] = '\n';
            mBluetoothLeService.bleWrite(encoded);
        }catch (UninitializedMessageException e){
            e.printStackTrace();
        }
    }

    public static void motionMessageSend(int motion) {
        //command
        StnEggPkt.StnEggPacket.Builder sendBuilder2 = StnEggPkt.StnEggPacket.newBuilder();
        sendBuilder2.setSerpCommand(45);
        sendBuilder2.setUserUint01(motion);

        try {
            byte[] encoded = new byte[sendBuilder2.build().toByteArray().length + 5];
            byte[] builderTobyte = sendBuilder2.build().toByteArray();

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
    }

}

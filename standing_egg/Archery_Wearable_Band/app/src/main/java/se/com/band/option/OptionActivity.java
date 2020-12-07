package se.com.band.option;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import se.com.band.R;
import se.com.band.login.DeviceConnectActivity;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener {

    Button mSearchDevice, mBattery, mAlarm, mNotification, mOta, mUnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSearchDevice = (Button) findViewById(R.id.search_device_button);
        mBattery = (Button) findViewById(R.id.battery_button);
        mAlarm = (Button) findViewById(R.id.alarm_button);
        mNotification = (Button) findViewById(R.id.notification_button);
        mOta = (Button) findViewById(R.id.ota_button);
        mUnregister = (Button) findViewById(R.id.unregister_button);

        mSearchDevice.setOnClickListener(this);
        mBattery.setOnClickListener(this);
        mAlarm.setOnClickListener(this);
        mNotification.setOnClickListener(this);
        mOta.setOnClickListener(this);
        mUnregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.search_device_button:
                intent = new Intent(getApplicationContext(), DeviceConnectActivity.class);
                startActivity(intent);
                break;
            case R.id.battery_button:
                intent = new Intent(getApplicationContext(), BatteryActivity.class);
                startActivity(intent);
                break;
            case R.id.alarm_button:
                intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.notification_button:
                intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
                break;
            case R.id.ota_button:
                intent = new Intent(getApplicationContext(), OtaActivity.class);
                startActivity(intent);
                break;
            case R.id.unregister_button:
                intent = new Intent(getApplicationContext(), UnregisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}

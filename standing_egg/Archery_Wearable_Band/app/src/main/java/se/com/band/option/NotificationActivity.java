package se.com.band.option;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.com.band.R;
import se.com.band.utility.Constants;
import se.com.band.utility.ExpandCollapse;
import se.com.band.utility.Preferences;
import se.com.band.utility.ULog;

public class NotificationActivity extends AppCompatActivity {

    Context mContext;
    Preferences mPreference;

    ExpandCollapse animExpand;

    View ExpandView, parentView, snsListView, moveTimeView, moveStartView, moveEndView;
    ImageView parentImage;
    TextView moveTimeText, moveStartText, moveEndText;
    Switch moveSwitch, callSwitch, smsSwitch;

    Boolean expandFlag = false;

    int timeFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;
        mPreference = Preferences.getInstance(mContext);

        animExpand = new ExpandCollapse();

        ExpandView = (View) findViewById(R.id.move_expand_layout);
        parentView = (View) findViewById(R.id.move_parent_layout);
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandFlag) {
                    expandFlag = false;
                    animExpand.expand(ExpandView);
                    parentImage.setImageResource(R.mipmap.notify_close);
                }else if(!expandFlag) {
                    expandFlag = true;
                    animExpand.collapse(ExpandView);
                    parentImage.setImageResource(R.mipmap.notify_open);
                }
            }
        });

        parentImage = (ImageView) findViewById(R.id.parent_img);
        parentView.callOnClick();

        snsListView = (View) findViewById(R.id.sns_list_view);
        snsListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageManager pm = getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                List<ApplicationInfo> smsPackages = new ArrayList<ApplicationInfo>();
                NotificationAdapter adapter = new NotificationAdapter(mContext);

                for (ApplicationInfo applicationInfo : packages) {
                    try {
                        PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

                        //Get Permissions
                        String[] requestedPermissions = packageInfo.requestedPermissions;

                        if(requestedPermissions != null) {
                            for (int i = 0; i < requestedPermissions.length; i++) {
//                                if(requestedPermissions[i].equals("android.permission.READ_SMS") && ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
                                if(requestedPermissions[i].equals("android.permission.READ_SMS") && requestedPermissions[i].equals("android.permission.SEND_SMS")) {
                                    smsPackages.add(applicationInfo);
                                    adapter.addList(applicationInfo);
                                }
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }


                for (ApplicationInfo smsApplicationInfo : smsPackages) {
                    if((smsApplicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        ULog.d("Notifiaction", "App: " + smsApplicationInfo.loadLabel(getPackageManager()) + " Package: " + smsApplicationInfo.packageName + " Flag: " + smsApplicationInfo.flags);
                    }
                }

                if(smsPackages.size() == 0) {
                    Toast.makeText(getApplicationContext(), "The device does not have the sms application installed.", Toast.LENGTH_LONG).show();
                    return;
                }

                adapter.notifyDataSetChanged();
                AlertDialog.Builder notifiDialogBuilder = new AlertDialog.Builder(mContext);
                notifiDialogBuilder.setTitle("NOTIFICATION");
                notifiDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });

                notifiDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ULog.d("HelloActivity", "which : " + which);
                    }
                });
                //Create alert dialog object via builder
                AlertDialog alertDialogObject = notifiDialogBuilder.create();
                //Show the dialog
                alertDialogObject.show();
            }
        });

        moveTimeText = (TextView) findViewById(R.id.every_text);
        moveStartText = (TextView) findViewById(R.id.start_text);
        moveEndText = (TextView) findViewById(R.id.end_text);

        moveSwitch = (Switch) findViewById(R.id.move_switch);
        callSwitch = (Switch) findViewById(R.id.call_switch);
        smsSwitch = (Switch) findViewById(R.id.sms_switch);


        moveTimeView = (View) findViewById(R.id.move_time_layout);
        moveTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlag = 1;
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, TimePickerDialog.THEME_HOLO_LIGHT, timeListener, 10, 0, true);
                timePickerDialog.show();
            }
        });


        moveStartView = (View) findViewById(R.id.move_start_layout);
        moveStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlag = 2;
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, timeListener, 9, 0, false);
                timePickerDialog.show();
            }
        });


        moveEndView = (View) findViewById(R.id.move_end_layout);
        moveEndView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlag = 3;
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, timeListener, 18, 0, false);
                timePickerDialog.show();
            }
        });

        initPreference();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notiPreference();
    }


    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ULog.d("onTimeSet: ");
            if(timeFlag == 1) {
                String s_hour = "";
                String s_minute = "";
                if(minute > 0) {
                    s_minute = " " + minute + " minute";
                }
                if(hourOfDay > 0) {
                    s_hour = hourOfDay + " hour";
                }

                moveTimeText.setText(s_hour + s_minute);
            }else if(timeFlag == 2) {
                String periods = "";
                if(hourOfDay > 12) {
                    periods = "PM";
                }else {
                    periods = "AM";
                }
                moveStartText.setText(hourOfDay + ":" + minute + " " + periods);
            }else if(timeFlag == 3) {
                String periods = "";
                if(hourOfDay > 12) {
                    periods = "PM";
                }else {
                    periods = "AM";
                }
                moveEndText.setText(hourOfDay + ":" + minute + " " + periods);
            }
        }
    };

    private void setPreference() {
        if(moveSwitch.isChecked()) {
            Bundle d = new Bundle();
            d.putBoolean(Constants.PREFERENCE_MOVE_NOTI, true);
            d.putString(Constants.PREFERENCE_ALERT_EVERY_TEXT, moveTimeText.getText().toString());
            d.putString(Constants.PREFERENCE_ALERT_START_TEXT, moveStartText.getText().toString());
            d.putString(Constants.PREFERENCE_ALERT_END_TEXT, moveEndText.getText().toString());
            mPreference.setAlert(d);
        }else {
            mPreference.resetAlert();
        }
    }

    private void initPreference() {
        if(mPreference.getMoveNoti()) {
            moveTimeText.setText(mPreference.getAlertTimeTxt());
            moveStartText.setText(mPreference.getAlertStartTxt());
            moveEndText.setText(mPreference.getAlertEndTxt());
            moveSwitch.setChecked(true);
        }

        if(mPreference.getCAllNoti()) {
            callSwitch.setChecked(true);
        }

        if(mPreference.getSMSNoti()) {
            smsSwitch.setChecked(true);
        }
    }

    private void notiPreference() {
        setPreference();

        Bundle d = new Bundle();
        d.putBoolean(Constants.PREFERENCE_CALL_NOTI, callSwitch.isChecked());
        d.putBoolean(Constants.PREFERENCE_SMS_NOTI, smsSwitch.isChecked());
        d.putBoolean(Constants.PREFERENCE_SNS_NOTI, false);
        d.putBoolean(Constants.PREFERENCE_MOVE_NOTI, moveSwitch.isChecked());

        mPreference.setSetting(d);
    }
}

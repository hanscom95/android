package se.com.band.option;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;

import se.com.band.R;

public class AlarmActivity extends AppCompatActivity {

    Context mContext;

    ListView mAlarmList;
    AlarmAdapter mAdapter;

    ArrayList<String> time = new ArrayList<>();
    ArrayList<Boolean> aSwitch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;

        mAlarmList = (ListView) findViewById(R.id.alarm_list);
        mAdapter = new AlarmAdapter(mContext);

        time.add("08:22 AM");
        time.add("08:30 AM");
        time.add("08:35 AM");

        time.add("17:22 AM");
        time.add("18:22 AM");
        time.add("20:22 AM");

        aSwitch.add(true);
        aSwitch.add(true);
        aSwitch.add(true);

        aSwitch.add(false);
        aSwitch.add(true);
        aSwitch.add(false);
        mAdapter.addList(time, aSwitch);
        mAdapter.notifyDataSetChanged();
        mAlarmList.setAdapter(mAdapter);

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activit_option_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String periods = "";
                        if(hourOfDay > 12) {
                            periods = "PM";
                        }else {
                            periods = "AM";
                        }
                        String sTime =  (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute) +  ":" + periods;
                        boolean sSwitch = true;
                        time.add(sTime);
                        aSwitch.add(sSwitch);

                        mAdapter.clear();
                        mAdapter.addList(time, aSwitch);
                        mAdapter.notifyDataSetChanged();
                        mAlarmList.setAdapter(mAdapter);
                    }
                }, 9, 0, false);
                timePickerDialog.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

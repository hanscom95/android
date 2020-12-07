package se.com.band.goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import se.com.band.R;
import se.com.band.utility.Constants;

public class GoalSettingActivity extends AppCompatActivity {

    RelativeLayout mActivityLayout, mMotionLayout;

    Constants mConstant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mActivityLayout = (RelativeLayout) findViewById(R.id.activity_layout);
        mMotionLayout = (RelativeLayout) findViewById(R.id.motion_layout);

        mActivityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("flag", mConstant.ACTIVITY_FLAG);


                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        mMotionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("flag", mConstant.MOTION_FLAG);


                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

}

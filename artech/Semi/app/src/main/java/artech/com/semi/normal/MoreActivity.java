package artech.com.semi.normal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import artech.com.semi.R;
import artech.com.semi.utility.BottomNavigationViewHelper;

public class MoreActivity extends AppCompatActivity {

    Context mContext;

    RelativeLayout mNoticeLayout, mEventLayout, mAdLayout, mQuestionLayout, mSettingLayout, mFirstLayout;
    Button mVersionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_more);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_more);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        mFirstLayout = findViewById(R.id.first_layout);
        mNoticeLayout = findViewById(R.id.notice_layout);
        mEventLayout = findViewById(R.id.event_layout);
        mAdLayout = findViewById(R.id.ad_layout);
        mQuestionLayout = findViewById(R.id.question_layout);
        mSettingLayout = findViewById(R.id.setting_layout);
        mVersionButton = findViewById(R.id.version_button);

        mNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoticeActivity.class);
                startActivity(intent);
            }
        });
        mEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EventActivity.class);
                startActivity(intent);
            }
        });
        mAdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, QuestionWriteActivity.class);
                startActivity(intent);
            }
        });
        mQuestionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, QuestionActivity.class);
                startActivity(intent);
            }
        });
        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });
        mFirstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, QuestionWriteActivity.class);
                startActivity(intent);
            }
        });

        mVersionButton.setText("Verision " + getString(R.string.app_version));

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(mContext, NormalMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_basket:
                    intent = new Intent(mContext, ProductManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_favorites:
                    intent = new Intent(mContext, FavoritesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_my_info:
                    intent = new Intent(mContext, MyInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_more:
                    return true;
            }
            return false;
        }
    };

}

package artech.com.fivics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    Context mContext;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    RelativeLayout mNonMemberLayout, mMemberLayout;
    RelativeLayout mBeforeLayout, mNextLayout;

    ImageView mNonMemberImg, mMemberImg, mLogoImg;

    TextView mMemberText;

    boolean mNonMember = false;
    boolean mMember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.content_signup);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            }
        });

        Configuration config = new Configuration();
        config.setLocale(Locale.CHINESE);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        mLogoImg = (ImageView) findViewById(R.id.prohibition_logo);
        mLogoImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        mNonMemberLayout = (RelativeLayout) findViewById(R.id.nonmember_layout);
        mMemberLayout = (RelativeLayout) findViewById(R.id.member_layout);

        mBeforeLayout = (RelativeLayout) findViewById(R.id.before_layout);
        mNextLayout = (RelativeLayout) findViewById(R.id.next_layout);

        mNonMemberImg = (ImageView) findViewById(R.id.nonmember_img);
        mMemberImg = (ImageView) findViewById(R.id.member_img);

        mMemberText = (TextView) findViewById(R.id.member_text);

        mNonMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNonMemberImg.setImageResource(R.mipmap.btn1_signup_on);
                mMemberImg.setImageResource(R.mipmap.btn_signup_off);

                mMemberText.setTextColor(getResources().getColor(android.R.color.white));

                mNonMember = true;
                mMember = false;
            }
        });

        mMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNonMemberImg.setImageResource(R.mipmap.btn_signup_off);
                mMemberImg.setImageResource(R.mipmap.btn2_signup_on);

                mMemberText.setTextColor(getResources().getColor(R.color.signup_member_select_text));

                mNonMember = false;
                mMember = true;
            }
        });

        mBeforeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsConditionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mNextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNonMember || mMember) {
                    int flag = 0;
                    if(mNonMember) {
                        flag = 1;
                    }else if(mMember) {
                        flag = 2;
                    }

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("member", flag);
                    bundle.putInt("menu", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("회원");
                    alertDialogBuilder.setMessage("사용하실 유형을 선택하신 후 클릭해 주세요.");
                    alertDialogBuilder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    @SuppressLint("NewApi")
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

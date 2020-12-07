package artech.com.arcam.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import artech.com.arcam.R;
import artech.com.arcam.utility.DBManager;


public class IntroActivity extends AppCompatActivity {

    Context mContext;
    DBManager dbManager;

    RelativeLayout mIntroLayout;
    ImageView introLogoImg;

    TimerTask mTask;
    Timer mTimer;

    int flags = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.content_intro);
        dbManager = new DBManager(mContext, "arcam.db", null, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }


        /*getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            }
        });*/

        introLogoImg = (ImageView) findViewById(R.id.intro_logo);

        mIntroLayout = (RelativeLayout) findViewById(R.id.content_intro);

        mIntroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.cancel();
                mTimer.cancel();


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate (R.layout.content_intro_wifi_popup, null);
                alertDialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                alertDialog.getWindow().setLayout(600, 300);
//                alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                    }
                });
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
//                        Intent intent = new Intent(getApplicationContext(), AutoLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        mName = dbManager.selectId();

        mTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("IntroActivity", "mName : "  + mName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!"".equals(mName)) {
                            Intent intent = new Intent(getApplicationContext(), AutoLoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.content_intro_wifi_popup, null);
                            alertDialogBuilder.setView(dialogView);
                            final AlertDialog alertDialog = alertDialogBuilder.show();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            alertDialog.getWindow().setLayout(600, 300);
//                alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                                }
                            });
                            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 100);


    }
    /*@Override
    @SuppressLint("NewApi")
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }*/
}

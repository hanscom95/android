package artech.com.fivics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Locale;

import artech.com.fivics.guide.GuideActivity;
import artech.com.fivics.prohibition.ProhibitionActivity;
import artech.com.fivics.ranking.RankingActivity;
import artech.com.fivics.score.ArcherySettingActivity;
import artech.com.fivics.utility.NetworkConnection;
import artech.com.fivics.utility.Preferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    Preferences mPreference;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    RelativeLayout mBannelLayout, mMainGuideLayout;
    Button mPrecautionsButton, mUserManualButton, mArcheryButton, mRangkingButton, mBannelButton;
    ImageView mSettingButton, mHomeButton, mLogoImg;
    View mMainView, mBeforeView;
    TextView mBannelShopText;
    ViewFlipper mBannelInfoText;

    AdminTask mAdminTask;

    String mPhone = "", mName = "";
    int mMember = 0;
    int mMenu = 0;

    String mBannelNumber = "";
    String[] mBannelInfo;

    ArrayList<Admin> mAdminArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_menu);
        mContext = this;
        mPreference = Preferences.getInstance(mContext);
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


        mLogoImg = (ImageView) findViewById(R.id.main_logo);
        mLogoImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });


//        mMainView = getWindow().getDecorView();
//        mMainView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                System.out.println("focus  onSystemUiVisibilityChange " +  visibility);
//                if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
//                    hideSystemBar();
//                }
//            }
//        });
//        onWindowFocusChanged(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        mPrecautionsLayout = (RelativeLayout) findViewById(R.id.precautions_layout);
//        mUserManualLayout = (RelativeLayout) findViewById(R.id.user_manual_layout);
//        mArcheryLayout = (RelativeLayout) findViewById(R.id.archery_layout);
//        mRangkingLayout = (RelativeLayout) findViewById(R.id.rangking_layout);
//        mBannelLayout = (RelativeLayout) findViewById(R.id.bannel_layout);

        mMainGuideLayout = (RelativeLayout) findViewById(R.id.main_guide_layout);

        mPrecautionsButton = (Button) findViewById(R.id.precautions_button);
        mUserManualButton = (Button) findViewById(R.id.user_manual_button);
        mArcheryButton = (Button) findViewById(R.id.archery_button);
        mRangkingButton = (Button) findViewById(R.id.rangking_button);

        mSettingButton = (ImageView) findViewById(R.id.setting_button);
        mHomeButton = (ImageView) findViewById(R.id.home_button);

        mMainView = findViewById(R.id.next_layout);
        mBeforeView = findViewById(R.id.before_layout);

        mBannelShopText = (TextView) findViewById(R.id.main_title);
        mBannelInfoText = (ViewFlipper) findViewById(R.id.main_sub_title);
        itemSelected();

        mMainGuideLayout.setOnClickListener(this);

        mPrecautionsButton.setOnClickListener(this);
        mUserManualButton.setOnClickListener(this);
        mArcheryButton.setOnClickListener(this);
        mRangkingButton.setOnClickListener(this);

        mHomeButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);

        mMainView.setOnClickListener(this);
        mBeforeView.setOnClickListener(this);

//        mPrecautionsLayout.setOnClickListener(this);
//        mUserManualLayout.setOnClickListener(this);
//        mArcheryLayout.setOnClickListener(this);
//        mRangkingLayout.setOnClickListener(this);
//        mBannelLayout.setOnClickListener(this);

        mAdminArrayList = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPhone = bundle.getString("phone");
        mName = bundle.getString("name");
        mMember = bundle.getInt("member");
        mMenu  = bundle.getInt("menu");
        mBannelNumber = bundle.getString("bannelNummber");
        mBannelInfo = bundle.getStringArray("bannelInfo");

        if(mMember == 1) {

            if(mMenu == 1) {
                mMainGuideLayout.setVisibility(View.VISIBLE);

                mUserManualButton.setEnabled(false);
                mArcheryButton.setEnabled(false);
                mRangkingButton.setEnabled(false);
            }else if(mMenu == 2) {
                mMainGuideLayout.setVisibility(View.GONE);

                mUserManualButton.setEnabled(true);
                mArcheryButton.setEnabled(false);
                mRangkingButton.setEnabled(false);
            }else if(mMenu == 3) {
                mMainGuideLayout.setVisibility(View.GONE);

                mUserManualButton.setEnabled(true);
                mArcheryButton.setEnabled(true);
                mRangkingButton.setEnabled(false);
            }else {
                mMainGuideLayout.setVisibility(View.GONE);

                mUserManualButton.setEnabled(true);
                mArcheryButton.setEnabled(true);
                mRangkingButton.setEnabled(true);
            }
        }else {
//            mMainGuideLayout.setVisibility(View.GONE);
            if(mMenu == 1) {
                mMainGuideLayout.setVisibility(View.VISIBLE);
            }else{
                mMainGuideLayout.setVisibility(View.GONE);
            }

            mUserManualButton.setEnabled(true);
            mArcheryButton.setEnabled(true);
            mRangkingButton.setEnabled(true);
        }

        mAdminTask = new AdminTask();
        mAdminTask.execute((Void) null);


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        bundle.putString("phone", mPhone);
        bundle.putString("name", mName);
        bundle.putInt("member", mMember);
        bundle.putInt("menu", mMenu);
        bundle.putString("bannelNummber", mBannelNumber);
        bundle.putStringArray("bannelInfo", mBannelInfo);

        switch (v.getId()) {
            case R.id.precautions_button:
//                intent = new Intent(getApplicationContext(), CafeMenuSecondActivity.class);
//                startActivity(intent);
                intent = new Intent(getApplicationContext(), ProhibitionActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.user_manual_button:
                intent = new Intent(getApplicationContext(), GuideActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.archery_button:
                intent = new Intent(getApplicationContext(), ArcherySettingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.rangking_button:
                bundle.putInt("rank", 1);
                intent = new Intent(getApplicationContext(), RankingActivity.class);
                intent.putExtras(bundle);
//                intent = new Intent(getApplicationContext(), FullscreenActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.next_layout:
//                intent = new Intent(getApplicationContext(), RankingActivity.class);
                intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.before_layout:
//                intent = new Intent(getApplicationContext(), RankingActivity.class);
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.main_guide_layout:
                mMainGuideLayout.setVisibility(View.GONE);
                break;

            case R.id.setting_button:
                final String url = mPreference.getURL();
                Log.d("MainActivity", "init url : " +url);


                final EditText confirmEdit = new EditText(mContext);
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(mContext);
                confirmBuilder.setTitle("관리자 비밀번호");
                confirmBuilder.setView(confirmEdit);
                confirmBuilder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if("0388".equals(confirmEdit.getText().toString())) {
                            final EditText editText = new EditText(mContext);
                            if("".equals(url)) {
                                String URL = "rtsp://192.168.31.15:554/ch1/main/av_stream";//"rtmp://192.168.1.88:1935/flash/11:admin:admin";//"rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
                                editText.setText(URL);
                            }else {
                                editText.setText(url);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Camera 주소");
                            builder.setView(editText);
                            builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String popupUrl = editText.getText().toString();
                                    Log.d("MainActivity", "editText url : " + popupUrl);
//                        ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
//                        fragment.mFilePath = popupUrl;
                                    mPreference.setURL(popupUrl);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            final AlertDialog alertDialog = builder.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                                }
                            });
                        }else {
                            Toast.makeText(mContext, "관리자 패스워드가 틀렸습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog confirmDialog = confirmBuilder.show();
                confirmDialog.setCanceledOnTouchOutside(false);
                confirmDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                confirmDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        confirmDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                    }
                });

                break;

            case R.id.home_button:
                String franchisee = mPreference.getFranchisee();

                final EditText shopEdit = new EditText(mContext);
                shopEdit.setText(franchisee);
                AlertDialog.Builder shopBuilder = new AlertDialog.Builder(mContext);
                shopBuilder.setTitle("직영점");
                shopBuilder.setView(shopEdit);
                shopBuilder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String popupText = shopEdit.getText().toString();
//                        ArcheryCameraFragment fragment = (ArcheryCameraFragment) getFragmentManager().findFragmentById(R.id.archery_video_view);
//                        fragment.mFilePath = popupUrl;
                        mPreference.setFranchisee(popupText);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog alertShopDialog = shopBuilder.show();
                alertShopDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                alertShopDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertShopDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                    }
                });

                break;
//            case R.id.bannel_layout:
//                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
//                alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                alert.setMessage("광고용 팝업창 입니다!!!");
//                alert.show();
//                intent = new Intent(getApplicationContext(), GuideActivity.class);
//                startActivity(intent);
//                finish();
//                break;

            default:
                break;
        }
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



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AdminTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        ArrayList<Object> mArrayList;

        AdminTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);
                connect = network.shopSelect(1);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAdminTask = null;

            if(success) {
                try {
                    ArrayList<Admin> arrayList = new ArrayList<>();
                    mBannelInfo = new String[network.mShopInfo.length()];
                    for (int i = 0; i < network.mShopInfo.length(); i++) {
                        Admin admin = new Admin();
                        admin._id = network.mShopInfo.optJSONObject(i).getInt("_id");
                        admin.col = network.mShopInfo.optJSONObject(i).getInt("col");
                        admin.name = Html.fromHtml(network.mShopInfo.optJSONObject(i).getString("name")).toString();
                        admin.info = Html.fromHtml(network.mShopInfo.optJSONObject(i).getString("info")).toString();
                        admin.number = Html.fromHtml(network.mShopInfo.optJSONObject(i).getString("number")).toString();

                        arrayList.add(admin);

                        mBannelNumber = "[" + admin.name + " " + admin.number + "]";
                        mBannelInfo[i] = admin.info;
                        Log.d("MainActivity" , "info : " + admin.info);
                        TextView textView = new TextView(mContext);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        textView.setLayoutParams(params);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setIncludeFontPadding(false);
                        textView.setLines(1);
                        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        textView.setFocusable(true);
                        textView.setFocusableInTouchMode(true);
                        textView.setSingleLine();
                        textView.setTextSize(22);
                        textView.setTextColor(getResources().getColor(R.color.main_grey));
//                        textView.setFont("NotoSansCJKkr_Medium.otf");
                        textView.setText(mBannelInfo[i]);
                        mBannelInfoText.addView(textView);
                    }

                    mBannelShopText.setText(mBannelNumber);
//                    mBannelInfoText.setText(mBannelInfo);

                    mAdminArrayList.addAll(arrayList);

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                if(!"".equals(mBannelNumber) && mBannelNumber != null) {
                    mBannelShopText.setText(mBannelNumber);
                    mBannelInfo = new String[1];
                    mBannelInfo[0] = "슈팅존의 오신걸 환영합니다!";
                    for (String string : mBannelInfo) {
                        Log.d("MainActivity", "string : " + string);
                        TextView textView = new TextView(mContext);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        textView.setLayoutParams(params);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setIncludeFontPadding(false);
                        textView.setLines(1);
                        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        textView.setFocusable(true);
                        textView.setFocusableInTouchMode(true);
                        textView.setSingleLine();
                        textView.setTextSize(22);
                        textView.setTextColor(getResources().getColor(R.color.main_grey));
//                        textView.setFont("NotoSansCJKkr_Medium.otf");
                        textView.setText(string);
                        mBannelInfoText.addView(textView);
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAdminTask = null;
        }
    }

    private void itemSelected() {
        mBannelInfoText.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_up_in));
        mBannelInfoText.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_up_out));
        mBannelInfoText.startFlipping();
    };

    public class Admin{
        int _id;
        int col;
        String name;
        String info;
        String number;
    }
}

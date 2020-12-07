package artech.com.fivics.ranking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import artech.com.fivics.IntroActivity;
import artech.com.fivics.MainActivity;
import artech.com.fivics.R;
import artech.com.fivics.utility.NetworkConnection;
import artech.com.fivics.utility.Preferences;

public class RankingActivity extends AppCompatActivity{

    Context mContext;
    Preferences mPreference;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    RelativeLayout mBeforeLayout, mNextLayout, mMyHistoryLayout;

    ListView mRankList;
    ListViewAutoScrollHelper mSrollHelper;
    RankListAdapter mRankAdapter;

    TextView mRankText, mPlayerText, mAvgText, mTotalText, mArrowText, mHomeShopText, mTimeText, mNumberOnePayerText, mNumberOneAvgText, mNumberOneTotalText, mNumberOneArrowText, mNumberOneHomeShopText,
    mNumberTwoPayerText, mNumberTwoAvgText, mNumberTwoTotalText, mNumberTwoArrowText, mNumberTwoHomeShopText,
    mNumberThreePayerText, mNumberThreeAvgText, mNumberThreeTotalText, mNumberThreeArrowText, mNumberThreeHomeShopText;
    ImageView mRankMemberImg, mRankMedalImg, mSettingButton, mHomeButton, mListAvgTitleLayout, mListTotalTitleLayout, mLogoImg, mNumberOneMemberImg, mNumberTwoMemberImg, mNumberThreeMemberImg;
    Button mListUpButton, mListDownButton;

    ArrayList<Ranking> mArrayList;
    ArrayList<RankingDeatilHistory> mMyHistoryArrayList;

    String mPhone = "", mName = "";
    int mMember = 0;
    int mMenu = 0;
    int mRank = 0;
    int mSearch = 4;
    int mTime = 0;
    int mCount = 0;

    boolean mAvgBoolean = false, mTotalBoolean = false;

    float mTotalAvg = 0.0f;
    int mTotalScore = 0, mTotalArrow = 0;

    RankSelectTask mAuthTask;

    TimerTask mTask;
    Timer mTimer;

    TextView mBannelShopText;
    ViewFlipper mBannelInfoText;

    String mBannelNumber = "";
    String[] mBannelInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mPreference = Preferences.getInstance(mContext);

        setContentView(R.layout.content_ranking);

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


        mLogoImg = (ImageView) findViewById(R.id.ranking_logo);
        mLogoImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        mBeforeLayout = (RelativeLayout) findViewById(R.id.before_layout);
        mBeforeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMember);
                bundle.putInt("menu", mMenu);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        mNextLayout = (RelativeLayout) findViewById(R.id.next_layout);
        mNextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mPhone);
                bundle.putString("name", mName);
                bundle.putInt("member", mMember);
                bundle.putInt("menu", mMenu);
                bundle.putString("bannelNummber", mBannelNumber);
                bundle.putStringArray("bannelInfo", mBannelInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        mRankList = (ListView) findViewById(R.id.ranking_list);
        mRankAdapter = new RankListAdapter(mContext);

        mRankText = (TextView) findViewById(R.id.my_rank_text);
        mPlayerText = (TextView) findViewById(R.id.my_player_text);
        mAvgText = (TextView) findViewById(R.id.my_avg_text);
        mTotalText = (TextView) findViewById(R.id.my_total_text);
        mArrowText = (TextView) findViewById(R.id.my_arrow_text);
        mHomeShopText = (TextView) findViewById(R.id.my_homeshop_text);
        mTimeText = (TextView) findViewById(R.id.rank_update_date_text);

        mNumberOnePayerText = (TextView) findViewById(R.id.player_number_one_text);
        mNumberOneAvgText = (TextView) findViewById(R.id.avg_number_one_text);
        mNumberOneTotalText = (TextView) findViewById(R.id.total_number_one_text);
        mNumberOneArrowText = (TextView) findViewById(R.id.arrow_number_one_text);
        mNumberOneHomeShopText = (TextView) findViewById(R.id.homeshop_number_one_text);

        mNumberTwoPayerText = (TextView) findViewById(R.id.player_number_two_text);
        mNumberTwoAvgText = (TextView) findViewById(R.id.avg_number_two_text);
        mNumberTwoTotalText = (TextView) findViewById(R.id.total_number_two_text);
        mNumberTwoArrowText = (TextView) findViewById(R.id.arrow_number_two_text);
        mNumberTwoHomeShopText = (TextView) findViewById(R.id.homeshop_number_two_text);

        mNumberThreePayerText = (TextView) findViewById(R.id.player_number_three_text);
        mNumberThreeAvgText = (TextView) findViewById(R.id.avg_number_three_text);
        mNumberThreeTotalText = (TextView) findViewById(R.id.total_number_three_text);
        mNumberThreeArrowText = (TextView) findViewById(R.id.arrow_number_three_text);
        mNumberThreeHomeShopText = (TextView) findViewById(R.id.homeshop_number_three_text);

        mNumberOneMemberImg = (ImageView) findViewById(R.id.player_number_one_member_img);
        mNumberTwoMemberImg = (ImageView) findViewById(R.id.player_number_two_member_img);
        mNumberThreeMemberImg = (ImageView) findViewById(R.id.player_number_three_member_img);


        mRankMedalImg = (ImageView) findViewById(R.id.my_ranking_medal_img);
        mRankMemberImg = (ImageView) findViewById(R.id.my_player_member_img);
        mSettingButton = (ImageView) findViewById(R.id.setting_button);
        mHomeButton = (ImageView) findViewById(R.id.home_button);

        mListAvgTitleLayout = (ImageView) findViewById(R.id.list_avg_title_text);
        mListTotalTitleLayout = (ImageView) findViewById(R.id.list_total_title_text);

        mMyHistoryLayout = (RelativeLayout) findViewById(R.id.my_detail_history_layout);
        mListUpButton = (Button) findViewById(R.id.list_up_button);
        mListDownButton = (Button) findViewById(R.id.list_down_button);

        mListAvgTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mArrayList.sort((o1, o2) -> o1.score > 0 ? -1 : o1.score < o2.score ? 1:0);
                if(mAvgBoolean) {
                    mListAvgTitleLayout.setImageResource(R.mipmap.avg_text);
                    mSearch = 1;
//                    Collections.sort(mArrayList, AvgSort);
                }else {
//                    Collections.sort(mArrayList, AvgSort);
                    mListAvgTitleLayout.setImageResource(R.mipmap.avg_text_top);
                    mSearch = 2;
//                    Collections.reverseOrder(AvgSort);
//                    Collections.reverse(mArrayList);
                }
                mAvgBoolean = !mAvgBoolean;

//                mRankAdapter.clear();
//                mRankAdapter.addList(mArrayList);
//
//                mRankAdapter.notifyDataSetChanged();

                mAuthTask = new RankSelectTask(mSearch);
                mAuthTask.execute((Void) null);
            }
        });

        mListTotalTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTotalBoolean) {
                    mListTotalTitleLayout.setImageResource(R.mipmap.total_text);
                    mSearch = 3;
//                    Collections.sort(mArrayList);
                }else{
//                    Collections.sort(mArrayList);
                    mListTotalTitleLayout.setImageResource(R.mipmap.total_text_top);
                    mSearch = 4;
//                    Collections.reverse(mArrayList);
                }
                mTotalBoolean = !mTotalBoolean;

//                mRankAdapter.clear();
//                mRankAdapter.addList(mArrayList);
//
//                mRankAdapter.notifyDataSetChanged();

                mAuthTask = new RankSelectTask(mSearch);
                mAuthTask.execute((Void) null);


            }
        });

        mMyHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate (R.layout.content_ranking_deatil_popup, null);
                alertDialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().setLayout(1590, 820);
                alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                    }
                });

                ImageView closeImg = (ImageView) dialogView.findViewById(R.id.my_rank_close_img);
                closeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                TextView totalAvgText = (TextView) dialogView.findViewById(R.id.total_avg_value_text);
                TextView totalScoreText = (TextView) dialogView.findViewById(R.id.total_score_value_text);
                TextView totalArrowText = (TextView) dialogView.findViewById(R.id.total_arrow_value_text);

                totalAvgText.setText(String.format("%.1f", mTotalAvg));
                totalScoreText.setText(mTotalScore+"");
                totalArrowText.setText(mTotalArrow+"");


                ListView rankDetailListView = (ListView) dialogView.findViewById(R.id.ranking_popup_list);
                RankDetailListAdapter rankDetailListAdapter = new RankDetailListAdapter(mContext);

                rankDetailListView.setAdapter(rankDetailListAdapter);
                rankDetailListAdapter.clear();
                rankDetailListAdapter.addList(mMyHistoryArrayList);
                rankDetailListAdapter.notifyDataSetChanged();

            }
        });

        mListUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRankList.setSelection(0);
            }
        });

        mListDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRankList.setSelection(mRankAdapter.getCount() -1);
            }
        });

        mArrayList = new ArrayList<>();
        mMyHistoryArrayList = new ArrayList<>();

        mRankAdapter.notifyDataSetChanged();
        mRankList.setAdapter(mRankAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPhone = bundle.getString("phone");
        mName = bundle.getString("name");
        mMember = bundle.getInt("member");
        mMenu = bundle.getInt("menu");
        mRank = bundle.getInt("rank");
        mBannelNumber = bundle.getString("bannelNummber");
        mBannelInfo = bundle.getStringArray("bannelInfo");

        mMenu = 4;

        if(mName != null && !"".equals(mName)) {
            mPlayerText.setText(mName + "(" + mPhone.substring(mPhone.length()-4,mPhone.length()) + ")");
            if(mMember == 2) {
                mRankMemberImg.setImageResource(R.mipmap.img_member);
            }else {
                mRankMemberImg.setImageResource(R.mipmap.img_nonmember);
            }
        }

        String franchisee = mPreference.getFranchisee();
        if(franchisee != null || !"".equals(franchisee)) {
            mHomeShopText.setText(franchisee);
        }

        if(mRank == 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.content_step_finish_popup, null);
            alertDialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = alertDialogBuilder.show();
            alertDialog.getWindow().setLayout(1190, 595);
            alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            });

            RelativeLayout mStepFinishPopupLayout = (RelativeLayout) dialogView.findViewById(R.id.content_step_finish);
            mStepFinishPopupLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }else if(mRank == 3) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.content_email_finish_popup, null);
            alertDialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = alertDialogBuilder.show();
            alertDialog.getWindow().setLayout(1190, 595);
            alertDialog.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            });

            RelativeLayout mStepFinishPopupLayout = (RelativeLayout) dialogView.findViewById(R.id.content_step_finish);
            mStepFinishPopupLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }

        mAuthTask = new RankSelectTask(mSearch);
        mAuthTask.execute((Void) null);

        mBannelShopText = (TextView) findViewById(R.id.main_title);
        mBannelInfoText = (ViewFlipper) findViewById(R.id.main_sub_title);
        itemSelected();

        if(!"".equals(mBannelNumber) && mBannelNumber != null) {
            mBannelShopText.setText(mBannelNumber);
            for (String string : mBannelInfo) {
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

        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTime += 1000;
                        mCount++;
                        if(mTime%10000 == 0) {
                            Date from = new Date();
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = transFormat.format(from);
                            mTimeText.setText("UPDATE: " + date);

                            mAuthTask = new RankSelectTask(mSearch);
                            mAuthTask.execute((Void) null);
                        }

                        if(mCount == mRankAdapter.getCount()-3) {
                            mCount = 0;
                            mRankList.smoothScrollToPosition(0);
                        }else {
                            mRankList.smoothScrollToPosition(mCount+2);
                        }
                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 3000, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public class RankSelectTask extends AsyncTask<Void, Void, Boolean> {

        NetworkConnection network;
        int mFlag = 0;

        RankSelectTask(int flag) {
            mFlag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;
            boolean connectDetail = false;
            try {
                // Simulate network access.
//                Thread.sleep(2000);
                network = new NetworkConnection(mContext);
                if(mFlag == 1) {
                    connect = network.rankSelectTotalASC();
                }else if(mFlag == 2) {
                    connect = network.rankSelectTotalDESC();
                }else if(mFlag == 3) {
                    connect = network.rankSelectScoreASC();
                }else if(mFlag == 4) {
                    connect = network.rankSelectScoreDESC();
                }
                connectDetail = network.rankScoreDetailSelect(mName, mPhone);

            } catch (Exception e) {
//            } catch (InterruptedException e) {
                return false;
            }

            if(!connect) {
                return false;
            }else {
                // TODO: register the new account here.
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            Log.d("RankingActivity", "success : " + success);
            if (success) {
                try {
                    mTotalAvg = 0;
                    mTotalScore = 0;
                    mTotalArrow = 0;

                    ArrayList<Ranking> rankingArrayList = new ArrayList<>();
                    ArrayList<RankingDeatilHistory> myRankingDeatilArrayList = new ArrayList<>();
//                    ArrayList<String> phone = new ArrayList<>();
//                    ArrayList<Integer> score = new ArrayList<>();
//                    ArrayList<Integer> arrow = new ArrayList<>();
//                    ArrayList<String> shop = new ArrayList<>();
//                    ArrayList<Integer> member = new ArrayList<>();
                    Log.d("RankingActivity", "length : " + network.mRankInfo.length());
                    for(int i = 0; i < network.mRankInfo.length(); i++) {
                        Ranking ranking = new Ranking();
                        ranking.rank = i+1;
                        ranking.name = Html.fromHtml(network.mRankInfo.optJSONObject(i).getString("name")).toString();
                        ranking.phone = network.mRankInfo.optJSONObject(i).getString("phone");
                        ranking.score = Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("score"));
                        ranking.arrow = Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("arrow"));
                        float avg = (float)((float)ranking.score /(float)ranking.arrow);
                        ranking.avg = avg;
                        ranking.member = Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("member"));
                        ranking.shop = Html.fromHtml(network.mRankInfo.optJSONObject(i).getString("shop")).toString();


                        if(i == 0) {
                            String phone = ranking.phone.substring(ranking.phone.length()-4,ranking.phone.length());
                            mNumberOnePayerText.setText(ranking.name+"("+phone+")");
                            mNumberOneAvgText.setText(String.format("%.1f", ranking.avg));
                            mNumberOneTotalText.setText(ranking.score+"");
                            mNumberOneArrowText.setText("("+ranking.arrow+")");
                            mNumberOneHomeShopText.setText(ranking.shop);


                            if(ranking.member == 2) {
                                mNumberOneMemberImg.setImageResource(R.mipmap.img_member);
                            }else {
                                mNumberOneMemberImg.setImageResource(R.mipmap.img_nonmember);
                            }

                        }else if(i == 1) {
                            String phone = ranking.phone.substring(ranking.phone.length()-4,ranking.phone.length());
                            mNumberTwoPayerText.setText(ranking.name+"("+phone+")");
                            mNumberTwoAvgText.setText(String.format("%.1f", ranking.avg));
                            mNumberTwoTotalText.setText(ranking.score+"");
                            mNumberTwoArrowText.setText("("+ranking.arrow+")");
                            mNumberTwoHomeShopText.setText(ranking.shop);


                            if(ranking.member == 2) {
                                mNumberTwoMemberImg.setImageResource(R.mipmap.img_member);
                            }else {
                                mNumberTwoMemberImg.setImageResource(R.mipmap.img_nonmember);
                            }

                        }else if(i == 2) {
                            String phone = ranking.phone.substring(ranking.phone.length()-4,ranking.phone.length());
                            mNumberThreePayerText.setText(ranking.name+"("+phone+")");
                            mNumberThreeAvgText.setText(String.format("%.1f", ranking.avg));
                            mNumberThreeTotalText.setText(ranking.score+"");
                            mNumberThreeArrowText.setText("("+ranking.arrow+")");
                            mNumberThreeHomeShopText.setText(ranking.shop);


                            if(ranking.member == 2) {
                                mNumberThreeMemberImg.setImageResource(R.mipmap.img_member);
                            }else {
                                mNumberThreeMemberImg.setImageResource(R.mipmap.img_nonmember);
                            }

                        }else {
                            rankingArrayList.add(ranking);
                        }

//                        name.add(Html.fromHtml(network.mRankInfo.optJSONObject(i).getString("name")).toString());
//                        name.add(URLEncoder.encode(network.mRankInfo.optJSONObject(i).getString("name")));
//                        phone.add(network.mRankInfo.optJSONObject(i).getString("phone"));
//                        score.add(Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("score")));
//                        arrow.add(Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("arrow")));
//                        shop.add(Html.fromHtml(network.mRankInfo.optJSONObject(i).getString("shop")).toString());
//                        member.add(Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("member")));
//                        Log.d("RankingActivity", "name : " + rankingArrayList.get(i).name);
//                        Log.d("RankingActivity", "phone : " + rankingArrayList.get(i).phone);
//                        Log.d("RankingActivity", "score : " + rankingArrayList.get(i).score);
//                        Log.d("RankingActivity", "arrow : " + rankingArrayList.get(i).arrow);
//                        Log.d("RankingActivity", "shop : " + rankingArrayList.get(i).shop);
//                        Log.d("RankingActivity", "member : " + rankingArrayList.get(i).member);

                        if(mName.equals(ranking.name) && mPhone.equals(ranking.phone)) {
//                            float avgValue = (float)((float)rankingArrayList.get(i).score/(float)rankingArrayList.get(i).arrow);
                            mRankText.setText(""+ ranking.rank);
                            mAvgText.setText(String.format("%.1f", ranking.avg));

                            mTotalText.setText(ranking.score+"");
                            mArrowText.setText("("+ranking.arrow+")");

                            mHomeShopText.setText(ranking.shop);
                        }
                    }

                    mArrayList.addAll(rankingArrayList);

                    mRankAdapter.clear();
                    mRankAdapter.addList(rankingArrayList);

                    mRankAdapter.notifyDataSetChanged();

                    for(int i = 0; i < network.mMyRankDeatilInfo.length(); i++) {
                        RankingDeatilHistory myRanking = new RankingDeatilHistory();
                        myRanking.name = Html.fromHtml(network.mMyRankDeatilInfo.optJSONObject(i).getString("name")).toString();
                        myRanking.date = network.mMyRankDeatilInfo.optJSONObject(i).getString("date");
                        myRanking.score = Integer.parseInt(network.mMyRankDeatilInfo.optJSONObject(i).getString("score"));
                        myRanking.arrow = Integer.parseInt(network.mMyRankDeatilInfo.optJSONObject(i).getString("arrow"));
                        float avg = (float)((float)myRanking.score /(float)myRanking.arrow);
                        myRanking.avg = avg;
                        myRankingDeatilArrayList.add(myRanking);

                        mTotalAvg += avg;
                        mTotalScore += myRanking.score;
                        mTotalArrow += myRanking.arrow;
                    }

                    mMyHistoryArrayList.clear();
                    mMyHistoryArrayList.addAll(myRankingDeatilArrayList);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //finish();
            } else {
                Toast.makeText(mContext, "인터넷에 견결할 수 없습니다. 인터넷 상태를 확인해 주세요, ", Toast.LENGTH_LONG);

//                ArrayList<String> name = new ArrayList<>();
//                ArrayList<String> phone = new ArrayList<>();
//                ArrayList<Integer> score = new ArrayList<>();
//                ArrayList<Integer> arrow = new ArrayList<>();
//                ArrayList<String> shop = new ArrayList<>();
//                ArrayList<Integer> member = new ArrayList<>();


//                name.add("유한철");
//                phone.add("01083728331");
//                score.add(17780);
//                arrow.add(1800);
//                shop.add("수원권선점");
//                member.add(2);

//                name.add("박보검");
//                phone.add("01083728321");
//                score.add(16780);
//                arrow.add(1800);
//                shop.add("수원권선점");
//                member.add(2);

//                name.add("송강호");
//                phone.add("01083729323");
//                score.add(1323);
//                arrow.add(140);
//                shop.add("수원권선점");
//                member.add(2);

//                name.add("김고은");
//                phone.add("01083721182");
//                score.add(2532);
//                arrow.add(290);
//                shop.add("서울 강남점");
//                member.add(2);

//                name.add("김우빈");
//                phone.add("01083726692");
//                score.add(1773);
//                arrow.add(180);
//                shop.add("평택점");
//                member.add(1);


//                name.add("신민아");
//                phone.add("01083723333");
//                score.add(328);
//                arrow.add(40);
//                shop.add("진주점");
//                member.add(1);

//                name.add("최지우");
//                phone.add("01083722312");
//                score.add(321);
//                arrow.add(400);
//                shop.add("평택점");
//                member.add(1);

//                name.add("김수현");
//                phone.add("01083726692");
//                score.add(1892);
//                arrow.add(200);
//                shop.add("진주점");
//                member.add(1);

//                name.add("아이린");
//                phone.add("01083726693");
//                score.add(132);
//                arrow.add(15);
//                shop.add("서울 강남점");
//                member.add(1);

                mRankAdapter.clear();
//                mRankAdapter.addList(name, phone, score, arrow, shop, member);

                mRankAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public class Ranking{
        int rank;
        String name;
        String phone;
        int member;
        int score;
        int arrow;
        float avg;
        String shop;
    }

    public class RankingDeatilHistory{
        String name;
        String date;
        int arrow;
        float avg;
        int score;
    }



    /*Comparator for sorting the list by roll no*/
    private static Comparator<Ranking> AvgSort = new Comparator<Ranking>() {

        @Override
        public int compare(Ranking s1, Ranking s2) {
            float rollno1 = s1.avg;
            float rollno2 = s2.avg;

	   /*For ascending order*/
            if (rollno1 > rollno2) {
                return 1;
            } else if (rollno1 < rollno2) {
                return -1;
            }
            return 0;
        }

    };

    private void itemSelected() {
        mBannelInfoText.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_up_in));
        mBannelInfoText.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_up_out));
        mBannelInfoText.startFlipping();
    };
}

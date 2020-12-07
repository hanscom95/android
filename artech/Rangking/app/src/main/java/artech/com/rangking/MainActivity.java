package artech.com.rangking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import artech.com.rangking.RankListAdapter;

public class MainActivity extends AppCompatActivity {

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    Context mContext;

    ListView mMemberList, mNonMemberList;
    RankListAdapter mMemberAdapter, mNonMemberAdapter;
    TextView mDateText, mNumberOnePayerText, mNumberOneScoreText, mNumberOneHomeShopText, mNonNumberOnePayerText, mNonNumberOneScoreText, mNonNumberOneHomeShopText;
    ImageView mNumberOneMemberImg, mNonNumberOneMemberImg;

    RankSelectTask mAuthTask;

    TimerTask mTask;
    Timer mTimer;

    int mTime = 0;
    int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            }
        });

        mMemberList = (ListView) findViewById(R.id.ranking_list);
        mNonMemberList = (ListView) findViewById(R.id.non_ranking_list);

        mMemberAdapter = new RankListAdapter(mContext);
        mNonMemberAdapter = new RankListAdapter(mContext);

        mDateText = (TextView) findViewById(R.id.date_text);

        mNumberOnePayerText = (TextView) findViewById(R.id.player_number_one_text);
        mNumberOneScoreText = (TextView) findViewById(R.id.avg_number_one_text);
        mNumberOneHomeShopText = (TextView) findViewById(R.id.homeshop_number_one_text);

        mNonNumberOnePayerText = (TextView) findViewById(R.id.non_player_number_one_text);
        mNonNumberOneScoreText = (TextView) findViewById(R.id.non_avg_number_one_text);
        mNonNumberOneHomeShopText = (TextView) findViewById(R.id.non_homeshop_number_one_text);

        mNumberOneMemberImg = (ImageView) findViewById(R.id.player_number_one_member_img) ;
        mNonNumberOneMemberImg = (ImageView) findViewById(R.id.non_player_number_one_member_img) ;


//        mMemberAdapter.addList(mArrayList);
//        mNonMemberAdapter.addList(mArrayList);

        mMemberAdapter.notifyDataSetChanged();
        mMemberList.setAdapter(mMemberAdapter);

        mNonMemberAdapter.notifyDataSetChanged();
        mNonMemberList.setAdapter(mNonMemberAdapter);


        mAuthTask = new RankSelectTask();
        mAuthTask.execute((Void) null);


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
                            mDateText.setText("UPDATE: " + date);

                            mAuthTask = new RankSelectTask();
                            mAuthTask.execute((Void) null);
                        }

                        if(mCount == mMemberAdapter.getCount()-1) {
                            mCount = 0;
                            mMemberList.smoothScrollToPosition(0);
                            mNonMemberList.smoothScrollToPosition(0);
                        }else {
                            mMemberList.smoothScrollToPosition(mCount+1);
                            mNonMemberList.smoothScrollToPosition(mCount+1);
                        }

                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 3000, 3000);
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

        RankSelectTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;
            try {
                // Simulate network access.
//                Thread.sleep(2000);
                network = new NetworkConnection(mContext);
                connect = network.rankSelectTotalDESC();
                connect = network.rankSelectScoreDESC();

            } catch (Exception e) {
//            } catch (InterruptedException e) {
                return false;
            }

            if (!connect) {
                return false;
            } else {
                // TODO: register the new account here.
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                try {
                    ArrayList<Ranking> rankingAvgArrayList = new ArrayList<>();
                    ArrayList<Ranking> rankingTotalArrayList = new ArrayList<>();


                    for (int i = 0; i < network.mRankInfo.length(); i++) {
                        Ranking ranking = new Ranking();
                        ranking.rank = i + 1;
                        ranking.name = Html.fromHtml(network.mRankInfo.optJSONObject(i).getString("name")).toString();
                        ranking.phone = network.mRankInfo.optJSONObject(i).getString("phone");
                        ranking.score = Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("score"));
                        ranking.arrow = Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("arrow"));
                        float avg = (float)((float)ranking.score /(float)ranking.arrow);
                        ranking.avg = avg;
                        ranking.member = Integer.parseInt(network.mRankInfo.optJSONObject(i).getString("member"));
                        ranking.shop = Html.fromHtml(network.mRankInfo.optJSONObject(i).getString("shop")).toString();
                        ranking.flag = 1;
//                            Log.d("MainActivity", "i : " + i + " / name : " + ranking.name);

                        if(i == 0) {
                            String phone = ranking.phone.substring(ranking.phone.length()-4,ranking.phone.length());
                            mNumberOnePayerText.setText(ranking.name+"("+phone+")");
                            mNumberOneScoreText.setText(String.format("%.1f", ranking.avg));
                            mNumberOneHomeShopText.setText(ranking.shop);

                            if(ranking.member == 2) {
                                mNumberOneMemberImg.setImageResource(R.mipmap.img_member);
                            }else {
                                mNumberOneMemberImg.setImageResource(R.mipmap.img_nonmember);
                            }
                        }else {
                            rankingAvgArrayList.add(ranking);
                        }
                    }

                    for (int i = 0; i < network.mScoreInfo.length(); i++) {
                        Ranking ranking = new Ranking();
                        ranking.rank = i + 1;
                        ranking.name = Html.fromHtml(network.mScoreInfo.optJSONObject(i).getString("name")).toString();
                        ranking.phone = network.mScoreInfo.optJSONObject(i).getString("phone");
                        ranking.score = Integer.parseInt(network.mScoreInfo.optJSONObject(i).getString("score"));
                        ranking.arrow = Integer.parseInt(network.mScoreInfo.optJSONObject(i).getString("arrow"));
                        float avg = (float)((float)ranking.score /(float)ranking.arrow);
                        ranking.avg = avg;
                        ranking.member = Integer.parseInt(network.mScoreInfo.optJSONObject(i).getString("member"));
                        ranking.shop = Html.fromHtml(network.mScoreInfo.optJSONObject(i).getString("shop")).toString();
                        ranking.flag = 2;

                        if(i == 0) {
                            String phone = ranking.phone.substring(ranking.phone.length()-4,ranking.phone.length());
                            mNonNumberOnePayerText.setText(ranking.name+"("+phone+")");
                            mNonNumberOneScoreText.setText(ranking.score+"("+ranking.arrow+")");
                            mNonNumberOneHomeShopText.setText(ranking.shop);

                            if(ranking.member == 2) {
                                mNonNumberOneMemberImg.setImageResource(R.mipmap.img_member);
                            }else {
                                mNonNumberOneMemberImg.setImageResource(R.mipmap.img_nonmember);
                            }
                        }else {
                            rankingTotalArrayList.add(ranking);
                        }
                    }


//                    Log.d("MainActivity", "rankingAvgArrayList : " + rankingAvgArrayList.size() + " / name : " + rankingAvgArrayList.get(0).name);
                    mMemberAdapter.clear();
                    mMemberAdapter.addList(rankingAvgArrayList);
                    mMemberAdapter.notifyDataSetChanged();

                    mNonMemberAdapter.clear();
                    mNonMemberAdapter.addList(rankingTotalArrayList);
                    mNonMemberAdapter.notifyDataSetChanged();


                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Ranking{
        int rank;
        String name;
        String phone;
        int member;
        int score;
        int arrow;
        float avg;
        String shop;
        int flag;
    }
}

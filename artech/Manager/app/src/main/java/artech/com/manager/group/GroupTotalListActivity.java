package artech.com.manager.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import artech.com.manager.MainActivity;
import artech.com.manager.R;
import artech.com.manager.admin.AdminListActivity;
import artech.com.manager.booking.BookingListActivity;
import artech.com.manager.guide.GuideActivity;
import artech.com.manager.tournament.Tournament16Activity;
import artech.com.manager.tournament.Tournament8Activity;
import artech.com.manager.utility.Mail;
import artech.com.manager.utility.SendEmailAsyncTask;

import static artech.com.manager.group.GroupTotalListAdapter.*;

public class GroupTotalListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    TextView mTeamTitleText, mDateText;
    static Button mPrintButton;
    Button mSendButton;
    RelativeLayout mFirstButtonLayout;
    RelativeLayout mSecondButtonLayout;
    static RelativeLayout mEmailLayout;
    static LinearLayout mGroupTotalLayout;
    View mPrintLayout;
    ListView mGroupFirstList, mGroupSecondList;
    GroupTotalListAdapter mGroupFirstListAdapter, mGroupSecondListAdapter;
    EditText mEmailEdit;
    ProgressBar mProgressBar;

    String mTeamName = "", mEmail = "";
    int mPeople = 0, mTeam = 0;
    boolean buttonBoolean = false;
    static boolean emailBoolean = false;

    ArrayList<String> mNameArrayList;
    ArrayList<Integer> mScore1ArrayList;
    ArrayList<Integer> mScore2ArrayList;
    ArrayList<Integer> mScore3ArrayList;
    ArrayList<Integer> mScore4ArrayList;
    ArrayList<Integer> mScore5ArrayList;
    ArrayList<Integer> mScore6ArrayList;
    ArrayList<Integer> mScore7ArrayList;
    ArrayList<Integer> mScore8ArrayList;
    ArrayList<Integer> mScore9ArrayList;
    ArrayList<Integer> mScore10ArrayList;
    ArrayList<Ranking> mRankingArrayList, mRankingLeftArrayList, mRankingRightArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_group_total_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTeam = bundle.getInt("team");
        mPeople  = bundle.getInt("people");
        mTeamName = bundle.getString("name");
        mEmail = bundle.getString("email");
        mNameArrayList = bundle.getStringArrayList("nameArray");
        mScore1ArrayList = bundle.getIntegerArrayList("score1Array");
        mScore2ArrayList = bundle.getIntegerArrayList("score2Array");
        mScore3ArrayList = bundle.getIntegerArrayList("score3Array");
        mScore4ArrayList = bundle.getIntegerArrayList("score4Array");
        mScore5ArrayList = bundle.getIntegerArrayList("score5Array");
        mScore6ArrayList = bundle.getIntegerArrayList("score6Array");
        mScore7ArrayList = bundle.getIntegerArrayList("score7Array");
        mScore8ArrayList = bundle.getIntegerArrayList("score8Array");
        mScore9ArrayList = bundle.getIntegerArrayList("score9Array");
        mScore10ArrayList = bundle.getIntegerArrayList("score10Array");

        mRankingArrayList = new ArrayList<Ranking>();
        mRankingLeftArrayList = new ArrayList<Ranking>();
        mRankingRightArrayList = new ArrayList<Ranking>();

        int rank = 0;

        for(int i = 0; i < mNameArrayList.size(); i++) {
            GroupTotalListAdapter.Ranking rankingArray = new GroupTotalListAdapter.Ranking();
            rankingArray.rank = rank+1;
            rankingArray.name = mNameArrayList.get(i);
            rankingArray.score[0] = mScore1ArrayList.get(i);
            rankingArray.score[1] = mScore2ArrayList.get(i);
            rankingArray.score[2] = mScore3ArrayList.get(i);
            rankingArray.score[3] = mScore4ArrayList.get(i);
            rankingArray.score[4] = mScore5ArrayList.get(i);
            rankingArray.score[5] = mScore6ArrayList.get(i);
            rankingArray.score[6] = mScore7ArrayList.get(i);
            rankingArray.score[7] = mScore8ArrayList.get(i);
            rankingArray.score[8] = mScore9ArrayList.get(i);
            rankingArray.score[9] = mScore10ArrayList.get(i);
            rankingArray.total = (mScore1ArrayList.get(i) > 10 ? 10 : mScore1ArrayList.get(i)) +
                    (mScore2ArrayList.get(i) > 10 ? 10 : mScore2ArrayList.get(i)) +
                    (mScore3ArrayList.get(i) > 10 ? 10 : mScore3ArrayList.get(i)) +
                    (mScore4ArrayList.get(i) > 10 ? 10 : mScore4ArrayList.get(i)) +
                    (mScore5ArrayList.get(i) > 10 ? 10 : mScore5ArrayList.get(i)) +
                    (mScore6ArrayList.get(i) > 10 ? 10 : mScore6ArrayList.get(i)) +
                    (mScore7ArrayList.get(i) > 10 ? 10 : mScore7ArrayList.get(i)) +
                    (mScore8ArrayList.get(i) > 10 ? 10 : mScore8ArrayList.get(i)) +
                    (mScore9ArrayList.get(i) > 10 ? 10 : mScore9ArrayList.get(i)) +
                    (mScore10ArrayList.get(i) > 10 ? 10 : mScore10ArrayList.get(i));
            rankingArray.sort = (mScore1ArrayList.get(i) +
                    mScore2ArrayList.get(i) +
                    mScore3ArrayList.get(i) +
                    mScore4ArrayList.get(i) +
                    mScore5ArrayList.get(i) +
                    mScore6ArrayList.get(i) +
                    mScore7ArrayList.get(i) +
                    mScore8ArrayList.get(i) +
                    mScore9ArrayList.get(i) +
                    mScore10ArrayList.get(i));
            rankingArray.flag = true;

            mRankingArrayList.add(rankingArray);
//            if (i < 25) {
//                mRankingLeftArrayList.add(rankingArray);
//            } else {
//                mRankingRightArrayList.add(rankingArray);
//            }

            rank++;
        }
        Collections.sort(mRankingArrayList);

        for(int i = 0; i < mRankingArrayList.size(); i++) {
            mRankingArrayList.get(i).rank = (i+1);

            if (i < 25) {
                mRankingLeftArrayList.add(mRankingArrayList.get(i));
            } else {
                mRankingRightArrayList.add(mRankingArrayList.get(i));
            }
        }

        /*if(mRankingLeftArrayList.size() != 25) {
            for(int i = 0; i < (25-mRankingLeftArrayList.size()); i++) {
                GroupTotalListAdapter.Ranking rankingArray = new GroupTotalListAdapter.Ranking();
                rankingArray.flag = false;
                mRankingLeftArrayList.add(rankingArray);
            }
        }

        if(mRankingRightArrayList.size() != 25) {
            for(int i = 0; i < (25-mRankingRightArrayList.size()); i++) {
                GroupTotalListAdapter.Ranking rankingArray = new GroupTotalListAdapter.Ranking();
                rankingArray.flag = false;
                mRankingRightArrayList.add(rankingArray);
            }
        }*/

        mTeamTitleText = (TextView) findViewById(R.id.team_title);
        mTeamTitleText.setText(mTeamName);

        mDateText = (TextView)findViewById(R.id.team_record_date);

        Date from = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = transFormat.format(from);
        mDateText.setText(date);



//        ArrayList<Ranking> rankingArrayList = new ArrayList<>();
//        Ranking ranking = new Ranking();
//
//        for(int i = 0; i < mNameArrayList.size(); i++) {
//            ranking.name = mNameArrayList.get(i);
//            ranking.score[0] = mScore1ArrayList.get(i);
//            ranking.score[1] = mScore2ArrayList.get(i);
//            ranking.score[2] = mScore3ArrayList.get(i);
//            ranking.score[3] = mScore4ArrayList.get(i);
//            ranking.score[4] = mScore5ArrayList.get(i);
//            ranking.score[5] = mScore6ArrayList.get(i);
//            ranking.score[6] = mScore7ArrayList.get(i);
//            ranking.score[7] = mScore8ArrayList.get(i);
//            ranking.score[8] = mScore9ArrayList.get(i);
//            ranking.score[9] = mScore10ArrayList.get(i);
//            ranking.total = (mScore1ArrayList.get(i)>10?10:mScore1ArrayList.get(i))+
//                            (mScore2ArrayList.get(i)>10?10:mScore2ArrayList.get(i))+
//                            (mScore3ArrayList.get(i)>10?10:mScore3ArrayList.get(i))+
//                            (mScore4ArrayList.get(i)>10?10:mScore4ArrayList.get(i))+
//                            (mScore5ArrayList.get(i)>10?10:mScore5ArrayList.get(i))+
//                            (mScore6ArrayList.get(i)>10?10:mScore6ArrayList.get(i))+
//                            (mScore7ArrayList.get(i)>10?10:mScore7ArrayList.get(i))+
//                            (mScore8ArrayList.get(i)>10?10:mScore8ArrayList.get(i))+
//                            (mScore9ArrayList.get(i)>10?10:mScore9ArrayList.get(i))+
//                            (mScore10ArrayList.get(i)>10?10:mScore10ArrayList.get(i));
//
//            rankingArrayList.add(ranking);
//            Log.d("ScaleListActivity", "i :" +i+ " / name : " + rankingArrayList.get(i).name);
//            Log.d("ScaleListActivity", "mScore1ArrayList : " + rankingArrayList.get(i).score[0]);
//            Log.d("ScaleListActivity", "mScore2ArrayList : " + rankingArrayList.get(i).score[1]);
//            Log.d("ScaleListActivity", "mScore3ArrayList : " + rankingArrayList.get(i).score[2]);
//            Collections.sort(mArrayList);
//        }

//        mArrayList.addAll(rankingArrayList);

        mPrintLayout = (View) findViewById(R.id.content_scale_list_layout);
        mEmailLayout = (RelativeLayout) findViewById(R.id.email_layout);
        mGroupTotalLayout = (LinearLayout) findViewById(R.id.group_total_main_layout);

        mEmailEdit = (EditText) findViewById(R.id.email_edit);

        mSendButton = (Button) findViewById(R.id.send_button);
        mPrintButton = (Button) findViewById(R.id.print_button);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mEmailEdit.setText(mEmail);

        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrintButton.setVisibility(View.GONE);

                View v1 = getWindow().getDecorView().getRootView().findViewById(R.id.group_total_coordinatorLayout);
                v1.setDrawingCacheEnabled(true);

                Bitmap printBitmap = Bitmap.createBitmap(v1.getDrawingCache());
                String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                // Get Absolute Path in External Sdcard
                String foler_name = "/shootingzone/";
                String file_name = "group_total_list.jpg";
                String string_path = ex_storage+foler_name;


                File file_path;
                try{
                    file_path = new File(string_path);
                    if(!file_path.isDirectory()){
                        file_path.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(string_path+file_name);

                    printBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+string_path+file_name)));
                    out.close();

                }catch(FileNotFoundException exception){
                    Log.e("FileNotFoundException", exception.getMessage());
                }catch(IOException exception){
                    Log.e("IOException", exception.getMessage());
                }

                mEmailLayout.setVisibility(View.VISIBLE);
                mGroupTotalLayout.setVisibility(View.GONE);
                emailBoolean = true;
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String[] recipients = { mEmailEdit.getText().toString(), "archerycafe@naver.com" };
                SendEmailAsyncTask email = new SendEmailAsyncTask("GroupTotalListActivity", fab, mProgressBar);
//                email.m = new Mail(user.getText().toString(), pass.getText()
//                        .toString());
                email.m = new Mail();
//                email.m.set_from("");
                email.m.setBody("슈팅존을 이용해 주셔서 감사합니다.");
                email.m.setPath("group_total_list.jpg");
                email.m.set_to(recipients);
                email.m.set_subject("SHOOTING ZONE");
                email.execute();
            }
        });

        mGroupFirstList = (ListView) findViewById(R.id.group_list_first_total_view);
        mGroupSecondList = (ListView) findViewById(R.id.group_list_second_total_view);

        mGroupFirstListAdapter = new GroupTotalListAdapter(mContext);
        mGroupSecondListAdapter = new GroupTotalListAdapter(mContext);

//        mGroupSecondListAdapter.addList(mNameArrayList, mScore1ArrayList, mScore2ArrayList, mScore3ArrayList, mScore4ArrayList
//                , mScore5ArrayList, mScore6ArrayList, mScore7ArrayList, mScore8ArrayList, mScore9ArrayList, mScore10ArrayList
//                , mNameArrayList.size(), mTeam);
        mGroupFirstListAdapter.addList(mRankingLeftArrayList);
        mGroupFirstListAdapter.notifyDataSetChanged();
        mGroupFirstList.setAdapter(mGroupFirstListAdapter);

//        mGroupSecondListAdapter.addList(mNameArrayList, mScore1ArrayList, mScore2ArrayList, mScore3ArrayList, mScore4ArrayList
//                , mScore5ArrayList, mScore6ArrayList, mScore7ArrayList, mScore8ArrayList, mScore9ArrayList, mScore10ArrayList
//                , mNameArrayList.size(), mTeam);
        mGroupSecondListAdapter.addList(mRankingRightArrayList);
        mGroupSecondListAdapter.notifyDataSetChanged();
        mGroupSecondList.setAdapter(mGroupSecondListAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(buttonBoolean) {
            buttonBoolean = false;
            mSecondButtonLayout.setVisibility(View.GONE);
            mFirstButtonLayout.setVisibility(View.VISIBLE);
        } if(emailBoolean){
            emailBoolean = false;
            mEmailLayout.setVisibility(View.GONE);
            mGroupTotalLayout.setVisibility(View.VISIBLE);
            mPrintButton.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_guide) {
            Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_ranking) {
        } else if (id == R.id.nav_8tournament) {
            Intent intent = new Intent(getApplicationContext(), Tournament8Activity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("nameArray", new ArrayList<String>());
            bundle.putString("name", "");
            bundle.putString("email", "");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_16tournament) {
            Intent intent = new Intent(getApplicationContext(), Tournament16Activity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("nameArray", new ArrayList<String>());
            bundle.putString("name", "");
            bundle.putString("email", "");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplicationContext(), BookingListActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_admin) {
            Intent intent = new Intent(getApplicationContext(), AdminListActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void setVisibleEmailView() {
        mEmailLayout.setVisibility(View.GONE);
        mGroupTotalLayout.setVisibility(View.VISIBLE);
        mPrintButton.setVisibility(View.VISIBLE);
        emailBoolean = false;
    }
}

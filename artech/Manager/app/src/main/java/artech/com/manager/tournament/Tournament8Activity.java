package artech.com.manager.tournament;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import artech.com.manager.MainActivity;
import artech.com.manager.R;
import artech.com.manager.admin.AdminListActivity;
import artech.com.manager.booking.BookingListActivity;
import artech.com.manager.group.GroupActivity;
import artech.com.manager.group.GroupListActivity;
import artech.com.manager.guide.GuideActivity;
import artech.com.manager.utility.Mail;
import artech.com.manager.utility.SendEmailAsyncTask;

public class Tournament8Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    EditText mEmailEdit;
    Button mPrintButton, mSendButton, m34RankingButton;
    ProgressBar mProgressBar;
    TextView mTeamText, mDateText;

    static RelativeLayout mTournamentLayout;
    static RelativeLayout mEmailLayout;

    View mLeft34RankView, mRight34RankView;

    String mTeamName = "", mEmail = "";
    boolean finishBoolean = false;
    static boolean emailBoolean = false;

    int m1R1ThScore = -1, m1R2ThScore = -1, m1R3ThScore = -1, m1R4ThScore = -1, m1R5ThScore = -1, m1R6ThScore = -1, m1R7ThScore = -1, m1R8ThScore = -1,
        m2R1ThScore = -1, m2R2ThScore = -1, m2R3ThScore = -1, m2R4ThScore = -1,
        m3R1ThScore = -1, m3R2ThScore = -1;

    ArrayList<String> mNameArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_tournament8);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mEmailLayout = (RelativeLayout) findViewById(R.id.email_layout);
        mTournamentLayout = (RelativeLayout) findViewById(R.id.tournament_layout);

        mEmailEdit = (EditText) findViewById(R.id.email_edit);

        mSendButton = (Button) findViewById(R.id.send_button);
        mPrintButton = (Button) findViewById(R.id.print_button);
        m34RankingButton = (Button) findViewById(R.id.rangking_3_4_button);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mTeamText = (TextView) findViewById(R.id.team_name_text);
        mDateText = (TextView) findViewById(R.id.date_text);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mNameArrayList = bundle.getStringArrayList("nameArray");
        mTeamName = bundle.getString("name");
        mEmail = bundle.getString("email");
        if(!"".equals(mTeamName)) {
            mTeamText.setText(mTeamName);
        }
        if(!"".equals(mEmail)) {
            mEmailEdit.setText(mEmail);
        }

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

        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrintButton.setVisibility(View.GONE);
                m34RankingButton.setVisibility(View.GONE);
                View v1 = getWindow().getDecorView().getRootView().findViewById(R.id.tournament8_coordinatorLayout);
                v1.setDrawingCacheEnabled(true);

                Bitmap printBitmap = Bitmap.createBitmap(v1.getDrawingCache());
                String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                // Get Absolute Path in External Sdcard
                String foler_name = "/shootingzone/";
                String file_name = "tournament8.jpg";
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
                mTournamentLayout.setVisibility(View.GONE);
                emailBoolean = true;
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mPrintButton.setVisibility(View.VISIBLE);
                m34RankingButton.setVisibility(View.VISIBLE);
                String[] recipients = { mEmailEdit.getText().toString(), "archerycafe@naver.com" };
                SendEmailAsyncTask email = new SendEmailAsyncTask("Tournament8Activity", fab, mProgressBar);
//                email.m = new Mail(user.getText().toString(), pass.getText()
//                        .toString());
                email.m = new Mail();
//                email.m.set_from("");
                email.m.setBody("슈팅존을 이용해 주셔서 감사합니다.");
                email.m.setPath("tournament8.jpg");
                email.m.set_to(recipients);
                email.m.set_subject("SHOOTING ZONE");
                email.execute();
            }
        });

        m34RankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLeft34RankView == null || mRight34RankView == null) {
                    return;
                }

                final int[] leftRightInt = {0};


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate (R.layout.content_3_4_rangking_popup, null);
                alertDialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().setLayout(761, 900);
                alertDialog.setCanceledOnTouchOutside(false);

                final TextView leftNameText = (TextView) alertDialog.findViewById(R.id.rd_left_name_text);
                final TextView rightNameText = (TextView) alertDialog.findViewById(R.id.rd_right_name_text);

                TextView nameText;
                switch (mLeft34RankView.getId()) {
                    case R.id.rd2_1th_score_layout:
                        nameText = (TextView) findViewById(R.id.rd2_1th_left_name_text);
                        leftNameText.setText(nameText.getText().toString());
                        break;
                    case R.id.rd2_2th_score_layout:
                        nameText = (TextView) findViewById(R.id.rd2_2th_left_name_text);
                        leftNameText.setText(nameText.getText().toString());
                        break;

                    default:
                        break;
                }

                switch (mRight34RankView.getId()) {
                    case R.id.rd2_3th_score_layout:
                        nameText = (TextView) findViewById(R.id.rd2_3th_left_name_text);
                        rightNameText.setText(nameText.getText().toString());
                        break;
                    case R.id.rd2_4th_score_layout:
                        nameText = (TextView) findViewById(R.id.rd2_4th_left_name_text);
                        rightNameText.setText(nameText.getText().toString());
                        break;

                    default:
                        break;
                }

                final EditText leftScoreEdit = (EditText) alertDialog.findViewById(R.id.rd_left_score_text);
                final EditText rightScoreEdit = (EditText) alertDialog.findViewById(R.id.rd_right_score_text);

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int leftScore = 0;
                        int rightScore = 0;

                        if(!"".equals(leftScoreEdit.getText().toString()) && !"".equals(rightScoreEdit.getText().toString())) {
                            leftScore = Integer.parseInt(leftScoreEdit.getText().toString());
                            rightScore = Integer.parseInt(rightScoreEdit.getText().toString());

                            RelativeLayout winnerLayout = (RelativeLayout) alertDialog.findViewById(R.id.rd_top_score_layout);

                            TextView winnerNameText = (TextView) alertDialog.findViewById(R.id.rd_top_name_text);
                            TextView winnerScoreText = (TextView) alertDialog.findViewById(R.id.rd_top_score_text);

                            ImageView defualtLineImg = (ImageView)alertDialog.findViewById(R.id.rd_line_default);
                            ImageView leftWinLineImg = (ImageView)alertDialog.findViewById(R.id.rd_line_left_win);
                            ImageView rightWinLineImg = (ImageView)alertDialog.findViewById(R.id.rd_line_right_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(leftScore > rightScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                winnerLayout.setBackgroundResource(R.mipmap.box34_win);
                                winnerScoreText.setText(leftScoreEdit.getText().toString());
                                winnerNameText.setText(leftNameText.getText().toString());

                                leftRightInt[0] = 1;
                            }else if(leftScore < rightScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                winnerLayout.setBackgroundResource(R.mipmap.box34_win);
                                winnerScoreText.setText(rightScoreEdit.getText().toString());
                                winnerNameText.setText(rightNameText.getText().toString());

                                leftRightInt[0] = 2;
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);

                                winnerLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerScoreText.setText("");
                                winnerNameText.setText("");

                                leftRightInt[0] = 0;
                            }
                        }
                    }
                };

                leftScoreEdit.addTextChangedListener(textWatcher);
                rightScoreEdit.addTextChangedListener(textWatcher);

                Button confirmButton = (Button) alertDialog.findViewById(R.id.confirm_button);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView r1RakingImg = (ImageView) findViewById(R.id.rd2_1th_34_ranking_img);
                        ImageView r2RakingImg = (ImageView) findViewById(R.id.rd2_2th_34_ranking_img);
                        ImageView r3RakingImg = (ImageView) findViewById(R.id.rd2_3th_34_ranking_img);
                        ImageView r4RakingImg = (ImageView) findViewById(R.id.rd2_4th_34_ranking_img);

                        r1RakingImg.setVisibility(View.INVISIBLE);
                        r2RakingImg.setVisibility(View.INVISIBLE);
                        r3RakingImg.setVisibility(View.INVISIBLE);
                        r4RakingImg.setVisibility(View.INVISIBLE);

                        if(leftRightInt[0] == 1) {
                            switch (mLeft34RankView.getId()) {
                                case R.id.rd2_1th_score_layout:
                                    r1RakingImg.setVisibility(View.VISIBLE);
                                    break;
                                case R.id.rd2_2th_score_layout:
                                    r2RakingImg.setVisibility(View.VISIBLE);
                                    break;

                                default:
                                    break;
                            }
                        }else if(leftRightInt[0] == 2) {
                            switch (mRight34RankView.getId()) {
                                case R.id.rd2_3th_score_layout:
                                    r3RakingImg.setVisibility(View.VISIBLE);
                                    break;
                                case R.id.rd2_4th_score_layout:
                                    r4RakingImg.setVisibility(View.VISIBLE);
                                    break;

                                default:
                                    break;
                            }
                        }
                        alertDialog.dismiss();
                    }
                });
            }
        });


        Date from = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String date = transFormat.format(from);
        mDateText.setText(date);

        initSetData();
    }

    private void initSetData() {
        if(mNameArrayList != null && mNameArrayList.size() > 0) {
            for (int i = 0; i < mNameArrayList.size(); i++) {
                if (i == 0) {
                    TextView round1ThText = (TextView) findViewById(R.id.rd3_1th_name_text);
                    TextView round2ThText = (TextView) findViewById(R.id.rd3_2th_name_text);
                    round1ThText.setText(mNameArrayList.get(i));
                    if (mNameArrayList.size() > 7) {
                        round2ThText.setText(mNameArrayList.get(7));
                    }
                }else if (i == 1) {
                    TextView round7ThText = (TextView) findViewById(R.id.rd3_7th_name_text);
                    TextView round8ThText = (TextView) findViewById(R.id.rd3_8th_name_text);
                    round7ThText.setText(mNameArrayList.get(i));
                    if (mNameArrayList.size() > 6) {
                        round8ThText.setText(mNameArrayList.get(6));
                    }
                }else if (i == 2) {
                    TextView round3ThText = (TextView) findViewById(R.id.rd3_3th_name_text);
                    TextView round4ThText = (TextView) findViewById(R.id.rd3_4th_name_text);
                    round3ThText.setText(mNameArrayList.get(i));
                    if (mNameArrayList.size() > 5) {
                        round4ThText.setText(mNameArrayList.get(5));
                    }
                }else if (i == 3) {
                    TextView round5ThText = (TextView) findViewById(R.id.rd3_5th_name_text);
                    TextView round6ThText = (TextView) findViewById(R.id.rd3_6th_name_text);
                    round5ThText.setText(mNameArrayList.get(i));
                    if (mNameArrayList.size() > 4) {
                        round6ThText.setText(mNameArrayList.get(4));
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(emailBoolean){
            emailBoolean = false;
            mEmailLayout.setVisibility(View.GONE);
            mTournamentLayout.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_8tournament) {
        } else if (id == R.id.nav_16tournament) {
            Intent intent = new Intent(getApplicationContext(), Tournament16Activity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("nameArray", new ArrayList<String>());
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



    public void tournamentClick(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.content_tournament_popup, null);

        TextView nowNameText = (TextView) view.findViewWithTag("name_text");

        final EditText nameEdit = (EditText) dialogView.findViewById(R.id.name_text);
        final EditText scoreEdit = (EditText) dialogView.findViewById(R.id.score_text);

        nameEdit.setText(nowNameText.getText());

        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (view.getId()) {
                    case R.id.rd3_1th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_1th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_2th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_1th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_1th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R1ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R2ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_1th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_2th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line1_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line1_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line1_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R1ThScore > m1R2ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerText.setText(nameText.getText());
                            }else if(m1R1ThScore < m1R2ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerText.setText(opponentText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_2th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_1th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_1th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_2th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_2th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R2ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R1ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_1th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_2th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line1_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line1_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line1_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R1ThScore > m1R2ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerText.setText(opponentText.getText());
                            }else if(m1R1ThScore < m1R2ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerText.setText(nameText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_3th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_2th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_4th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_3th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_3th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R3ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R4ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_3th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_4th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line2_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line2_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line2_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R3ThScore > m1R4ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());
                            }else if(m1R3ThScore < m1R4ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_4th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_2th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_3th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_4th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_4th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R4ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R3ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_3th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_4th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line2_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line2_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line2_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R3ThScore > m1R4ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());
                            }else if(m1R3ThScore < m1R4ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_5th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_3th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_6th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_5th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_5th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R5ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R6ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_5th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_6th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line3_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line3_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line3_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R5ThScore > m1R6ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());
                            }else if(m1R5ThScore < m1R6ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_6th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_3th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_5th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_6th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_6th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R6ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R5ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_5th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_6th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line3_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line3_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line3_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R5ThScore > m1R6ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());
                            }else if(m1R5ThScore < m1R6ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_7th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_4th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_8th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_7th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_7th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R7ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R8ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_7th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_8th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line4_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line4_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line4_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R7ThScore > m1R8ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());
                            }else if(m1R7ThScore < m1R8ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_8th_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd2_4th_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd3_7th_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_8th_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_8th_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m1R8ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m1R7ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_7th_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_8th_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd1_line4_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd1_line4_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd1_line4_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m1R7ThScore > m1R8ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());
                            }else if(m1R7ThScore < m1R8ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd2_1th_score_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd3_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd2_2th_left_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd2_1th_left_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd2_1th_left_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m2R1ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m2R2ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd2_1th_score_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd2_2th_score_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd2_line1_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd2_line1_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd2_line1_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m2R1ThScore > m2R2ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());

                                mLeft34RankView = (View) findViewById(R.id.rd2_2th_score_layout);
                            }else if(m2R1ThScore < m2R2ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());
                                mLeft34RankView = (View) findViewById(R.id.rd2_1th_score_layout);
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd2_2th_score_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd3_left_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd2_1th_left_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd2_2th_left_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd2_2th_left_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m2R2ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m2R1ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd2_1th_score_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd2_2th_score_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd2_line1_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd2_line1_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd2_line1_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m2R1ThScore > m2R2ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());

                                mLeft34RankView = (View) findViewById(R.id.rd2_2th_score_layout);
                            }else if(m2R1ThScore < m2R2ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());

                                mLeft34RankView = (View) findViewById(R.id.rd2_1th_score_layout);
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd2_3th_score_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd3_right_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd2_4th_left_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd2_3th_left_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd2_3th_left_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m2R3ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m2R4ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd2_3th_score_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd2_4th_score_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd2_line2_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd2_line2_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd2_line2_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m2R3ThScore > m2R4ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());

                                mRight34RankView = (View) findViewById(R.id.rd2_4th_score_layout);
                            }else if(m2R3ThScore < m2R4ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());

                                mRight34RankView = (View) findViewById(R.id.rd2_3th_score_layout);
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd2_4th_score_layout: {
                        TextView winnerText = (TextView)findViewById(R.id.rd3_right_name_text);
                        TextView opponentText = (TextView)findViewById(R.id.rd2_3th_left_name_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd2_4th_left_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd2_4th_left_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m2R4ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m2R3ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd2_3th_score_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd2_4th_score_layout);

                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd2_line2_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd2_line2_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd2_line2_righ_win_win);

                            defualtLineImg.setVisibility(View.INVISIBLE);
                            leftWinLineImg.setVisibility(View.INVISIBLE);
                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m2R3ThScore > m2R4ThScore) {
                                leftWinLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(opponentText.getText());

                                mRight34RankView = (View) findViewById(R.id.rd2_4th_score_layout);
                            }else if(m2R3ThScore < m2R4ThScore){
                                rightWinLineImg.setVisibility(View.VISIBLE);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                winnerText.setText(nameText.getText());

                                mRight34RankView = (View) findViewById(R.id.rd2_3th_score_layout);
                            }else {
                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_left_layout: {
//                        RelativeLayout winnerlayout = (RelativeLayout)findViewById(R.id.winner_layout);
                        TextView winnerNameText = (TextView)findViewById(R.id.winner_name_text);
                        TextView winnerScoreText = (TextView)findViewById(R.id.winner_score_text);
                        TextView opponentNameText = (TextView)findViewById(R.id.rd3_right_name_text);
                        TextView opponentScoreText = (TextView)findViewById(R.id.rd3_right_score_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_left_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_left_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m3R1ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m3R2ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_left_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_right_layout);

//                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd3_line_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd3_line_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd3_line_right_win);

//                            defualtLineImg.setVisibility(View.INVISIBLE);
//                            leftWinLineImg.setVisibility(View.INVISIBLE);
//                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m3R1ThScore > m3R2ThScore) {
//                                leftWinLineImg.setVisibility(View.VISIBLE);
                                rightWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                leftWinLineImg.setImageResource(R.mipmap.t3r_line_8_win);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
//                                winnerlayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerNameText.setText(nameText.getText());
                                winnerScoreText.setText(scoreText.getText());
                                finishBoolean = true;
                            }else if(m3R1ThScore < m3R2ThScore){
//                                rightWinLineImg.setVisibility(View.VISIBLE);
                                leftWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                rightWinLineImg.setImageResource(R.mipmap.t3r_line_8_win);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
//                                winnerlayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerNameText.setText(opponentNameText.getText());
                                winnerScoreText.setText(opponentScoreText.getText());
                                finishBoolean = true;
                            }else {
//                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                rightWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    case R.id.rd3_right_layout: {
//                        RelativeLayout winnerlayout = (RelativeLayout)findViewById(R.id.winner_layout);
                        TextView winnerNameText = (TextView)findViewById(R.id.winner_name_text);
                        TextView winnerScoreText = (TextView)findViewById(R.id.winner_score_text);
                        TextView opponentNameText = (TextView)findViewById(R.id.rd3_left_name_text);
                        TextView opponentScoreText = (TextView)findViewById(R.id.rd3_left_score_text);

                        TextView nameText = (TextView)view.findViewById(R.id.rd3_right_name_text);
                        TextView scoreText = (TextView)view.findViewById(R.id.rd3_right_score_text);
                        nameText.setText(nameEdit.getText());
                        scoreText.setText(scoreEdit.getText());
                        m3R2ThScore = Integer.parseInt(scoreEdit.getText().toString().equals("")?"0":scoreEdit.getText().toString());

                        if(!(m3R1ThScore < 0)) {
                            RelativeLayout leftScoreLayout = (RelativeLayout) findViewById(R.id.rd3_left_layout);
                            RelativeLayout rightScoreLayout = (RelativeLayout) findViewById(R.id.rd3_right_layout);

//                            ImageView defualtLineImg = (ImageView)findViewById(R.id.rd3_line_default_win);
                            ImageView leftWinLineImg = (ImageView)findViewById(R.id.rd3_line_left_win);
                            ImageView rightWinLineImg = (ImageView)findViewById(R.id.rd3_line_right_win);

//                            defualtLineImg.setVisibility(View.INVISIBLE);
//                            leftWinLineImg.setVisibility(View.INVISIBLE);
//                            rightWinLineImg.setVisibility(View.INVISIBLE);

                            if(m3R1ThScore > m3R2ThScore) {
//                                leftWinLineImg.setVisibility(View.VISIBLE);
                                rightWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                leftWinLineImg.setImageResource(R.mipmap.t3r_line_8_win);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
//                                winnerlayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerNameText.setText(opponentNameText.getText());
                                winnerScoreText.setText(opponentScoreText.getText());
                                finishBoolean = true;
                            }else if(m3R1ThScore < m3R2ThScore){
//                                rightWinLineImg.setVisibility(View.VISIBLE);
                                leftWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                rightWinLineImg.setImageResource(R.mipmap.t3r_line_8_win);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
//                                winnerlayout.setBackgroundResource(R.drawable.regular_tournament_8th_winner);
                                winnerNameText.setText(nameText.getText());
                                winnerScoreText.setText(scoreText.getText());
                                finishBoolean = true;
                            }else {
//                                defualtLineImg.setVisibility(View.VISIBLE);
                                leftWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                rightWinLineImg.setImageResource(R.mipmap.t8_3r_line);
                                leftScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                                rightScoreLayout.setBackgroundResource(R.drawable.regular_tournament_8th_default);
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }

                if(finishBoolean) {
                    mPrintButton.setVisibility(View.VISIBLE);
                }
            }
        }).show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public static void setVisibleEmailView() {
        mEmailLayout.setVisibility(View.GONE);
        mTournamentLayout.setVisibility(View.VISIBLE);
        emailBoolean = false;
    }
}

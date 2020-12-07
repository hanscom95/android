package artech.com.arcam.record;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import artech.com.arcam.R;
import artech.com.arcam.finetuning.FineTuningActivity;
import artech.com.arcam.info.EquipmentActivity;
import artech.com.arcam.info.InfoActivity;
import artech.com.arcam.info.UserActivity;
import artech.com.arcam.login.IntroActivity;
import artech.com.arcam.scope.ScopeActivity;
import artech.com.arcam.score.ScoreActivity;
import artech.com.arcam.utility.DBManager;

public class RecordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        Context mContext;

        DBManager dbManager;

        RelativeLayout mTabLayout;
        RelativeLayout mTab1TitleLayout, mTab2TitleLayout, mTab3TitleLayout, mTab1Layout, mTab2Layout, mTab3Layout;
        RelativeLayout mTab1ListLayout, mTab1ButtonLayout, mTab1ButtonUpdateLayout;
        ImageView mSettingButton, mTab1WeatherImg, mTab1WindImg, mTab1DistanceImg;
        RadioGroup mTab2RadioGroup, mTab2WeatherRadioGroup, mTab2WindRadioGroup, mTab2DistanceRadioGroup;
        EditText mTab1CommentText;
        Button mTab1SaveButton, mTab1ListButton, mTab1DeleteButton, mTab1ModifyButton;
        TextView mTab1DateText, mTab1ScoreText, mTab1BraceText, mTab1TillerText, mTab1NockingText;
        TextView mTab2ScoreText, mTab2BraceText, mTab2TillerText, mTab2NockingText, mTab2DistanceText, mTab2ChartNoSearchText;
        TextView mTab3ScoreX10Text, mTab3Score10Text, mTab3Score9Text, mTab3Score8Text, mTab3Score7Text, mTab3Score6Text
                , mTab3Score5Text, mTab3Score4Text, mTab3Score3Text, mTab3Score2Text, mTab3Score1Text, mTab3Score0Text, mTab3ChartNoSearchText;
        ImageView mTab2WindImg;

        Spinner mTab1YearSpinner, mTab1MonthSpinner, mTab2YearSpinner, mTab2MonthSpinner, mTab3YearSpinner, mTab3MonthSpinner;

        ListView mRecordList;
        RecordAdapter mRecordAdapter;

        WebView mTab2ChartWebview, mTab3ChartWebview;


        String _id;
        static final String[] Months = new String[] {"January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_record);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
            }

            mContext = this;
            dbManager = new DBManager(mContext, "arcam.db", null, 1);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_menu);
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    TextView navEmailText = (TextView) drawer.findViewById(R.id.email_text);
                    String email = dbManager.selectEmail();
                    if(!"".equals(email)) {
                        navEmailText.setText(email);
                    }
                }
            };
            toggle.setDrawerIndicatorEnabled(false);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(3).setChecked(true);
            navigationView.setNavigationItemSelectedListener(this);


//            NavigationView navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
//            navigationRightView.setNavigationItemSelectedListener(this);

            mSettingButton = (ImageView) findViewById(R.id.setting_button);
            mTab1WeatherImg = (ImageView) findViewById(R.id.tab1_weather_img);
            mTab1WindImg = (ImageView) findViewById(R.id.tab1_wind_img);
            mTab1DistanceImg = (ImageView) findViewById(R.id.tab1_distance_img);
            mTab2WindImg = (ImageView) findViewById(R.id.tab2_second_wind_img);

            mTabLayout = (RelativeLayout) findViewById(R.id.tab_layout);
            mTab1TitleLayout = (RelativeLayout) findViewById(R.id.tab_one_layout);
            mTab2TitleLayout = (RelativeLayout) findViewById(R.id.tab_two_layout);
            mTab3TitleLayout = (RelativeLayout) findViewById(R.id.tab_three_layout);

            mTab1Layout = (RelativeLayout) findViewById(R.id.tab1_layout);
            mTab2Layout = (RelativeLayout) findViewById(R.id.tab2_layout);
            mTab3Layout = (RelativeLayout) findViewById(R.id.tab3_layout);

            mTab1ListLayout = (RelativeLayout) findViewById(R.id.tab_one_record_layout);
            mTab1ButtonLayout = (RelativeLayout) findViewById(R.id.tab1_button_layout);
            mTab1ButtonUpdateLayout = (RelativeLayout) findViewById(R.id.tab1_button_update_layout);

            mTab2RadioGroup = (RadioGroup) findViewById(R.id.record_tab2_radio_group);
            mTab2WeatherRadioGroup = (RadioGroup) findViewById(R.id.tab2_weather_radio_group);
            mTab2WindRadioGroup = (RadioGroup) findViewById(R.id.tab2_wind_radio_group);
            mTab2DistanceRadioGroup = (RadioGroup) findViewById(R.id.tab2_distance_radio_group);

            mTab1CommentText = (EditText) findViewById(R.id.tab_one_edit_text);

            mTab1SaveButton = (Button) findViewById(R.id.tab1_save_button);
            mTab1ListButton = (Button) findViewById(R.id.tab1_list_button);
            mTab1DeleteButton = (Button) findViewById(R.id.tab1_delete_button);
            mTab1ModifyButton = (Button) findViewById(R.id.tab1_modify_button);

            mTab1YearSpinner = (Spinner) findViewById(R.id.tab1_year_spinner);
            mTab1MonthSpinner = (Spinner) findViewById(R.id.tab1_month_spinner);

            mTab2YearSpinner = (Spinner) findViewById(R.id.tab2_year_spinner);
            mTab2MonthSpinner = (Spinner) findViewById(R.id.tab2_month_spinner);

            mTab3YearSpinner = (Spinner) findViewById(R.id.tab3_year_spinner);
            mTab3MonthSpinner = (Spinner) findViewById(R.id.tab3_month_spinner);

            mTab1DateText = (TextView) findViewById(R.id.tab1_date_text);
            mTab1ScoreText = (TextView) findViewById(R.id.tab1_score_text);
            mTab1BraceText = (TextView) findViewById(R.id.tab1_brace_high_value_text);
            mTab1TillerText = (TextView) findViewById(R.id.tab1_tiller_high_value_text);
            mTab1NockingText = (TextView) findViewById(R.id.tab1_nocking_point_value_text);
            mTab2ScoreText = (TextView) findViewById(R.id.tab2_second_score_value_text);
            mTab2BraceText = (TextView) findViewById(R.id.tab2_brace_high_value_text);
            mTab2TillerText = (TextView) findViewById(R.id.tab2_tiller_high_value_text);
            mTab2NockingText = (TextView) findViewById(R.id.tab2_nocking_point_value_text);
            mTab2DistanceText = (TextView) findViewById(R.id.tab2_second_distance_value_text);
            mTab2ChartNoSearchText = (TextView) findViewById(R.id.tab2_chart_no_search_text);

            mTab3ScoreX10Text = (TextView) findViewById(R.id.tab3_x10_value_text);
            mTab3Score10Text = (TextView) findViewById(R.id.tab3_10_value_text);
            mTab3Score9Text = (TextView) findViewById(R.id.tab3_9_value_text);
            mTab3Score8Text = (TextView) findViewById(R.id.tab3_8_value_text);
            mTab3Score7Text = (TextView) findViewById(R.id.tab3_7_value_text);
            mTab3Score6Text = (TextView) findViewById(R.id.tab3_6_value_text);
            mTab3Score5Text = (TextView) findViewById(R.id.tab3_5_value_text);
            mTab3Score4Text = (TextView) findViewById(R.id.tab3_4_value_text);
            mTab3Score3Text = (TextView) findViewById(R.id.tab3_3_value_text);
            mTab3Score2Text = (TextView) findViewById(R.id.tab3_2_value_text);
            mTab3Score1Text = (TextView) findViewById(R.id.tab3_1_value_text);
            mTab3Score0Text = (TextView) findViewById(R.id.tab3_0_value_text);
            mTab3ChartNoSearchText = (TextView) findViewById(R.id.tab3_chart_no_search_text);


            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH)+1;

            ArrayList<Integer> years = new ArrayList<Integer>();
            for( int i = 0; i < 10 ; i ++) {
                years.add(year--);
            }
            ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(mContext, R.layout.record_spinner_selected, years);
            yearAdapter.setDropDownViewResource(R.layout.signup_spinner);
            mTab1YearSpinner.setAdapter(yearAdapter);
            mTab2YearSpinner.setAdapter(yearAdapter);
            mTab3YearSpinner.setAdapter(yearAdapter);

            ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(mContext, R.layout.record_spinner_selected, Months);
            monthAdapter.setDropDownViewResource(R.layout.signup_spinner);
            mTab1MonthSpinner.setAdapter(monthAdapter);
            mTab2MonthSpinner.setAdapter(monthAdapter);
            mTab3MonthSpinner.setAdapter(monthAdapter);
            mTab1MonthSpinner.setSelection(month-1);
            mTab2MonthSpinner.setSelection(month-1);
            mTab3MonthSpinner.setSelection(month-1);

            mRecordList = (ListView) findViewById(R.id.tab1_record_list_view);
            mRecordAdapter = new RecordAdapter(mContext);
            mRecordList.setAdapter(mRecordAdapter);

            mTab2ChartWebview = (WebView) findViewById(R.id.tab2_chart_webview);
            mTab3ChartWebview = (WebView) findViewById(R.id.tab3_chart_webview);

            mTab2RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.weather_radio:
                            mTab2WeatherRadioGroup.setVisibility(View.VISIBLE);
                            mTab2WindRadioGroup.setVisibility(View.GONE);
                            mTab2DistanceRadioGroup.setVisibility(View.GONE);
                            break;
                        case R.id.wind_radio:
                            mTab2WeatherRadioGroup.setVisibility(View.GONE);
                            mTab2WindRadioGroup.setVisibility(View.VISIBLE);
                            mTab2DistanceRadioGroup.setVisibility(View.GONE);
                            break;
                        case R.id.distance_radio:
                            mTab2WeatherRadioGroup.setVisibility(View.GONE);
                            mTab2WindRadioGroup.setVisibility(View.GONE);
                            mTab2DistanceRadioGroup.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                }
            });

            mTab2WeatherRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int weather = -1;

                    switch (checkedId) {
                        case R.id.sunny_radio:
                            weather = 0;
                            break;
                        case R.id.cloud_radio:
                            weather = 1;
                            break;
                        case R.id.fog_radio:
                            weather = 2;
                            break;
                        case R.id.typhoon_radio:
                            weather = 3;
                            break;
                        case R.id.rain_radio:
                            weather = 4;
                            break;
                        case R.id.snow_radio:
                            weather = 5;
                            break;
                        default:
                            break;
                    }

                    int month = mTab2MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab2YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);
                    setTab2WeatherSearch(date, weather);
                }
            });

            mTab2WindRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int wind = -1;

                    switch (checkedId) {
                        case R.id.wind_zero_radio:
                            wind = 0;
                            break;
                        case R.id.wind_one_radio:
                            wind = 1;
                            break;
                        case R.id.wind_two_radio:
                            wind = 2;
                            break;
                        case R.id.wind_three_radio:
                            wind = 3;
                            break;
                        case R.id.wind_four_radio:
                            wind = 4;
                            break;
                        default:
                            break;
                    }

                    int month = mTab2MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab2YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);
                    setTab2WindSearch(date, wind);
                }
            });

            mTab2DistanceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int distance = -1;

                    switch (checkedId) {
                        case R.id.distance_18m_radio:
                            distance = 0;
                            break;
                        case R.id.distance_30m_radio:
                            distance = 1;
                            break;
                        case R.id.distance_50m_radio:
                            distance = 2;
                            break;
                        case R.id.distance_70m_radio:
                            distance = 3;
                            break;
                        default:
                            break;
                    }

                    int month = mTab2MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab2YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);
                    setTab2DistanceSearch(date, distance);
                }
            });

            mSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (drawer.isDrawerVisible(GravityCompat.END)) {
//                        drawer.closeDrawer(GravityCompat.END);
//                    } else {
//                        drawer.openDrawer(GravityCompat.END);
//                    }
                }
            });

            mTab1TitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabLayout.setBackgroundResource(R.mipmap.tab1);
                    mTab1TitleLayout.setBackgroundResource(0);
                    mTab2TitleLayout.setBackgroundResource(R.mipmap.tab_btn_non);
                    mTab3TitleLayout.setBackgroundResource(R.mipmap.tab_btn_non);

                    mTab1Layout.setVisibility(View.VISIBLE);
                    mTab2Layout.setVisibility(View.GONE);
                    mTab3Layout.setVisibility(View.GONE);
                }
            });

            mTab2TitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabLayout.setBackgroundResource(R.mipmap.tab2);
                    mTab1TitleLayout.setBackgroundResource(R.mipmap.tab_btn_non);
                    mTab2TitleLayout.setBackgroundResource(0);
                    mTab3TitleLayout.setBackgroundResource(R.mipmap.tab_btn_non);

                    mTab1Layout.setVisibility(View.GONE);
                    mTab2Layout.setVisibility(View.VISIBLE);
                    mTab3Layout.setVisibility(View.GONE);

                    tab2ChartView();
                }
            });

            mTab3TitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabLayout.setBackgroundResource(R.mipmap.tab3);
                    mTab1TitleLayout.setBackgroundResource(R.mipmap.tab_btn_non);
                    mTab2TitleLayout.setBackgroundResource(R.mipmap.tab_btn_non);
                    mTab3TitleLayout.setBackgroundResource(0);

                    mTab1Layout.setVisibility(View.GONE);
                    mTab2Layout.setVisibility(View.GONE);
                    mTab3Layout.setVisibility(View.VISIBLE);

                    tab3ChartView();
                }
            });

            mTab1SaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbManager.updateScore(_id, mTab1CommentText.getText().toString());

                    mTab1CommentText.setVisibility(View.GONE);
                    mTab1ButtonLayout.setVisibility(View.GONE);
                    mTab1ButtonUpdateLayout.setVisibility(View.VISIBLE);
                    mTab1ListLayout.setVisibility(View.VISIBLE);


                    int month = mTab1MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab1YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                    setTab1ListSearch(date);
                }
            });

            mTab1ListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTab1CommentText.setVisibility(View.GONE);
                    mTab1ButtonLayout.setVisibility(View.GONE);
                    mTab1ButtonUpdateLayout.setVisibility(View.VISIBLE);
                    mTab1ListLayout.setVisibility(View.VISIBLE);
                }
            });

            mTab1DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String col = mRecordAdapter.getColItem();

                    if(!"".equals(col) && null != col) {
                        dbManager.removeScore(col);


                        int month = mTab1MonthSpinner.getSelectedItemPosition()+1;
                        String date = mTab1YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                        setTab1ListSearch(date);
                    }
                }
            });

            mTab1ModifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RecordActivity" , "col : " + mRecordAdapter.getColItem());
                    if(!"".equals(mRecordAdapter.getColItem())) {

                        final String col = mRecordAdapter.getColItem();
                        final int[] braceHigh = {-1};
                        final int[] tillerHigh = {-1};
                        final int[] nockingPoint = {-1};
                        final int[] weather = {-1};
                        final int[] wind = {-1};
                        final int[] distance = {-1};
                        String comment = "";

                        final boolean[] weatherRadioBoolean = {false};
                        final boolean[] windRadioBoolean = {false};
                        final boolean[] distanceRadioBoolean = {false};


                        Cursor cursor = dbManager.selectIdDateScore(col);
                        while (cursor.moveToNext()) {
                            braceHigh[0] = cursor.getInt(21);
                            tillerHigh[0] = cursor.getInt(22);
                            nockingPoint[0] = cursor.getInt(23);
                            weather[0] = cursor.getInt(18);
                            wind[0] = cursor.getInt(19);
                            distance[0] = cursor.getInt(20);
                            comment = cursor.getString(24);
                        }

                        AlertDialog.Builder subAlertDialogBuilder = new AlertDialog.Builder(mContext);
                        LayoutInflater inflater = getLayoutInflater();
                        final View subDialogView = inflater.inflate(R.layout.content_record_popup, null);
                        subAlertDialogBuilder.setView(subDialogView);
                        final AlertDialog subAlertDialog = subAlertDialogBuilder.show();
                        subAlertDialog.setCanceledOnTouchOutside(false);
                        subAlertDialog.getWindow().setLayout(801, 467);
                        subAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //finish();
                            }
                        });

                        RelativeLayout weatherLayout = (RelativeLayout) subAlertDialog.findViewById(R.id.weather_layout);
                        RelativeLayout windLayout = (RelativeLayout) subAlertDialog.findViewById(R.id.wind_layout);
                        RelativeLayout distanceLayout = (RelativeLayout) subAlertDialog.findViewById(R.id.distance_layout);

                        final EditText braceHighText = (EditText) subAlertDialog.findViewById(R.id.brace_high_edittext);
                        final EditText tillerHighText = (EditText) subAlertDialog.findViewById(R.id.tiller_high_edittext);
                        final EditText nockingPointhText = (EditText) subAlertDialog.findViewById(R.id.nocking_point_edittext);
                        final EditText commentText = (EditText) subAlertDialog.findViewById(R.id.comment_edittext);

                        braceHighText.setText(""+ braceHigh[0]);
                        tillerHighText.setText(""+ tillerHigh[0]);
                        nockingPointhText.setText(""+ nockingPoint[0]);
                        commentText.setText(""+ comment);

                        final ImageView weatherImg = (ImageView) subAlertDialog.findViewById(R.id.weather_img);
                        final ImageView windImg = (ImageView) subAlertDialog.findViewById(R.id.wind_img);
                        final ImageView distanceImg = (ImageView) subAlertDialog.findViewById(R.id.distance_img);

                        final RelativeLayout weatherRadioLayout = (RelativeLayout) subAlertDialog.findViewById(R.id.weather_radio_layout);
                        final RelativeLayout windRadioLayout = (RelativeLayout) subAlertDialog.findViewById(R.id.wind_radio_layout);
                        final RelativeLayout distanceRadioLayout = (RelativeLayout) subAlertDialog.findViewById(R.id.distance_radio_layout);

                        final RadioGroup weatherRadioGroup = (RadioGroup) subAlertDialog.findViewById(R.id.weather_radio_group);
                        final RadioGroup windRadioGroup = (RadioGroup) subAlertDialog.findViewById(R.id.wind_radio_group);
                        final RadioGroup distanceRadioGroup = (RadioGroup) subAlertDialog.findViewById(R.id.distance_radio_group);

                        Button cancelButton = (Button) subAlertDialog.findViewById(R.id.cancel_button);
                        Button okButton = (Button) subAlertDialog.findViewById(R.id.ok_button);

                        if(weather[0] == 0) {
                            weatherImg.setImageResource(R.mipmap.ic_sunny_press);
                        }else if(weather[0] == 1) {
                            weatherImg.setImageResource(R.mipmap.ic_cloudy_press);
                        }else if(weather[0] == 2) {
                            weatherImg.setImageResource(R.mipmap.ic_fog_press);
                        }else if(weather[0] == 3) {
                            weatherImg.setImageResource(R.mipmap.ic_typhoon_press);
                        }else if(weather[0] == 4) {
                            weatherImg.setImageResource(R.mipmap.ic_rain_press);
                        }else if(weather[0] == 5) {
                            weatherImg.setImageResource(R.mipmap.ic_snow_press);
                        }

                        if(wind[0] == 0) {
                            windImg.setImageResource(R.mipmap.ic_wind_zero_press);
                        }else if(wind[0] == 1) {
                            windImg.setImageResource(R.mipmap.ic_wind_one_press);
                        }else if(wind[0] == 2) {
                            windImg.setImageResource(R.mipmap.ic_wind_two_press);
                        }else if(wind[0] == 3) {
                            windImg.setImageResource(R.mipmap.ic_wind_three_press);
                        }else if(wind[0] == 4) {
                            windImg.setImageResource(R.mipmap.ic_wind_four_press);
                        }

                        if(distance[0] == 0) {
                            distanceImg.setImageResource(R.mipmap.img_18m_press);
                        }else if(distance[0] == 1) {
                            distanceImg.setImageResource(R.mipmap.img_30m_press);
                        }else if(distance[0] == 2) {
                            distanceImg.setImageResource(R.mipmap.img_50m_press);
                        }else if(distance[0] == 3) {
                            distanceImg.setImageResource(R.mipmap.img_70m_press);
                        }

                        weatherLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.d("RecordActivity", "weatherLayout on click : " + weatherRadioBoolean[0]);

                                if (weatherRadioBoolean[0]) {
                                    Log.d("RecordActivity", "weatherRadioLayout true");
                                    weatherRadioBoolean[0] = false;
                                    weatherRadioLayout.setVisibility(View.GONE);
                                } else {
                                    Log.d("RecordActivity", "weatherRadioLayout false");


                                    if(weather[0] == 0) {
                                        weatherRadioGroup.check(R.id.sunny_radio);
                                    }else if(weather[0] == 1) {
                                        weatherRadioGroup.check(R.id.cloud_radio);
                                    }else if(weather[0] == 2) {
                                        weatherRadioGroup.check(R.id.fog_radio);
                                    }else if(weather[0] == 3) {
                                        weatherRadioGroup.check(R.id.typhoon_radio);
                                    }else if(weather[0] == 4) {
                                        weatherRadioGroup.check(R.id.rain_radio);
                                    }else if(weather[0] == 5) {
                                        weatherRadioGroup.check(R.id.snow_radio);
                                    }

                                    weatherRadioBoolean[0] = true;
                                    weatherRadioLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        windLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("RecordActivity", "windLayout on click : " + windRadioBoolean[0]);


                                if (windRadioBoolean[0]) {
                                    Log.d("RecordActivity", "windLayout true");
                                    windRadioBoolean[0] = false;
                                    windRadioLayout.setVisibility(View.GONE);
                                } else {
                                    Log.d("RecordActivity", "windLayout false");

                                    if(wind[0] == 0) {
                                        windRadioGroup.check(R.id.wind_zero_radio);
                                    }else if(wind[0] == 1) {
                                        windRadioGroup.check(R.id.wind_one_radio);
                                    }else if(wind[0] == 2) {
                                        windRadioGroup.check(R.id.wind_two_radio);
                                    }else if(wind[0] == 3) {
                                        windRadioGroup.check(R.id.wind_three_radio);
                                    }else if(wind[0] == 4) {
                                        windRadioGroup.check(R.id.wind_four_radio);
                                    }

                                    windRadioBoolean[0] = true;
                                    windRadioLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        distanceLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("RecordActivity", "distanceLayout on click : " +distanceRadioBoolean[0]);

                                if (distanceRadioBoolean[0]) {
                                    Log.d("RecordActivity", "distanceLayout true");
                                    distanceRadioBoolean[0] = false;
                                    distanceRadioLayout.setVisibility(View.GONE);
                                } else {
                                    Log.d("RecordActivity", "distanceLayout false");

                                    if(distance[0] == 0) {
                                        distanceRadioGroup.check(R.id.distance_18m_radio);
                                    }else if(distance[0] == 1) {
                                        distanceRadioGroup.check(R.id.distance_30m_radio);
                                    }else if(distance[0] == 2) {
                                        distanceRadioGroup.check(R.id.distance_50m_radio);
                                    }else if(distance[0] == 3) {
                                        distanceRadioGroup.check(R.id.distance_70m_radio);
                                    }

                                    distanceRadioBoolean[0] = true;
                                    distanceRadioLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        weatherRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.sunny_radio:
                                        weatherImg.setImageResource(R.mipmap.ic_sunny_press);
                                        weather[0] = 0;
                                        break;

                                    case R.id.cloud_radio:
                                        weatherImg.setImageResource(R.mipmap.ic_cloudy_press);
                                        weather[0] = 1;
                                        break;

                                    case R.id.fog_radio:
                                        weatherImg.setImageResource(R.mipmap.ic_fog_press);
                                        weather[0] = 2;
                                        break;

                                    case R.id.typhoon_radio:
                                        weatherImg.setImageResource(R.mipmap.ic_typhoon_press);
                                        weather[0] = 3;
                                        break;

                                    case R.id.rain_radio:
                                        weatherImg.setImageResource(R.mipmap.ic_rain_press);
                                        weather[0] = 4;
                                        break;

                                    case R.id.snow_radio:
                                        weatherImg.setImageResource(R.mipmap.ic_snow_press);
                                        weather[0] = 5;
                                        break;
                                }

                                weatherRadioBoolean[0] = false;
                                weatherRadioLayout.setVisibility(View.GONE);
                            }
                        });

                        windRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.wind_zero_radio:
                                        windImg.setImageResource(R.mipmap.ic_wind_zero_press);
                                        wind[0] = 0;
                                        break;

                                    case R.id.wind_one_radio:
                                        windImg.setImageResource(R.mipmap.ic_wind_one_press);
                                        wind[0] = 1;
                                        break;

                                    case R.id.wind_two_radio:
                                        windImg.setImageResource(R.mipmap.ic_wind_two_press);
                                        wind[0] = 2;
                                        break;

                                    case R.id.wind_three_radio:
                                        windImg.setImageResource(R.mipmap.ic_wind_three_press);
                                        wind[0] = 3;
                                        break;

                                    case R.id.wind_four_radio:
                                        windImg.setImageResource(R.mipmap.ic_wind_four_press);
                                        wind[0] = 4;
                                        break;
                                }

                                windRadioBoolean[0] = false;
                                windRadioLayout.setVisibility(View.GONE);
                            }
                        });

                        distanceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.distance_18m_radio:
                                        distanceImg.setImageResource(R.mipmap.img_18m_press);
                                        distance[0] = 0;
                                        break;

                                    case R.id.distance_30m_radio:
                                        distanceImg.setImageResource(R.mipmap.img_30m_press);
                                        distance[0] = 1;
                                        break;

                                    case R.id.distance_50m_radio:
                                        distanceImg.setImageResource(R.mipmap.img_50m_press);
                                        distance[0] = 2;
                                        break;

                                    case R.id.distance_70m_radio:
                                        distanceImg.setImageResource(R.mipmap.img_70m_press);
                                        distance[0] = 3;
                                        break;
                                }

                                distanceRadioBoolean[0] = false;
                                distanceRadioLayout.setVisibility(View.GONE);
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                subAlertDialog.dismiss();
                            }
                        });

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!weatherRadioBoolean[0] && !windRadioBoolean[0] && !distanceRadioBoolean[0]) {
                                    dbManager.updateScoreTuning(col, Integer.parseInt(braceHighText.getText().toString()), Integer.parseInt(tillerHighText.getText().toString()), Integer.parseInt(nockingPointhText.getText().toString()), weather[0], wind[0], distance[0], commentText.getText().toString());
                                    subAlertDialog.dismiss();

                                    int month = mTab1MonthSpinner.getSelectedItemPosition()+1;
                                    String date = mTab1YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);
                                    setTab1ListSearch(date);
                                }
                            }
                        });
                    }
                }
            });

            mTab1YearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int month = mTab1MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab1YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                    setTab1ListSearch(date);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mTab1MonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int month = mTab1MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab1YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                    setTab1ListSearch(date);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            mTab2YearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int month = mTab2MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab2YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                    switch (mTab2RadioGroup.getCheckedRadioButtonId()) {
                        case R.id.weather_radio:
                            int weather = -1;
                            switch (mTab2WeatherRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.sunny_radio:
                                    weather = 0;
                                    break;
                                case R.id.cloud_radio:
                                    weather = 1;
                                    break;
                                case R.id.fog_radio:
                                    weather = 2;
                                    break;
                                case R.id.typhoon_radio:
                                    weather = 3;
                                    break;
                                case R.id.rain_radio:
                                    weather = 4;
                                    break;
                                case R.id.snow_radio:
                                    weather = 5;
                                    break;
                                default:
                                    break;
                            }
                            setTab2WeatherSearch(date, weather);
                            break;
                        case R.id.wind_radio:
                            int wind = -1;
                            switch (mTab2WindRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.wind_zero_radio:
                                    wind = 0;
                                    break;
                                case R.id.wind_one_radio:
                                    wind = 1;
                                    break;
                                case R.id.wind_two_radio:
                                    wind = 2;
                                    break;
                                case R.id.wind_three_radio:
                                    wind = 3;
                                    break;
                                case R.id.wind_four_radio:
                                    wind = 4;
                                    break;
                                default:
                                    break;
                            }
                            setTab2WindSearch(date, wind);
                            break;
                        case R.id.distance_radio:
                            int distance = -1;
                            switch (mTab2DistanceRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.distance_18m_radio:
                                    distance = 0;
                                    break;
                                case R.id.distance_30m_radio:
                                    distance = 1;
                                    break;
                                case R.id.distance_50m_radio:
                                    distance = 2;
                                    break;
                                case R.id.distance_70m_radio:
                                    distance = 3;
                                    break;
                                default:
                                    break;
                            }
                            setTab2DistanceSearch(date, distance);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mTab2MonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int month = mTab2MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab2YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                    switch (mTab2RadioGroup.getCheckedRadioButtonId()) {
                        case R.id.weather_radio:
                            int weather = -1;
                            switch (mTab2WeatherRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.sunny_radio:
                                    weather = 0;
                                    break;
                                case R.id.cloud_radio:
                                    weather = 1;
                                    break;
                                case R.id.fog_radio:
                                    weather = 2;
                                    break;
                                case R.id.typhoon_radio:
                                    weather = 3;
                                    break;
                                case R.id.rain_radio:
                                    weather = 4;
                                    break;
                                case R.id.snow_radio:
                                    weather = 5;
                                    break;
                                default:
                                    break;
                            }
                            setTab2WeatherSearch(date, weather);
                            break;
                        case R.id.wind_radio:
                            int wind = -1;
                            switch (mTab2WindRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.wind_zero_radio:
                                    wind = 0;
                                    break;
                                case R.id.wind_one_radio:
                                    wind = 1;
                                    break;
                                case R.id.wind_two_radio:
                                    wind = 2;
                                    break;
                                case R.id.wind_three_radio:
                                    wind = 3;
                                    break;
                                case R.id.wind_four_radio:
                                    wind = 4;
                                    break;
                                default:
                                    break;
                            }
                            setTab2WindSearch(date, wind);
                            break;
                        case R.id.distance_radio:
                            int distance = -1;
                            switch (mTab2DistanceRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.distance_18m_radio:
                                    distance = 0;
                                    break;
                                case R.id.distance_30m_radio:
                                    distance = 1;
                                    break;
                                case R.id.distance_50m_radio:
                                    distance = 2;
                                    break;
                                case R.id.distance_70m_radio:
                                    distance = 3;
                                    break;
                                default:
                                    break;
                            }
                            setTab2DistanceSearch(date, distance);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            mTab3YearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int month = mTab3MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab3YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                    setTab3ScoreSearch(date);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mTab3MonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int month = mTab3MonthSpinner.getSelectedItemPosition()+1;
                    String date = mTab3YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                    setTab3ScoreSearch(date);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            initSetData();
    }

    private void initSetData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            _id = bundle.getString("col");
        }
        Log.d("RecordActivity", "_id : " + _id);


        String date = "";
        String today = "";
        int day = -1;
        int total = 0;

        int t1BraceHigh = 0;
        int t1TillerHigh = 0;
        int t1NockingPoint = 0;

        int t1Weather = -1;
        int t1Wind = -1;
        int t1Distance = -1;

        if(_id != null && !"".equals(_id)) {
            Log.d("RecordActivity", "1 _id :  false");
            mTab1ListLayout.setVisibility(View.GONE);
            mTab1CommentText.setVisibility(View.VISIBLE);
            mTab1ButtonUpdateLayout.setVisibility(View.GONE);
            mTab1ButtonLayout.setVisibility(View.VISIBLE);

            Cursor cursor = dbManager.selectIdDateScore(_id);

            while (cursor.moveToNext()) {
                Log.d("RecordActivity", "cursor col : " + cursor.getString(0) + " / date : " + cursor.getString(1) + " / total : " + cursor.getInt(2) + " / round : " + cursor.getInt(3));
                total = cursor.getInt(3);
                date = cursor.getString(1).substring(0,10);
                day = cursor.getInt(2);
                t1Weather = cursor.getInt(18);
                t1Wind = cursor.getInt(19);
                t1Distance = cursor.getInt(20);
                t1BraceHigh = cursor.getInt(21);
                t1TillerHigh = cursor.getInt(22);
                t1NockingPoint = cursor.getInt(23);
            }
        }else {
            Log.d("RecordActivity", "1 _id :  true");
            mTab1CommentText.setVisibility(View.GONE);
            mTab1ButtonLayout.setVisibility(View.GONE);
            mTab1ButtonUpdateLayout.setVisibility(View.VISIBLE);
            mTab1ListLayout.setVisibility(View.VISIBLE);

            int month = mTab1MonthSpinner.getSelectedItemPosition()+1;
            String col = mTab1YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);
//                Cursor cursor = dbManager.selectDateScore(col);
            Cursor maxCursor = dbManager.selectDateMaxScore(col);

                /*while (cursor.moveToNext()) {
                    tdate.add(cursor.getString(1).substring(0,10));
                    score.add(cursor.getInt(3));
                    brace.add(cursor.getInt(21));
                    tiller.add(cursor.getInt(22));
                    nocking.add(cursor.getInt(23));
                    weather.add(cursor.getInt(18));
                    wind.add(cursor.getInt(19));
                    distance.add(cursor.getInt(20));
                    comment.add(cursor.getString(24));
                    Log.d("RecordActivity", "cursor col : " + cursor.getString(0) + " / date : " + cursor.getString(1) + " / total : " + cursor.getInt(2) + " / round : " + cursor.getInt(3) + " / comment : " + cursor.getString(24) + " / ss : " + cursor.getInt(23));
                }*/

            while (maxCursor.moveToNext()) {
                if(maxCursor.getString(1) != null) {
                    Log.d("RecordActivity", "maxCursor total : " + maxCursor.getInt(0) + " / col : " + maxCursor.getString(1) + " / date : " + maxCursor.getString(2) + " / day_of _week : " + maxCursor.getInt(3));
                    total = maxCursor.getInt(0);
                    date = maxCursor.getString(2).substring(0,10);
                    day = maxCursor.getInt(3);
                    t1Weather = maxCursor.getInt(18);
                    t1Wind = maxCursor.getInt(19);
                    t1Distance = maxCursor.getInt(20);
                    t1BraceHigh = maxCursor.getInt(21);
                    t1TillerHigh = maxCursor.getInt(22);
                    t1NockingPoint = maxCursor.getInt(23);
                }else {
                }
            }
        }

//            mRecordAdapter.addList(tdate, score, brace, tiller, nocking, weather, wind, distance, comment);
//            mRecordAdapter.notifyDataSetChanged();

        if(day == 1) {
            today = "("+getString(R.string.monday)+")";
        }else if(day == 2) {
            today = "("+getString(R.string.tuesday)+")";
        }else if(day == 3) {
            today = "("+getString(R.string.wednesday)+")";
        }else if(day == 4) {
            today = "("+getString(R.string.thursday)+")";
        }else if(day == 5) {
            today = "("+getString(R.string.friday)+")";
        }else if(day == 6) {
            today = "("+getString(R.string.saturday)+")";
        }else if(day == 0) {
            today = "("+getString(R.string.sunday)+")";
        }

        mTab1DateText.setText(date + " " + today);
        mTab1ScoreText.setText(total + " points");
        mTab1BraceText.setText(t1BraceHigh + "");
        mTab1TillerText.setText(t1TillerHigh + "");
        mTab1NockingText.setText(t1NockingPoint + "");


        if(t1Weather == 0) {
            mTab1WeatherImg.setImageResource(R.mipmap.ic_sunny_white);
        }else if(t1Weather == 1) {
            mTab1WeatherImg.setImageResource(R.mipmap.ic_cloudy_white);
        }else if(t1Weather == 2) {
            mTab1WeatherImg.setImageResource(R.mipmap.ic_fog_white);
        }else if(t1Weather == 3) {
            mTab1WeatherImg.setImageResource(R.mipmap.ic_typhoon2_non_press);
        }else if(t1Weather == 4) {
            mTab1WeatherImg.setImageResource(R.mipmap.ic_rain_white);
        }else if(t1Weather == 5) {
            mTab1WeatherImg.setImageResource(R.mipmap.ic_snow_white);
        }

        if(t1Wind == 0) {
            mTab1WindImg.setImageResource(R.mipmap.ic_wind_zero_white);
        }else if(t1Wind == 1) {
            mTab1WindImg.setImageResource(R.mipmap.ic_wind_one_white);
        }else if(t1Wind == 2) {
            mTab1WindImg.setImageResource(R.mipmap.ic_wind_two_white);
        }else if(t1Wind == 3) {
            mTab1WindImg.setImageResource(R.mipmap.ic_wind_three_white);
        }else if(t1Wind == 4) {
            mTab1WindImg.setImageResource(R.mipmap.ic_wind_four_white);
        }

        if(t1Distance == 0) {
            mTab1DistanceImg.setImageResource(R.mipmap.ic_18msc_press);
        }else if(t1Distance == 1) {
            mTab1DistanceImg.setImageResource(R.mipmap.ic_30msc_press);
        }else if(t1Distance == 2) {
            mTab1DistanceImg.setImageResource(R.mipmap.ic_50msc_press);
        }else if(t1Distance == 3) {
            mTab1DistanceImg.setImageResource(R.mipmap.ic_70msc_press);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//        }else if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
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

        if (id == R.id.nav_finetuning) {
            Intent intent = new Intent(mContext, FineTuningActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_scope) {
            Intent intent = new Intent(mContext, ScopeActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_score) {
            Intent intent = new Intent(mContext, ScoreActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_user) {
            Intent intent = new Intent(mContext, UserActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_equipment) {
            Intent intent = new Intent(mContext, EquipmentActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_info) {
            Intent intent = new Intent(mContext, InfoActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.logout_message)
                    .setPositiveButton(R.string.logout_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbManager.dropDB();

                            Intent intent = new Intent(mContext, IntroActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton(R.string.logout_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//        }else if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
        }
        return true;
    }

    private void setTab1ListSearch(String date) {
        ArrayList<String> col = new ArrayList<>();
        ArrayList<String> tdate = new ArrayList<>();
        ArrayList<Integer> score = new ArrayList<>();
        ArrayList<Integer> brace = new ArrayList<>();
        ArrayList<Integer> tiller = new ArrayList<>();
        ArrayList<Integer> nocking = new ArrayList<>();
        ArrayList<Integer> weather = new ArrayList<>();
        ArrayList<Integer> wind = new ArrayList<>();
        ArrayList<Integer> distance = new ArrayList<>();
        ArrayList<String> comment = new ArrayList<>();

        Cursor cursor = dbManager.selectDateScore(date);


        while (cursor.moveToNext()) {
            col.add(cursor.getString(0));
            tdate.add(cursor.getString(1).substring(0,10));
            score.add(cursor.getInt(3));
            brace.add(cursor.getInt(21));
            tiller.add(cursor.getInt(22));
            nocking.add(cursor.getInt(23));
            weather.add(cursor.getInt(18));
            wind.add(cursor.getInt(19));
            distance.add(cursor.getInt(20));
            comment.add(cursor.getString(24));
            Log.d("RecordActivity", "cursor col : " + cursor.getString(0) + " / date : " + cursor.getString(1) + " / total : " + cursor.getInt(2) + " / round : " + cursor.getInt(3) + " / comment : " + cursor.getString(24) + " / ss : " + cursor.getInt(23));
        }


        mRecordAdapter.clear();
        mRecordAdapter.addList(col, tdate, score, brace, tiller, nocking, weather, wind, distance, comment);
        mRecordAdapter.notifyDataSetChanged();
    }


    private void setTab2WeatherSearch(String date, int value) {
        String tdate = "";
        int score = 0;
        int brace = 0;
        int tiller = 0;
        int nocking = 0;
        int weather = 0;
        int wind = 0;
        int distance = 0;
        String comment = "";
        ArrayList<Integer> arrowArrayList = new ArrayList<>();

        Cursor cursor = dbManager.selectWeatherMaxScore(date, value);


        while (cursor.moveToNext()) {
            if(cursor.getString(2) != null) {
                Log.d("RecordActivity", "setTab2WeatherSearch date :  " + date + " / value : " + value + "/ col : " + cursor.getString(0) + " / date : " + cursor.getString(1) + " / total : " + cursor.getInt(2) + " / round : " + cursor.getInt(3) + " / comment : " + cursor.getString(24) + " / ss : " + cursor.getInt(23));
                tdate = cursor.getString(2).substring(0, 10);
                score = cursor.getInt(0);
                brace = cursor.getInt(21);
                tiller = cursor.    getInt(22);
                nocking = cursor.getInt(23);
                weather = cursor.getInt(18);
                wind = cursor.getInt(19);
                distance = cursor.getInt(20);
                comment = cursor.getString(24);

                arrowArrayList.add(cursor.getInt(6));
                arrowArrayList.add(cursor.getInt(7));
                arrowArrayList.add(cursor.getInt(8));
                arrowArrayList.add(cursor.getInt(9));
                arrowArrayList.add(cursor.getInt(10));
                arrowArrayList.add(cursor.getInt(11));
                arrowArrayList.add(cursor.getInt(12));
                arrowArrayList.add(cursor.getInt(13));
                arrowArrayList.add(cursor.getInt(14));
                arrowArrayList.add(cursor.getInt(15));
                arrowArrayList.add(cursor.getInt(16));
                arrowArrayList.add(cursor.getInt(17));
            }
        }

        mTab2ScoreText.setText(score + "");
        mTab2BraceText.setText(brace + "");
        mTab2TillerText.setText(tiller + "");
        mTab2NockingText.setText(nocking + "");

        if(distance == 0) {
            mTab2DistanceText.setText("18m");
        }else if(distance == 1) {
            mTab2DistanceText.setText("30m");
        }else if(distance == 2) {
            mTab2DistanceText.setText("50m");
        }else if(distance == 3) {
            mTab2DistanceText.setText("70m");
        }

        if(wind == 0) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_zero_press);
        }else if(wind == 1) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_one_press);
        }else if(wind == 2) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_two_press);
        }else if(wind == 3) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_three_press);
        }else if(wind == 4) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_four_press);
        }

        if(arrowArrayList.size() == 0) {
            for(int i = 0; i < 12; i++) {
                arrowArrayList.add(0);
            }
        }

        tab2ChartData(arrowArrayList);
    }


    private void setTab2WindSearch(String date, int value) {
        String tdate = "";
        int score = 0;
        int brace = 0;
        int tiller = 0;
        int nocking = 0;
        int weather = 0;
        int wind = 0;
        int distance = 0;
        String comment = "";
        ArrayList<Integer> arrowArrayList = new ArrayList<>();

        Cursor cursor = dbManager.selectWindMaxScore(date, value);


        while (cursor.moveToNext()) {
            if(cursor.getString(2) != null) {
                Log.d("RecordActivity", "setTab2WeatherSearch date :  " + date + " / value : " + value + "/ col : " + cursor.getString(0) + " / date : " + cursor.getString(1) + " / total : " + cursor.getInt(2) + " / round : " + cursor.getInt(3) + " / comment : " + cursor.getString(24) + " / ss : " + cursor.getInt(23));
                tdate = cursor.getString(2).substring(0, 10);
                score = cursor.getInt(0);
                brace = cursor.getInt(21);
                tiller = cursor.getInt(22);
                nocking = cursor.getInt(23);
                weather = cursor.getInt(18);
                wind = cursor.getInt(19);
                distance = cursor.getInt(20);
                comment = cursor.getString(24);


                arrowArrayList.add(cursor.getInt(6));
                arrowArrayList.add(cursor.getInt(7));
                arrowArrayList.add(cursor.getInt(8));
                arrowArrayList.add(cursor.getInt(9));
                arrowArrayList.add(cursor.getInt(10));
                arrowArrayList.add(cursor.getInt(11));
                arrowArrayList.add(cursor.getInt(12));
                arrowArrayList.add(cursor.getInt(13));
                arrowArrayList.add(cursor.getInt(14));
                arrowArrayList.add(cursor.getInt(15));
                arrowArrayList.add(cursor.getInt(16));
                arrowArrayList.add(cursor.getInt(17));
            }
        }

        mTab2ScoreText.setText(score + "");
        mTab2BraceText.setText(brace + "");
        mTab2TillerText.setText(tiller + "");
        mTab2NockingText.setText(nocking + "");

        if(distance == 0) {
            mTab2DistanceText.setText("18m");
        }else if(distance == 1) {
            mTab2DistanceText.setText("30m");
        }else if(distance == 2) {
            mTab2DistanceText.setText("50m");
        }else if(distance == 3) {
            mTab2DistanceText.setText("70m");
        }

        if(wind == 0) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_zero_press);
        }else if(wind == 1) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_one_press);
        }else if(wind == 2) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_two_press);
        }else if(wind == 3) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_three_press);
        }else if(wind == 4) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_four_press);
        }

        if(arrowArrayList.size() == 0) {
            for(int i = 0; i < 12; i++) {
                arrowArrayList.add(0);
            }
        }

        tab2ChartData(arrowArrayList);
    }


    private void setTab2DistanceSearch(String date, int value) {
        String tdate = "";
        int score = 0;
        int brace = 0;
        int tiller = 0;
        int nocking = 0;
        int weather = 0;
        int wind = 0;
        int distance = 0;
        String comment = "";
        ArrayList<Integer> arrowArrayList = new ArrayList<>();

        Cursor cursor = dbManager.selectDistanceMaxScore(date, value);



        while (cursor.moveToNext()) {
            if(cursor.getString(2) != null) {
                Log.d("RecordActivity", "setTab2WeatherSearch date :  " + date + " / value : " + value + "/ col : " + cursor.getString(0) + " / date : " + cursor.getString(1) + " / total : " + cursor.getInt(2) + " / round : " + cursor.getInt(3) + " / comment : " + cursor.getString(24) + " / ss : " + cursor.getInt(23));
                tdate = cursor.getString(2).substring(0, 10);
                score = cursor.getInt(0);
                brace = cursor.getInt(21);
                tiller = cursor.getInt(22);
                nocking = cursor.getInt(23);
                weather = cursor.getInt(18);
                wind = cursor.getInt(19);
                distance = cursor.getInt(20);
                comment = cursor.getString(24);


                arrowArrayList.add(cursor.getInt(6));
                arrowArrayList.add(cursor.getInt(7));
                arrowArrayList.add(cursor.getInt(8));
                arrowArrayList.add(cursor.getInt(9));
                arrowArrayList.add(cursor.getInt(10));
                arrowArrayList.add(cursor.getInt(11));
                arrowArrayList.add(cursor.getInt(12));
                arrowArrayList.add(cursor.getInt(13));
                arrowArrayList.add(cursor.getInt(14));
                arrowArrayList.add(cursor.getInt(15));
                arrowArrayList.add(cursor.getInt(16));
                arrowArrayList.add(cursor.getInt(17));
            }
        }

        mTab2ScoreText.setText(score + "");
        mTab2BraceText.setText(brace + "");
        mTab2TillerText.setText(tiller + "");
        mTab2NockingText.setText(nocking + "");

        if(distance == 0) {
            mTab2DistanceText.setText("18m");
        }else if(distance == 1) {
            mTab2DistanceText.setText("30m");
        }else if(distance == 2) {
            mTab2DistanceText.setText("50m");
        }else if(distance == 3) {
            mTab2DistanceText.setText("70m");
        }

        if(wind == 0) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_zero_press);
        }else if(wind == 1) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_one_press);
        }else if(wind == 2) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_two_press);
        }else if(wind == 3) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_three_press);
        }else if(wind == 4) {
            mTab2WindImg.setImageResource(R.mipmap.ic_w_wind_four_press);
        }


        if(arrowArrayList.size() == 0) {
            for(int i = 0; i < 12; i++) {
                arrowArrayList.add(0);
            }
        }

        tab2ChartData(arrowArrayList);
    }


    private void setTab3ScoreSearch(String date) {
        int arrow = 0;
        int zero = 0;
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int seven = 0;
        int eigh = 0;
        int nine = 0;
        int ten = 0;
        int xten = 0;

        Cursor cursor = dbManager.selectDateSumScore(date);


        while (cursor.moveToNext()) {
            if(cursor.getString(0) != null) {
                arrow = cursor.getInt(2);
                zero = cursor.getInt(3);
                one = cursor.getInt(4);
                two = cursor.getInt(5);
                three = cursor.getInt(6);
                four = cursor.getInt(7);
                five = cursor.getInt(8);
                six = cursor.getInt(9);
                seven = cursor.getInt(10);
                eigh = cursor.getInt(11);
                nine = cursor.getInt(12);
                ten = cursor.getInt(13);
                xten = cursor.getInt(14);


                zero = ((zero * 100) / arrow);
                one = ((one * 100) / arrow);
                two = ((two * 100) / arrow);
                three = ((three * 100) / arrow);
                four = ((four * 100) / arrow);
                five = ((five * 100) / arrow);
                six = ((six * 100) / arrow);
                seven = ((seven * 100) / arrow);
                eigh = ((eigh * 100) / arrow);
                nine = ((nine * 100) / arrow);
                ten = ((ten * 100) / arrow);
                xten = ((xten * 100) / arrow);

                Log.d("RecordActivity", "setTab3ScoreSearch arrow : " +  arrow + " / 1 " + one + " / 2 " + two + " / 3 " + three + " / 10 " + ten + " / 10x " + xten + " / 6 " + six);
            }
        }

        ArrayList<Integer> array = new ArrayList<>();
        if(arrow > 0) {
            mTab3Score0Text.setText(zero+ "%");
            mTab3Score1Text.setText(one+ "%");
            mTab3Score2Text.setText(two+ "%");
            mTab3Score3Text.setText(three+ "%");
            mTab3Score4Text.setText(four+ "%");
            mTab3Score5Text.setText(five+ "%");
            mTab3Score6Text.setText(six+ "%");
            mTab3Score7Text.setText(seven+ "%");
            mTab3Score8Text.setText(eigh+ "%");
            mTab3Score9Text.setText(nine+ "%");
            mTab3Score10Text.setText(ten+ "%");
            mTab3ScoreX10Text.setText(xten+ "%");

            array.add(zero);
            array.add(one);
            array.add(two);
            array.add(three);
            array.add(four);
            array.add(five);
            array.add(six);
            array.add(seven);
            array.add(eigh);
            array.add(nine);
            array.add(ten);
            array.add(xten);
        }else {
            mTab3Score0Text.setText("0%");
            mTab3Score1Text.setText("0%");
            mTab3Score2Text.setText("0%");
            mTab3Score3Text.setText("0%");
            mTab3Score4Text.setText("0%");
            mTab3Score5Text.setText("0%");
            mTab3Score6Text.setText("0%");
            mTab3Score7Text.setText("0%");
            mTab3Score8Text.setText("0%");
            mTab3Score9Text.setText("0%");
            mTab3Score10Text.setText("0%");
            mTab3ScoreX10Text.setText("0%");

            for(int i = 0; i < 12; i++) {
                array.add(0);
            }
        }

        tab3ChartData(array);
    }


    private void tab2ChartView() {
        //line chart
        mTab2ChartWebview.getSettings().setJavaScriptEnabled(true);
        mTab2ChartWebview.setBackgroundColor(Color.TRANSPARENT);
        mTab2ChartWebview.setWebChromeClient(new WebChromeClient());
        mTab2ChartWebview.addJavascriptInterface(new ChartDataCallback(), "android");
        mTab2ChartWebview.loadUrl("file:///android_asset/chart/record_tab2_chart.html");

        mTab2ChartWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                int month = mTab2MonthSpinner.getSelectedItemPosition()+1;
//                String date = mTab2YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);
//
//                setTab2ScoreSearch(date);

                ArrayList<Integer> arrayList = new ArrayList<>();
                for(int i = 0; i<12; i++) {
                    arrayList.add(0);
                }
                tab2ChartData(arrayList);
            }
        });
    }


    private void tab3ChartView() {
        //line chart
        mTab3ChartWebview.getSettings().setJavaScriptEnabled(true);
        mTab3ChartWebview.setBackgroundColor(Color.TRANSPARENT);
        mTab3ChartWebview.setWebChromeClient(new WebChromeClient());
        mTab3ChartWebview.addJavascriptInterface(new ChartDataCallback(), "android");
        mTab3ChartWebview.loadUrl("file:///android_asset/chart/record_tab3_chart.html");

        mTab3ChartWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                int month = mTab3MonthSpinner.getSelectedItemPosition()+1;
                String date = mTab3YearSpinner.getSelectedItem().toString() + "-" + (month>10?month:"0"+month);

                setTab3ScoreSearch(date);
            }
        });
    }


    public void tab3ChartData(ArrayList<Integer> arrayList){
        String data = "";
        data += "[";
        for(int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i) > 0) {
                switch (i) {
                    case 0:
                        data += ("['0(miss)', " + arrayList.get(i) +"],");
                        break;
                    case 1:
                        data += ("['1', " + arrayList.get(i) +"],");
                        break;
                    case 2:
                        data += ("['2', " + arrayList.get(i) +"],");
                        break;
                    case 3:
                        data += ("['3', " + arrayList.get(i) +"],");
                        break;
                    case 4:
                        data += ("['4', " + arrayList.get(i) +"],");
                        break;
                    case 5:
                        data += ("['5', " + arrayList.get(i) +"],");
                        break;
                    case 6:
                        data += ("['6', " + arrayList.get(i) +"],");
                        break;
                    case 7:
                        data += ("['7', " + arrayList.get(i) +"],");
                        break;
                    case 8:
                        data += ("['8', " + arrayList.get(i) +"],");
                        break;
                    case 9:
                        data += ("['9', " + arrayList.get(i) +"],");
                        break;
                    case 10:
                        data += ("['10', " + arrayList.get(i) +"],");
                        break;
                    case 11:
                        data += ("['x-10', " + arrayList.get(i) +"]");
                        break;
                }
            }
        }

        if(",".equals(data.substring(data.length()-1, data.length()))) {
            data = data.substring(0, data.length()-1);
        }
        data += "]";

//        data = "[['0(miss)', "+arrayList.get(0)+"], ['1', "+arrayList.get(1)+"], ['2', "+arrayList.get(2)+"], ['3', "+arrayList.get(3)+"], ['4', "+arrayList.get(4)+"], ['5', "+arrayList.get(5)+"], ['6', "+arrayList.get(6)+"]," +
//                "['7', "+arrayList.get(7)+"], ['8', "+arrayList.get(8)+"], ['9', "+arrayList.get(9)+"], ['10', "+arrayList.get(10)+"], ['x-10', "+arrayList.get(11)+"]]";
        Log.d("chartSampleData", "data1 : " + data);


        int total = 0;
        for(int i = 0; i < arrayList.size(); i++) {
            total += arrayList.get(i);
        }
        if(total > 0) {
            mTab3ChartNoSearchText.setVisibility(View.GONE);
            mTab3ChartWebview.setVisibility(View.VISIBLE);
            mTab3ChartWebview.loadUrl("javascript:setChartData("+data+")");
        }else {
            mTab3ChartWebview.setVisibility(View.GONE);
            mTab3ChartNoSearchText.setVisibility(View.VISIBLE);
        }
    }


    public void tab2ChartData(ArrayList<Integer> arrayList){
        String data = "";
//        data2 = "[1, 0, 2, 1, 3, 2, 4, 5, 7, 2, 8, 11]";
        data = "["+arrayList.get(0)+","+arrayList.get(1)+","+arrayList.get(2)+","+arrayList.get(3)+","+arrayList.get(4)+","+arrayList.get(5)+","+arrayList.get(6)+","
                +arrayList.get(7)+","+arrayList.get(8)+","+arrayList.get(9)+","+arrayList.get(10)+","+arrayList.get(11) +"]";

        int total = 0;
        for(int i = 0; i < arrayList.size(); i++) {
            total += arrayList.get(i);
        }

        if(total > 0) {
            mTab2ChartNoSearchText.setVisibility(View.GONE);
            mTab2ChartWebview.setVisibility(View.VISIBLE);
            mTab2ChartWebview.loadUrl("javascript:setChartData(" + data + ")");
        }else {
            mTab2ChartWebview.setVisibility(View.GONE);
            mTab2ChartNoSearchText.setVisibility(View.VISIBLE);
        }
    }


    final public class ChartDataCallback {
        ChartDataCallback() {
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @JavascriptInterface
        public void callBackChart(String str) {
        }
    }

}

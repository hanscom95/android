package artech.com.manager.booking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import artech.com.manager.MainActivity;
import artech.com.manager.R;
import artech.com.manager.admin.AdminListActivity;
import artech.com.manager.group.GroupActivity;
import artech.com.manager.group.GroupListAdapter;
import artech.com.manager.guide.GuideActivity;
import artech.com.manager.tournament.Tournament16Activity;
import artech.com.manager.tournament.Tournament8Activity;
import artech.com.manager.utility.DBManager;

public class BookingListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    DBManager dbManager;

    ListView mBookingList;
    BookingListAdapter mBookingListAdapter;

    Button mInsertButton, mUpdateButton, mRemoveButton, mCloseButton;

//    int mPeople = 0, mTeam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_booking_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dbManager = new DBManager(getApplicationContext(), "fivics_manager.db", null, 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        mInsertButton = (Button) findViewById(R.id.insert_button);
        mUpdateButton = (Button) findViewById(R.id.update_button);
        mRemoveButton = (Button) findViewById(R.id.remove_button);
        mCloseButton = (Button) findViewById(R.id.close_button);

        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate (R.layout.content_booking_popup, null);
                alertDialogBuilder.setView(dialogView);
                alertDialogBuilder.setTitle("신규등록");
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().setLayout(844, 593);
                alertDialog.setCanceledOnTouchOutside(false);

                final EditText dateEdit = (EditText) dialogView.findViewById(R.id.date_text);
                final EditText teamEdit = (EditText) dialogView.findViewById(R.id.team_text);
                final EditText emailEdit = (EditText) dialogView.findViewById(R.id.email_text);
                final EditText bookingPersonnelEdit = (EditText) dialogView.findViewById(R.id.booking_personnel_text);
                final EditText bookingPhoneEdit = (EditText) dialogView.findViewById(R.id.booking_phone_text);
                final EditText numberEdit = (EditText) dialogView.findViewById(R.id.number_text);
                final EditText teamNumberEdit = (EditText) dialogView.findViewById(R.id.team_number_text);
                final EditText programEdit = (EditText) dialogView.findViewById(R.id.program_text);
                final EditText etcEdit = (EditText) dialogView.findViewById(R.id.etc_text);
                final EditText personsEdit = (EditText) dialogView.findViewById(R.id.team_person_text);
                Button updateButton = (Button) dialogView.findViewById(R.id.update_button);
                Button saveButton = (Button) dialogView.findViewById(R.id.save_button);

                updateButton.setVisibility(View.GONE);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Object> arrayList = new ArrayList<Object>();
                        arrayList.add(dateEdit.getText().toString());
                        arrayList.add(emailEdit.getText().toString());
                        arrayList.add(teamEdit.getText().toString());
                        arrayList.add(bookingPersonnelEdit.getText().toString());
                        arrayList.add(bookingPhoneEdit.getText().toString());
                        arrayList.add(numberEdit.getText().toString());
                        arrayList.add(teamNumberEdit.getText().toString());
                        arrayList.add(programEdit.getText().toString());
                        arrayList.add(etcEdit.getText().toString());
                        arrayList.add(personsEdit.getText().toString());
                        dbManager.insertUser(arrayList);
                        alertDialog.dismiss();

                        listSearch();
                    }
                });
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int row = mBookingListAdapter.getCheckedRow();
                if(!(row  < 0)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.content_booking_popup, null);
                    alertDialogBuilder.setView(dialogView);
                    alertDialogBuilder.setTitle("목록수정");
                    final AlertDialog alertDialog = alertDialogBuilder.show();
                    alertDialog.getWindow().setLayout(844, 593);
                    alertDialog.setCanceledOnTouchOutside(false);

                    final EditText dateEdit = (EditText) dialogView.findViewById(R.id.date_text);
                    final EditText teamEdit = (EditText) dialogView.findViewById(R.id.team_text);
                    final EditText emailEdit = (EditText) dialogView.findViewById(R.id.email_text);
                    final EditText bookingPersonnelEdit = (EditText) dialogView.findViewById(R.id.booking_personnel_text);
                    final EditText bookingPhoneEdit = (EditText) dialogView.findViewById(R.id.booking_phone_text);
                    final EditText numberEdit = (EditText) dialogView.findViewById(R.id.number_text);
                    final EditText teamNumberEdit = (EditText) dialogView.findViewById(R.id.team_number_text);
                    final EditText programEdit = (EditText) dialogView.findViewById(R.id.program_text);
                    final EditText etcEdit = (EditText) dialogView.findViewById(R.id.etc_text);
                    final EditText personsEdit = (EditText) dialogView.findViewById(R.id.team_person_text);
                    Button updateButton = (Button) dialogView.findViewById(R.id.update_button);
                    Button saveButton = (Button) dialogView.findViewById(R.id.save_button);


                    dateEdit.setText(mBookingListAdapter.dateList.get(row));
                    teamEdit.setText(mBookingListAdapter.teamList.get(row));
                    emailEdit.setText(mBookingListAdapter.emailList.get(row));
                    bookingPersonnelEdit.setText(mBookingListAdapter.nameList.get(row));
                    bookingPhoneEdit.setText(mBookingListAdapter.phoneList.get(row));
                    numberEdit.setText(mBookingListAdapter.numberList.get(row));
                    teamNumberEdit.setText(mBookingListAdapter.teamNumberList.get(row));
                    programEdit.setText(mBookingListAdapter.programList.get(row));
                    etcEdit.setText(mBookingListAdapter.etcList.get(row));
                    personsEdit.setText(mBookingListAdapter.personsList.get(row));

                    saveButton.setVisibility(View.GONE);

                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!(row < 0)) {
                                ArrayList<Object> arrayList = new ArrayList<Object>();
                                arrayList.add(mBookingListAdapter.idList.get(row));
                                arrayList.add(dateEdit.getText().toString());
                                arrayList.add(emailEdit.getText().toString());
                                arrayList.add(teamEdit.getText().toString());
                                arrayList.add(bookingPersonnelEdit.getText().toString());
                                arrayList.add(bookingPhoneEdit.getText().toString());
                                arrayList.add(numberEdit.getText().toString());
                                arrayList.add(teamNumberEdit.getText().toString());
                                arrayList.add(programEdit.getText().toString());
                                arrayList.add(etcEdit.getText().toString());
                                arrayList.add(personsEdit.getText().toString());
                                dbManager.updateUser(arrayList);
                                alertDialog.dismiss();

                                listSearch();
                            } else {
                                Toast.makeText(mContext, "수정할 목록이 없습니다. 수정하실 목록을 정해주세요.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int row = mBookingListAdapter.getCheckedRow();
                if(!(row  < 0)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("목록삭제");
                    alertDialogBuilder.setMessage("선택된 항목을 삭제하시겠습니까?");
                    alertDialogBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbManager.removeUser(mBookingListAdapter.idList.get(row));
                            mBookingListAdapter.setinitCheckedRow();
                            listSearch();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }else {
                    Toast.makeText(mContext, "수정할 목록이 없습니다. 수정하실 목록을 정해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        mTeam = bundle.getInt("team");
//        mPeople  = bundle.getInt("people");

//        Log.d("GroupListActivity", "team : " + mTeam + " / people : " + mPeople);
        mBookingList = (ListView) findViewById(R.id.booking_list_view);

        mBookingListAdapter = new BookingListAdapter(mContext);

        listSearch();
        mBookingList.setAdapter(mBookingListAdapter);
        mBookingList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
        } else if (id == R.id.nav_admin) {
            Intent intent = new Intent(getApplicationContext(), AdminListActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void listSearch() {
        ArrayList<String> idArrayList = new ArrayList<>();
        ArrayList<String> dateArrayList = new ArrayList<>();
        ArrayList<String> emailArrayList = new ArrayList<>();
        ArrayList<String> teamArrayList = new ArrayList<>();
        ArrayList<String> nameArrayList = new ArrayList<>();
        ArrayList<String> phoneArrayList = new ArrayList<>();
        ArrayList<String> numberArrayList = new ArrayList<>();
        ArrayList<String> teamNumberArrayList = new ArrayList<>();
        ArrayList<String> programArrayList = new ArrayList<>();
        ArrayList<String> etcArrayList = new ArrayList<>();
        ArrayList<String> personsArrayList = new ArrayList<>();

        Cursor cursor = dbManager.selectUser();
        while (cursor.moveToNext()) {
            idArrayList.add(cursor.getString(0));
            dateArrayList.add(cursor.getString(1));
            emailArrayList.add(cursor.getString(2));
            teamArrayList.add(cursor.getString(3));
            nameArrayList.add(cursor.getString(4));
            phoneArrayList.add(cursor.getString(5));
            numberArrayList.add(cursor.getString(6));
            teamNumberArrayList.add(cursor.getString(7));
            programArrayList.add(cursor.getString(8));
            etcArrayList.add(cursor.getString(9));
            personsArrayList.add(cursor.getString(10));
        }

        mBookingListAdapter.clear();
        mBookingListAdapter.addList(idArrayList, dateArrayList, emailArrayList, teamArrayList, nameArrayList, phoneArrayList, numberArrayList, teamNumberArrayList, programArrayList, etcArrayList, personsArrayList);
        mBookingListAdapter.notifyDataSetChanged();
    }
}

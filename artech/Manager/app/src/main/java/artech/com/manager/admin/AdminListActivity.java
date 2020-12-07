package artech.com.manager.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import artech.com.manager.MainActivity;
import artech.com.manager.R;
import artech.com.manager.booking.BookingListActivity;
import artech.com.manager.group.GroupActivity;
import artech.com.manager.guide.GuideActivity;
import artech.com.manager.tournament.Tournament16Activity;
import artech.com.manager.tournament.Tournament8Activity;
import artech.com.manager.utility.DBManager;
import artech.com.manager.utility.NetworkConnection;

public class AdminListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    DBManager dbManager;

    AdminTask mAdminTask;

    ListView mAdminList;
    AdminListAdapter mAdminListAdapter;

    Button mInsertButton, mUpdateButton, mRemoveButton, mCloseButton;

//    int mPeople = 0, mTeam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_admin_list);
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
                View dialogView = inflater.inflate (R.layout.content_admin_popup, null);
                alertDialogBuilder.setView(dialogView);
                alertDialogBuilder.setTitle("신규등록");
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().setLayout(844, 393);
                alertDialog.setCanceledOnTouchOutside(false);

                final EditText infoEdit = (EditText) dialogView.findViewById(R.id.info_text);
                final TextView idText = (TextView) dialogView.findViewById(R.id.id_text);
                final EditText nameText = (EditText) dialogView.findViewById(R.id.name_text);
                final EditText numberText = (EditText) dialogView.findViewById(R.id.number_text);
                Button updateButton = (Button) dialogView.findViewById(R.id.update_button);
                Button saveButton = (Button) dialogView.findViewById(R.id.save_button);

                updateButton.setVisibility(View.GONE);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Object> arrayList = new ArrayList<Object>();
                        arrayList.add(1);
                        arrayList.add(nameText.getText().toString());
                        arrayList.add(infoEdit.getText().toString());
                        arrayList.add(numberText.getText().toString());

                        mAdminTask = new AdminTask(1, arrayList);
                        mAdminTask.execute((Void) null);

                        alertDialog.dismiss();

//                        listSearch();
                    }
                });
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int row = mAdminListAdapter.getCheckedRow();
                if(!(row  < 0)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.content_admin_popup, null);
                    alertDialogBuilder.setView(dialogView);
                    alertDialogBuilder.setTitle("목록수정");
                    final AlertDialog alertDialog = alertDialogBuilder.show();
                    alertDialog.getWindow().setLayout(844, 393);
                    alertDialog.setCanceledOnTouchOutside(false);

                    final EditText infoEdit = (EditText) dialogView.findViewById(R.id.info_text);
                    final TextView idText = (TextView) dialogView.findViewById(R.id.id_text);
                    final EditText nameText = (EditText) dialogView.findViewById(R.id.name_text);
                    final EditText numberText = (EditText) dialogView.findViewById(R.id.number_text);
                    Button updateButton = (Button) dialogView.findViewById(R.id.update_button);
                    Button saveButton = (Button) dialogView.findViewById(R.id.save_button);


                    infoEdit.setText(mAdminListAdapter.adminList.get(row).info);
                    idText.setText(mAdminListAdapter.adminList.get(row)._id + "");
                    nameText.setText(mAdminListAdapter.adminList.get(row).name);
                    numberText.setText(mAdminListAdapter.adminList.get(row).number);

                    saveButton.setVisibility(View.GONE);

                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!(row < 0)) {
                                ArrayList<Object> arrayList = new ArrayList<Object>();
                                arrayList.add(mAdminListAdapter.adminList.get(row)._id);
                                arrayList.add(nameText.getText().toString());
                                arrayList.add(infoEdit.getText().toString());
                                arrayList.add(numberText.getText().toString());


                                mAdminTask = new AdminTask(2, arrayList);
                                mAdminTask.execute((Void) null);
//                                dbManager.updateUser(arrayList);
                                alertDialog.dismiss();

//                                listSearch();
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
                final int row = mAdminListAdapter.getCheckedRow();
                if(!(row  < 0)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("목록삭제");
                    alertDialogBuilder.setMessage("선택된 항목을 삭제하시겠습니까?");
                    alertDialogBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            dbManager.removeUser(mAdminListAdapter.adminList.get(row));
                            ArrayList<Object> arrayList = new ArrayList<Object>();
                            arrayList.add(mAdminListAdapter.adminList.get(row)._id);
                            mAdminTask = new AdminTask(3, arrayList);
                            mAdminTask.execute((Void) null);

                            mAdminListAdapter.setinitCheckedRow();
//                            listSearch();
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
        mAdminList = (ListView) findViewById(R.id.admin_list_view);

        mAdminListAdapter = new AdminListAdapter(mContext);
        mAdminListAdapter.notifyDataSetChanged();

//        listSearch();
        mAdminList.setAdapter(mAdminListAdapter);
        mAdminList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        mAdminTask = new AdminTask(0, null);
        mAdminTask.execute((Void) null);
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
            Intent intent = new Intent(getApplicationContext(), BookingListActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_admin) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*

    private void listSearch() {
        ArrayList<String> idArrayList = new ArrayList<>();
        ArrayList<String> nameArrayList = new ArrayList<>();
        ArrayList<String> infoArrayList = new ArrayList<>();

        Cursor cursor = dbManager.selectUser();
        while (cursor.moveToNext()) {
            idArrayList.add(cursor.getString(0));
            nameArrayList.add(cursor.getString(4));
            infoArrayList.add(cursor.getString(5));
        }

        mAdminListAdapter.clear();
        mAdminListAdapter.addList(idArrayList, nameArrayList, infoArrayList);
        mAdminListAdapter.notifyDataSetChanged();
    }
*/


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AdminTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        ArrayList<Object> mArrayList;
        int mFlag = 0;

        AdminTask(int flag, ArrayList<Object> arrayList) {
            mFlag = flag;
            mArrayList = arrayList;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);
                if(mFlag == 0) {
                    connect = network.shopSelect(1);
                }else if(mFlag == 1) {
                    connect = network.shopInsert(mArrayList);
                }else if(mFlag == 2) {
                    connect = network.shopUpdate(mArrayList);
                }else if(mFlag == 3) {
                    connect = network.shopDelete(mArrayList);
                }
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
                    if(mFlag != 0) {
                        mAdminTask = new AdminTask(0, null);
                        mAdminTask.execute((Void) null);
                    }else {
                        ArrayList<Admin> arrayList = new ArrayList<>();
                        for (int i = 0; i < network.mShopInfo.length(); i++) {
                            Admin admin = new Admin();
                            admin._id = network.mShopInfo.optJSONObject(i).getInt("_id");
                            admin.col = network.mShopInfo.optJSONObject(i).getInt("col");
                            admin.name = Html.fromHtml(network.mShopInfo.optJSONObject(i).getString("name")).toString();
                            admin.info = Html.fromHtml(network.mShopInfo.optJSONObject(i).getString("info")).toString();
                            admin.number = Html.fromHtml(network.mShopInfo.optJSONObject(i).getString("number")).toString();

                            arrayList.add(admin);
                        }


                        mAdminListAdapter.clear();
                        mAdminListAdapter.addList(arrayList);

                        mAdminListAdapter.notifyDataSetChanged();
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAdminTask = null;
        }
    }

    public class Admin{
        int _id;
        int col;
        String name;
        String info;
        String number;
    }
}

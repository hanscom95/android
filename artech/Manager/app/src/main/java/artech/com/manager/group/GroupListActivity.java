package artech.com.manager.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import artech.com.manager.MainActivity;
import artech.com.manager.R;
import artech.com.manager.admin.AdminListActivity;
import artech.com.manager.booking.BookingListActivity;
import artech.com.manager.guide.GuideActivity;
import artech.com.manager.scale.ScaleListActivity;
import artech.com.manager.tournament.Tournament16Activity;
import artech.com.manager.tournament.Tournament8Activity;

public class GroupListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    Button mNextButton;
    ListView mGroupList;
    GroupListAdapter mGroupListAdapter;

    ArrayList<String> mPersonsArrayList = new ArrayList<>();
    ArrayList<String> mNameArrayList = new ArrayList<>();
    String mTeamName = "", mEmail = "";
    int mPeople = 0, mTeam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTeam = bundle.getInt("team");
        mPeople  = bundle.getInt("people");
        mTeamName = bundle.getString("name");
        mEmail = bundle.getString("email");
        if(bundle.getStringArrayList("nameArray") != null) {
            mPersonsArrayList.addAll(bundle.getStringArrayList("nameArray"));
        }else {
            for(int i = 0; i < mPeople; i++) {
                mNameArrayList.add("");
            }
        }

        if(mPersonsArrayList.size() > 0) {
            mTeam = 0;
            for(int i = 0; i < mPersonsArrayList.size(); i++) {
                ArrayList<String> arrayList = convertToArray(mPersonsArrayList.get(i));
                mTeam++;
                for(int j = 0; j < arrayList.size(); j++) {
                    mNameArrayList.add(arrayList.get(j));
//                    Log.d("GroupListActivity", "arrayList " + i + "/ name : " + arrayList.get(j));
                }
            }
        }

//        Log.d("GroupListActivity", "team : " + mTeam + " / people : " + mPeople + "/ mNameArrayList" + mNameArrayList.size());
        mNextButton = (Button) findViewById(R.id.add_button);
        mGroupList = (ListView) findViewById(R.id.group_list_view);

        mGroupListAdapter = new GroupListAdapter(mContext);

//        mGroupListAdapter.addList(mNameArrayList, mPeople, mTeam);
        mGroupListAdapter.addList(mNameArrayList, mNameArrayList.size(), mTeam);
        mGroupListAdapter.notifyDataSetChanged();
        mGroupList.setAdapter(mGroupListAdapter);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScaleListActivity.class);
//                Intent intent = new Intent(getApplicationContext(), GroupTotalListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("people", mPeople);
                bundle.putInt("team", mTeam);
                bundle.putString("name", mTeamName);
                bundle.putString("email", mEmail);
                bundle.putStringArrayList("nameArray", mGroupListAdapter.nameList);
                bundle.putIntegerArrayList("score1Array", mGroupListAdapter.score1List);
                bundle.putIntegerArrayList("score2Array", mGroupListAdapter.score2List);
                bundle.putIntegerArrayList("score3Array", mGroupListAdapter.score3List);
                bundle.putIntegerArrayList("score4Array", mGroupListAdapter.score4List);
                bundle.putIntegerArrayList("score5Array", mGroupListAdapter.score5List);
                bundle.putIntegerArrayList("score6Array", mGroupListAdapter.score6List);
                bundle.putIntegerArrayList("score7Array", mGroupListAdapter.score7List);
                bundle.putIntegerArrayList("score8Array", mGroupListAdapter.score8List);
                bundle.putIntegerArrayList("score9Array", mGroupListAdapter.score9List);
                bundle.putIntegerArrayList("score10Array", mGroupListAdapter.score10List);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

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


    private ArrayList<String> convertToArray(String string) {
        try {
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(",")));
            return list;
        } catch (Exception e) {
            return null;
        }
    }
}

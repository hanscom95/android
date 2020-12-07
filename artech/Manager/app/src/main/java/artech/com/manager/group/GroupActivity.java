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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import artech.com.manager.MainActivity;
import artech.com.manager.R;
import artech.com.manager.admin.AdminListActivity;
import artech.com.manager.booking.BookingListActivity;
import artech.com.manager.guide.GuideActivity;
import artech.com.manager.tournament.Tournament16Activity;
import artech.com.manager.tournament.Tournament8Activity;

public class GroupActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    Button mPeopleRemoveButton, mPeopleAddButton, mTeamRemoveButton, mTeamAddButton, mNextButton;
    TextView mPeopleText, mTeamText;
    EditText mGroupEdit;

    int mPeople = 0, mTeam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_group);
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

        mPeopleRemoveButton = (Button) findViewById(R.id.people_remove_button);
        mPeopleAddButton = (Button) findViewById(R.id.people_add_button);
        mTeamRemoveButton = (Button) findViewById(R.id.team_remove_button);
        mTeamAddButton = (Button) findViewById(R.id.team_add_button);
        mNextButton = (Button) findViewById(R.id.next_button);

        mPeopleText = (TextView) findViewById(R.id.people_number_text);
        mTeamText = (TextView) findViewById(R.id.team_number_text);

        mGroupEdit = (EditText) findViewById(R.id.group_name_edit);

        mPeopleRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPeople > 0) {
                    mPeople--;
                }

                mPeopleText.setText(mPeople+"");
            }
        });

        mPeopleAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeople++;
                mPeopleText.setText(mPeople+"");
            }
        });

        mTeamRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTeam > 0) {
                    mTeam--;
                }
                mTeamText.setText(mTeam+"");
            }
        });
        mTeamAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam++;
                mTeamText.setText(mTeam+"");
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPeople > 0) {
                    Intent intent = new Intent(mContext, GroupListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mGroupEdit.getText().toString());
                    bundle.putInt("people", mPeople);
                    bundle.putInt("team", mTeam > 0 ?mTeam:1);
                    bundle.putString("email", "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(mContext, "인원수와 조(그룹)수를 세팅해 주세요,", Toast.LENGTH_LONG).show();
                }
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
}

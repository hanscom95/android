package se.com.band.option;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import se.com.band.R;
import se.com.band.utility.DBManager;
import se.com.band.utility.NetworkConnection;

public class ProfileActivity extends AppCompatActivity {

    Context mContext;
    DBManager dbManager;
    NetworkConnection network;
    UserAccountTask mAuthTask = null;

    RadioGroup mHeightGroup, mWeightGroup, mDistanceGroup;
    EditText mHeightEdit, mWeightEdit;
    TextView mHeightUnitText, mWeightUnitText;
    Button mContinueButton;

    HashMap<String, Object> dbAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;
        dbManager = new DBManager(getApplicationContext(), "apex.db", null, 1);

        mHeightUnitText = (TextView) findViewById(R.id.height_2nd_text);
        mWeightUnitText = (TextView) findViewById(R.id.weight_2nd_text);

        mHeightGroup = (RadioGroup)findViewById(R.id.height_group);
        mHeightGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkeditem)
            {
                RadioButton ft = (RadioButton)findViewById(R.id.ft_button);
                RadioButton cm = (RadioButton)findViewById(R.id.cm_button);

                switch(checkeditem)
                { // change button style, opacity (height RadioButton)
                    case R.id.ft_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        ft.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        cm.setTypeface(typeface, Typeface.NORMAL);

                        mHeightUnitText.setText("ft");
                        break;
                    }
                    case R.id.cm_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        cm.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        ft.setTypeface(typeface, Typeface.NORMAL);

                        mHeightUnitText.setText("cm");
                        break;
                    }
                }
            }
        });

        mWeightGroup = (RadioGroup)findViewById(R.id.weight_group);
        mWeightGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkeditem)
            {
                RadioButton lbs = (RadioButton)findViewById(R.id.lbs_button);
                RadioButton kg = (RadioButton)findViewById(R.id.kg_button);

                switch(checkeditem)
                { // change button style, opacity (weight RadioButton)
                    case R.id.lbs_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        lbs.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        kg.setTypeface(typeface, Typeface.NORMAL);

                        mWeightUnitText.setText("lbs");
                        break;
                    }
                    case R.id.kg_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        kg.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        lbs.setTypeface(typeface, Typeface.NORMAL);

                        mWeightUnitText.setText("kg");
                        break;
                    }
                }
            }
        });

        mDistanceGroup = (RadioGroup)findViewById(R.id.distance_group);
        mDistanceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkeditem)
            {
                RadioButton mile = (RadioButton)findViewById(R.id.mile_button);
                RadioButton km = (RadioButton)findViewById(R.id.km_button);

                switch(checkeditem)
                { // change button style, opacity (distance RadioButton)
                    case R.id.mile_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        mile.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        km.setTypeface(typeface, Typeface.NORMAL);
                        break;
                    }
                    case R.id.km_button : {
                        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
                        km.setTypeface(typeface, Typeface.BOLD);
                        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
                        mile.setTypeface(typeface, Typeface.NORMAL);
                        break;
                    }
                }
            }
        });

        mHeightEdit = (EditText) findViewById(R.id.height_edittext);
        mWeightEdit = (EditText) findViewById(R.id.weight_edittext);

        mContinueButton = (Button) findViewById(R.id.continue_button);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String height = mHeightEdit.getText().toString();
                String weight = mWeightEdit.getText().toString();

                ArrayList<Object> account = new ArrayList<>();
                account.add(dbAccount.get("id"));
                account.add("");//pw
                account.add(dbAccount.get("name"));
                account.add(dbAccount.get("birth"));
                account.add(height);//tall
                account.add(weight);//weight
                account.add(dbAccount.get("gender"));
                account.add(dbAccount.get("age"));//age
                account.add(dbAccount.get("device_address"));
                account.add(dbAccount.get("hand"));//hand


                mAuthTask = new UserAccountTask(account);
                mAuthTask.execute((Void) null);
            }
        });


        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
        RadioButton ft = (RadioButton) findViewById(R.id.ft_button) ;
        ft.setTypeface(typeface, Typeface.NORMAL);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
        RadioButton cm = (RadioButton) findViewById(R.id.cm_button) ;
        cm.setTypeface(typeface, Typeface.BOLD);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
        RadioButton lbs = (RadioButton) findViewById(R.id.lbs_button) ;
        lbs.setTypeface(typeface, Typeface.NORMAL);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
        RadioButton kg = (RadioButton) findViewById(R.id.kg_button) ;
        kg.setTypeface(typeface, Typeface.BOLD);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Light.otf");
        RadioButton mile = (RadioButton) findViewById(R.id.mile_button) ;
        mile.setTypeface(typeface, Typeface.NORMAL);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Semibold.otf");
        RadioButton km = (RadioButton) findViewById(R.id.km_button) ;
        km.setTypeface(typeface, Typeface.BOLD);

        setDBText();
    }

    private void setDBText() {
        dbAccount = dbManager.selectAccunt();

        mHeightEdit.setText(dbAccount.get("tall").toString());
        mWeightEdit.setText(dbAccount.get("weight").toString());
    }






    public class UserAccountTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<Object> mAccount;

        UserAccountTask(ArrayList<Object> account) {
            mAccount = account;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean connect = false;
            try {
                // Simulate network access.
                network = new NetworkConnection(mContext);
                connect = network.accountUpdate(mAccount);
            } catch (Exception e) {
                return false;
            }

            if(!connect) {
                return false;
            }else {
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                dbManager.updateUser(mAccount);

                Toast.makeText(mContext, "Success to edit account information.", Toast.LENGTH_LONG).show();
                finish();
            }else {
                Toast.makeText(mContext, "Failed to edit account information.(not success)", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            Toast.makeText(mContext, "Failed to edit account information.(onCancelled)", Toast.LENGTH_LONG).show();
        }
    }

}

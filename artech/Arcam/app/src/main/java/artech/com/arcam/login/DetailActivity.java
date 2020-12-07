package artech.com.arcam.login;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import artech.com.arcam.MainActivity;
import artech.com.arcam.R;
import artech.com.arcam.utility.DBManager;
import artech.com.arcam.utility.NetworkConnection;


public class DetailActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    Button mBackButton, mCloseButton, mCancelButton, mInsertButton, mSkipButton;

    CheckBox mAutoCheck;

    Spinner mRiserBrandSpinner, mRiserInchSpinner, mLimbsBrandSpinner, mLimbsInchSpinner, mStabilizerInchSpinner,
            mLongstaffInchSpinner, mSidestabiInchSpinner, mFingerTabBrandSpinner, mSightBrandSpinner, mCushionPlungerBrandSpinner, mRestBrandSpinner,
            mArrowBrandSpinner;

    EditText mRiserBrandText, mRiserInchText, mLimbsBrandText, mLimbsInchText, mLimbsPoundText, mStabilizerInchText,
            mLongstaffInchText, mSidestabiInchText, mFingerTabBrandText, mSightBrandText, mCushionPlungerBrandText, mRestBrandText,
            mArrowBrandText;

    DetailTask mDetailTask;

    int mFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "arcam.db", null, 1);

        setContentView(R.layout.content_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
        }

        mBackButton = (Button) findViewById(R.id.back_button);
        mCloseButton = (Button) findViewById(R.id.close_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mInsertButton = (Button) findViewById(R.id.insert_button);
        mSkipButton = (Button) findViewById(R.id.skip_button);

        mAutoCheck = (CheckBox) findViewById(R.id.auto_save_checkbox);

        mRiserBrandSpinner = (Spinner) findViewById(R.id.riser_brand_spinner);
        mRiserInchSpinner = (Spinner) findViewById(R.id.riser_inch_spinner);
        mLimbsBrandSpinner = (Spinner) findViewById(R.id.limbs_brand_spinner);
        mLimbsInchSpinner = (Spinner) findViewById(R.id.limbs_inch_spinner);
        mStabilizerInchSpinner = (Spinner) findViewById(R.id.stabilizer_brand_spinner);
        mLongstaffInchSpinner = (Spinner) findViewById(R.id.longstaff_inch_spinner);
        mSidestabiInchSpinner = (Spinner) findViewById(R.id.sidestabi_inch_spinner);
        mFingerTabBrandSpinner = (Spinner) findViewById(R.id.fingertab_brand_spinner);
        mSightBrandSpinner = (Spinner) findViewById(R.id.sight_brand_spinner);
        mCushionPlungerBrandSpinner = (Spinner) findViewById(R.id.cushionplunger_brand_spinner);
        mRestBrandSpinner = (Spinner) findViewById(R.id.rest_brand_spinner);
        mArrowBrandSpinner = (Spinner) findViewById(R.id.arrow_brand_spinner);


        mRiserBrandText = (EditText) findViewById(R.id.riser_brand_edittext);
        mRiserInchText = (EditText) findViewById(R.id.riser_inch_edittext);
        mLimbsBrandText = (EditText) findViewById(R.id.limbs_brand_edittext);
        mLimbsInchText = (EditText) findViewById(R.id.limbs_inch_edittext);
        mLimbsPoundText = (EditText) findViewById(R.id.limbs_pound_edittext);
        mStabilizerInchText = (EditText) findViewById(R.id.stabilizer_brand_edittext);
        mLongstaffInchText = (EditText) findViewById(R.id.longstaff_inch_edittext);
        mSidestabiInchText = (EditText) findViewById(R.id.sidestabi_inch_edittext);
        mFingerTabBrandText = (EditText) findViewById(R.id.fingertab_brand_edittext);
        mSightBrandText = (EditText) findViewById(R.id.sight_brand_edittext);
        mCushionPlungerBrandText = (EditText) findViewById(R.id.cushionplunger_brand_edittext);
        mRestBrandText = (EditText) findViewById(R.id.rest_brand_edittext);
        mArrowBrandText = (EditText) findViewById(R.id.arrow_brand_edittext);


        ArrayAdapter<String> adapter;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.riser));
        adapter.setDropDownViewResource(R.layout.signup_spinner);

        mRiserBrandSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.riser_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mRiserInchSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.limbs));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mLimbsBrandSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.limbs_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mLimbsInchSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.stabilizer));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mStabilizerInchSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.longstaff_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mLongstaffInchSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.sidestabi_inch));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mSidestabiInchSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.fingertab));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mFingerTabBrandSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.sight));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mSightBrandSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.cushionplunger));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mCushionPlungerBrandSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.rest));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mRestBrandSpinner.setAdapter(adapter);;
        adapter= new ArrayAdapter<String>(mContext, R.layout.signup_spinner_selected, getResources().getStringArray(R.array.arrow));
        adapter.setDropDownViewResource(R.layout.signup_spinner);
        mArrowBrandSpinner.setAdapter(adapter);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
//                startActivity(intent);
//                finish();


//                ArrayList<Object> arrayList = new ArrayList<>();
//                arrayList.add(mRiserBrandSpinner.getSelectedItemPosition());
//                arrayList.add(mRiserBrandText.getText().toString());
//                arrayList.add(mRiserInchSpinner.getSelectedItemPosition());
//                arrayList.add(mRiserInchText.getText().toString());
//                arrayList.add(mLimbsBrandSpinner.getSelectedItemPosition());
//                arrayList.add(mLimbsBrandText.getText().toString());
//                arrayList.add(mLimbsInchSpinner.getSelectedItemPosition());
//                arrayList.add(mLimbsInchText.getText().toString());
//                arrayList.add(mLimbsPoundText.getText().toString());
//                arrayList.add(mStabilizerInchSpinner.getSelectedItemPosition());
//                arrayList.add(mStabilizerInchText.getText().toString());
//                arrayList.add(mLongstaffInchSpinner.getSelectedItemPosition());
//                arrayList.add(mLongstaffInchText.getText().toString());
//                arrayList.add(mSidestabiInchSpinner.getSelectedItemPosition());
//                arrayList.add(mSidestabiInchText.getText().toString());
//                arrayList.add(mFingerTabBrandSpinner.getSelectedItemPosition());
//                arrayList.add(mFingerTabBrandText.getText().toString());
//                arrayList.add(mSightBrandSpinner.getSelectedItemPosition());
//                arrayList.add(mSightBrandText.getText().toString());
//                arrayList.add(mCushionPlungerBrandSpinner.getSelectedItemPosition());
//                arrayList.add(mCushionPlungerBrandText.getText().toString());
//                arrayList.add(mRestBrandSpinner.getSelectedItemPosition());
//                arrayList.add(mRestBrandText.getText().toString());
//                arrayList.add(mArrowBrandSpinner.getSelectedItemPosition());
//                arrayList.add(mArrowBrandText.getText().toString());
//                if(mAutoCheck.isChecked()) {
//                    arrayList.add(1);
//                }else {
//                    arrayList.add(0);
//                }

//                mDbManager.updateEquipment(arrayList);
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);

                if(mFlag == 0) {
                    mDbManager.insertEquipment(arrayList);
                }else {
                    mDbManager.updateEquipment(arrayList);
                }

                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();

//                Cursor cursor = mDbManager.selectEquipment();
//                while (cursor.moveToNext()) {
//                    Log.d("DetailActivity", "riser_brand : "+ cursor.getInt(0)+ " / riser_text : "+ cursor.getString(1)+ " / auto : "+ cursor.getInt(25));
//                }


            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add("");
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);
                arrayList.add(0);

                if(mFlag == 0) {
                    mDbManager.insertEquipment(arrayList);
                }else {
                    mDbManager.updateEquipment(arrayList);
                }

                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<Object> arrayList = new ArrayList<>();
                    arrayList.add(mRiserBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mRiserBrandText.getText().toString());
                    arrayList.add(mRiserInchSpinner.getSelectedItemPosition());
                    arrayList.add(mRiserInchText.getText().toString());
                    arrayList.add(mLimbsBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mLimbsBrandText.getText().toString());
                    arrayList.add(mLimbsInchSpinner.getSelectedItemPosition());
                    arrayList.add(mLimbsInchText.getText().toString());
                    arrayList.add(mLimbsPoundText.getText().toString());
                    arrayList.add(mStabilizerInchSpinner.getSelectedItemPosition());
                    arrayList.add(mStabilizerInchText.getText().toString());
                    arrayList.add(mLongstaffInchSpinner.getSelectedItemPosition());
                    arrayList.add(mLongstaffInchText.getText().toString());
                    arrayList.add(mSidestabiInchSpinner.getSelectedItemPosition());
                    arrayList.add(mSidestabiInchText.getText().toString());
                    arrayList.add(mFingerTabBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mFingerTabBrandText.getText().toString());
                    arrayList.add(mSightBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mSightBrandText.getText().toString());
                    arrayList.add(mCushionPlungerBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mCushionPlungerBrandText.getText().toString());
                    arrayList.add(mRestBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mRestBrandText.getText().toString());
                    arrayList.add(mArrowBrandSpinner.getSelectedItemPosition());
                    arrayList.add(mArrowBrandText.getText().toString());
                    if (mAutoCheck.isChecked()) {
                        arrayList.add(1);
                    } else {
                        arrayList.add(0);
                    }
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);


                    mDetailTask = new DetailTask(mFlag, arrayList);
                    mDetailTask.execute((Void) null);
                }catch (Exception e) {
                    Toast.makeText(mContext, getText(R.string.detail_insert_error).toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<Object> arrayList = new ArrayList<>();
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add("");
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);
                    arrayList.add(0);

                    if(mFlag == 0) {
                        mDbManager.insertEquipment(arrayList);
                    }else {
                        mDbManager.updateEquipment(arrayList);
                    }


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e) {
                    Toast.makeText(mContext, getText(R.string.detail_insert_error).toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        initSetting();
    }


    @Override
    public void onBackPressed() {
        try {
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add("");
            arrayList.add(0);
            arrayList.add(0);
            arrayList.add(0);
            arrayList.add(0);
            arrayList.add(0);
            arrayList.add(0);
            arrayList.add(0);

            if(mFlag == 0) {
                mDbManager.insertEquipment(arrayList);
            }else {
                mDbManager.updateEquipment(arrayList);
            }


            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e) {
            Toast.makeText(mContext, getText(R.string.detail_insert_error).toString(), Toast.LENGTH_LONG).show();
        }

        super.onBackPressed();
    }

    private void initSetting() {
        Cursor cursor = mDbManager.selectEquipment();
        if(cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Log.d("DetailActivity", "riser_brand : " + cursor.getInt(0) + " / riser_text : " + cursor.getString(1) + " / auto : " + cursor.getInt(25));

                mFlag = 1;

                mInsertButton.setText(R.string.detail_button);


                mRiserBrandSpinner.setSelection(cursor.getInt(0));
                mRiserInchSpinner.setSelection(cursor.getInt(2));
                mLimbsBrandSpinner.setSelection(cursor.getInt(4));
                mLimbsInchSpinner.setSelection(cursor.getInt(6));
                mStabilizerInchSpinner.setSelection(cursor.getInt(9));
                mLongstaffInchSpinner.setSelection(cursor.getInt(11));
                mSidestabiInchSpinner.setSelection(cursor.getInt(13));
                mFingerTabBrandSpinner.setSelection(cursor.getInt(15));
                mSightBrandSpinner.setSelection(cursor.getInt(17));
                mCushionPlungerBrandSpinner.setSelection(cursor.getInt(19));
                mRestBrandSpinner.setSelection(cursor.getInt(21));
                mArrowBrandSpinner.setSelection(cursor.getInt(23));

                mRiserBrandText.setText(cursor.getString(1));
                mRiserInchText.setText(cursor.getString(3));
                mLimbsBrandText.setText(cursor.getString(5));
                mLimbsInchText.setText(cursor.getString(7));
                mLimbsPoundText.setText(cursor.getString(8));
                mStabilizerInchText.setText(cursor.getString(10));
                mLongstaffInchText.setText(cursor.getString(12));
                mSidestabiInchText.setText(cursor.getString(14));
                mFingerTabBrandText.setText(cursor.getString(16));
                mSightBrandText.setText(cursor.getString(18));
                mCushionPlungerBrandText.setText(cursor.getString(20));
                mRestBrandText.setText(cursor.getString(22));
                mArrowBrandText.setText(cursor.getString(24));

                if(cursor.getInt(25) == 1) {
                    mAutoCheck.setChecked(true);
                }else {
                    mAutoCheck.setChecked(false);
                }
            }
        }
    }





    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DetailTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        ArrayList<Object> mArrayList;
        int flag;

        DetailTask(int flag, ArrayList<Object> arrayList) {
            mArrayList = arrayList;
            flag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.add(mDbManager.selectId());
                for(int i = 0;  i < mArrayList.size(); i++) {
                    arrayList.add(mArrayList.get(i));
                }


                network = new NetworkConnection(mContext);

                if(flag == 0) {
                    connect = network.equipmentInsert(arrayList);
                }else {
                    connect = network.equipmentUpdate(arrayList);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mDetailTask = null;


            if(flag == 0) {
                mDbManager.insertEquipment(mArrayList);
            }else {
                mDbManager.updateEquipment(mArrayList);
            }


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onCancelled() {
            mDetailTask = null;
        }
    }
}

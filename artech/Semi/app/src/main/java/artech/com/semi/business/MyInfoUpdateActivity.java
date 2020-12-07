package artech.com.semi.business;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import artech.com.semi.R;
import artech.com.semi.login.IntroActivity;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class MyInfoUpdateActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    ImageView mProfileImg;
    EditText mNameEdit, mEmailEdit, mPwEdit, mPwCheckEdit;
    TextView mLogoutText;

    String mProfile;

    UpdateTask mUpdateTask;
    SelectTask mSelectTask;

    JSONObject mJsonObject;

    int mFlag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_business_my_info_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView saveText = findViewById(R.id.save_text);
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNameEdit.getText().toString() == "" || "".equals(mNameEdit.getText().toString())) {
                    Toast.makeText(mContext, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mEmailEdit.getText().toString() == "" || "".equals(mEmailEdit.getText().toString())) {
                    Toast.makeText(mContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mFlag == 0) {
                    if (mPwEdit.getText().toString() == "" || "".equals(mPwEdit.getText().toString())) {
                        Toast.makeText(mContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!mPwEdit.getText().toString().equals(mPwCheckEdit.getText().toString())) {
                        Toast.makeText(mContext, "비밀번호를 확인 후 다시 저장해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mDbManager.selectUserId());
                    jsonObject.put("name", mNameEdit.getText().toString());
                    jsonObject.put("email", mEmailEdit.getText().toString());
                    jsonObject.put("pw", mPwEdit.getText().toString());
                    if(mProfile != null || mProfile != "" || "".equals(mProfile)) {
                        jsonObject.put("picture", mProfile);
                    }

                    mUpdateTask = new UpdateTask(jsonObject);
                    mUpdateTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        mNameEdit = findViewById(R.id.nickname_edit);
        mEmailEdit = findViewById(R.id.email_edit);
        mPwEdit = findViewById(R.id.password_edit);
        mPwCheckEdit = findViewById(R.id.password_check_edit);

        mLogoutText = findViewById(R.id.logout_text);


        mProfileImg = findViewById(R.id.img_thumbnail);



        mLogoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setMessage("로그아웃 하시겠습니까?");
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDbManager.dropDB();
                        dialogInterface.dismiss();

                        Intent intent = new Intent(mContext, IntroActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }
        });

        mProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }


                selectGallery();
            }
        });

        mSelectTask = new SelectTask();
        mSelectTask.execute();
    }


    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }


    private String sendPicture(Uri imgUri) {

        String imagePath = Util.getRealPathFromURI(mContext, imgUri); // path 경로
        Log.d("sendPicture", "imagePath : " + imagePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);//경로를 통해 비트맵으로 전환

        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = Util.exifOrientationToDegrees(exifOrientation);
            bitmap = Util.rotate(bitmap, exifDegree);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap != null) {
            return Util.getEncoded64ImageStringFromBitmap(bitmap);
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            mProfile = "";

            if(data.getClipData() == null) {
                mProfile = sendPicture(data.getData());
            }

            if(mProfile != "" || !"".equals(mProfile)) {
                mProfileImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mProfile));
            }

        }
    }


    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        UpdateTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.accountUpdate(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;

            Log.d("MyInfoUpdateActivity", "mAdminTask : " + success);
            if(success) {
                finish();
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
        }
    }


    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        SelectTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.accountSelect(mDbManager.selectUserId());
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;
            String path = null;

            if(success) {

                try {
                    mJsonObject = network.mAccountInfo.getJSONObject(0);

                    mNameEdit.setText(Html.fromHtml(mJsonObject.getString("name")).toString());
                    mEmailEdit.setText(Html.fromHtml(mJsonObject.getString("email")).toString());
                    mFlag = mJsonObject.getInt("join_flag");

                    if(mFlag == 0) {
                        mPwEdit.setText(Html.fromHtml(mJsonObject.getString("pw")).toString());
                    }else {
                        mPwEdit.setEnabled(false);
                        mPwCheckEdit.setEnabled(false);
                    }


                    if(mJsonObject.getString("picture").length() > 0) {
                        mProfileImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mJsonObject.getString("picture")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
                Toast.makeText(mContext, "회원 정보를 조회하는데 실패하였습니다. 다시 접속해 주세요.", Toast.LENGTH_SHORT).show();
                finish();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }
}

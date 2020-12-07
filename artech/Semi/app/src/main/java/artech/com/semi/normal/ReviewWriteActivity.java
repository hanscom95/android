package artech.com.semi.normal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class ReviewWriteActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    CheckBox mOnePointCheckbox, mTwoPointCheckbox, mThreePointCheckbox, mFourPointCheckbox, mFivePointCheckbox;
    RelativeLayout mFirstCameraLayout, mSecondCameraLayout, mThirdCameraLayout;
    ImageView mFirstImg, mSecondImg, mThirdImg;
    TextView mSuccessText, mNameText;
    EditText mContentsEdit;

    ArrayList<String> mImgArrayList = new ArrayList<>();

    InsertTask mInsertTask;

    String mId;

    int mPoint = 0;

    String mFirst, mSecond, mThird;

    private final int GALLERY_CODE=1112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_review_write);
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

        mOnePointCheckbox = findViewById(R.id.one_point_checkbox);
        mTwoPointCheckbox = findViewById(R.id.two_point_checkbox);
        mThreePointCheckbox = findViewById(R.id.three_point_checkbox);
        mFourPointCheckbox = findViewById(R.id.four_point_checkbox);
        mFivePointCheckbox = findViewById(R.id.five_point_checkbox);

        mFirstCameraLayout = findViewById(R.id.first_camera_layout);
        mSecondCameraLayout = findViewById(R.id.second_camera_layout);
        mThirdCameraLayout = findViewById(R.id.third_camera_layout);

        mFirstImg = findViewById(R.id.first_img);
        mSecondImg = findViewById(R.id.second_img);
        mThirdImg = findViewById(R.id.third_img);

        mSuccessText = findViewById(R.id.review_success_text);
        mNameText = findViewById(R.id.nickname_value_text);

        mContentsEdit = findViewById(R.id.review_value_text);

        mSuccessText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if("".equals(mContentsEdit.getText().toString()) || mContentsEdit.getText().toString() == "") {
                    Toast.makeText(mContext, "리뷰를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", mDbManager.selectUserId());
                    jsonObject.put("market_id", mId);
                    jsonObject.put("point", mPoint);
                    jsonObject.put("contents",mContentsEdit.getText().toString());


                    if(mFirst != null) {
                        mImgArrayList.add(mFirst);
                    }

                    if(mSecond != null) {
                        mImgArrayList.add(mSecond);
                    }

                    if(mThird != null) {
                        mImgArrayList.add(mThird);
                    }

                    if(mImgArrayList.size() > 0) {
                        jsonObject.put("picture_length", mImgArrayList.size());
                        for(int i = 0; i < mImgArrayList.size(); i++) {
                            jsonObject.put("picture"+ (i+1), mImgArrayList.get(i));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mInsertTask = new InsertTask(jsonObject);
                mInsertTask.execute();
            }
        });

        mOnePointCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnePointCheckbox.isChecked()) {
                    mPoint = 1;

                    mTwoPointCheckbox.setChecked(false);
                    mThreePointCheckbox.setChecked(false);
                    mFourPointCheckbox.setChecked(false);
                    mFivePointCheckbox.setChecked(false);
                }else {
                    if(mTwoPointCheckbox.isChecked() || mThreePointCheckbox.isChecked() || mFourPointCheckbox.isChecked() || mFivePointCheckbox.isChecked()) {
                        mPoint = 1;

                        mOnePointCheckbox.setChecked(true);
                        mTwoPointCheckbox.setChecked(false);
                        mThreePointCheckbox.setChecked(false);
                        mFourPointCheckbox.setChecked(false);
                        mFivePointCheckbox.setChecked(false);
                    }else {
                        mPoint = 0;

                        mOnePointCheckbox.setChecked(false);
                        mTwoPointCheckbox.setChecked(false);
                        mThreePointCheckbox.setChecked(false);
                        mFourPointCheckbox.setChecked(false);
                        mFivePointCheckbox.setChecked(false);
                    }
                }
            }
        });

        mTwoPointCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTwoPointCheckbox.isChecked()) {
                    mPoint = 2;

                    mOnePointCheckbox.setChecked(true);

                    mThreePointCheckbox.setChecked(false);
                    mFourPointCheckbox.setChecked(false);
                    mFivePointCheckbox.setChecked(false);
                }else {
                    if(mThreePointCheckbox.isChecked() || mFourPointCheckbox.isChecked() || mFivePointCheckbox.isChecked()) {
                        mPoint = 2;

                        mOnePointCheckbox.setChecked(true);
                        mTwoPointCheckbox.setChecked(true);
                        mThreePointCheckbox.setChecked(false);
                        mFourPointCheckbox.setChecked(false);
                        mFivePointCheckbox.setChecked(false);
                    }else {
                        mPoint = 0;

                        mOnePointCheckbox.setChecked(false);
                        mTwoPointCheckbox.setChecked(false);
                        mThreePointCheckbox.setChecked(false);
                        mFourPointCheckbox.setChecked(false);
                        mFivePointCheckbox.setChecked(false);
                    }
                }
            }
        });

        mThreePointCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreePointCheckbox.isChecked()) {
                    mPoint = 3;

                    mOnePointCheckbox.setChecked(true);
                    mTwoPointCheckbox.setChecked(true);

                    mFourPointCheckbox.setChecked(false);
                    mFivePointCheckbox.setChecked(false);
                }else {
                    if(mFourPointCheckbox.isChecked() || mFivePointCheckbox.isChecked()){
                        mPoint = 3;

                        mThreePointCheckbox.setChecked(true);
                        mFourPointCheckbox.setChecked(false);
                        mFivePointCheckbox.setChecked(false);
                    }else {
                        mPoint = 0;

                        mOnePointCheckbox.setChecked(false);
                        mTwoPointCheckbox.setChecked(false);
                        mThreePointCheckbox.setChecked(false);
                        mFourPointCheckbox.setChecked(false);
                        mFivePointCheckbox.setChecked(false);
                    }
                }
            }
        });

        mFourPointCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFourPointCheckbox.isChecked()) {
                    mPoint = 4;

                    mOnePointCheckbox.setChecked(true);
                    mTwoPointCheckbox.setChecked(true);
                    mThreePointCheckbox.setChecked(true);

                    mFivePointCheckbox.setChecked(false);
                }else {
                    if(mFivePointCheckbox.isChecked()) {
                        mPoint = 4;

                        mFourPointCheckbox.setChecked(true);
                        mFivePointCheckbox.setChecked(false);
                    }else {
                        mPoint = 0;

                        mOnePointCheckbox.setChecked(false);
                        mTwoPointCheckbox.setChecked(false);
                        mThreePointCheckbox.setChecked(false);
                        mFourPointCheckbox.setChecked(false);
                        mFivePointCheckbox.setChecked(false);
                    }
                }
            }
        });

        mFivePointCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFivePointCheckbox.isChecked()) {
                    mPoint = 5;

                    mOnePointCheckbox.setChecked(true);
                    mTwoPointCheckbox.setChecked(true);
                    mThreePointCheckbox.setChecked(true);
                    mFourPointCheckbox.setChecked(true);
                }else {
                    mPoint = 0;

                    mOnePointCheckbox.setChecked(false);
                    mTwoPointCheckbox.setChecked(false);
                    mThreePointCheckbox.setChecked(false);
                    mFourPointCheckbox.setChecked(false);
                    mFivePointCheckbox.setChecked(false);
                }
            }
        });

        mFirstCameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }

                selectGallery(0);
            }
        });

        mSecondCameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }

                selectGallery(1);
            }
        });

        mThirdCameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }


                selectGallery(2);
            }
        });


        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
    }

    private void selectGallery(int flag) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, flag);
    }


    private String sendPicture(Uri imgUri) {

        String imagePath = Util.getRealPathFromURI(mContext, imgUri); // path 경로
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

            String send = "";

            if(data.getClipData() == null) {
                send = sendPicture(data.getData());

                if(requestCode == 0) {
                    mFirst = send;
                }else if(requestCode == 1) {
                    mSecond = send;
                }else if(requestCode == 2) {
                    mThird = send;
                }
//                mImgArrayList.add(send); //갤러리에서 가져오기
            }

            Log.d("onActivityResult", "mImgArrayList : " + mImgArrayList.size());
            if(send != "" || !"".equals(send)) {
                if(requestCode == 0) {
                    mFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mFirst));
                }else if(requestCode == 1) {
                    mSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mSecond));
                }else if(requestCode == 2) {
                    mThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mThird));
                }
            }

        }
    }


    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject mJsonObject;

        InsertTask(JSONObject jsonObject) {
            mJsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.reviewInsert(mJsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
//                Intent intent = new Intent(mContext, ProductManagementActivity.class);
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//                startActivity(intent);
//                finish();

                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mInsertTask = null;
        }
    }


}

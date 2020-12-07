package artech.com.semi.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.business.BusinessMainActivity;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

import static android.os.Build.VERSION_CODES.M;

public class SignupBusinessSecondActivity extends AppCompatActivity {

    Context mContext;
    SignupTask mSignupTask;
    DBManager mDbManager;

    RelativeLayout mBankSelectLayout, mAddressLayout, mAttachLayout, mAttachFirstLayout, mAttachSecondLayout, mCompanyLayout, mCompanyFirstLayout, mCompanySecondLayout;
    TextView mBankSelectText, mAddressText;
    EditText mNameEdit, mCompanyEdit, mRegistEdit, mNumberEdit, mExplanationEdit, mNoticeEdit, mTagEdit, mAccountHolderEdit, mAccountNumberEdit;
    CheckBox mParkingCheckbox, mWifiCheckbox, mParcelsCheckbox, mAsCheckbox, mAllDayCheckbox, mShopCheckbox, mGuideCheckbox, mBathroomCheckbox, mProductCheckbox;
    ImageView mAttachFileImg, mCompanyFirstImg, mCompanySecondImg, mCompanyThirdImg;
    Spinner mBankSpinner;
    Button mSignupButton;

    int mFlag = -1;
    JSONObject mJsonData;

    ArrayList<String> mImgArrayList = new ArrayList<>();
    ArrayList<String> mAttachImgArrayList = new ArrayList<>();

    String mLat, mLon;

    private final int ATTACH_CODE=1112;
    private final int COMPANY_CODE=1113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_business_second);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView closeImg = findViewById(R.id.app_bar_close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBankSelectLayout = findViewById(R.id.bank_select_layout);
        mAddressLayout = findViewById(R.id.address_layout);
        mAttachLayout = findViewById(R.id.file_layout);
        mAttachFirstLayout = findViewById(R.id.file_first_layout);
        mAttachSecondLayout = findViewById(R.id.file_second_layout);
        mCompanyLayout = findViewById(R.id.company_picture_layout);
        mCompanyFirstLayout = findViewById(R.id.company_picture_first_layout);
        mCompanySecondLayout = findViewById(R.id.company_picture_second_layout);

        mAttachFileImg = findViewById(R.id.attach_file_img);
        mCompanyFirstImg = findViewById(R.id.company_first_img);
        mCompanySecondImg= findViewById(R.id.company_second_img);
        mCompanyThirdImg = findViewById(R.id.company_third_img);

        mBankSelectText = findViewById(R.id.signup_bank_text);

        mBankSpinner = findViewById(R.id.bank_spinner);

        mNameEdit = findViewById(R.id.name_edit);
        mCompanyEdit = findViewById(R.id.company_edit);
        mRegistEdit = findViewById(R.id.regist_edit);
        mAddressText = findViewById(R.id.address_edit);
        mNumberEdit = findViewById(R.id.number_edit);
        mCompanyEdit = findViewById(R.id.company_edit);
        mExplanationEdit = findViewById(R.id.explanation_edit);
        mNoticeEdit = findViewById(R.id.notice_edit);
        mTagEdit = findViewById(R.id.tag_edit);
        mAccountHolderEdit = findViewById(R.id.account_holder_edit);
        mAccountNumberEdit = findViewById(R.id.account_number_edit);

        mParkingCheckbox = findViewById(R.id.parking_checkbox);
        mWifiCheckbox = findViewById(R.id.wifi_checkbox);
        mParcelsCheckbox = findViewById(R.id.parcels_checkbox);
        mAsCheckbox = findViewById(R.id.as_checkbox);
        mAllDayCheckbox = findViewById(R.id.all_day_checkbox);
        mShopCheckbox = findViewById(R.id.shop_checkbox);
        mGuideCheckbox = findViewById(R.id.guide_checkbox);
        mBathroomCheckbox = findViewById(R.id.bathroom_checkbox);
        mProductCheckbox = findViewById(R.id.product_checkbox);

        mSignupButton = findViewById(R.id.signup_button);

        ArrayAdapter<String> adapter = new ArrayAdapter(mContext, R.layout.custom_address_spinner, getResources().getStringArray(R.array.bank_code));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBankSpinner.setAdapter(adapter);

        mAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressSearchActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

        mAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressSearchActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

        mAttachLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAttachGallery();

            }
        });

        mCompanyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCompanyGallery();
            }
        });

        mBankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0 && mBankSelectText.getVisibility() == View.VISIBLE) {
                    mBankSelectText.setVisibility(View.GONE);
                    mBankSelectLayout.setBackgroundColor(getColor(R.color.white));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        ((TextView)mBankSpinner.getChildAt(0)).setTextColor(Color.TRANSPARENT);
        Intent intent = getIntent();
        mFlag = intent.getIntExtra("flag", 0);
        Log.d("Signup", "flag : " + intent.getIntExtra("flag", 0));
        Log.d("Signup", "json : " + intent.getStringExtra("user"));
//        mIdLayout = (RelativeLayout) findViewById(R.id.id_layout);
//        mPwLayout = (RelativeLayout) findViewById(R.id.pw_layout);
//        mPwCheckLayout = (RelativeLayout) findViewById(R.id.pw_check_layout);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNameEdit.getText().toString() == "" || "".equals(mNameEdit.getText().toString())) {
                    Toast.makeText(mContext, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mCompanyEdit.getText().toString() == "" || "".equals(mCompanyEdit.getText().toString())) {
                    Toast.makeText(mContext, "업체명을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mRegistEdit.getText().toString() == "" || "".equals(mRegistEdit.getText().toString())) {
                    Toast.makeText(mContext, "사업자등록번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mAddressText.getText().toString() == "" || "".equals(mAddressText.getText().toString())) {
                    Toast.makeText(mContext, "주소를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }


                try {
                    int service = 0;
                    if(mParkingCheckbox.isChecked())
                        service += 1;

                    if(mWifiCheckbox.isChecked())
                        service += 2;

                    if(mParcelsCheckbox.isChecked())
                        service += 4;

                    if(mAsCheckbox.isChecked())
                        service += 8;

                    if(mAllDayCheckbox.isChecked())
                        service += 16;

                    if(mShopCheckbox.isChecked())
                        service += 32;

                    if(mGuideCheckbox.isChecked())
                        service += 64;

                    if(mBathroomCheckbox.isChecked())
                        service += 128;

                    if(mProductCheckbox.isChecked())
                        service += 256;


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mJsonData.get("id").toString()); // 아이디
                    jsonObject.put("pw", mJsonData.get("pw").toString()); // 패스워드
                    jsonObject.put("name", mNameEdit.getText().toString()); // 이름
                    jsonObject.put("email", mJsonData.get("email").toString()); // 이메일
                    if(mFlag > 0) {
                        jsonObject.put("img", mJsonData.get("img").toString()); // 유저 사진
                    }
                    jsonObject.put("flag", mFlag); // 가입 sns
                    jsonObject.put("state", 1); // 판매자
                    jsonObject.put("auth", 0); // 업체 인증여부
                    jsonObject.put("market_name", mNameEdit.getText().toString()); // 판매자 이름
                    jsonObject.put("company", mCompanyEdit.getText().toString()); // 업체 이름
                    jsonObject.put("address", mAddressText.getText().toString()); // 업체 주소
                    jsonObject.put("phone", mNumberEdit.getText().toString());// 업체 연락처
                    jsonObject.put("number", mRegistEdit.getText().toString());//사업자 등록번호
                    if(mAttachImgArrayList.size() > 0) {
                        jsonObject.put("number_pictrue", mAttachImgArrayList.get(0));// 사업자 등록번호 이미지
                    }
                    jsonObject.put("bank", mBankSpinner.getSelectedItemPosition());
                    jsonObject.put("account_holder", mAccountHolderEdit.getText().toString());
                    jsonObject.put("account_number", mAccountNumberEdit.getText().toString());
                    jsonObject.put("lat", mLat);
                    jsonObject.put("lon", mLon);
                    jsonObject.put("comment", mExplanationEdit.getText().toString());
                    jsonObject.put("service", service);
                    jsonObject.put("info", mNoticeEdit.getText().toString());
                    jsonObject.put("tag", mTagEdit.getText().toString());
                    jsonObject.put("picture_length", mImgArrayList.size());
                    if(mImgArrayList.size() > 0) {
                        jsonObject.put("picture1", mImgArrayList.get(0));
                    }
                    if(mImgArrayList.size() > 1) {
                        jsonObject.put("picture2", mImgArrayList.get(1));
                    }
                    if(mImgArrayList.size() > 2) {
                        jsonObject.put("picture3", mImgArrayList.get(2));
                    }

                    Log.d("mSignupButton", "mJsonData : " + jsonObject.toString());
                    mSignupTask = new SignupTask(jsonObject);
                    mSignupTask.execute();
//                    mJsonData.put("number_pictrue", mBusinessNumberEdit.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        setInitData(intent);

        checkPermissionF();
    }

    private void setInitData(Intent intent) {
        String result = intent.getStringExtra("user").toString();
        try {
            mJsonData = new JSONObject(result);

            if(mFlag == 1) {
                mNameEdit.setText(mJsonData.getString("name"));
                mNameEdit.setEnabled(false);
                Log.d("kakao", "name : " + mJsonData.getString("name"));
                Log.d("kakao", "id : " + mJsonData.getString("id"));
                Log.d("kakao", "img : " + mJsonData.getString("img"));
            }else if(mFlag == 2) {
                mNameEdit.setText(mJsonData.getString("name"));
                mNameEdit.setEnabled(false);
                Log.d("facebook", "name : " + mJsonData.getString("name"));
                Log.d("facebook", "id : " + mJsonData.getString("id"));
                Log.d("facebook", "img : " + mJsonData.getString("img"));
            }else if(mFlag == 3) {
                mNameEdit.setText(mJsonData.get("name").toString());
                mNameEdit.setEnabled(false);
//                    Log.d("naver", "id : "+ mJsonData.getJSONObject("response").get("id"));
//                    Log.d("naver", "name : "+ mJsonData.getJSONObject("response").get("name"));
                Log.d("naver", "id : "+ mJsonData.get("id"));
                Log.d("naver", "name : "+ mJsonData.get("name"));
                Log.d("naver", "img : " + mJsonData.getString("img"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SignupBusinessSecondActivity", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("SignupBusinessSecondActivity", "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SignupBusinessSecondActivity", "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case COMPANY_CODE:
                    if(mImgArrayList.size() > 0)
                        mImgArrayList.clear();

                    if(data.getClipData() == null) {
                        mImgArrayList.add(sendPicture(data.getData())); //갤러리에서 가져오기
                    }else {
                        ClipData clipData = data.getClipData();

                        if (clipData.getItemCount() > 3){
                            Toast.makeText(mContext, "사진은 3개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 멀티 선택에서 하나만 선택했을 경우
                        else if (clipData.getItemCount() == 1) {
                            String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                            Uri uri = Uri.parse(clipData.getItemAt(0).getUri().getPath());
//                            mImgArrayList.add(sendPicture(clipData.getItemAt(0).getUri()));
                            mImgArrayList.add(sendPicture(clipData.getItemAt(0).getUri()));

                        } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri uri = Uri.parse(clipData.getItemAt(i).getUri().getPath());
                                mImgArrayList.add(sendPicture(clipData.getItemAt(i).getUri()));
                            }
                        }
                    }



                    if(mImgArrayList.size() > 0) {
                        mCompanyFirstLayout.setVisibility(View.GONE);
                        mCompanySecondLayout.setVisibility(View.VISIBLE);
                        for(int i = 0; i < mImgArrayList.size(); i++) {
                            if(i == 0) {
                                mCompanyFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                            }else if(i == 1) {
                                mCompanySecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                            }else if(i == 2) {
                                mCompanyThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                            }

                        }
                    }
                    break;

                case ATTACH_CODE:
                    if(data.getClipData() == null) {
                        mAttachImgArrayList.add(sendPicture(data.getData())); //갤러리에서 가져오기
                    }else {
                        ClipData clipData = data.getClipData();

                        if (clipData.getItemCount() == 1) {
                            String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                            Uri uri = Uri.parse(clipData.getItemAt(0).getUri().getPath());
//                            mImgArrayList.add(sendPicture(clipData.getItemAt(0).getUri()));
                            mAttachImgArrayList.add(sendPicture(clipData.getItemAt(0).getUri()));

                        }
                    }



                    if(mAttachImgArrayList.size() > 0) {
                        mAttachFirstLayout.setVisibility(View.GONE);
                        mAttachSecondLayout.setVisibility(View.VISIBLE);
                        for(int i = 0; i < mAttachImgArrayList.size(); i++) {
                            if(i == 0) {
                                mAttachFileImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mAttachImgArrayList.get(i)));
                            }

                        }
                    }
                    break;

                default:
                    break;
            }

        }


        if(resultCode == 1000) {
            mAddressText.setText(data.getStringExtra("address"));
            mLat = data.getDoubleExtra("lon", 0) + "";
            mLon = data.getDoubleExtra("lat", 0) + "";
        }
    }


    private void selectAttachGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, ATTACH_CODE);
    }
    private void selectCompanyGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, COMPANY_CODE);
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



    private class SignupTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject mJsonObject;

        SignupTask(JSONObject jsonObject) {
            mJsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.accountInsert(mJsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSignupTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                mDbManager.insertUser(mJsonObject);

                Intent intent = new Intent(getApplicationContext(), BusinessMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(mContext, "계정을 만들 수 없습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSignupTask = null;
        }
    }



    @SuppressLint("WrongConstant")
    private void checkPermissionF() {

        if (android.os.Build.VERSION.SDK_INT >= M) {
            // only for LOLLIPOP and newer versions
            int permissionResult = 0;
            permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionResult == getPackageManager().PERMISSION_DENIED) {
                //요청한 권한( WRITE_EXTERNAL_STORAGE )이 없을 때..거부일때...
                        /* 사용자가 WRITE_EXTERNAL_STORAGE 권한을 한번이라도 거부한 적이 있는 지 조사한다.
                        * 거부한 이력이 한번이라도 있다면, true를 리턴한다.
                        */
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                    if (Build.VERSION.SDK_INT >= M) {
                  /*          requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);*/
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }


                    //최초로 권한을 요청할 때.
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    //        getThumbInfo();
                }
            }else{
                //권한이 있을 때.
                //       getThumbInfo();
            }

        } else {
            //   getThumbInfo();
        }

    }

    /**
     * 사용자가 권한을 허용했는지 거부했는지 체크
     * @param requestCode   1번
     * @param permissions   개발자가 요청한 권한들
     * @param grantResults  권한에 대한 응답들
     *                    permissions와 grantResults는 인덱스 별로 매칭된다.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            /* 요청한 권한을 사용자가 "허용"했다면 인텐트를 띄워라
                내가 요청한 게 하나밖에 없기 때문에. 원래 같으면 for문을 돈다.*/
/*            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);*/

            for(int i = 0 ; i < permissions.length ; i++) {
                if (grantResults.length > 0 && grantResults[i] == getPackageManager().PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED) {
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != getPackageManager().PERMISSION_GRANTED) {
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != getPackageManager().PERMISSION_GRANTED) {
                    }
          /*          if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_PHONE_STATE ( 권한 성공 ) ");
                    }*/
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED) {
                    }
                }


            }

        } else {
            Toast.makeText(mContext, "권한설정 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }

    }

}

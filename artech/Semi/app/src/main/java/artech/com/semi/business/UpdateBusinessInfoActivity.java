package artech.com.semi.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import artech.com.semi.login.AddressSearchActivity;
import artech.com.semi.login.IntroActivity;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

import static android.os.Build.VERSION_CODES.M;

public class UpdateBusinessInfoActivity extends AppCompatActivity {

    Context mContext;
    UpdateTask mUpdateTask;
    SelectTask mSelectTask;
    DBManager mDbManager;

    RelativeLayout mBankSelectLayout, mAddressLayout, mAttachLayout, mAttachFirstLayout, mAttachSecondLayout, mCompanyLayout, mCompanyFirstLayout, mCompanySecondLayout;
    TextView mBankSelectText, mAddressText, mLogoutText, mWithdrawalText;
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
        setContentView(R.layout.activity_update_business_info);

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
        mLogoutText = findViewById(R.id.logout_text);
        mWithdrawalText = findViewById(R.id.withdrawal_text);
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

                if(mBankSpinner.getSelectedItemPosition() == 0 || mAccountNumberEdit.getText().toString() == "" || "".equals(mAccountNumberEdit.getText().toString())
                        || mAccountHolderEdit.getText().toString() == "" || "".equals(mAccountHolderEdit.getText().toString())) {
                    Toast.makeText(mContext, "계좌정보를 정확히 입력해주세요.", Toast.LENGTH_LONG).show();
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
                    jsonObject.put("id", mDbManager.selectUserId()); // 아이디
                    jsonObject.put("market_name", mNameEdit.getText().toString()); // 판매자 이름
                    jsonObject.put("company", mCompanyEdit.getText().toString()); // 업체 이름
                    jsonObject.put("address", mAddressText.getText().toString()); // 업체 주소
                    jsonObject.put("phone", mNumberEdit.getText().toString());// 업체 연락처
                    jsonObject.put("number", mRegistEdit.getText().toString());//사업자 등록번호
                    jsonObject.put("number_picture_length",mAttachImgArrayList.size());
                    if(mAttachImgArrayList.size() > 0) {
                        jsonObject.put("number_picture", mAttachImgArrayList.get(0));// 사업자 등록번호 이미지
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

                    Log.d("UpdateBusinessInfoActivity", "mJsonData : " + jsonObject.toString());
                    mUpdateTask = new UpdateTask(jsonObject);
                    mUpdateTask.execute();
//                    mJsonData.put("number_pictrue", mBusinessNumberEdit.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });



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

        mWithdrawalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setMessage("탈퇴 하시겠습니까?");
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




        setInitData();
    }

    private void setInitData() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mDbManager.selectUserId());

            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                connect = network.marketUpdate(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                Toast.makeText(mContext, "업체정보가 저장됐습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(mContext, "계정을 만들 수 없습니다.", Toast.LENGTH_SHORT).show();
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

        SelectTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.marketUpdateSearch(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {

                try {
                    JSONObject jsonObject = network.mMarketArray.getJSONObject(0);

                    mNameEdit.setText(Html.fromHtml(jsonObject.getString("name")).toString());
                    mCompanyEdit.setText(Html.fromHtml(jsonObject.getString("company")).toString());
                    mAddressText.setText(Html.fromHtml(jsonObject.getString("address").replace("\n", "<br>")).toString());
                    mRegistEdit.setText(jsonObject.getString("company_number"));
                    mNumberEdit.setText(jsonObject.getString("phone"));
                    mAccountNumberEdit.setText(jsonObject.getString("account_number"));
                    mAccountHolderEdit.setText(Html.fromHtml(jsonObject.getString("account_holder")).toString());
                    mBankSpinner.setSelection(jsonObject.getInt("bank"));
                    mExplanationEdit.setText(Html.fromHtml(jsonObject.getString("comment").replace("\n", "<br>")).toString());
                    mNoticeEdit.setText(Html.fromHtml(jsonObject.getString("info").replace("\n", "<br>")).toString());
                    mTagEdit.setText(Html.fromHtml(jsonObject.getString("tag").replace("\n", "<br>")).toString());

                    mLat = jsonObject.getDouble("lat")+"";
                    mLon = jsonObject.getDouble("lon")+ "";


                    int service = jsonObject.getInt("service");
                    int[] number =  Util.toNumberFormat(service);

                    for(int i = 0; i < number.length; i++)
                    Log.d("test", "service : " + service + " / i : " + i + " / number : " + number[i]);

                    if(number[1] == 1) {
                        mParkingCheckbox.setChecked(true);
                    }

                    if(number[2] == 1) {
                        mWifiCheckbox.setChecked(true);
                    }

                    if(number[3] == 1) {
                        mParcelsCheckbox.setChecked(true);
                    }

                    if(number[4] == 1) {
                        mAsCheckbox.setChecked(true);
                    }

                    if(number[5] == 1) {
                        mAllDayCheckbox.setChecked(true);
                    }

                    if(number[6] == 1) {
                        mShopCheckbox.setChecked(true);
                    }

                    if(number[7] == 1) {
                        mGuideCheckbox.setChecked(true);
                    }

                    if(number[8] == 1) {
                        mBathroomCheckbox.setChecked(true);
                    }

                    if(number[9] == 1) {
                        mProductCheckbox.setChecked(true);
                    }


                    String numberPath = jsonObject.getString("number_picture");
                    String picture1 = jsonObject.getString("picture1");
                    String picture2 = jsonObject.getString("picture2");
                    String picture3 = jsonObject.getString("picture3");

                    if(numberPath != "null") {
                        mAttachFileImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(numberPath));
                    }


                    if(picture1 != "null") {
                        mCompanyFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(picture1));
                    }
                    if(picture2 != "null") {
                        mCompanySecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(picture2));
                    }
                    if(picture3 != "null") {
                        mCompanyThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(picture3));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(mContext, "계정을 만들 수 없습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }

}

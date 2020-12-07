package artech.com.semi.business;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.utility.BottomNavigationViewHelper;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

public class ProductUpdateActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    SelectTask mSelectTask;
    UpdateTask mUpdateTask;

    TextView mConfirmText;
    Spinner mCategorySpinner, mSaleSpinner;//, mSaleUnitSpinner, mSaleUnitSecondSpinner;
    EditText mNameEdit, mSaleUnitEdit, mSalePriceEdit, mPriceEdit, mAmountEdit, mDetailEdit, mOriginEdit;//, mSaleUnitSecondEdit;
    RadioGroup mFreshnessRadioGroup;
    CheckBox mSeaCheckbox, mFreshWaterCheckbox;
    ImageView mFirstImg, mSecondImg, mThirdImg;
    RelativeLayout mEighthLayout, mRemoveLayout;

    ArrayList<String> mImgArrayList = new ArrayList<>();

    String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_product_update);
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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_product_manage);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                selectGallery();
            }
        });

        mConfirmText = toolbar.findViewById(R.id.product_confirm_text);

        mCategorySpinner = findViewById(R.id.category_spinner);
        mSaleSpinner = findViewById(R.id.sale_spinner);
//        mSaleUnitSpinner = findViewById(R.id.sale_unit_spinner);
//        mSaleUnitSecondSpinner = findViewById(R.id.sale_unit2_spinner);

        mNameEdit = findViewById(R.id.product_name_edit);
        mSaleUnitEdit = findViewById(R.id.sale_unit_edit);
//        mSaleUnitSecondEdit = findViewById(R.id.sale_unit2_edit);
        mPriceEdit = findViewById(R.id.price_edit);
        mSalePriceEdit = findViewById(R.id.sale_price_edit);
        mAmountEdit = findViewById(R.id.inventory_amount_edit);
        mDetailEdit = findViewById(R.id.product_detail_edit);
        mOriginEdit = findViewById(R.id.origin_edit);

        mFreshnessRadioGroup = findViewById(R.id.freshness_radio_group);
        mSeaCheckbox = findViewById(R.id.sea_button);
        mFreshWaterCheckbox = findViewById(R.id.fresh_water_button);

        mFirstImg = findViewById(R.id.first_insert_img);
        mSecondImg = findViewById(R.id.second_insert_img);
        mThirdImg = findViewById(R.id.third_insert_img);

        mEighthLayout = findViewById(R.id.eighth_layout);
        mRemoveLayout = findViewById(R.id.remove_layout);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_product_insert);
//        mCategorySpinner.setAdapter(adapter);


        mSaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0) {
                    mSalePriceEdit.setEnabled(false);
                    if(i == 1) {
                        mSalePriceEdit.setText("10");
                    }else if(i == 2) {
                        mSalePriceEdit.setText("20");
                    }else if(i == 3) {
                        mSalePriceEdit.setText("30");
                    }else if(i == 4) {
                        mSalePriceEdit.setText("40");
                    }else if(i == 5) {
                        mSalePriceEdit.setText("50");
                    }
                }else {
                    mSalePriceEdit.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mConfirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(mNameEdit.getText().toString()) || mNameEdit.getText().toString() == "") {
                    Toast.makeText(mContext, "상품명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if("".equals(mPriceEdit.getText().toString()) || mPriceEdit.getText().toString() == "") {
                    Toast.makeText(mContext, "판매가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*if("".equals(mAmountEdit.getText().toString()) || mAmountEdit.getText().toString() == "") {
                    Toast.makeText(mContext, "재고수량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if("".equals(mSaleUnitEdit.getText().toString()) || mSaleUnitEdit.getText().toString() == "") {
                    Toast.makeText(mContext, "단위를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if("".equals(mSaleUnitSecondEdit.getText().toString()) || mSaleUnitSecondEdit.getText().toString() == "") {
//                    Toast.makeText(mContext, "판매단위2를 입력해주세요. 단위가 없을시 0으로 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }



                if("".equals(mSalePriceEdit.getText().toString()) || mSalePriceEdit.getText().toString() == "") {
                    mSalePriceEdit.setText("0");
                }else {
                    if (Integer.parseInt(mSalePriceEdit.getText().toString()) > 80) {
                        Toast.makeText(mContext, "할인율은 80% 이상일 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

//                if("".equals(mOriginEdit.getText().toString()) || mOriginEdit.getText().toString() == "") {
//                    Toast.makeText(mContext, "원산지를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mId);
                    jsonObject.put("name", mNameEdit.getText().toString());
                    jsonObject.put("content", mDetailEdit.getText().toString());
                    jsonObject.put("origin", mOriginEdit.getText().toString());
                    jsonObject.put("kinds", 0);
                    jsonObject.put("state", 0);
                    jsonObject.put("price", Integer.parseInt(mPriceEdit.getText().toString()));
                    jsonObject.put("sale_price", Integer.parseInt(mSalePriceEdit.getText().toString()));
//                    jsonObject.put("quantity", Integer.parseInt(mAmountEdit.getText().toString()));
                    jsonObject.put("category", mCategorySpinner.getSelectedItemPosition());
                    jsonObject.put("first_unit", mSaleUnitEdit.getText().toString());
//                    jsonObject.put("second_unit", mSaleUnitSecondSpinner.getSelectedItemPosition());
//                    jsonObject.put("first_quantity", Integer.parseInt(mSaleUnitEdit.getText().toString()));
//                    jsonObject.put("second_quantity", Integer.parseInt(mSaleUnitSecondEdit.getText().toString()));
                    if(mFreshnessRadioGroup.getCheckedRadioButtonId() == R.id.high_button) {
                        jsonObject.put("freshness", 2);
                    }else if(mFreshnessRadioGroup.getCheckedRadioButtonId() == R.id.middle_button) {
                        jsonObject.put("freshness", 1);
                    }else if(mFreshnessRadioGroup.getCheckedRadioButtonId() == R.id.low_button) {
                        jsonObject.put("freshness", 0);
                    }
                    jsonObject.put("origin", mOriginEdit.getText().toString());


                    int classification = 0;
                    if(mSeaCheckbox.isChecked() && mFreshWaterCheckbox.isChecked()) {
                        classification = 3;
                    }else if(mSeaCheckbox.isChecked()) {
                        classification = 1;
                    }else if(mFreshWaterCheckbox.isChecked()) {
                        classification = 2;
                    }
                    jsonObject.put("classification", classification);

                    if(mImgArrayList.size() > 0) {
                        jsonObject.put("picture_length", mImgArrayList.size());
                        for(int i = 0; i < mImgArrayList.size(); i++) {
                            jsonObject.put("picture"+ (i+1), mImgArrayList.get(i));
                        }
                    }

                    mUpdateTask = new UpdateTask(jsonObject, 0);
                    mUpdateTask.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mRemoveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View alertView = inflater.inflate(R.layout.popup_product_remove, null);
                builder.setView(alertView);

                RelativeLayout cancelLayout = alertView.findViewById(R.id.cancel_layout);
                RelativeLayout confrimLayout = alertView.findViewById(R.id.confirm_layout);

                TextView detailText = alertView.findViewById(R.id.product_detail_text);
                detailText.setText("현재 판매중인 "+ mNameEdit.getText().toString() + "\n상품을 삭제하시겠습니까?");

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                params.height = 196;
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, 1050);
//                dialog.getWindow().setAttributes(params);

                cancelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                confrimLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", mId);
                            jsonObject.put("use_yn", 0);


                            mUpdateTask = new UpdateTask(jsonObject, 1);
                            mUpdateTask.execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });


        Intent intent = getIntent();
        Log.d("ProductUpdateActivity", "id : " + intent.getStringExtra("product_id"));
        mId = intent.getStringExtra("product_id").toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", mId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSelectTask = new SelectTask(jsonObject);
        mSelectTask.execute();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(mContext, BusinessMainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_product_insert:
                    intent = new Intent(mContext, ProductInsertActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_product_manage:
                    intent = new Intent(mContext, ProductManagementActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                    return true;
//                case R.id.navigation_sale:
//                    intent = new Intent(mContext, SaleManagementActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    finish();
//                    return true;
                case R.id.navigation_question:
                    intent = new Intent(mContext, QuestionManagementActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                    return true;
            }
            return false;
        }
    };



    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject mJsonObject;

        int mFlag;

        UpdateTask(JSONObject jsonObject, int flag) {
            mJsonObject = jsonObject;
            mFlag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                if(mFlag == 0) {
                    connect = network.productUpdate(mJsonObject);
                }else if(mFlag == 1){
                    connect = network.productUseUpdate(mJsonObject);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;

            Log.d("InsertTask", "mAdminTask : " + success);
            if(success) {
//                Intent intent = new Intent(mContext, ProductManagementActivity.class);
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//                startActivity(intent);
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
        JSONObject mJsonObject;

        SelectTask(JSONObject jsonObject) {
            mJsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.productItemSelect(mJsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;

            Log.d("SelectTask", "mAdminTask : " + success);
            if(success) {
                try {
                    JSONArray jsonArray = network.mProductArray;

                    mCategorySpinner.setSelection(jsonArray.getJSONObject(0).getInt("category"));
                    mNameEdit.setText(Html.fromHtml(jsonArray.getJSONObject(0).getString("product_name")));
                    mDetailEdit.setText(Html.fromHtml(jsonArray.getJSONObject(0).getString("product_content")));
                    mOriginEdit.setText(Html.fromHtml(jsonArray.getJSONObject(0).getString("origin")));
                    mSaleUnitEdit.setText(Html.fromHtml(jsonArray.getJSONObject(0).getString("first_unit")));
//                    mSaleUnitSecondEdit.setText(jsonArray.getJSONObject(0).getInt("second_quantity")+"");
//                    mSaleUnitSpinner.setSelection(jsonArray.getJSONObject(0).getInt("first_unit"));
//                    mSaleUnitSecondSpinner.setSelection(jsonArray.getJSONObject(0).getInt("second_unit"));
                    mPriceEdit.setText(jsonArray.getJSONObject(0).getInt("price")+"");
                    mSalePriceEdit.setText(jsonArray.getJSONObject(0).getInt("sale_price")+"");
//                    mAmountEdit.setText(jsonArray.getJSONObject(0).getInt("quantity")+"");

                    int freshness = jsonArray.getJSONObject(0).getInt("freshness");
                    if(freshness == 2) {
                        mFreshnessRadioGroup.check(R.id.high_button);
                    }else if(freshness == 1) {
                        mFreshnessRadioGroup.check(R.id.middle_button);
                    }else if(freshness == 0) {
                        mFreshnessRadioGroup.check(R.id.low_button);
                    }

                    int classification = jsonArray.getJSONObject(0).getInt("classification");

                    if(classification == 1) {
                        mSeaCheckbox.setChecked(true);
                    }else if(classification == 2) {
                        mFreshWaterCheckbox.setChecked(true);
                    }else if(classification == 3) {
                        mSeaCheckbox.setChecked(true);
                        mFreshWaterCheckbox.setChecked(true);
                    }

                    if(jsonArray.getJSONObject(0).getString("picture").length() > 0) {
                        mEighthLayout.setVisibility(View.VISIBLE);
                        mFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(jsonArray.getJSONObject(0).getString("picture")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }

    private final int GALLERY_CODE=1112;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
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
                    break;

                default:
                    break;
            }

            Log.d("onActivityResult", "mImgArrayList : " + mImgArrayList.size());
            if(mImgArrayList.size() > 0) {
                mEighthLayout.setVisibility(View.VISIBLE);
                for(int i = 0; i < mImgArrayList.size(); i++) {
                    Log.d("onActivityResult", "mImgArrayList get " + i +" : " + mImgArrayList.get(i));
                    if(i == 0) {
                        mFirstImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                    }else if(i == 1) {
                        mSecondImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                    }else if(i == 2) {
                        mThirdImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(mImgArrayList.get(i)));
                    }

                }
            }

        }
    }

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    private String sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
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

    private String getRealPathFromURI(Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(mContext, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(mContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;


//        int column_index=0;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if(cursor.moveToFirst()){
//            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        }
//
//        return cursor.getString(column_index);
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}

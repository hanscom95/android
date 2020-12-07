package artech.com.semi.normal;

import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

public class TalkWriteActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    RelativeLayout mNoticeLayout, mCategoryLayout, mFirstCameraLayout, mSecondCameraLayout, mThirdCameraLayout;
    ImageView mFirstImg, mSecondImg, mThirdImg, mCameraImg, mLocImg;
    TextView mInsertText, mNoticeText, mCategoryText;
    EditText mTitleEdit, mLocationEdit, mContentsEdit;

    ArrayList<String> mImgArrayList = new ArrayList<>();

    InsertTask mInsertTask;

    String mId;

    int mNotice = -1;
    int mCategory = -1;

    private final int GALLERY_CODE=1112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_talk_write);
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

        TextView mInsertText = findViewById(R.id.insert_text);


        mNoticeText = findViewById(R.id.notice_first_text);
        mCategoryText = findViewById(R.id.notice_second_text);

        mTitleEdit = findViewById(R.id.title_text);
        mLocationEdit = findViewById(R.id.location_text);
        mContentsEdit = findViewById(R.id.value_text);
        mCameraImg = findViewById(R.id.photo_img);

        mNoticeLayout = (RelativeLayout) findViewById(R.id.first_layout);
        mCategoryLayout = (RelativeLayout) findViewById(R.id.notice_second_layout);

        mInsertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mTitleEdit.getText().toString() == "" || "".equals(mTitleEdit.getText().toString())) {
                    Toast.makeText(mContext, "제목을 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }


                if(mLocationEdit.getText().toString() == "" || "".equals(mLocationEdit.getText().toString())) {
                    Toast.makeText(mContext, "주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }


                if(mContentsEdit.getText().toString() == "" || "".equals(mContentsEdit.getText().toString())) {
                    Toast.makeText(mContext, "상세내역을 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }


                if(mNotice < 0) {
                    Toast.makeText(mContext, " 게시판을 선택해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }


                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", mDbManager.selectUserId());
                    jsonObject.put("title", mTitleEdit.getText().toString());
                    jsonObject.put("location", mLocationEdit.getText().toString());
                    jsonObject.put("contents", mContentsEdit.getText().toString());
                    jsonObject.put("notice", mNotice);
                    jsonObject.put("category", mCategory);


                    if(mImgArrayList.size() > 0) {
                        jsonObject.put("picture_length", mImgArrayList.size());
                        for(int i = 0; i < mImgArrayList.size(); i++) {
                            jsonObject.put("picture"+ (i+1), mImgArrayList.get(i));
                        }
                    }

                    mInsertTask = new InsertTask(jsonObject);
                    mInsertTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View alertView = inflater.inflate(R.layout.popup_talk_write_notice_select, null);
                builder.setView(alertView);


                final AlertDialog dialog = builder.create();


                final RelativeLayout oneLayout = alertView.findViewById(R.id.one_layout);
                final RelativeLayout twoLayout = alertView.findViewById(R.id.two_layout);
                final RelativeLayout threeLayout = alertView.findViewById(R.id.three_layout);

                final RelativeLayout noticeOneLayout = alertView.findViewById(R.id.second_layout); // 동출요청
                final RelativeLayout noticeTwoLayout = alertView.findViewById(R.id.third_layout); // 조행기
                final RelativeLayout noticeThreeLayout = alertView.findViewById(R.id.fourth_layout); // 낚시정보
                final RelativeLayout noticeFourthLayout = alertView.findViewById(R.id.fifth_layout); // 자유 게시판


                final RelativeLayout twoFirstLayout = alertView.findViewById(R.id.two_third_layout); // 바다 조행기
                final RelativeLayout twoSecondLayout = alertView.findViewById(R.id.two_fourth_layout); // 민물 조행기


                final RelativeLayout threeFirstLayout = alertView.findViewById(R.id.three_third_layout); // 조황
                final RelativeLayout threeSecondLayout = alertView.findViewById(R.id.three_fourth_layout); // 포인트
                final RelativeLayout threeThirdLayout = alertView.findViewById(R.id.three_fifth_layout); // 용품
                final RelativeLayout threeFourthLayout = alertView.findViewById(R.id.three_sixth_layout); // 조법
                final RelativeLayout threeFifthLayout= alertView.findViewById(R.id.three_seventh_layout); // 기타

                noticeOneLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 0;
                        mCategory = 0;
                        dialog.dismiss();
                    }
                });

                noticeTwoLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oneLayout.setVisibility(View.GONE);
                        twoLayout.setVisibility(View.VISIBLE);
                    }
                });

                noticeThreeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oneLayout.setVisibility(View.GONE);
                        threeLayout.setVisibility(View.VISIBLE);
                    }
                });

                noticeFourthLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 3;
                        mCategory = 0;
                        dialog.dismiss();
                    }
                });

                twoFirstLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 1;
                        mCategory = 0;
                        dialog.dismiss();
                    }
                });

                twoSecondLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 1;
                        mCategory = 1;
                        dialog.dismiss();
                    }
                });


                threeFirstLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 2;
                        mCategory = 0;
                        dialog.dismiss();
                    }
                });

                threeSecondLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 2;
                        mCategory = 1;
                        dialog.dismiss();
                    }
                });

                threeThirdLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 2;
                        mCategory = 2;
                        dialog.dismiss();
                    }
                });

                threeFourthLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 2;
                        mCategory = 3;
                        dialog.dismiss();
                    }
                });

                threeFourthLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotice = 2;
                        mCategory = 4;
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if(mNotice == 0) {
                            mNoticeText.setText("동출요청");
                            mCategoryLayout.setVisibility(View.GONE);
                        }else if(mNotice == 1) {
                            mNoticeText.setText("조행기");

                            if(mCategory == 0) {
                                mCategoryText.setText("바다조행기");
                            }else if(mCategory == 1) {
                                mCategoryText.setText("민물조행기");
                            }
                            mCategoryLayout.setVisibility(View.VISIBLE);
                        }else if(mNotice == 2) {
                            mNoticeText.setText("낚시정보");

                            if(mCategory == 0) {
                                mCategoryText.setText("조황");
                            }else if(mCategory == 1) {
                                mCategoryText.setText("포인트");
                            }else if(mCategory == 1) {
                                mCategoryText.setText("용품");
                            }else if(mCategory == 1) {
                                mCategoryText.setText("조법");
                            }else if(mCategory == 1) {
                                mCategoryText.setText("기타");
                            }

                            mCategoryLayout.setVisibility(View.VISIBLE);
                        }else if(mNotice == 3) {
                            mNoticeText.setText("자유게시판");
                            mCategoryLayout.setVisibility(View.GONE);
                        }


                    }
                });
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                params.height = 196;
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, 1100);

            }
        });

        mCameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }


                selectGallery(0);
            }
        });

//        Intent intent = getIntent();
//        mId = intent.getStringExtra("id");
    }

    private void selectGallery(int flag) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, flag);
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
            mImgArrayList = new ArrayList<>();

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

        }
    }


    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        InsertTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.talkInsert(jsonObject);
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

                Intent intent = new Intent(mContext, TalkActivity.class);
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

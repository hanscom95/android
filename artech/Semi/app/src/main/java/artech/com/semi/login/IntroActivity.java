package artech.com.semi.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import artech.com.semi.R;
import artech.com.semi.business.BusinessMainActivity;
import artech.com.semi.normal.NormalMainActivity;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

import static android.os.Build.VERSION_CODES.M;

public class IntroActivity extends AppCompatActivity {
    View mContentView;
    Context mContext;
    DBManager mDbManager;

    TimerTask mTimerTask;

    SelectTask mSelectTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);


        mContentView = findViewById(R.id.intro_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(mContext, LoginActivity.class);
//                Intent intent = new Intent(mContext, BusinessMainActivity.class);
//                Intent intent = new Intent(mContext, ProductManagementActivity.class);
//                Intent intent = new Intent(mContext, SaleManagementActivity.class);
//                startActivity(intent);
//                finish();
            }
        });



        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("IntroActivity", "mDbManager.selectId : " + mDbManager.selectId());
                if(mDbManager.selectId() > 0) {
                    if(mDbManager.selectState() == 0) {
                        Intent intent = new Intent(mContext, NormalMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(mContext, BusinessMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    Intent intent = new Intent(mContext, PaymentSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }
//                Intent intent = new Intent(mContext, NormalMainActivity.class);
//                startActivity(intent);
//                finish();
            }
        };



        FirebaseApp.initializeApp(mContext);

//        FirebaseInstanceId.getInstance().
//        FirebaseInstanceId.getInstance().getToken();

//        if (FirebaseInstanceId.getInstance().getToken() != null) {
//            Log.d("IntroActivity", "token = " + FirebaseInstanceId.getInstance().getToken());
//        }else {
//            Log.d("IntroActivity", "token null");
//        }

//        String receipt_dt =  mJsonArray.getJSONObject(i).getString("receipt_dt");
//        try {
//            String receipt_dt =  "2018-09-15 0:45:12";
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            Date receiptDate = format.parse(receipt_dt);
//
//            Date nowDate = new Date();
//            nowDate.setHours(0);
//            nowDate.setMinutes(0);
//            nowDate.setSeconds(0);
//
//            long time = receiptDate.getTime() - nowDate.getTime();
//            long minus = 1000*60*60*24*2;//2day
//            long day = Math.round((time*1000)/(1000*60*60*24)*0.001);
//
//            Log.d("time", "receiptDate : " + receiptDate.toString() + " / nowDate : " + nowDate.toString());
//            Log.d("time", "receiptDate: " + receiptDate.getTime() + " / nowDate: " + nowDate.getTime() +  " / time : " + time + " / minus : " + minus + " / day : " + day );
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        Toast.makeText(mContext, "베타 테스트 버전입니다.", Toast.LENGTH_LONG).show();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", getString(R.string.app_version));

            mSelectTask = new SelectTask(jsonObject);
            mSelectTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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
                    }else {
                        Log.d("IntroActivity", "VERSION.SDK_INT false" );
                    }


                    //최초로 권한을 요청할 때.
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    //        getThumbInfo();
                }
            }else{
                Timer timer = new Timer();
                timer.schedule(mTimerTask, 3000);
                //권한이 있을 때.
                //            Timer timer = new Timer();
                //            timer.schedule(mTimerTask, 3000);
                //       getThumbInfo();
            }

        } else {
            Timer timer = new Timer();
            timer.schedule(mTimerTask, 3000);
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

            for (int i = 0; i < permissions.length; i++) {
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
                }else {
                    finish();
                    return;
                }
            }

            Timer timer = new Timer();
            timer.schedule(mTimerTask, 3000);

        } else {
            Toast.makeText(mContext, "권한설정 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
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

                connect = network.appSelect(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;

            Log.d("IntroActivity", "mAdminTask : " + success);
            if(success) {
                checkPermissionF();

            }else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("업데이트");
                alertDialog.setMessage("마켓에 새로운 버전이 존재합니다.");
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=artech.com.semi.free")));
                    }
                }).show();
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }


}

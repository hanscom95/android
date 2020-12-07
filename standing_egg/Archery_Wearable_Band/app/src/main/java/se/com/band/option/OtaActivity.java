package se.com.band.option;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;
import se.com.band.HelloActivity;
import se.com.band.R;
import se.com.band.dfu.DfuService;
import se.com.band.utility.Preferences;

public class OtaActivity extends AppCompatActivity {

    Context mContext;
    Preferences mPreference;

    ProgressBar mOtaProgress;
    TextView mOtaStatusText;
    Button mOtaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;
        mPreference = Preferences.getInstance(mContext);

        mOtaProgress = (ProgressBar) findViewById(R.id.ota_progress);

        mOtaStatusText = (TextView) findViewById(R.id.ota_status_text);

        mOtaButton = (Button) findViewById(R.id.ota_button);
        mOtaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDfuServiceRunning()) {
                    showUploadCancelDialog();
                    return;
                }



                DfuServiceInitiator starter = new DfuServiceInitiator(mPreference.getUserDeviceAdderess())
                        .setDeviceName(mPreference.getmUserDeviceName())
                        .setKeepBond(false);
//                Uri mFileStreamUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ota_file.zip");
                Uri mFileStreamUri = Uri.parse("file:///mnt/sdcard/Download/ota_file.zip");

                String mFilePath = null;
                starter.setZip(mFileStreamUri, mFilePath);
                starter.start(mContext, DfuService.class);
            }
        });

        otaFileDownload();
    }


    @Override
    protected void onResume() {
        super.onResume();

        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
    }

    private void otaFileDownload() {
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(getApplicationContext(), "BLUETOOTH Admin storage", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);
        }else {
            new FileDownloadTask().execute();
        }
    }

    private class FileDownloadTask extends AsyncTask<String, Integer, Integer> {

        ProgressDialog mDlg;

        @Override
        protected Integer doInBackground(String... params) {
            String address = "http://52.78.81.0/ota/ota_download.php";

            int count = 0;
            try {
                Thread.sleep(100);
                URL url = new URL(address);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ota_file.zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            }catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDlg = new ProgressDialog(mContext);
            mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDlg.setMessage("OTA File Downloading...");
            mDlg.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mDlg.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mDlg.dismiss();
        }
    }


    private void showUploadCancelDialog() {
        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        final Intent pauseAction = new Intent(DfuService.BROADCAST_ACTION);
        pauseAction.putExtra(DfuService.EXTRA_ACTION, DfuService.ACTION_PAUSE);
        manager.sendBroadcast(pauseAction);

    }


    private boolean isDfuServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(DfuService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(final String deviceAddress) {
            mOtaProgress.setIndeterminate(true);
            mOtaStatusText.setText(R.string.dfu_status_connecting);
        }

        @Override
        public void onDfuProcessStarting(final String deviceAddress) {
            mOtaProgress.setIndeterminate(true);
            mOtaStatusText.setText(R.string.dfu_status_starting);
        }

        @Override
        public void onEnablingDfuMode(final String deviceAddress) {
            mOtaProgress.setIndeterminate(true);
            mOtaStatusText.setText(R.string.dfu_status_switching_to_dfu);
        }

        @Override
        public void onFirmwareValidating(final String deviceAddress) {
            mOtaProgress.setIndeterminate(true);
            mOtaStatusText.setText(R.string.dfu_status_validating);
        }

        @Override
        public void onDeviceDisconnecting(final String deviceAddress) {
            mOtaProgress.setIndeterminate(true);
            mOtaStatusText.setText(R.string.dfu_status_disconnecting);
        }

        @Override
        public void onDfuCompleted(final String deviceAddress) {
            mOtaStatusText.setText(R.string.dfu_status_completed);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    onTransferCompleted();

                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);
        }

        @Override
        public void onDfuAborted(final String deviceAddress) {
            mOtaStatusText.setText(R.string.dfu_status_aborted);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    onUploadCanceled();

                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);
        }

        @Override
        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
            mOtaProgress.setIndeterminate(false);
            mOtaProgress.setProgress(percent);
            mOtaStatusText.setText(getString(R.string.dfu_uploading_percentage, percent));
//            if (partsTotal > 1)
//                mTextUploading.setText(getString(R.string.dfu_status_uploading_part, currentPart, partsTotal));
//            else
//                mTextUploading.setText(R.string.dfu_status_uploading);
        }

        @Override
        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
//            showErrorMessage(message);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

            // We have to wait a bit before canceling notification. This is called before DfuService creates the last notification.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);
        }
    };

}

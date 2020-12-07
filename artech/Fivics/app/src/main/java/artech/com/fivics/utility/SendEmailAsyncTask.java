package artech.com.fivics.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import artech.com.fivics.score.ArcheryBoardActivity;

public class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    public Mail m;
    public Context mContext;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;

    public SendEmailAsyncTask(Context context, ProgressBar prograssBar) {
        mContext = context;
        mProgressBar = prograssBar;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (m.send()) {
//                Snackbar.make(mFab, "Email sent.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Toast.makeText(mContext, "Email sent.", Toast.LENGTH_LONG).show();
            } else {
//                Snackbar.make(mFab, "Email failed to send.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

//                Toast.makeText(mContext, "Email failed to send.", Toast.LENGTH_LONG).show();
            }

            return true;
        } catch (AuthenticationFailedException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
            e.printStackTrace();
//            Snackbar.make(mFab, "Authentication failed.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

//            Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_LONG).show();
            return false;
        } catch (MessagingException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
            e.printStackTrace();
//            Snackbar.make(mFab, "Email failed to send.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

//            Toast.makeText(mContext, "Email failed to send.", Toast.LENGTH_LONG).show();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
//            Snackbar.make(mFab, "Unexpected error occured.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//            Toast.makeText(mContext, "Unexpected error occured.", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    @Override
    protected void onPostExecute(final Boolean success) {
        mProgressBar.setVisibility(View.GONE);
        if(success) {
            ArcheryBoardActivity.setVisibleEmailView();
        }else {
            ArcheryBoardActivity.setEmailSendFail();
        }
    }
}

package artech.com.manager.utility;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import artech.com.manager.group.GroupTotalListActivity;
import artech.com.manager.scale.ScaleListActivity;
import artech.com.manager.tournament.Tournament16Activity;
import artech.com.manager.tournament.Tournament8Activity;


public class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    public Mail m;
    public String mActivity;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;

    public SendEmailAsyncTask(String activity, FloatingActionButton fab, ProgressBar prograssBar) {
        mActivity = activity;
        mFab = fab;
        mProgressBar = prograssBar;
    }



    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (m.send()) {
                Snackbar.make(mFab, "Email sent.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(mFab, "Email failed to send.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            return true;
        } catch (AuthenticationFailedException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
            e.printStackTrace();
            Snackbar.make(mFab, "Authentication failed.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        } catch (MessagingException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
            e.printStackTrace();
            Snackbar.make(mFab, "Email failed to send.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mFab, "Unexpected error occured.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
    }


    @Override
    protected void onPostExecute(final Boolean success) {
        mProgressBar.setVisibility(View.GONE);
        if(success) {
            if(mActivity.equals("ScaleListActivity")) {
                ScaleListActivity.setVisibleEmailView();
            }else if(mActivity.equals("Tournament8Activity")) {
                Tournament8Activity.setVisibleEmailView();
            }else if(mActivity.equals("Tournament16Activity")) {
                Tournament16Activity.setVisibleEmailView();
            }else if(mActivity.equals("GroupTotalListActivity")) {
                GroupTotalListActivity.setVisibleEmailView();
            }
        }
    }
}

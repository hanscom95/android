package artech.com.fivics.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import artech.com.fivics.IntroActivity;

/**]rdf
 * Created by moon on 2017-10-23.
 */

public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent(context, IntroActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}

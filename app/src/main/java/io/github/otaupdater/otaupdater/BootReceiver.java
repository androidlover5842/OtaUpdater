package io.github.otaupdater.otaupdater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import io.github.otaupdater.otaupdater.misc.Constants;

/**
 * Created by sumit on 18/12/16.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "UpdateCheckReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, CheckUpdate.class);
        context.startService(startServiceIntent);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean bootCheckCompleted = prefs.getBoolean(Constants.BOOT_CHECK_COMPLETED, false);

        if (!bootCheckCompleted) {
            Log.i(TAG, "Start an on-boot check");
            Intent i = new Intent(context, CheckUpdate.class);
            context.startService(i);
        } else {
            // Nothing to do
            Log.i(TAG, "On-boot update check was already completed.");
            return;
        }
    }
}

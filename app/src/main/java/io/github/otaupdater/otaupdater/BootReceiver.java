package io.github.otaupdater.otaupdater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sumit on 18/12/16.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, CheckUpdate.class);
        context.startService(startServiceIntent);
    }
}

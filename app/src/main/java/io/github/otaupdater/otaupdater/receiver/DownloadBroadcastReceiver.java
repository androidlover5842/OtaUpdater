package io.github.otaupdater.otaupdater.receiver;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import io.github.otaupdater.otaupdater.R;
import io.github.otaupdater.otaupdater.activity.OpenScriptGenerator;

import static io.github.otaupdater.otaupdater.util.Config.Downloading;

/**
 * Created by sumit on 15/1/17.
 */

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            if(Downloading==true) {
                Downloading=false;
                Log.i("DownloadBreceiver", "DownloadFinished");
                showNotification(context);
        }

        }

    }
    private void showNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, OpenScriptGenerator.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_system_update)
                        .setContentTitle("Rom updater")
                        .setContentText("Start updating device");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

}

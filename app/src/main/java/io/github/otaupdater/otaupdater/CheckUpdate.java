package io.github.otaupdater.otaupdater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import io.github.otaupdater.otalibary.AppUpdaterUtils;
import io.github.otaupdater.otalibary.enums.AppUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;

/**
 * Created by sumit on 18/12/16.
 */

public class CheckUpdate extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Context context;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.XML)
                .setUpdateXML("https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/Updater.xml")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(final Update update, Boolean isUpdateAvailable) {
                        Log.d("Found", "Update Found");
                        Log.d("AppUpdater", update.getLatestVersion() + ", " + update.getUrlToDownload() + ", " + Boolean.toString(isUpdateAvailable));
                        if(isUpdateAvailable==true)
                        {
                            Intent notificationIntent = new Intent(context, MainActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(context,
                                    0, notificationIntent,
                                    PendingIntent.FLAG_CANCEL_CURRENT);

                            NotificationManager nm = (NotificationManager) context
                                    .getSystemService(Context.NOTIFICATION_SERVICE);

                            Resources res = context.getResources();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                            builder.setContentIntent(contentIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                                    .setTicker("lol")
                                    .setWhen(System.currentTimeMillis())
                                    .setAutoCancel(true)
                                    .setContentTitle("Message")
                                    .setContentText("lol");
                            Notification n = builder.getNotification();

                            n.defaults |= Notification.DEFAULT_ALL;
                            nm.notify(0, n);

                            Log.d("Found", String.valueOf(update.getUrlToDownload()));
                        }

                    }
                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater", "Something went wrong");
                    }

                });
        appUpdaterUtils.start();

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}

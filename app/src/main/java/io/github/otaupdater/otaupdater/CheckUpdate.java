package io.github.otaupdater.otaupdater;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
    private Intent StartMainActivity;
    private PendingIntent contentIntent;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        StartMainActivity = new Intent(this, MainActivity.class);
        contentIntent = PendingIntent.getActivity(this, 0, StartMainActivity,
                PendingIntent.FLAG_ONE_SHOT);

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
                            Notification.Builder builder = new Notification.Builder(CheckUpdate.this)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setWhen(System.currentTimeMillis())
                                    .setTicker("")
                                    .setContentTitle("Update Found")
                                    .setContentText("Click to download update")
                                    .setContentIntent(contentIntent)
                                    .setAutoCancel(true);
                            builder.build();
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

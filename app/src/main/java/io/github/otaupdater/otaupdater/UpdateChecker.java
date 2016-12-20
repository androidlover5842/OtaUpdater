package io.github.otaupdater.otaupdater;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import io.github.otaupdater.otalibary.RomUpdaterUtils;
import io.github.otaupdater.otalibary.enums.RomUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;

import static io.github.otaupdater.otaupdater.Config.ShowToast;
import static io.github.otaupdater.otaupdater.Config.Showlog;
import static io.github.otaupdater.otaupdater.Config.UpdaterUri;

/**
 * Created by sumit on 20/12/16.
 */

public class UpdateChecker extends Service {
    private String Tag="UpdateChecker";
    private NotificationCompat.Builder mBuilder;

    public IBinder onBind(Intent intent) {
        return null;
    }
    @Nullable
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Tag,"Started");
        RomUpdaterUtils romUpdaterUtils = new RomUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.XML)
                .setUpdateXML(UpdaterUri())
                .withListener(new RomUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(final Update update, Boolean isUpdateAvailable) {
                        if(Showlog().equals(true));
                        {
                            Log.d("Found", "Update Found");
                            Log.d("RomUpdater", update.getLatestVersion() + ", " + update.getUrlToDownload() + ", " + Boolean.toString(isUpdateAvailable));
                        }
                        if(isUpdateAvailable==true)
                        {
                            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(UpdateChecker.this)
                                    .setSmallIcon(R.drawable.ic_system_update_white_24dp)
                                    .setContentTitle("Rom Updater")
                                    .setContentText("Found Rom Update")
                                    .setContentInfo("non")
                                    .setAutoCancel(true);
                            Intent intent = new Intent(UpdateChecker.this, MainActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(UpdateChecker.this,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
                            mBuilder.setContentIntent(pi);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(0, mBuilder.build());
                            if(Showlog().equals(true));
                            {
                                Log.d("Found", String.valueOf(update.getUrlToDownload()));
                            }
                        }

                    }
                    @Override
                    public void onFailed(RomUpdaterError error) {
                        if(Showlog().equals(true));
                        {
                            Log.d("RomUpdater", "Something went wrong");
                        }
                    }

                });
        romUpdaterUtils.start();
        if(ShowToast==true);
        {
            Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }

}

package io.github.otaupdater.otaupdater;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.github.otaupdater.otalibary.AppUpdaterUtils;
import io.github.otaupdater.otalibary.enums.AppUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;

public class MainActivity extends AppCompatActivity {
    private Intent StartMainActivity;
    private PendingIntent contentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                            Notification.Builder builder = new Notification.Builder(MainActivity.this)
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

    }
}

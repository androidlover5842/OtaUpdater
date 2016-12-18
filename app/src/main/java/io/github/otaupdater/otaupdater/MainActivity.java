package io.github.otaupdater.otaupdater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.github.otaupdater.otalibary.AppUpdaterUtils;
import io.github.otaupdater.otalibary.enums.AppUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.XML)
                .setUpdateXML("https://raw.githubusercontent.com/Arubadel/Arubadel/master/Updater.xml")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(final Update update, Boolean isUpdateAvailable) {
                        Log.d("Found", "Update Found");
                        if(isUpdateAvailable==true)
                        {
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

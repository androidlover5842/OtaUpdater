package io.github.otaupdater.otaupdater;

import android.Manifest;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;

import io.github.otaupdater.otalibary.RomUpdaterUtils;
import io.github.otaupdater.otalibary.enums.RomUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;

;import static io.github.otaupdater.otaupdater.Config.UpdaterUri;

public class MainActivity extends AppCompatActivity {
    private PanterDialog UpdaterDialog;
    private DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        RomUpdaterUtils romUpdaterUtils = new RomUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.XML)
                .setUpdateXML(UpdaterUri())
                .withListener(new RomUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(final Update update, Boolean isUpdateAvailable) {
                        Log.d("Found", "Update Found");
                        Log.d("RomUpdater", update.getLatestVersion() + ", " + update.getUrlToDownload() + ", " + Boolean.toString(isUpdateAvailable));
                        if(isUpdateAvailable==true)
                        {
                            UpdaterDialog = new PanterDialog(MainActivity.this);
                            UpdaterDialog= new PanterDialog(MainActivity.this);
                            UpdaterDialog.setTitle("Update Found")
                                    .setHeaderBackground(R.color.colorPrimaryDark)
                                    .setMessage("Changelog :- \n\n"+update.getReleaseNotes())
                                    .setPositive("Download",new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Uri uri = Uri.parse(String.valueOf(update.getUrlToDownload()));
                                            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                            DownloadManager.Request request = new DownloadManager.Request(uri);
                                            String  fileName = uri.getLastPathSegment();
                                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

                                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                            Long reference = downloadManager.enqueue(request);
                                            UpdaterDialog.dismiss();

                                        }
                                    })
                                    .setNegative("DISMISS")
                                    .isCancelable(false)
                                    .withAnimation(Animation.SIDE)
                                    .show();                            Log.d("Found", String.valueOf(update.getUrlToDownload()));
                        }

                    }
                    @Override
                    public void onFailed(RomUpdaterError error) {
                        Log.d("RomUpdater", "Something went wrong");
                    }

                });
        romUpdaterUtils.start();

    }
}

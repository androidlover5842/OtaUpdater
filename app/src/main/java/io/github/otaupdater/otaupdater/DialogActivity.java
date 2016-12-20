package io.github.otaupdater.otaupdater;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;

import cn.refactor.lib.colordialog.PromptDialog;
import io.github.otaupdater.otalibary.RomUpdaterUtils;
import io.github.otaupdater.otalibary.enums.RomUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;

import static io.github.otaupdater.otaupdater.Config.Showlog;
import static io.github.otaupdater.otaupdater.Config.UpdaterUri;

public class DialogActivity extends Activity {
    private PanterDialog UpdaterDialog;
    private DownloadManager downloadManager;
    private ProgressBar mProgressBar;
    private PromptDialog mNoUpdate;
    private String Tag="RomUpdater";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        UpdaterDialog= new PanterDialog(DialogActivity.this);
        mNoUpdate = new PromptDialog(this);
        ActivityCompat.requestPermissions(DialogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        RomUpdaterUtils romUpdaterUtils = new RomUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.XML)
                .setUpdateXML(UpdaterUri())
                .withListener(new RomUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(final Update update, Boolean isUpdateAvailable) {
                        if(Showlog().equals(true));
                        {
                        Log.d(Tag, "Update Found");
                        Log.d(Tag, update.getLatestVersion() + ", " + update.getUrlToDownload() + ", " + Boolean.toString(isUpdateAvailable));
                    }
                        if(isUpdateAvailable==false){
                            mProgressBar.setVisibility(View.GONE);
                            if(Showlog().equals(true));
                            {
                                Log.i(Tag, "No update found "+String.valueOf(isUpdateAvailable));
                            }
                            mNoUpdate.setTitle("No Updates Found");
                            mNoUpdate.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                                    .setAnimationEnable(true)
                                    .setTitleText("No update found")
                                    .setContentText("Try again later")
                                    .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            mNoUpdate.dismiss();
                                            finish();
                                        }
                                    }).show();
                        }

                        if(isUpdateAvailable==true)
                        {
                            mProgressBar.setVisibility(View.GONE);
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
                                    .setNegative("DISMISS", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                            UpdaterDialog.dismiss();
                                        }
                                    })
                                    .isCancelable(false)
                                    .withAnimation(Animation.SIDE)
                                    .show();
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

    }
}

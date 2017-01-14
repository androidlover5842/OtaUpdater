package io.github.otaupdater.otaupdater.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;

import cn.refactor.lib.colordialog.PromptDialog;
import io.github.otaupdater.otalibary.RomUpdaterUtils;
import io.github.otaupdater.otalibary.enums.RomUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;
import io.github.otaupdater.otaupdater.R;

import static io.github.otaupdater.otaupdater.util.Config.DownloadFileName;
import static io.github.otaupdater.otaupdater.util.Config.Downloader;
import static io.github.otaupdater.otaupdater.util.Config.PermissionRequest;
import static io.github.otaupdater.otaupdater.util.Config.Showlog;
import static io.github.otaupdater.otaupdater.util.Config.UpdaterUri;
import static io.github.otaupdater.otaupdater.util.Config.isOnline;
import static io.github.otaupdater.otaupdater.util.Config.uri;
import static java.lang.String.valueOf;

public class DialogActivity extends Activity {
    private PanterDialog UpdaterDialog;
    private ProgressBar mProgressBar;
    private PromptDialog mNoUpdate;
    private String Tag="RomUpdater",Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        UpdaterDialog= new PanterDialog(DialogActivity.this);
        PermissionRequest(this);
        mNoUpdate = new PromptDialog(this);
        ActivityCompat.requestPermissions(DialogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        if(isOnline(this)) {
            if(!isVersionValid(UpdaterUri())){
                Url="https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/Updater.xml";
            }else {
                Url=UpdaterUri();
            }
            RomUpdaterUtils romUpdaterUtils = new RomUpdaterUtils(this)
                    .setUpdateFrom(UpdateFrom.XML)
                    .setUpdateXML(Url)
                    .withListener(new RomUpdaterUtils.UpdateListener() {
                        @Override
                        public void onSuccess(final Update update, Boolean isUpdateAvailable) {
                            if (Showlog().equals(true)) ;
                            {
                                Log.d(Tag, "Update Found");
                                Log.d(Tag, update.getLatestVersion() + ", " + update.getUrlToDownload() + ", " + Boolean.toString(isUpdateAvailable));
                            }
                            if (isUpdateAvailable == false) {
                                mProgressBar.setVisibility(View.GONE);
                                if (Showlog().equals(true)) ;
                                {
                                    Log.i(Tag, "No update found " + valueOf(isUpdateAvailable));
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

                            if (isUpdateAvailable == true) {
                                mProgressBar.setVisibility(View.GONE);
                                UpdaterDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                                            finish();
                                            UpdaterDialog.dismiss();
                                        }

                                        return true;
                                    }
                                });
                                UpdaterDialog.setTitle("Update Found")
                                        .setHeaderBackground(R.color.colorPrimaryDark)
                                        .setMessage("Changelog :- \n\n" + update.getReleaseNotes())
                                        .setPositive("Download", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                uri = Uri.parse(valueOf(update.getUrlToDownload()));
                                                DownloadFileName=uri.getLastPathSegment();
                                                Downloader(DialogActivity.this);
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
                                        .withAnimation(Animation.SIDE);

                                try {
                                    UpdaterDialog.show();
                                }catch (WindowManager.BadTokenException Bt){

                                }
                                if (Showlog().equals(true)) ;
                                {
                                    Log.d("Found", valueOf(update.getUrlToDownload()));
                                }

                            }
                        }

                        @Override
                        public void onFailed(RomUpdaterError error) {
                            if (Showlog().equals(true)) ;
                            {
                                Log.d("RomUpdater", "Something went wrong");
                            }
                        }

                    });
            romUpdaterUtils.start();
        } else
        {
            mProgressBar.setVisibility(View.GONE);
            mNoUpdate.setTitle("No network found");
            mNoUpdate.setCanceledOnTouchOutside(false);

            mNoUpdate.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                    .setAnimationEnable(true)
                    .setTitleText("No network found")
                    .setContentText("Please connect to network")
                    .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog dialog) {
                            mNoUpdate.dismiss();
                            finish();
                        }
                    })
                    .show();
            mNoUpdate.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        finish();
                        UpdaterDialog.dismiss();
                    }

                    return true;
                }
            });

        }

    }
    private boolean isVersionValid(String version) {
        return version.length() > 3;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( UpdaterDialog!=null && UpdaterDialog.isShowing() ){
            UpdaterDialog.cancel();
        }
        if(mNoUpdate!=null&&mNoUpdate.isShowing()){
            mNoUpdate.cancel();
        }
    }

}

package io.github.otaupdater.otaupdater.util;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;

import io.github.otaupdater.otalibary.util.ShellExecuter;
import io.github.otaupdater.otaupdater.BuildConfig;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by sumit on 20/12/16.
 */

public class Config {
    public static boolean Downloading;
    public static Uri uri;
    public static String DownloadFileName;
    public static String Tag="RomUpdaterConfig";
    public static DownloadManager downloadManager;
    public static SharedPreferences settings;
    public static SharedPreferences.Editor editor;
    public static String type= BuildConfig.BUILD_TYPE;
    public static  String debug="debug";

    public static String URL_OLD_RELEASES(){
        String output=ShellExecuter.runAsRoot("getprop ro.updater.oldrelease.url");
        return output;
    }
    public static String UpdaterUri(){
        String Output=ShellExecuter.runAsRoot("getprop ro.updater.uri");
        if(Output==null)
        {
            Log.d("RomUpdater","Try to add Custom Url in Config.java");
            Output="";
        }
        return Output;
    }
    public static boolean isOnline(Context c) {
        ConnectivityManager conMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Log.i(Tag,"No Internet connection");
            return false;
        }
        return true;
    }

    public static void Downloader(Context c){
        downloadManager = (DownloadManager) c.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, DownloadFileName);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        Downloading=true;

    }
    static String getRomVersion() {
        String version;
        version=ShellExecuter.runAsRoot("getprop ro.rom.version");
        return version;
    }

    public static String getRomInstalledVersion() {
        String version;
        if(!isVersionValid(getRomVersion()))
        {
            version="20161220";
            Log.e(Tag,"no version found in build.prop using default "+ version  );
        }
        else {
            version=getRomVersion();
        }
        return version;
    }
    public static boolean isVersionValid(String version) {
        return version.length() > 2;
    }
    public static void PermissionRequest(final Activity app){
        new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        // given permissions are granted
                    }
                })
                .whenPermissionsRefused(new PermissionsRefusedListener() {
                    @Override
                    public void onPermissionsRefused(String[] permissions) {
                        app.finish();
                    }
                })
                .execute(app);
    }

        public static String getPreferences(Context context,String Name){
                String o;
                settings = context.getSharedPreferences(Name, 0); // 0 - for private mode
                o=settings.getString(Name,null);

                        return o;
            }
        public static void PutStringPreferences(Context context,String Name,String Function){
                settings = context.getSharedPreferences(Name, 0); // 1 - for public mode
                editor = settings.edit();
                editor.putString(Name, Function);
                editor.commit();

                    }
        public static void updateFragment(FragmentActivity fragmentActivity, Fragment fragment,int id)
        {
                FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
                        ft.replace(id, fragment);
                ft.commit();
            }


}

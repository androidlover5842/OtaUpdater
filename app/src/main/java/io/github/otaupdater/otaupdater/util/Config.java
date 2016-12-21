package io.github.otaupdater.otaupdater.util;

import android.util.Log;

import io.github.otaupdater.otalibary.util.ShellExecuter;

/**
 * Created by sumit on 20/12/16.
 */

public class Config {
    public static boolean ShowLog;
    public static String URL_OLD_RELEASES(){
        ShellExecuter.command="getprop ro.updater.oldrelease.url";
        String output=ShellExecuter.runAsRoot();
        return output;
    }
    public static String Showlog(){
        ShellExecuter.command="getprop ro.otaupdate.enable_log";
        String output=ShellExecuter.runAsRoot();
        if(output.equals(false))
        {
            ShowLog=false;
        }else {
            ShowLog=true;
        }
        if(output==null)
        {
            ShowLog=true;
        }
        return output;
    }
    public static boolean ShowToast;
    public static String ShowToast(){
        String output;
        ShellExecuter.command="getprop ro.otaupdate.enable_toast";
        output=ShellExecuter.runAsRoot();
        if(output.equals(false))
        {
            ShowToast=false;
        }else {
            ShowToast=true;
        }
        if(output==null)
        {
            ShowToast=true;
        }
        return output;
    }
    public static String UpdaterUri(){
        ShellExecuter.command="getprop ro.updater.uri";
        String Output=ShellExecuter.runAsRoot();
        if(Output==null)
        {
            Log.d("RomUpdater","Try to add Custom Url in Config.java");
            Output="";
        }
        return Output;
    }
}

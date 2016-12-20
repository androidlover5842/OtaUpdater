package io.github.otaupdater.otaupdater;

import android.util.Log;

import io.github.otaupdater.otalibary.util.ShellExecuter;

/**
 * Created by sumit on 20/12/16.
 */

public class Config {
    public static boolean ShowLog;
    public static String Showlog(){
        ShellExecuter.command="getprop ro.otaupdate.enablelog";
        String output=ShellExecuter.runAsRoot();
        if(output==null)
        {
            ShowLog=true;
        }else {
            ShowLog=false;
        }
        return output;
    }
    public static boolean ShowToast=true;
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

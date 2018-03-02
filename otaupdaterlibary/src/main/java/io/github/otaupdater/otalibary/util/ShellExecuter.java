package io.github.otaupdater.otalibary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by sumit on 5/11/16.
 */

public class ShellExecuter {

    static {
        System.loadLibrary("OtaUpdater");
    }
    private static native String RunCommand(String command);

    public static String runAsRoot(String command){
        String co=RunCommand("su -c \""+command+"\"");
        return co;
    }

}

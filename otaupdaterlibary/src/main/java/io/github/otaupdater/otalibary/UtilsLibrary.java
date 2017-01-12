package io.github.otaupdater.otalibary;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import io.github.otaupdater.otalibary.enums.Duration;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;
import io.github.otaupdater.otalibary.objects.Version;
import io.github.otaupdater.otalibary.util.ShellExecuter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class UtilsLibrary {

    private static String TAG="RomUpdater";

    static String getRomName(Context context) {
        return context.getString(context.getApplicationInfo().labelRes);
    }

    static String getRomPackageName(Context context) {
        return context.getPackageName();
    }

    static String getRomVersion() {
        String version;
        version=ShellExecuter.runAsRoot("getprop ro.rom.version");
        return version;
    }

    static String getRomInstalledVersion(){
        String version;
        if(!isVersionValid(getRomVersion()))
        {
            version="20161220";
            Log.e(TAG,"no version found in build.prop using default "+ version  );
        }
        else {
            version=getRomVersion();
        }
        return version;
    }
    private static boolean isVersionValid(String version) {
        return version.length() > 2;
    }
    static Boolean isUpdateAvailable(String installedVersion, String latestVersion) {
        Boolean res = false;

        if (!installedVersion.equals("0.0.0.0") && !latestVersion.equals("0.0.0.0")) {
            Version installed = new Version(installedVersion);
            Version latest = new Version(latestVersion);
            res = installed.compareTo(latest) < 0;
        }

        return res;
    }

    static Boolean isStringAVersion(String version) {
        return version.matches(".*\\d+.*");
    }

    static Boolean isStringAnUrl(String s) {
        Boolean res = false;
        try {
            new URL(s);
            res = true;
        } catch (MalformedURLException ignored) {}

        return res;
    }

    static Boolean getDurationEnumToBoolean(Duration duration) {
        Boolean res = false;

        switch (duration) {
            case INDEFINITE:
                res = true;
                break;
        }

        return res;
    }

    static URL getUpdateURL(Context context, UpdateFrom updateFrom) {
        String res;

        switch (updateFrom) {
            default:
                res = String.format(getRomPackageName(context), Locale.getDefault().getLanguage());
                break;
        }

        try {
            return new URL(res);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    static Update getLatestRomVersionHttp(Context context, UpdateFrom updateFrom) {
        Boolean isAvailable = false;
        String source = "";
        OkHttpClient client = new OkHttpClient();
        URL url = getUpdateURL(context, updateFrom);
        Request request = new Request.Builder()
                .url(url)
                .build();
        ResponseBody body = null;

        try {
            Response response = client.newCall(request).execute();
            body = response.body();
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream(), "UTF-8"));
            StringBuilder str = new StringBuilder();

            if (str.length() == 0) {
                Log.e("RomUpdater", "Cannot retrieve latest version. Is it configured properly?");
            }

            response.body().close();
            source = str.toString();
        } catch (FileNotFoundException e) {
            Log.e("RomUpdater", "Rom wasn't found in the provided source. Is it published?");
        } catch (IOException ignore) {

        } finally {
            if (body != null) {
                body.close();
            }
        }

        final String version = getVersion(updateFrom, isAvailable, source);
        final String recentChanges = getRecentChanges(updateFrom, isAvailable, source);
        final URL updateUrl = getUpdateURL(context, updateFrom);

        return new Update(version, recentChanges, updateUrl);
    }

    private static String getVersion(UpdateFrom updateFrom, Boolean isAvailable, String source) {
        String version = "0.0.0.0";
        if (isAvailable) {
            switch (updateFrom) {
                default:
                        version = null;
                    break;
            }
        }
        return version;
    }

    private static String getRecentChanges(UpdateFrom updateFrom, Boolean isAvailable, String source) {
        String recentChanges = "";
        if (isAvailable) {

            recentChanges = null;
        }
        return recentChanges;
    }

    static Update getLatestRomVersion(UpdateFrom updateFrom, String url) {
        if (updateFrom == UpdateFrom.XML){
            RssParser parser = new RssParser(url);
            return parser.parse();
        } else {
            return new JSONParser(url).parse();
        }
    }


    static Intent intentToUpdate(Context context, UpdateFrom updateFrom, URL url) {
        Intent intent;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
        return intent;
    }

    static void goToUpdate(Context context, UpdateFrom updateFrom, URL url) {
        Intent intent = intentToUpdate(context, updateFrom, url);

            context.startActivity(intent);

    }

    static Boolean isAbleToShow(Integer successfulChecks, Integer showEvery) {
        return successfulChecks % showEvery == 0;
    }

    static Boolean isNetworkAvailable(Context context) {
        Boolean res = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null) {
                res = networkInfo.isConnected();
            }
        }

        return res;
    }

}

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
import io.github.otaupdater.otalibary.objects.GitHub;
import io.github.otaupdater.otalibary.objects.Update;
import io.github.otaupdater.otalibary.objects.Version;
import io.github.otaupdater.otalibary.util.ShellExecuter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class UtilsLibrary {

    static String getAppName(Context context) {
        return context.getString(context.getApplicationInfo().labelRes);
    }

    static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    static String getAppInstalledVersion() {
        String version;
        ShellExecuter.command="getprop ro.build.date.utc";
        version=ShellExecuter.runAsRoot();
        return version;
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

    static URL getUpdateURL(Context context, UpdateFrom updateFrom, GitHub gitHub) {
        String res;

        switch (updateFrom) {
            default:
                res = String.format(Config.PLAY_STORE_URL, getAppPackageName(context), Locale.getDefault().getLanguage());
                break;
        }

        try {
            return new URL(res);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    static Update getLatestAppVersionHttp(Context context, UpdateFrom updateFrom, GitHub gitHub) {
        Boolean isAvailable = false;
        String source = "";
        OkHttpClient client = new OkHttpClient();
        URL url = getUpdateURL(context, updateFrom, gitHub);
        Request request = new Request.Builder()
                .url(url)
                .build();
        ResponseBody body = null;

        try {
            Response response = client.newCall(request).execute();
            body = response.body();
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream(), "UTF-8"));
            StringBuilder str = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                switch (updateFrom) {
                    default:
                        if (line.contains(Config.PLAY_STORE_TAG_RELEASE)) {
                            str.append(line);
                            isAvailable = true;
                        }
                        break;

                }
            }

            if (str.length() == 0) {
                Log.e("RomUpdater", "Cannot retrieve latest version. Is it configured properly?");
            }

            response.body().close();
            source = str.toString();
        } catch (FileNotFoundException e) {
            Log.e("RomUpdater", "App wasn't found in the provided source. Is it published?");
        } catch (IOException ignore) {

        } finally {
            if (body != null) {
                body.close();
            }
        }

        final String version = getVersion(updateFrom, isAvailable, source);
        final String recentChanges = getRecentChanges(updateFrom, isAvailable, source);
        final URL updateUrl = getUpdateURL(context, updateFrom, gitHub);

        return new Update(version, recentChanges, updateUrl);
    }

    private static String getVersion(UpdateFrom updateFrom, Boolean isAvailable, String source) {
        String version = "0.0.0.0";
        if (isAvailable) {
            switch (updateFrom) {
                default:
                    String[] splitPlayStore = source.split(Config.PLAY_STORE_TAG_RELEASE);
                    if (splitPlayStore.length > 1) {
                        splitPlayStore = splitPlayStore[1].split("(<)");
                        version = splitPlayStore[0].trim();
                    }
                    break;
            }
        }
        return version;
    }

    private static String getRecentChanges(UpdateFrom updateFrom, Boolean isAvailable, String source) {
        String recentChanges = "";
        if (isAvailable) {
            switch (updateFrom) {
                default:
                    String[] splitPlayStore = source.split(Config.PLAY_STORE_TAG_CHANGES);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < splitPlayStore.length; i++) {
                        sb.append(splitPlayStore[i].split("(<)")[0]).append("\n");
                    }
                    recentChanges = sb.toString();
                    break;
            }
        }
        return recentChanges;
    }

    static Update getLatestAppVersion(UpdateFrom updateFrom, String url) {
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

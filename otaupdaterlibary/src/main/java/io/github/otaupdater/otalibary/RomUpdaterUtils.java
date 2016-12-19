package io.github.otaupdater.otalibary;

import android.content.Context;
import android.support.annotation.NonNull;

import io.github.otaupdater.otalibary.enums.RomUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;


public class RomUpdaterUtils {
    private Context context;
    private UpdateListener updateListener;
    private RomUpdaterListener romUpdaterListener;
    private UpdateFrom updateFrom;
    private String xmlOrJSONUrl;
    private UtilsAsync.LatestRomVersion latestRomVersion;

    public interface UpdateListener {
        /**
         * onSuccess method called after it is successful
         * onFailed method called if it can't retrieve the latest version
         *
         * @param update            object with the latest update information: version and url to download
         * @param isUpdateAvailable compare installed version with the latest one
         */
        void onSuccess(Update update, Boolean isUpdateAvailable);

        void onFailed(RomUpdaterError error);
    }

    @Deprecated
    public interface RomUpdaterListener {
        /**
         * onSuccess method called after it is successful
         * onFailed method called if it can't retrieve the latest version
         *
         * @param latestVersion     available in the provided source
         * @param isUpdateAvailable compare installed version with the latest one
         */
        void onSuccess(String latestVersion, Boolean isUpdateAvailable);

        void onFailed(RomUpdaterError error);
    }

    public RomUpdaterUtils(Context context) {
        this.context = context;
    }

    /**
     * Set the source where the latest update can be found. Default: GOOGLE_PLAY.
     *
     * @param updateFrom source where the latest update is uploaded. If GITHUB is selected, .setGitHubAndRepo method is required.
     * @return this
     * @see <a href="https://github.com/grace5921/OtaUpdater/wiki">Additional documentation</a>
     */
    public RomUpdaterUtils setUpdateFrom(UpdateFrom updateFrom) {
        this.updateFrom = updateFrom;
        return this;
    }

    /**
     * Set the user and repo where the releases are uploaded. You must upload your updates as a release in order to work properly tagging them as vX.X.X or X.X.X.
     *
     * @param user GitHub user
     * @param repo GitHub repository
     * @return this
     */
    public RomUpdaterUtils setGitHubUserAndRepo(String user, String repo) {
        return this;
    }

    /**
     * Set the url to the xml with the latest version info.
     *
     * @param xmlUrl file
     * @return this
     */
    public RomUpdaterUtils setUpdateXML(@NonNull String xmlUrl) {
        this.xmlOrJSONUrl = xmlUrl;
        return this;
    }

    /**
     * Set the url to the xml with the latest version info.
     *
     * @param jsonUrl file
     * @return this
     */
    public RomUpdaterUtils setUpdateJSON(@NonNull String jsonUrl) {
        this.xmlOrJSONUrl = jsonUrl;
        return this;
    }


    /**
     * Method to set the AppUpdaterListener for the RomUpdaterUtils actions
     *
     * @param RomUpdaterListener the listener to be notified
     * @return this
     * @deprecated
     */
    public RomUpdaterUtils withListener(RomUpdaterListener RomUpdaterListener) {
        this.romUpdaterListener = RomUpdaterListener;
        return this;
    }

    /**
     * Method to set the UpdateListener for the RomUpdaterUtils actions
     *
     * @param updateListener the listener to be notified
     * @return this
     */
    public RomUpdaterUtils withListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
        return this;
    }

    /**
     * Execute RomUpdaterUtils in background.
     */
    public void start() {
        latestRomVersion = new UtilsAsync.LatestRomVersion(context, true, updateFrom, xmlOrJSONUrl, new RomUpdater.LibraryListener() {
            @Override
            public void onSuccess(Update update) {
                if (updateListener != null) {
                    updateListener.onSuccess(update, UtilsLibrary.isUpdateAvailable(UtilsLibrary.getRomInstalledVersion(), update.getLatestVersion()));
                } else if (romUpdaterListener != null) {
                    romUpdaterListener.onSuccess(update.getLatestVersion(), UtilsLibrary.isUpdateAvailable(UtilsLibrary.getRomInstalledVersion(), update.getLatestVersion()));
                } else {
                    throw new RuntimeException("You must provide a listener for the RomUpdaterUtils");
                }
            }

            @Override
            public void onFailed(RomUpdaterError error) {
                if (updateListener != null) {
                    updateListener.onFailed(error);
                } else if (romUpdaterListener != null) {
                    romUpdaterListener.onFailed(error);
                } else {
                    throw new RuntimeException("You must provide a listener for the RomUpdaterUtils");
                }
            }
        });

        latestRomVersion.execute();
    }

    /**
     * Stops the execution of RomUpdater.
     */
    public void stop() {
        if (latestRomVersion != null && !latestRomVersion.isCancelled()) {
            latestRomVersion.cancel(true);
        }
    }
}

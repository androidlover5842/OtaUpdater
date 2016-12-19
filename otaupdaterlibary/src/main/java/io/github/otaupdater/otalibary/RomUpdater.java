package io.github.otaupdater.otalibary;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.github.javiersantos.appupdater.R;

import io.github.otaupdater.otalibary.enums.RomUpdaterError;
import io.github.otaupdater.otalibary.enums.Display;
import io.github.otaupdater.otalibary.enums.Duration;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.GitHub;
import io.github.otaupdater.otalibary.objects.Update;

public class RomUpdater implements IRomUpdater {
    private Context context;
    private LibraryPreferences libraryPreferences;
    private Display display;
    private UpdateFrom updateFrom;
    private Duration duration;
    private GitHub gitHub;
    private String xmlOrJsonUrl;
    private Integer showEvery;
    private Boolean showAppUpdated;
    private String titleUpdate, descriptionUpdate, btnDismiss, btnUpdate, btnDisable; // Update available
    private String titleNoUpdate, descriptionNoUpdate; // Update not available
    private int iconResId;
    private UtilsAsync.LatestAppVersion latestAppVersion;

    private AlertDialog alertDialog;
    private Snackbar snackbar;

    public RomUpdater(Context context) {
        this.context = context;
        this.libraryPreferences = new LibraryPreferences(context);
        this.display = Display.DIALOG;
        this.duration = Duration.NORMAL;
        this.showEvery = 1;
        this.showAppUpdated = false;
        this.iconResId = R.drawable.ic_stat_name;

        // Dialog
        this.titleUpdate = context.getResources().getString(R.string.appupdater_update_available);
        this.titleNoUpdate = context.getResources().getString(R.string.appupdater_update_not_available);
        this.btnUpdate = context.getResources().getString(R.string.appupdater_btn_update);
        this.btnDismiss = context.getResources().getString(R.string.appupdater_btn_dismiss);
        this.btnDisable = context.getResources().getString(R.string.appupdater_btn_disable);
    }

    @Override
    public RomUpdater setDisplay(Display display) {
        this.display = display;
        return this;
    }

    @Override
    public RomUpdater setUpdateFrom(UpdateFrom updateFrom) {
        this.updateFrom = updateFrom;
        return this;
    }

    @Override
    public RomUpdater setDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public RomUpdater setGitHubUserAndRepo(@NonNull String user, @NonNull String repo) {
        this.gitHub = new GitHub(user, repo);
        return this;
    }

    @Override
    public RomUpdater setUpdateXML(@NonNull String xmlUrl) {
        this.xmlOrJsonUrl = xmlUrl;
        return this;
    }

    @Override
    public RomUpdater setUpdateJSON(@NonNull String jsonUrl) {
        this.xmlOrJsonUrl = jsonUrl;
        return this;
    }


    @Override
    public RomUpdater showEvery(Integer times) {
        this.showEvery = times;
        return this;
    }

    @Override
    public RomUpdater showAppUpdated(Boolean res) {
        this.showAppUpdated = res;
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogTitleWhenUpdateAvailable(@NonNull String title) {
        setTitleOnUpdateAvailable(title);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogTitleWhenUpdateAvailable(@StringRes int textResource) {
        setTitleOnUpdateAvailable(textResource);
        return this;
    }

    @Override
    public RomUpdater setTitleOnUpdateAvailable(@NonNull String title) {
        this.titleUpdate = title;
        return this;
    }

    @Override
    public RomUpdater setTitleOnUpdateAvailable(@StringRes int textResource) {
        this.titleUpdate = context.getString(textResource);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogDescriptionWhenUpdateAvailable(@NonNull String description) {
        setContentOnUpdateAvailable(description);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogDescriptionWhenUpdateAvailable(@StringRes int textResource) {
        setContentOnUpdateAvailable(textResource);
        return this;
    }

    @Override
    public RomUpdater setContentOnUpdateAvailable(@NonNull String description) {
        this.descriptionUpdate = description;
        return this;
    }

    @Override
    public RomUpdater setContentOnUpdateAvailable(@StringRes int textResource) {
        this.descriptionUpdate = context.getString(textResource);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogTitleWhenUpdateNotAvailable(@NonNull String title) {
        setTitleOnUpdateNotAvailable(title);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogTitleWhenUpdateNotAvailable(@StringRes int textResource) {
        setTitleOnUpdateNotAvailable(textResource);
        return this;
    }

    @Override
    public RomUpdater setTitleOnUpdateNotAvailable(@NonNull String title) {
        this.titleNoUpdate = title;
        return this;
    }

    @Override
    public RomUpdater setTitleOnUpdateNotAvailable(@StringRes int textResource) {
        this.titleNoUpdate = context.getString(textResource);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogDescriptionWhenUpdateNotAvailable(@NonNull String description) {
        setContentOnUpdateNotAvailable(description);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogDescriptionWhenUpdateNotAvailable(@StringRes int textResource) {
        setContentOnUpdateNotAvailable(textResource);
        return this;
    }

    @Override
    public RomUpdater setContentOnUpdateNotAvailable(@NonNull String description) {
        this.descriptionNoUpdate = description;
        return this;
    }

    @Override
    public RomUpdater setContentOnUpdateNotAvailable(@StringRes int textResource) {
        this.descriptionNoUpdate = context.getString(textResource);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogButtonUpdate(@NonNull String text) {
        setButtonUpdate(text);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogButtonUpdate(@StringRes int textResource) {
        setButtonUpdate(textResource);
        return this;
    }

    @Override
    public RomUpdater setButtonUpdate(@NonNull String text) {
        this.btnUpdate = text;
        return this;
    }

    @Override
    public RomUpdater setButtonUpdate(@StringRes int textResource) {
        this.btnUpdate = context.getString(textResource);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogButtonDismiss(@NonNull String text) {
        setButtonDismiss(text);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogButtonDismiss(@StringRes int textResource) {
        setButtonDismiss(textResource);
        return this;
    }

    @Override
    public RomUpdater setButtonDismiss(@NonNull String text) {
        this.btnDismiss = text;
        return this;
    }

    @Override
    public RomUpdater setButtonDismiss(@StringRes int textResource) {
        this.btnDismiss = context.getString(textResource);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogButtonDoNotShowAgain(@NonNull String text) {
        setButtonDoNotShowAgain(text);
        return this;
    }

    @Override
    @Deprecated
    public RomUpdater setDialogButtonDoNotShowAgain(@StringRes int textResource) {
        setButtonDoNotShowAgain(textResource);
        return this;
    }

    @Override
    public RomUpdater setButtonDoNotShowAgain(@NonNull String text) {
        this.btnDisable = text;
        return this;
    }

    @Override
    public RomUpdater setButtonDoNotShowAgain(@StringRes int textResource) {
        this.btnDisable = context.getString(textResource);
        return this;
    }

    @Override
    public RomUpdater setIcon(@DrawableRes int iconRes) {
        this.iconResId = iconRes;
        return this;
    }

    @Override
    public RomUpdater init() {
        start();
        return this;
    }

    @Override
    public void start() {
        latestAppVersion = new UtilsAsync.LatestAppVersion(context, false, updateFrom, gitHub, xmlOrJsonUrl, new LibraryListener() {
            @Override
            public void onSuccess(Update update) {
                if (UtilsLibrary.isUpdateAvailable(UtilsLibrary.getAppInstalledVersion(), update.getLatestVersion())) {
                    Integer successfulChecks = libraryPreferences.getSuccessfulChecks();
                    if (UtilsLibrary.isAbleToShow(successfulChecks, showEvery)) {
                        switch (display) {
                            case DIALOG:
                                alertDialog = UtilsDisplay.showUpdateAvailableDialog(context, titleUpdate, getDescriptionUpdate(context, update, Display.DIALOG), btnDismiss, btnUpdate, btnDisable, updateFrom, update.getUrlToDownload());
                                alertDialog.show();
                                break;
                            case SNACKBAR:
                                snackbar = UtilsDisplay.showUpdateAvailableSnackbar(context, getDescriptionUpdate(context, update, Display.SNACKBAR), UtilsLibrary.getDurationEnumToBoolean(duration), updateFrom, update.getUrlToDownload());
                                snackbar.show();
                                break;
                            case NOTIFICATION:
                                UtilsDisplay.showUpdateAvailableNotification(context, titleUpdate, getDescriptionUpdate(context, update, Display.NOTIFICATION), updateFrom, update.getUrlToDownload(), iconResId);
                                break;
                        }
                    }
                    libraryPreferences.setSuccessfulChecks(successfulChecks + 1);
                } else if (showAppUpdated) {
                    switch (display) {
                        case DIALOG:
                            alertDialog = UtilsDisplay.showUpdateNotAvailableDialog(context, titleNoUpdate, getDescriptionNoUpdate(context));
                            alertDialog.show();
                            break;
                        case SNACKBAR:
                            snackbar = UtilsDisplay.showUpdateNotAvailableSnackbar(context, getDescriptionNoUpdate(context), UtilsLibrary.getDurationEnumToBoolean(duration));
                            snackbar.show();
                            break;
                        case NOTIFICATION:
                            UtilsDisplay.showUpdateNotAvailableNotification(context, titleNoUpdate, getDescriptionNoUpdate(context), iconResId);
                            break;
                    }
                }
            }

            @Override
            public void onFailed(RomUpdaterError error) {
                if (error == RomUpdaterError.UPDATE_VARIES_BY_DEVICE) {
                    Log.e("RomUpdater", "UpdateFrom.GOOGLE_PLAY isn't valid: update varies by device.");
                } else if (error == RomUpdaterError.GITHUB_USER_REPO_INVALID) {
                    throw new IllegalArgumentException("GitHub user or repo is empty!");
                } else if (error == RomUpdaterError.XML_URL_MALFORMED) {
                    throw new IllegalArgumentException("XML file is not valid!");
                } else if (error == RomUpdaterError.JSON_URL_MALFORMED) {
                    throw new IllegalArgumentException("JSON file is not valid!");
                }
            }
        });

        latestAppVersion.execute();
    }

    @Override
    public void stop() {
        if (latestAppVersion != null && !latestAppVersion.isCancelled()) {
            latestAppVersion.cancel(true);
        }
    }

    @Override
    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    private String getDescriptionUpdate(Context context, Update update, Display display) {
        if (descriptionUpdate == null) {
            switch (display) {
                case DIALOG:
                    if (update.getReleaseNotes() != null && !TextUtils.isEmpty(update.getReleaseNotes())) {
                        return String.format(context.getResources().getString(R.string.appupdater_update_available_description_dialog_before_release_notes), update.getLatestVersion(), update.getReleaseNotes());
                    } else {
                        return String.format(context.getResources().getString(R.string.appupdater_update_available_description_dialog), update.getLatestVersion(), UtilsLibrary.getAppName(context));
                    }

                case SNACKBAR:
                    return String.format(context.getResources().getString(R.string.appupdater_update_available_description_snackbar), update.getLatestVersion());

                case NOTIFICATION:
                    return String.format(context.getResources().getString(R.string.appupdater_update_available_description_notification), update.getLatestVersion(), UtilsLibrary.getAppName(context));

            }
        }
        return descriptionUpdate;

    }

    private String getDescriptionNoUpdate(Context context) {
        if (descriptionNoUpdate == null) {
            return String.format(context.getResources().getString(R.string.appupdater_update_not_available_description), UtilsLibrary.getAppName(context));
        } else {
            return descriptionNoUpdate;
        }
    }

}

package io.github.otaupdater.otalibary;

import android.content.Context;
import android.os.AsyncTask;

import io.github.otaupdater.otalibary.enums.RomUpdaterError;
import io.github.otaupdater.otalibary.enums.UpdateFrom;
import io.github.otaupdater.otalibary.objects.Update;


class UtilsAsync {

    static class LatestRomVersion extends AsyncTask<Void, Void, Update> {
        private Context context;
        private LibraryPreferences libraryPreferences;
        private Boolean fromUtils;
        private UpdateFrom updateFrom;
        private String xmlOrJsonUrl;
        private RomUpdater.LibraryListener listener;

        public LatestRomVersion(Context context, Boolean fromUtils, UpdateFrom updateFrom, String xmlOrJsonUrl, RomUpdater.LibraryListener listener) {
            this.context = context;
            this.libraryPreferences = new LibraryPreferences(context);
            this.fromUtils = fromUtils;
            this.updateFrom = updateFrom;
            this.xmlOrJsonUrl = xmlOrJsonUrl;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (UtilsLibrary.isNetworkAvailable(context)) {
                if (!fromUtils && !libraryPreferences.getRomUpdaterShow()) {
                    cancel(true);
                } else {
                    if (updateFrom == UpdateFrom.XML && (xmlOrJsonUrl == null || !UtilsLibrary.isStringAnUrl(xmlOrJsonUrl))) {
                        listener.onFailed(RomUpdaterError.XML_URL_MALFORMED);

                        cancel(true);
                    } else if (updateFrom == UpdateFrom.JSON && (xmlOrJsonUrl == null || !UtilsLibrary.isStringAnUrl(xmlOrJsonUrl))) {
                        listener.onFailed(RomUpdaterError.JSON_URL_MALFORMED);

                        cancel(true);
                    }
                }
            } else {
                listener.onFailed(RomUpdaterError.NETWORK_NOT_AVAILABLE);
                cancel(true);
            }
        }

        @Override
        protected Update doInBackground(Void... voids) {
            if (updateFrom == UpdateFrom.XML || updateFrom == UpdateFrom.JSON) {
                Update update = UtilsLibrary.getLatestRomVersion(updateFrom, xmlOrJsonUrl);
                    if (update != null) {
                    return update;
                } else {
                    RomUpdaterError error = updateFrom == UpdateFrom.XML ? RomUpdaterError.XML_ERROR
                            : RomUpdaterError.JSON_ERROR;

                    listener.onFailed(error);
                    cancel(true);
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Update update) {
            super.onPostExecute(update);
            if (UtilsLibrary.isStringAVersion(update.getLatestVersion())) {
                listener.onSuccess(update);
            } else {
                listener.onFailed(RomUpdaterError.UPDATE_VARIES_BY_DEVICE);
            }
        }
    }

}

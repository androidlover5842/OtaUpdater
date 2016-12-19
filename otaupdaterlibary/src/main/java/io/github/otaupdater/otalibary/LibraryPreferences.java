package io.github.otaupdater.otalibary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class LibraryPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    static final String KeyRomUpdaterShow = "prefRomUpdaterShow";
    static final String KeySuccessfulChecks = "prefSuccessfulChecks";

    public LibraryPreferences(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
    }

    public Boolean getRomUpdaterShow() {
        return sharedPreferences.getBoolean(KeyRomUpdaterShow, true);
    }

    public void setRomUpdaterShow(Boolean res) {
        editor.putBoolean(KeyRomUpdaterShow, res);
        editor.commit();
    }

    public Integer getSuccessfulChecks() {
        return sharedPreferences.getInt(KeySuccessfulChecks, 0);
    }

    public void setSuccessfulChecks(Integer checks) {
        editor.putInt(KeySuccessfulChecks, checks);
        editor.commit();
    }

}

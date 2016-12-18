package io.github.otaupdater.otaupdater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.otaupdater.otalibary.AppUpdater;
import io.github.otaupdater.otalibary.enums.Display;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AppUpdater(this)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?")
                .setButtonDismiss("Maybe later")
                .setButtonDoNotShowAgain("Huh, not interested")
                .setDisplay(Display.SNACKBAR)
                .start();

    }
}

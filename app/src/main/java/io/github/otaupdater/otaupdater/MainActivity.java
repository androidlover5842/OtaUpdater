package io.github.otaupdater.otaupdater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import io.github.otaupdater.otaupdater.fragment.GithubReleasesFragment;

public class MainActivity extends AppCompatActivity {
    private GithubReleasesFragment mFragmentOldRelease;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mFragmentOldRelease = new GithubReleasesFragment().setTargetURL(Config.URL_OLD_RELEASES);
        updateFragment(mFragmentOldRelease);
    }
    protected void updateFragment(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }
}


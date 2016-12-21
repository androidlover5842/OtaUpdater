package io.github.otaupdater.otaupdater.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.github.otaupdater.otaupdater.util.Config;
import io.github.otaupdater.otaupdater.R;
import io.github.otaupdater.otaupdater.fragment.GithubReleasesFragment;

public class MainActivity extends AppCompatActivity {
    private GithubReleasesFragment mFragmentOldRelease;
    private Button mCheckUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mFragmentOldRelease = new GithubReleasesFragment().setTargetURL(Config.URL_OLD_RELEASES());
        updateFragment(mFragmentOldRelease);
        mCheckUpdate=(Button)findViewById(R.id.activity_main_check_for_update);
        mCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), DialogActivity.class);
                startActivity(i);
            }
        });
    }
    protected void updateFragment(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }
}


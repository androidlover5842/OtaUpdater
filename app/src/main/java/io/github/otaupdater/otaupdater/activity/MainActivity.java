package io.github.otaupdater.otaupdater.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.github.otaupdater.otaupdater.R;
import io.github.otaupdater.otaupdater.fragment.GithubReleasesFragment;
import io.github.otaupdater.otaupdater.util.Config;

import static io.github.otaupdater.otaupdater.util.Config.PermissionRequest;
import static io.github.otaupdater.otaupdater.util.Config.PutStringPreferences;
import static io.github.otaupdater.otaupdater.util.Config.updateFragment;

public class MainActivity extends AppCompatActivity {
    private GithubReleasesFragment mFragmentOldRelease;
    private Button mCheckUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PutStringPreferences(this,"version", Config.getRomInstalledVersion());
        this.mFragmentOldRelease = new GithubReleasesFragment().setTargetURL(Config.URL_OLD_RELEASES());
        updateFragment(this,mFragmentOldRelease,R.id.content_frame);
        mCheckUpdate=(Button)findViewById(R.id.activity_main_check_for_update);
        mCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), DialogActivity.class);
                startActivity(i);
            }
        });
        PermissionRequest(this);
    }

}


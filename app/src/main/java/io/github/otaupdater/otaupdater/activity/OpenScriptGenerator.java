package io.github.otaupdater.otaupdater.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;

import io.github.otaupdater.otaupdater.R;


/**
 * Created by sumit on 15/1/17.
 */

public class OpenScriptGenerator extends AppCompatActivity {
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flasher);
        setTheme(R.style.AppTheme);

        SwitchCompat mWipeData=(SwitchCompat)findViewById(R.id.wipe_data);
        SwitchCompat mWipeCache=(SwitchCompat)findViewById(R.id.wipe_cache);
        TextView Path=(TextView)findViewById(R.id.file_path_textView);
        String p=getPreferences(OpenScriptGenerator.this,"FilePath");

        Path.setText(p);

    }

    private String getPreferences(Context context, String Name) {
        String o;
        settings = context.getSharedPreferences(Name, 0); // 0 - for private mode
        o=settings.getString(Name,null);

        return o;

    }
}

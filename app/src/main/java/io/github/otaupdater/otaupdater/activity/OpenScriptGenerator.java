package io.github.otaupdater.otaupdater.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import eu.chainfire.libsuperuser.Shell;
import io.github.otaupdater.otaupdater.R;


/**
 * Created by sumit on 15/1/17.
 */

public class OpenScriptGenerator extends AppCompatActivity {
    private SharedPreferences settings;
    private SwitchCompat mWipeData,mWipeCache;
    private TextView Path;
    private Button FlashButton;
    private String p,SCRIPT_PATH = "/cache/recovery/openrecoveryscript";
    private boolean WipeData,WipeCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flasher);
        setTheme(R.style.AppTheme);

        mWipeData=(SwitchCompat)findViewById(R.id.wipe_data);
        mWipeCache=(SwitchCompat)findViewById(R.id.wipe_cache);
        Path=(TextView)findViewById(R.id.file_path_textView);
        FlashButton=(Button)findViewById(R.id.flash_button);
        p=getPreferences(OpenScriptGenerator.this,"FilePath");

        Path.setText(p);
        FlashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWipeData.isEnabled())
                {
                    Toast.makeText(OpenScriptGenerator.this, "Data will wiped",
                            Toast.LENGTH_LONG).show();
                    WipeData=true;

                }
                if(mWipeCache.isEnabled())
                {
                    Toast.makeText(OpenScriptGenerator.this, "Cache will wiped",
                            Toast.LENGTH_SHORT).show();
                    WipeCache=true;

                }
                Shell.SU.run("mount -o ro,remount,ro /system");
                Shell.SU.run("touch "+SCRIPT_PATH);
                Shell.SU.run("echo ' install "+p+" ' > "+ SCRIPT_PATH);
                if(WipeData==true){
                    Shell.SU.run("echo 'install wipe data' >> "+ SCRIPT_PATH);
                }
                if(WipeCache==true){
                    Shell.SU.run("echo 'install wipe cache' >> "+ SCRIPT_PATH);
                }
                Shell.SU.run("reboot recovery");

            }
        });


    }

    private String getPreferences(Context context, String Name) {
        String o;
        settings = context.getSharedPreferences(Name, 0); // 0 - for private mode
        o=settings.getString(Name,null);

        return o;

    }
}

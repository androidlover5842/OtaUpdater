package io.github.otaupdater.otaupdater.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;
import com.stericson.RootTools.RootTools;

import eu.chainfire.libsuperuser.Shell;
import io.github.otaupdater.otaupdater.R;
import io.github.otaupdater.otaupdater.util.Tools;


/**
 * Created by sumit on 15/1/17.
 */

public class OpenScriptGenerator extends AppCompatActivity {
    private SharedPreferences settings;
    private SwitchCompat mWipeData,mWipeCache;
    private TextView Path;
    private Button FlashButton;
    private String p,SCRIPT_PATH = "/cache/recovery/openrecoveryscript";
    private PanterDialog FlashDialog;
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
        FlashDialog= new PanterDialog(OpenScriptGenerator.this);

        mWipeCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWipeCache.isChecked()) {
                    Snackbar.make(v, "Cache wipe enabled", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(v, "Cache wipe disabled", Snackbar.LENGTH_LONG).show();

                }

            }
        });
        mWipeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWipeData.isChecked()) {
                    Snackbar.make(v, "Data wipe enabled", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(v, "Data wipe disabled", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        Path.setText(p);
        FlashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RootTools.isRootAvailable()) {

                    if (RootTools.isAccessGiven()) {
                        Tools.shell("mount -o rw,remount,rw /cache", true);
                        Tools.shell("touch " + SCRIPT_PATH, true);
                        Tools.shell("echo 'install " + p + " ' > " + SCRIPT_PATH, true);
                        if (mWipeData.isChecked()) {
                            Tools.shell("echo 'install wipe data ' >> " + SCRIPT_PATH, true);
                        }
                        if (mWipeCache.isChecked()) {
                            Tools.shell("echo 'install wipe cache ' >> " + SCRIPT_PATH, true);
                        }

                        FlashDialog.setTitle("Are you sure ?")
                                .setHeaderBackground(R.color.colorPrimaryDark)
                                .setMessage(p)
                                .setPositive("Flash", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Shell.SU.run("reboot recovery");
                                        FlashDialog.dismiss();

                                    }
                                })
                                .setNegative("Cancel", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FlashDialog.dismiss();
                                    }
                                })
                                .isCancelable(false)
                                .withAnimation(Animation.SIDE);
                        FlashDialog.show();
                    } else {
                        Snackbar.make(v, "Not having enough permission .", Snackbar.LENGTH_LONG).show();

                    }
                }else{
                    Snackbar.make(v, "Device not rooted .", Snackbar.LENGTH_LONG).show();

                }
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

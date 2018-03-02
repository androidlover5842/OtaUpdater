package io.github.otaupdater.otaupdater.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import io.github.otaupdater.otalibary.util.ShellExecuter;
import io.github.otaupdater.otaupdater.R;

import static io.github.otaupdater.otaupdater.util.Config.PutStringPreferences;


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
    private ProgressBar progressBar;
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
        progressBar=findViewById(R.id.progressBar_flash);
        if (!p.endsWith(".zip") && new File(p).exists())
        {
            FlashButton.setEnabled(false);
            Toast.makeText(OpenScriptGenerator.this,"File type not flashable",Toast.LENGTH_SHORT).show();
        }

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

                        FlashDialog.setTitle("Are you sure ?")
                                .setHeaderBackground(R.color.colorPrimaryDark)
                                .setMessage(p)
                                .setPositive("Flash", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new ROMFlasher().execute();
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

    private class ROMFlasher extends AsyncTask<Double ,Double ,Double >{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FlashDialog.dismiss();
        }

        @Override
        protected Double doInBackground(Double... strings) {

            File filein  = new File(p);
            File fileout = new File(getCacheDir()+"/update.zip");
            FileInputStream fin  = null;
            FileOutputStream fout = null;
            long length  = filein.length();
            long counter = 0;
            int r = 0;
            byte[] b = new byte[1024];
            try {
            fin  = new FileInputStream(filein);
            fout = new FileOutputStream(fileout);
            while( (r = fin.read(b)) != -1) {
                counter += r;
                fout.write(b, 0, r);
                publishProgress((counter * 100.0) / length);
            }

        }catch (Exception e)
            {
                e.printStackTrace();
            }
            PutStringPreferences(getApplicationContext(),"NewPath",getCacheDir()+"/update.zip");
            p=getPreferences(getApplicationContext(),"NewPath");
            FlashDialog.setMessage(p);
            ShellExecuter.runAsRoot("mount -o rw,remount,rw /cache");
            ShellExecuter.runAsRoot("touch " + SCRIPT_PATH);
            ShellExecuter.runAsRoot("echo 'install "+getCacheDir()+"/update.zip" + " ' > " + SCRIPT_PATH);
            if (mWipeData.isChecked()) {
                ShellExecuter.runAsRoot("echo 'install wipe data ' >> " + SCRIPT_PATH);
            }
            if (mWipeCache.isChecked()) {
                ShellExecuter.runAsRoot("echo 'install wipe cache ' >> " + SCRIPT_PATH);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            int d=values[0].intValue();
            progressBar.setProgress(d);
            FlashButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            progressBar.setVisibility(View.GONE);
            new AlertDialog.Builder(
                    OpenScriptGenerator.this)
                    .setTitle("Reboot ").setMessage("Do you want to reboot recovery now ?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ShellExecuter.runAsRoot("reboot recovery");
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }
    }
}

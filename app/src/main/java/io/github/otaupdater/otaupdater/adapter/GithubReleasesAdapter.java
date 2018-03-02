package io.github.otaupdater.otaupdater.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;
import com.stericson.RootTools.RootTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.github.otaupdater.otaupdater.R;
import io.github.otaupdater.otaupdater.activity.OpenScriptGenerator;

import static io.github.otaupdater.otaupdater.util.Config.DownloadFileName;
import static io.github.otaupdater.otaupdater.util.Config.Downloader;
import static io.github.otaupdater.otaupdater.util.Config.PutStringPreferences;
import static io.github.otaupdater.otaupdater.util.Config.uri;

/**
 * Created by: veli
 * Date: 10/25/16 10:13 PM
 */

public class GithubReleasesAdapter extends GithubAdapterIDEA
{
	private String fileName;
	private Long fileId;
	private TextView text1,text2,betaWarningText,StableText,latestRomText,oldRomText;
	private PanterDialog DownloaderDialog;
	public GithubReleasesAdapter(Context context)
	{
		super(context);
	}
	private View finalConvertView;

	@Override
	protected View onView(int position, View convertView, ViewGroup parent)
	{
		final JSONObject release = (JSONObject) getItem(position);

		if (convertView == null)
			convertView = mInflater.inflate(R.layout.list_release, parent, false);

		text1 = (TextView) convertView.findViewById(R.id.list_release_name);
		text2 = (TextView) convertView.findViewById(R.id.list_release_changelog);
		betaWarningText = (TextView) convertView.findViewById(R.id.list_release_beta_release_beta);
		StableText = (TextView) convertView.findViewById(R.id.list_release_stable);
		latestRomText=(TextView)convertView.findViewById(R.id.list_release_latest);
		oldRomText=(TextView)convertView.findViewById(R.id.list_release_old);
		DownloaderDialog= new PanterDialog(getContext());

		final Button actionButton = (Button) convertView.findViewById(R.id.list_release_action_button);


		try
		{

			finalConvertView = convertView;
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try {
					fileName = release.getString("name");
					fileId = release.getLong("id");
					uri = Uri.parse(release.getString("browser_download_url"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					DownloadFileName=fileId + "-" + fileName;

					final File fileIns = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + DownloadFileName);
					if (fileIns.isFile()){
						actionButton.setVisibility((actionButton.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
						actionButton.setText(R.string.install);
						actionButton.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								PutStringPreferences(mContext,"FilePath", fileIns.getPath());
								if(RootTools.isRootAvailable()) {

								if (RootTools.isAccessGiven()) {
									mContext.startActivity(new Intent(mContext, OpenScriptGenerator.class));
								}
								else {
									Snackbar.make(v, "Not having enough permission .", Snackbar.LENGTH_LONG).show();

								}
								}else {
									Snackbar.make(v, "Device not rooted .", Snackbar.LENGTH_LONG).show();

								}

							}
						});
					}else {
						DownloaderDialog.setTitle("Download ?")
								.setHeaderBackground(R.color.colorPrimaryDark)
								.setMessage(DownloadFileName)
								.setPositive("Download", new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										Downloader(getContext());
										PutStringPreferences(mContext,"FilePath", fileIns.getPath());
										DownloaderDialog.dismiss();
									}
								})
								.setNegative("Cancel", new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										DownloaderDialog.dismiss();
									}
								})
								.isCancelable(false)
								.withAnimation(Animation.SIDE);
						DownloaderDialog.show();


					}
				}
			});

			if (release.getBoolean("prerelease")) {
				if (release.getBoolean("prerelease") == true)

				{
					betaWarningText.setVisibility(View.VISIBLE);
				}
			}
			if (release.getBoolean("stable")) {
				if (release.getBoolean("stable") == true) {
					StableText.setVisibility(View.VISIBLE);
				}
			}
			if (release.has("tag_name"))
				text1.setText(release.getString("tag_name"));

			if (release.has("body"))
				text2.setText(release.getString("body"));
			if(release.has("latest")==true)
			{
				latestRomText.setText("Latest");
				latestRomText.setVisibility(View.VISIBLE);
				oldRomText.setVisibility(View.GONE);
			}
			else {
				oldRomText.setText("Old");
				oldRomText.setVisibility(View.VISIBLE);
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		return convertView;
	}
}
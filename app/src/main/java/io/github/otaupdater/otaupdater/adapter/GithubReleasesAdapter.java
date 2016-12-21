package io.github.otaupdater.otaupdater.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.otaupdater.otaupdater.R;

import static io.github.otaupdater.otaupdater.util.Config.DownloadFileName;
import static io.github.otaupdater.otaupdater.util.Config.Downloader;
import static io.github.otaupdater.otaupdater.util.Config.uri;

/**
 * Created by: veli
 * Date: 10/25/16 10:13 PM
 */

public class GithubReleasesAdapter extends GithubAdapterIDEA
{
	private String fileName;
	private Long fileId;
	private TextView text1,text2,betaWarningText,StableText;
	public GithubReleasesAdapter(Context context)
	{
		super(context);
	}


	@Override
	protected View onView(int position, View convertView, ViewGroup parent)
	{
		final JSONObject release = (JSONObject) getItem(position);

		if (convertView == null)
			convertView = mInflater.inflate(R.layout.list_release, parent, false);

		text1 = (TextView) convertView.findViewById(R.id.list_release_text1);
		text2 = (TextView) convertView.findViewById(R.id.list_release_text2);
		betaWarningText = (TextView) convertView.findViewById(R.id.list_release_beta_release_beta);
		StableText = (TextView) convertView.findViewById(R.id.list_release_stable);

		final Button actionButton = (Button) convertView.findViewById(R.id.list_release_action_button);

		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				actionButton.setVisibility((actionButton.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
			}
		});

		try
		{

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

			if(release.has("browser_download_url"))
			{
				actionButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{

						try
						{
							fileName = release.getString("name");
							fileId = release.getLong("id");
							DownloadFileName=fileId + "-" + fileName;
							uri = Uri.parse(release.getString("browser_download_url"));

							Downloader(getContext());

							actionButton.setEnabled(false);
						} catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				});
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		return convertView;
	}
}
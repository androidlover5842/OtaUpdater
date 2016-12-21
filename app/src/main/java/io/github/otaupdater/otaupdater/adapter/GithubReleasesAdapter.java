package io.github.otaupdater.otaupdater.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.otaupdater.otaupdater.R;

/**
 * Created by: veli
 * Date: 10/25/16 10:13 PM
 */

public class GithubReleasesAdapter extends GithubAdapterIDEA
{
	private String fileName;
	private Long fileId;
	private Uri uri;
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

			if (release.getBoolean("prerelease"))
				betaWarningText.setVisibility(View.VISIBLE);
			if (release.getBoolean("stable"))
				StableText.setVisibility(View.VISIBLE);

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
						DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(getContext().DOWNLOAD_SERVICE);

						try
						{
							fileName = release.getString("name");
							fileId = release.getLong("id");
							uri = Uri.parse(release.getString("browser_download_url"));
							DownloadManager.Request request = new DownloadManager.Request(uri);
							request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileId + "-" + fileName);

							request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
							Long reference = downloadManager.enqueue(request);

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
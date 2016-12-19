package io.github.otaupdater.otalibary.objects;

import java.net.URL;

public class Update {
    private String version;
    private String releaseNotes;
    private URL RomZip;

    public Update() {}

    public Update(String latestVersion, String releaseNotes, URL apk) {
        this.version = latestVersion;
        this.RomZip = apk;
        this.releaseNotes = releaseNotes;
    }

    public String getLatestVersion() {
        return version;
    }

    public void setLatestVersion(String latestVersion) {
        this.version = latestVersion;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public URL getUrlToDownload() {
        return RomZip;
    }

    public void setUrlToDownload(URL apk) {
        this.RomZip = apk;
    }
}

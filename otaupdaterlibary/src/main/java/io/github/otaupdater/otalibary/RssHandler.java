package io.github.otaupdater.otalibary;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
import java.net.URL;

import io.github.otaupdater.otalibary.objects.Update;

class RssHandler extends DefaultHandler {
    private Update update;
    private StringBuilder builder;

    public Update getUpdate() {
        return update;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        if (this.update != null) {
            builder.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);

        if (this.update != null) {
            if (localName.equals("RomLatestVersion")) {
                update.setLatestVersion(builder.toString().trim());
            } else if (localName.equals("Changelog")) {
                update.setReleaseNotes(builder.toString().trim());
            } else if (localName.equals("URL")) {
                try {
                    update.setUrlToDownload(new URL(builder.toString().trim()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName,
                             String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equals("update")) {
            update = new Update();
        }
    }

}

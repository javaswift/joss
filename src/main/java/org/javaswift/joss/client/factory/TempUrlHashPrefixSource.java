package org.javaswift.joss.client.factory;

import java.net.MalformedURLException;
import java.net.URL;

import org.javaswift.joss.exception.CommandException;

public enum TempUrlHashPrefixSource {
    PUBLIC_URL_PATH,
    INTERNAL_URL_PATH,
    ADMIN_URL_PATH;

    public static String getPath(String url) {
        try {
            return new URL(url).getPath();
        } catch (MalformedURLException e) {
            throw new CommandException("Unable to parse URL "+url);
        }
    }

}

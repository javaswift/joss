package org.javaswift.joss.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * The purpose of this class is to supplement URLEncode.encode with the option to convert
 * '+' into %20.
 * See also: <a href="http://stackoverflow.com/questions/4737841/urlencoder-not-able-to-translate-space-character">
 * http://stackoverflow.com/questions/4737841/urlencoder-not-able-to-translate-space-character</a>
 * @author Robert Bor
 */
public class SpaceURLEncoder {

    public static String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
    }

}

package org.javaswift.joss.client.core;

import org.javaswift.joss.util.HashSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.HashMap;

public class ImageURL {

    public static final Logger LOG = LoggerFactory.getLogger(ImageURL.class);

    final private AbstractStoredObject object;

    final private String prefix;

    final private String method;

    private long expiry;

    private static final Map<String, String> hostnameMap;
    static
    {
        hostnameMap = new HashMap<String, String>();
        hostnameMap.put("mss.sankuai.com", "mss-img.sankuai.com");
        hostnameMap.put("mss.vip.sankuai.com", "mss-img.vip.sankuai.com");
        hostnameMap.put("msstest.sankuai.com", "msstest-img.sankuai.com");
        hostnameMap.put("msstest.vip.sankuai.com", "msstest-img.vip.sankuai.com");

    }

    public ImageURL(final String method, final String prefix, final AbstractStoredObject object) {
        this.method = method;
        this.prefix = prefix;
        this.object = object;
    }

    public ImageURL setServerTimeExpiry(final long seconds) {
        this.expiry = this.object.getAccount().getActualServerTimeInSeconds(seconds);
        return this;
    }

    public ImageURL setFixedExpiry(final long seconds) {
        this.expiry = seconds;
        return this;
    }

    protected String getSignaturePlainText(String param) {
        // Note that we're not making use here of the standard getPath() because we don't want the URL encoded names,
        // but the pure names. Swift uses the same approach to compose a signature plaintext with container/object names.
        String objectPath = this.prefix + "/" + this.object.getContainer().getName() + "/" + this.object.getName() + "@" + param;
        return this.method + "\n" + this.expiry + "\n" + objectPath;
    }

    protected String getSignature(String param) {
        String plainText = getSignaturePlainText(param);
        LOG.debug("Text to hash for the signature (CRLF replaced by readable \\n): "+plainText.replaceAll("\n", "\\n"));
        return HashSignature.getSignature(this.object.getContainer().getAccount().getHashPassword(), plainText);
    }

    private String getImagePublicURL(String param) {
        String public_url = this.object.getPublicURL();
        String http_prefix = "";
        String public_url_without_prefix;
        if (public_url.startsWith("http://")) {
            http_prefix = "http://";
            public_url_without_prefix = public_url.substring(7);
        } else if (public_url.startsWith("https://")) {
            http_prefix = "https://";
            public_url_without_prefix = public_url.substring(8);
        } else {
            public_url_without_prefix = public_url;
        }
        String[] url_split = public_url_without_prefix.split("/", 2);
        String hostname_str = url_split[0];
        String replace_hostname = this.hostnameMap.get(hostname_str);
        if (replace_hostname != null) {
            hostname_str = replace_hostname;
        }
        for (int i = 1; i < url_split.length; i++) {
            hostname_str += "/" + url_split[i];
        }
        return http_prefix + hostname_str + "@" + param;

    }

    public String getImageScaleUrl(int percentage) {
        String perentage_str = String.valueOf(percentage) + "p";
        return this.getImageProcessUrl(perentage_str);
    }


    public String getImageProcessUrl(String param_string) {
        return this.getImagePublicURL(param_string);
    }

    public String getImageScaleTempUrl(int percentage) {
        String perentage_str = String.valueOf(percentage) + "p";
        return this.getImageProcessTempUrl(perentage_str);
    }

    public String getImageProcessTempUrl(String param_string) {
        return this.getImagePublicURL(param_string)+
                "?temp_url_sig="+getSignature(param_string)+
                "&temp_url_expires="+this.expiry;

    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean verify(String signature, long expiry, String param_string) {
        if (signature == null || !signature.equals(getSignature(param_string))) {
            return false;
        }
        return expiry > this.object.getAccount().getActualServerTimeInSeconds(0);
    }

}

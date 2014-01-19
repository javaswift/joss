package org.javaswift.joss.client.core;

import org.javaswift.joss.util.HashSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TempURL {

    public static final Logger LOG = LoggerFactory.getLogger(TempURL.class);

    final private AbstractStoredObject object;

    final private String prefix;

    final private String method;

    private long expiry;

    public TempURL(final String method, final String prefix, final AbstractStoredObject object) {
        this.method = method;
        this.prefix = prefix;
        this.object = object;
    }

    public TempURL setServerTimeExpiry(final long seconds) {
        this.expiry = this.object.getAccount().getActualServerTimeInSeconds(seconds);
        return this;
    }

    public TempURL setFixedExpiry(final long seconds) {
        this.expiry = seconds;
        return this;
    }

    protected String getSignaturePlainText() {
        // Note that we're not making use here of the standard getPath() because we don't want the URL encoded names,
        // but the pure names. Swift uses the same approach to compose a signature plaintext with container/object names.
        String objectPath = this.prefix + "/" + this.object.getContainer().getName() + "/" + this.object.getName();
        return this.method + "\n" + this.expiry + "\n" + objectPath;
    }

    protected String getSignature() {
        String plainText = getSignaturePlainText();
        LOG.debug("Text to hash for the signature (CRLF replaced by readable \\n): "+plainText.replaceAll("\n", "\\n"));
        return HashSignature.getSignature(this.object.getContainer().getAccount().getHashPassword(), plainText);
    }

    public String getTempUrl() {
        return this.object.getPublicURL()+
                "?temp_url_sig="+getSignature()+
                "&temp_url_expires="+this.expiry;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean verify(String signature, long expiry) {
        if (signature == null || !signature.equals(getSignature())) {
            return false;
        }
        return expiry > this.object.getAccount().getActualServerTimeInSeconds(0);
    }

}

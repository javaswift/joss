package nl.tweeenveertig.openstack.headers;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Sets the MD5 hash on an object. The server uses this hash to verify that the upload succeeded
 */
public class Etag extends Header {

    public static final String ETAG = "ETag";

    private String md5;

    public Etag(String md5) throws IOException {
        this.md5 = md5;
    }

    public Etag(InputStream inputStream) throws IOException {
        this.md5 = DigestUtils.md5Hex(inputStream);
    }

    @Override
    public String getHeaderValue() {
        return this.md5;
    }

    @Override
    public String getHeaderName() {
        return ETAG;
    }
}

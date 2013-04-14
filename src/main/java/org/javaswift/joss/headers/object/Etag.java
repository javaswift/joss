package org.javaswift.joss.headers.object;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Sets the MD5 hash on an object. The server uses this hash to verify that the upload succeeded
 */
public class Etag extends SimpleHeader {

    public static final String ETAG = "ETag";

    public Etag(String md5) {
        super(md5);
    }

    public Etag(InputStream inputStream) throws IOException {
        this(DigestUtils.md5Hex(inputStream));
    }

    @Override
    public String getHeaderName() {
        return ETAG;
    }

    public static Etag fromResponse(HttpResponse response) {
        return new Etag(convertResponseHeader(response, ETAG));
    }
}

package org.javaswift.joss.headers.object;

import org.apache.commons.codec.digest.DigestUtils;
import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class EtagTest extends AbstractHeaderTest {

    @Test
    public void testMd5() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        Etag etag = createEtag(bytes);
        assertEquals(DigestUtils.md5Hex(bytes), etag.getHeaderValue());
    }

    @Test
    public void testAddHeader() {
        testHeader(createEtag(new byte[]{0x01, 0x02, 0x03}));
    }

    protected Etag createEtag(byte[] bytes) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return new Etag(inputStream);
        } catch (IOException err) {
            fail(err.getMessage());
        }
        return null;
    }
}

package org.javaswift.joss.swift;

import org.javaswift.joss.headers.object.ObjectContentLength;
import org.javaswift.joss.headers.object.ObjectMetadata;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HeaderStoreTest {

    @Test
    public void getHeader() {
        HeaderStore headers = new HeaderStore();
        headers.addHeader(new ObjectContentLength("123"));
        assertEquals("123", headers.get(ObjectContentLength.CONTENT_LENGTH).getHeaderValue());
    }

    @Test
    public void getMetadata() {
        HeaderStore headers = new HeaderStore();
        headers.addHeader(new ObjectMetadata("alpha", "1"));
        headers.addHeader(new ObjectMetadata("beta", "2"));
        headers.addHeader(new ObjectContentLength("123"));
        assertEquals(2, headers.getMetadata().size());
    }
}

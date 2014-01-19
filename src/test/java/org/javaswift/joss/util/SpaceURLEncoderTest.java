package org.javaswift.joss.util;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static junit.framework.Assert.assertEquals;

public class SpaceURLEncoderTest {

    @Test
    public void encode() throws UnsupportedEncodingException {
        new SpaceURLEncoder();
        assertEquals("a%20b%20c", SpaceURLEncoder.encode("a b c"));
    }
}

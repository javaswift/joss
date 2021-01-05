package org.javaswift.joss.util;

import static junit.framework.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class SpaceURLEncoderTest {

    @Test
    public void encode() throws UnsupportedEncodingException {
        new SpaceURLEncoder();
        assertEquals("a%20b%20c", SpaceURLEncoder.encode("a b c"));
    }
}

package org.javaswift.joss.swift;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockInputStreamWrapperTest {
    @Test
    public void construct() throws IOException {
        InputStream inputStream = new MockInputStreamWrapper(new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03} ));
        inputStream.close();
    }
}

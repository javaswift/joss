package org.javaswift.joss.swift;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class MockInputStreamWrapperTest {
    @Test
    public void construct() throws IOException {
        InputStream inputStream = new MockInputStreamWrapper(new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03} ));
        inputStream.close();
    }
}

package org.javaswift.joss.command.impl.object;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

public class InputStreamWrapperTest {

    @Test
    public void readFromWrapper() throws IOException {
        InputStream inputStream = new InputStreamWrapper(null, new ByteArrayInputStream(new byte[] { 0x01 }));
        assertEquals(0x01, inputStream.read());
        inputStream.close();
    }
}

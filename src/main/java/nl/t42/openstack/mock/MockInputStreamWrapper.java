package nl.t42.openstack.mock;

import nl.t42.openstack.command.object.InputStreamWrapper;

import java.io.IOException;
import java.io.InputStream;

public class MockInputStreamWrapper extends InputStreamWrapper {

    public MockInputStreamWrapper(InputStream inputStream) {
        super(null, inputStream);
    }

    @Override
    public void closeStream() throws IOException {
        inputStream.close();
    }
}

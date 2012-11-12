package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.command.object.InputStreamWrapper;

import java.io.IOException;
import java.io.InputStream;

public class MockInputStreamWrapper extends InputStreamWrapper {

    public MockInputStreamWrapper(InputStream inputStream) {
        super(null, inputStream);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}

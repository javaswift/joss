package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.AbstractCommand;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWrapper {

    protected InputStream inputStream;

    private AbstractCommand command;

    public InputStreamWrapper(AbstractCommand command, InputStream inputStream) {
        this.command = command;
        this.inputStream = inputStream;
    }

    public void closeStream() throws IOException {
        command.closeStream();
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }
}

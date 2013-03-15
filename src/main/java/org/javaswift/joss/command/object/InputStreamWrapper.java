package org.javaswift.joss.command.object;

import org.javaswift.joss.command.core.AbstractCommand;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWrapper extends InputStream {

    protected InputStream inputStream;

    private AbstractCommand command;

    public InputStreamWrapper(AbstractCommand command, InputStream inputStream) {
        this.command = command;
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return inputStream.read(buffer);
    }

    @Override
    public void close() throws IOException {
        command.close();
    }
}

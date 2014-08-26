package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.impl.core.AbstractCommand;

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
        if (inputStream == null) {
            throw new IOException("No input stream defined.");
        }
        return inputStream.read();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        if (inputStream == null) {
            throw new IOException("No input stream defined.");
        }
        return inputStream.read(buffer);
    }

    @Override
    public void close() throws IOException {
        if (command != null) {
            command.close();
        }
    }
}

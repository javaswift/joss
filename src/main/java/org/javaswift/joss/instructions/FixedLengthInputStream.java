package org.javaswift.joss.instructions;

import java.io.IOException;
import java.io.InputStream;

/**
* The purpose of this class is to read a limited number of bytes. It is used to read from
* a RandomAccessFile and it supports the segmentation strategy.
* Inspired by http://stackoverflow.com/questions/2888335/how-can-i-create-constrained-inputstream-to-read-only-part-of-the-file
*/
public class FixedLengthInputStream extends InputStream {

    private final InputStream decorated;
    private long length;

    public FixedLengthInputStream(InputStream decorated, long length) {
        this.decorated = decorated;
        this.length = length;
    }

    @Override
    public int read() throws IOException {
        return (length-- <= 0) ? -1 : decorated.read();
    }

}

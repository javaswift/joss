package org.javaswift.joss.instructions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;

public class SegmentationPlanFile extends SegmentationPlan {

    private RandomAccessFile randomAccessFile;

    private Long fileLength;

    public SegmentationPlanFile(File file, long segmentationSize) throws IOException {
        super(segmentationSize);
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.fileLength = this.randomAccessFile.length();
    }

    @Override
    protected Long getFileLength() {
        return this.fileLength;
    }

    @Override
    protected InputStream createSegment() throws IOException {
        return new FixedLengthInputStream(
            Channels.newInputStream(this.randomAccessFile.getChannel().position(currentSegment * segmentationSize)), segmentationSize);
    }

    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
    }
}

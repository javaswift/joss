package org.javaswift.joss.instructions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SegmentationPlanByteArray extends SegmentationPlan {

    private byte[] data;

    private int fileLength;

    public SegmentationPlanByteArray(byte[] data, long segmentationSize) {
        super(segmentationSize);
        this.data = data;
        this.fileLength = this.data.length;
    }

    @Override
    protected Long getFileLength() {
        return (long)this.fileLength;
    }

    @Override
    protected InputStream createSegment() throws IOException {
        return new ByteArrayInputStream(data, currentSegment.intValue() * segmentationSize.intValue(), segmentationSize.intValue());
    }

    @Override
    public void close() throws IOException { /* ignore */ }
}

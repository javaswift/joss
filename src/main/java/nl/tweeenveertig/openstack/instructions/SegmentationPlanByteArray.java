package nl.tweeenveertig.openstack.instructions;

import java.io.*;

public class SegmentationPlanByteArray extends SegmentationPlan {

    private byte[] data;

    private int fileLength;

    public SegmentationPlanByteArray(byte[] data, long segmentationSize) {
        super(segmentationSize);
        this.data = data;
        this.fileLength = this.data.length;
    }

    @Override
    protected boolean segmentAvailable() {
        return currentSegment * segmentationSize <= fileLength;
    }

    @Override
    protected InputStream createSegment() throws IOException {
        return new ByteArrayInputStream(data, currentSegment.intValue() * segmentationSize.intValue(), segmentationSize.intValue());
    }

    @Override
    public void close() throws IOException { /* ignore */ }
}

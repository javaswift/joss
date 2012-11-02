package nl.tweeenveertig.openstack.model;

import java.io.*;
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
    protected boolean segmentAvailable() {
        return currentSegment * segmentationSize <= fileLength;
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

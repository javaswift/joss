package nl.tweeenveertig.openstack.instructions;

import java.io.IOException;
import java.io.InputStream;

/**
* The Segmentation plan provides a way to access the underlying data as separate InputStreams
* for the purpose of uploading in several segments.
*/
public abstract class SegmentationPlan {

    protected Long segmentationSize;

    protected Long currentSegment = 0L;

    public Long getSegmentNumber() {
        return this.currentSegment;
    }

    public SegmentationPlan(Long segmentationSize) {
        this.segmentationSize = segmentationSize;
    }

    public InputStream getNextSegment() throws IOException {
        if (!segmentAvailable()) {
            return null;
        }
        InputStream segment = createSegment();
        this.currentSegment++;
        return segment;
    }

    /**
    * Checks whether the current segment actually exists
    * @return true if the segment can be read
    */
    protected abstract boolean segmentAvailable();

    /**
    * Creates an InputStream from the current segment
    * @return the inputstream
    * @throws IOException thrown if error occurs
    */
    protected abstract InputStream createSegment() throws IOException;

    /**
    * Closes the underlying File, if appropriate
    * @throws IOException thrown if error occurs
    */
    public abstract void close() throws IOException;

}

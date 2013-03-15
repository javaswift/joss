package org.javaswift.joss.instructions;

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
        if (done()) {
            return null;
        }
        InputStream segment = createSegment();
        this.currentSegment++;
        return segment;
    }

    /**
    * Checks whether all segments have been done
    * @return true if the segment can be read
    */
    protected boolean done() {
        return currentSegment * segmentationSize > getFileLength();
    }

    /**
    * Returns the file length of the object
    * @return file length of the object
    */
    protected abstract Long getFileLength();

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

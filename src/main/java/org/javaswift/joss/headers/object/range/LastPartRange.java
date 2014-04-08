package org.javaswift.joss.headers.object.range;

/**
 * Take the last bytes of the object with a length equal to 'lastBytes'
 */
public class LastPartRange extends AbstractRange {

    public LastPartRange(int lastBytes) {
        super(-1, lastBytes);
    }

    @Override
    public long getFrom(int byteArrayLength) {
        return byteArrayLength - length;
    }

    @Override
    public long getTo(int byteArrayLength) {
        return byteArrayLength;
    }
}

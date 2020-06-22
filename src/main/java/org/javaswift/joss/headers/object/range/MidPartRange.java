package org.javaswift.joss.headers.object.range;

/**
 * Take the object starting at 'startPos' and ending at 'endPos'
 */
public class MidPartRange extends AbstractRange {

    public MidPartRange(long startPos, long endPos) {
        super(startPos, endPos);
    }

    @Override
    public long getFrom(int byteArrayLength) {
        return offset;
    }

    @Override
    public long getTo(int byteArrayLength) {
        return length;
    }
}

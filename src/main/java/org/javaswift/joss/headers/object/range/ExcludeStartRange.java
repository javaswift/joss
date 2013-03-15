package org.javaswift.joss.headers.object.range;

/**
 * Take the entire object, starting from offset. The bytes before offset are ignored
 */
public class ExcludeStartRange extends AbstractRange {

    public ExcludeStartRange(int offset) {
        super(offset, -1);
    }

    @Override
    public int getFrom(int byteArrayLength) {
        return this.offset;
    }

    @Override
    public int getTo(int byteArrayLength) {
        return byteArrayLength;
    }
}

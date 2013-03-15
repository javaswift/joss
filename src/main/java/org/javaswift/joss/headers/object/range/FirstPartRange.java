package org.javaswift.joss.headers.object.range;

/**
 * Take the first bytes of the object until -- ie not including -- position 'until'
 */
public class FirstPartRange extends AbstractRange {
    public FirstPartRange(int until) {
        super(0, until);
    }

    @Override
    public int getFrom(int byteArrayLength) {
        return 0;
    }

    @Override
    public int getTo(int byteArrayLength) {
        return length;
    }
}

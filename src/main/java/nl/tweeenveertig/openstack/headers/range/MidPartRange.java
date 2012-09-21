package nl.tweeenveertig.openstack.headers.range;

/**
 * Take the object starting at 'startPos' and ending at 'endPos'
 */
public class MidPartRange extends AbstractRange {

    public MidPartRange(long startPos, long endPos) {
        super(startPos, endPos);
    }
}

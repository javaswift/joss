package nl.tweeenveertig.openstack.headers.range;

/**
 * Take the last bytes of the object with a length equal to 'lastBytes'
 */
public class LastPartRange extends AbstractRange {

    public LastPartRange(long lastBytes) {
        super(-1, lastBytes);
    }

}

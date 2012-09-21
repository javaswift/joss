package nl.tweeenveertig.openstack.command.object.range;

/**
 * Offers the option to return not the entire object, but only a designated part. The various options are
 * <ul>
 *     <li>{@link ExcludeStartRange} - take the entire object, expect the first x bytes</li>
 *     <li>{@link FirstPartRange} - take the first x bytes of the object</li>
 *     <li>{@link MidPartRange} - take the object from offset for a certain length</li>
 *     <li>{@link LastPartRange} - take the last x bytes</li>
 * </ul>
 * Also see: http://docs.openstack.org/api/openstack-object-storage/1.0/content/retrieve-object.html
 */
public class AbstractRange {

    public String RANGE_HEADER_NAME = "Range";
    public String RANGE_HEADER_VALUE_PREFIX = "bytes: ";

    private long offset;

    private long length;

    protected AbstractRange(long offset, long length) {
        this.offset = offset;
        this.length = length;
    }

    public String generateHeaderValue() {
        return
            RANGE_HEADER_VALUE_PREFIX+
                (offset >= 0 ? Long.toString(offset) : "") +
                "-" +
                (length >= 0 ? Long.toString(length) : "");
    }
}

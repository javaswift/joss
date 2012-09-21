package nl.tweeenveertig.openstack.command.object.range;

/**
 * Take the entire object, starting from offset. The bytes before offset are ignored
 */
public class ExcludeStartRange extends AbstractRange {

    public ExcludeStartRange(long offset) {
        super(offset, -1);
    }
}

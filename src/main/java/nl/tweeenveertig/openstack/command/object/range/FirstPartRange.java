package nl.tweeenveertig.openstack.command.object.range;

/**
 * Take the first bytes of the object until position 'until'
 */
public class FirstPartRange extends AbstractRange {
    public FirstPartRange(long until) {
        super(0, until);
    }
}

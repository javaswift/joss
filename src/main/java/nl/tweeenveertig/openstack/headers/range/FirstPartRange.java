package nl.tweeenveertig.openstack.headers.range;

/**
 * Take the first bytes of the object until -- ie not including -- position 'until'
 */
public class FirstPartRange extends AbstractRange {
    public FirstPartRange(long until) {
        super(0, until - 1);
    }
}

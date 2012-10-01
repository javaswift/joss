package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.headers.SimpleHeader;

public abstract class AbstractIfMatch extends SimpleHeader {

    public AbstractIfMatch(String value) {
        super(value);
    }
}

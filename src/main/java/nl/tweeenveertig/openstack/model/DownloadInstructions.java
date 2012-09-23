package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.range.AbstractRange;

public class DownloadInstructions {

    private AbstractRange range;

    public DownloadInstructions setRange(AbstractRange range) {
        this.range = range;
        return this;
    }

    public AbstractRange getRange() {
        return this.range;
    }

}

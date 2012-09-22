package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.range.AbstractRange;
import org.apache.http.HttpEntity;

public class DownloadInstructions {

    private HttpEntity entity;

    private AbstractRange range;

    public DownloadInstructions setRange(AbstractRange range) {
        this.range = range;
        return this;
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public AbstractRange getRange() {
        return this.range;
    }

}

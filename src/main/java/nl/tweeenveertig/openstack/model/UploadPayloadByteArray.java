package nl.tweeenveertig.openstack.model;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

public class UploadPayloadByteArray extends UploadPayload {

    private byte[] bytes;

    public UploadPayloadByteArray(final byte[] bytes) {
        this.bytes = bytes;
    }

    public HttpEntity getEntity() {
        return new ByteArrayEntity(this.bytes);
    }

    @Override
    public boolean mustBeSegmented(Long segmentationSize) {
        return this.bytes.length > segmentationSize;
    }

}

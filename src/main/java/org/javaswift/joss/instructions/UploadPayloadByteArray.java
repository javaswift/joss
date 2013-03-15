package org.javaswift.joss.instructions;

import org.javaswift.joss.headers.object.Etag;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;

public class UploadPayloadByteArray extends UploadPayload {

    private byte[] bytes;

    public UploadPayloadByteArray(final byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public HttpEntity getEntity() {
        return new ByteArrayEntity(this.bytes);
    }

    @Override
    public boolean mustBeSegmented(Long segmentationSize) {
        return this.bytes.length > segmentationSize;
    }

    @Override
    public Etag getEtag() throws IOException {
        return new Etag(getEntity().getContent());
    }

    @Override
    public SegmentationPlan getSegmentationPlan(Long segmentationSize) throws IOException {
        return new SegmentationPlanByteArray(this.bytes, segmentationSize);
    }

}

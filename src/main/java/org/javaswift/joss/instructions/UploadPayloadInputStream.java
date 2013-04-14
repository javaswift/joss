package org.javaswift.joss.instructions;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.javaswift.joss.headers.object.Etag;

import java.io.IOException;
import java.io.InputStream;

public class UploadPayloadInputStream extends UploadPayload {

    private InputStream inputStream;

    public UploadPayloadInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public HttpEntity getEntity() {
        return new InputStreamEntity(inputStream, -1);
    }

    @Override
    public boolean mustBeSegmented(Long segmentationSize) {
        return false;
    }

    @Override
    public Etag getEtag() throws IOException {
        return null;
    }

    @Override
    public SegmentationPlan getSegmentationPlan(Long segmentationSize) throws IOException {
        throw new UnsupportedOperationException("This operation is not support for InputStream");
    }

}

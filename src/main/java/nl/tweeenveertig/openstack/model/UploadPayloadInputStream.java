package nl.tweeenveertig.openstack.model;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;

import java.io.InputStream;

public class UploadPayloadInputStream extends UploadPayload {

    private InputStream inputStream;

    public UploadPayloadInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public HttpEntity getEntity() {
        return new InputStreamEntity(inputStream, -1);
    }

    @Override
    public boolean mustBeSegmented(Long segmentationSize) {
        return false;
    }

}

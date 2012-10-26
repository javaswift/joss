package nl.tweeenveertig.openstack.model;

import org.apache.http.HttpEntity;
import org.apache.http.entity.FileEntity;

import java.io.File;

public class UploadPayloadFile extends UploadPayload {

    private File file;

    public UploadPayloadFile(final File file) {
        this.file = file;
    }

    public HttpEntity getEntity() {
        return new FileEntity(this.file);
    }

    @Override
    public boolean mustBeSegmented(Long segmentationSize) {
        return this.file.length() > segmentationSize;
    }

}

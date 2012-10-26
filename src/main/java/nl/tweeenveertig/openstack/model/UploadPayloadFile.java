package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.object.Etag;
import org.apache.http.HttpEntity;
import org.apache.http.entity.FileEntity;

import java.io.File;
import java.io.IOException;

public class UploadPayloadFile extends UploadPayload {

    private File file;

    public UploadPayloadFile(final File file) {
        this.file = file;
    }

    @Override
    public HttpEntity getEntity() {
        return new FileEntity(this.file);
    }

    @Override
    public boolean mustBeSegmented(Long segmentationSize) {
        return this.file.length() > segmentationSize;
    }

    @Override
    public Etag getEtag() throws IOException {
        return new Etag(getEntity().getContent());
    }

}

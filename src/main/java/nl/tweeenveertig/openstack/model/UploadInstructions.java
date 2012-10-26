package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.object.DeleteAfter;
import nl.tweeenveertig.openstack.headers.object.DeleteAt;
import nl.tweeenveertig.openstack.headers.object.ObjectManifest;
import org.apache.http.HttpEntity;

import java.io.File;
import java.io.InputStream;

public class UploadInstructions {

    private UploadPayload uploadPayload;

    private String md5;

    private String contentType;

    private DeleteAfter deleteAfter;

    private DeleteAt deleteAt;

    private ObjectManifest objectManifest;

    private Long segmentationSize = 5368709120L; // 5 GB, max object size

    public UploadInstructions(File fileToUpload) {
        this.uploadPayload = new UploadPayloadFile(fileToUpload);
    }

    public UploadInstructions(InputStream inputStream) {
        this.uploadPayload = new UploadPayloadInputStream(inputStream);
    }

    public UploadInstructions(byte[] fileToUpload) {
        this.uploadPayload = new UploadPayloadByteArray(fileToUpload);
    }

    public boolean requiresSegmentation() {
        return this.uploadPayload.mustBeSegmented(this.segmentationSize);
    }

    public UploadInstructions setObjectManifest(ObjectManifest objectManifest) {
        this.objectManifest = objectManifest;
        return this;
    }

    public UploadInstructions setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public UploadInstructions setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public UploadInstructions setDeleteAfter(DeleteAfter deleteAfter) {
        this.deleteAfter = deleteAfter;
        return this;
    }

    public UploadInstructions setDeleteAt(DeleteAt deleteAt) {
        this.deleteAt = deleteAt;
        return this;
    }

    public UploadInstructions setSegmentationSize(Long segmentationSize) {
        this.segmentationSize = segmentationSize;
        return this;
    }

    public HttpEntity getEntity() {
        return this.uploadPayload.getEntity();
    }

    public ObjectManifest getObjectManifest() {
        return this.objectManifest;
    }

    public String getMd5() {
        return this.md5;
    }

    public String getContentType() {
        return this.contentType;
    }

    public DeleteAt getDeleteAt() {
        return this.deleteAt;
    }

    public DeleteAfter getDeleteAfter() {
        return this.deleteAfter;
    }

    public Long getSegmentationSize() {
        return this.segmentationSize;
    }

}

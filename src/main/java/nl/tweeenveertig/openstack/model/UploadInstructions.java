package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.object.DeleteAfter;
import nl.tweeenveertig.openstack.headers.object.DeleteAt;
import nl.tweeenveertig.openstack.headers.object.Etag;
import nl.tweeenveertig.openstack.headers.object.ObjectManifest;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import org.apache.http.HttpEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UploadInstructions {

    public static Long MAX_SEGMENTATION_SIZE = 5368709120L; // 5 GB, max object size

    private UploadPayload uploadPayload;

    private String md5;

    private ObjectContentType contentType;

    private DeleteAfter deleteAfter;

    private DeleteAt deleteAt;

    private ObjectManifest objectManifest;

    private Long segmentationSize = MAX_SEGMENTATION_SIZE;

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
        return setContentType(new ObjectContentType(contentType));
    }

    public UploadInstructions setContentType(ObjectContentType contentType) {
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

    public Long getSegmentationSize() {
        return this.segmentationSize;
    }

    public String getMd5() {
        return this.md5;
    }

    public Etag getEtag() throws IOException {
        return getMd5() == null ? this.uploadPayload.getEtag() : new Etag(getMd5());
    }

    public ObjectManifest getObjectManifest() {
        return this.objectManifest;
    }

    public ObjectContentType getContentType() {
        return this.contentType;
    }

    public DeleteAt getDeleteAt() {
        return this.deleteAt;
    }

    public DeleteAfter getDeleteAfter() {
        return this.deleteAfter;
    }

}

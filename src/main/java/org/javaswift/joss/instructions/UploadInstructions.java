package org.javaswift.joss.instructions;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.object.DeleteAfter;
import org.javaswift.joss.headers.object.DeleteAt;
import org.javaswift.joss.headers.object.Etag;
import org.javaswift.joss.headers.object.ObjectManifest;
import org.javaswift.joss.headers.object.ObjectContentType;
import org.apache.http.HttpEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
* Specific instructions for uploading files. You can control the following aspects:
* <ul>
*     <li>md5 (aka etag); the hash of the file, which is useful when uploading from an
*           InputStream, since it is not possible (desirable) to calculate a new MD5</li>
*     <li>contentType; if you know better what content type the file is then the file
*           extension matcher or content-sniffers, use this</li>
*     <li>deleteAfter; order a file to be deleted after a certain number of seconds</li>
*     <li>deleteAt; order a file to be deleted at a specific date</li>
*     <li>objectManifest; if the file is to be a manifest for a number of segmented file
*           that together comprise a single large file, use this</li>
*     <li>segmentationSize; supply a custom segmentation size which determines how large
*           a single segment can be</li>
* </ul>
*/
public class UploadInstructions {

    /** Maximum segmentation size allowed by the ObjectStore. */
    public static Long MAX_SEGMENTATION_SIZE = 5368709120L; // 5 GB, max object size

    /** Consists of either the File, InputStream or byte[] */
    private UploadPayload uploadPayload;

    /** MD5 hash or etag of the payload */
    private String md5;

    /** Content-Type of the payload */
    private ObjectContentType contentType;

    /** After how many seconds the object must be deleted from the ObjectStore */
    private DeleteAfter deleteAfter;

    /** At which time the object must be deleted from the ObjectStore */
    private DeleteAt deleteAt;

    /**
    * Determines whether the object is a manifest to a series of segments, together
    * comprising a single large file. Also says where the segments are located.
    */
    private ObjectManifest objectManifest;

    /** Size at which a file must be segmented into smaller pieces */
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

    /**
     * Facade method for checking out the payload to see if must be segmented. Used
     * internally.
     * @return true if segmentation is required
     */
    public boolean requiresSegmentation() {
        return this.uploadPayload.mustBeSegmented(this.segmentationSize);
    }

    /**
    * Facade method for determining the segmentation plan of the payload on the
    * basis of the known segmentation size. Used internally.
    * @return full-fledged segmentation plan
    */
    public SegmentationPlan getSegmentationPlan() {
        try {
            return this.uploadPayload.getSegmentationPlan(this.segmentationSize);
        } catch (IOException err) {
            throw new CommandException("Unable to set up segmentation plan", err);
        }
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

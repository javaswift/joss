package nl.tweeenveertig.openstack.model;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;

import java.io.File;
import java.io.InputStream;

public class UploadInstructions {

    private HttpEntity entity;

    private String md5;

    public UploadInstructions(File fileToUpload) {
        this.entity = new FileEntity(fileToUpload);
    }

    public UploadInstructions(InputStream inputStream) {
        this.entity = new InputStreamEntity(inputStream, -1);
    }

    public UploadInstructions(byte[] fileToUpload) {
        this.entity = new ByteArrayEntity(fileToUpload);
    }

    public UploadInstructions setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public String getMd5() {
        return this.md5;
    }
}

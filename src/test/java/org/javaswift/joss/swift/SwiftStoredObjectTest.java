package org.javaswift.joss.swift;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import mockit.Expectations;
import mockit.Mocked;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.javaswift.joss.headers.object.DeleteAfter;
import org.javaswift.joss.headers.object.DeleteAt;
import org.javaswift.joss.instructions.UploadInstructions;
import org.junit.Test;


public class SwiftStoredObjectTest {

    @Test
    public void uploadObject() {
        UploadInstructions instructions = new UploadInstructions(new byte[] { 0x01, 0x02, 0x03});
        SwiftStoredObject object = new SwiftStoredObject("alpha");
        object.uploadObject(instructions);
        assertEquals(3, object.getContent().length);
    }

    @Test
    public void uploadObject_withDeleteAt_populatesDeleteAt() {
        UploadInstructions instructions = new UploadInstructions(new byte[] { 0x01, 0x02, 0x03})
                .setDeleteAfter(new DeleteAfter(20));
        SwiftStoredObject object = new SwiftStoredObject("alpha");
        object.uploadObject(instructions);
        assertNotNull(object.getInfo().getDeleteAt());
    }

    @Test
    public void uploadObject_withDeleteAfter_populatesDeleteAt() {
        UploadInstructions instructions = new UploadInstructions(new byte[] { 0x01, 0x02, 0x03})
                .setDeleteAt(new DeleteAt(System.currentTimeMillis() + 10000));
        SwiftStoredObject object = new SwiftStoredObject("alpha");
        object.uploadObject(instructions);
        assertNotNull(object.getInfo().getDeleteAt());
    }

    @Test
    public void uploadObjectThrowsException() throws IOException {
        new Expectations() {
            @Mocked(stubOutClassInitialization = true) IOUtils unused; {
            IOUtils.toByteArray((InputStream)any); result = new IOException();
        }};
        SwiftStoredObject object = new SwiftStoredObject("alpha");
        SwiftResult result = object.uploadObject(new UploadInstructions(new byte[] { 0x01, 0x02, 0x03 }));
        assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, result.getStatus());
    }

    @Test
    public void getCoverage() {
        SwiftStoredObject object = new SwiftStoredObject("");
        object.metadataSetFromHeaders();
        object.getBareName();
        object.getAsObject();
        object.getAsDirectory();
        object.isDirectory();
        object.isObject();
    }

}

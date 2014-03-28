package org.javaswift.joss.swift;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.javaswift.joss.client.mock.ContainerMock;
import org.javaswift.joss.instructions.UploadInstructions;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

public class SwiftStoredObjectTest {

    @Test
    public void uploadObject() {
        UploadInstructions instructions = new UploadInstructions(new byte[] { 0x01, 0x02, 0x03});
        SwiftStoredObject object = new SwiftStoredObject("alpha");
        object.uploadObject(instructions);
        assertEquals(3, object.getContent().length);
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

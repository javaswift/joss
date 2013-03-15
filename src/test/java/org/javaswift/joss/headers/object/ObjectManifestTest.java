package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ObjectManifestTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectManifest("images/extremely-big-file.dat"));
    }

    @Test
    public void getContainerName() {
        ObjectManifest manifest = new ObjectManifest("segment_container/segmented_object.txt");
        assertEquals("segment_container", manifest.getContainerName());
    }

    @Test
    public void getObjectPrefix() {
        ObjectManifest manifest = new ObjectManifest("segment_container/segmented_object.txt");
        assertEquals("segmented_object.txt", manifest.getObjectPrefix());
    }
}

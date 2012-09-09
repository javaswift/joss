package nl.t42.openstack.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MimeTypesTest {

    @Test
    public void testMimeTypes() {
        assertEquals("application/andrew-inset", MimeTypeMap.getContentType("somefile.ez"));
        assertEquals("video/x-f4v", MimeTypeMap.getContentType("somefile.f4v"));
        assertEquals("test/42", MimeTypeMap.getContentType("somefile.42")); // added fake format to test reading of mime.types file
    }
}

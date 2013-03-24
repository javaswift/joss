package org.javaswift.joss.command.impl.account;

import org.javaswift.joss.command.shared.account.ContainerListElement;
import org.javaswift.joss.util.ClasspathTemplateResource;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class ContainerListingTest {

    @Test
    public void testUnmarshallingSingleElement() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-container-listing.json").loadTemplate();
        ObjectMapper mapper = new ObjectMapper();
        ContainerListElement listing = mapper.readValue(jsonString, ContainerListElement.class);
        assertEquals("Amersfoort", listing.name);
        assertEquals(48, listing.count);
        assertEquals(1028296, listing.bytes);
    }

    @Test
    public void testUnmarshallingList() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-container-list.json").loadTemplate();
        ObjectMapper mapper = new ObjectMapper();
        ContainerListElement[] list = mapper.readValue(jsonString, ContainerListElement[].class);
        assertEquals(4, list.length);
    }

}

package org.javaswift.joss.command.impl.account;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.javaswift.joss.command.shared.account.ContainerListElement;
import org.javaswift.joss.util.ClasspathTemplateResource;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class ContainerListingTest {

    @Test
    public void testUnmarshallingSingleElement() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-container-listing.json").loadTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);
        ContainerListElement listing = mapper.readValue(jsonString, ContainerListElement.class);
        assertEquals("Amersfoort", listing.name);
        assertEquals(48, listing.count);
        assertEquals(1028296, listing.bytes);
    }

    @Test
    public void testUnmarshallingSingleElementSwift2130() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-container-listing-swift2.13.0.json").loadTemplate();
        ObjectMapper mapper = new ObjectMapper();
        ContainerListElement listing = mapper.readValue(jsonString, ContainerListElement.class);
        assertEquals("Amersfoort", listing.name);
        assertEquals(48, listing.count);
        assertEquals(1028296, listing.bytes);
        assertEquals("2013-11-19T20:08:13.283452", listing.lastModified);
    }

    @Test
    public void testUnmarshallingList() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-container-list.json").loadTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);
        ContainerListElement[] list = mapper.readValue(jsonString, ContainerListElement[].class);
        assertEquals(4, list.length);
    }

}

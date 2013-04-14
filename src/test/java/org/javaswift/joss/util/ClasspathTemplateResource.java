package org.javaswift.joss.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ClasspathTemplateResource {

    private final String templateResource;

    public ClasspathTemplateResource(String resource) {
        this.templateResource = resource;
    }

    public String loadTemplate() {
        try {
            InputStream inputStream = ClasspathTemplateResource.class.getResourceAsStream(templateResource);
            if (inputStream == null) {
                throw new IOException("Template '" + templateResource + "' does not exist.");
            }
            try {
                return IOUtils.toString(inputStream, "UTF-8");
            } finally {
                inputStream.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Unable to load template " + templateResource, ex);
        }
    }
}
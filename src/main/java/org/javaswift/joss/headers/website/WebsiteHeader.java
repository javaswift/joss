package org.javaswift.joss.headers.website;

import org.javaswift.joss.headers.container.ContainerMetadata;

public abstract class WebsiteHeader extends ContainerMetadata {

    public static final String STATIC_WEBSITE_HEADER = "Web-";

    private String metadataHeader;

    public WebsiteHeader(String name, String value) {
        super(STATIC_WEBSITE_HEADER + name, value);
        this.metadataHeader = STATIC_WEBSITE_HEADER + name;
    }

    public String getMetadataHeader() {
        return this.metadataHeader;
    }

}

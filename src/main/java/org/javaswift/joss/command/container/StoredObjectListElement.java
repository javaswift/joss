package org.javaswift.joss.command.container;

import org.codehaus.jackson.annotate.JsonProperty;

public class StoredObjectListElement {

    public String name;

    public String hash;

    public long bytes;

    @JsonProperty(value="content_type")
    public String contentType;

    @JsonProperty(value="last_modified")
    public String lastModified;

}

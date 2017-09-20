package org.javaswift.joss.command.shared.account;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContainerListElement {

    public String name;

    public int count;

    public long bytes;

    @JsonProperty(value="last_modified")
    public String lastModified;
}

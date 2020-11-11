package org.javaswift.joss.command.shared.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContainerListElement {

    public String name;

    public int count;

    public long bytes;

    @JsonProperty(value="last_modified")
    public String lastModified;
}

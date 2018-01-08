package org.javaswift.joss.command.shared.identity.bulkdelete;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorObject {

    public String name;

    public String status;

}

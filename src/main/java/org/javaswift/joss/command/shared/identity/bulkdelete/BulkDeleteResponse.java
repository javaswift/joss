package org.javaswift.joss.command.shared.identity.bulkdelete;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkDeleteResponse {

    @JsonProperty("Number Deleted")
    public long          numberDeleted;

    @JsonProperty("Number Not Found")
    public long          numberNotFound;

    @JsonProperty("Errors")
    public ErrorObject[] errors;
}

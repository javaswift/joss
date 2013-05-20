package org.javaswift.joss.command.shared.identity.access;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;

@JsonRootName(value="access")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessNoTenant extends AbstractAccess {

    @Override
    public boolean isTenantSupplied() {
        return false;
    }

    @Override
    public String getTempUrlPrefix(TempUrlHashPrefixSource tempUrlHashPrefixSource) {
        return "";
    }

    @Override
    protected EndPoint determineCurrentEndPoint() {
        return null;  // Unused
    }

}

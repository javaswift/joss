package org.javaswift.joss.command.shared.identity.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
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

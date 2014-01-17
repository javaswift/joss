package org.javaswift.joss.command.shared.identity.access;

import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.model.Access;

public class AccessBasic implements Access {

    private String url;
    private String token;

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    @Override
    public void setPreferredRegion(String preferredRegion) {}

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getInternalURL() {
        return url;
    }

    @Override
    public String getPublicURL() {
        return url;
    }

    @Override
    public boolean isTenantSupplied() {
        return true;
    }

    @Override
    public String getTempUrlPrefix(TempUrlHashPrefixSource tempUrlHashPrefixSource) {
        return url.endsWith("/") ? url.substring(0, url.length()-1) : url;
    }
}

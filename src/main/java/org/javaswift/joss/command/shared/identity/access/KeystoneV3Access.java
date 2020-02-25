package org.javaswift.joss.command.shared.identity.access;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.model.Access;

public class KeystoneV3Access implements Access {

    private final String token;
    private final Collection<EndPoint> endPoints;

    private EndPoint currentEndpoint;

    public KeystoneV3Access(String token, JsonNode response) {
        this.token = token;
        this.endPoints = getEndPoints(response);
        this.currentEndpoint = getEndPoint(null);
    }

    @Override
    public void setPreferredRegion(String preferredRegion) {
        this.currentEndpoint = getEndPoint(preferredRegion);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getInternalURL() {
        return currentEndpoint.internalURL;
    }

    @Override
    public String getPublicURL() {
        return currentEndpoint.publicURL;
    }

    /**
     * @return Always true, v3 doesn't use the tenant ID for auth
     */
    @Override
    public boolean isTenantSupplied() {
        return true;
    }

    @Override
    public String getTempUrlPrefix(TempUrlHashPrefixSource tempUrlHashPrefixSource) {
        final String tempUrlPrefix;
        if (tempUrlHashPrefixSource == null) {
            tempUrlPrefix = "";
        } else if (tempUrlHashPrefixSource == TempUrlHashPrefixSource.PUBLIC_URL_PATH) {
            tempUrlPrefix = TempUrlHashPrefixSource.getPath(currentEndpoint.publicURL);
        } else if (tempUrlHashPrefixSource == TempUrlHashPrefixSource.INTERNAL_URL_PATH) {
            tempUrlPrefix = TempUrlHashPrefixSource.getPath(currentEndpoint.internalURL);
        } else {
            tempUrlPrefix = TempUrlHashPrefixSource.getPath(currentEndpoint.adminURL);
        }
        return tempUrlPrefix.endsWith("/") ? tempUrlPrefix.substring(0, tempUrlPrefix.length()-1) : tempUrlPrefix;
    }

    private EndPoint getEndPoint(String region) {
        if (endPoints.isEmpty()) {
            throw new IllegalStateException("No endpoints available");
        }

        if (region == null) {
            return endPoints.iterator().next();
        }

        for (EndPoint endPoint : endPoints) {
            if (region.equals(endPoint.region)) {
                return endPoint;
            }
        }

        throw new IllegalArgumentException("No endpoint for region: " + region);
    }

    private Collection<EndPoint> getEndPoints(JsonNode response) {
        Map<String, EndPoint> result = new LinkedHashMap<String, EndPoint>();

        JsonNode token = response.get("token");
        for (JsonNode catalog : token.get("catalog")) {
            if ("object-store".equals(catalog.get("type").asText())) {
                for (JsonNode endpointNode : catalog.get("endpoints")) {
                    String interfaceType = endpointNode.get("interface").asText();
                    String url = endpointNode.get("url").asText();
                    String region = endpointNode.get("region").asText();;

                    EndPoint endPoint = result.get(region);

                    if (endPoint == null) {
                        endPoint = new EndPoint();
                        endPoint.region = region;
                        result.put(region, endPoint);
                    }

                    if ("public".equals(interfaceType)) {
                        endPoint.publicURL = url;
                    } else if ("admin".equals(interfaceType)) {
                        endPoint.adminURL = url;
                    } else if ("internal".equals(interfaceType)) {
                        endPoint.internalURL = url;
                    }
                }
            }
        }

        return result.values();
    }
}

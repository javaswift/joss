package org.javaswift.joss.command.shared.identity.access;

public class EndPointBuilder {

    private EndPoint endPoint = new EndPoint();

    public EndPointBuilder setAdminURL(String adminURL) {
        endPoint.adminURL = adminURL;
        return this;
    }

    public EndPointBuilder setPublicURL(String publicURL) {
        endPoint.publicURL = publicURL;
        return this;
    }

    public EndPointBuilder setInternalURL(String internalURL) {
        endPoint.internalURL = internalURL;
        return this;
    }

    public EndPointBuilder setRegion(String region) {
        endPoint.region = region;
        return this;
    }

    public EndPoint getEndPoint() {
        return this.endPoint;
    }

    public static EndPoint createEndPoint(String adminURL, String publicURL, String internalURL, String region) {
        return new EndPointBuilder()
                .setAdminURL(adminURL)
                .setInternalURL(internalURL)
                .setPublicURL(publicURL)
                .setRegion(region)
                .getEndPoint();
    }

}

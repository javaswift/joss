package org.javaswift.joss.command.impl.core.httpstatus;

public class HttpStatusMatch implements HttpStatusMatcher {

    private int matchCode;

    public HttpStatusMatch(final int matchCode) {
        this.matchCode = matchCode;
    }

    public boolean matches(int statusCode) {
        return statusCode == this.matchCode;
    }
}

package org.javaswift.joss.command.core.httpstatus;

public interface HttpStatusMatcher {

    public boolean matches(int statusCode);
}

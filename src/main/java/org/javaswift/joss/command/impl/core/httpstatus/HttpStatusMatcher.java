package org.javaswift.joss.command.impl.core.httpstatus;

public interface HttpStatusMatcher {

    public boolean matches(int statusCode);
}

package org.javaswift.joss.client.mock;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.CommandExceptionError;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class MockUserStore {

    public static final Logger LOG = LoggerFactory.getLogger(MockUserStore.class);

    private Map<String, String> users = new TreeMap<String, String>();

    public void authenticate(String username, String password) {
        String expectedPassword = users.get(username);
        if (expectedPassword == null || !expectedPassword.equals(password)) {
            LOG.warn("JOSS / Failed to authenticate with user '"+username+"'");
            throw new CommandException(HttpStatus.SC_UNAUTHORIZED, CommandExceptionError.UNAUTHORIZED);
        }
    }

    public void addUser(String username, String password) {
        this.users.put(username, password);
    }

}

package org.javaswift.joss.swift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class MockUserStore {

    public static final Logger LOG = LoggerFactory.getLogger(MockUserStore.class);

    private Map<String, String> users = new TreeMap<String, String>();

    public boolean authenticate(String tenant, String username, String password) {
        String expectedPassword = users.get(username);
        if (expectedPassword == null || !expectedPassword.equals(password)) {
            LOG.warn("JOSS / Failed to authenticate with user '"+username+"'");
            return false;
        }
        return true;
    }

    public void addUser(String username, String password) {
        this.users.put(username, password);
    }

}

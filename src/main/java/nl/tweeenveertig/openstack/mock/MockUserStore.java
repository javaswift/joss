package nl.tweeenveertig.openstack.mock;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.http.HttpStatus;

import java.util.Map;
import java.util.TreeMap;

public class MockUserStore {

    private Map<String, String> users = new TreeMap<String, String>();

    public void authenticate(String username, String password) {
        String expectedPassword = users.get(username);
        if (expectedPassword == null || !expectedPassword.equals(password)) {
            throw new CommandException(HttpStatus.SC_UNAUTHORIZED, CommandExceptionError.UNAUTHORIZED);
        }
    }

    public void addUser(String username, String password) {
        this.users.put(username, password);
    }

}

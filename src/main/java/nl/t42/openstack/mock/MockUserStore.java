package nl.t42.openstack.mock;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
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

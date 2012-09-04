package nl.t42.openstack.client;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.mock.MockAccount;
import nl.t42.openstack.mock.MockUserStore;
import nl.t42.openstack.model.*;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.Map;

public class OpenStackClientInMemory implements OpenStackClient {

    private MockUserStore users = new MockUserStore();

    private MockAccount account = new MockAccount();

    private boolean authenticated = false;

    public void authenticate(String username, String password, String authUrl) {
        this.authenticated = false;
        users.authenticate(username, password);
        this.authenticated = true;
    }

    public AccountInformation getAccountInformation() {
        checkAuthentication();
        return null;  
    }

    public void setAccountInformation(Map<String, Object> metadata) {
        checkAuthentication();

    }

    public Container[] listContainers() {
        checkAuthentication();
        return new Container[0];
    }

    public void createContainer(Container container) {
        checkAuthentication();

    }

    public void deleteContainer(Container container) {
        checkAuthentication();

    }

    public void makeContainerPublic(Container container) {
        checkAuthentication();

    }

    public void makeContainerPrivate(Container container) {
        checkAuthentication();

    }

    public ContainerInformation getContainerInformation(Container container) {
        checkAuthentication();
        return null;
    }

    public void setContainerInformation(Container container, Map<String, Object> metadata) {
        checkAuthentication();

    }

    public StoreObject[] listObjects(Container container) {
        checkAuthentication();
        return new StoreObject[0];
    }

    public byte[] downloadObject(Container container, StoreObject object) {
        checkAuthentication();
        return new byte[0];
    }

    public void downloadObject(Container container, StoreObject object, File targetFile) {
        checkAuthentication();

    }

    public void uploadObject(Container container, StoreObject target, byte[] fileToUpload) {
        checkAuthentication();

    }

    public void uploadObject(Container container, StoreObject target, File fileToUpload) {
        checkAuthentication();

    }

    public ObjectInformation getObjectInformation(Container container, StoreObject object) {
        checkAuthentication();
        return null;
    }

    public void setObjectInformation(Container container, StoreObject object, Map<String, Object> metadata) {
        checkAuthentication();

    }

    public void deleteObject(Container container, StoreObject object) {
        checkAuthentication();

    }

    public void copyObject(Container sourceContainer, StoreObject sourceObject, Container targetContainer, StoreObject targetObject) {
        checkAuthentication();

    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    protected void checkAuthentication() {
        if (!isAuthenticated()) {
            throw new CommandException(HttpStatus.SC_UNAUTHORIZED, CommandExceptionError.UNAUTHORIZED);
        }
    }

    public MockUserStore getUsers() { return this.users; }
    public void setUsers(MockUserStore users) { this.users = users; }
    public void setAccount(MockAccount account) { this.account = account; }
}

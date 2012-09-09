package nl.t42.openstack.client;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.mock.MockAccount;
import nl.t42.openstack.mock.MockUserStore;
import nl.t42.openstack.model.*;
import org.apache.http.HttpStatus;

import java.io.*;
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
        return account.getInfo();
    }

    public void setAccountInformation(Map<String, Object> metadata) {
        checkAuthentication();
        account.setInfo(metadata);
    }

    public Container[] listContainers() {
        checkAuthentication();
        return account.listContainers();
    }

    public void createContainer(Container container) {
        checkAuthentication();
        account.createContainer(container);
    }

    public void deleteContainer(Container container) {
        checkAuthentication();
        account.deleteContainer(container);
    }

    public void makeContainerPublic(Container container) {
        checkAuthentication();
        account.getContainer(container).makeContainerPublic();
    }

    public void makeContainerPrivate(Container container) {
        checkAuthentication();
        account.getContainer(container).makeContainerPrivate();
    }

    public ContainerInformation getContainerInformation(Container container) {
        checkAuthentication();
        return account.getContainer(container).getInfo();
    }

    public void setContainerInformation(Container container, Map<String, Object> metadata) {
        checkAuthentication();
        account.getContainer(container).setInfo(metadata);
    }

    public StoreObject[] listObjects(Container container) {
        checkAuthentication();
        return account.getContainer(container).listObjects();
    }

    public byte[] downloadObject(Container container, StoreObject object) {
        checkAuthentication();
        return account.getContainer(container).getObject(object).getObject();
    }

    public void downloadObject(Container container, StoreObject object, File targetFile) {
        checkAuthentication();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new ByteArrayInputStream(downloadObject(container, object));
            os = new FileOutputStream(targetFile);
            byte[] buffer = new byte[65536];
            for (int length; (length = is.read(buffer)) > 0;) {
                os.write(buffer, 0, length);
            }
        } catch (IOException err) {
            throw new CommandException("IO Failure", err);
        } finally {
            if (os != null) try { os.close(); } catch (IOException logOrIgnore) {}
            if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
        }
    }

    public void uploadObject(Container container, StoreObject target, byte[] fileToUpload) {
        checkAuthentication();
        account.getContainer(container).getOrCreateObject(target).saveObject(target, fileToUpload);
    }

    public void uploadObject(Container container, StoreObject target, File fileToUpload) {
        checkAuthentication();
        InputStream is = null;
        OutputStream os = null;
        try {
            // When doing a mime type check, this would be the right place to do it
            is = new FileInputStream(fileToUpload);
            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[65536];
            for (int length; (length = is.read(buffer)) > 0;) {
                os.write(buffer, 0, length);
            }
            uploadObject(container, target, ((ByteArrayOutputStream)os).toByteArray());
        } catch (IOException err) {
            throw new CommandException("IO Failure", err);
        } finally {
            if (os != null) try { os.close(); } catch (IOException logOrIgnore) {}
            if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
        }
    }

    public ObjectInformation getObjectInformation(Container container, StoreObject object) {
        checkAuthentication();
        return account.getContainer(container).getObject(object).getInfo();
    }

    public void setObjectInformation(Container container, StoreObject object, Map<String, Object> metadata) {
        checkAuthentication();
        account.getContainer(container).getObject(object).setInfo(metadata);
    }

    public void deleteObject(Container container, StoreObject object) {
        checkAuthentication();
        account.getContainer(container).deleteObject(object);
    }

    public void copyObject(Container sourceContainer, StoreObject sourceObject, Container targetContainer, StoreObject targetObject) {
        checkAuthentication();
        account.copyObject(sourceContainer, sourceObject, targetContainer, targetObject);
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

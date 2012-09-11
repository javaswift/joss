package nl.tweeenveertig.openstack.client;

import nl.tweeenveertig.openstack.command.account.AccountInformationCommand;
import nl.tweeenveertig.openstack.command.account.AccountMetadataCommand;
import nl.tweeenveertig.openstack.command.account.ListContainersCommand;
import nl.tweeenveertig.openstack.command.container.*;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.command.object.*;
import nl.tweeenveertig.openstack.model.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public class OpenStackClientImpl implements OpenStackClient {

    private Access access;

    private HttpClient httpClient = new DefaultHttpClient();

    private boolean authenticated = false;

    public void authenticate(String username, String password, String authUrl) {

        this.access = null;
        this.authenticated = false;

        this.access = new AuthenticationCommand(httpClient, authUrl, username, password).call();
        this.authenticated = true;
    }

    public AccountInformation getAccountInformation() {
        return new AccountInformationCommand(httpClient, access).call();
    }

    public void setAccountInformation(Map<String, Object> metadata) {
        new AccountMetadataCommand(httpClient, access, metadata).call();
    }

    public Collection<Container> listContainers() {
        return new ListContainersCommand(httpClient, access).call();
    }

    public void createContainer(Container container) {
        new CreateContainerCommand(httpClient, access, container).call();
    }

    public void deleteContainer(Container container) {
        new DeleteContainerCommand(httpClient, access, container).call();
    }

    public void makeContainerPublic(Container container) {
        new ContainerRightsCommand(httpClient, access, container, true).call();
    }

    public void makeContainerPrivate(Container container) {
        new ContainerRightsCommand(httpClient, access, container, false).call();
    }

    public ContainerInformation getContainerInformation(Container container) {
        return new ContainerInformationCommand(httpClient, access, container).call();
    }

    public void setContainerInformation(Container container, Map<String, Object> metadata) {
        new ContainerMetadataCommand(httpClient, access, container, metadata).call();
    }

    public Collection<StoreObject> listObjects(Container container) {
        return new ListObjectsCommand(httpClient, access, container).call();
    }

    public InputStreamWrapper downloadObjectAsInputStream(Container container, StoreObject object) {
        return new DownloadObjectAsInputStreamCommand(httpClient, access, container, object).call();
    }

    public byte[] downloadObject(Container container, StoreObject object) {
        return new DownloadObjectAsByteArrayCommand(httpClient, access, container, object).call();
    }

    public void downloadObject(Container container, StoreObject object, File targetFile) {
        new DownloadObjectToFileCommand(httpClient, access, container, object, targetFile).call();
    }

    public void uploadObject(Container container, StoreObject target, InputStream inputStream) {
        new UploadObjectCommand(httpClient, access, container, target, inputStream).call();
    }

    public void uploadObject(Container container, StoreObject target, byte[] fileToUpload) {
        new UploadObjectCommand(httpClient, access, container, target, fileToUpload).call();
    }

    public void uploadObject(Container container, StoreObject target, File fileToUpload) {
        new UploadObjectCommand(httpClient, access, container, target, fileToUpload).call();
    }

    public ObjectInformation getObjectInformation(Container container, StoreObject object) {
        return new ObjectInformationCommand(httpClient, access, container, object).call();
    }

    public void setObjectInformation(Container container, StoreObject object, Map<String, Object> metadata) {
        new ObjectMetadataCommand(httpClient, access, container, object, metadata).call();
    }

    public void deleteObject(Container container, StoreObject object) {
        new DeleteObjectCommand(httpClient, access, container, object).call();
    }

    public void copyObject(Container sourceContainer, StoreObject sourceObject, Container targetContainer, StoreObject targetObject) {
        new CopyObjectCommand(httpClient, access, sourceContainer, sourceObject, targetContainer, targetObject).call();
    }

    public String getPublicURL(Container container, StoreObject object) {
        return access.getPublicURL() + "/" + container.getName() + "/" + object.getName();
    }

    public boolean isAuthenticated() { return this.authenticated; }
    public void setHttpClient(HttpClient httpClient) { this.httpClient = httpClient; }
}

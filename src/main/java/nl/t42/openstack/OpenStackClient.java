package nl.t42.openstack;

import nl.t42.openstack.command.account.AccountInformationCommand;
import nl.t42.openstack.command.account.AccountMetadataCommand;
import nl.t42.openstack.command.account.ListContainersCommand;
import nl.t42.openstack.command.identity.AuthenticationCommand;
import nl.t42.openstack.command.container.*;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.AccountInformation;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

public class OpenStackClient {

    private Access access;

    private HttpClient httpClient = new DefaultHttpClient();

    private boolean authenticated = false;

    public void authenticate(String username, String password, String authUrl) throws IOException {

        this.access = null;
        this.authenticated = false;

        this.access = new AuthenticationCommand(httpClient, authUrl, username, password).execute();
        this.authenticated = true;
    }

    public AccountInformation getAccountInformation() throws IOException {
        return new AccountInformationCommand(httpClient, access).execute();
    }

    public void setAccountInformation(Map<String, Object> metadata) throws IOException {
        new AccountMetadataCommand(httpClient, access, metadata).execute();
    }

    public Container[] listContainers() throws IOException {
        return new ListContainersCommand(httpClient, access).execute();
    }

    public void createContainer(Container container) throws IOException {
        new CreateContainerCommand(httpClient, access, container).execute();
    }

    public void deleteContainer(Container container) throws IOException {
        new DeleteContainerCommand(httpClient, access, container).execute();
    }

    public ContainerInformation getContainerInformation(Container container) throws IOException {
        return new ContainerInformationCommand(httpClient, access, container).execute();
    }

    public void setContainerInformation(Container container, Map<String, Object> metadata) throws IOException {
        new ContainerMetadataCommand(httpClient, access, container, metadata).execute();
    }

    public StoreObject[] listObjects(Container container) throws IOException {
        return new ListObjectsCommand(httpClient, access, container).execute();
    }

    public boolean isAuthenticated() { return this.authenticated; }
    public void setHttpClient(HttpClient httpClient) { this.httpClient = httpClient; }
}

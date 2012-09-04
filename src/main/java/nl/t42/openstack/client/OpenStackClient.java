package nl.t42.openstack.client;

import nl.t42.openstack.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface OpenStackClient {

    public void authenticate(String username, String password, String authUrl) throws IOException;

    public AccountInformation getAccountInformation() throws IOException;

    public void setAccountInformation(Map<String, Object> metadata) throws IOException;
    
    public Container[] listContainers() throws IOException;

    public void createContainer(Container container) throws IOException;

    public void deleteContainer(Container container) throws IOException;

    public void makeContainerPublic(Container container) throws IOException;

    public void makeContainerPrivate(Container container) throws IOException;

    public ContainerInformation getContainerInformation(Container container) throws IOException;

    public void setContainerInformation(Container container, Map<String, Object> metadata) throws IOException;

    public StoreObject[] listObjects(Container container) throws IOException;

    public byte[] downloadObject(Container container, StoreObject object) throws IOException;

    public void downloadObject(Container container, StoreObject object, File targetFile) throws IOException;

    public void uploadObject(Container container, StoreObject target, byte[] fileToUpload) throws IOException;

    public void uploadObject(Container container, StoreObject target, File fileToUpload) throws IOException;

    public ObjectInformation getObjectInformation(Container container, StoreObject object) throws IOException;

    public void setObjectInformation(Container container, StoreObject object, Map<String, Object> metadata) throws IOException;

    public void deleteObject(Container container, StoreObject object) throws IOException;

    public void copyObject(Container sourceContainer, StoreObject sourceObject, Container targetContainer, StoreObject targetObject) throws IOException;

    public boolean isAuthenticated();
}

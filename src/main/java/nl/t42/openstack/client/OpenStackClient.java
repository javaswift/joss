package nl.t42.openstack.client;

import nl.t42.openstack.model.*;

import java.io.File;
import java.util.Map;

public interface OpenStackClient {

    public void authenticate(String username, String password, String authUrl);

    public AccountInformation getAccountInformation();

    public void setAccountInformation(Map<String, Object> metadata);
    
    public Container[] listContainers();

    public void createContainer(Container container);

    public void deleteContainer(Container container);

    public void makeContainerPublic(Container container);

    public void makeContainerPrivate(Container container);

    public ContainerInformation getContainerInformation(Container container);

    public void setContainerInformation(Container container, Map<String, Object> metadata);

    public StoreObject[] listObjects(Container container);

    public byte[] downloadObject(Container container, StoreObject object);

    public void downloadObject(Container container, StoreObject object, File targetFile);

    public void uploadObject(Container container, StoreObject target, byte[] fileToUpload);

    public void uploadObject(Container container, StoreObject target, File fileToUpload);

    public ObjectInformation getObjectInformation(Container container, StoreObject object);

    public void setObjectInformation(Container container, StoreObject object, Map<String, Object> metadata);

    public void deleteObject(Container container, StoreObject object);

    public void copyObject(Container sourceContainer, StoreObject sourceObject, Container targetContainer, StoreObject targetObject);

    public boolean isAuthenticated();
}

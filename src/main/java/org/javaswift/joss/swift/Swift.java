package org.javaswift.joss.swift;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.identity.bulkdelete.BulkDeleteResponse;
import org.javaswift.joss.command.shared.identity.bulkdelete.ErrorObject;
import org.javaswift.joss.command.shared.identity.tenant.Tenant;
import org.javaswift.joss.command.shared.identity.tenant.Tenants;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.account.*;
import org.javaswift.joss.headers.object.DeleteAfter;
import org.javaswift.joss.headers.object.DeleteAt;
import org.javaswift.joss.headers.object.ObjectManifest;
import org.javaswift.joss.information.AccountInformation;
import org.javaswift.joss.information.ContainerInformation;
import org.javaswift.joss.information.ObjectInformation;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.*;
import org.javaswift.joss.swift.scheduled.ObjectDeleter;
import org.javaswift.joss.swift.statusgenerator.StatusGenerator;
import org.javaswift.joss.util.LocalTime;

import java.io.*;
import java.io.File;
import java.util.*;

import javax.management.ObjectName;

/**
* Mock implementation of the Swift Object Store
*/
public class Swift {

    private Map<String, SwiftContainer> containers = new TreeMap<String, SwiftContainer>();

    private boolean allowObjectDeleter;

    private ObjectDeleter objectDeleter;

    private MockUserStore users = new MockUserStore();

    private String publicHost;

    private String privateHost;

    private HeaderStore headers = new HeaderStore();

    private long millisDelay = 0;

    private int objectDeleterStartAfterSeconds = 10;

    private int objectDeleterIntervalSeconds = 10;

    private boolean tenantSupplied = false;

    private Character delimiter = '/';

    StatusGenerator statusGenerator = new StatusGenerator();

    public Swift setOnFileObjectStore(Class mockClasspath, String onFileObjectStore, boolean absolutePath) {
        if (onFileObjectStore == null) {
            return this;
        }
        OnFileObjectStoreLoader loader = new OnFileObjectStoreLoader();
        try {
            containers = loader.createContainers(mockClasspath, onFileObjectStore, absolutePath);
        } catch (Exception err) {
            throw new CommandException("Unable to load the object store from file: "+err.getMessage(), err);
        }
        return this;
    }

    public boolean isObjectDeleterActive() {
        return  this.objectDeleter != null &&
                !this.objectDeleter.isShutdown();
    }

    public ObjectDeleter getCurrentObjectDeleter() {
        if (!allowObjectDeleter) {
            return null;
        }
        if (!isObjectDeleterActive()) {
            this.objectDeleter = new ObjectDeleter(objectDeleterStartAfterSeconds, objectDeleterIntervalSeconds);
        }
        return this.objectDeleter;
    }

    public Swift setAllowObjectDeleter(boolean allowObjectDeleter) {
        this.allowObjectDeleter = allowObjectDeleter;
        return this;
    }

    public Swift setUserStore(MockUserStore users) {
        this.users = users;
        return this;
    }

    public MockUserStore getUserStore() {
        return this.users;
    }

    public Swift setPublicHost(String publicHost) {
        this.publicHost = publicHost;
        return this;
    }

    public Swift setPrivateHost(String privateHost) {
        this.privateHost = privateHost;
        return this;
    }

    public Swift setMillisDelay(long millisDelay) {
        this.millisDelay = millisDelay;
        return this;
    }

    public Swift setObjectDeleterStartAfterSeconds(int objectDeleterStartAfterSeconds) {
        this.objectDeleterStartAfterSeconds = objectDeleterStartAfterSeconds;
        return this;
    }

    public Swift setObjectDeleterIntervalSeconds(int objectDeleterIntervalSeconds) {
        this.objectDeleterIntervalSeconds = objectDeleterIntervalSeconds;
        return this;
    }

    public Swift setTenantSupplied(String tenantId, String tenantName) {
        this.tenantSupplied = tenantId != null || tenantName != null;
        return this;
    }

    public Swift setDelimiter(Character delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public SwiftResult<Tenants> getTenant() {
        Tenants tenants = new Tenants();
        Tenant tenant = new Tenant();
        tenant.id = "mock-id";
        tenant.name = "mock-name";
        tenant.enabled = true;
        tenants.tenants.add(tenant);
        return new SwiftResult<Tenants>(tenants, HttpStatus.SC_OK);
    }

    public Character getDelimiter() {
        return this.delimiter;
    }

    public void addIgnore() {
        this.statusGenerator.addIgnore();
    }

    public void addIgnore(Class<? extends CommandMock> ignoreClass) {
        this.statusGenerator.addIgnore(ignoreClass);
    }

    public void addStatus(int status) {
        this.statusGenerator.addStatus(status);
    }

    public void addStatus(Class<? extends CommandMock> clazz, int status) {
        this.statusGenerator.addStatus(clazz, status);
    }

    public int getStatus(Class<? extends CommandMock> clazz) {
        return this.statusGenerator.getStatus(clazz);
    }

    public String getHashPassword() {
        Header header = this.headers.get(AccountMetadata.X_ACCOUNT_META_PREFIX+ HashPassword.X_ACCOUNT_TEMP_URL_KEY);
        return header == null ? null : header.getHeaderValue();
    }

    public SwiftResult<Access> authenticate(String tenantName, String tenantId, String username, String password) {
        if (users.authenticate(tenantName, tenantId, username, password)) {
            return new SwiftResult<Access>(null, HttpStatus.SC_OK);
        } else {
            return new SwiftResult<Access>(HttpStatus.SC_UNAUTHORIZED);
        }
    }

    public SwiftResult<Collection<Container>> listContainers(Account account, ListInstructions listInstructions) {
        Collection<SwiftContainer> pagedContainers = new PageServer<SwiftContainer>().createPage(
                containers.values(),
                listInstructions.getPrefix(),
                listInstructions.getMarker(),
                listInstructions.getLimit());

        List<Container> containers = new ArrayList<Container>();
        for (SwiftContainer containerHeader : pagedContainers) {
            Container container = account.getContainer(containerHeader.getName());
            container.setCount(containerHeader.getCount());
            container.setBytesUsed(containerHeader.getBytesUsed());
            container.metadataSetFromHeaders();
            containers.add(container);
        }

        return new SwiftResult<Collection<Container>>(
                containers,
                HttpStatus.SC_OK
        );
    }

    public String getPublicHost() {
        return this.publicHost == null ? "" : this.publicHost;
    }

    public String getPrivateHost() {
        return this.privateHost == null ? "" : this.privateHost;
    }

    public SwiftResult<AccountInformation> getAccountInformation() {
        AccountInformation accountInformation = new AccountInformation();
        accountInformation.setMetadata(headers.getMetadata());
        int containerCount= 0;
        int objectCount = 0;
        long bytesUsed = 0;
        for (SwiftContainer container : this.containers.values()) {
            containerCount++;
            objectCount += container.getCount();
            bytesUsed += container.getBytesUsed();
        }
        accountInformation.setContainerCount(new AccountContainerCount(Integer.toString(containerCount)));
        accountInformation.setObjectCount(new AccountObjectCount(Integer.toString(objectCount)));
        accountInformation.setBytesUsed(new AccountBytesUsed(Long.toString(bytesUsed)));
        accountInformation.setServerDate(new ServerDate(LocalTime.currentTime()));

        return new SwiftResult<AccountInformation>(accountInformation, HttpStatus.SC_NO_CONTENT);
    }

    public SwiftResult<Object> saveMetadata(Collection<? extends Header> headers) {
        this.headers.saveMetadata(headers);
        return new SwiftResult<Object>(HttpStatus.SC_NO_CONTENT);
    }

    public SwiftResult<Object> saveHashPassword(String hashPassword) {
        this.headers.put(new HashPassword(hashPassword));
        return new SwiftResult<Object>(HttpStatus.SC_NO_CONTENT);
    }

    public SwiftResult<BulkDeleteResponse> bulkDelete(Collection<ObjectIdentifier> objectsToDelete) {
        int numberDeleted = 0;
        int numberNotFound = 0;
        ArrayList<ErrorObject> errors = new ArrayList<ErrorObject>();
        for (ObjectIdentifier objectIdentifier : objectsToDelete) {
            Optional<String> objectName = objectIdentifier.getObjectName();
            if (objectName.isPresent()) {
                SwiftResult<Object> deleteResult = deleteObjectByName(objectIdentifier.getContainerName(), objectName.get());
                if (deleteResult.getStatus() == HttpStatus.SC_NOT_FOUND){
                    numberNotFound++;
                }else if (deleteResult.getStatus() == HttpStatus.SC_OK){
                    numberDeleted++;
                } else {
                    addErrorObject(errors, objectIdentifier, deleteResult);
                }
            } else {
                SwiftResult<String[]> deleteResult = deleteContainer(objectIdentifier.getContainerName());
                if (deleteResult.getStatus() == HttpStatus.SC_NOT_FOUND){
                    numberNotFound++;
                }else if (deleteResult.getStatus() == HttpStatus.SC_OK){
                    numberDeleted++;
                } else {
                    addErrorObject(errors, objectIdentifier, deleteResult);
                }
            }
        }
        BulkDeleteResponse bulkDeleteResponse = new BulkDeleteResponse();
        bulkDeleteResponse.numberDeleted = numberDeleted;
        bulkDeleteResponse.numberNotFound = numberNotFound;
        bulkDeleteResponse.errors = errors.toArray(new ErrorObject[errors.size()]);
        return new SwiftResult<BulkDeleteResponse>(bulkDeleteResponse, HttpStatus.SC_OK);
    }

    private void addErrorObject(ArrayList<ErrorObject> errors, ObjectIdentifier objectIdentifier, SwiftResult<?> deleteContainer) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.name = objectIdentifier.getContainerName();
        errorObject.status = Integer.toString(deleteContainer.getStatus());
        errors.add(errorObject);
    }
    

    // +-----------------------------------------+
    // |           C O N T A I N E R             |
    // +-----------------------------------------+

    public SwiftContainer getContainer(String name) {
        return this.containers.get(name);
    }

    public SwiftResult<Object> createContainer(String name) {
        if (this.containers.get(name) == null) {
            this.containers.put(name, new SwiftContainer(name));
            return new SwiftResult<Object>(HttpStatus.SC_CREATED);
        } else {
            return new SwiftResult<Object>(HttpStatus.SC_ACCEPTED);
        }
    }

    public SwiftResult<String[]> deleteContainer(String name) {
        SwiftContainer container = getContainer(name);
        if (container == null) {
            return new SwiftResult<String[]>(HttpStatus.SC_NOT_FOUND);
        } else if (container.getCount() > 0) {
            return new SwiftResult<String[]>(HttpStatus.SC_CONFLICT);
        } else {
            this.containers.remove(name);
            return new SwiftResult<String[]>(new String[]{}, HttpStatus.SC_NO_CONTENT);
        }
    }

    public SwiftResult<ContainerInformation> getContainerInformation(String name) {
        SwiftContainer container = getContainer(name);
        if (container == null) {
            return new SwiftResult<ContainerInformation>(HttpStatus.SC_NOT_FOUND);
        } else {
            return new SwiftResult<ContainerInformation>(container.getInfo(), HttpStatus.SC_NO_CONTENT);
        }
    }

    public SwiftResult<Object> saveContainerMetadata(String name, Collection<? extends Header> headers) {
        SwiftContainer container = getContainer(name);
        if (container == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        } else {
            return container.saveMetadata(headers);
        }
    }

    public SwiftResult<String[]> setContainerPrivacy(String name, boolean publicContainer) {
        SwiftContainer container = getContainer(name);
        if (container == null) {
            return new SwiftResult<String[]>(HttpStatus.SC_NOT_FOUND);
        } else {
            return container.setContainerPrivacy(publicContainer);
        }
    }

    public SwiftResult<Collection<StoredObject>> listObjects(Container container, ListInstructions listInstructions) {
        SwiftContainer swiftContainer = getContainer(container.getName());
        if (swiftContainer == null) {
            return new SwiftResult<Collection<StoredObject>>(HttpStatus.SC_NOT_FOUND);
        } else {
            return swiftContainer.listObjects(container, listInstructions);
        }
    }

    public SwiftResult<Collection<DirectoryOrObject>> listDirectory(Container container, ListInstructions listInstructions) {
        SwiftContainer swiftContainer = getContainer(container.getName());
        if (swiftContainer == null) {
            return new SwiftResult<Collection<DirectoryOrObject>>(HttpStatus.SC_NOT_FOUND);
        } else {
            return swiftContainer.listDirectories(container, listInstructions);
        }
    }

    // +-----------------------------------------+
    // |              O B J E C T                |
    // +-----------------------------------------+

    public SwiftResult<Object> uploadObject(Container container, StoredObject object, UploadInstructions uploadInstructions) {
        SwiftContainer foundContainer = containers.get(container.getName());
        if (foundContainer == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftStoredObject foundObject = foundContainer.getObject(object.getName());
        if (foundObject == null) { // Create the object
            foundObject = foundContainer.createObject(object.getName());
        }
        return foundObject.uploadObject(uploadInstructions);
    }

    public SwiftResult<Object> copyObject(Container sourceContainer, StoredObject sourceObject,
                                          Container targetContainer, StoredObject targetObject) {

        SwiftContainer foundSourceContainer = containers.get(sourceContainer.getName());
        if (foundSourceContainer == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftStoredObject foundSourceObject = foundSourceContainer.getObject(sourceObject.getName());
        if (foundSourceObject == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftContainer foundTargetContainer = containers.get(targetContainer.getName());
        if (foundTargetContainer== null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftStoredObject foundTargetObject = foundTargetContainer.getObject(targetObject.getName());
        if (foundTargetObject == null) { // Create the object
            foundTargetObject = foundTargetContainer.createObject(targetObject.getName());
        }
        return foundTargetObject.copyFrom(foundSourceObject);
    }

    public SwiftResult<Object> deleteObject(Container container, StoredObject object) {
        return deleteObjectByName(container.getName(), object.getName());
    }

    private SwiftResult<Object> deleteObjectByName(String containerName, String objectName) {
        SwiftContainer foundContainer = containers.get(containerName);
        if (foundContainer == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftStoredObject foundObject = foundContainer.getObject(objectName);
        if (foundObject == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        foundContainer.deleteObject(objectName);
        return new SwiftResult<Object>(HttpStatus.SC_NO_CONTENT);
    }

    public SwiftResult<Object> saveObjectMetadata(Container container, StoredObject object, Collection<? extends Header> headers) {
        SwiftContainer foundContainer = containers.get(container.getName());
        if (foundContainer == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftStoredObject foundObject = foundContainer.getObject(object.getName());
        if (foundObject == null) {
            return new SwiftResult<Object>(HttpStatus.SC_NOT_FOUND);
        }
        // Insert logic here for scheduled file deletion
        DeleteAt deleteAt = getSpecificHeader(headers, DeleteAt.class);
        if (deleteAt == null) {
            DeleteAfter deleteAfter = getSpecificHeader(headers, DeleteAfter.class);
            if (deleteAfter != null) {
                deleteAt = new DeleteAt(new Date(LocalTime.currentTime() + deleteAfter.getExpireAfterSeconds() * 1000));
            }
        }
        if (deleteAt != null) {
            if (this.allowObjectDeleter) {
                getCurrentObjectDeleter().scheduleForDeletion(foundContainer, foundObject, deleteAt.getDate());
                foundObject.setDeleteAt(deleteAt);
            } else { // Remove immediately
                foundContainer.deleteObject(foundObject.getName());
            }
        }
        return foundObject.saveMetadata(headers);
    }

    private <T extends Header> T getSpecificHeader(Collection<? extends Header> headers, Class<T> matchClass) {
        for (Header header : headers) {
            if (matchClass.isInstance(header)) {
                return (T)header;
            }
        }
        return null;
    }

    public SwiftResult<ObjectInformation> getObjectInformation(Container container, StoredObject object) {
        SwiftContainer foundContainer = containers.get(container.getName());
        if (foundContainer == null) {
            return new SwiftResult<ObjectInformation>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftStoredObject foundObject = foundContainer.getObject(object.getName());
        if (foundObject == null) {
            return new SwiftResult<ObjectInformation>(HttpStatus.SC_NOT_FOUND);
        }
        return new SwiftResult<ObjectInformation>(foundObject.getInfo(), HttpStatus.SC_OK);
    }

    public SwiftResult<byte[]> downloadObject(Container container, StoredObject object, DownloadInstructions downloadInstructions) {
        SwiftContainer foundContainer = containers.get(container.getName());
        if (foundContainer == null) {
            return new SwiftResult<byte[]>(HttpStatus.SC_NOT_FOUND);
        }
        SwiftStoredObject foundObject = foundContainer.getObject(object.getName());
        if (foundObject == null) {
            return new SwiftResult<byte[]>(HttpStatus.SC_NOT_FOUND);
        }
        ObjectManifest objectManifest = foundObject.getObjectManifest();
        if (objectManifest != null) {
            return new SwiftResult<byte[]>(mergeSegmentedObjects(objectManifest), HttpStatus.SC_OK);
        } else {
            return foundObject.downloadObject(downloadInstructions);
        }
    }

    public SwiftResult<Object> downloadObject(Container container, StoredObject object, File targetFile, DownloadInstructions downloadInstructions) {
        InputStream is = null;
        OutputStream os = null;
        try {
            SwiftResult<byte[]> byteArray = downloadObject(container, object, downloadInstructions);
            if (byteArray.getPayload() == null) {
                return new SwiftResult<Object>(byteArray.getStatus());
            }
            is = new ByteArrayInputStream(byteArray.getPayload());
            os = new FileOutputStream(targetFile);
            IOUtils.copy(is, os);
        } catch (IOException err) {
            return new SwiftResult<Object>(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        } finally {
            closeStreams(is, os);
        }
        return new SwiftResult<Object>(HttpStatus.SC_OK);
    }

    @SuppressWarnings("EmptyCatchBlock")
    protected void closeStreams(InputStream is, OutputStream os) {
        if (os != null) try { os.close(); } catch (IOException logOrIgnore) {}
        if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
    }

    public SwiftResult<InputStream> downloadObjectAsInputStream(Container container, StoredObject object,
                                                                DownloadInstructions downloadInstructions) {
        SwiftResult<byte[]> byteArray = downloadObject(container, object, downloadInstructions);
        if (byteArray.getPayload() == null) {
            return new SwiftResult<InputStream>(byteArray.getStatus());
        }
        return new SwiftResult<InputStream>(
                new MockInputStreamWrapper(new ByteArrayInputStream(byteArray.getPayload())),
                HttpStatus.SC_OK);
    }

    protected byte[] mergeSegmentedObjects(ObjectManifest objectManifest) {
        SwiftContainer segmentFolder = getContainer(objectManifest.getContainerName());
        String objectPrefix = objectManifest.getObjectPrefix();
        Collection<SwiftStoredObject> segments = new ArrayList<SwiftStoredObject>();
        int byteCount = 0;
        for (SwiftStoredObject storedObject : segmentFolder.getAllObjects()) {
            if (storedObject.getName().startsWith(objectPrefix) && ! storedObject.getName().equals(objectPrefix)) {
                segments.add(storedObject);
                byteCount += storedObject.getBytesUsed();
            }
        }
        byte[] mergedObject = new byte[byteCount];
        int offset = 0;
        for (SwiftStoredObject segment : segments) {
            System.arraycopy(segment.getContent(), 0, mergedObject, offset, (int)segment.getBytesUsed());
            offset += segment.getBytesUsed();
        }
        return mergedObject;
    }

    public long getMillisDelay() {
        return millisDelay;
    }

    public boolean isTenantSupplied() {
        return tenantSupplied;
    }

}

package org.javaswift.joss.swift;

import org.apache.http.HttpStatus;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.container.ContainerBytesUsed;
import org.javaswift.joss.headers.container.ContainerObjectCount;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.information.ContainerInformation;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.ListSubject;
import org.javaswift.joss.model.StoredObject;

import java.util.*;

public class SwiftContainer implements ListSubject {

    private Map<String, SwiftStoredObject> objects = new TreeMap<String, SwiftStoredObject>();

    private String name;

    private HeaderStore headers = new HeaderStore();

    private boolean publicContainer;

    public SwiftContainer(String name) {
        this.name = name;
        setContainerPrivacy(false);
    }

    public int getCount() {
        return objects.size();
    }

    public long getBytesUsed() {
        long bytesUsed = 0;
        for (SwiftStoredObject object : objects.values()) {
            bytesUsed += object.getBytesUsed();
        }
        return bytesUsed;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void metadataSetFromHeaders() { /* not used */ }

    public ContainerInformation getInfo() {
        ContainerInformation containerInformation= new ContainerInformation();
        containerInformation.setMetadata(headers.getMetadata());
        containerInformation.setPublicContainer(new ContainerRights(isPublicContainer()));
        int objectCount = 0;
        long bytesUsed = 0;
        for (SwiftStoredObject object : objects.values()) {
            objectCount++;
            bytesUsed += object.getBytesUsed();
        }
        containerInformation.setBytesUsed(new ContainerBytesUsed(Long.toString(bytesUsed)));
        containerInformation.setObjectCount(new ContainerObjectCount(Integer.toString(objectCount)));
        return containerInformation;
    }

    public SwiftResult<Object> saveMetadata(Collection<? extends Header> headers) {
        this.headers.saveMetadata(headers);
        return new SwiftResult<Object>(HttpStatus.SC_NO_CONTENT);
    }

    public SwiftResult<String[]> setContainerPrivacy(boolean publicContainer) {
        this.publicContainer = publicContainer;
        return new SwiftResult<String[]>(HttpStatus.SC_ACCEPTED);
    }

    public SwiftResult<Collection<StoredObject>> listObjects(Container container, ListInstructions listInstructions) {
        Collection<SwiftStoredObject> pagedObjects = new PageServer<SwiftStoredObject>().createPage(
                objects.values(),
                listInstructions.getPrefix(),
                listInstructions.getMarker(),
                listInstructions.getLimit());

        List<StoredObject> objects = new ArrayList<StoredObject>();
        for (SwiftStoredObject pagedObject : pagedObjects) {
            StoredObject object = container.getObject(pagedObject.getName());
            object.setContentLength(pagedObject.getBytesUsed());
            object.setContentTypeWithoutSaving(pagedObject.getContentType().getHeaderValue());
            object.setEtag(pagedObject.getEtag().getHeaderValue());
            object.setLastModified(pagedObject.getLastModified());
            object.metadataSetFromHeaders();
            objects.add(object);
        }

        return new SwiftResult<Collection<StoredObject>>(
                objects,
                objects.size() == 0 ? HttpStatus.SC_NO_CONTENT : HttpStatus.SC_OK
        );
    }

    public SwiftStoredObject getObject(String name) {
        return this.objects.get(name);
    }

    public boolean isPublicContainer() {
        return this.publicContainer;
    }

    public SwiftStoredObject createObject(String name) {
        SwiftStoredObject object = new SwiftStoredObject(name);
        this.objects.put(name, object);
        return object;
    }

    public void deleteObject(String name) {
        this.objects.remove(name);
    }

    public Collection<SwiftStoredObject> getAllObjects() {
        return this.objects.values();
    }

}

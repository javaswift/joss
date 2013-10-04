package org.javaswift.joss.information;

import org.javaswift.joss.headers.container.ContainerBytesUsed;
import org.javaswift.joss.headers.container.ContainerObjectCount;
import org.javaswift.joss.headers.container.ContainerReadPermissions;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.headers.container.ContainerWritePermissions;

public class ContainerInformation extends AbstractInformation {

    private ContainerObjectCount objectCount;

    private ContainerBytesUsed bytesUsed;

    private ContainerRights containerRights;
    
    private String readPermissions;
    private String writePermissions;

    public ContainerInformation() {
        this.containerRights = new ContainerRights(false);
    }

    public int getObjectCount() {
        return Integer.parseInt(objectCount.getHeaderValue());
    }

    public void setObjectCount(ContainerObjectCount objectCount) {
        this.objectCount = objectCount;
    }

    public long getBytesUsed() {
        return Long.parseLong(bytesUsed.getHeaderValue());
    }

    public void setBytesUsed(ContainerBytesUsed bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public boolean isPublicContainer() {
        return containerRights.isPublic();
    }

    public void setPublicContainer(boolean publicContainer) {
        setPublicContainer(new ContainerRights(publicContainer));
    }

    public void setPublicContainer(ContainerRights containerRights) {
        this.containerRights = containerRights;
    }

    public String getReadPermissions() {
        return readPermissions;
    }

    public void setReadPermissions(String readPermissions) {
        this.readPermissions = readPermissions;
    }
    
    public void setReadPermissions(ContainerReadPermissions readPermissions) {
        this.readPermissions = readPermissions.getHeaderValue();
    }

    public String getWritePermissions() {
        return writePermissions;
    }

    public void setWritePermissions(String writePermissions) {
        this.writePermissions = writePermissions;
    }
    
    public void setWritePermissions(ContainerWritePermissions writePermissions) {
        this.writePermissions = writePermissions.getHeaderValue();
    }
}

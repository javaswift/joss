package org.javaswift.joss.swift.scheduled;

import org.javaswift.joss.swift.SwiftContainer;
import org.javaswift.joss.swift.SwiftStoredObject;

import java.util.Date;

public class ScheduledForDeletion {

    private SwiftContainer container;

    private SwiftStoredObject object;

    private Date deleteAt;

    public ScheduledForDeletion(SwiftContainer container, SwiftStoredObject object, Date deleteAt) {
        this.container = container;
        this.object = object;
        this.deleteAt = deleteAt;
    }

    public boolean deleteIf(Date now) {
        return now.compareTo(deleteAt) > 0;
    }

    public SwiftContainer getContainer() {
        return this.container;
    }

    public String getContainerName() {
        return getContainer().getName();
    }

    public String getObjectName() {
        return this.object.getName();
    }

    public boolean equals(Object o) {
        if (!(o instanceof ScheduledForDeletion)) {
            return false;
        }
        ScheduledForDeletion other = (ScheduledForDeletion)o;
        return
            getContainerName().equals(other.getContainerName()) &&
            getObjectName().equals(other.getObjectName());
    }

    public int hashCode() {
        return getObjectName().hashCode();
    }

}

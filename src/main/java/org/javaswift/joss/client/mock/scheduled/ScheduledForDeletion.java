package org.javaswift.joss.client.mock.scheduled;

import org.javaswift.joss.model.StoredObject;

import java.util.Date;

public class ScheduledForDeletion {

    private StoredObject storedObject;

    private Date deleteAt;

    public ScheduledForDeletion(StoredObject storedObject, Date deleteAt) {
        this.storedObject = storedObject;
        this.deleteAt = deleteAt;
    }

    public boolean deleteIf(Date now) {
        if (now.compareTo(deleteAt) > 0) {
            storedObject.delete();
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        return o instanceof ScheduledForDeletion && storedObject.equals(((ScheduledForDeletion) o).storedObject);
    }

    public int hashCode() {
        return storedObject.hashCode();
    }

}

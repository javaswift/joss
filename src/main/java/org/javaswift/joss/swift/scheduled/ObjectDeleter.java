package org.javaswift.joss.swift.scheduled;

import org.javaswift.joss.swift.SwiftContainer;
import org.javaswift.joss.swift.SwiftStoredObject;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ObjectDeleter implements Runnable {

    private Set<ScheduledForDeletion> objectsToDelete = new HashSet<ScheduledForDeletion>();

    public ObjectDeleter(int startAfter, int intervalInSeconds) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this, startAfter, intervalInSeconds, TimeUnit.SECONDS);
    }

    public void scheduleForDeletion(SwiftContainer container, SwiftStoredObject object, Date deleteAt) {
        objectsToDelete.add(new ScheduledForDeletion(container, object, deleteAt));
    }

    public void run() {
        Date now = new Date();
        List<ScheduledForDeletion> objectsToDeleteNow = new ArrayList<ScheduledForDeletion>();
        for (ScheduledForDeletion scheduledForDeletion : objectsToDelete) {
            if (scheduledForDeletion.deleteIf(now)) {
                scheduledForDeletion.getContainer().deleteObject(scheduledForDeletion.getObjectName());
                objectsToDeleteNow.add(scheduledForDeletion);
            }
        }
        objectsToDelete.removeAll(objectsToDeleteNow);
    }
}

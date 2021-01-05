package org.javaswift.joss.swift.scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.javaswift.joss.swift.SwiftContainer;
import org.javaswift.joss.swift.SwiftStoredObject;
import org.javaswift.joss.util.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectDeleter implements Runnable {

    public static final Logger LOG = LoggerFactory.getLogger(ObjectDeleter.class);

    private ScheduledExecutorService scheduler;

    private Set<ScheduledForDeletion> objectsToDelete = new HashSet<ScheduledForDeletion>();

    public ObjectDeleter(int startAfter, int intervalInSeconds) {
        LOG.debug("OD.init: "+startAfter+"/"+intervalInSeconds);
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduler.scheduleAtFixedRate(this, startAfter, intervalInSeconds, TimeUnit.SECONDS);
    }

    public void scheduleForDeletion(SwiftContainer container, SwiftStoredObject object, Date deleteAt) {
        LOG.debug("OD.schedule");
        objectsToDelete.add(new ScheduledForDeletion(container, object, deleteAt));
    }

    public boolean isShutdown() {
        return this.scheduler.isShutdown();
    }

    public void shutdown() {
        LOG.debug("OD.shutdown");
        this.scheduler.shutdown();
    }

    public void run() {
        LOG.debug("OD.run");

        Date now = LocalTime.currentDate();
        List<ScheduledForDeletion> objectsToDeleteNow = new ArrayList<ScheduledForDeletion>();
        for (ScheduledForDeletion scheduledForDeletion : objectsToDelete) {
            if (scheduledForDeletion.deleteIf(now)) {
                scheduledForDeletion.getContainer().deleteObject(scheduledForDeletion.getObjectName());
                objectsToDeleteNow.add(scheduledForDeletion);
            }
        }
        objectsToDelete.removeAll(objectsToDeleteNow);

        if (objectsToDelete.isEmpty()) {
            shutdown();
        }
    }
}

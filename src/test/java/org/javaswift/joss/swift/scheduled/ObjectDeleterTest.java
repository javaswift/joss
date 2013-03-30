package org.javaswift.joss.swift.scheduled;

import org.javaswift.joss.swift.SwiftContainer;
import org.javaswift.joss.swift.SwiftStoredObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ObjectDeleterTest {

    private Date before;
    private Date after;

    @Before
    public void setup() {
        Date now = new Date();
        before = new Date(now.getTime()-86400);
        after = new Date(now.getTime()+86400);
    }

    @Test
    public void variousObjectsMustBeRemovedNow() throws InterruptedException {
        SwiftContainer container = new SwiftContainer("someContainer");
        container.createObject("before");
        SwiftStoredObject object1 = container.getObject("before");
        container.createObject("after");
        SwiftStoredObject object2 = container.getObject("after");

        ObjectDeleter objectDeleter = new ObjectDeleter(1, 10000); // Serious interval to prevent double run
        objectDeleter.scheduleForDeletion(container, object1, before);
        objectDeleter.scheduleForDeletion(container, object2, after);
        Thread.sleep(1500);
        assertNull(container.getObject("before"));
        assertNotNull(container.getObject("after"));
    }
}

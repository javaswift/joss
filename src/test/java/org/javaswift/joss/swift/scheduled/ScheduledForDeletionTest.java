package org.javaswift.joss.swift.scheduled;

import org.javaswift.joss.swift.SwiftContainer;
import org.javaswift.joss.swift.SwiftStoredObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.*;

public class ScheduledForDeletionTest {

    private Date before;
    private Date now;
    private Date after;

    @Before
    public void setup() {
        now = new Date();
        before = new Date(now.getTime()-86400);
        after = new Date(now.getTime()+86400);
    }

    @Test
    public void objectMustBeDeleted() {
        SwiftContainer container = new SwiftContainer("someContainer");
        container.createObject("alpha");
        SwiftStoredObject object = container.getObject("alpha");
        ScheduledForDeletion scheduledForDeletion = new ScheduledForDeletion(container, object, before);
        assertTrue(scheduledForDeletion.deleteIf(now));
    }

    @Test
    public void objectMustNotBeDeleted() {
        SwiftContainer container = new SwiftContainer("someContainer");
        container.createObject("alpha");
        SwiftStoredObject object = container.getObject("alpha");
        ScheduledForDeletion scheduledForDeletion = new ScheduledForDeletion(container, object, after);
        assertFalse(scheduledForDeletion.deleteIf(now));
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void equals() {
        SwiftContainer container = new SwiftContainer("someContainer");
        container.createObject("alpha");
        ScheduledForDeletion sched1 = new ScheduledForDeletion(container, container.getObject("alpha"), new Date());
        container.createObject("beta");
        ScheduledForDeletion sched2 = new ScheduledForDeletion(container, container.getObject("beta"), new Date());
        ScheduledForDeletion sched3 = new ScheduledForDeletion(container, container.getObject("alpha"), new Date());
        assertTrue(sched1.equals(sched1));
        assertFalse(sched1.equals(sched2));
        assertNotSame(sched1.hashCode(), sched2.hashCode());
        assertEquals(sched1.hashCode(), sched3.hashCode());
        assertFalse(sched1.equals("bla"));
    }

}

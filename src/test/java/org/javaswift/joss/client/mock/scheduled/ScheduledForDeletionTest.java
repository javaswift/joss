package org.javaswift.joss.client.mock.scheduled;

import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.client.mock.ContainerMock;
import org.javaswift.joss.client.mock.StoredObjectMock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        StoredObject object = Mockito.mock(StoredObject.class);
        ScheduledForDeletion scheduledForDeletion = new ScheduledForDeletion(object, before);
        assertTrue(scheduledForDeletion.deleteIf(now));
        verify(object).delete();
    }

    @Test
    public void objectMustNotBeDeleted() {
        StoredObject object = Mockito.mock(StoredObject.class);
        ScheduledForDeletion scheduledForDeletion = new ScheduledForDeletion(object, after);
        assertFalse(scheduledForDeletion.deleteIf(now));
        verify(object, times(0)).delete();
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void equals() {
        ScheduledForDeletion sched1 = new ScheduledForDeletion(
                new StoredObjectMock(new ContainerMock(null, "alpha"), "somename.png"), new Date());
        ScheduledForDeletion sched2 = new ScheduledForDeletion(
                new StoredObjectMock(new ContainerMock(null, "beta"), "somename.png"), new Date());
        assertTrue(sched1.equals(sched1));
        assertFalse(sched1.equals(sched2));
        assertEquals(sched1.hashCode(), sched2.hashCode());
        assertFalse(sched1.equals("bla"));
    }

}

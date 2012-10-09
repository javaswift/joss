package nl.tweeenveertig.openstack.client.mock.scheduled;

import junit.framework.Assert;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
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
}

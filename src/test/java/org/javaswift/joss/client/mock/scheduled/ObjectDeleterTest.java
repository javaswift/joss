package org.javaswift.joss.client.mock.scheduled;

import org.javaswift.joss.model.StoredObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.verify;

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
        StoredObject object1 = Mockito.mock(StoredObject.class);
        StoredObject object2 = Mockito.mock(StoredObject.class);
        ObjectDeleter objectDeleter = new ObjectDeleter(1, 10000); // Serious interval to prevent double run
        objectDeleter.scheduleForDeletion(object1, before);
        objectDeleter.scheduleForDeletion(object2, after);
        Thread.sleep(1500);
        verify(object1).delete();
    }
}

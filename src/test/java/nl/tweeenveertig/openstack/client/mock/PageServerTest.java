package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.impl.StoredObjectImpl;
import nl.tweeenveertig.openstack.model.StoredObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static junit.framework.Assert.assertEquals;

public class PageServerTest {

    @Test
    public void createPage() {
        PageServer<StoredObject> pageServer = new PageServer<StoredObject>();
        Collection<StoredObject> objects = new ArrayList<StoredObject>();
        objects.add(new StoredObjectImpl(null, "A", true));
        objects.add(new StoredObjectImpl(null, "B", true));
        objects.add(new StoredObjectImpl(null, "C", true));
        objects.add(new StoredObjectImpl(null, "D", true));
        objects.add(new StoredObjectImpl(null, "E", true));
        Collection<StoredObject> page = pageServer.createPage(objects, "B", 2);
        assertEquals(2, page.size());
        Iterator<StoredObject> pageIt = page.iterator();
        assertEquals("C", pageIt.next().getName());
        assertEquals("D", pageIt.next().getName());
    }

}

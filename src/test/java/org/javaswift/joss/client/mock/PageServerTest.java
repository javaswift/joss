package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.impl.StoredObjectImpl;
import org.javaswift.joss.model.StoredObject;
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
        Collection<StoredObject> page = pageServer.createPage(objects, null, "B", 2);
        assertEquals(2, page.size());
        Iterator<StoredObject> pageIt = page.iterator();
        assertEquals("C", pageIt.next().getName());
        assertEquals("D", pageIt.next().getName());
    }

    @Test
    public void createPageWithPrefix() {
        PageServer<StoredObject> pageServer = new PageServer<StoredObject>();
        Collection<StoredObject> objects = new ArrayList<StoredObject>();
        objects.add(new StoredObjectImpl(null, "A", true));
        objects.add(new StoredObjectImpl(null, "A-tst", true));
        objects.add(new StoredObjectImpl(null, "B", true));
        objects.add(new StoredObjectImpl(null, "B-prd", true));
        objects.add(new StoredObjectImpl(null, "B-tst", true));
        objects.add(new StoredObjectImpl(null, "C", true));
        Collection<StoredObject> page = pageServer.createPage(objects, "B-", "B-prd", 2);
        assertEquals(1, page.size());
        Iterator<StoredObject> pageIt = page.iterator();
        assertEquals("B-tst", pageIt.next().getName());
    }

}
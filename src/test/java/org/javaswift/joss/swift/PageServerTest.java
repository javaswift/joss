package org.javaswift.joss.swift;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static junit.framework.Assert.assertEquals;

public class PageServerTest {

    @Test
    public void createPage() {
        PageServer<SwiftStoredObject> pageServer = new PageServer<SwiftStoredObject>();
        Collection<SwiftStoredObject> objects = new ArrayList<SwiftStoredObject>();
        objects.add(new SwiftStoredObject("A"));
        objects.add(new SwiftStoredObject("B"));
        objects.add(new SwiftStoredObject("C"));
        objects.add(new SwiftStoredObject("D"));
        objects.add(new SwiftStoredObject("E"));
        Collection<SwiftStoredObject> page = pageServer.createPage(objects, null, "B", 2);
        assertEquals(2, page.size());
        Iterator<SwiftStoredObject> pageIt = page.iterator();
        assertEquals("C", pageIt.next().getName());
        assertEquals("D", pageIt.next().getName());
    }

    @Test
    public void createPageWithPrefix() {
        PageServer<SwiftStoredObject> pageServer = new PageServer<SwiftStoredObject>();
        Collection<SwiftStoredObject> objects = new ArrayList<SwiftStoredObject>();
        objects.add(new SwiftStoredObject("A"));
        objects.add(new SwiftStoredObject("A-tst"));
        objects.add(new SwiftStoredObject("B"));
        objects.add(new SwiftStoredObject("B-prd"));
        objects.add(new SwiftStoredObject("B-tst"));
        objects.add(new SwiftStoredObject("C"));
        Collection<SwiftStoredObject> page = pageServer.createPage(objects, "B-", "B-prd", 2);
        assertEquals(1, page.size());
        Iterator<SwiftStoredObject> pageIt = page.iterator();
        assertEquals("B-tst", pageIt.next().getName());
    }

}
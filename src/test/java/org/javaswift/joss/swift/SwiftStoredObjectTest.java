package org.javaswift.joss.swift;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SwiftStoredObjectTest {

    @Test
    public void compare() {
        SwiftStoredObject object1 = new SwiftStoredObject("alpha");
        SwiftStoredObject object2 = new SwiftStoredObject("beta");
        SwiftStoredObject object3 = new SwiftStoredObject("alpha");
        assertEquals(object1, object3);
        assertEquals(0, object1.compareTo(object3));
        assertEquals(-1, object1.compareTo(object2));
        assertEquals(1, object2.compareTo(object3));
    }

}

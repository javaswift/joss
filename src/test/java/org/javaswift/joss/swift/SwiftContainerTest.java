package org.javaswift.joss.swift;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SwiftContainerTest {

    @Test
    public void compare() {
        SwiftContainer container1 = new SwiftContainer("alpha");
        SwiftContainer container2 = new SwiftContainer("beta");
        SwiftContainer container3 = new SwiftContainer("alpha");
        assertEquals(container1, container3);
        assertEquals(0, container1.compareTo(container3));
        assertEquals(-1, container1.compareTo(container2));
        assertEquals(1, container2.compareTo(container3));
    }

}

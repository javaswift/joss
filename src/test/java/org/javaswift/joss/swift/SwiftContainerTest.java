package org.javaswift.joss.swift;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SwiftContainerTest {

    @Test
    public void getCoverage() {
        new SwiftContainer("").metadataSetFromHeaders();
    }

}

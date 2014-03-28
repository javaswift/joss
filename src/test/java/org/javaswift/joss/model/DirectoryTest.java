package org.javaswift.joss.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DirectoryTest {

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void equals() {
        assertEquals(new Directory("abc", '/'), new Directory("abc", '/'));
        assertFalse(new Directory("abc", '/').equals("huh?"));
        assertFalse(new Directory("abc", '/').equals(new Directory("def", '/')));
    }

    @Test
    public void nameNull() {
        Directory directory = new Directory(null, '/');
        assertNull(directory.getName());
        assertNull(directory.getBareName());
    }

    @Test
    public void namePath() {
        Directory directory = new Directory("/abc/def/geh/", '/');
        assertEquals("/abc/def/geh/", directory.getName());
        assertEquals("geh", directory.getBareName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAsObject() {
        new Directory(null, '/').getAsObject();
    }

    @Test
    public void getAsDirectory() {
        assertNotNull(new Directory(null, '/').getAsDirectory());
    }

    @Test
    public void whatItIs() {
        Directory directory = new Directory(null, '/');
        directory.metadataSetFromHeaders();
        assertTrue(directory.isDirectory());
        assertFalse(directory.isObject());
    }

}

package org.javaswift.joss.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

    @Test
    public void checkHashCode() {
        assertEquals("/abc".hashCode(), new Directory("/abc", '/').hashCode());
        assertEquals(31, new Directory(null, '/').hashCode());
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

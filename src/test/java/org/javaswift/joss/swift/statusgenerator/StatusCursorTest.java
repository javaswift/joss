package org.javaswift.joss.swift.statusgenerator;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StatusCursorTest {

    @Test
    public void noStatus() {
        StatusCursor cursor = new StatusCursor();
        assertEquals(-1, cursor.getStatus());
    }

    @Test
    public void justOneStatus() {
        StatusCursor cursor = new StatusCursor();
        cursor.addStatus(500);
        assertEquals(500, cursor.getStatus());
        assertEquals(500, cursor.getStatus());
    }

    @Test
    public void successThenFailure() {
        StatusCursor cursor = new StatusCursor();
        cursor.addStatus(-1);
        cursor.addStatus(-1);
        cursor.addStatus(404);
        assertEquals(-1, cursor.getStatus());
        assertEquals(-1, cursor.getStatus());
        assertEquals(404, cursor.getStatus());
        assertEquals(-1, cursor.getStatus());
    }

}

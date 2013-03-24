package org.javaswift.joss.command.impl.core.httpstatus;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class HttpStatusRangeTest {

    @Test
    public void inRange() {
        HttpStatusRange range = new HttpStatusRange(200, 299);
        assertTrue("Should be in the 200-range", range.matches(201));
    }

    @Test
    public void notInRange() {
        HttpStatusRange range = new HttpStatusRange(200, 299);
        assertFalse("Should NOT be in the 200-range", range.matches(500));
    }

    @Test
    public void beforeRange() {
        HttpStatusRange range = new HttpStatusRange(200, 299);
        assertFalse("Should NOT be in the 200-range", range.matches(102));
    }
}

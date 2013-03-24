package org.javaswift.joss.command.impl.core.httpstatus;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class HttpStatusMatchTest {

    @Test
    public void isEqual() {
        HttpStatusMatch range = new HttpStatusMatch(201);
        assertTrue("Should NOT be equal to 201", range.matches(201));
    }

    @Test
    public void notEqual() {
        HttpStatusMatch range = new HttpStatusMatch(201);
        assertFalse("Should NOT be equal to 201", range.matches(500));
    }

}

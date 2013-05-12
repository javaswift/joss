package org.javaswift.joss.command.mock.identity;

import org.junit.Test;

import static junit.framework.Assert.assertNull;

public class AuthenticationCommandMockTest {

    @Test
    public void getUrl() {
        AuthenticationCommandMock command = new AuthenticationCommandMock(null, null, null, null, null, null);
        assertNull(command.getUrl());
    }

}

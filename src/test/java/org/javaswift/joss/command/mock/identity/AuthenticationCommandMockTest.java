package org.javaswift.joss.command.mock.identity;

import static junit.framework.Assert.assertNull;

import org.junit.Test;

public class AuthenticationCommandMockTest {

    @Test
    public void getUrl() {
        AuthenticationCommandMock command = new AuthenticationCommandMock(null, null, null, null, null, null);
        assertNull(command.getUrl());
    }

}

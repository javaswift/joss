package org.javaswift.joss.command.shared.identity.access;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class AccessBasicTest {

    @Test
    public void settersAndGetters() {
        String url = "http://www.abc.nl/";
        String urlNoSlashAtEnd = "http://www.abc.nl";
        String token = "cafebabe";
        AccessBasic access = new AccessBasic();
        access.setUrl(url);
        assertEquals(url, access.getInternalURL());
        assertEquals(url, access.getPublicURL());
        access.setToken(token);
        assertEquals(token, access.getToken());
        access.setPreferredRegion(null); // does nothing
        assertEquals(urlNoSlashAtEnd, access.getTempUrlPrefix(null));
        access.setUrl(urlNoSlashAtEnd);
        assertEquals(urlNoSlashAtEnd, access.getTempUrlPrefix(null));
        assertTrue(access.isTenantSupplied());
    }
}

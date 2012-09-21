package nl.tweeenveertig.openstack.headers.metadata;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class AccountMetadataTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountMetadata("name", "value"));
    }
}

package nl.tweeenveertig.openstack.headers.account;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.account.AccountMetadata;
import org.junit.Test;

public class AccountMetadataTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountMetadata("name", "value"));
    }
}

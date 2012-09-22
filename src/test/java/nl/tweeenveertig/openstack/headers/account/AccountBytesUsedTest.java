package nl.tweeenveertig.openstack.headers.account;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class AccountBytesUsedTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountBytesUsed("123654789"));
    }
}

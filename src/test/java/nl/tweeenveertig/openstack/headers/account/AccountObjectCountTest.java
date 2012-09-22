package nl.tweeenveertig.openstack.headers.account;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class AccountObjectCountTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountObjectCount("123"));
    }
}

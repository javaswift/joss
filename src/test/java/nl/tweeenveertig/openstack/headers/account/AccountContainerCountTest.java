package nl.tweeenveertig.openstack.headers.account;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class AccountContainerCountTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountContainerCount("15"));
    }
}

package nl.tweeenveertig.openstack.headers.account;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class AccountBytesUsedTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountBytesUsed("123654789"));
    }
}

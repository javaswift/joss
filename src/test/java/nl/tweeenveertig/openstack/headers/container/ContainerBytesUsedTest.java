package nl.tweeenveertig.openstack.headers.container;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.account.AccountBytesUsed;
import org.junit.Test;

public class ContainerBytesUsedTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerBytesUsed("123654789"));
    }
}

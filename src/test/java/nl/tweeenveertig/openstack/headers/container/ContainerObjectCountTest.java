package nl.tweeenveertig.openstack.headers.container;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.account.AccountObjectCount;
import org.junit.Test;

public class ContainerObjectCountTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerObjectCount("123"));
    }
}

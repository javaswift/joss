package nl.tweeenveertig.openstack.information;

import nl.tweeenveertig.openstack.headers.account.AccountBytesUsed;
import nl.tweeenveertig.openstack.headers.account.AccountContainerCount;
import nl.tweeenveertig.openstack.headers.account.AccountMetadata;
import nl.tweeenveertig.openstack.headers.account.AccountObjectCount;
import nl.tweeenveertig.openstack.information.AccountInformation;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AccountInformationTest {

    @Test
    public void construct() {
        AccountInformation info = new AccountInformation();
        info.setBytesUsed(new AccountBytesUsed("123456"));
        info.setObjectCount(new AccountObjectCount("345"));
        info.setContainerCount(new AccountContainerCount("3"));
        assertEquals(123456, info.getBytesUsed());
        assertEquals(345, info.getObjectCount());
        assertEquals(3, info.getContainerCount());
    }

    @Test
    public void metadata() {
        AccountInformation info = new AccountInformation();
        info.addMetadata(new AccountMetadata("some-name", "some-value"));
        assertEquals("some-value", info.getMetadata("some-name"));
        assertEquals(null, info.getMetadata("some-nonexisting-name"));
    }
}

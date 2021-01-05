package org.javaswift.joss.information;

import static junit.framework.Assert.assertEquals;

import org.javaswift.joss.headers.account.AccountBytesUsed;
import org.javaswift.joss.headers.account.AccountContainerCount;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.headers.account.AccountObjectCount;
import org.javaswift.joss.headers.account.ServerDate;
import org.junit.Test;

public class AccountInformationTest {

    @Test
    public void construct() {
        AccountInformation info = new AccountInformation();
        info.setBytesUsed(new AccountBytesUsed("123456"));
        info.setObjectCount(new AccountObjectCount("345"));
        info.setContainerCount(new AccountContainerCount("3"));
        info.setServerDate(new ServerDate(12345678L));
        assertEquals(123456, info.getBytesUsed());
        assertEquals(345, info.getObjectCount());
        assertEquals(3, info.getContainerCount());
        assertEquals(12345678L, info.getServerDate());
    }

    @Test
    public void metadata() {
        AccountInformation info = new AccountInformation();
        info.addMetadata(new AccountMetadata("some-name", "some-value"));
        assertEquals("some-value", info.getMetadata("Some-Name"));
        assertEquals(null, info.getMetadata("some-nonexisting-name"));
    }
}

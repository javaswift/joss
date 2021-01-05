package org.javaswift.joss.headers.account;

import static junit.framework.Assert.assertEquals;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class AccountMetadataTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountMetadata("name", "value"));
    }

    @Test
    public void capitalize() {
        AccountMetadata metadata = new AccountMetadata("encryption-algorithm", "AES");
        assertEquals("encryption-algorithm", metadata.getName());
    }

    @Test
    public void replaceUnderscores() {
        AccountMetadata metadata = new AccountMetadata("log_all_entities", "mostdef");
        assertEquals("log-all-entities", metadata.getName());
    }

    @Test
    public void onlyCapitalizeTheFirstCharacter() {
        AccountMetadata metadata = new AccountMetadata("log ALL entities", "mostdef");
        assertEquals("log all entities", metadata.getName());
    }

}

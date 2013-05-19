package org.javaswift.joss.headers.account;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AccountMetadataTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountMetadata("name", "value"));
    }

    @Test
    public void capitalize() {
        AccountMetadata metadata = new AccountMetadata("encryption-algorithm", "AES");
        assertEquals("Encryption-Algorithm", metadata.getName());
    }

    @Test
    public void replaceUnderscores() {
        AccountMetadata metadata = new AccountMetadata("log_all_entities", "mostdef");
        assertEquals("Log-All-Entities", metadata.getName());
    }

}

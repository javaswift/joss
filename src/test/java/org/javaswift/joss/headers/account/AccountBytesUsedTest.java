package org.javaswift.joss.headers.account;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class AccountBytesUsedTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new AccountBytesUsed("123654789"));
    }
}

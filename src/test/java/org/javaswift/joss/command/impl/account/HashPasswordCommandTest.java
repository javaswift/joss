package org.javaswift.joss.command.impl.account;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.headers.account.HashPassword;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class HashPasswordCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        expectStatusCode(204);
        new HashPasswordCommandImpl(this.account, httpClient, defaultAccess, "somepwd").call();
        verifyHeaderValue("somepwd", AccountMetadata.X_ACCOUNT_META_PREFIX + HashPassword.X_ACCOUNT_TEMP_URL_KEY);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new HashPasswordCommandImpl(this.account, httpClient, defaultAccess, "somepwd"), 204);
    }

}

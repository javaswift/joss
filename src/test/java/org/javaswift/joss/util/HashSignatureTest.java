package org.javaswift.joss.util;

import org.javaswift.joss.exception.CommandException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HashSignatureTest {

    @Test(expected = CommandException.class)
    public void hashNullString() throws Exception {
        new HashSignature(); // coverage measure
        HashSignature.getSignature("somepwd", null);
    }

    @Test
    public void hashFullPlaintext() {
        String plainText = "GET\n2737152115\n/v1/AUTH_a32c0e5f920a4dbc967e50dd2a4e3957/secret/hum3.png";
        String password = "welkom#42";
        String signature = HashSignature.getSignature(password, plainText);
        assertEquals("e9dab41e3ec1ae2d6c9dfb794bc9cb401757992c", signature);
    }

}

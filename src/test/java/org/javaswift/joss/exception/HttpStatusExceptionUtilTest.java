package org.javaswift.joss.exception;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class HttpStatusExceptionUtilTest {

    @Test(expected = ForbiddenException.class)
    public void regularError() throws Exception {
        new HttpStatusExceptionUtil();
        throw HttpStatusToExceptionMapper._403.getException(null);
    }

    @Test
    public void exceptionCannotBeInstantiated(@Mocked({ "getExceptionToThrow()" }) HttpStatusToExceptionMapper unused) throws Exception {
        new Expectations() {{
            onInstance(HttpStatusToExceptionMapper._403).getExceptionToThrow();
            result = null;
        }};
        try {
            throw HttpStatusToExceptionMapper._403.getException(null);
        } catch (CommandException err) {
            assertTrue(err.getMessage().contains("403"));
        }
    }
}

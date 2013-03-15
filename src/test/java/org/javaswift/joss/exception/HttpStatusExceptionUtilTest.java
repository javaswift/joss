package org.javaswift.joss.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpStatusToExceptionMapper.class })
public class HttpStatusExceptionUtilTest {

    @Test(expected = ForbiddenException.class)
    public void regularError() throws Exception {
        new HttpStatusExceptionUtil();
        throw HttpStatusToExceptionMapper._403.getException(null);
    }

    @Test
    public void exceptionCannotBeInstantiated() throws Exception {
        HttpStatusToExceptionMapper original = spy(HttpStatusToExceptionMapper._403);
        when(original, "getExceptionToThrow").thenReturn(null);
        try {
            throw original.getException(null);
        } catch (CommandException err) {
            assertTrue(err.getMessage().contains("403"));
        }
    }
}

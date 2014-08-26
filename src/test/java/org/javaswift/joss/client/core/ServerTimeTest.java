package org.javaswift.joss.client.core;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.javaswift.joss.util.LocalTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ServerTimeTest {

    @Test
    public void getServerTime(@Mocked(stubOutClassInitialization = true) final LocalTime localTime) {
        final long today = 1369581120L;
        ServerTime serverTime = ServerTime.create((today + 10) * 1000, today);
        new NonStrictExpectations() {{
            LocalTime.currentTime();
            result = today;
        }};
        assertEquals(today + 10, serverTime.getServerTimeInSeconds());
        assertEquals(today + 10 + 86400, serverTime.getServerTime(86400));
    }

}

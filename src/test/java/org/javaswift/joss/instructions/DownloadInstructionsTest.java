package org.javaswift.joss.instructions;

import org.javaswift.joss.headers.object.conditional.IfModifiedSince;
import org.javaswift.joss.headers.object.conditional.IfNoneMatch;
import org.javaswift.joss.headers.object.range.FirstPartRange;
import org.apache.http.impl.cookie.DateParseException;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class DownloadInstructionsTest {

    public static final String expectedDateString = "Tue, 02 Oct 2012 17:34:17 GMT";

    @Test
    public void construct() throws DateParseException {
        DownloadInstructions instructions = new DownloadInstructions()
                .setRange(new FirstPartRange(1))
                .setMatchConditional(new IfNoneMatch("ebabefac"))
                .setSinceConditional(new IfModifiedSince(expectedDateString));
        assertNotNull(instructions.getRange());
        assertEquals("ebabefac", instructions.getMatchConditional().getHeaderValue());
        assertEquals(expectedDateString, instructions.getSinceConditional().getHeaderValue());
    }

    @Test
    public void supplyEmptyIfMatch() {
        DownloadInstructions downloadInstructions = new DownloadInstructions()
                .setMatchConditional(new IfNoneMatch(null));
        assertNull(downloadInstructions.getMatchConditional());
    }

    @Test
    public void supplyEmptyIfSince() throws DateParseException {
        DownloadInstructions downloadInstructions = new DownloadInstructions()
                .setSinceConditional(new IfModifiedSince((Date)null));
        assertNull(downloadInstructions.getSinceConditional());
    }

}

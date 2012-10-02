package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.object.conditional.IfModifiedSince;
import nl.tweeenveertig.openstack.headers.object.conditional.IfNoneMatch;
import nl.tweeenveertig.openstack.headers.object.range.FirstPartRange;
import org.apache.http.impl.cookie.DateParseException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

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

}

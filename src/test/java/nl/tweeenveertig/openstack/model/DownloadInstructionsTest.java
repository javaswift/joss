package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.object.conditional.IfNoneMatch;
import nl.tweeenveertig.openstack.headers.object.range.FirstPartRange;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class DownloadInstructionsTest {

    @Test
    public void construct() {
        DownloadInstructions instructions = new DownloadInstructions()
                .setRange(new FirstPartRange(1))
                .setMatchConditional(new IfNoneMatch("ebabefac"));
        assertNotNull(instructions.getRange());
        assertEquals("ebabefac", instructions.getMatchConditional().getHeaderValue());
    }

}

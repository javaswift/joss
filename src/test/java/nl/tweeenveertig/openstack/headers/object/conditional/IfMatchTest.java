package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.exception.ModifiedException;
import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class IfMatchTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new IfMatch("cafebabe"));
    }

    @Test
    public void contentMustBeTheSame() {
        new IfMatch("cafebabe").matchAgainst("cafebabe");
    }

    @Test(expected = ModifiedException.class)
    public void differentContentIsError() {
        new IfMatch("cafebabe").matchAgainst("ebacefac");
    }

}

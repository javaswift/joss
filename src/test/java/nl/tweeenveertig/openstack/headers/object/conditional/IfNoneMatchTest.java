package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.exception.NotModifiedException;
import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class IfNoneMatchTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new IfNoneMatch("cafebabe"));
    }

    @Test
    public void contentMustBeDifferent() {
        new IfNoneMatch("cafebabe").matchAgainst("ebabefac");
    }

    @Test(expected = NotModifiedException.class)
    public void sameContentIsError() {
        new IfNoneMatch("cafebabe").matchAgainst("cafebabe");
    }

}

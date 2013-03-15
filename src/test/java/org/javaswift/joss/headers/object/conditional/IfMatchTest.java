package org.javaswift.joss.headers.object.conditional;

import org.javaswift.joss.exception.ModifiedException;
import org.javaswift.joss.headers.AbstractHeaderTest;
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

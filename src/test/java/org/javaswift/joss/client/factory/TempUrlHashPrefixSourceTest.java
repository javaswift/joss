package org.javaswift.joss.client.factory;

import static junit.framework.Assert.assertEquals;

import org.javaswift.joss.exception.CommandException;
import org.junit.Test;

public class TempUrlHashPrefixSourceTest {

    @Test
    public void getPath() {
        assertEquals("/pick/me", TempUrlHashPrefixSource.getPath("http://localhost:8080/pick/me"));
    }

    @Test(expected = CommandException.class)
    public void illegalURL() {
        TempUrlHashPrefixSource.getPath("---===[[[000]]]===---");
    }

}

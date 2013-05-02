package org.javaswift.joss.client.website;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.util.FileAction;
import org.javaswift.joss.util.FileReference;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LocalFileObjectTest {

    @Test(expected = CommandException.class)
    public void getMd5(@Mocked final FileAction fileAction, @Mocked final FileReference fileReference) throws IOException {
        new NonStrictExpectations() {{
            fileReference.hasPath();
            result = true;
            FileAction.getMd5((File)any);
            result = new IOException();
        }};
        new LocalFileObject(fileReference);
    }

}

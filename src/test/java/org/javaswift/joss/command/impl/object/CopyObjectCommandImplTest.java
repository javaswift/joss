package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.object.CopyFrom;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CopyObjectCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        expectStatusCode(201);
        new CopyObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")).call();
        verifyHeaderValue("/container/objectName", CopyFrom.X_COPY_FROM);
    }

    @Test (expected = NotFoundException.class)
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new CopyObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new CopyObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new CopyObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")), 201);
    }
}

package org.javaswift.joss.command.object;

import org.javaswift.joss.command.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.object.CopyFrom;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CopyObjectCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new CopyObjectCommand(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("/container/objectName", requestArgument.getValue().getFirstHeader(CopyFrom.X_COPY_FROM).getValue());
    }

    @Test (expected = NotFoundException.class)
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new CopyObjectCommand(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new CopyObjectCommand(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new CopyObjectCommand(this.account, httpClient, defaultAccess, getObject("objectName"),
                getObject("objectName")), 201);
    }
}

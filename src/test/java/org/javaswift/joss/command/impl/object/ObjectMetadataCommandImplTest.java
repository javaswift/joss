package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.object.ObjectMetadata;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ObjectMetadataCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new ObjectMetadata("Year", "1989"));
        headers.add(new ObjectMetadata("Company", "42 BV"));
        new ObjectMetadataCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"), headers).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("1989", requestArgument.getValue().getFirstHeader(X_OBJECT_META_PREFIX + "Year").getValue());
        assertEquals("42 BV", requestArgument.getValue().getFirstHeader(X_OBJECT_META_PREFIX + "Company").getValue());
    }

    @Test (expected = NotFoundException.class)
    public void objectDoesNotExist() throws IOException {
        checkForError(404, new ObjectMetadataCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"), new ArrayList<Header>()));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ObjectMetadataCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"), new ArrayList<Header>()));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ObjectMetadataCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"), new ArrayList<Header>()), 202);
    }
}

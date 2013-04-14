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

import static org.javaswift.joss.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;

public class ObjectMetadataCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        expectStatusCode(202);
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new ObjectMetadata("Year", "1989"));
        headers.add(new ObjectMetadata("Company", "42 BV"));
        new ObjectMetadataCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName"), headers).call();
        verifyHeaderValue("1989", X_OBJECT_META_PREFIX + "Year", "POST");
        verifyHeaderValue("42 BV", X_OBJECT_META_PREFIX + "Company", "POST");
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

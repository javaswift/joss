package org.javaswift.joss.command.impl.container;

import mockit.Verifications;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.DirectoryOrObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ListDirectoryCommandImplTest extends BaseCommandTest {

    public static final Character DELIMITER = '/';

    public static final ListInstructions listInstructions = new ListInstructions()
            .setMarker(null)
            .setLimit(10);

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void listObjects() throws IOException {
        new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions, DELIMITER).call();
    }

    @Test
    public void listObjectsWithNoneThere() throws IOException {
        expectStatusCode(204);
        new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions, DELIMITER).call();
    }

    @Test (expected = NotFoundException.class)
    public void containerDoesNotExist() throws IOException {
        checkForError(404, new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions, DELIMITER));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions, DELIMITER));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions, DELIMITER));
    }

    @Test
    public void queryParameters() throws IOException {
        expectStatusCode(204);
        new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"),
                new ListInstructions().setDelimiter('/').setMarker("dogs").setLimit(10), DELIMITER).call();
        new Verifications() {{
            List<HttpRequestBase> requests = new ArrayList<HttpRequestBase>();
            httpClient.execute(withCapture(requests));
            for (HttpRequestBase request : requests) {
                String assertQueryParameters = "?marker=dogs&limit=10&delimiter=%2F";
                String uri = request.getURI().toString();
                assertTrue(uri+" must contain "+assertQueryParameters, uri.contains(assertQueryParameters));
            }
        }};
    }

    @Test
    public void setHeaderValues() throws IOException {
        loadSampleJson("/sample-directory-or-object-list.json");
        expectStatusCode(204);
        Collection<DirectoryOrObject> objects =
                new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), listInstructions, DELIMITER).call();
        assertEquals(7, objects.size());
        Iterator<DirectoryOrObject> iterator = objects.iterator();
        DirectoryOrObject directory = iterator.next();
        assertEquals("abc", directory.getAsDirectory().getBareName());
        DirectoryOrObject object = iterator.next();
        assertEquals("image/jpeg", object.getAsObject().getContentType());
        assertEquals("c8fc5698c0b3ca145f2c98937cbd9ff2", object.getAsObject().getEtag());
        assertTrue(object.getAsObject().getLastModified().contains("Wed, 05 Dec 2012"));
        assertEquals(22979, object.getAsObject().getContentLength());
    }

    @Test
    public void checkThatHeaderFieldsDoNotCostAnExtraCall() throws IOException {
        loadSampleJson("/sample-directory-or-object-list.json");
        expectStatusCode(204);
        Collection<DirectoryOrObject> objects =
                new ListDirectoryCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), listInstructions, DELIMITER).call();
        assertEquals(1, account.getNumberOfCalls());
        Iterator<DirectoryOrObject> iterator = objects.iterator();
        iterator.next(); // skip the Directory
        DirectoryOrObject object = iterator.next();
        object.getAsObject().getContentType();
        object.getAsObject().getContentLength();
        object.getAsObject().getEtag();
        object.getAsObject().getLastModified();
        assertEquals(1, account.getNumberOfCalls());
    }

}

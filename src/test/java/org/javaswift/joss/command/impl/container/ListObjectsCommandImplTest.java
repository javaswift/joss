package org.javaswift.joss.command.impl.container;

import mockit.Verifications;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.StoredObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ListObjectsCommandImplTest extends BaseCommandTest {

    public static final ListInstructions listInstructions = new ListInstructions()
            .setMarker(null)
            .setLimit(10);

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void listObjects() throws IOException {
        new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions).call();
    }

    @Test
    public void listObjectsWithNoneThere() throws IOException {
        expectStatusCode(204);
        new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions).call();
    }

    @Test (expected = NotFoundException.class)
    public void containerDoesNotExist() throws IOException {
        checkForError(404, new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions));
    }

    @Test
    public void queryParameters() throws IOException {
        expectStatusCode(204);
        new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"),
                new ListInstructions().setMarker("dogs").setLimit(10)).call();
        new Verifications() {{
            httpClient.execute((HttpRequestBase)any);
            forEachInvocation = new Object() {
                public void validate(HttpRequestBase request) {
                    String assertQueryParameters = "?marker=dogs&limit=10";
                    String uri = request.getURI().toString();
                    assertTrue(uri+" must contain "+assertQueryParameters, uri.contains(assertQueryParameters));
                }
            };
        }};
    }

    @Test
    public void setHeaderValues() throws IOException {
        loadSampleJson("/sample-object-list.json");
        expectStatusCode(204);
        Collection<StoredObject> objects =
                new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), listInstructions).call();
        assertEquals(4, objects.size());
        StoredObject object = objects.iterator().next();
        assertEquals("image/jpeg", object.getContentType());
        assertEquals("c8fc5698c0b3ca145f2c98937cbd9ff2", object.getEtag());
        assertTrue(object.getLastModified().contains("Wed, 05 Dec 2012"));
        assertEquals(22979, object.getContentLength());
    }

    @Test
    public void checkThatHeaderFieldsDoNotCostAnExtraCall() throws IOException {
        loadSampleJson("/sample-object-list.json");
        expectStatusCode(204);
        Collection<StoredObject> objects =
                new ListObjectsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), listInstructions).call();
        assertEquals(1, account.getNumberOfCalls());
        StoredObject object = objects.iterator().next();
        object.getContentType();
        object.getContentLength();
        object.getEtag();
        object.getLastModified();
        assertEquals(1, account.getNumberOfCalls());
    }

}

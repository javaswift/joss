package org.javaswift.joss.command.impl.account;

import mockit.Verifications;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Container;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ListContainersCommandTest extends BaseCommandTest {

    public static final ListInstructions listInstructions = new ListInstructions()
            .setMarker(null)
            .setLimit(10);

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void listContainers() throws IOException {
        new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions).call();
    }

    @Test
    public void listContainersWithNoneThere() throws IOException {
        expectStatusCode(204);
        new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions).call();
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions));
    }

    @Test
    public void queryParameters() throws IOException {
        expectStatusCode(204);
        new ListContainersCommandImpl(this.account, httpClient, defaultAccess,
                new ListInstructions().setPrefix("tst-").setMarker("dogs").setDelimiter('/').setLimit(10)).call();
        new Verifications() {{
            List<HttpRequestBase> requests = new ArrayList<HttpRequestBase>();
            httpClient.execute(withCapture(requests));
            for (HttpRequestBase request : requests) {
                String assertQueryParameters = "?prefix=tst-&marker=dogs&limit=10&delimiter=%2F";
                String uri = request.getURI().toString();
                assertTrue(uri+" must contain "+assertQueryParameters, uri.contains(assertQueryParameters));
            }
        }};
    }

    @Test
    public void setHeaderValues() throws IOException {
        loadSampleJson("/sample-container-list.json");
        expectStatusCode(204);
        Collection<Container> containers =
                new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions).call();
        assertEquals(4, containers.size());
        Container container = containers.iterator().next();
        assertEquals(1028296, container.getBytesUsed());
        assertEquals(48, container.getCount());
    }

    @Test
    public void checkThatHeaderFieldsDoNotCostAnExtraCall() throws IOException {
        loadSampleJson("/sample-container-list.json");
        expectStatusCode(204);
        Collection<Container> containers =
                new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions).call();
        assertEquals(1, account.getNumberOfCalls());
        Container container = containers.iterator().next();
        container.getBytesUsed();
        container.getCount();
        assertEquals(1, account.getNumberOfCalls());
    }

}

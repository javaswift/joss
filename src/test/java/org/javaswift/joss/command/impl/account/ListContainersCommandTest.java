package org.javaswift.joss.command.impl.account;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(statusLine.getStatusCode()).thenReturn(204);
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
        when(statusLine.getStatusCode()).thenReturn(204);
        new ListContainersCommandImpl(this.account, httpClient, defaultAccess,
                new ListInstructions().setPrefix("tst-").setMarker("dogs").setLimit(10)).call();
        verify(httpClient).execute(requestArgument.capture());
        String assertQueryParameters = "?prefix=tst-&marker=dogs&limit=10";
        String uri = requestArgument.getValue().getURI().toString();
        assertTrue(uri+" must contain "+assertQueryParameters, uri.contains(assertQueryParameters));
    }

    @Test
    public void setHeaderValues() throws IOException {
        when(httpEntity.getContent()).thenReturn(
                IOUtils.toInputStream(new ClasspathTemplateResource("/sample-container-list.json").loadTemplate()));
        when(statusLine.getStatusCode()).thenReturn(204);
        Collection<Container> containers =
                new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions).call();
        assertEquals(4, containers.size());
        Container container = containers.iterator().next();
        assertEquals(1028296, container.getBytesUsed());
        assertEquals(48, container.getCount());
    }

    @Test
    public void checkThatHeaderFieldsDoNotCostAnExtraCall() throws IOException {
        when(httpEntity.getContent()).thenReturn(
                IOUtils.toInputStream(new ClasspathTemplateResource("/sample-container-list.json").loadTemplate()));
        when(statusLine.getStatusCode()).thenReturn(204);
        Collection<Container> containers =
                new ListContainersCommandImpl(this.account, httpClient, defaultAccess, listInstructions).call();
        assertEquals(1, account.getNumberOfCalls());
        Container container = containers.iterator().next();
        container.getBytesUsed();
        container.getCount();
        assertEquals(1, account.getNumberOfCalls());
    }

}

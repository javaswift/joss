package org.javaswift.joss.client.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.javaswift.joss.headers.container.ContainerBytesUsed.X_CONTAINER_BYTES_USED;
import static org.javaswift.joss.headers.container.ContainerMetadata.X_CONTAINER_META_PREFIX;
import static org.javaswift.joss.headers.container.ContainerObjectCount.X_CONTAINER_OBJECT_COUNT;
import static org.javaswift.joss.headers.container.ContainerRights.X_CONTAINER_READ;
import static org.javaswift.joss.headers.container.ContainerWritePermissions.X_CONTAINER_WRITE;
import static org.javaswift.joss.headers.container.vipr.ProjectId.PROJECT_ID;
import static org.javaswift.joss.headers.container.vipr.Vpool.VPOOL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mockit.NonStrictExpectations;

import org.apache.http.Header;
import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.shared.identity.access.AccessTest;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.headers.account.HashPassword;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.headers.container.vipr.ProjectId;
import org.javaswift.joss.headers.container.vipr.Vpool;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.DirectoryOrObject;
import org.javaswift.joss.model.FormPost;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.util.HashSignature;
import org.javaswift.joss.util.LocalTime;
import org.junit.Before;
import org.junit.Test;

public class ContainerImplTest extends BaseCommandTest {

    private Container container;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        container = account.getContainer("alpha");
    }

    protected void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Year", "1989", headers);
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Company", "42 BV", headers);
        prepareHeader(response, X_CONTAINER_READ, ContainerRights.PUBLIC_CONTAINER, headers);
        prepareHeader(response, X_CONTAINER_WRITE, "1,4,3", headers);
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_CONTAINER_BYTES_USED, "654321", headers);
        prepareHeadersForRetrieval(response, headers);
    }

    @Test
    public void listObjects() throws IOException {
        loadSampleJson("/sample-object-list.json");
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "4", headers);
        prepareHeadersForRetrieval(response, headers);
        Collection<StoredObject> objects = container.list();
        assertEquals(4, objects.size());
    }

    @Test
    public void listDirectory() throws IOException {
        loadSampleJson("/sample-directory-or-object-list.json");
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "7", headers);
        prepareHeadersForRetrieval(response, headers);
        Collection<DirectoryOrObject> objects = container.listDirectory();
        assertEquals(7, objects.size());
    }

    @Test
    public void makePublic() throws IOException {
        expectStatusCode(202);
        container.makePublic();
        verifyHeaderValue(ContainerRights.PUBLIC_CONTAINER, X_CONTAINER_READ);
    }

    @Test
    public void makePrivate() throws IOException {
        expectStatusCode(202);
        container.makePrivate();
        verifyHeaderValue("", X_CONTAINER_READ);
    }

    @Test
    public void setContainerRights() throws IOException {
        expectStatusCode(202);
        container.setContainerRights("4,3,1", "1,3,4");
        verifyHeaderValue("4,3,1", X_CONTAINER_WRITE);
        verifyHeaderValue("1,3,4", X_CONTAINER_READ);
    }

    @Test
    public void createDelete() {
        expectStatusCode(201);
        container.create();
        expectStatusCode(204);
        container.delete();
    }

    @Test
    public void setMetadata() throws IOException {
        expectStatusCode(204);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", "1989");
        metadata.put("Company", "42 BV");
        container.setMetadata(metadata);
        verifyHeaderValue("1989", X_CONTAINER_META_PREFIX + "Year", "POST");
        verifyHeaderValue("42 BV", X_CONTAINER_META_PREFIX + "Company", "POST");
    }

    @Test
    public void getMetadata() throws IOException {
        expectStatusCode(204);
        prepareMetadata();
        assertEquals("1989", container.getMetadata("Year"));
        assertEquals("42 BV", container.getMetadata("Company"));
        assertTrue(container.isPublic());
        assertEquals(ContainerRights.PUBLIC_CONTAINER, container.getContainerReadPermission());
        assertEquals("1,4,3", container.getcontainerWritePermission());
        assertEquals(123, container.getCount());
        assertEquals(654321, container.getBytesUsed());
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void compareContainers() {
        Container container1 = account.getContainer("alpha");
        Container container2 = account.getContainer("beta");
        assertFalse(container1.equals("alpha"));
        assertFalse(container1.equals(container2));
        Map<Container, String> containers = new TreeMap<Container, String>();
        containers.put(container1, container1.getName());
        containers.put(container2, container2.getName());
        assertEquals(container1.getName(), containers.get(container1));
        assertEquals(container2.getName(), containers.get(container2));
        assertEquals(container1.getName().hashCode(), container1.hashCode());
    }

    @Test
    public void getFormPost(final String method, final LocalTime localTime) {
        final String redirectUrl = "http://do.not.follow/me";
        final long maxFileSize = 104857600;
        final long maxFileCount = 10;
        final String password = "welkom#42";
        final long todayInMS = 1369581129861L;
        final long oneDayInSeconds = 86400L;

        final AbstractContainer container = createContainerForFormPost(password);
        // Make sure that a fixed date is used
        useFixedDateForToday(todayInMS);
        // Check whether the secure URL contains the right signature and expiry date
        FormPost formPost = container.getFormPost(redirectUrl, maxFileSize, maxFileCount, oneDayInSeconds);
        String plainText = "/internal/path/alpha\n"+redirectUrl+"\n"+maxFileSize+"\n"+maxFileCount+"\n"+formPost.getExpires();
        String signature = HashSignature.getSignature(password, plainText);
        assertEquals("The signature must match", signature, formPost.getSignature());
    }

    @Test
    public void setCustomHeaders() throws IOException {
        expectStatusCode(201);
        List<org.javaswift.joss.headers.Header> customHeaders = new ArrayList<org.javaswift.joss.headers.Header>();
        customHeaders.add(new Vpool("42"));
        customHeaders.add(new ProjectId("1138"));
        container.setCustomHeaders(customHeaders);
        container.create();
        verifyHeaderValue("42", VPOOL, "PUT");
        verifyHeaderValue("1138", PROJECT_ID, "PUT");
    }

    protected void useFixedDateForToday(final long todayInMS) {
        new NonStrictExpectations(container) {{
            LocalTime.currentTime();
            result = todayInMS;
        }};
    }

    protected AbstractContainer createContainerForFormPost(String password) {
        account = new AccountImpl(null, httpClient, AccessTest.setUpAccessWithURLwithPaths(), true, TempUrlHashPrefixSource.INTERNAL_URL_PATH, '/');
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, AccountMetadata.X_ACCOUNT_META_PREFIX + HashPassword.X_ACCOUNT_TEMP_URL_KEY, password, headers);
        prepareHeadersForRetrieval(response, headers);
        account.setHashPassword("welkom#42");
        Container container = account.getContainer("alpha");
        return (AbstractContainer)container;
    }

}

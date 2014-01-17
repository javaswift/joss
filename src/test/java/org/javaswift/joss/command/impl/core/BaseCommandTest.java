package org.javaswift.joss.command.impl.core;

import mockit.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.command.shared.identity.access.AccessTenant;
import org.javaswift.joss.headers.Token;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.util.ClasspathTemplateResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

public abstract class BaseCommandTest {

    protected AccountImpl account;

    @Injectable
    protected AccessTenant defaultAccess;

    @Injectable
    protected HttpClient httpClient;

    @Injectable
    protected HttpResponse response;

    @Injectable
    protected HttpEntity httpEntity;

    @Injectable
    protected StatusLine statusLine;

    public void setup() throws IOException {
        final InputStream inputStream = IOUtils.toInputStream("[]");
        account = new AccountImpl(null, httpClient, defaultAccess, true, TempUrlHashPrefixSource.PUBLIC_URL_PATH);
        new NonStrictExpectations() {{
            defaultAccess.getInternalURL(); result = "http://someurl.nowhere/v1/AUTH_Account";
            defaultAccess.getPublicURL(); result = "http://someurl.public";
            defaultAccess.getToken(); result = "cafebabe";
            httpEntity.getContent(); result = inputStream;
            response.getEntity(); result = httpEntity;
            statusLine.getStatusCode(); result = 200;
            response.getStatusLine(); result = statusLine;
            httpClient.execute((HttpRequestBase)any); result = response;
        }};
    }

    protected StoredObject getObject(String name) {
        return account.getContainer("container").getObject(name);
    }

    protected void checkForError(final int httpStatusCode, AbstractCommand command) throws IOException {
        new Expectations() {{
            statusLine.getStatusCode(); result = httpStatusCode;
        }};
        command.call();
        fail("Should have thrown an exception");
    }

    public void setHttpEntity(final HttpEntity httpEntity) {
        new NonStrictExpectations() {{
            response.getEntity();
            result = httpEntity;
        }};
    }

    public static void prepareHeader(final HttpResponse response, final String name, final String value,
                                     final List<Header> headers) {
        new NonStrictExpectations() {{
            Header header = new Header() {
                @Override public String getName() { return name; }
                @Override public String getValue() { return value; }
                @Override public HeaderElement[] getElements() throws ParseException { return null; }
            };
            response.getHeaders(name); result = new Header[] { header };
        }};
        if (headers != null) {
            headers.add(response.getHeaders(name)[0]);
        }
    }

    public static void prepareHeadersForRetrieval(final HttpResponse response, final List<Header> headers) {
        new NonStrictExpectations() {{
            response.getAllHeaders();
            result = headers.toArray(new Header[headers.size()]);
        }};
    }

    protected void loadSampleJson(final String[] jsonFiles) throws IOException {
        new NonStrictExpectations() {{
            httpEntity.getContent();
            List<String> results = new ArrayList<String>();
            for (String json : jsonFiles) {
                result = IOUtils.toInputStream(new ClasspathTemplateResource(json).loadTemplate());
            }
        }};

    }

    protected void loadSampleJson(final String json) throws IOException {
        new NonStrictExpectations() {{
            httpEntity.getContent();
            result = IOUtils.toInputStream(new ClasspathTemplateResource(json).loadTemplate());
        }};
    }

    protected void expectStatusCode(final int statusCode) {
        expectStatusCode(statusCode, true);
    }

    protected void expectStatusCode(final int statusCode, boolean strict) {
        if (strict) {
            expectStatusCodeStrict(statusCode);
        } else {
            expectStatusCodeNonStrict(statusCode);
        }
    }

    protected void expectStatusCodeStrict(final int statusCode) {
        new Expectations() {{
            statusLine.getStatusCode(); result = statusCode;
        }};
    }

    protected void expectStatusCodeNonStrict(final int statusCode) {
        new NonStrictExpectations() {{
            statusLine.getStatusCode(); result = statusCode;
        }};
    }

    protected void prepareHeader(HttpResponse response, String name, String value) {
        prepareHeader(response, name, value, null);
    }

    protected void verifyHeaderValue(final String expectedValue, final String header, final String matchHttpMethod) throws IOException {
        new Verifications() {{
            List<HttpRequestBase> requests = new ArrayList<>();
            httpClient.execute(withCapture(requests));
            for (HttpRequestBase request : requests) {
                if (matchHttpMethod == null || matchHttpMethod.equals(request.getMethod())) {
                    if (expectedValue == null) {
                        assertNull(request.getFirstHeader(header));
                    } else {
                        assertEquals(expectedValue, request.getFirstHeader(header).getValue());
                    }
                }
            }
        }};
    }

    protected void verifyHeaderValue(final String expectedValue, final String header) throws IOException {
        verifyHeaderValue(expectedValue, header, null);
    }

    protected void isSecure(AbstractCommand command, final int expectedOk) throws IOException {
        new NonStrictExpectations() {{
            statusLine.getStatusCode(); result = expectedOk;
        }};
        command.call();
        verifyHeaderValue("cafebabe", Token.X_AUTH_TOKEN);
    }

    protected void isSecure(AbstractCommand command) throws IOException {
        isSecure(command, 200);
    }

}

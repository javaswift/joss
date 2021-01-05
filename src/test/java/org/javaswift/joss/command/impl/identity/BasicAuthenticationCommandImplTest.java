package org.javaswift.joss.command.impl.identity;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.command.impl.identity.BasicAuthenticationCommandImpl.X_AUTH_TOKEN;
import static org.javaswift.joss.command.impl.identity.BasicAuthenticationCommandImpl.X_STORAGE_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.headers.identity.XAuthKey;
import org.javaswift.joss.headers.identity.XAuthUser;
import org.javaswift.joss.model.Access;
import org.junit.Before;
import org.junit.Test;

public class BasicAuthenticationCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getUrl() {
        AuthenticationCommand command = new BasicAuthenticationCommandImpl(httpClient, "someurl", "user", "pwd", null);
        assertEquals("someurl", command.getUrl());
    }

    @Test
    public void call() throws Exception {

        final String storageUrl = "http://www.store-files-here.com";
        final String user = "user";
        final String key = "key";
        final String authenticationUrl = "http://www.authenticate-here.com";
        final String token = "CAFEBABE";

        new NonStrictExpectations() {{
            response.getEntity(); result = httpEntity;
            statusLine.getStatusCode(); result = 200;
            response.getStatusLine(); result = statusLine;
            response.getFirstHeader(X_STORAGE_URL); result = createHeader(X_STORAGE_URL, storageUrl);
            response.getFirstHeader(X_AUTH_TOKEN); result = createHeader(X_AUTH_TOKEN, token);
            httpClient.execute((HttpRequestBase)any); result = response;
        }};

        Access access = new BasicAuthenticationCommandImpl(httpClient, authenticationUrl, user, key, null).call();
        new Verifications() {{
            List<HttpRequestBase> requests = new ArrayList<HttpRequestBase>();
            httpClient.execute(withCapture(requests));
            for (HttpRequestBase request : requests) {
                assertEquals(user, request.getFirstHeader(XAuthUser.X_AUTH_USER).getValue());
                assertEquals(key, request.getFirstHeader(XAuthKey.X_AUTH_KEY).getValue());
            }
        }};

        assertEquals(storageUrl, access.getPublicURL());
        assertEquals(storageUrl, access.getInternalURL());
        assertEquals(token, access.getToken());
    }

}

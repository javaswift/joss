package org.javaswift.joss.command.identity;

import org.javaswift.joss.command.core.*;
import org.javaswift.joss.command.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.command.identity.authentication.Authentication;
import org.javaswift.joss.exception.CommandException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class AuthenticationCommand extends AbstractCommand<HttpPost, AccessImpl> {

    public AuthenticationCommand(HttpClient httpClient, String url, String tenant, String username, String password) {
        super(httpClient, url);
        setAuthenticationHeader(tenant, username, password);
    }

    private void setAuthenticationHeader(String tenant, String username, String password) {
        try {
            Authentication auth = new Authentication(tenant, username, password);
            String jsonString = createObjectMapper(true).writeValueAsString(auth);
            StringEntity input = new StringEntity(jsonString);
            input.setContentType("application/json");
            request.setEntity(input);
        } catch (IOException err) {
            throw new CommandException("Unable to set the JSON body for the authentication header", err);
        }
    }

    @Override
    public AccessImpl getReturnObject(HttpResponse response) throws IOException {
        return createObjectMapper(true)
                .readValue(response.getEntity().getContent(), AccessImpl.class)
                .initCurrentEndPoint(); // If only this would exist: http://jira.codehaus.org/browse/JACKSON-645
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }
}

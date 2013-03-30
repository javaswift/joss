package org.javaswift.joss.command.impl.identity;

import org.javaswift.joss.command.impl.core.*;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.identity.authentication.Authentication;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.exception.CommandException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class AuthenticationCommandImpl extends AbstractCommand<HttpPost, AccessImpl> implements AuthenticationCommand {

    public AuthenticationCommandImpl(HttpClient httpClient, String url, String tenant, String username, String password) {
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
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }
}

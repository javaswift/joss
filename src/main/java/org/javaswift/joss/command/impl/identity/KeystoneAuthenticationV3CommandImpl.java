package org.javaswift.joss.command.impl.identity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.impl.core.AbstractCommand;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.KeystoneV3Access;
import org.javaswift.joss.command.shared.identity.authentication.KeystoneV3Authentication;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Accept;
import org.javaswift.joss.model.Access;

import java.io.IOException;

public class KeystoneAuthenticationV3CommandImpl extends AbstractCommand<HttpPost, Access> implements AuthenticationCommand {

    private final String url;
    private final ObjectMapper requestMapper;
    private final ObjectMapper responseMapper;

    public KeystoneAuthenticationV3CommandImpl(HttpClient httpClient, AccountConfig config) {
        super(httpClient, config.getAuthUrl());

        this.url = config.getAuthUrl();
        this.requestMapper = createObjectMapper(true);
        this.responseMapper = createObjectMapper(false);

        setHeader(new Accept(ContentType.APPLICATION_JSON.getMimeType()));
        setRequestBody(config.getUsername(), config.getPassword(), config.getDomain());
    }

    private void setRequestBody(String username, String password, String domain) {
        try {
            String jsonString = requestMapper.writeValueAsString(
                new KeystoneV3Authentication(username, password, domain)
            );
            request.setEntity(
                new StringEntity(jsonString, ContentType.APPLICATION_JSON)
            );
        } catch (IOException ex) {
            throw new CommandException("Unable to set the JSON body on the request", ex);
        }
    }

    @Override
    public Access getReturnObject(HttpResponse response) throws IOException {
        String tokenValue = response.getFirstHeader("X-Subject-Token").getValue();

        JsonNode responseBody = responseMapper.readTree(response.getEntity().getContent());

        return new KeystoneV3Access(tokenValue, responseBody);
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

    @Override
    public String getUrl() {
        return this.url;
    }
}

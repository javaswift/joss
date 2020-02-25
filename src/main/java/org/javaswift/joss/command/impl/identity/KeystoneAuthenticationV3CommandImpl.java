package org.javaswift.joss.command.impl.identity;

import java.io.IOException;

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
import org.javaswift.joss.command.shared.identity.authentication.KeystoneV3Domain;
import org.javaswift.joss.command.shared.identity.authentication.KeystoneV3DomainScope;
import org.javaswift.joss.command.shared.identity.authentication.KeystoneV3Identity;
import org.javaswift.joss.command.shared.identity.authentication.KeystoneV3ProjectScope;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Accept;
import org.javaswift.joss.model.Access;

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

        KeystoneV3Authentication auth = null;
        switch (config.getAuthenticationMethodScope()) {
            case DOMAIN_NAME:
                auth = createKeystoneV3AuthenticationDomainScope(
                        config.getUsername(),
                        config.getPassword(),
                        config.getDomain()
                );
                break;
            case PROJECT_NAME:
                auth = createKeystoneV3AuthenticationProjectScope(
                        config.getUsername(),
                        config.getPassword(),
                        config.getDomain(),
                        config.getTenantName()
                );
                break;
            default:
                auth = createKeystoneV3AuthenticationDefaultScope(
                        config.getUsername(),
                        config.getPassword(),
                        config.getDomain()
                );
                break;
        }
        setRequestBody(auth);
    }

    private KeystoneV3Authentication createKeystoneV3AuthenticationDefaultScope(String username, String password, String domain) {
        KeystoneV3Authentication auth = new KeystoneV3Authentication();
        auth.setIdentity(new KeystoneV3Identity(username, password, domain));
        return auth;
    }

    private KeystoneV3Authentication createKeystoneV3AuthenticationProjectScope(String username, String password, String domain, String project) {
        KeystoneV3Authentication auth = new KeystoneV3Authentication();
        auth.setIdentity(new KeystoneV3Identity(username, password, domain));
        auth.setScope(new KeystoneV3ProjectScope(project, new KeystoneV3Domain(domain)));
        return auth;
    }

    private KeystoneV3Authentication createKeystoneV3AuthenticationDomainScope(String username, String password, String domain) {
        KeystoneV3Authentication auth = new KeystoneV3Authentication();
        auth.setIdentity(new KeystoneV3Identity(username, password, domain));
        auth.setScope(new KeystoneV3DomainScope(new KeystoneV3Domain(domain)));
        return auth;
    }

    private void setRequestBody(KeystoneV3Authentication auth) {
        try {
            String jsonString = requestMapper.writeValueAsString(auth);
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
        return new HttpStatusChecker[]{
                new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}

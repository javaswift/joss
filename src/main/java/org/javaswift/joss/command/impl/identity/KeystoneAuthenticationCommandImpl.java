package org.javaswift.joss.command.impl.identity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.javaswift.joss.command.impl.core.AbstractCommand;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.AccessNoTenant;
import org.javaswift.joss.command.shared.identity.access.AccessTenant;
import org.javaswift.joss.command.shared.identity.authentication.Authentication;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Accept;
import org.javaswift.joss.model.Access;

import java.io.IOException;

public class KeystoneAuthenticationCommandImpl extends AbstractCommand<HttpPost, Access> implements AuthenticationCommand {

    private boolean tenantSupplied;

    private String url;

    public KeystoneAuthenticationCommandImpl(HttpClient httpClient, String url, String tenantName, String tenantId, String username, String password) {
        super(httpClient, url);
        setAuthenticationHeader(tenantName, tenantId, username, password);
        setTenantSupplied(tenantName, tenantId);
        setHeader(new Accept("application/json"));
        this.url = url;
    }

    private void setTenantSupplied(String tenantName, String tenantId) {
        this.tenantSupplied = tenantName != null || tenantId != null;
    }

    private boolean isTenantSupplied() {
        return this.tenantSupplied;
    }

    private void setAuthenticationHeader(String tenantName, String tenantId, String username, String password) {
        try {
            Authentication auth = new Authentication(tenantName, tenantId, username, password);
            String jsonString = createObjectMapper(true).writeValueAsString(auth);
            StringEntity input = new StringEntity(jsonString);
            input.setContentType("application/json");
            request.setEntity(input);
        } catch (IOException err) {
            throw new CommandException("Unable to set the JSON body for the authentication header", err);
        }
    }

    @Override
    public Access getReturnObject(HttpResponse response) throws IOException {
        if (isTenantSupplied()) {
            return createObjectMapper(true)
                    .readValue(response.getEntity().getContent(), AccessTenant.class);
        } else { // This logic exists solely to support a Folsom glitch. See https://github.com/javaswift/joss/issues/33
            return createObjectMapper(true)
                    .readValue(response.getEntity().getContent(), AccessNoTenant.class);
        }
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

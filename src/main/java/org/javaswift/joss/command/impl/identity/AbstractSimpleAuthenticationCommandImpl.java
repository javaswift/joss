package org.javaswift.joss.command.impl.identity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.impl.core.AbstractCommand;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.AccessBasic;
import org.javaswift.joss.headers.identity.XAuthKey;
import org.javaswift.joss.headers.identity.XAuthUser;
import org.javaswift.joss.model.Access;

import java.io.IOException;

public abstract class AbstractSimpleAuthenticationCommandImpl extends AbstractCommand<HttpGet, Access> implements AuthenticationCommand {

    public static final String X_AUTH_TOKEN  = "X-Auth-Token";
    public static final String X_STORAGE_URL = "X-Storage-Url";

    private String url;

    public AbstractSimpleAuthenticationCommandImpl(HttpClient httpClient, String url) {
        super(httpClient, url);
        this.url = url;
    }

    @Override
    public Access getReturnObject(HttpResponse response) throws IOException {
        AccessBasic access = new AccessBasic();
        access.setToken(response.getFirstHeader(X_AUTH_TOKEN).getValue());
        access.setUrl(response.getFirstHeader(X_STORAGE_URL).getValue());
        return access;
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
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

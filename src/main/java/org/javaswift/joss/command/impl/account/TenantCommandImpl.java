package org.javaswift.joss.command.impl.account;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.account.TenantCommand;
import org.javaswift.joss.command.shared.identity.tenant.Tenants;
import org.javaswift.joss.headers.Accept;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;

public class TenantCommandImpl extends AbstractSecureCommand<HttpGet, Tenants> implements TenantCommand {

    public TenantCommandImpl(Account account, HttpClient httpClient, Access access, String url) {
        super(account, httpClient, modifyUrl(url), access.getToken());
        setHeader(new Accept("application/json"));
    }

    protected static String modifyUrl(String authUrl) {
        return authUrl.replaceAll("/tokens", "/tenants");
    }

    @Override
    protected Tenants getReturnObject(HttpResponse response) throws IOException {
        return createObjectMapper(false)
                .readValue(response.getEntity().getContent(), Tenants.class);
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusRange(200, 299)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}

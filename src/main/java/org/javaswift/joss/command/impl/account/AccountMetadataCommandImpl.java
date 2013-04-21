package org.javaswift.joss.command.impl.account;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.account.AccountMetadataCommand;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.model.Account;

import java.util.Collection;

public class AccountMetadataCommandImpl extends AbstractAccountCommand<HttpPost, Object> implements AccountMetadataCommand {

    public AccountMetadataCommandImpl(Account account, HttpClient httpClient, AccessImpl access, Collection<? extends Header> headers) {
        super(account, httpClient, access);
        addHeaders(headers);
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}

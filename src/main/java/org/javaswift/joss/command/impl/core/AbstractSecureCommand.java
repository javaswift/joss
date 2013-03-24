package org.javaswift.joss.command.impl.core;

import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.exception.UnauthorizedException;
import org.javaswift.joss.headers.Token;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractSecureCommand<M extends HttpRequestBase, N> extends AbstractCommand<M, N> {

    private Account account;

    public AbstractSecureCommand(Account account, HttpClient httpClient, String url, String token) {
        super(httpClient, url);
        this.account = account;
        setToken(token);
    }

    public AbstractSecureCommand(Account account, HttpClient httpClient, Access access) {
        this(account, httpClient, access.getInternalURL(), access.getToken());
    }

    @Override
    public N call() {
        account.increaseCallCounter();
        try {
            return super.call();
        } catch (UnauthorizedException err) {
            if (account.isAllowReauthenticate()) {
                Access access = account.authenticate();
                setToken(access.getToken());
                return super.call();
            }
            throw err;
        }
    }

    private void setToken(String token) {
        setHeader(new Token(token));
    }

}

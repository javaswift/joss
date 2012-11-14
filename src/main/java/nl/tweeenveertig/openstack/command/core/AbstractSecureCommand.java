package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.model.Access;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.exception.UnauthorizedException;
import nl.tweeenveertig.openstack.headers.Token;
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
        try {
            return super.call();
        } catch (UnauthorizedException err) {
            if (!account.isAllowReauthenticate()) {
                throw err;
            }
            Access access = account.authenticate();
            setToken(access.getToken());
            return super.call();
        }
    }

    private void setToken(String token) {
        setHeader(new Token(token));
    }

}

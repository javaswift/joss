package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.exception.UnauthorizedException;
import nl.tweeenveertig.openstack.headers.Token;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractSecureCommand<M extends HttpRequestBase, N extends Object> extends AbstractCommand<M, N> {

    private Account account;

    public AbstractSecureCommand(Account account, HttpClient httpClient, String url, String token) {
        super(httpClient, url, token);
        this.account = account;
        addToken(token);
    }

    public AbstractSecureCommand(Account account, HttpClient httpClient, Access access) {
        this(account, httpClient, access.getInternalURL(), access.getToken());
    }

    @Override
    public N call() {
        try {
            return super.call();
        } catch (UnauthorizedException err) {
            Access access = account.authenticate();
            addToken(access.getToken());
            return super.call();
        }
    }

    private void addToken(String token) {
        setHeader(new Token(token));
    }

}

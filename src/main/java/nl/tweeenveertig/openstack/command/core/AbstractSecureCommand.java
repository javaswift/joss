package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.exception.CommandException;
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
        } catch (CommandException err) {
            if (CommandExceptionError.UNAUTHORIZED.equals(err.getError())) {
                Access access = account.authenticate();
                removeHeaders(Token.X_AUTH_TOKEN);
                addToken(access.getToken());
                return super.call();
            } else {
                throw err; // No retry, just rethrow the error
            }
        }

    }

    private void addToken(String token) {
        addHeader(new Token(token));
    }

}

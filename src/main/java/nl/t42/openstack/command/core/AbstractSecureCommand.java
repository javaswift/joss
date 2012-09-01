package nl.t42.openstack.command.core;

import nl.t42.openstack.command.identity.access.Access;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractSecureCommand<M extends HttpRequestBase, N extends Object> extends AbstractCommand<M, N> {

    public static String X_AUTH_TOKEN = "X-Auth-Token";

    public AbstractSecureCommand(HttpClient httpClient, String url, String token) {
        super(httpClient, url, token);
        addToken(token);
    }

    public AbstractSecureCommand(HttpClient httpClient, Access access) {
        this(httpClient, access.getInternalURL(), access.getToken());
    }

    private void addToken(String token) {
        request.addHeader(X_AUTH_TOKEN, token);
    }

}

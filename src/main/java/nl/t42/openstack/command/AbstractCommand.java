package nl.t42.openstack.command;

import nl.t42.openstack.model.access.Access;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;
import java.util.List;

import static nl.t42.openstack.command.CommandUtil.convertResponseToString;

public abstract class AbstractCommand<M extends HttpRequestBase, N extends Object> {

    public static String X_AUTH_TOKEN = "X-Auth-Token";

    private HttpClient httpClient;

    protected M request;

    protected HttpResponse response;

    private AbstractCommand(HttpClient httpClient, String url, String token) {
        this.httpClient = httpClient;
        this.request = createRequest(url);
        if (isSecureCall()) {
            addToken(token);
        }
    }

    public AbstractCommand(HttpClient httpClient, String url) {
        this(httpClient, url, null);
    }

    public AbstractCommand(HttpClient httpClient, Access access) {
        this(httpClient, access.getInternalURL(), access.getToken());
    }

    private void addToken(String token) {
        request.addHeader(X_AUTH_TOKEN, token);
    }

    public N execute() throws IOException {
        response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed to setAuthenticationHeader: "+response.getStatusLine().getStatusCode());
        }
        convertResponseBody(convertResponseToString(response));
        return getReturnObject();
    }

    protected abstract boolean isSecureCall();

    protected abstract M createRequest(String url);

    protected N getReturnObject() {
        return null; // returns null by default
    }

    protected void convertResponseBody(List<String> responseBody) throws IOException {
        // empty by default
    }

}

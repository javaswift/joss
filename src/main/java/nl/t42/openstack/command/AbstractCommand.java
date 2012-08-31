package nl.t42.openstack.command;

import nl.t42.openstack.model.access.Access;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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

    public AbstractCommand(HttpClient httpClient, String url, String token) {
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
        checkHttStatusCode(response.getStatusLine().getStatusCode());
        return getReturnObject(convertResponseToString(response));
    }

    protected abstract boolean isSecureCall();

    protected abstract M createRequest(String url);

    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode != HttpStatus.SC_OK) {
            throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
        }
    }

    protected N getReturnObject(List<String> responseBody) throws IOException {
        return null; // returns null by default
    }
}

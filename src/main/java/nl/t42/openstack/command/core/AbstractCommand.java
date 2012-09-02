package nl.t42.openstack.command.core;

import nl.t42.openstack.command.identity.access.Access;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractCommand<M extends HttpRequestBase, N extends Object> {

    private HttpClient httpClient;

    protected M request;

    protected HttpResponse response;

    public AbstractCommand(HttpClient httpClient, String url, String token) {
        this.httpClient = httpClient;
        this.request = createRequest(url);
    }

    public AbstractCommand(HttpClient httpClient, String url) {
        this(httpClient, url, null);
    }

    public AbstractCommand(HttpClient httpClient, Access access) {
        this(httpClient, access.getInternalURL(), access.getToken());
    }

    public N execute() throws IOException {
        response = httpClient.execute(request);
        checkHttStatusCode(response.getStatusLine().getStatusCode());
        N object = getReturnObject(response);
        EntityUtils.consume(response.getEntity());
        return object;
    }

    protected abstract M createRequest(String url);

    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode != HttpStatus.SC_OK) {
            throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
        }
    }

    protected N getReturnObject(HttpResponse response) throws IOException {
        return null; // returns null by default
    }
}

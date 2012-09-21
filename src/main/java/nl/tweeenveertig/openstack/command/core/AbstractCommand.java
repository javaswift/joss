package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.headers.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class AbstractCommand<M extends HttpRequestBase, N extends Object> implements Callable<N>, Closeable {

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

    public N call() {
        try {
            response = httpClient.execute(request);
            HttpStatusChecker.verifyCode(getStatusCheckers(), response.getStatusLine().getStatusCode());
            return getReturnObject(response);
        } catch (IOException err) {
            throw new CommandException("Unable to execute the HTTP call or to convert the HTTP Response", err);
        } finally {
            if (closeStreamAutomatically()) {
                try { close(); } catch (IOException err) { /* ignore */ }
            }
        }
    }

    protected void removeHeaders(String headerName) {
        this.request.removeHeaders(headerName);
    }

    protected void addHeader(Header header) {
        if (header == null) {
            return;
        }
        header.addHeader(request);
    }

    public void close() throws IOException {
        EntityUtils.consume(response.getEntity());
    }

    protected boolean closeStreamAutomatically() {
        return true;
    }

    protected abstract M createRequest(String url);

    protected abstract HttpStatusChecker[] getStatusCheckers();

    protected N getReturnObject(HttpResponse response) throws IOException {
        return null; // returns null by default
    }
}

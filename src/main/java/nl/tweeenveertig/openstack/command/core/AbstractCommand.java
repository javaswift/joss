package nl.tweeenveertig.openstack.command.core;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.concurrent.Callable;

import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.headers.Header;
import nl.tweeenveertig.openstack.instructions.QueryParameters;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

public abstract class AbstractCommand<M extends HttpRequestBase, N> implements Callable<N>, Closeable {

    private HttpClient httpClient;

    protected M request;

    protected HttpResponse response;

    public AbstractCommand(HttpClient httpClient, String url) {
        this.httpClient = httpClient;
        this.request = createRequest(url);
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

    protected void setHeader(Header header) {
        if (header == null) {
            return;
        }
        header.setHeader(request);
    }

    public void close() throws IOException {
        if (response != null) {
            EntityUtils.consume(response.getEntity());
        }
    }

    protected boolean closeStreamAutomatically() {
        return true;
    }

    protected void addHeaders(Collection<? extends Header> headers) {
        for (Header header : headers) {
            setHeader(header);
        }
    }

    protected abstract M createRequest(String url);

    protected abstract HttpStatusChecker[] getStatusCheckers();

    protected N getReturnObject(HttpResponse response) throws IOException {
        return null; // returns null by default
    }

    protected void modifyURI(QueryParameters queryParameters) {
        request.setURI(URI.create(queryParameters.createUrl(request.getURI().toString())));
    }
}

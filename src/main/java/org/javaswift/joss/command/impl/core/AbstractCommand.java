package org.javaswift.joss.command.impl.core;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.QueryParameters;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCommand<M extends HttpRequestBase, N> implements Callable<N>, Closeable {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

    private HttpClient httpClient;

    protected M request;

    protected HttpResponse response;

    public AbstractCommand(HttpClient httpClient, String url) {
        this.httpClient = httpClient;
        this.request = createRequest(url);
    }

    public N call() {
        LOG.debug("JOSS / Sending HTTP "+request.getMethod()+" call "+request.getURI().toString());
        try {
            response = httpClient.execute(request);
            HttpStatusChecker.verifyCode(getStatusCheckers(), response.getStatusLine().getStatusCode());
            return getReturnObject(response);
        } catch (CommandException err) {
            LOG.error(
                    "JOSS / HTTP "+request.getMethod()+
                    " call "+request.getURI().toString()+
                    (err.getHttpStatusCode() == 0 ? "" : ", HTTP status "+err.getHttpStatusCode())+
                    (err.getError() == null ? "" : ", Error "+err.getError())+
                    (err.getMessage() == null ? "" : ", Message '"+err.getMessage()+"'")+
                    (err.getCause() == null ? "" : ", Cause "+err.getCause().getClass().getSimpleName()));

            for (org.apache.http.Header header : request.getAllHeaders()) {
                LOG.error(header.getName()+"="+header.getValue());
            }
            throw err;
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

    protected ObjectMapper createObjectMapper(boolean dealWithRootValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (dealWithRootValue) {
            objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
            objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        }
        return objectMapper;
    }

}

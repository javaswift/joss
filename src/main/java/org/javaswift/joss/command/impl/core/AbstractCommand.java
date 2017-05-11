package org.javaswift.joss.command.impl.core;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.QueryParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class AbstractCommand<M extends HttpRequestBase, N> implements Callable<N>, Closeable {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

    private HttpClient httpClient;

    protected M request;

    protected HttpResponse response;

    private boolean allowErrorLog = true;

    public AbstractCommand(HttpClient httpClient, String url) {
        this.httpClient = httpClient;
        this.request = createRequest(url);
    }

    public void setAllowErrorLog(boolean allowErrorLog) {
        this.allowErrorLog = allowErrorLog;
    }

    public N call() {
        logCall(request);
        try {
            response = httpClient.execute(request);
            HttpStatusChecker.verifyCode(getStatusCheckers(), response.getStatusLine().getStatusCode());
            return getReturnObject(response);
        } catch (CommandException err) {
        	request.releaseConnection();
            if (allowErrorLog) { // This is disabled, for example, for exists(), where we want to ignore the exception
                logError(request, err);
            }
            throw err;
        } catch (IOException err) {
        	request.releaseConnection();
            throw new CommandException("Unable to execute the HTTP call or to convert the HTTP Response", err);
        } finally {
            if (closeStreamAutomatically()) {
                try { close(); } catch (IOException err) { /* ignore */ }
            }
        }
    }

    private void logCall(M request) {
        LOG.debug("JOSS / Sending "+getPrintableCall(request));
        for (String printableHeaderLine : getPrintableHeaderLines(request)) {
            LOG.debug("* "+printableHeaderLine);
        }
    }

    private void logError(M request, CommandException err) {
        LOG.error(
                "JOSS / "+getPrintableCall(request)+
                        (err.getHttpStatusCode() == 0 ? "" : ", HTTP status "+err.getHttpStatusCode())+
                        (err.getError() == null ? "" : ", Error "+err.getError())+
                        (err.getMessage() == null ? "" : ", Message '"+err.getMessage()+"'")+
                        (err.getCause() == null ? "" : ", Cause "+err.getCause().getClass().getSimpleName()));

        for (String printableHeaderLine : getPrintableHeaderLines(request)) {
            LOG.error("* "+printableHeaderLine);
        }
    }

    private String getPrintableCall(M request) {
        return "HTTP "+request.getMethod()+" call "+request.getURI().toString();
    }

    private List<String> getPrintableHeaderLines(M request) {
        List<String> printableHeaderLines = new ArrayList<String>();
        for (org.apache.http.Header header : request.getAllHeaders()) {
            printableHeaderLines.add(header.getName()+"="+header.getValue());
        }
        return printableHeaderLines;
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

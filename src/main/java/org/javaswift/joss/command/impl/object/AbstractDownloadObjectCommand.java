package org.javaswift.joss.command.impl.object;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

import java.io.IOException;

import static org.javaswift.joss.headers.object.ObjectManifest.X_OBJECT_MANIFEST;

public abstract class AbstractDownloadObjectCommand<M extends HttpGet, N> extends AbstractObjectCommand<HttpGet, N> {

    public static final String ETAG             = "ETag";
    public static final String CONTENT_LENGTH   = "Content-Length";

    public AbstractDownloadObjectCommand(Account account, HttpClient httpClient, Access access,
                                         StoredObject object, DownloadInstructions downloadInstructions) {
        super(account, httpClient, access, object);
        processDownloadInstructions(downloadInstructions);
    }

    private void processDownloadInstructions(DownloadInstructions downloadInstructions) {
        setHeader(downloadInstructions.getRange());
        setHeader(downloadInstructions.getMatchConditional());
        setHeader(downloadInstructions.getSinceConditional());
        if (downloadInstructions.getHeaders() != null) {
                for (Header header :
				downloadInstructions.getHeaders().
				values()) {
			setHeader(header);
		}
        }
    }

    @Override
    protected N getReturnObject(HttpResponse response) throws IOException {
        String expectedMd5 = getEtagHeader(response);
        boolean isManifest = Header.headerNotEmpty(response, X_OBJECT_MANIFEST);
        handleEntity(response.getEntity());
        if (    !isManifest &&  // Manifest files may not be checked
                HttpStatus.SC_PARTIAL_CONTENT != response.getStatusLine().getStatusCode()) {
                                // etag match on partial content makes no sense)
            String realMd5 = getMd5();
            if (    realMd5 != null &&
                    !expectedMd5.equals(realMd5)) { // Native Inputstreams are not checked for their MD5
                HttpStatusExceptionUtil.throwException(HttpStatus.SC_UNPROCESSABLE_ENTITY);
            }
        }
        return getObjectAsReturnObject();
    }

    /**
     * @param response to extract the etag from
     * @return null if there was no etag, "" if the header was there but empty, etag as String otherwise.
     */
    private String getEtagHeader(HttpResponse response) {
        org.apache.http.Header[] headers = response.getHeaders(ETAG);
        if(headers.length == 0) {
            return null;
        }
        String etagHeaderValue = response.getHeaders(ETAG)[0].getValue();
        if(etagHeaderValue == null) {
            return "";
        }
        return etagHeaderValue.replaceAll("\"", "");
    }

    protected abstract void handleEntity(HttpEntity entity) throws IOException;

    protected abstract String getMd5() throws IOException;

    protected abstract N getObjectAsReturnObject();

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_PARTIAL_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_MODIFIED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_PRECONDITION_FAILED))
        };
    }

}

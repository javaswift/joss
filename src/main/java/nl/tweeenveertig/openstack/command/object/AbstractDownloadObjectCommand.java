package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.*;
import nl.tweeenveertig.openstack.model.DownloadInstructions;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

public abstract class AbstractDownloadObjectCommand<M extends HttpGet, N extends Object> extends AbstractObjectCommand<HttpGet, N> {

    public static final String ETAG             = "ETag";
    public static final String CONTENT_LENGTH   = "Content-Length";

    private DownloadInstructions downloadInstructions;

    public AbstractDownloadObjectCommand(Account account, HttpClient httpClient, Access access, Container container,
                                         StoredObject object, DownloadInstructions downloadInstructions) {
        super(account, httpClient, access, container, object);
        processDownloadInstructions(downloadInstructions);
    }

    private void processDownloadInstructions(DownloadInstructions downloadInstructions) {
        addHeader(downloadInstructions.getRange());
        addHeader(downloadInstructions.getMatchConditional());
        addHeader(downloadInstructions.getSinceConditional());
    }

    @Override
    protected N getReturnObject(HttpResponse response) throws IOException {
        String expectedMd5 = response.getHeaders(ETAG)[0].getValue();

        handleEntity(response.getEntity());
        if (HttpStatus.SC_PARTIAL_CONTENT != response.getStatusLine().getStatusCode()) { // etag match on partial content makes no sense)
            String realMd5 = getMd5();
            if (    realMd5 != null &&
                    !expectedMd5.equals(realMd5)) { // Native Inputstreams are not checked for their MD5
                throw new CommandException(HttpStatus.SC_UNPROCESSABLE_ENTITY, CommandExceptionError.MD5_CHECKSUM);
            }
        }
        return getObjectAsReturnObject();
    }

    protected abstract void handleEntity(HttpEntity entity) throws IOException;

    protected abstract String getMd5() throws IOException;

    protected abstract N getObjectAsReturnObject();

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_OK), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_PARTIAL_CONTENT), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_MODIFIED), CommandExceptionError.CONTENT_NOT_MODIFIED, NotModifiedException.class),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_PRECONDITION_FAILED), CommandExceptionError.CONTENT_DIFFERENT, ModifiedException.class)
        };
    }

}

package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.HttpStatusRange;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.model.ObjectInformation;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class ObjectInformationCommand extends AbstractObjectCommand<HttpHead, ObjectInformation> {

    public static final String X_OBJECT_META_PREFIX      = "X-Object-Meta-";
    public static final String LAST_MODIFIED             = "Last-Modified";
    public static final String ETAG                      = "Etag";
    public static final String CONTENT_LENGTH            = "Content-Length";
    public static final String CONTENT_TYPE              = "Content-Type";

    public ObjectInformationCommand(Account account, HttpClient httpClient, Access access, Container container, StoredObject object) {
        super(account, httpClient, access, container, object);
    }

    @Override
    protected ObjectInformation getReturnObject(HttpResponse response) throws IOException {
        ObjectInformation info = new ObjectInformation();
        for (Header header : response.getAllHeaders()) {
            if (header.getName().startsWith(X_OBJECT_META_PREFIX)) {
                info.addMetadata(header.getName().substring(X_OBJECT_META_PREFIX.length()), header.getValue());
            }
        }
        info.setLastModified(response.getHeaders(LAST_MODIFIED)[0].getValue());
        info.setEtag(response.getHeaders(ETAG)[0].getValue());
        info.setContentLength(Integer.parseInt(response.getHeaders(CONTENT_LENGTH)[0].getValue()));
        info.setContentType(response.getHeaders(CONTENT_TYPE)[0].getValue());
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusRange(200, 299), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST)
        };
    }
}

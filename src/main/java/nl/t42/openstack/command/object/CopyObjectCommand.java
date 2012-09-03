package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;

public class CopyObjectCommand extends AbstractObjectCommand<HttpPut, Object> {

    public static final String X_COPY_FROM      = "X-Copy-From";
    public static final String CONTENT_LENGTH   = "Content-Length";

    public CopyObjectCommand(HttpClient httpClient, Access access, Container sourceContainer, StoreObject sourceObject,
                             Container targetContainer, StoreObject targetObject) throws IOException {
        super(httpClient, access, targetContainer, targetObject);
        request.addHeader(X_COPY_FROM, getObjectPath(sourceContainer, sourceObject));
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_CREATED), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST)
        };
    }

}

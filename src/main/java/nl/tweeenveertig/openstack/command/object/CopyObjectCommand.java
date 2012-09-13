package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.impl.AccountImpl;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class CopyObjectCommand extends AbstractObjectCommand<HttpPut, Object> {

    public static final String X_COPY_FROM      = "X-Copy-From";

    public CopyObjectCommand(AccountImpl account, HttpClient httpClient, Access access, Container sourceContainer,
                             StoredObject sourceObject, Container targetContainer, StoredObject targetObject) {
        super(account, httpClient, access, targetContainer, targetObject);
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

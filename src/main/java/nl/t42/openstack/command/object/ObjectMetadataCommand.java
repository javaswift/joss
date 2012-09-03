package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Map;

public class ObjectMetadataCommand extends AbstractObjectCommand<HttpPost, Object> {

    public static final String X_OBJECT_META_PREFIX = "X-Object-Meta-";

    public ObjectMetadataCommand(HttpClient httpClient, Access access, Container container, StoreObject object, Map<String, Object> metadata) {
        super(httpClient, access, container, object);
        addMetadata(metadata);
    }

    protected void addMetadata(Map<String, Object> metadata) {
        for (String name : metadata.keySet()) {
            request.addHeader(X_OBJECT_META_PREFIX+name, metadata.get(name).toString());
        }
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_ACCEPTED), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST)
        };
    }

}

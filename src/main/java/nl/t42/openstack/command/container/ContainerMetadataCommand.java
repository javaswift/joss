package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Map;

public class ContainerMetadataCommand extends AbstractContainerCommand<HttpPost, Object> {

    public static final String X_CONTAINER_META_PREFIX      = "X-Container-Meta-";

    public ContainerMetadataCommand(HttpClient httpClient, Access access, Container container, Map<String, Object> metadata) {
        super(httpClient, access, container);
        addMetadata(metadata);
    }

    protected void addMetadata(Map<String, Object> metadata) {
        for (String name : metadata.keySet()) {
            request.addHeader(X_CONTAINER_META_PREFIX+name, metadata.get(name).toString());
        }
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST)
        };
    }

}

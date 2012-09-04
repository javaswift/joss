package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class ContainerRightsCommand extends AbstractContainerCommand<HttpPut, String[]> {

    public static final String X_CONTAINER_READ = "X-Container-Read";

    public ContainerRightsCommand(HttpClient httpClient, Access access, Container container, boolean publicContainer) {
        super(httpClient, access, container);
        request.addHeader(X_CONTAINER_READ, publicContainer ? ".r:*" : "");
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_ACCEPTED), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST)
        };
    }

}

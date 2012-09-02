package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;

public class DeleteContainerCommand extends AbstractContainerCommand<HttpDelete, String[]> {

    public DeleteContainerCommand(HttpClient httpClient, Access access, Container container) {
        super(httpClient, access, container);
    }

    @Override
    protected HttpDelete createRequest(String url) {
        return new HttpDelete(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_CONFLICT), CommandExceptionError.CONTAINER_NOT_EMPTY)
        };
    }

}

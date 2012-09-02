package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
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
    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode == HttpStatus.SC_NO_CONTENT) {
            return;
        } else if (httpStatusCode == HttpStatus.SC_NOT_FOUND) {
            throw new CommandException(httpStatusCode, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        } else if (httpStatusCode == HttpStatus.SC_CONFLICT) {
            throw new CommandException(httpStatusCode, CommandExceptionError.CONTAINER_NOT_EMPTY);
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }

}

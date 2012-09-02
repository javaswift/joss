package nl.t42.openstack.command.objectstorage;

import nl.t42.openstack.command.core.AbstractSecureCommand;
import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.command.objectstorage.model.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class CreateContainerCommand extends AbstractSecureCommand<HttpPut, String[]> {

    public CreateContainerCommand(HttpClient httpClient, Access access, Container container) {
        super(httpClient, access.getInternalURL() + "/" + container.getName(), access.getToken());
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode == HttpStatus.SC_CREATED) {
            return;
        } else if (httpStatusCode == HttpStatus.SC_ACCEPTED) {
            throw new CommandException(httpStatusCode, CommandExceptionError.CONTAINER_ALREADY_EXISTS);
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }
}

package nl.t42.openstack.command;

import nl.t42.openstack.model.access.Access;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.util.List;

public class CreateContainerCommand extends AbstractCommand<HttpPut, String[]> {

    public CreateContainerCommand(HttpClient httpClient, Access access, String containerName) {
        super(httpClient, access.getInternalURL() + "/" + containerName, access.getToken());
    }

    @Override
    protected boolean isSecureCall() {
        return true;
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

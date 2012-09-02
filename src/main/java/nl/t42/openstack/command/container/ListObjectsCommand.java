package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static nl.t42.openstack.command.core.CommandUtil.convertResponseToString;

public class ListObjectsCommand extends ContainerCommand<HttpGet, StoreObject[]> {

    public ListObjectsCommand(HttpClient httpClient, Access access, Container container) {
        super(httpClient, access, container);
    }

    @Override
    protected StoreObject[] getReturnObject(HttpResponse response) throws IOException {
        List<String> responseBody = convertResponseToString(response);
        List<StoreObject> storeObjects = new ArrayList<StoreObject>();
        for (String storeObjectName : responseBody) {
            storeObjects.add(new StoreObject(storeObjectName));
        }
        return storeObjects.toArray(new StoreObject[storeObjects.size()]);
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode == HttpStatus.SC_OK || httpStatusCode == HttpStatus.SC_NO_CONTENT) {
            return;
        } else if (httpStatusCode == HttpStatus.SC_NOT_FOUND) {
            throw new CommandException(httpStatusCode, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }
}

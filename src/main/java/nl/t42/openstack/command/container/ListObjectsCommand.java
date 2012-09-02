package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
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

public class ListObjectsCommand extends AbstractContainerCommand<HttpGet, StoreObject[]> {

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
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_OK), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST)
        };
    }
}

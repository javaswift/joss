package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoreObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static nl.tweeenveertig.openstack.command.core.CommandUtil.convertResponseToString;

public class ListObjectsCommand extends AbstractContainerCommand<HttpGet, Collection<StoreObject>> {

    public ListObjectsCommand(HttpClient httpClient, Access access, Container container) {
        super(httpClient, access, container);
    }

    @Override
    protected Collection<StoreObject> getReturnObject(HttpResponse response) throws IOException {
        List<String> responseBody = convertResponseToString(response);
        List<StoreObject> storeObjects = new ArrayList<StoreObject>();
        for (String storeObjectName : responseBody) {
            storeObjects.add(new StoreObject(storeObjectName));
        }
        return storeObjects;
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

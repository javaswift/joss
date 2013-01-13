package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.account.ContainerListElement;
import nl.tweeenveertig.openstack.instructions.ListInstructions;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListObjectsCommand extends AbstractContainerCommand<HttpGet, Collection<StoredObject>> {

    protected Container container;

    public ListObjectsCommand(Account account, HttpClient httpClient, AccessImpl access, Container container, ListInstructions listInstructions) {
        super(account, httpClient, access, container);
        this.container = container;
        modifyURI(listInstructions.getQueryParameters());
    }

    @Override
    protected Collection<StoredObject> getReturnObject(HttpResponse response) throws IOException {
        StoredObjectListElement[] list = createObjectMapper(false)
                .readValue(response.getEntity().getContent(), StoredObjectListElement[].class);
        List<StoredObject> objects = new ArrayList<StoredObject>();
        for (StoredObjectListElement header : list) {
            objects.add(container.getObject(header.name));
        }
        return objects;
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}

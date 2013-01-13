package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.command.ObjectStoreListElement;
import nl.tweeenveertig.openstack.instructions.ListInstructions;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.command.core.*;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import nl.tweeenveertig.openstack.model.Container;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListContainersCommand extends AbstractSecureCommand<HttpGet, Collection<Container>> {

    protected Account account;

    public ListContainersCommand(Account account, HttpClient httpClient, AccessImpl access, ListInstructions listInstructions) {
        super(account, httpClient, access);
        this.account = account;
        modifyURI(listInstructions.getQueryParameters());
    }

    @Override
    protected Collection<Container> getReturnObject(HttpResponse response) throws IOException {
        ObjectStoreListElement[] list = createObjectMapper(false)
                .readValue(response.getEntity().getContent(), ObjectStoreListElement[].class);
        List<Container> containers = new ArrayList<Container>();
        for (ObjectStoreListElement containerHeader : list) {
            containers.add(account.getContainer(containerHeader.name));
        }
        return containers;
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}

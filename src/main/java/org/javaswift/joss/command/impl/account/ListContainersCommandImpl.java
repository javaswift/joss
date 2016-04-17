package org.javaswift.joss.command.impl.account;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.account.ContainerListElement;
import org.javaswift.joss.command.shared.account.ListContainersCommand;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListContainersCommandImpl extends AbstractAccountCommand<HttpGet, Collection<Container>> implements ListContainersCommand {

    protected Account account;

    public ListContainersCommandImpl(Account account, HttpClient httpClient, Access access, ListInstructions listInstructions) {
        super(account, httpClient, access);
        this.account = account;
        modifyURI(listInstructions.getQueryParameters());
    }

    @Override
    protected Collection<Container> getReturnObject(HttpResponse response) throws IOException {
        ContainerListElement[] list = createObjectMapper(false)
                .readValue(response.getEntity().getContent(), ContainerListElement[].class);
        List<Container> containers = new ArrayList<Container>();
        for (ContainerListElement containerHeader : list) {
            Container container = account.getContainer(containerHeader.name);
            container.setCount(containerHeader.count);
            container.setBytesUsed(containerHeader.bytes);
            container.metadataSetFromHeaders();
            containers.add(container);
        }
        return containers;
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}

package org.javaswift.joss.command.impl.container;

import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.container.ContainerRightsCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.headers.container.ContainerRights;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class ContainerRightsCommandImpl extends AbstractContainerCommand<HttpPut, String[]> implements ContainerRightsCommand {

    public ContainerRightsCommandImpl(Account account, HttpClient httpClient, AccessImpl access, Container container, boolean publicContainer) {
        super(account, httpClient, access, container);
        setHeader(new ContainerRights(publicContainer));
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_ACCEPTED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}

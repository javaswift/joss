package org.javaswift.joss.command.impl.container;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.container.ContainerRightsCommand;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

public class ContainerRightsCommandImpl extends AbstractContainerCommand<HttpPut, String[]> implements ContainerRightsCommand {

    public ContainerRightsCommandImpl(Account account, HttpClient httpClient, Access access, Container container, boolean publicContainer) {
        super(account, httpClient, access, container);
        setHeader(new ContainerRights(publicContainer));
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_ACCEPTED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}

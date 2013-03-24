package org.javaswift.joss.command.impl.container;

import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.model.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class CreateContainerCommand extends AbstractContainerCommand<HttpPut, Object> {

    public CreateContainerCommand(Account account, HttpClient httpClient, AccessImpl access, Container container) {
        super(account, httpClient, access, container);
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_CREATED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_ACCEPTED))
        };
    }

}

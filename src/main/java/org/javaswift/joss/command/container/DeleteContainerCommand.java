package org.javaswift.joss.command.container;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.model.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;

public class DeleteContainerCommand extends AbstractContainerCommand<HttpDelete, String[]> {

    public DeleteContainerCommand(Account account, HttpClient httpClient, AccessImpl access, Container container) {
        super(account, httpClient, access, container);
    }

    @Override
    protected HttpDelete createRequest(String url) {
        return new HttpDelete(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_CONFLICT))
        };
    }

}

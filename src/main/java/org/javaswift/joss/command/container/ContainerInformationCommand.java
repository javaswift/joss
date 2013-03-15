package org.javaswift.joss.command.container;

import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.headers.container.ContainerBytesUsed;
import org.javaswift.joss.headers.container.ContainerMetadata;
import org.javaswift.joss.headers.container.ContainerObjectCount;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.information.ContainerInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class ContainerInformationCommand extends AbstractContainerCommand<HttpHead, ContainerInformation> {

    public ContainerInformationCommand(Account account, HttpClient httpClient, AccessImpl access, Container container) {
        super(account, httpClient, access, container);
    }

    @Override
    protected ContainerInformation getReturnObject(HttpResponse response) throws IOException {
        ContainerInformation info = new ContainerInformation();
        info.setMetadata(ContainerMetadata.fromResponse(response));
        info.setObjectCount(ContainerObjectCount.fromResponse(response));
        info.setBytesUsed(ContainerBytesUsed.fromResponse(response));
        info.setPublicContainer(ContainerRights.fromResponse(response));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}

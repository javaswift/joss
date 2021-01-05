package org.javaswift.joss.command.impl.container;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.container.ContainerInformationCommand;
import org.javaswift.joss.headers.container.ContainerBytesUsed;
import org.javaswift.joss.headers.container.ContainerMetadata;
import org.javaswift.joss.headers.container.ContainerObjectCount;
import org.javaswift.joss.headers.container.ContainerReadPermissions;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.headers.container.ContainerWritePermissions;
import org.javaswift.joss.information.ContainerInformation;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

public class ContainerInformationCommandImpl extends AbstractContainerCommand<HttpHead, ContainerInformation> implements ContainerInformationCommand {

    public ContainerInformationCommandImpl(Account account, HttpClient httpClient, Access access, Container container, boolean allowErrorLog) {
        super(account, httpClient, access, container);
        setAllowErrorLog(allowErrorLog);
    }

    @Override
    protected ContainerInformation getReturnObject(HttpResponse response) throws IOException {
        ContainerInformation info = new ContainerInformation();
        info.setMetadata(ContainerMetadata.fromResponse(response));
        info.setObjectCount(ContainerObjectCount.fromResponse(response));
        info.setBytesUsed(ContainerBytesUsed.fromResponse(response));
        info.setPublicContainer(ContainerRights.fromResponse(response));
        info.setWritePermissions(ContainerWritePermissions.fromResponse(response));
        info.setReadPermissions(ContainerReadPermissions.fromResponse(response));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusRange(200, 299)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}

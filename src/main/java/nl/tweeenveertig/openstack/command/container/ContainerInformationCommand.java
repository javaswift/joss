package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.headers.container.ContainerBytesUsed;
import nl.tweeenveertig.openstack.headers.container.ContainerMetadata;
import nl.tweeenveertig.openstack.headers.container.ContainerObjectCount;
import nl.tweeenveertig.openstack.headers.container.ContainerRights;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import nl.tweeenveertig.openstack.model.ContainerInformation;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class ContainerInformationCommand extends AbstractContainerCommand<HttpHead, ContainerInformation> {

    public ContainerInformationCommand(Account account, HttpClient httpClient, Access access, Container container) {
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
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST)
        };
    }
}

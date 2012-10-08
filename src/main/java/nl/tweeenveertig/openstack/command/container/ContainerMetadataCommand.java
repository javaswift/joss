package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.headers.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Collection;

public class ContainerMetadataCommand extends AbstractContainerCommand<HttpPost, Object> {

    public ContainerMetadataCommand(Account account, HttpClient httpClient, Access access, Container container, Collection<? extends Header> headers) {
        super(account, httpClient, access, container);
        addHeaders(headers);
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}

package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;

public class DeleteContainerCommand extends AbstractContainerCommand<HttpDelete, String[]> {

    public DeleteContainerCommand(Account account, HttpClient httpClient, Access access, Container container) {
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

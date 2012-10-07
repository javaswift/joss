package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class CreateContainerCommand extends AbstractContainerCommand<HttpPut, Object> {

    public CreateContainerCommand(Account account, HttpClient httpClient, Access access, Container container) {
        super(account, httpClient, access, container);
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_CREATED), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_ACCEPTED), CommandExceptionError.ENTITY_ALREADY_EXISTS)
        };
    }

}

package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.Collection;

import static nl.tweeenveertig.openstack.command.core.CommandUtil.convertResponseToString;

public class ListObjectsCommand extends AbstractContainerCommand<HttpGet, Collection<String>> {

    public ListObjectsCommand(Account account, HttpClient httpClient, Access access, Container container) {
        super(account, httpClient, access, container);
    }

    @Override
    protected Collection<String> getReturnObject(HttpResponse response) throws IOException {
        return convertResponseToString(response);
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_OK), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST)
        };
    }
}

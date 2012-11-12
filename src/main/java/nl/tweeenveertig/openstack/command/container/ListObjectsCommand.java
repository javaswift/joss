package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import nl.tweeenveertig.openstack.model.Container;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.Collection;

import static nl.tweeenveertig.openstack.command.core.CommandUtil.convertResponseToString;

public class ListObjectsCommand extends AbstractContainerCommand<HttpGet, Collection<String>> {

    public ListObjectsCommand(Account account, HttpClient httpClient, AccessImpl access, Container container) {
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
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}

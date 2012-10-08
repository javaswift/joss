package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.*;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.Collection;

import static nl.tweeenveertig.openstack.command.core.CommandUtil.convertResponseToString;

public class ListContainersCommand extends AbstractSecureCommand<HttpGet, Collection<String>> {

    public ListContainersCommand(Account account, HttpClient httpClient, Access access) {
        super(account, httpClient, access);
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
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null)
        };
    }

}

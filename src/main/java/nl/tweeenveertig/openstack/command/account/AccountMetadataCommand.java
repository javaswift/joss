package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.AbstractSecureCommand;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.headers.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Collection;

public class AccountMetadataCommand extends AbstractSecureCommand<HttpPost, Object> {

    public AccountMetadataCommand(Account account, HttpClient httpClient, Access access, Collection<? extends Header> headers) {
        super(account, httpClient, access);
        addHeaders(headers);
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}

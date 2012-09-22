package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.AbstractSecureCommand;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.headers.account.AccountMetadata;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Map;

public class AccountMetadataCommand extends AbstractSecureCommand<HttpPost, Object> {

    public AccountMetadataCommand(Account account, HttpClient httpClient, Access access, Map<String, Object> metadata) {
        super(account, httpClient, access);
        addMetadata(metadata);
    }

    protected void addMetadata(Map<String, Object> metadata) {
        for (String name : metadata.keySet()) {
            addHeader(new AccountMetadata(name, metadata.get(name).toString()));
        }
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null)
        };
    }

}

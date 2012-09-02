package nl.t42.openstack.command.account;

import nl.t42.openstack.command.core.AbstractSecureCommand;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Map;

public class AccountMetadataCommand extends AbstractSecureCommand<HttpPost, Object> {

    public static final String X_ACCOUNT_META_PREFIX = "X-Account-Meta-";

    public AccountMetadataCommand(HttpClient httpClient, Access access, Map<String, Object> metadata) {
        super(httpClient, access);
        addMetadata(metadata);
    }

    protected void addMetadata(Map<String, Object> metadata) {
        for (String name : metadata.keySet()) {
            request.addHeader(X_ACCOUNT_META_PREFIX +name, metadata.get(name).toString());
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

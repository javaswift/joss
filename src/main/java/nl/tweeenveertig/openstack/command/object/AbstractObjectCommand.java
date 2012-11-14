package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.command.core.AbstractSecureCommand;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractObjectCommand<M extends HttpRequestBase, N> extends AbstractSecureCommand<M, N> {

    public AbstractObjectCommand(Account account, HttpClient httpClient, AccessImpl access, Container container, StoredObject object) {
        super(account, httpClient, access.getInternalURL() + getObjectPath(container, object), access.getToken());
    }

    protected static String getObjectPath(Container container, StoredObject object) {
        return "/" + object.getPath();
    }
}

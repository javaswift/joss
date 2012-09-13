package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.impl.AccountImpl;
import nl.tweeenveertig.openstack.command.core.AbstractSecureCommand;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.command.identity.authentication.Authentication;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractObjectCommand<M extends HttpRequestBase, N extends Object> extends AbstractSecureCommand<M, N> {

    public AbstractObjectCommand(AccountImpl account, HttpClient httpClient, Access access, Container container, StoredObject object) {
        super(account, httpClient, access.getInternalURL() + getObjectPath(container, object), access.getToken());
    }


    protected static String getObjectPath(Container container, StoredObject object) {
        return "/" + container.getName() + "/" + object.getName();
    }
}

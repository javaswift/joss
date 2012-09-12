package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.AbstractSecureCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractObjectCommand<M extends HttpRequestBase, N extends Object> extends AbstractSecureCommand<M, N> {

    public AbstractObjectCommand(HttpClient httpClient, Access access, Container container, StoredObject object) {
        super(httpClient, access.getInternalURL() + getObjectPath(container, object), access.getToken());
    }

    protected static String getObjectPath(Container container, StoredObject object) {
        return "/" + container.getName() + "/" + object.getName();
    }
}

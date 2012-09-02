package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.AbstractSecureCommand;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractObjectCommand<M extends HttpRequestBase, N extends Object> extends AbstractSecureCommand<M, N> {

    public AbstractObjectCommand(HttpClient httpClient, Access access, Container container, StoreObject object) {
        super(httpClient, access.getInternalURL() + "/" + container.getName() + "/" + object.getName(), access.getToken());
    }

}

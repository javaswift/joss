package nl.t42.openstack.command.objectstorage;

import nl.t42.openstack.command.core.AbstractSecureCommand;
import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.command.objectstorage.model.Container;
import nl.t42.openstack.command.objectstorage.model.ContainerInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;

public abstract class ContainerCommand<M extends HttpRequestBase, N extends Object> extends AbstractSecureCommand<M, N> {

    public ContainerCommand(HttpClient httpClient, Access access, Container container) {
        super(httpClient, access.getInternalURL() + "/" + container.getName(), access.getToken());
    }
}

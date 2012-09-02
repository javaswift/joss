package nl.t42.openstack.command.objectstorage;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class ContainerInformationCommand extends ContainerCommand<HttpHead, ContainerInformation> {

    public static final String X_CONTAINER_META_DESCRIPTION = "X-Container-Meta-Description";
    public static final String X_CONTAINER_OBJECT_COUNT     = "X-Container-Object-Count";
    public static final String X_CONTAINER_BYTES_USED       = "X-Container-Bytes-Used";

    public ContainerInformationCommand(HttpClient httpClient, Access access, Container container) {
        super(httpClient, access, container);
    }

    @Override
    protected ContainerInformation getReturnObject(HttpResponse response) throws IOException {
        ContainerInformation info = new ContainerInformation();
        info.setDescription(response.getHeaders(X_CONTAINER_META_DESCRIPTION)[0].getValue());
        info.setObjectCount(Integer.parseInt(response.getHeaders(X_CONTAINER_OBJECT_COUNT)[0].getValue()));
        info.setBytesUsed(Long.parseLong(response.getHeaders(X_CONTAINER_BYTES_USED)[0].getValue()));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode == HttpStatus.SC_NO_CONTENT) {
            return;
        } else if (httpStatusCode == HttpStatus.SC_NOT_FOUND) {
            throw new CommandException(httpStatusCode, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }
}

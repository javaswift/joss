package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class ContainerInformationCommand extends AbstractContainerCommand<HttpHead, ContainerInformation> {

    public static final String X_CONTAINER_META_PREFIX      = "X-Container-Meta-";
    public static final String X_CONTAINER_OBJECT_COUNT     = "X-Container-Object-Count";
    public static final String X_CONTAINER_BYTES_USED       = "X-Container-Bytes-Used";
    public static final String X_CONTAINER_READ             = "X-Container-Read";

    public ContainerInformationCommand(HttpClient httpClient, Access access, Container container) {
        super(httpClient, access, container);
    }

    @Override
    protected ContainerInformation getReturnObject(HttpResponse response) throws IOException {
        ContainerInformation info = new ContainerInformation();
        for (Header header : response.getAllHeaders()) {
            if (header.getName().startsWith(X_CONTAINER_META_PREFIX)) {
                info.addMetadata(header.getName().substring(X_CONTAINER_META_PREFIX.length()), header.getValue());
            }
        }
        info.setPublicContainer(response.getHeaders(X_CONTAINER_READ).length > 0);
        info.setObjectCount(Integer.parseInt(response.getHeaders(X_CONTAINER_OBJECT_COUNT)[0].getValue()));
        info.setBytesUsed(Long.parseLong(response.getHeaders(X_CONTAINER_BYTES_USED)[0].getValue()));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST)
        };
    }
}

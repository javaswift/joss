package nl.t42.openstack.command;

import nl.t42.openstack.model.access.Access;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;

public class DeleteContainerCommand extends AbstractCommand<HttpDelete, String[]> {

    public DeleteContainerCommand(HttpClient httpClient, Access access, String containerName) {
        super(httpClient, access.getInternalURL() + "/" + containerName, access.getToken());
    }

    @Override
    protected boolean isSecureCall() {
        return true;
    }

    @Override
    protected HttpDelete createRequest(String url) {
        return new HttpDelete(url);
    }

    @Override
    protected int successCode() {
        return HttpStatus.SC_NO_CONTENT;
    }
}

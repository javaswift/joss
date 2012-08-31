package nl.t42.openstack.command;

import nl.t42.openstack.model.access.Access;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.List;

public class ListContainerCommand extends AbstractCommand<HttpGet, String[]> {

    private List<String> containers;

    public ListContainerCommand(HttpClient httpClient, Access access) {
        super(httpClient, access);
    }

    @Override
    protected String[] getReturnObject() {
        return this.containers.toArray(new String[this.containers.size()]);
    }

    @Override
    protected void convertResponseBody(List<String> responseBody) throws IOException {
        this.containers = responseBody;
    }

    @Override
    protected boolean isSecureCall() {
        return true;
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }
}

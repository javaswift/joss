package nl.t42.openstack.command;

import nl.t42.openstack.model.access.Access;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.List;

public class ListContainerCommand extends AbstractCommand<HttpGet, String[]> {

    public ListContainerCommand(HttpClient httpClient, Access access) {
        super(httpClient, access);
    }

    @Override
    protected String[] getReturnObject(List<String> responseBody) throws IOException {
        return responseBody.toArray(new String[responseBody.size()]);
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

package nl.t42.openstack.command.account;

import nl.t42.openstack.command.core.*;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static nl.t42.openstack.command.core.CommandUtil.convertResponseToString;

public class ListContainersCommand extends AbstractSecureCommand<HttpGet, Container[]> {

    public ListContainersCommand(HttpClient httpClient, Access access) {
        super(httpClient, access);
    }

    @Override
    protected Container[] getReturnObject(HttpResponse response) throws IOException {
        List<String> responseBody = convertResponseToString(response);
        List<Container> containers = new ArrayList<Container>();
        for (String containerName : responseBody) {
            containers.add(new Container(containerName));
        }
        return containers.toArray(new Container[containers.size()]);
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_OK), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null)
        };
    }

}

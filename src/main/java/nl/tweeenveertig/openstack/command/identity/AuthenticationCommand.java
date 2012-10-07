package nl.tweeenveertig.openstack.command.identity;

import nl.tweeenveertig.openstack.command.core.*;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.command.identity.authentication.Authentication;
import nl.tweeenveertig.openstack.exception.CommandException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.List;

import static nl.tweeenveertig.openstack.command.core.CommandUtil.convertResponseToString;
import static nl.tweeenveertig.openstack.command.core.CommandUtil.createObjectMapper;

public class AuthenticationCommand extends AbstractCommand<HttpPost, Access> {

    public AuthenticationCommand(HttpClient httpClient, String url, String tenant, String username, String password) {
        super(httpClient, url);
        setAuthenticationHeader(tenant, username, password);
    }

    private void setAuthenticationHeader(String tenant, String username, String password) {
        try {
            Authentication auth = new Authentication(tenant, username, password);
            String jsonString = createObjectMapper().writeValueAsString(auth);
            StringEntity input = new StringEntity(jsonString);
            input.setContentType("application/json");
            request.setEntity(input);
        } catch (IOException err) {
            throw new CommandException("Unable to set the JSON body for the authentication header", err);
        }
    }

    @Override
    public Access getReturnObject(HttpResponse response) throws IOException {
        return createObjectMapper().readValue(createSingleString(convertResponseToString(response)), Access.class);
    }

    protected String createSingleString(List<String> lines) {
        StringBuilder oneString = new StringBuilder();
        for (String line : lines) {
            oneString.append(line);
        }
        return oneString.toString();
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusRange(200, 299), null)
        };
    }
}

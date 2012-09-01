package nl.t42.openstack.command.identity;

import nl.t42.openstack.command.core.AbstractCommand;
import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.command.identity.authentication.Authentication;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

import static nl.t42.openstack.command.core.CommandUtil.convertResponseToString;
import static nl.t42.openstack.command.core.CommandUtil.createObjectMapper;

public class AuthenticationCommand extends AbstractCommand<HttpPost, Access> {

    public AuthenticationCommand(HttpClient httpClient, String url, String username, String password) throws IOException {
        super(httpClient, url);
        setAuthenticationHeader(username, password);
    }

    private void setAuthenticationHeader(String username, String password) throws IOException {
        Authentication auth = new Authentication(username, password);
        String jsonString = createObjectMapper().writeValueAsString(auth);
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        request.setEntity(input);
    }

    @Override
    public Access getReturnObject(HttpResponse response) throws IOException {
        return createObjectMapper().readValue(StringUtils.join(convertResponseToString(response), ""), Access.class);
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode == HttpStatus.SC_UNAUTHORIZED) {
            throw new CommandException(httpStatusCode, CommandExceptionError.UNAUTHORIZED);
        } else if (httpStatusCode >= HttpStatus.SC_OK && httpStatusCode < 300) {
            return;
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }

}

package nl.t42.openstack.command;

import nl.t42.openstack.model.access.Access;
import nl.t42.openstack.model.authentication.Authentication;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.List;

import static nl.t42.openstack.command.CommandUtil.createObjectMapper;

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
    public Access getReturnObject(List<String> responseBody) throws IOException {
        return createObjectMapper().readValue(StringUtils.join(responseBody, ""), Access.class);
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected boolean isSecureCall() {
        return false;
    }

}

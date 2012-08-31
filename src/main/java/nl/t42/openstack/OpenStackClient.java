package nl.t42.openstack;

import nl.t42.openstack.model.access.Access;
import nl.t42.openstack.model.authentication.Authentication;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OpenStackClient {

    private Access access;

    private ObjectMapper objectMapper;

    private HttpClient httpClient;

    private boolean authenticated = false;

    public OpenStackClient() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        this.httpClient = new DefaultHttpClient();
    }

    public void authenticate(String username, String password, String authUrl) throws IOException {
        this.access = null;
        this.authenticated = false;
        HttpResponse response = sendChallenge(username, password, authUrl);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed to authenticate: "+response.getStatusLine().getStatusCode());
        }
        this.access = convertResponseToAccess(response);
        this.authenticated = true;
    }

    private HttpResponse sendChallenge(String username, String password, String authUrl) throws IOException {
        HttpPost httpPost = new HttpPost(authUrl);
        Authentication auth = new Authentication(username, password);
        String jsonString = getObjectMapper().writeValueAsString(auth);
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        httpPost.setEntity(input);
        return getHttpClient().execute(httpPost);
    }

    private Access convertResponseToAccess(HttpResponse response) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        StringBuilder responseString = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            responseString.append(output);
        }
        Access access = getObjectMapper().readValue(responseString.toString(), Access.class);
        return access;
    }

    public boolean isAuthenticated() { return this.authenticated; }
    public void setObjectMapper(ObjectMapper objectMapper) { this.objectMapper = objectMapper; }
    public void setHttpClient(HttpClient httpClient) { this.httpClient = httpClient; }
    public ObjectMapper getObjectMapper() { return this.objectMapper; }
    public HttpClient getHttpClient() { return this.httpClient; }
}

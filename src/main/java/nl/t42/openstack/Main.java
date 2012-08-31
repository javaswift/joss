package nl.t42.openstack;

import nl.t42.openstack.model.authentication.Authentication;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String args[]) throws IOException {

        if (args.length != 3) {
            System.out.println("Use syntax: <USERNAME> <PASSWORD> <URL>");
            return;
        }
        String username = args[0];
        String password = args[1];
        String url = args[2];
        System.out.println("Executing with "+username+"/"+password+"@"+url);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        Authentication auth = new Authentication(username, password);
        String jsonString = mapper.writeValueAsString(auth);
        System.out.println("JSON: "+jsonString);
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        httpPost.setEntity(input);

        HttpResponse response = httpClient.execute(httpPost);

        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        StringBuilder responseString = new StringBuilder();
        System.out.println("Output from Server .... \n");
        String output;
        while ((output = br.readLine()) != null) {
            responseString.append(output);
        }
        System.out.println(responseString.toString());

    }

}

package nl.t42.openstack.command;

import org.apache.http.HttpResponse;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandUtil {

    protected static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        return objectMapper;
    }

    protected static List<String> convertResponseToString(HttpResponse response) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        List<String> responseString = new ArrayList<String>();
        String output;
        while ((output = br.readLine()) != null) {
            responseString.add(output);
        }
        return responseString;
    }
}

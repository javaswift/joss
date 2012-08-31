package nl.t42.openstack;

import java.io.IOException;

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

        OpenStackClient client = new OpenStackClient();
        client.authenticate(username, password, url);

        client.createContainer("Leiden");

        String[] containers = client.listContainers();
        for (String container : containers) {
            System.out.println(container);
        }

    }

}

package nl.t42.openstack;

import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;

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

//        client.createContainer(new Container("Leiden"));
//        client.deleteContainer(new Container("Leiden"));

        ContainerInformation info = client.containerInformation(new Container("Tilburg"));
        System.out.println("Object count: "+info.getObjectCount());
        System.out.println("Bytes used: "+info.getBytesUsed());
        System.out.println("Description: "+info.getDescription());

        Container[] containers = client.listContainers();
        for (Container container : containers) {
            System.out.println(container);
        }

    }

}

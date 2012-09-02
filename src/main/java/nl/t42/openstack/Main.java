package nl.t42.openstack;

import nl.t42.openstack.model.AccountInformation;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;
import nl.t42.openstack.model.StoreObject;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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

//        AccountInformation accountInformation = client.getAccountInformation();
//        System.out.println("Containers in use: "+accountInformation.getContainerCount());
//        System.out.println("Objects in use: "+accountInformation.getObjectCount());
//        System.out.println("Bytes used: "+accountInformation.getBytesUsed());

//        StoreObject objects[] = client.listObjects(new Container("Tilburg"));
//        for (StoreObject object : objects) {
//            System.out.println("* object -> "+object);
//        }

//        client.createContainer(new Container("Leiden"));
//        client.deleteContainer(new Container("Leiden"));

//        Map<String, Object> metadata = new TreeMap<String, Object>();
//        metadata.put("Description", "Kantoor Eindhoven, inclusief randgemeenten");
//        metadata.put("Province", "Noord Brabant");
//        metadata.put("Country", "Nederland");
//        client.setContainerMetadata(new Container("Eindhoven"), metadata);

        ContainerInformation info = client.getContainerInformation(new Container("Eindhoven"));

        System.out.println("Object count: "+info.getObjectCount());
        System.out.println("Bytes used: "+info.getBytesUsed());
        System.out.println("Description: "+info.getMetadata().get("Description"));
        System.out.println("Province: "+info.getMetadata().get("Province"));
        System.out.println("Country: "+info.getMetadata().get("Country"));

//
//        Container[] containers = client.listContainers();
//        for (Container container : containers) {
//            System.out.println(container);
//        }

    }

}

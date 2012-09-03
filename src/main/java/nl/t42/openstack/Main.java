package nl.t42.openstack;

import nl.t42.openstack.client.OpenStackClient;
import nl.t42.openstack.model.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
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

//        Map<String, Object> metadata = new TreeMap<String, Object>();
//        metadata.put("Year", "1989");
//        metadata.put("Owner", "42 BV");
//        client.setAccountInformation(metadata);

//        AccountInformation accountInformation = client.getAccountInformation();
//        System.out.println("Containers in use: "+accountInformation.getContainerCount());
//        System.out.println("Objects in use: "+accountInformation.getObjectCount());
//        System.out.println("Bytes used: "+accountInformation.getBytesUsed());
//        System.out.println("Year: "+accountInformation.getMetadata().get("Year"));
//        System.out.println("Owner: "+accountInformation.getMetadata().get("Owner"));

//        ContainerInformation info = client.getContainerInformation(new Container("Tilburg"));
//        System.out.println("\nBEFORE");
//        System.out.println("Object count: "+info.getObjectCount());
//        System.out.println("Bytes used: "+info.getBytesUsed());

//        InputStream inputStream = new FileInputStream(new File("/Users/robertbor/Downloads/dog.png"));
//        byte[] fileToUpload = IOUtils.toByteArray(inputStream);
//        inputStream.close();
//        client.uploadObject(new Container("Tilburg"), new StoreObject("somedog3.png"), fileToUpload);
//        client.uploadObject(new Container("Tilburg"), new StoreObject("somedog4.png"), new File("/Users/robertbor/Downloads/dog.png"));

//        ContainerInformation info2 = client.getContainerInformation(new Container("Tilburg"));
//        System.out.println("\nAFTER");
//        System.out.println("Object count: "+info2.getObjectCount());
//        System.out.println("Bytes used: "+info2.getBytesUsed());

        ObjectInformation info = client.getObjectInformation(new Container("Tilburg"), new StoreObject("somedog.png"));
        System.out.println("Last modified:  "+info.getLastModified());
        System.out.println("ETag:           "+info.getEtag());
        System.out.println("Content type:   "+info.getContentType());
        System.out.println("Content length: "+info.getContentLength());
        for (String key : info.getMetadata().keySet()) {
            System.out.println("META / "+key+": "+info.getMetadata().get(key));
        }

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
//        client.setContainerInformation(new Container("Eindhoven"), metadata);

//        ContainerInformation info = client.getContainerInformation(new Container("Eindhoven"));
//        System.out.println("Object count: "+info.getObjectCount());
//        System.out.println("Bytes used: "+info.getBytesUsed());
//        System.out.println("Description: "+info.getMetadata().get("Description"));
//        System.out.println("Province: "+info.getMetadata().get("Province"));
//        System.out.println("Country: "+info.getMetadata().get("Country"));

//
//        Container[] containers = client.listContainers();
//        for (Container container : containers) {
//            System.out.println(container);
//        }

    }

}

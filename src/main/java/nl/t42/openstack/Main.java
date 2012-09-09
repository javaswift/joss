package nl.t42.openstack;

import nl.t42.openstack.client.OpenStackClient;
import nl.t42.openstack.client.OpenStackClientImpl;
import nl.t42.openstack.model.*;
import org.apache.commons.codec.digest.DigestUtils;
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

        OpenStackClient client = new OpenStackClientImpl();
        client.authenticate(username, password, url);

//        client.makeContainerPublic(new Container("Eindhoven"));
//        client.makeContainerPrivate(new Container("Eindhoven"));

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

//        System.out.println("Tilburg: "+client.getContainerInformation(new Container("Tilburg")).isPublicContainer());
//        System.out.println("Breda: "+client.getContainerInformation(new Container("Breda")).isPublicContainer());
//        System.out.println("Eindhoven: " + client.getContainerInformation(new Container("Eindhoven")).isPublicContainer());

//        ContainerInformation info = client.getContainerInformation(new Container("Tilburg"));
//        System.out.println("Object count: "+info.getObjectCount());
//        System.out.println("Bytes used: "+info.getBytesUsed());
//        System.out.println("Public?: "+info.isPublicContainer());

//        InputStream inputStream = new FileInputStream(new File("/Users/robertbor/Downloads/dog.png"));
//        byte[] fileToUpload = IOUtils.toByteArray(inputStream);
//        inputStream.close();
//        client.uploadObject(new Container("Tilburg"), new StoreObject("somedog3.png"), fileToUpload);

//        Container container = new Container("Tilburg");
//        StoreObject sourceObject = new StoreObject("somedog5.png");
//        StoreObject targetObject = new StoreObject("newdog.png");
//        client.uploadObject(container, sourceObject, new File("/Users/robertbor/Downloads/dog.png"));
//        client.copyObject(container, sourceObject, container, targetObject);

        byte[] plaatje = client.downloadObject(new Container("Tilburg"), new StoreObject("doggie.png"));
        System.out.println("Grootte plaatje: " + plaatje.length);
        FileOutputStream fos = new FileOutputStream(new File("plaatje.png"));
        fos.write(plaatje);
        fos.close();

        client.downloadObject(new Container("Tilburg"), new StoreObject("doggie.png"), new File("plaatje2.png"));

//        uploadFile(client, "Tilburg", "doggie.png", new File("/Users/robertbor/Downloads/dog.png"));
//        client.uploadObject(new Container("Tilburg"), new StoreObject("doggie.png"), new File("/Users/robertbor/Downloads/dog.png"));
//        client.deleteObject(new Container("Tilburg"), new StoreObject("somedog5.png"));

//        ContainerInformation info2 = client.getContainerInformation(new Container("Tilburg"));
//        System.out.println("\nAFTER");
//        System.out.println("Object count: "+info2.getObjectCount());
//        System.out.println("Bytes used: "+info2.getBytesUsed());

//        Map<String, Object> metadata = new TreeMap<String, Object>();
//        metadata.put("Description", "Kantoor Eindhoven, inclusief randgemeenten");
//        metadata.put("Province", "Noord Brabant");
//        metadata.put("Country", "Nederland");
//        client.setContainerInformation(new Container("Eindhoven"), metadata);
//        client.setObjectInformation(new Container("Tilburg"), new StoreObject("somedog.png"), metadata);
//
//        ObjectInformation info = client.getObjectInformation(new Container("Tilburg"), new StoreObject("somedog.png"));
//        System.out.println("Last modified:  "+info.getLastModified());
//        System.out.println("ETag:           "+info.getEtag());
//        System.out.println("Content type:   "+info.getContentType());
//        System.out.println("Content length: "+info.getContentLength());
//        for (String key : info.getMetadata().keySet()) {
//            System.out.println("META / "+key+": "+info.getMetadata().get(key));
//        }

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

    public static void uploadFile(OpenStackClient client, String containerName, String objectName, File file) throws IOException {
        Container container = new Container(containerName);
        StoreObject object = new StoreObject(objectName);

        // TEST WITH PURE STREAM
        InputStream inputStream = new FileInputStream(file);
        client.uploadObject(container, object, inputStream);
        inputStream.close();

        ObjectInformation info = client.getObjectInformation(container, object);
        System.out.println("Last modified:  "+info.getLastModified());
        System.out.println("ETag:           "+info.getEtag());
        System.out.println("Content type:   "+info.getContentType());
        System.out.println("Content length: "+info.getContentLength());
        for (String key : info.getMetadata().keySet()) {
            System.out.println("META / "+key+": "+info.getMetadata().get(key));
        }
    }

}

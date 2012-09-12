package nl.tweeenveertig.openstack;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.impl.ClientImpl;
import nl.tweeenveertig.openstack.client.mock.AccountMock;
import nl.tweeenveertig.openstack.client.mock.ClientMock;
import nl.tweeenveertig.openstack.client.mock.MockUserStore;

import java.io.*;
import java.util.Collection;
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

//        Account account = new ClientImpl().authenticate(username, password, url);

//        Container container = account.getContainer("images");
//        container.create();
//        container.makePublic();
//
//        Collection<Container> containers = account.listContainers();
//        for (Container currentContainer : containers) {
//            System.out.println(currentContainer.getName());
//        }

//        StoredObject object = container.getObject("dog.png");
//        object.uploadObject(new File("/dog.png"));
//        System.out.println("Public URL: "+object.getPublicURL());

//        System.out.println("Last modified:  "+object.getLastModified());
//        System.out.println("ETag:           "+object.getEtag());
//        System.out.println("Content type:   "+object.getContentType());
//        System.out.println("Content length: "+object.getContentLength());
//
//        Collection<StoredObject> objects = container.listObjects();
//        for (StoredObject currentObject : objects) {
//            System.out.println(currentObject.getName());
//        }

//        object.downloadObject(new File("/Users/robertbor/Downloads/dog2.png"));

//        StoredObject newObject = container.getObject("new-dog.png");
//        object.copyObject(container, newObject);
//        object.delete();
//        System.out.println("Public URL: "+object.getPublicURL()); // no longer retrievable
//        System.out.println("Public URL: "+newObject.getPublicURL()); // the new URL

//        Map<String, Object> metadata = new TreeMap<String, Object>();
//        metadata.put("title", "Some Title");
//        metadata.put("department", "Some Department");
//        object.setMetadata(metadata);
//        container.setMetadata(metadata);
//        object.setMetadata(metadata);

//        metadata = object.getMetadata();
//        for (String name : metadata.keySet()) {
//            System.out.println("META / "+name+": "+metadata.get(name));
//        }

        ClientMock client = new ClientMock();
        MockUserStore users = new MockUserStore();
        users.addUser("testuser", "testpassword");
        client.setUsers(users);

//        OpenStackClient client = new OpenStackClientImpl();
//        client.authenticate(username, password, url);

//        Container container = new Container("Tilburg");
//        StoredObject object = new StoredObject("doggie.png");
//        System.out.println("Public URL: "+client.getPublicURL(container, object));

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
//        client.uploadObject(new Container("Tilburg"), new StoredObject("somedog3.png"), fileToUpload);

//        Container container = new Container("Tilburg");
//        StoredObject sourceObject = new StoredObject("somedog5.png");
//        StoredObject targetObject = new StoredObject("newdog.png");
//        client.uploadObject(container, sourceObject, new File("/Users/robertbor/Downloads/dog.png"));
//        client.copyObject(container, sourceObject, container, targetObject);

        // DOWNLOAD AN IMAGE TO A BYTE ARRAY AND THEN SAVE IT TO FILE
//        byte[] plaatje = client.downloadObject(new Container("Tilburg"), new StoredObject("doggie.png"));
//        System.out.println("Grootte plaatje: " + plaatje.length);
//        FileOutputStream fos = new FileOutputStream(new File("plaatje.png"));
//        fos.write(plaatje);
//        fos.close();

        // DOWNLOAD AN IMAGE TO FILE
//        client.downloadObject(new Container("Tilburg"), new StoredObject("doggie.png"), new File("plaatje2.png"));

        // DOWNLOAD AN IMAGE TO AN INPUTSTREAM AND SAVE IT TO FILE
//        InputStreamWrapper wrapper = client.downloadObjectAsInputStream(new Container("Tilburg"), new StoredObject("doggie.png"));
//        FileOutputStream fos = new FileOutputStream(new File("plaatje.png"));
//        IOUtils.copy(wrapper.getInputStream(), fos);
//        fos.close();
//        wrapper.closeStream(); // Important to clean up the original response object

//        uploadFile(client, "Tilburg", "doggie.png", new File("/Users/robertbor/Downloads/dog.png"));
//        client.uploadObject(new Container("Tilburg"), new StoredObject("doggie.png"), new File("/Users/robertbor/Downloads/dog.png"));
//        client.deleteObject(new Container("Tilburg"), new StoredObject("somedog5.png"));

//        ContainerInformation info2 = client.getContainerInformation(new Container("Tilburg"));
//        System.out.println("\nAFTER");
//        System.out.println("Object count: "+info2.getObjectCount());
//        System.out.println("Bytes used: "+info2.getBytesUsed());

//        Map<String, Object> metadata = new TreeMap<String, Object>();
//        metadata.put("Description", "Kantoor Eindhoven, inclusief randgemeenten");
//        metadata.put("Province", "Noord Brabant");
//        metadata.put("Country", "Nederland");
//        client.setContainerInformation(new Container("Eindhoven"), metadata);
//        client.setObjectInformation(new Container("Tilburg"), new StoredObject("somedog.png"), metadata);
//
//        ObjectInformation info = client.getObjectInformation(new Container("Tilburg"), new StoredObject("somedog.png"));
//        System.out.println("Last modified:  "+info.getLastModified());
//        System.out.println("ETag:           "+info.getEtag());
//        System.out.println("Content type:   "+info.getContentType());
//        System.out.println("Content length: "+info.getContentLength());
//        for (String key : info.getMetadata().keySet()) {
//            System.out.println("META / "+key+": "+info.getMetadata().get(key));
//        }

//        StoredObject objects[] = client.listObjects(new Container("Tilburg"));
//        for (StoredObject object : objects) {
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

//    public static void uploadFile(OpenStackClient client, String containerName, String objectName, File file) throws IOException {
//        Container container = new Container(containerName);
//        StoredObject object = new StoredObject(objectName);
//
//        // TEST WITH PURE STREAM
//        InputStream inputStream = new FileInputStream(file);
//        client.uploadObject(container, object, inputStream);
//        inputStream.close();
//
//        ObjectInformation info = client.getObjectInformation(container, object);
//        System.out.println("Last modified:  "+info.getLastModified());
//        System.out.println("ETag:           "+info.getEtag());
//        System.out.println("Content type:   "+info.getContentType());
//        System.out.println("Content length: "+info.getContentLength());
//        for (String key : info.getMetadata().keySet()) {
//            System.out.println("META / "+key+": "+info.getMetadata().get(key));
//        }
//    }

}

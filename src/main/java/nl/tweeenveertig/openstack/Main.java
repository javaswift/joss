package nl.tweeenveertig.openstack;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.UploadInstructions;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.mock.ClientMock;
import nl.tweeenveertig.openstack.client.mock.MockUserStore;

import java.io.*;

public class Main {
    public static void main(String args[]) throws IOException {

        if (args.length != 4) {
            System.out.println("Use syntax: <TENANT> <USERNAME> <PASSWORD> <URL>");
            return;
        }
        String tenant = args[0];
        String username = args[1];
        String password = args[2];
        String url = args[3];
        System.out.println("Executing with "+username+"/"+password+"@"+url);

//        Account account = new ClientImpl().authenticate(tenant, username, password, url, "AMS-1");
//        System.out.println("Bytes used:      "+account.getBytesUsed());
//        System.out.println("Container count: "+account.getContainerCount());
//        System.out.println("Object count:    "+account.getObjectCount());

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

        Account account = client.authenticate("tenant", "testuser", "testpassword", null);
        Container container = account.getContainer("alpha");
        container.create();
        StoredObject object = container.getObject("some-object");
        object.uploadObject(new UploadInstructions(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 }));
        byte[] bytes = object.downloadObject();
        System.out.println("Number of bytes: "+bytes.length);
    }
}

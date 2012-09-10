Java OpenStack Storage (JOSS)
=============================
JOSS is a Java client for the [OpenStack Storage component](http://docs.openstack.org/essex/openstack-object-storage/admin/content/ch_introduction-to-openstack-object-storage.html) REST interface. In order to use it you must have an account with an OpenStack Storage provider, such as the [CloudVPS Object Store](https://www.cloudvps.nl/blog/cloudvps-object-store-beta-test-join-and-get-free-storage/).

In order to use JOSS in your project, simply add the following dependency:

```xml
        <dependency>
            <groupId>nl.t42.openstack</groupId>
            <artifactId>joss</artifactId>
            <version>0.1.0</version>
        </dependency>
```

You can also download the artifact [Maven Search](http://search.maven.org)

Getting Started
---------------
Your Object Store provider will have provided you with the following information in order to authenticate yourself:
* username
* password
* authentication URL

We start by opening up a stateful client and **authenticating** ourselves:

```java
    OpenStackClient client = new OpenStackClientImpl();
    client.authenticate(username, password, url);
```

On failure, the client will throw an exception. On success, the client can now be used to execute actions on the Object Store. The client takes care of adding the token to the calls, so you don't have to worry about that. You should be aware, however, that tokens expire, probably after 24 hours or so. You can control this by reauthenticating. You will have to take care of this logic on your side of the application.

Next, we want to create a public container, where we can store our images:

```java
    Container container = new Container("images")
    client.createContainer(container);
    client.makeContainerPublic(container);
```

A container is private by default, so you will have to explicitly set it to public to be able to access it through its public URL.

To check whether the creation succeeded, list the containers.

```java
    Container[] containers = client.listContainers();
    for (Container foundContainer : containers) {
        System.out.println(foundContainer);
    }
```

Now it is time to upload a file to the Object Store. In this example, we take an image located in the root directory of the file system.

```java
    StoreObject object = new StoreObject("dog.png");
    client.uploadObject(container, object, new File("/dog.png"));
    System.out.println("Public URL: "+client.getPublicURL(container, object));
```

Note that *object* denotes the target of the file within the Object Store, whereas the file signifies the actual content that needs to be uploaded.

If you fire up your browser, you can navigate to the public URL to see your resource for real. You can also check whether the upload succeeded by listing the objects in a container or by fetching the object information.

```java
    StoreObject[] objects = client.listObjects(container);
    for (StoreObject foundObject : objects) {
        System.out.println(foundObject);
    }
    ObjectInformation info = client.getObjectInformation(container, object);
    System.out.println("Last modified:  "+info.getLastModified());
    System.out.println("ETag:           "+info.getEtag());
    System.out.println("Content type:   "+info.getContentType());
    System.out.println("Content length: "+info.getContentLength());
```

Alternatively, besides [File](http://docs.oracle.com/javase/6/docs/api/java/io/File.html), you could also use a *byte[]* or an [InputStream](http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html) to upload a file.

It is time to download what you just uploaded. Here we go.

```java
   client.downloadObject(container, object, new File("/dog2.png"));
```

Open the file "/dog2.png" on the file system to verify that the operation worked. Again, also File and InputStream are at your disposal. *On using InputStream be aware that you are responsible for closing the stream here, by calling close() on the wrapper*.

Now, if you want the object to be retrievable through another URL, you will have to move the object. This is accomplished by executing first a copy, then a delete action.

```java
    StoreObject targetObject = new StoreObject("new-dog.png");
    client.copyObject(container, object, container, targetObject);
    client.deleteObject(container, object);
    System.out.println("Public URL: "+client.getPublicURL(container, object)); // no longer retrievable
    System.out.println("Public URL: "+client.getPublicURL(container, targetObject)); // the new URL
```

You can once more use the browser to access both resources. You will see that the first URL gives you a 404, while the second one now gives you the original resource.

It is possible to add metadata to both containers and objects.

```java
    Map<String, Object> metadata = new TreeMap<String, Object>();
    metadata.put("title", "Some Title");
    metadata.put("department", "Some Department");
    client.setContainerInformation(container, metadata);
    client.setObjectInformation(container, object, metadata);
```

Likewise, this information can be retrieved, as seen above.

```java
    ObjectInformation info = client.getObjectInformation(container, object);
    for (String key : info.getMetadata().keySet()) {
        System.out.println("META / "+key+": "+info.getMetadata().get(key));
    }
```

There are many situations in which it is not necessary, not possible, or even plain clumsy, to be connected to external dependencies. For those situations, JOSS offers the InMemory implementation of the OpenStackClient.

```java
    OpenStackClient client = new OpenStackClientInMemory();
```

All the operations work basically in the same way. It is possible to run the in-memory client and have it hold the resources for a local run of your application. *Note that there is no such thing as a public URL for the in-memory run*.

Note that you will have to add some users to authenticate against.

```java
    MockUserStore users = new MockUserStore();
    users.addUser("testuser", "testpassword");
    client.setUsers(users);
```

Presumably, you are using Spring or something similar, in which case it will be easy to configure your profiles to either use the real client or the mock client.

This wraps up the tutorial. Good luck using JOSS.

Background
----------
JOSS provides access to the Container part of the OpenStack API. It is a specialized utility for this purpose. You will be able to:
* access your account
* handle containers
* handle objects, including upload and download

Generally, Java developers want to be able to isolate their applications from the target servers during development, which is why JOSS support an in-memory mode. Simply have your application instantiate OpenStackClientInMemory (instead of OpenStackClientImpl) and you have a fully fledged in-memory Storage container, useful for testing your application on your own development system and isolating your application during various test stages.

Done and To-do
--------------
Things JOSS can do for you:
* Java-based wrapper library for OpenStack, so REST calls do not have to be made directly
* option to switch between real (ie, HTTPS target) and mock mode
* in-memory storage of objects for unit testing and local application run purposes

Things to do for JOSS:
* reading of a single directory structure and based on that, building up an in-memory Object Store
* on-file storage of objects for visual screening purposes
* fine-grained control of container authentication settings
* introduction of Tenants

License
-------
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

More information
----------------
* http://api.openstack.org/
* http://docs.openstack.org/api/openstack-object-storage/1.0/content/
* http://www.vankouteren.eu/blog/2012/08/cloudvps-object-store-beta-introduction/
* https://www.cloudvps.nl/blog/cloudvps-object-store-beta-test-join-and-get-free-storage/

Keep up to date on the library development by joining the Java OpenStack Storage discussion group [Google Group](https://groups.google.com/forum/?fromgroups#!forum/java-openstack-storage---joss)

or follow us on [Twitter](http://twitter.com/robert_bor)

Sponsor
-------
This component was graciously donated by [42 BV](http://www.42.nl) [42 logo](http://www.42.nl/images/42-54x59.png "42")
Java OpenStack Storage (JOSS) [![Build Status](https://buildhive.cloudbees.com/job/javaswift/job/joss/badge/icon)](https://buildhive.cloudbees.com/job/javaswift/job/joss/)
=============================
JOSS is a Java client for the [OpenStack Storage component](https://docs.openstack.org/swift/latest/admin/objectstorage-intro.html) REST interface. In order to use it you must have an account with an OpenStack Storage provider, such as the [CloudVPS Object Store](https://www.cloudvps.nl/blog/cloudvps-object-store-beta-test-join-and-get-free-storage/).

In order to use JOSS in your project, simply add the following dependency:

```xml
        <dependency>
            <groupId>org.javaswift</groupId>
            <artifactId>joss</artifactId>
            <version>0.10.2</version>
        </dependency>
```

You can also download the artifact on [Maven Search](http://search.maven.org)

Getting Started
---------------
Your Object Store provider will have provided you with the following information in order to authenticate yourself:
* tenant ID
* tenant name
* username
* password
* authentication URL

We start by opening up a stateful client and [**authenticating**](http://joss.javaswift.org/authentication.html) ourselves:

```java
    Account account = new AccountFactory()
            .setUsername(username)
            .setPassword(password)
            .setAuthUrl(url)
            .setTenantId(tenantId)
            .setTenantName(tenantName)
            .createAccount();
```

Note that you do not have to pass both tenant ID and tenant name; passing either tenant ID or tenant name is sufficient.

On failure, the client will throw an exception. On success, the account can now be used to execute actions on the Object Store. The account takes care of adding the token to the calls, so you don't have to worry about that. You should be aware, however, that tokens expire after 24 hours. The client will get a new token when it encounters a 401 and retry the original command just once.

Next, we want to create a public container, where we can store our images:

```java
    Container container = account.getContainer("images");
    container.create();
    container.makePublic();
```

A container is private by default, so you will have to explicitly set it to public to be able to access it through its public URL.

To check whether the creation succeeded, list the containers.

```java
    Collection<Container> containers = account.list();
    for (Container currentContainer : containers) {
        System.out.println(currentContainer.getName());
    }
```

Now it is time to upload a file to the Object Store. In this example, we take an image located in the root directory of the file system.

```java
    StoredObject object = container.getObject("dog.png");
    object.uploadObject(new File("/dog.png"));
    System.out.println("Public URL: "+object.getPublicURL());
```

If you fire up your browser, you can navigate to the public URL to see your resource for real. This is only possible because the container has been set to public. If it was private, you would not be able to do this.

You can also check whether the upload succeeded by listing the objects in a container or by fetching the object information.

```java
    System.out.println("Last modified:  "+object.getLastModified());
    System.out.println("ETag:           "+object.getEtag());
    System.out.println("Content type:   "+object.getContentType());
    System.out.println("Content length: "+object.getContentLength());

    Collection<StoredObject> objects = container.list();
    for (StoredObject currentObject : objects) {
        System.out.println(currentObject.getName());
    }
```

Alternatively, besides [File](http://docs.oracle.com/javase/6/docs/api/java/io/File.html), you could also use a *byte[]* or an [InputStream](http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html) to upload a file.

It is time to download what you just uploaded. Here we go.

```java
    object.downloadObject(new File("/dog2.png"));
```

Open the file "/dog2.png" on the file system to verify that the operation worked. Again, also byte[] and InputStream are at your disposal. *On using InputStream be aware that you are responsible for closing the stream by calling close() on the wrapper*.

Now, if you want the object to be retrievable through another URL, you will have to move the object. This is accomplished by executing first a copy, then a delete action.

```java
    StoredObject newObject = container.getObject("new-dog.png");
    object.copyObject(container, newObject);
    object.delete();
    System.out.println("Public URL: "+object.getPublicURL()); // no longer retrievable
    System.out.println("Public URL: "+newObject.getPublicURL()); // the new URL
```

You can once more use the browser to access both resources. You will see that the first URL gives you a 404, while the second one now gives you the original resource.

It is possible to add metadata to both containers and objects.

```java
    Map<String, Object> metadata = new TreeMap<String, Object>();
    metadata.put("title", "Some Title");
    metadata.put("department", "Some Department");
    container.setMetadata(metadata);
```

Likewise, this information can be retrieved, as seen above.

```java
    Map<String, Object> returnedMetadata = container.getMetadata();
    for (String name : returnedMetadata.keySet()) {
        System.out.println("META / "+name+": "+returnedMetadata.get(name));
    }
```

It is possible to connect with proxy, there are two options how to setup it:

Option A: Programmatically

```java
    AccountConfig config = new AccountConfig()                                
                            config.setUseProxy(true)
                            config.setProxyHost("hostname")
                            config.setProxyPort(3128);
```

Also you can specify username and password if authentication required:

```java
    AccountConfig config = new AccountConfig()
    config.setProxyUsername("some-user")
    config.setProxyPort("some-password");
```

Option B: JVM Proxy Settings

- Edit `${JAVA_HOME}/jre/lib/net.properties`, set properties:

```
    java.net.useSystemProxies=true
     
    ...
    http.proxyHost=some-host
    http.proxyPort=some-port
```

or

- Specify as JVM options: 

```
    -Djava.net.useSystemProxies=true 
    -Dhttp.proxyHost=http://PROXY_HOST \
    -Dhttp.proxyPort=PROXY_PORT \ 
    -Dhttp.proxyUser=USERNAME \
    -Dhttp.proxyPassword=PASSWORD
```

There are many situations in which it is not necessary, not possible, or even plain clumsy, to be connected to external dependencies. For those situations, JOSS offers the InMemory implementation of the OpenStackClient.

```java
    AccountConfig config = new AccountConfig()
            .setUsername( username)
            .setPassword(password)
            .setAuthUrl(url)
            .setTenant(tenant)
            .setMock(true);
    Account account = new AccountFactory(config).createAccount();
```

All the operations work basically in the same way. It is possible to run the in-memory client and have it hold the resources for a local run of your application.

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
* auto-reconnecting your Swift session
* documentation and tutorials to help you get started

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

_On JOSS_

* High level explanation of JOSS; http://blog.42.nl/articles/accessing-openstack-object-storage-with-java-through-joss

_On Cloud VPS Object Store_

* How to get a beta-tester account on the CloudVPS ObjectStore; https://www.cloudvps.com/blog/cloudvps-object-store-beta-test-more-testers-welcome
* CloudVPS writes on collaboration with 42 to create JOSS; https://www.cloudvps.com/blog/cloudvps-object-store-beta-tester-contributes-java-library/

_On the OpenStack API_

* OpenStack API; https://developer.openstack.org/
* OpenStack API; https://developer.openstack.org/api-ref/object-store/

_Openstack Swift on Github_
* Github project https://github.com/openstack/swift

or follow us on [Twitter](http://twitter.com/javaswiftorg)

Special thanks to Oscar Westra van Holthe - Kind and Erik Hooijmeijer for premium advice and support.

Sponsor
-------
This component was graciously donated by [42 BV](http://www.42.nl) ![42 logo](http://www.42.nl/images/42-54x59.png "42")

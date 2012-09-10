Java OpenStack Storage (JOSS)
=============================
JOSS is a Java client for the [OpenStack Storage component](http://docs.openstack.org/essex/openstack-object-storage/admin/content/ch_introduction-to-openstack-object-storage.html). In order to use it you, must have an OpenStack Storage provider, such as the [CloudVPS Object Store](https://www.cloudvps.nl/blog/cloudvps-object-store-beta-test-join-and-get-free-storage/). In order to use JOSS in your project, simply add the following dependency:

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

We start by opening up a stateful client and authenticating ourselves:

```java
        OpenStackClient client = new OpenStackClientImpl();
        client.authenticate(username, password, url);
```

On failure, the client will throw an exception. On success, the client can now be used to execute actions on the Object Store.

Introduction
------------
JOSS provides access to the Container part of the OpenStack API. It is a specialized utility for this purpose. You will be able to:
* access your account
* handle containers
* handle objects, including upload and download

Generally, Java developers want to be able to isolate their applications from the target servers during development, which is why JOSS support an in-memory mode. Simply have your application instantiate OpenStackClientInMemory (instead of OpenStackClientImpl) and you have a fully fledged in-memory Storage container, useful for testing your application on your own development system and isolating your application during various test stages.

Design goals for JOSS:
+ Java-based wrapper library for OpenStack, so REST calls do not have to be made directly
+ option to switch between real (ie, HTTPS target) and mock mode
+ in-memory storage of objects for unit testing purposes
/ on-file storage of objects for visual screening purposes
/ conversion of on-file objects to OpenStack Storage structure for isolation purposes

Components of OpenStack and their level of support in this component:
* Keystone (Authentication and authorization) - partially supported
* Nova (Computing) - not supported
* Swift (ObjectStore) - SUPPORTED
* Glance (ImageStore) - not supported
* Horizon (Dashboard) - not supported

Based on the OpenStack documentation:
* http://api.openstack.org/
* http://docs.openstack.org/api/openstack-object-storage/1.0/content/
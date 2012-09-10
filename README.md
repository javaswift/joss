Java OpenStack Storage (JOSS)
-----------------------------


Getting Started
===============
In order to use JOSS in your project, simply add the following dependency:

```xml
        <dependency>
            <groupId>nl.t42.openstack</groupId>
            <artifactId>joss</artifactId>
            <version>0.1.0</version>
        </dependency>
```

You can also download the artifact

[Maven Search](http://search.maven.org)

Introduction
============
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
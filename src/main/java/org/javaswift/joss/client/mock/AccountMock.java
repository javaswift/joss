package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.command.mock.factory.AccountCommandFactoryMock;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;

public class AccountMock extends AbstractAccount {

//    protected Swift swift = new Swift();

    public AccountMock(Swift swift) {
        super(new AccountCommandFactoryMock(swift), ALLOW_CACHING);
    }

    public AccountMock() {
        this(new Swift());
    }

    @Override
    public Container getContainer(String name) {
        return new ContainerMock(this, name);
    }

//    public AccessImpl authenticate() { return null; /* ignore */ }

//    public AccountMock setSwift(Swift swift) {
//        this.swift = swift;
//        return this;
//    }

//    public ObjectDeleter getObjectDeleter() {
//        return this.swift.getObjectDeleter();
//    }
//
//    @Override
//    protected void saveMetadata() {} // no action necessary
//
//    @Override
//    protected void getInfo() {
//
//        int containerCount= 0;
//        int objectCount = 0;
//        long bytesUsed = 0;
//        for (Container container : this.swift.getContainers()) {
//            containerCount++;
//            objectCount += container.getCount();
//            bytesUsed += container.getBytesUsed();
//        }
//        this.info.setContainerCount(new AccountContainerCount(Integer.toString(containerCount)));
//        this.info.setObjectCount(new AccountObjectCount(Integer.toString(objectCount)));
//        this.info.setBytesUsed(new AccountBytesUsed(Long.toString(bytesUsed)));
//    }
//
//    @Override
//    public Collection<Container> list(String prefix, String marker, int pageSize) {
//        return new PageServer<Container>().createPage(this.swift.getContainers(), prefix, marker, pageSize);
//    }
//
//    public Container getContainer(String name) {
//        Container foundContainer = this.swift.getContainer(name);
//        return foundContainer == null ? new ContainerMock(this, name) : foundContainer;
//    }
//
//    public void createContainer(Container container) {
//        this.swift.createContainer(container.getName());
//    }
//
//    public void deleteContainer(Container container) {
//        this.swift.deleteContainer(container.getName());
//    }
//
//    public String getPublicURL() {
//        return this.swift.getPublicURL();
//    }

}

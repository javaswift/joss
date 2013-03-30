package org.javaswift.joss.client.impl;

import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.command.shared.factory.ContainerCommandFactory;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

import java.util.Collection;

public class ContainerImpl extends AbstractContainer {

    public ContainerImpl(AccountImpl account, String name, boolean allowCaching) {
        super(account.getFactory().getContainerCommandFactory(), account, name, allowCaching);
    }

    public StoredObject getObject(String objectName) {
        return new StoredObjectImpl(this, objectName, isAllowCaching());
    }

}

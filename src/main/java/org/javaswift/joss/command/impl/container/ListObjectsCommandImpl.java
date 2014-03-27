package org.javaswift.joss.command.impl.container;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.shared.container.ListObjectsCommand;
import org.javaswift.joss.command.shared.container.StoredObjectListElement;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListObjectsCommandImpl extends AbstractListCommandImpl<Collection<StoredObject>> implements ListObjectsCommand {

    public ListObjectsCommandImpl(Account account, HttpClient httpClient, Access access, Container container, ListInstructions listInstructions) {
        super(account, httpClient, access, container, listInstructions);
    }

    protected Collection<StoredObject> getReturnObject(HttpResponse response) throws IOException {
        StoredObjectListElement[] list = createObjectMapper(false)
                .readValue(response.getEntity().getContent(), StoredObjectListElement[].class);
        List<StoredObject> objects = new ArrayList<StoredObject>();
        for (StoredObjectListElement header : list) {

            // TEMP
            if (header.subdir != null) {
                System.out.println("Subdirectory: "+header.subdir);
                continue;
            }

            StoredObject object = getStoredObject(header);
            objects.add(object);
        }
        return objects;
    }

}

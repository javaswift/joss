package org.javaswift.joss.command.impl.container;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.shared.container.ListDirectoryCommand;
import org.javaswift.joss.command.shared.container.StoredObjectListElement;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListDirectoryCommandImpl extends AbstractListCommandImpl<Collection<DirectoryOrObject>> implements ListDirectoryCommand {

    public ListDirectoryCommandImpl(Account account, HttpClient httpClient, Access access, Container container, ListInstructions listInstructions) {
        super(account, httpClient, access, container, listInstructions);
    }

    protected Collection<DirectoryOrObject> getReturnObject(HttpResponse response) throws IOException {

        StoredObjectListElement[] list = createObjectMapper(false)
                .readValue(response.getEntity().getContent(), StoredObjectListElement[].class);
        List<DirectoryOrObject> files = new ArrayList<DirectoryOrObject>();
        for (StoredObjectListElement header : list) {
            if (header.subdir != null) {
                files.add(new Directory(header.subdir));
            } else {
                files.add(getStoredObject(header));
            }
        }
        return files;
    }

}

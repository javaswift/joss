package org.javaswift.joss.client.core;

import org.javaswift.joss.client.core.AbstractPaginationMap;
import org.javaswift.joss.client.core.AccountPaginationMap;
import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.client.impl.ContainerImpl;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.impl.factory.AccountCommandFactoryImpl;
import org.javaswift.joss.model.Container;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaginationMapImplTest extends BaseCommandTest {

    @Test
    public void buildMapButContainersAreGone() {
        account = mock(AccountImpl.class);
        when(account.getCount()).thenReturn(9);
        // Note that no containers are returned here, let's say they're quickly deleted
        AbstractPaginationMap paginationMap = new AccountPaginationMap(account, null, 4)
                .setBlockSize(2)
                .buildMap();
        assertEquals(4, paginationMap.getPageSize());
    }

    @Test
    public void buildMap() {
        account = mock(AccountImpl.class);
        when(account.getFactory()).thenReturn(new AccountCommandFactoryImpl(null, null, null, null));
        when(account.getCount()).thenReturn(9);
        whenList(account, null, new String[]{ "A", "B" });
        whenList(account, "B", new String[]{ "C", "D" });
        whenList(account, "D", new String[]{ "E", "F" });
        whenList(account, "F", new String[]{ "G", "H" });
        whenList(account, "H", new String[]{ "I" });
        AbstractPaginationMap paginationMap = new AccountPaginationMap(account, null, 4)
                .setBlockSize(2)
                .buildMap();
        assertEquals(4, paginationMap.getPageSize());
        assertEquals(3, paginationMap.getNumberOfPages());
        assertEquals(null, paginationMap.getMarker(0));
        assertEquals("D", paginationMap.getMarker(1));
        assertEquals("H", paginationMap.getMarker(2));
    }

    protected void whenList(AccountImpl account, String markerContainer, String[] returnedContainers) {
        Collection<Container> collection = createContainerCollection(account, returnedContainers);
        when(account.list(null, markerContainer, 2)).thenReturn(collection);
    }

    protected Collection<Container> createContainerCollection(AccountImpl account, String[] containerNames) {
        List<Container> containers = new ArrayList<Container>();
        for (String containerName : containerNames) {
            containers.add(new ContainerImpl(account, containerName, true));
        }
        return containers;
    }
}

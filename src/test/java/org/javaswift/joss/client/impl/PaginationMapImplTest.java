package org.javaswift.joss.client.impl;

import org.javaswift.joss.command.core.BaseCommandTest;
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
        when(account.getCount()).thenReturn(9);
        when(account.list(null, null, 2)).thenReturn(createContainerCollection(new String[]{ "A", "B" }));
        when(account.list(null, "B", 2)).thenReturn(createContainerCollection(new String[] { "C", "D"}));
        when(account.list(null, "D", 2)).thenReturn(createContainerCollection(new String[] { "E", "F" }));
        when(account.list(null, "F", 2)).thenReturn(createContainerCollection(new String[] { "G", "H" }));
        when(account.list(null, "H", 2)).thenReturn(createContainerCollection(new String[] { "I" }));
        AbstractPaginationMap paginationMap = new AccountPaginationMap(account, null, 4)
                .setBlockSize(2)
                .buildMap();
        assertEquals(4, paginationMap.getPageSize());
        assertEquals(3, paginationMap.getNumberOfPages());
        assertEquals(null, paginationMap.getMarker(0));
        assertEquals("D", paginationMap.getMarker(1));
        assertEquals("H", paginationMap.getMarker(2));
    }

    protected Collection<Container> createContainerCollection(String[] containerNames) {
        List<Container> containers = new ArrayList<Container>();
        for (String containerName : containerNames) {
            containers.add(new ContainerImpl(null, containerName, true));
        }
        return containers;
    }
}

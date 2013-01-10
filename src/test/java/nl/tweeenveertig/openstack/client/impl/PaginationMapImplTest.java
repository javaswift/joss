package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.model.Container;
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
        when(account.getContainerCount()).thenReturn(9);
        // Note that no containers are returned here, let's say they're quickly deleted
        PaginationMapImpl paginationMap = new PaginationMapImpl(account, 4)
                .setBlockSize(2)
                .buildMap();
        assertEquals((Integer)4, paginationMap.getPageSize());
    }

    @Test
    public void buildMap() {
        account = mock(AccountImpl.class);
        when(account.getContainerCount()).thenReturn(9);
        when(account.listContainers((String) null, 2)).thenReturn(createContainerCollection(new String[]{ "A", "B" }));
        when(account.listContainers("B", 2)).thenReturn(createContainerCollection(new String[]          { "C", "D"}));
        when(account.listContainers("D", 2)).thenReturn(createContainerCollection(new String[]          { "E", "F" }));
        when(account.listContainers("F", 2)).thenReturn(createContainerCollection(new String[]          { "G", "H" }));
        when(account.listContainers("H", 2)).thenReturn(createContainerCollection(new String[]          { "I" }));
        PaginationMapImpl paginationMap = new PaginationMapImpl(account, 4)
                .setBlockSize(2)
                .buildMap();
        assertEquals((Integer)4, paginationMap.getPageSize());
        assertEquals((Integer)3, paginationMap.getNumberOfPages());
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

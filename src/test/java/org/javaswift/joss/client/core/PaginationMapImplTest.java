package org.javaswift.joss.client.core;

import mockit.Mocked;
import mockit.NonStrictExpectations;
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

public class PaginationMapImplTest extends BaseCommandTest {

    @Test
    public void buildMapButContainersAreGone(@Mocked final AccountImpl account) {
        new NonStrictExpectations() {{
            account.getCount(); result = 9;
        }};
        // Note that no containers are returned here, let's say they're quickly deleted
        AbstractPaginationMap paginationMap = new AccountPaginationMap(account, null, 4)
                .setBlockSize(2)
                .buildMap();
        assertEquals(4, paginationMap.getPageSize());
    }

    @Test
    public void buildMap(@Mocked final AccountImpl account) {
        new NonStrictExpectations() {{
            account.getCount(); result = 9;
            account.getFactory(); result = new AccountCommandFactoryImpl(null, null, null, null, '/');
        }};
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

    protected void whenList(final AccountImpl account, final String markerContainer, final String[] returnedContainers) {
        final Collection<Container> collection = createContainerCollection(account, returnedContainers);
        new NonStrictExpectations() {{
            account.list(null, markerContainer, 2);
            result = collection;
        }};
    }

    protected Collection<Container> createContainerCollection(AccountImpl account, String[] containerNames) {
        List<Container> containers = new ArrayList<Container>();
        for (String containerName : containerNames) {
            containers.add(new ContainerImpl(account, containerName, true));
        }
        return containers;
    }
}

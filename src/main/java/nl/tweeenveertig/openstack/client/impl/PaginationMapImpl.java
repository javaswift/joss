package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.PaginationMap;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class PaginationMapImpl implements PaginationMap {

    private Account account;

    private Integer pageSize;

    private Map<Integer, String> pageToMarker = new TreeMap<Integer, String>();

    private Integer blockSize;

    private Integer numberOfRecords = 0;

    public PaginationMapImpl(Account account, Integer pageSize) {
        this.account = account;
        this.blockSize = account.getMaxPageSize();
        this.pageSize = pageSize;
    }

    public PaginationMapImpl buildMap() {
        Integer containersToGo = account.getContainerCount();
        String marker = null;
        Integer page = 0;
        Integer locationInPage = 0;
        pageToMarker.put(page++, null); // First marker is always null
        while (containersToGo > 0) {
            Collection<Container> containers = account.listContainers(marker, blockSize);
            for (Container container : containers) {
                marker = container.getName();
                numberOfRecords++;
                if (++locationInPage == getPageSize()) {
                    pageToMarker.put(page++, marker);
                    locationInPage = 0;
                }
            }
            containersToGo -= containers.size() == 0 ? containersToGo : (containers.size() < blockSize ? containers.size() : blockSize);
        }
        return this;
    }

    @Override
    public String getMarker(Integer page) {
        return pageToMarker.get(page);
    }

    public Integer getNumberOfPages() {
        return pageToMarker.size();
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Integer getNumberOfRecords() {
        return this.numberOfRecords;
    }

    public PaginationMapImpl setBlockSize(Integer blockSize) {
        this.blockSize = blockSize;
        return this;
    }

}

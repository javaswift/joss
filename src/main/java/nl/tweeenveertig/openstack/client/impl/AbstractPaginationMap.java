package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.model.*;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractPaginationMap<Child extends ListSubject> implements PaginationMap {

    private ListHolder<Child> listHolder;

    private int pageSize;

    private Map<Integer, String> pageToMarker = new TreeMap<Integer, String>();

    private int blockSize;

    private int numberOfRecords = 0;

    private String prefix;

    public AbstractPaginationMap(ListHolder<Child> listHolder, String prefix, int pageSize) {
        this.listHolder = listHolder;
        this.blockSize = listHolder.getMaxPageSize();
        this.prefix = prefix;
        this.pageSize = pageSize;
    }

    public AbstractPaginationMap buildMap() {
        int recordsToGo = listHolder.getCount();
        String marker = null;
        int page = 0;
        int locationInPage = 0;
        pageToMarker.put(page++, null); // First marker is always null
        while (recordsToGo > 0) {
            Collection<Child> children = listHolder.list(prefix, marker, blockSize);
            for (Child child : children) {
                marker = child.getName();
                numberOfRecords++;
                if (++locationInPage == getPageSize()) {
                    pageToMarker.put(page++, marker);
                    locationInPage = 0;
                }
            }
            recordsToGo -= children.size() == 0 ? recordsToGo : (children.size() < blockSize ? children.size() : blockSize);
        }
        return this;
    }

    @Override
    public String getMarker(Integer page) {
        return pageToMarker.get(page);
    }

    public int getNumberOfPages() {
        return pageToMarker.size();
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getNumberOfRecords() {
        return this.numberOfRecords;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public AbstractPaginationMap setBlockSize(int blockSize) {
        this.blockSize = blockSize;
        return this;
    }

}

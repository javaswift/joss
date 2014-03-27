package org.javaswift.joss.client.core;

import org.javaswift.joss.model.ListSubject;
import org.javaswift.joss.model.PaginationMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractPaginationMap<Child extends ListSubject> implements PaginationMap {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractPaginationMap.class);

    private int blockSize;

    private int pageSize;

    private Map<Integer, String> pageToMarker = new TreeMap<Integer, String>();

    private int numberOfRecords = 0;

    private String prefix;

    public AbstractPaginationMap(String prefix, int pageSize, int maxPageSize) {
        this.blockSize = maxPageSize;
        this.prefix = prefix;
        this.pageSize = pageSize;
    }

    public AbstractPaginationMap buildMap() {
        listAllItems();
        return this;
    }

    public abstract Collection<Child> list(String prefix, String marker, int blockSize);

    public abstract int getCount();

    public Collection<Child> listAllItems() {
        Collection<Child> allChildren = new ArrayList<Child>();
        int recordsToGo = getCount();
        String marker = null;
        int page = 0;
        int locationInPage = 0;
        pageToMarker.put(page++, null); // First marker is always null
        while (recordsToGo > 0) {
            Collection<Child> children = list(prefix, marker, blockSize);
            for (Child child : children) {
                marker = child.getName();
                numberOfRecords++;
                if (++locationInPage == getPageSize()) {
                    pageToMarker.put(page++, marker);
                    locationInPage = 0;
                }
            }
            recordsToGo -= children.size() == 0 ? recordsToGo : (children.size() < blockSize ? children.size() : blockSize);
            allChildren.addAll(children);
        }
        if (locationInPage == 0) { // Remove last page if no elements follow it
            pageToMarker.remove(pageToMarker.size() - 1);
        }
        LOG.info("JOSS / Created PaginationMap with "+pageToMarker.size()+" pages for a total of "+numberOfRecords+" records");
        return allChildren;
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

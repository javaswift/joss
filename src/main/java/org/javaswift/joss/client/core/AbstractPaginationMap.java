package org.javaswift.joss.client.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.javaswift.joss.model.ListSubject;
import org.javaswift.joss.model.PaginationMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        boolean loopUntilNoMoreData = false;
        int recordsToGo = getCount();
        String marker = null;
        int page = 0;
        int locationInPage = 0;
        pageToMarker.put(page++, null); // First marker is always null

        if (recordsToGo == 0) {
           // recordsToGo hints that there are no items to list, but the
           // container might have changed since we last check it.  Rather
           // than loop over recordsToGo, we will loop over the list
           // interface to the object store until no items are returned.
           loopUntilNoMoreData = true;
           recordsToGo = 1;
        }
        
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
            if (children.isEmpty()) {
                // We have processed all the records.
                recordsToGo = 0;
            }
            else {
                allChildren.addAll(children);
                if (!loopUntilNoMoreData) {
                    recordsToGo -= (children.size() < blockSize) ?
                                   children.size() : blockSize;
                }
            }
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

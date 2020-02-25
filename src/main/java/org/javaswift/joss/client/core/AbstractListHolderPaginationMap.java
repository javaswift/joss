package org.javaswift.joss.client.core;

import java.util.Collection;

import org.javaswift.joss.model.ListHolder;
import org.javaswift.joss.model.ListSubject;

public class AbstractListHolderPaginationMap<Child extends ListSubject> extends AbstractPaginationMap<Child> {

    private ListHolder<Child> listHolder;

    public AbstractListHolderPaginationMap(ListHolder<Child> listHolder, String prefix, Integer pageSize) {
        super(prefix, pageSize, listHolder.getMaxPageSize());
        this.listHolder = listHolder;
    }

    @Override
    public Collection list(String prefix, String marker, int blockSize) {
        return listHolder.list(prefix, marker, blockSize);
    }

    @Override
    public int getCount() {
        return listHolder.getCount();
    }

}

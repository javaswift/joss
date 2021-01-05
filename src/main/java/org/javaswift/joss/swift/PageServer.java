package org.javaswift.joss.swift;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.javaswift.joss.model.ListSubject;

public class PageServer<Child extends ListSubject> {

    public Collection<Child> createPage(Collection<Child> children, String prefix, String marker, Integer pageSize) {
        List<Child> page = new ArrayList<Child>();
        boolean foundMarker = marker == null;
        Integer containersOnPage = 0;
        for (Child child : children) {
            if (foundMarker && (prefix == null || child.getName().startsWith(prefix))) {
                page.add(child);
                containersOnPage++;
            }
            if (containersOnPage == pageSize) {
                break;
            }
            // Do this as the last action, because it only starts working on the next element
            if (!foundMarker && child.getName().equals(marker)) {
                foundMarker = true;
            }
        }
        return page;
    }

}

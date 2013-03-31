package org.javaswift.joss.swift.statusgenerator;

import java.util.ArrayList;
import java.util.List;

public class StatusCursor {

    public final static int IGNORE = -1;

    List<Integer> statusList = new ArrayList<Integer>();

    int position = 0;

    public void addStatus(int status) {
        this.statusList.add(status);
    }

    public int getStatus() {
        if (position == statusList.size()) {
            position = 0;
        }
        if (statusList.size() == 0) {
            return -1;
        }
        return statusList.get(position++);
    }

}

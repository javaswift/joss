package org.javaswift.joss.swift.statusgenerator;

import org.javaswift.joss.command.mock.core.CommandMock;

import java.util.HashMap;
import java.util.Map;

public class StatusGenerator {

    StatusCursor genericCursor = new StatusCursor();

    Map<Class<? extends CommandMock>, StatusCursor> classCursors = new HashMap<Class<? extends CommandMock>, StatusCursor>();

    public int getStatus(Class<? extends CommandMock> clazz) {
        StatusCursor cursor = getClassCursor(clazz);
        if (cursor == null) {
            cursor = genericCursor;
        }
        return cursor.getStatus();
    }

    public void addIgnore() {
        addStatus(StatusCursor.IGNORE);
    }

    public void addIgnore(Class<? extends CommandMock> ignoreClass) {
        addStatus(ignoreClass, StatusCursor.IGNORE);
    }

    public void addStatus(int status) {
        genericCursor.addStatus(status);
    }

    public void addStatus(Class<? extends CommandMock> clazz, int status) {
        StatusCursor cursor = getClassCursor(clazz);
        if (cursor == null) {
            cursor = new StatusCursor();
            classCursors.put(clazz, cursor);
        }
        cursor.addStatus(status);
    }

    protected StatusCursor getClassCursor(Class<? extends CommandMock> clazz) {
        return classCursors.get(clazz);
    }

}

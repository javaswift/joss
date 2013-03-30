package org.javaswift.joss.swift;

public class SwiftResult<T extends Object> {

    private final int status;

    private final T payload;

    public SwiftResult(int status) {
        this(null, status);
    }

    public SwiftResult(T payload, int status) {
        this.payload = payload;
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public T getPayload() {
        return this.payload;
    }
}

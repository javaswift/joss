package org.javaswift.joss.exception;

public class CommandException extends RuntimeException {

    private int httpStatusCode;
    private CommandExceptionError error;

    public CommandException(Integer httpStatusCode, CommandExceptionError error) {
        this.httpStatusCode = httpStatusCode;
        this.error = error;
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public CommandExceptionError getError() {
        return this.error;
    }

    public String toString() {
        return httpStatusCode != 0 && error != null ?
            "Command exception, HTTP Status code: "+httpStatusCode+" => " + error :
            super.toString();
    }
}

package org.javaswift.joss.exception;

public class NotModifiedException extends CommandException {

    public NotModifiedException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

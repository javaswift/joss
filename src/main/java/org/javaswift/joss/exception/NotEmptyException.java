package org.javaswift.joss.exception;

public class NotEmptyException extends CommandException {

    public NotEmptyException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

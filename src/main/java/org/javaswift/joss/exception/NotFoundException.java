package org.javaswift.joss.exception;

public class NotFoundException extends CommandException {

    public NotFoundException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

package org.javaswift.joss.exception;

public class AlreadyExistsException extends CommandException {

    public AlreadyExistsException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

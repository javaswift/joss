package org.javaswift.joss.exception;

public class ModifiedException extends CommandException {

    public ModifiedException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

public class MissingContentLengthOrTypeException extends CommandException {

    public MissingContentLengthOrTypeException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

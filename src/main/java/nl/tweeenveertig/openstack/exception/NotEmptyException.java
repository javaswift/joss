package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

public class NotEmptyException extends CommandException {

    public NotEmptyException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

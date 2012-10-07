package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

public class AlreadyExistsException extends CommandException {

    public AlreadyExistsException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

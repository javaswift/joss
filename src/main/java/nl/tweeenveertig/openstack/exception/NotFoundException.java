package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

public class NotFoundException extends CommandException {

    public NotFoundException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

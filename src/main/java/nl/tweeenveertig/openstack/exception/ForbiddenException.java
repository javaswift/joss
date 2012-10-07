package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

public class ForbiddenException extends CommandException {

    public ForbiddenException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

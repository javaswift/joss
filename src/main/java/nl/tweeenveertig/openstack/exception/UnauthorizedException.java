package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

public class UnauthorizedException extends CommandException {

    public UnauthorizedException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

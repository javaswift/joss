package nl.tweeenveertig.openstack.exception;

public class ForbiddenException extends CommandException {

    public ForbiddenException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

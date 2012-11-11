package nl.tweeenveertig.openstack.exception;

public class UnauthorizedException extends CommandException {

    public UnauthorizedException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

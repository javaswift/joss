package nl.tweeenveertig.openstack.exception;

public class NotModifiedException extends CommandException {

    public NotModifiedException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

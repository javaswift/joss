package nl.tweeenveertig.openstack.command.core;

public class NotModifiedException extends CommandException {

    public NotModifiedException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

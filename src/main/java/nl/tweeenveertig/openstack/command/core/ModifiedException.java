package nl.tweeenveertig.openstack.command.core;

public class ModifiedException extends CommandException {

    public ModifiedException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

package nl.tweeenveertig.openstack.exception;

public class MissingContentLengthOrTypeException extends CommandException {

    public MissingContentLengthOrTypeException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

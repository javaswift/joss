package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

public class Md5ChecksumException extends CommandException {

    public Md5ChecksumException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

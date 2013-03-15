package org.javaswift.joss.exception;

public class Md5ChecksumException extends CommandException {

    public Md5ChecksumException(Integer httpStatusCode, CommandExceptionError error) {
        super(httpStatusCode, error);
    }
}

package nl.t42.openstack.command.core;

public class CommandException extends RuntimeException {

    private int httpStatusCode;
    private CommandExceptionError error;

    public CommandException(int httpStatusCode, CommandExceptionError error) {
        this.httpStatusCode = httpStatusCode;
        this.error = error;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public CommandExceptionError getError() {
        return this.error;
    }

    public String toString() {
        return "Command exception, HTTP Status code: "+httpStatusCode+" => " + error;
    }
}

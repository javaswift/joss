package org.javaswift.joss.exception;

import org.apache.http.HttpStatus;

import java.lang.reflect.Constructor;

public enum HttpStatusToExceptionMapper {

    _202 (HttpStatus.SC_ACCEPTED, CommandExceptionError.ENTITY_ALREADY_EXISTS, AlreadyExistsException.class),
    _401 (HttpStatus.SC_UNAUTHORIZED, CommandExceptionError.UNAUTHORIZED, UnauthorizedException.class),
    _403 (HttpStatus.SC_FORBIDDEN, CommandExceptionError.ACCESS_FORBIDDEN, ForbiddenException.class),
    _404 (HttpStatus.SC_NOT_FOUND, CommandExceptionError.ENTITY_DOES_NOT_EXIST, NotFoundException.class),
    _304 (HttpStatus.SC_NOT_MODIFIED, CommandExceptionError.CONTENT_NOT_MODIFIED, NotModifiedException.class),
    _409 (HttpStatus.SC_CONFLICT, CommandExceptionError.CONTAINER_NOT_EMPTY, NotEmptyException.class),
    _411 (HttpStatus.SC_LENGTH_REQUIRED, CommandExceptionError.MISSING_CONTENT_LENGTH_OR_TYPE, MissingContentLengthOrTypeException.class),
    _412 (HttpStatus.SC_PRECONDITION_FAILED, CommandExceptionError.CONTENT_DIFFERENT, ModifiedException.class),
    _422 (HttpStatus.SC_UNPROCESSABLE_ENTITY, CommandExceptionError.MD5_CHECKSUM, Md5ChecksumException.class);

    private final int httpStatus;

    private final CommandExceptionError error;

    protected Class<? extends CommandException> exceptionToThrow;

    private HttpStatusToExceptionMapper(int httpStatus, CommandExceptionError error, Class<? extends CommandException> exceptionToThrow) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.exceptionToThrow = exceptionToThrow;
    }

    public int getHttpStatus() {
        return this.httpStatus;
    }

    public Class<? extends CommandException> getExceptionToThrow() {
        return this.exceptionToThrow;
    }

    public CommandExceptionError getError() {
        return this.error;
    }

    public CommandException getException(CommandExceptionError customError) throws CommandException {
        CommandExceptionError showError = customError == null ? getError() : customError;
        try {
            Constructor constructor = getExceptionToThrow().getDeclaredConstructor(new Class[]{Integer.class, CommandExceptionError.class});
            Object[] arguments = new Object[] { getHttpStatus(), showError };
            return (CommandException)constructor.newInstance(arguments);
        } catch (Exception err) {
            return new CommandException("Programming error - unable to throw exception for "+getHttpStatus()+"/"+customError, err);
        }
    }

}

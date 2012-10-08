package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
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

    private int httpStatus;

    private CommandExceptionError error;

    private Class<? extends CommandException> exceptionToThrow;

    private HttpStatusToExceptionMapper(int httpStatus, CommandExceptionError error, Class<? extends CommandException> exceptionToThrow) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.exceptionToThrow = exceptionToThrow;
    }

    public static void throwException(int httpStatus, CommandExceptionError customError) throws CommandException {
        throw getException(httpStatus, customError);
    }

    public static void throwException(int httpStatus) throws CommandException {
        throw getException(httpStatus, null);
    }

    public static CommandException getException(int httpStatus, CommandExceptionError customError) {
        for (HttpStatusToExceptionMapper mapper : values()) {
            if (mapper.httpStatus == httpStatus) {
                return mapper.getException(customError);
            }
        }
        return new CommandException(httpStatus, CommandExceptionError.UNKNOWN);
    }

    public CommandException getException(CommandExceptionError customError) throws CommandException {
        CommandExceptionError showError = customError == null ? error : customError;
        try {
            Constructor constructor = exceptionToThrow.getDeclaredConstructor(new Class[]{Integer.class, CommandExceptionError.class});
            Object[] arguments = new Object[] { httpStatus, showError };
            return (CommandException)constructor.newInstance(arguments);
        } catch (Exception err) {
            return err instanceof CommandException ?
                    (CommandException)err :
                    new CommandException("Programming error - unable to throw exception for "+httpStatus+"/"+customError, err);
        }
    }

}

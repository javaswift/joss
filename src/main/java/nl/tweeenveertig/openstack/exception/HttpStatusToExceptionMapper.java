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

    private Class exceptionToThrow;

    private HttpStatusToExceptionMapper(int httpStatus, CommandExceptionError error, Class exceptionToThrow) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.exceptionToThrow = exceptionToThrow;
    }

    public static void throwException(int httpStatus) throws CommandException {
        for (HttpStatusToExceptionMapper mapper : values()) {
            if (mapper.httpStatus == httpStatus) {
                mapper.throwExceptionForMapper();
            }
        }
        throw new CommandException(httpStatus, CommandExceptionError.UNKNOWN);
    }

    public void throwExceptionForMapper() throws CommandException {
        if (exceptionToThrow == null) {
            throw new CommandException(httpStatus, error);
        } else {
            try {
                Constructor constructor = exceptionToThrow.getDeclaredConstructor(new Class[]{Integer.class, CommandExceptionError.class});
                Object[] arguments = new Object[] { httpStatus, error };
                throw (CommandException)constructor.newInstance(arguments);
            } catch (Exception err) {
                throw err instanceof CommandException ?
                        (CommandException)err :
                        new CommandException("Programming error - unable to throw exception for "+httpStatus+"/"+error.toString(), err);
            }
        }
    }

}

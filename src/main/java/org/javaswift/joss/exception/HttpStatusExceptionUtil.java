package org.javaswift.joss.exception;

public class HttpStatusExceptionUtil {

    public static void throwException(int httpStatus, CommandExceptionError customError) throws CommandException {
        throw getException(httpStatus, customError);
    }

    public static void throwException(int httpStatus) throws CommandException {
        throw getException(httpStatus, null);
    }

    public static CommandException getException(int httpStatus, CommandExceptionError customError) {
        for (HttpStatusToExceptionMapper mapper : HttpStatusToExceptionMapper.values()) {
            if (mapper.getHttpStatus() == httpStatus) {
                return mapper.getException(customError);
            }
        }
        return new CommandException(httpStatus, CommandExceptionError.UNKNOWN);
    }

}

package org.javaswift.joss.headers.account;

import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.DateParseException;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.DateHeader;

import java.util.Date;

public class ServerDate extends DateHeader {

    public static String DATE = "Date";

    public ServerDate(Long milliseconds) {
        super(milliseconds);
    }

    public ServerDate(String date) throws DateParseException {
        super(date);
    }

    public ServerDate(Date date) {
        super(date);
    }

    @Override
    public String getHeaderValue() {
        return Long.toString(getDate().getTime());
    }

    @Override
    public String getHeaderName() {
        return DATE;
    }

    public static ServerDate fromResponse(HttpResponse response) {
        // @TODO RB - I don't like this code. This must be refactored into something cleaner
        String serverDate = convertResponseHeader(response, DATE);
        try {
            return new ServerDate(serverDate);
        } catch (DateParseException err) {
            throw new CommandException(err.getMessage());
        }
    }

}

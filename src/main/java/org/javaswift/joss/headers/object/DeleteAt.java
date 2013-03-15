package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.DateHeader;
import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.DateParseException;

import java.util.Date;

public class DeleteAt extends DateHeader {

    public static String X_DELETE_AT = "X-Delete-At";

    public DeleteAt(Long milliseconds) {
        super(milliseconds);
    }

    public DeleteAt(String date) throws DateParseException {
        super(date);
    }

    public DeleteAt(Date date) {
        super(date);
    }

    @Override
    public String getHeaderValue() {
        return Long.toString(getDate().getTime() / 1000);
    }

    @Override
    public String getHeaderName() {
        return X_DELETE_AT;
    }

    public static DeleteAt fromResponse(HttpResponse response) {
        // @TODO RB - I don't like this code. This must be refactored into something cleaner
        String deleteAtString = convertResponseHeader(response, X_DELETE_AT);
        if (deleteAtString == null) {
            return null;
        }
        return new DeleteAt(Long.parseLong(deleteAtString)*1000);
    }
}

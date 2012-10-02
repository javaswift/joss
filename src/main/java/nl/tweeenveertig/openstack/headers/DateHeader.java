package nl.tweeenveertig.openstack.headers;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import java.util.Date;

public abstract class DateHeader extends Header {

    private Date date;

    public DateHeader(String date) throws DateParseException {
        this(convertStringToDate(date));
    }

    public DateHeader(Date date) {
        this.date = date;
    }

    public static Date convertStringToDate(String sinceDate) throws DateParseException {
        return DateUtils.parseDate(sinceDate);
    }

    public static String convertDateToString(Date date) {
        return DateUtils.formatDate(date);
    }

    public Date getDate() {
        return this.date;
    }

}

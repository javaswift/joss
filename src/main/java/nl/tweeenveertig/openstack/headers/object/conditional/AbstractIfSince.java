package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.headers.Header;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import java.util.Date;

public abstract class AbstractIfSince extends Header {

    private Date sinceDate;

    public AbstractIfSince(String sinceDate) throws DateParseException {
        this(convertStringToDate(sinceDate));
    }

    public AbstractIfSince(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public static Date convertStringToDate(String sinceDate) throws DateParseException {
        return DateUtils.parseDate(sinceDate);
    }

    public static String convertDateToString(Date date) {
        return DateUtils.formatDate(date);
    }

    public Date getSinceDate() {
        return this.sinceDate;
    }

    @Override
    public String getHeaderValue() {
        return convertDateToString(getSinceDate());
    }

}

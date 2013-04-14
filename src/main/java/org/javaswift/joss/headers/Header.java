package org.javaswift.joss.headers;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic header that needs to be added to a command. The underlying implementation takes care of the
 * specifics for a header.
 */
public abstract class Header {

    /**
    * Called before executing the request by the AbstractCommand. Makes sure the header is set on
    * the request object
    * @param request the request to which the header must be applied
    */
    public void setHeader(HttpRequestBase request) {
        request.setHeader(getHeaderName(), getHeaderValue());
    }

    /**
    * Returns the value that must be set in the request. Eg, "bytes: 4-12"
    * @return the value for the request header
    */
    public abstract String getHeaderValue();

    /**
    * Returns the header name that must be set in the request. Eg, "Range"
    * @return the name for the request header
    */
    public abstract String getHeaderName();

    public static List<org.apache.http.Header> getResponseHeadersStartingWith(HttpResponse response, String prefix) {
        List<org.apache.http.Header> headers = new ArrayList<org.apache.http.Header>();
        for (org.apache.http.Header header : response.getAllHeaders()) {
            if (header.getName().startsWith(prefix)) {
                headers.add(header);
            }
        }
        return headers;
    }

    public static String convertResponseHeader(HttpResponse response, String name) {
        return response.getHeaders(name) == null ? null : response.getHeaders(name).length == 0 ? null : response.getHeaders(name)[0].getValue();
    }

    public static boolean headerNotEmpty(HttpResponse response, String headerName) {
        return
                response.getHeaders(headerName) != null &&
                response.getHeaders(headerName).length > 0 &&
                response.getHeaders(headerName)[0].getValue().length() > 0;
    }

}

package nl.tweeenveertig.openstack.headers;

import org.apache.http.client.methods.HttpRequestBase;

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
    public void addHeader(HttpRequestBase request) {
        request.addHeader(getHeaderName(), getHeaderValue());
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

}

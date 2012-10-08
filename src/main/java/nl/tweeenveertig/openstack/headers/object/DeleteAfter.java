package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.Header;

public class DeleteAfter extends Header {

    public static String X_DELETE_AFTER = "X-Delete-After";

    private Long expireAfter;

    public DeleteAfter(Long expireAfter) {
        this.expireAfter = expireAfter;
    }

    @Override
    public String getHeaderValue() {
        return Long.toString(expireAfter);
    }

    @Override
    public String getHeaderName() {
        return X_DELETE_AFTER;
    }
}

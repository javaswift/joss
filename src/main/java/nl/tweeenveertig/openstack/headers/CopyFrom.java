package nl.tweeenveertig.openstack.headers;

/**
 * Determines the source object in a copy action
 */
public class CopyFrom extends Header {

    public static final String X_COPY_FROM      = "X-Copy-From";

    private String copyFrom;

    public CopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    @Override
    public String getHeaderValue() {
        return copyFrom;
    }

    @Override
    public String getHeaderName() {
        return X_COPY_FROM;
    }
}

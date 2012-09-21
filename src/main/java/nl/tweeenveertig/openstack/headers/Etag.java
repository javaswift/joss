package nl.tweeenveertig.openstack.headers;

public class Etag extends Header {

    public static final String ETAG = "ETag";

    private String md5;

    public Etag(String md5) {
        this.md5 = md5;
    }

    @Override
    public String getHeaderValue() {
        return this.md5;
    }

    @Override
    public String getHeaderName() {
        return ETAG;
    }
}

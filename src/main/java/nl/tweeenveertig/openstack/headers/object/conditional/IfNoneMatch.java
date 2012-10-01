package nl.tweeenveertig.openstack.headers.object.conditional;

public class IfNoneMatch extends AbstractIfMatch {

    public static final String IF_NONE_MATCH = "If-None-Match";

    public IfNoneMatch(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return IF_NONE_MATCH;
    }
}

package nl.tweeenveertig.openstack.headers.object.conditional;

public class IfMatch extends AbstractIfMatch {

    public static final String IF_MATCH = "If-Match";

    public IfMatch(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return IF_MATCH;
    }
}

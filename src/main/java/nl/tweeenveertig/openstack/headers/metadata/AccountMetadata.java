package nl.tweeenveertig.openstack.headers.metadata;

public class AccountMetadata extends Metadata {

    public static final String X_ACCOUNT_META_PREFIX = "X-Account-Meta-";

    public AccountMetadata(String name, String value) {
        super(name, value);
    }

    @Override
    public String getHeaderName() {
        return X_ACCOUNT_META_PREFIX + getName();
    }
}

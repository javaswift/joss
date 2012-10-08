package nl.tweeenveertig.openstack.command.core.httpstatus;

public interface HttpStatusMatcher {

    public boolean matches(int statusCode);
}

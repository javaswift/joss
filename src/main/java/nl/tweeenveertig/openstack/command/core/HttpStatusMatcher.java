package nl.tweeenveertig.openstack.command.core;

public interface HttpStatusMatcher {

    public boolean matches(int statusCode);
}

package nl.t42.openstack.command.core;

public interface HttpStatusMatcher {

    public boolean matches(int statusCode);
}

package nl.tweeenveertig.openstack.model;

public interface Access {

    public void setPreferredRegion(String preferredRegion);

    public String getToken();

    public String getInternalURL();

    public String getPublicURL();

}

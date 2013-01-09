package nl.tweeenveertig.openstack.model;

public interface PaginationMap {

    /**
    * Gets the marker text (ie, the name of the last record of the previous page) for navigating
    * to another page. The OpenStack API requires this information (why don't they use a regular
    * offset?).
    * @param offset the page to get the appropriate marker for
    * @return the marker for the required page
    */
    public String getMarker(int offset);

}

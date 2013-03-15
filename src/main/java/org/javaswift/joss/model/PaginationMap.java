package org.javaswift.joss.model;

/**
* Map for a ListHolder on a list of ListSubject. The map holds a mapping for a page number to a marker,
* which is used by the interface of the OpenStack API for pagination purposes. The PaginationMap is drawn
* up in one go, which -- regrettably -- is a necessity because of the marker-mechanism. When the underlying
* data changes and this needs to be reflected by the client, a new map must be generated.
*/
public interface PaginationMap {

    /**
    * Gets the marker text (ie, the name of the last record of the previous page) for navigating
    * to another page. The OpenStack API requires this information (why don't they use a regular
    * offset?).
    * @param page the page to get the appropriate marker for
    * @return the marker for the required page
    */
    public String getMarker(Integer page);

    /**
    * Returns the number of pages
    * @return number of pages
    */
    public int getNumberOfPages();

    /**
    * Returns the page size
    * @return page size
    */
    public int getPageSize();

    /**
    * Returns the number of records
    * @return the number of records
    */
    public int getNumberOfRecords();

    /**
    * Returns the prefix which is used to search the records
    * @return the prefix
    */
    public String getPrefix();

}

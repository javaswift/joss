package org.javaswift.joss.model;

/**
* ListSubject instances can be part of a list by a parent entity. In the current model, only
* Container and StoredObject are ListSubjects.
*/
public interface ListSubject {

    public String getName();

    /**
    * Declares to the ListSubject that a number of fields have been read from the list operation
    * and therefore do not have to be fetched individually, unless caching is turned off.
    */
    public void metadataSetFromHeaders();

}

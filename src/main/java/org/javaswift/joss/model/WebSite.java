package org.javaswift.joss.model;

import java.io.File;

/**
* A Website is a specialization of Container. The content in this Container acts as a static
* website. The objects within are set up using pseudo-hierarchies, meaning that object names
* include the full path after Container.
* @author Robert Bor
*/
public interface Website extends Container {

    /**
    * Gets the index page of the website. This is the page that is shown by default
    * @return index page of the website
    */
    public String getIndexPage();

    /**
    * Gets the error page of the website. This is the page that is shown when an error occurs
    * @return error page of the website
    */
    public String getErrorPage();

    /**
    * Sets the index page of the website. This is the page that is shown by default
    * @param indexPage index page of the website
    */
    public void setIndexPage(String indexPage);

    /**
    * Gets the error page of the website. This is the page that is shown when an error occurs
    * @param errorPage error page of the website
    */
    public void setErrorPage(String errorPage);

    /**
    * Compares a local directory to the contents of the Container on the basis of MD5 hashes (ie, Etag).
    * It makes a couple of potentially far-reaching decision, so be careful:
    * <ul>
    *     <li>Content exists locally, not in Container; copy to Container</li>
    *     <li>Content does not exist locally, exists in Container; delete from Container</li>
    *     <li>Content exists in both places, but is different; overwrite file in Container</li>
    * </ul>
    * @param directory the directory to check against the Container's content
    */
    public void pushDirectory(File directory);

    /**
    * Compares a local directory to the contents of the Container on the basis of MD5 hashes (ie, Etag).
    * It makes a couple of potentially far-reaching decision, so be careful:
    * <ul>
    *     <li>Content exists in Container, not locally; download locally</li>
    *     <li>Content does not exist in Container, exists locally; delete locally</li>
    *     <li>Content exists in both places, but is different; overwrite local file</li>
    * </ul>
    * @param directory the directory to check against the Container's content
    */
    public void pullDirectory(File directory);

}

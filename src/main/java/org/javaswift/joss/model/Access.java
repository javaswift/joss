package org.javaswift.joss.model;

/**
* Interface that determines the kind of access that was granted to the ObjectStore. It holds
* the token, which must be passed to all secure calls. Also, you can get the internal and
* public URLs for accessing content. If you have preference for a region, this is the place
* to make your preference known. If a suitable region was found, the URLs will show.
*/
public interface Access {

    /**
    * The ObjectStore holds multiple regions. If you state a preference, it will choose that region
    * (if available). You will see the result in getting the internal or public URL.
    * @param preferredRegion the region where you want to access the resources
    */
    public void setPreferredRegion(String preferredRegion);

    /**
    * The security token to pass to all secure ObjectStore calls. Note that this token has a
    * validity for a period of 24 hours. After that, you require a new token.
    * @return the token
    */
    public String getToken();

    /**
    * The internal URL to access resources with. This works on both public and private containers.
    * @return internal URL
    */
    public String getInternalURL();

    /**
    * The public URL of a resource. This is the URL you might want to display to the outside world,
    * expect that you may replace the domain with your own.
    * @return public URL
    */
    public String getPublicURL();

    /**
    * Ascertains whether tenant ID and/or name were supplied.
    * @return true if tenant ID and/or name were supplied before the authentication call
    */
    public boolean isTenantSupplied();

}

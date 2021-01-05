package org.javaswift.joss.client.website;

import java.io.File;

import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.headers.website.ErrorPage;
import org.javaswift.joss.headers.website.IndexPage;
import org.javaswift.joss.headers.website.Listing;
import org.javaswift.joss.headers.website.ListingCSS;
import org.javaswift.joss.headers.website.WebsiteHeader;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.model.Website;

public abstract class AbstractWebsite extends AbstractContainer implements Website {

    private String[] ignoreFilters = new String[] {};

    public AbstractWebsite(Account account, String name, boolean allowCaching) {
        super(account, name, allowCaching);
    }

    @Override
    public Website setIgnoreFilters(String[] ignoreFilters) {
        this.ignoreFilters = ignoreFilters;
        return this;
    }

    @Override
    /**
    * Websites are made public by default. If this is not desirable, the Website can be made private again
    */
    public Website create() {
        super.create();
        makePublic();
        return this;
    }

    protected Object getHeader(String metadataHeaderName) {
        return getMetadata(metadataHeaderName);
    }

    protected StoredObject getStoredObject(String metadataHeaderName) {
        Object value = getHeader(metadataHeaderName);
        return value != null ? getObject(value.toString()) : null;
    }

    protected void setHeader(WebsiteHeader header) {
        setAndSaveMetadata(header.getMetadataHeader(), header.getHeaderValue());
    }

    public String getMetadataHeader(String websiteHeader) {
        return WebsiteHeader.STATIC_WEBSITE_HEADER + websiteHeader;
    }
    
    @Override
    public StoredObject getIndexPage() {
        return getStoredObject(getMetadataHeader(IndexPage.INDEX));
    }

    @Override
    public StoredObject getErrorPage() {
        return getStoredObject(getMetadataHeader(ErrorPage.ERROR));
    }

    @Override
    public StoredObject getListingCSS() {
        return getStoredObject(getMetadataHeader(ListingCSS.LISTINGS_CSS));
    }

    @Override
    public boolean isListing() {
        Object value = getHeader(getMetadataHeader(Listing.LISTINGS));
        return value != null && Boolean.parseBoolean(value.toString());
    }

    @Override
    public void setIndexPage(StoredObject indexPage) {
        setHeader(new IndexPage(indexPage.getName()));
    }

    @Override
    public void setErrorPage(StoredObject errorPage) {
        setHeader(new ErrorPage(errorPage.getName()));
    }

    @Override
    public void setListingCSS(StoredObject listingCSS) {
        setHeader(new ListingCSS(listingCSS.getName()));
    }

    @Override
    public void setListing(boolean listing) {
        setHeader(new Listing(listing));
    }

    @Override
    public void pushDirectory(File directory) {
        syncDirectory(
                new LocalFileObjects(directory, ignoreFilters),    // source
                new ObjectStoreFileObjects(this));  // target
    }

    @Override
    public void pullDirectory(File directory) {
        syncDirectory(
                new ObjectStoreFileObjects(this),   // target
                new LocalFileObjects(directory, ignoreFilters ));   // source
    }

    protected void syncDirectory(FileObjects source, FileObjects target) {
        // Copy all new & changed files from the source to the target directory
        saveObjects(source, target);
        // Delete all files in the target directory that do not exist in the source directory
        deleteObjects(source, target);
        // If target is folder-based, make sure the empty folders are removed
        target.cleanup();
    }

    private void deleteObjects(FileObjects source, FileObjects target) {
        for (String targetPath : target.keys()) {
            if (target.ignore(targetPath)) {
                continue;
            }
            FileObject targetObject = target.get(targetPath);
            if (source.get(targetPath) == null) {
                targetObject.delete();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void saveObjects(FileObjects source, FileObjects target) {
        for (String sourcePath : source.keys()) {
            FileObject sourceObject = source.get(sourcePath);
            FileObject targetObject = target.get(sourcePath);
            if (targetObject == null) {
                targetObject = target.create(sourcePath);
            } else if (sourceObject.getMd5().equals(targetObject.getMd5())) {
                continue; // objects are equal, no action required
            }
            targetObject.save(sourceObject);
        }
    }

    @Override
    public String getHost() {
        String strippedHost = this.getAccount().getOriginalHost().replaceAll("http://", "").replaceAll("https://", "");
        return getName() + "." + strippedHost;
    }

}

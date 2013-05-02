package org.javaswift.joss.client.website;

import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.headers.website.*;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.model.Website;

import java.io.File;
import java.util.Map;

public abstract class AbstractWebsite extends AbstractContainer implements Website {

    public AbstractWebsite(Account account, String name, boolean allowCaching) {
        super(account, name, allowCaching);
    }

    protected Object getHeader(String metadataHeaderName) {
        Map<String, Object> metadata = getMetadata();
        return metadata.get(metadataHeaderName);
    }

    protected StoredObject getStoredObject(String metadataHeaderName) {
        Object value = getHeader(metadataHeaderName);
        return value != null ? getObject(value.toString()) : null;
    }

    protected void setHeader(WebsiteHeader header) {
        Map<String, Object> metadata = getMetadata();
        metadata.put(header.getMetadataHeader(), header.getHeaderValue());
        setMetadata(metadata);
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
                new LocalFileObjects(directory),    // source
                new ObjectStoreFileObjects(this));  // target
    }

    @Override
    public void pullDirectory(File directory) {
        syncDirectory(
                new ObjectStoreFileObjects(this),   // target
                new LocalFileObjects(directory));   // source
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

}

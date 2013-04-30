package org.javaswift.joss.client.core;

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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pullDirectory(File directory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

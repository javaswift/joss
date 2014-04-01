package org.javaswift.joss.client.mock;

import org.apache.commons.io.FileUtils;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.model.Website;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.util.FileAction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.*;

public class WebsiteMockTest {

    private Website website;

    File writeDir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setup() throws IOException, URISyntaxException {
        this.website = new WebsiteMock(new AccountMock(), "website");
        this.website.create();
        this.writeDir = new File(FileAction.getFile("websites").getPath().replaceAll(File.separator + "websites", File.separator + "temp"));
        writeDir.mkdir();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(writeDir);
    }

    @Test
    public void create() {
        Account account = new AccountMock();
        Website website = account.getWebsite("website");
        website.create();
        assertTrue(website.isPublic());
    }

    @Test
    public void getObject() {
        Account account = new AccountMock();
        Website website = account.getWebsite("website");
        StoredObject object = website.getObject("index.html");
        assertTrue(object instanceof StoredObjectMock);
    }

    @Test
    public void setIndexPage() {
        website.setIndexPage(website.getObject("index.html"));
        assertEquals("index.html", website.getIndexPage().getName());
    }

    @Test
    public void indexPageNotSet() {
        assertEquals(null, website.getIndexPage());
    }

    @Test
    public void setErrorPage() {
        website.setErrorPage(website.getObject("error.html"));
        assertEquals("error.html", website.getErrorPage().getName());
    }

    @Test
    public void setListingsCSS() {
        website.setListingCSS(website.getObject("styles/listings.css"));
        assertEquals("styles/listings.css", website.getListingCSS().getName());
    }

    @Test
    public void setListingTrue() {
        website.setListing(true);
        assertTrue(website.isListing());
    }

    @Test
    public void setListingFalse() {
        website.setListing(false);
        assertFalse(website.isListing());
    }

    @Test
    public void listingNotSet() {
        assertFalse(website.isListing());
    }

    @Test
    public void pushToEmptyWebsite() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("websites", false);
        website = new WebsiteMock(new AccountMock(swift), "website");
        website.pushDirectory(FileAction.getFile("object-store/container1"));
        assertEquals(2, website.list().size());
    }

    @Test
    public void pushTwiceToWebsite() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("websites", false);
        website = new WebsiteMock(new AccountMock(swift), "website");
        website.pushDirectory(FileAction.getFile("object-store/container1"));
        String lastModified1 = website.getObject("checkmark.png").getLastModified();
        website.pushDirectory(FileAction.getFile("object-store/container1"));
        String lastModified2 = website.getObject("checkmark.png").getLastModified();
        assertEquals(lastModified1, lastModified2);
        assertEquals(2, website.list().size());
    }

    @Test
    public void pushAndUpdate() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("websites", false);
        website = new WebsiteMock(new AccountMock(swift), "website");
        website.pushDirectory(FileAction.getFile("websites/website"));
        assertEquals(7, website.list().size());
        website.pushDirectory(FileAction.getFile("websites/website2"));
        assertEquals(6, website.list().size());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void pullWebsite() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("websites", false);
        website = new WebsiteMock(new AccountMock(swift), "website");
        website.pullDirectory(this.writeDir);
        assertEquals(5, writeDir.listFiles().length);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void pullDifferentWebsites() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("object-store", false);
        website = new WebsiteMock(new AccountMock(swift), "container1");
        website.pullDirectory(this.writeDir);
        assertEquals(2, writeDir.listFiles().length); // 2 files
        website = new WebsiteMock(new AccountMock(swift), "container2");
        website.pullDirectory(this.writeDir);
        assertEquals(5, writeDir.listFiles().length); // 5 files
    }

    @Test
    public void deleteEmptyFolders() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("websites", false);
        website = new WebsiteMock(new AccountMock(swift), "website");
        website.pullDirectory(this.writeDir);
        assertTrue(new File(writeDir.getPath() + "/script/modb").exists());
        website = new WebsiteMock(new AccountMock(swift), "website2");
        website.pullDirectory(this.writeDir);
        assertFalse(new File(writeDir.getPath()+"/script/modb").exists());
    }

    @Test
    public void ignoreFoldersOnPushing() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("websites", false);
        website = new WebsiteMock(new AccountMock(swift), "website")
                .setIgnoreFilters(new String[] {"script"} );
        website.pushDirectory(FileAction.getFile("websites/website"));
        assertEquals(4, website.list().size());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void ignoreFileOnPulling() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("object-store", false);
        website = new WebsiteMock(new AccountMock(swift), "container1");
        website.pullDirectory(this.writeDir);
        assertEquals(2, writeDir.listFiles().length); // 2 files
        website = new WebsiteMock(new AccountMock(swift), "container2")
                .setIgnoreFilters(new String[] {"checkmark.png"});
        website.pullDirectory(this.writeDir);
        assertEquals(6, writeDir.listFiles().length); // 6 files, checkmark.png has not been removed
    }


    @Test
    public void getHost() {
        Swift swift = new Swift()
                .setPublicHost("configured.public.url");
        website = new WebsiteMock(new AccountMock(swift), "my");
        assertEquals("my.configured.public.url", website.getHost());
    }

}

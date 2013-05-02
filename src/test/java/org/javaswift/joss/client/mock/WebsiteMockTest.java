package org.javaswift.joss.client.mock;

import org.javaswift.joss.model.Website;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.util.FileAction;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.*;

public class WebsiteMockTest {

    private Website website;

    @Before
    public void setup() {
        this.website = new WebsiteMock(new AccountMock(), "website");
        this.website.create();
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
                .setOnFileObjectStore("websites");
        website = new WebsiteMock(new AccountMock(swift), "website");
        website.pushDirectory(FileAction.getFile("object-store/container1"));
        assertEquals(2, website.list().size());
    }

    @Test
    public void pushTwiceToWebsite() throws IOException, URISyntaxException {
        Swift swift = new Swift()
                .setOnFileObjectStore("websites");
        website = new WebsiteMock(new AccountMock(swift), "website");
        website.pushDirectory(FileAction.getFile("object-store/container1"));
        website.pushDirectory(FileAction.getFile("object-store/container1"));
        assertEquals(2, website.list().size());
    }

//    @Test
//    public void listDirectory() throws IOException, URISyntaxException {
//        Swift swift = new Swift()
//                .setOnFileObjectStore("websites");
//        website = new WebsiteMock(new AccountMock(swift), "website");
//
//        website.pushDirectory(new File("/workspace/xxxxxx/site"));
//
//        for (StoredObject object : website.list()) {
//            System.out.println(object.getName()+" -> "+object.getEtag());
//        }
//
////            List<FileReference> files = FileAction.listFiles(url);
////            for (FileReference file : files) {
////                System.out.println(file.getPath());
////            }
//
//    }
}

package org.javaswift.joss.client.mock;

import org.javaswift.joss.model.Website;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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

}

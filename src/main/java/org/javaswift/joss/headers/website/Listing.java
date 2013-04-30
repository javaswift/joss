package org.javaswift.joss.headers.website;

public class Listing extends WebsiteHeader {

    public static final String LISTINGS = "Listing";

    public Listing(boolean value) {
        super(LISTINGS, Boolean.toString(value));
    }

}

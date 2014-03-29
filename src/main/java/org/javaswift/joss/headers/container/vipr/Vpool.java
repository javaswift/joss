package org.javaswift.joss.headers.container.vipr;

import org.javaswift.joss.headers.SimpleHeader;

/**
* This is a header specific to EMC ViPR that can be passed with a create container
* request.  It allows the container to be associated with a specific Object Virtual Pool.
*/
public class Vpool extends SimpleHeader {
    public static final String VPOOL = "x-emc-vpool";

    /**
    * Sets the Object Virtual Pool ID for the create container request.
    * @param value The Object Virtual Pool ID.  Note that this is the URN, not the name.
    */
    public Vpool(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return VPOOL;
    }

}

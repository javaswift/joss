package org.javaswift.joss.headers.container.vipr;

import org.javaswift.joss.headers.SimpleHeader;

/**
 * This is a header specific to EMC ViPR that can be passed with a create container 
 * request.  It allows the container to be associated with a specific project.
 */
public class ProjectIdHeader extends SimpleHeader {
    public static final String PROJECT_ID = "x-emc-project-id";
    

    /**
     * Sets the Project ID to be associated with the container.
     * @param value the project ID.  Note that this is the URN, not the name.
     */
    public ProjectIdHeader(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return PROJECT_ID;
    }

}

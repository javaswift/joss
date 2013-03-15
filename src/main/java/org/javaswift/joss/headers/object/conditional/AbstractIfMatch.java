package org.javaswift.joss.headers.object.conditional;

import org.javaswift.joss.headers.SimpleHeader;

public abstract class AbstractIfMatch extends SimpleHeader {

    public AbstractIfMatch(String value) {
        super(value);
    }

    /**
    * Makes a check against the matchValue and throws an exception (with the proper HTTP status code) if the value
    * means no content should be returned.
    * @param matchValue the value to match against
    */
    public abstract void matchAgainst(String matchValue);
}

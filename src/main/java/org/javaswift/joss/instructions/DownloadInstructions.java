package org.javaswift.joss.instructions;

import org.javaswift.joss.headers.object.conditional.AbstractIfMatch;
import org.javaswift.joss.headers.object.conditional.AbstractIfSince;
import org.javaswift.joss.headers.object.range.AbstractRange;

/**
 * Specific instructions for downloading content, these could be:
 * <ul>
 *     <li>Range; a partial download of the entire content, check {@link AbstractRange}</li>
 *     <li>If-Match; only return the content conditional the passed value matches the etag. If it does not match, throw
 *             a CommandException with HttpStatus."412 Precondition Failed"</li>
 *     <li>If-None-Match; only return the content conditional the passed value does NOT match the etag. If it matches,
 *             throw a CommandException with "304 Not Modified"</li>
 *     <li>If-Modified-Since; only return the content when it has been modified after the date; else, throw a
 *             CommandException with "304 Not Modified"</li>
 *     <li>If-Unmodified-Since; only return the content if it has been unmodified after the date; else, throw a
 *             CommandException with "412 Precondition Failed"</li>
 * </ul>
 */
public class DownloadInstructions {

    /** Determines what part of the object must be downloaded */
    private AbstractRange range;

    /** Determines under what conditions the object must be returned -- compares to etag */
    private AbstractIfMatch ifMatch;

    /** Determines under what conditions the object must be returned -- compares to last modification date */
    private AbstractIfSince ifSince;

    /**
    * Return the part of the content designated by the {@link AbstractRange} class.
    * @param range the range of the content to download
    * @return the download instructions, ready for more settings
    */
    public DownloadInstructions setRange(AbstractRange range) {
        this.range = range;
        return this;
    }

    public AbstractRange getRange() {
        return this.range;
    }

    /**
    * Return the content under a specific condition, which can be either:
    * <ul>
    *     <li>{@link org.javaswift.joss.headers.object.conditional.IfMatch If-Match}; if the etag matches
    *         the supplied value, return the content. If not, throw a {@link org.javaswift.joss.exception.CommandException}
    *         with status "412 Precondition Failed"</li>
    *     <li>{@link org.javaswift.joss.headers.object.conditional.IfNoneMatch If-None-Match}; if the etag
    *         does NOT match, return the content. If it matches, throw a {@link org.javaswift.joss.exception.CommandException}
    *         with status "304 Not Modified"</li>
    * </ul>
    * @param ifMatch value to match against the etag value
    * @return the download instructions, ready for more settings
    */
    public DownloadInstructions setMatchConditional(AbstractIfMatch ifMatch) {
        if (ifMatch.getHeaderValue() == null) {
            return this; // Ignore the setting if no etag has been supplied
        }
        this.ifMatch= ifMatch;
        return this;
    }

    public AbstractIfMatch getMatchConditional() {
        return this.ifMatch;
    }

    /**
    * Return the content under a specific condition, which can be either:
    * <ul>
    *     <li>{@link org.javaswift.joss.headers.object.conditional.IfUnmodifiedSince If-Unmodified-Since}; if the
    *         content has been unmodified since the date, return the content. If not, throw a
    *         {@link org.javaswift.joss.exception.CommandException} with status "412 Precondition Failed"</li>
    *     <li>{@link org.javaswift.joss.headers.object.conditional.IfModifiedSince If-Modified-Since}; if the
    *         content has been modified since the date, return the content. If not, throw a
    *         {@link org.javaswift.joss.exception.CommandException} with status "304 Not Modified"</li>
    * </ul>
    * @param ifSince date to match the last modification date
    * @return the download instructions, ready for more settings
    */
    public DownloadInstructions setSinceConditional(AbstractIfSince ifSince) {
        if (ifSince.getSinceDate() == null) {
            return this; // Ignore the setting if no date has been supplied
        }
        this.ifSince = ifSince;
        return this;
    }

    public AbstractIfSince getSinceConditional() {
        return this.ifSince;
    }

}

package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.object.conditional.AbstractIfMatch;
import nl.tweeenveertig.openstack.headers.object.range.AbstractRange;

/**
 * Specific instructions for downloading content, these could be:
 * <ul>
 *     <li>Range; a partial download of the entire content, check {@link AbstractRange}</li>
 *     <li>If-Match; only return the content conditional the passed value matches the etag. If it does not match, throw
 *             a CommandException with HttpStatus."412 Precondition Failed"</li>
 *     <li>If-None-Match; only return the content conditional the passed value does NOT match the etag. If it matches,
 *             throw a CommandException with "304 Not Modified"</li>
 * </ul>
 */
public class DownloadInstructions {

    private AbstractRange range;

    private AbstractIfMatch ifMatch;

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
    *     <li>{@link nl.tweeenveertig.openstack.headers.object.conditional.IfMatch If-Match}; if the etag matches
    *         the supplied value, return the content. If not, throw a {@link nl.tweeenveertig.openstack.command.core.CommandException}
    *         with status "412 Precondition Failed"</li>
    *     <li>{@link nl.tweeenveertig.openstack.headers.object.conditional.IfNoneMatch If-None-Match}; if the etag
    *         does NOT match, return the content. If it matches, throw a {@link nl.tweeenveertig.openstack.command.core.CommandException}
    *         with status "304 Not Modified"</li>
    * </ul>
    * @param ifMatch value to match against the etag value; conditional it matches, the content is not returned
    * @return the download instructions, ready for more settings
    */
    public DownloadInstructions setMatchConditional(AbstractIfMatch ifMatch) {
        this.ifMatch= ifMatch;
        return this;
    }

    public AbstractIfMatch getMatchConditional() {
        return this.ifMatch;
    }

}

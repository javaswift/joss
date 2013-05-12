package org.javaswift.joss.model;

/**
 * The authenticate call creates a new Account object, which allows you to interact with the Object Store.
 * Note that security tokens last for a maximum of 24 hours, after which a new one must be fetched.
 * <ul>
 *     <li>Instantiate ClientImpl for the real client to the Object Store.</li>
 *     <li>Instantiate ClientMock for the client with mocked access, simulated by holding objects in-memory</li>
 * </ul>
 * Call {@link #authenticate() authenticate} to get an {@link Account} which allows
 * you to start operating on the Object Store.
 * @author Robert Bor
 */
public interface Client<A extends Account> {

    /**
    * Calls the identity module of the OpenStack implementation to authenticate itself against. If
    * successful, it returns the internal URL to work with and the authentication token, which will
    * automatically be added to all calls made by the client henceforth.
    */
    public A authenticate();

    /**
     * Calls the identity module of the OpenStack implementation to authenticate itself against. If
     * successful, it returns the internal URL to work with and the authentication token, which will
     * automatically be added to all calls made by the client henceforth.
     * @param preferredRegion the end point region that is preferred. Note that this end point is not guaranteed. If
     *                        it is not available, another end point will be returned.
     */
    public A authenticate(String preferredRegion);

}

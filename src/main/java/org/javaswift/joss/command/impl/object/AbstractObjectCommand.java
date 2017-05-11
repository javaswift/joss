package org.javaswift.joss.command.impl.object;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

public abstract class AbstractObjectCommand<M extends HttpRequestBase, N> extends AbstractSecureCommand<M, N> {

	private StoredObject object;
	
    public AbstractObjectCommand(Account account, HttpClient httpClient, Access access, StoredObject object) {
        super(account, httpClient, getURL(access, object), access.getToken());
        this.object = object;
    }
    
    public StoredObject getStoredObject() {
		return object;
	}
    
    public void populateResponseHeaders(HttpResponse response) {
		object.setResponseHeaders(response.getAllHeaders());
	}
    
    protected N getReturnObject(HttpResponse response) throws IOException {
		populateResponseHeaders(response);
        return null; // returns null by default
    }

}

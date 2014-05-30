package org.javaswift.joss.command.impl.identity;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Access;

public class ExternalAuthenticationCommandImpl extends AbstractSimpleAuthenticationCommandImpl {

	private final AuthenticationMethod.AccessProvider accessProvier ;
	
    public ExternalAuthenticationCommandImpl(HttpClient httpClient, String url, AuthenticationMethod.AccessProvider accessProvier) {
        super(httpClient, url);
        if (accessProvier == null)
        	throw new NullPointerException () ;
        this.accessProvier = accessProvier ;
    }
    
    @Override
    public Access call() {
    	return accessProvier.authenticate() ;
    }
}


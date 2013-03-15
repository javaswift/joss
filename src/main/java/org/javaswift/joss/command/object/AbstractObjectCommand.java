package org.javaswift.joss.command.object;

import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.core.AbstractSecureCommand;
import org.javaswift.joss.model.StoredObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractObjectCommand<M extends HttpRequestBase, N> extends AbstractSecureCommand<M, N> {

    public AbstractObjectCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object) {
        super(account, httpClient, access.getInternalURL() + getObjectPath(object), access.getToken());
    }

    protected static String getObjectPath(StoredObject object) {
        return "/" + object.getPath();
    }
}

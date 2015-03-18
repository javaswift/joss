package org.javaswift.joss.command.impl.object;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

public abstract class AbstractObjectCommand<M extends HttpRequestBase, N> extends AbstractSecureCommand<M, N> {

    public AbstractObjectCommand(Account account, HttpClient httpClient, Access access, StoredObject object) {
        super(account, httpClient, getURL(access, object, account.isUsePrivateURL()), access.getToken());
    }

}

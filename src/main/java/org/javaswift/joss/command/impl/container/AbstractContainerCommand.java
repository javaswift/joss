package org.javaswift.joss.command.impl.container;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

public abstract class AbstractContainerCommand<M extends HttpRequestBase, N> extends AbstractSecureCommand<M, N> {

    public AbstractContainerCommand(Account account, HttpClient httpClient, Access access, Container container) {
        super(account, httpClient, getURL(access, container, account.isUsePrivateURL()), access.getToken());
    }
}

package org.javaswift.joss.command.impl.account;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;

public abstract class AbstractAccountCommand<M extends HttpRequestBase, N> extends AbstractSecureCommand<M, N> {

    public AbstractAccountCommand(Account account, HttpClient httpClient, Access access) {
        super(account, httpClient, getURL(access, account, account.isUsePrivateURL()), access.getToken());
    }

}

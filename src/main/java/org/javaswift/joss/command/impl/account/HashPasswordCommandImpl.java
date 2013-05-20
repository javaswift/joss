package org.javaswift.joss.command.impl.account;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.account.HashPasswordCommand;
import org.javaswift.joss.headers.account.HashPassword;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;

public class HashPasswordCommandImpl extends AbstractAccountCommand<HttpPost, Object> implements HashPasswordCommand {

    public HashPasswordCommandImpl(Account account, HttpClient httpClient, Access access, String password) {
        super(account, httpClient, access);
        setHeader(new HashPassword(password));
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }

}

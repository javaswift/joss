package org.javaswift.joss.command.impl.account;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.account.AccountInformationCommand;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.headers.account.AccountBytesUsed;
import org.javaswift.joss.headers.account.AccountContainerCount;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.headers.account.AccountObjectCount;
import org.javaswift.joss.information.AccountInformation;
import org.javaswift.joss.model.Account;

import java.io.IOException;

public class AccountInformationCommandImpl extends AbstractAccountCommand<HttpHead, AccountInformation> implements AccountInformationCommand {

    public AccountInformationCommandImpl(Account account, HttpClient httpClient, AccessImpl access) {
        super(account, httpClient, access);
    }

    @Override
    protected AccountInformation getReturnObject(HttpResponse response) throws IOException {
        AccountInformation info = new AccountInformation();
        info.setMetadata(AccountMetadata.fromResponse(response));
        info.setContainerCount(AccountContainerCount.fromResponse(response));
        info.setObjectCount(AccountObjectCount.fromResponse(response));
        info.setBytesUsed(AccountBytesUsed.fromResponse(response));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }

}

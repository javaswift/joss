package org.javaswift.joss.command.account;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.core.*;
import org.javaswift.joss.command.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.headers.account.AccountBytesUsed;
import org.javaswift.joss.headers.account.AccountContainerCount;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.headers.account.AccountObjectCount;
import org.javaswift.joss.information.AccountInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class AccountInformationCommand extends AbstractSecureCommand<HttpHead, AccountInformation> {

    public AccountInformationCommand(Account account, HttpClient httpClient, AccessImpl access) {
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
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}

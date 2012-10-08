package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.*;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.headers.account.AccountBytesUsed;
import nl.tweeenveertig.openstack.headers.account.AccountContainerCount;
import nl.tweeenveertig.openstack.headers.account.AccountMetadata;
import nl.tweeenveertig.openstack.headers.account.AccountObjectCount;
import nl.tweeenveertig.openstack.model.AccountInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class AccountInformationCommand extends AbstractSecureCommand<HttpHead, AccountInformation> {

    public AccountInformationCommand(Account account, HttpClient httpClient, Access access) {
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
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT), null)
        };
    }

}

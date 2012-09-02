package nl.t42.openstack.command.account;

import nl.t42.openstack.command.core.AbstractSecureCommand;
import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.AccountInformation;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static nl.t42.openstack.command.core.CommandUtil.convertResponseToString;

public class AccountInformationCommand extends AbstractSecureCommand<HttpHead, AccountInformation> {

    public static final String X_ACCOUNT_CONTAINER_COUNT  = "X-Account-Container-Count";
    public static final String X_ACCOUNT_OBJECT_COUNT     = "X-Account-Object-Count";
    public static final String X_ACCOUNT_BYTES_USED       = "X-Account-Bytes-Used";

    public AccountInformationCommand(HttpClient httpClient, Access access) {
        super(httpClient, access);
        request.addHeader("Content-type", "application/json");
    }

    @Override
    protected AccountInformation getReturnObject(HttpResponse response) throws IOException {
        AccountInformation info = new AccountInformation();
        info.setContainerCount(Integer.parseInt(response.getHeaders(X_ACCOUNT_CONTAINER_COUNT)[0].getValue()));
        info.setObjectCount(Integer.parseInt(response.getHeaders(X_ACCOUNT_OBJECT_COUNT)[0].getValue()));
        info.setBytesUsed(Long.parseLong(response.getHeaders(X_ACCOUNT_BYTES_USED)[0].getValue()));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode == HttpStatus.SC_NO_CONTENT) {
            return;
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }
}

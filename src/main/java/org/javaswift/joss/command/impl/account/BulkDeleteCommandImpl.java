package org.javaswift.joss.command.impl.account;

import java.io.IOException;
import java.util.Collection;
import java.util.StringJoiner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.account.BulkDeleteCommand;
import org.javaswift.joss.command.shared.identity.bulkdelete.BulkDeleteResponse;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Accept;
import org.javaswift.joss.instructions.QueryParameter;
import org.javaswift.joss.instructions.QueryParameters;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.ObjectIdentifier;

public class BulkDeleteCommandImpl extends AbstractAccountCommand<HttpPost, BulkDeleteResponse>
    implements BulkDeleteCommand {

    private static final String BULK_DELETE_PARAMETER = "bulk-delete";

    public BulkDeleteCommandImpl(Account account, HttpClient httpClient, Access access, Collection<ObjectIdentifier> objectsToDelete) {
        super(account, httpClient, access);
        modifyURI(getBulkDeleteQueryParameter());
        setHeader(new Accept("application/json"));
        setBulkDeleteContent(objectsToDelete);
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))};
    }

    @Override
    protected BulkDeleteResponse getReturnObject(HttpResponse response) throws IOException {
        return createObjectMapper(false)
            .readValue(response.getEntity().getContent(), BulkDeleteResponse.class);
    }

    private QueryParameters getBulkDeleteQueryParameter() {
        return new QueryParameters(new QueryParameter[] {new QueryParameter(BULK_DELETE_PARAMETER, Boolean.TRUE
            .toString())});
    }

    private void setBulkDeleteContent(Collection<ObjectIdentifier> objectsToDelete) {
        try {
            StringJoiner joiner = new StringJoiner("\n");
            for (ObjectIdentifier object : objectsToDelete) {
                joiner.add(object.getUrlEncodedIdentifier());
            }
            StringEntity input = new StringEntity(joiner.toString());
            request.setEntity(input);
        } catch (IOException err) {
            throw new CommandException("Unable to set the text/plain body for the bulk-delete request", err);
        }
    }
}
